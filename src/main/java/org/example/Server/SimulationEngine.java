package org.example.Server;

import org.example.Component.DensityMatrix;
import org.example.Factory;
import org.example.Interface.QuantumState;
import org.example.Math.Complex;
import org.example.Math.MathUtils;

public class SimulationEngine {
    private final int numQubits;
    private final int observedQubit;
    private QuantumState state;
    private QuantumState ideal;
    private long stepCount = 0;

    public SimulationEngine(int numQubits, int observedQubit) {
        this.numQubits = numQubits;
        this.observedQubit = observedQubit;
        reset();
    }

    public synchronized void reset() {
        Factory factory = new Factory();
        Complex[] ghz = factory.setGHZState(numQubits);
        this.state = new QuantumState(new DensityMatrix(ghz), numQubits);
        Complex[] ghz2 = factory.setGHZState(numQubits);
        this.ideal = new QuantumState(new DensityMatrix(ghz2), numQubits);
        this.stepCount = 0;
    }

    public synchronized BlochSnapshot step(double noise, String channel) {
        if (stepCount == 0) {
            // GHZ on 2 qubits == Bell state (|00>+|11>)/sqrt(2).
            // CNOT(0,1) turns it into (|00>+|10>)/sqrt(2) = |+>|0>, so the
            // observed qubit is in a pure state on the +x axis and free to evolve.
            int partner = (observedQubit + 1) % numQubits;
            state.applyCNOT(observedQubit, partner);
            ideal.applyCNOT(observedQubit, partner);
        }

        state.applyRy(observedQubit, 0.045);
        state.applyRz(observedQubit, 0.071);
        ideal.applyRy(observedQubit, 0.045);
        ideal.applyRz(observedQubit, 0.071);

        if (noise > 1e-6) {
            switch (channel) {
                case "bitflip"           -> state.bitFlip(observedQubit, clamp(noise));
                case "phaseflip"         -> state.phaseFlip(observedQubit, clamp(noise));
                case "depolarizing"      -> state.depolarizingChannel(observedQubit, clamp(noise));
                case "amplitudedamping"  -> state.amplitudeDamping(observedQubit, clamp(noise));
                default                  -> state.depolarizingChannel(observedQubit, clamp(noise));
            }
        }

        stepCount++;
        return snapshot();
    }

    public synchronized BlochSnapshot snapshot() {
        Complex[][] rho = state.getDensityMatrix().getDensityMatrix();
        Complex[][] reduced = partialTraceToQubit(rho, observedQubit, numQubits);

        double x = 2.0 * reduced[0][1].getReal();
        double y = -2.0 * reduced[0][1].getImag();
        double z = reduced[0][0].getReal() - reduced[1][1].getReal();
        double fidelity = MathUtils.fidelity(rho, ideal.getDensityMatrix().getDensityMatrix());
        return new BlochSnapshot(x, y, z, state.getPurity(), state.getEntropy(), fidelity, stepCount);
    }

    private static double clamp(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    public static Complex[][] partialTraceToQubit(Complex[][] rho, int keep, int n) {
        int dim = 1 << n;
        Complex[][] reduced = new Complex[][]{
                {new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(0, 0)},
        };
        int keepBit = n - 1 - keep;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int iKeep = (i >> keepBit) & 1;
                int jKeep = (j >> keepBit) & 1;
                int iRest = (i & ~(1 << keepBit));
                int jRest = (j & ~(1 << keepBit));
                if (iRest == jRest) {
                    reduced[iKeep][jKeep] = reduced[iKeep][jKeep].doAddition(rho[i][j]);
                }
            }
        }
        return reduced;
    }

    public record BlochSnapshot(double x, double y, double z, double purity, double entropy, double fidelity, long step) {}
}
