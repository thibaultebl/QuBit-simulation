package org.example;

import org.example.Component.DensityMatrix;
import org.example.Interface.QuantumState;
import org.example.Math.Complex;

import java.io.FileWriter;
import java.io.IOException;

public class ExperimentFidelitySigmoid {

    public static void main(String[] args) throws IOException {

        String[] noiseTypes = {"depolarizing", "phaseFlip", "amplitudeDamping"};
        String[] stateTypes = {"GHZ", "Product"};

        for (String stateType : stateTypes) {
            for (String noiseType : noiseTypes) {
                for (int numQubits = 2; numQubits <= 8; numQubits++) {
                    String filename = "RQ1_" + numQubits + "qBit_" + stateType + "_" + noiseType + ".csv";
                    System.out.println("Running: " + filename);
                    runExperiment(numQubits, stateType, noiseType, filename);
                }
            }
        }
    }

    private static void runExperiment(int numQubits, String stateType, String noiseType, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("noise_rate,fidelity,entropy,purity\n");

        Factory factory = new Factory();
        Complex[] system = factory.setGroundState(numQubits);

        DensityMatrix densityMatrix = new DensityMatrix(system);
        QuantumState idealQuantumState = new QuantumState(densityMatrix, numQubits);

        // Prepare the initial state
        if (stateType.equals("GHZ")) {
            idealQuantumState.applyHGate(0);
            for (int q = 0; q < numQubits - 1; q++) {
                idealQuantumState.applyCNOT(q, q + 1);
            }
        } else { // Product state
            for (int q = 0; q < numQubits; q++) {
                idealQuantumState.applyHGate(q);
            }
        }

        DensityMatrix idealState = idealQuantumState.getDensityMatrix();

        // Noise sweep
        for (double noise = 0.0; noise <= 1.0; noise += 0.01) {
            QuantumState noisyState = new QuantumState(idealState, numQubits);

            // Apply noise to all qubits
            for (int q = 0; q < numQubits; q++) {
                switch (noiseType) {
                    case "depolarizing":
                        noisyState.depolarizingChannel(q, noise);
                        break;
                    case "phaseFlip":
                        noisyState.phaseFlip(q, noise);
                        break;
                    case "amplitudeDamping":
                        noisyState.amplitudeDamping(q, noise);
                        break;
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