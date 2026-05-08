package org.example;
import org.example.Component.DensityMatrix;
import org.example.Interface.QuantumState;
import org.example.Math.Complex;
import java.io.FileWriter;
import java.io.IOException;

public class ExperimentCircuitDepth {

    public static void main(String[] args) throws IOException {

        int[] qubitCounts = {8};
        int maxDepth = 15;

        for (int numQubits : qubitCounts) {
            for (int depth = 1; depth <= maxDepth; depth++) {
                String filename = "RQ2_" + numQubits + "qBit_depth" + depth + "_depolarizing.csv";
                System.out.println("Running: " + filename);
                runExperiment(numQubits, depth, filename);
            }
        }
    }

    private static void runExperiment(int numQubits, int depth, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("noise_rate,fidelity,entropy,purity\n");

        Factory factory = new Factory();
        Complex[] system = factory.setGroundState(numQubits);

        // Build ideal state: apply gate layers without noise
        DensityMatrix initialDensity = new DensityMatrix(system);
        QuantumState idealQuantumState = new QuantumState(initialDensity, numQubits);

        for (int d = 0; d < depth; d++) {
            idealQuantumState.applyHGate(0);
            for (int q = 0; q < numQubits - 1; q++) {
                idealQuantumState.applyCNOT(q, q + 1);
            }
        }

        DensityMatrix idealState = idealQuantumState.getDensityMatrix();

        // Noise sweep
        for (double noise = 0.0; noise <= 1.0; noise += 0.01) {

            // Build noisy state: apply gate layers with noise after each layer
            DensityMatrix noisyInitial = new DensityMatrix(system);
            QuantumState noisyState = new QuantumState(noisyInitial, numQubits);

            for (int d = 0; d < depth; d++) {
                // Gate round
                noisyState.applyHGate(0);
                for (int q = 0; q < numQubits - 1; q++) {
                    noisyState.applyCNOT(q, q + 1);
                }

                // Noise on all qubits after this layer
                for (int q = 0; q < numQubits; q++) {
                    noisyState.depolarizingChannel(q, noise);
                }
            }

            double fidelity = noisyState.calculateFidelity(idealState);
            double entropy = noisyState.getEntropy();
            double purity = noisyState.getPurity();

            writer.write(noise + "," + fidelity + "," + entropy + "," + purity + "\n");
        }

        writer.close();
        System.out.println("Done: " + filename);
    }
}