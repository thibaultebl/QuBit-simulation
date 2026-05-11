package org.example;

import org.example.Component.DensityMatrix;
import org.example.Interface.QuantumState;
import org.example.Math.Complex;

import java.io.FileWriter;
import java.io.IOException;

// RQ.4 : Does von Neumann entropy spike at the fidelity threshold, confirming the phase-transition analogy?

public class ExperimentTopology {

    public static void main(String[] args) throws IOException {
        int[] qubitCounts = {8};
        String[] topologies = {"linear", "star", "allToAll"};

        for (int numQubits : qubitCounts) {
            for (String topology : topologies) {
                String filename = "RQ3_" + numQubits + "qBit_" + topology + "_depolarizing.csv";
                System.out.println("Running: " + filename);
                runExperiment(numQubits, topology, filename);
                System.out.println("Done: " + filename);
            }
        }
    }

    private static void runExperiment(int numQubits, String topology, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("noise_rate,fidelity,entropy,purity\n");

        Factory factory = new Factory();
        Complex[] system = factory.setGroundState(numQubits);

        DensityMatrix initialDensity = new DensityMatrix(system);
        QuantumState idealState = new QuantumState(initialDensity, numQubits);
        prepareTopology(idealState, topology, numQubits);
        DensityMatrix ideal = idealState.getDensityMatrix();

        for (double noise = 0.0; noise <= 1.0; noise += 0.01) {
            DensityMatrix freshInitial = new DensityMatrix(system);
            QuantumState noisyState = new QuantumState(freshInitial, numQubits);
            prepareTopology(noisyState, topology, numQubits);

            for (int q = 0; q < numQubits; q++) {
                noisyState.depolarizingChannel(q, noise);
            }

            double fidelity = noisyState.calculateFidelity(ideal);
            double entropy = noisyState.getEntropy();
            double purity = noisyState.getPurity();

            writer.write(noise + "," + fidelity + "," + entropy + "," + purity + "\n");
        }

        writer.close();
    }

    private static void prepareTopology(QuantumState state, String topology, int numQubits) {
        switch (topology) {

            case "linear":
                state.applyHGate(0);
                for (int q = 0; q < numQubits - 1; q++) {
                    state.applyCNOT(q, q + 1);
                }
                break;

            case "star":
                state.applyHGate(0);
                for (int q = 1; q < numQubits; q++) {
                    state.applyCNOT(0, q);
                }
                break;

            case "allToAll":
                for (int q = 0; q < numQubits; q++) {
                    state.applyHGate(q);
                }
                for (int q = 0; q < numQubits; q++) {
                    for (int r = q + 1; r < numQubits; r++) {
                        state.applyCNOT(q, r);
                    }
                }
                break;
        }
    }
}