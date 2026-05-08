interface Props {
  data: number[];
  accent: string;
  capacity: number;
}

// 2-qubit purity floor is 1/d = 0.25 (maximally mixed state).  Clip the
// vertical axis to [0.25, 1.00] so the curve uses the full plot height.
const Y_MIN = 0.25;
const Y_MAX = 1.0;
const Y_TICKS = [1.0, 0.75, 0.5, 0.25];

export function PurityGraph({ data, accent, capacity }: Props) {
  const last = data.length ? data[data.length - 1] : 1;

  // Map a purity value to a percentage from the top of the plot box.
  const pct = (p: number) => ((Y_MAX - p) / (Y_MAX - Y_MIN)) * 100;

  // Curve points in viewBox units (0..1000, 0..1000).  Will be stretched by
  // preserveAspectRatio="none"; only the curve is in the SVG so no text is
  // distorted.
  const VW = 1000;
  const VH = 1000;
  const points = data
    .map((p, i) => {
      const x = (i / Math.max(capacity - 1, 1)) * VW;
      const y = (pct(p) / 100) * VH;
      return `${x.toFixed(1)},${y.toFixed(1)}`;
    })
    .join(" ");

  return (
    <div className="graph">
      <div className="graph__header">
        <span className="graph__title">purity(t)</span>
        <span className="graph__cur">
          <span className="muted">latest = </span>
          {last.toFixed(4)}
        </span>
      </div>

      <div className="graph__body">
        <div className="graph__yaxis">
          {Y_TICKS.map((v) => (
            <div key={v} className="graph__yt" style={{ top: `${pct(v)}%` }}>
              <span className="graph__yt-num">{v.toFixed(2)}</span>
              <span className="graph__yt-tick" />
            </div>
          ))}
        </div>

        <div className="graph__plot">
          {Y_TICKS.map((v) => (
            <div
              key={v}
              className={"graph__grid " + (v === 1 || v === Y_MIN ? "graph__grid--solid" : "")}
              style={{ top: `${pct(v)}%` }}
            />
          ))}
          <svg
            className="graph__svg"
            viewBox={`0 0 ${VW} ${VH}`}
            preserveAspectRatio="none"
            shapeRendering="geometricPrecision"
          >
            {data.length > 1 && (
              <polyline
                points={points}
                fill="none"
                stroke={accent}
                strokeWidth={2.5}
                vectorEffect="non-scaling-stroke"
                strokeLinejoin="round"
                strokeLinecap="round"
              />
            )}
          </svg>
        </div>
      </div>

      <div className="graph__xaxis">
        <span>t − {capacity}</span>
        <span className="graph__xlbl">step</span>
        <span>t</span>
      </div>
    </div>
  );
}
