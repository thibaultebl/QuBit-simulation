package org.example.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class BlochServer {

    private static final int PORT = 8765;
    private static final int N_QUBITS = 2;

    private final Map<String, SimulationEngine> sims = new HashMap<>();

    public BlochServer() {
        // Two named simulations, "A" and "B", each observing qubit 0 of its own GHZ system.
        sims.put("A", new SimulationEngine(N_QUBITS, 0));
        sims.put("B", new SimulationEngine(N_QUBITS, 0));
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(4));

        server.createContext("/health", ex -> respond(ex, 200, "{\"ok\":true}"));
        server.createContext("/sim/", this::handleSim);

        server.start();
        System.out.println("[BlochServer] listening on http://127.0.0.1:" + PORT);
    }

    private void handleSim(HttpExchange ex) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) { respond(ex, 204, ""); return; }

        String path = ex.getRequestURI().getPath();           // /sim/A/step
        String[] parts = path.split("/");                      // ["", "sim", "A", "step"]
        if (parts.length < 4) { respond(ex, 400, err("bad path")); return; }
        String id = parts[2];
        String action = parts[3];
        SimulationEngine sim = sims.get(id);
        if (sim == null) { respond(ex, 404, err("unknown sim id " + id)); return; }

        try {
            switch (action) {
                case "reset" -> {
                    sim.reset();
                    respond(ex, 200, "{\"ok\":true}");
                }
                case "snapshot" -> respond(ex, 200, snapshotJson(sim.snapshot()));
                case "step" -> {
                    String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    double noise = parseDouble(body, "noise", 0.0);
                    String channel = parseString(body, "channel", "depolarizing");
                    respond(ex, 200, snapshotJson(sim.step(noise, channel)));
                }
                default -> respond(ex, 404, err("unknown action " + action));
            }
        } catch (Exception e) {
            respond(ex, 500, err(e.getMessage() == null ? e.toString() : e.getMessage()));
        }
    }

    private static String snapshotJson(SimulationEngine.BlochSnapshot s) {
        return "{"
                + "\"x\":" + jsonNum(s.x()) + ","
                + "\"y\":" + jsonNum(s.y()) + ","
                + "\"z\":" + jsonNum(s.z()) + ","
                + "\"purity\":" + jsonNum(s.purity()) + ","
                + "\"entropy\":" + jsonNum(s.entropy()) + ","
                + "\"fidelity\":" + jsonNum(s.fidelity()) + ","
                + "\"step\":" + s.step()
                + "}";
    }

    private static String jsonNum(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return "0";
        return Double.toString(v);
    }

    private static String err(String msg) {
        return "{\"error\":\"" + msg.replace("\"", "\\\"") + "\"}";
    }

    private static void respond(HttpExchange ex, int code, String body) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, data.length == 0 ? -1 : data.length);
        if (data.length > 0) {
            try (OutputStream os = ex.getResponseBody()) { os.write(data); }
        }
    }

    private static double parseDouble(String body, String key, double def) {
        String token = "\"" + key + "\"";
        int k = body.indexOf(token);
        if (k < 0) return def;
        int colon = body.indexOf(':', k);
        if (colon < 0) return def;
        int end = colon + 1;
        while (end < body.length() && "0123456789.-eE+".indexOf(body.charAt(end)) < 0 && !Character.isWhitespace(body.charAt(end))) end++;
        int start = end;
        while (end < body.length() && "0123456789.-eE+".indexOf(body.charAt(end)) >= 0) end++;
        if (start == end) return def;
        try { return Double.parseDouble(body.substring(start, end)); } catch (NumberFormatException e) { return def; }
    }

    private static String parseString(String body, String key, String def) {
        String token = "\"" + key + "\"";
        int k = body.indexOf(token);
        if (k < 0) return def;
        int colon = body.indexOf(':', k);
        if (colon < 0) return def;
        int q1 = body.indexOf('"', colon);
        if (q1 < 0) return def;
        int q2 = body.indexOf('"', q1 + 1);
        if (q2 < 0) return def;
        return body.substring(q1 + 1, q2);
    }
}
