import { SimPanel } from "./SimPanel";

const ACCENT_A = "#6b1414";
const ACCENT_B = "#152150";

export function App() {
  return (
    <div className="app">
      <div className="dragstrip" />
      <main className="split">
        <SimPanel id="A" accent={ACCENT_A} label="α" />
        <div className="divider" />
        <SimPanel id="B" accent={ACCENT_B} label="β" />
      </main>
    </div>
  );
}
