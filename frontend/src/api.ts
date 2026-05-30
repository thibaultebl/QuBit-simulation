const BASE = "http://127.0.0.1:8765";

export type NoiseChannel =
  | "bitflip"
  | "phaseflip"
  | "depolarizing"
  | "amplitudedamping";

export interface Snapshot {
  x: number;
  y: number;
  z: number;
  purity: number;
  entropy: number;
  fidelity: number;
  step: number;
}

export async function step(
  id: "A" | "B",
  noise: number,
  channel: NoiseChannel,
): Promise<Snapshot> {
  const res = await fetch(`${BASE}/sim/${id}/step`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ noise, channel }),
  });
  if (!res.ok) throw new Error(`step ${id} failed: ${res.status}`);
  return res.json();
}

export async function reset(id: "A" | "B"): Promise<void> {
  await fetch(`${BASE}/sim/${id}/reset`, { method: "POST" });
}
