package org.example;

import org.example.Component.DensityMatrix;
import org.example.Interface.QuantumState;
import org.example.Math.Complex;

import java.io.FileWriter;
import java.io.IOException;

// RQ.3 : Which topology (linear / star / all-to-all) is most resilient to noise?

public class ExperimentEntropySpike {

    public static void main(String[] args) throws IOException {
        int[] qubitCounts = {8};
        String[] noiseTypes = {"depolarizing", "phaseFlip", "amplitudeDamping"};

        for (int numQubits : qubitCounts) {
            for (String noiseType : noiseTypes) {
                String filename = "RQ4_" + numQubits + "qBit_" + noiseType + "_entropySpike.csv";
                System.out.println("Running: " + filename);
                runExperiment(numQubits, noiseType, filename);
                System.out.println("Done: " + filename);
            }
        }
    }

    private static void runExperiment(int numQubits, String noiseType, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("noise_rate,fidelity,entropy,purity,entropy_derivative\n");

        Factory factory = new Factory();
        Complex[] system = factory.setGroundState(numQubits);

        DensityMatrix initialDensity = new DensityMatrix(system);
        QuantumState idealQuantumState = new QuantumState(initialDensity, numQubits);
        idealQuantumState.applyHGate(0);
        for (int q = 0; q < numQubits - 1; q++) {
            idealQuantumState.applyCNOT(q, q + 1);
        }
        DensityMatrix idealState = idealQuantumState.getDensityMatrix();

        double prevEntropy = 0.0;
        double prevNoise = 0.0;

        for (double noise = 0.0; noise <= 1.0; noise += 0.01) {
            DensityMatrix freshInitial = new DensityMatrix(system);
            QuantumState noisyState = new QuantumState(freshInitial, numQubits);

            noisyState.applyHGate(0);
            for (int q = 0; q < numQubits - 1; q++) {
                noisyState.applyCNOT(q, q + 1);
            }

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

            double entropyDerivative = 0.0;
            if (noise > 0.0) {
                entropyDerivative = (entropy - prevEntropy) / (noise - prevNoise);
            }

            writer.write(noise + "," + fidelity + "," + entropy + "," + purity + "," + entropyDerivative + "\n");

            prevEntropy = entropy;
            prevNoise = noise;
        }

        writer.close();
    }
}