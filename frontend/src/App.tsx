import { SimPanel } from "./SimPanel";

const ACCENT = "#c8232d";

export function App() {
  return (
    <div className="app">
      <div className="dragstrip" />
      <main className="single">
        <SimPanel id="A" accent={ACCENT} label="α" />
      </main>
    </div>
  );
}
