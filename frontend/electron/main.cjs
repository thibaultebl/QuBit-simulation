const { app, BrowserWindow } = require("electron");
const { spawn } = require("child_process");
const path = require("path");
const http = require("http");

const DEV_URL = "http://127.0.0.1:5173";
const BACKEND_URL = "http://127.0.0.1:8765/health";
const PROJECT_ROOT = path.resolve(__dirname, "..", "..");
const JAVA_HOME =
  "/Users/thibaultebl/Library/Java/JavaVirtualMachines/graalvm-jdk-25/Contents/Home";

const isDev = !app.isPackaged;
let backend = null;

function startBackend() {
  console.log("[main] launching java backend in", PROJECT_ROOT);
  backend = spawn(
    "mvn",
    [
      "-q",
      "org.codehaus.mojo:exec-maven-plugin:3.4.1:java",
      "-Dexec.mainClass=org.example.BlochSphereVisu",
    ],
    {
      cwd: PROJECT_ROOT,
      env: { ...process.env, JAVA_HOME, PATH: `${JAVA_HOME}/bin:${process.env.PATH}` },
      shell: true,
    },
  );
  backend.stdout.on("data", (d) => process.stdout.write(`[java] ${d}`));
  backend.stderr.on("data", (d) => process.stderr.write(`[java] ${d}`));
  backend.on("exit", (c) => console.log(`[java] exited with code ${c}`));
}

function waitForBackend(timeoutMs = 90_000) {
  const t0 = Date.now();
  return new Promise((resolve, reject) => {
    const tick = () => {
      const req = http.get(BACKEND_URL, (res) => {
        res.resume();
        if (res.statusCode === 200) resolve();
        else retry();
      });
      req.on("error", retry);
      req.setTimeout(800, () => req.destroy());
    };
    const retry = () => {
      if (Date.now() - t0 > timeoutMs) reject(new Error("backend did not come up"));
      else setTimeout(tick, 600);
    };
    tick();
  });
}

function createWindow() {
  const win = new BrowserWindow({
    width: 1600,
    height: 900,
    minWidth: 1100,
    minHeight: 600,
    backgroundColor: "#0c0c0c",
    titleBarStyle: "hiddenInset",
    trafficLightPosition: { x: 14, y: 14 },
    webPreferences: { contextIsolation: true, nodeIntegration: false },
  });
  if (isDev) win.loadURL(DEV_URL);
  else win.loadFile(path.join(__dirname, "..", "dist", "index.html"));
}

app.whenReady().then(async () => {
  startBackend();
  try {
    await waitForBackend();
    console.log("[main] backend up, opening window");
  } catch (e) {
    console.error("[main]", e.message, "- opening window anyway");
  }
  createWindow();
});

app.on("before-quit", () => {
  if (backend && !backend.killed) {
    console.log("[main] killing java backend");
    backend.kill("SIGTERM");
  }
});
app.on("window-all-closed", () => {
  if (process.platform !== "darwin") app.quit();
});
app.on("activate", () => {
  if (BrowserWindow.getAllWindows().length === 0) createWindow();
});
