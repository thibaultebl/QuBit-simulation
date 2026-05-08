import { useEffect, useRef, useState } from "react";
import { BlochSphere } from "./BlochSphere";
import { PurityGraph } from "./PurityGraph";
import { reset, step, type NoiseChannel, type Snapshot } from "./api";

const TRAIL_LEN = 22;
const TICK_MS = 80;
const PURITY_HISTORY = 400;

interface Props {
  id: "A" | "B";
  accent: string;
  label: string;
}

const CHANNELS: { id: NoiseChannel; label: string }[] = [
  { id: "depolarizing", label: "depolarizing" },
  { id: "amplitudedamping", label: "amplitude damping" },
  { id: "phaseflip", label: "phase flip" },
  { id: "bitflip", label: "bit flip" },
];

const NOISE_MAX = 0.3;
const NOISE_STEP = 0.001;

export function SimPanel({ id, accent, label }: Props) {
  const [snap, setSnap] = useState<Snapshot>({ x: 0, y: 0, z: 0, purity: 1, entropy: 0, fidelity: 1, step: 0 });
  const [trail, setTrail] = useState<Array<{ x: number; y: number; z: number }>>([]);
  const [purityHist, setPurityHist] = useState<number[]>([]);
  const [noise, setNoise] = useState(0);
  const [channel, setChannel] = useState<NoiseChannel>("depolarizing");
  const [error, setError] = useState<string | null>(null);
  const noiseRef = useRef(noise);
  const channelRef = useRef(channel);
  noiseRef.current = noise;
  channelRef.current = channel;

  useEffect(() => {
    let cancelled = false;
    let timer: ReturnType<typeof setTimeout> | null = null;
    const loop = async () => {
      try {
        const s = await step(id, noiseRef.current, channelRef.current);
        if (cancelled) return;
        setSnap(s);
        setTrail((prev) => {
          const next = [...prev, { x: s.x, y: s.y, z: s.z }];
          if (next.length > TRAIL_LEN) next.splice(0, next.length - TRAIL_LEN);
          return next;
        });
        setPurityHist((prev) => {
          const next = [...prev, s.purity];
          if (next.length > PURITY_HISTORY) next.splice(0, next.length - PURITY_HISTORY);
          return next;
        });
        setError(null);
      } catch (e) {
        setError(String((e as Error).message ?? e));
      }
      if (!cancelled) timer = setTimeout(loop, TICK_MS);
    };
    loop();
    return () => {
      cancelled = true;
      if (timer) clearTimeout(timer);
    };
  }, [id]);

  const radius = Math.sqrt(snap.x * snap.x + snap.y * snap.y + snap.z * snap.z);

  return (
    <section className="panel" style={{ ["--accent" as string]: accent }}>
      <div className="panel__canvas">
        <BlochSphere vector={{ x: snap.x, y: snap.y, z: snap.z }} trail={trail} accent={accent} />
      </div>

      <div className="panel__head">
        <span>sim {label}</span>
        <span className="muted"> | bell(2) | obs q0 | t={snap.step}</span>
        {error && <span className="err"> | offline</span>}
      </div>

      <div className="panel__readout">
        <Row k="bloch.x" v={snap.x} sign />
        <Row k="bloch.y" v={snap.y} sign />
        <Row k="bloch.z" v={snap.z} sign />
        <Row k="|r|" v={radius} />
        <Row k="purity" v={snap.purity} />
        <Row k="entropy" v={snap.entropy} />
        <Row k="fidelity" v={snap.fidelity} />
      </div>

      <div className="panel__bottom">
        <div className="panel__ctrls">
          <div className="ctrl">
            <label>noise = {noise.toFixed(3)} <span className="muted">(0 .. {NOISE_MAX})</span></label>
            <input
              type="range"
              min={0}
              max={NOISE_MAX}
              step={NOISE_STEP}
              value={noise}
              onChange={(e) => setNoise(parseFloat(e.target.value))}
            />
          </div>

          <fieldset className="ctrl">
            <legend>channel</legend>
            {CHANNELS.map((c) => (
              <label key={c.id} className="opt">
                <input
                  type="radio"
                  name={`ch-${id}`}
                  checked={channel === c.id}
                  onChange={() => setChannel(c.id)}
                />
                <span>{c.label}</span>
              </label>
            ))}
          </fieldset>

          <button
            className="ctrl__reset"
            onClick={() => {
              setTrail([]);
              setPurityHist([]);
              reset(id);
            }}
          >[ reset ]</button>
        </div>

        <div className="panel__graph">
          <PurityGraph data={purityHist} accent={accent} capacity={PURITY_HISTORY} />
        </div>
      </div>
    </section>
  );
}

function Row({ k, v, sign }: { k: string; v: number; sign?: boolean }) {
  const text = sign ? (v >= 0 ? "+" : "") + v.toFixed(4) : v.toFixed(4);
  return (
    <div className="row">
      <span className="row__k">{k}</span>
      <span className="row__v">{text}</span>
    </div>
  );
}
