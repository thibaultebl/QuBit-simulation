package org.example;

import org.example.gate.HadamardGate;
import org.example.gate.IdentityGate;
import org.example.gate.PauliGate;
import org.example.gate.PhasesGate;

public class Environment {
    private Qbit qbit;
    private final int iterations;
    private IdentityGate identityGate;
    private PauliGate pauliGate;
    private HadamardGate hadamardGate;
    private PhasesGate phasesGate;
    private double zeroCount;
    private double oneCount;
    private double zeroProb;
    private double oneProb;

    public Environment(int iterations) {
        qbit = new Qbit();
        this.iterations = iterations;
        identityGate = new IdentityGate();
        pauliGate = new PauliGate();
        hadamardGate = new HadamardGate();
        phasesGate = new PhasesGate();
        zeroCount = 0;
        oneCount = 0;
        zeroProb = 0.0;
        oneProb = 0.0;
    }

    public void simulate() {
        for(int i = 0; i < iterations; i++) {
            identityGate.apply(qbit);
            pauliGate.applyX(qbit);
            pauliGate.applyY(qbit);
            pauliGate.applyZ(qbit);
            hadamardGate.applyGate(qbit);
            phasesGate.applySGate(qbit);
            phasesGate.applyTGate(qbit);


            oneCount += qbit.measure();

            if(qbit.getAlphaSquared() + qbit.getBetaSquared() != 1.0) {
                System.out.println("Error: Amplitudes do not sum to 1 after collapse.");
            }

        }
        zeroCount = iterations - oneCount;
        zeroProb = getZeroCountProbability();
        oneProb = getOneCountProbability();
        System.out.println("After " + iterations + " iterations:");
        System.out.println("Probability of measuring |0>: " + zeroProb);
        System.out.println("Probability of measuring |1>: " + oneProb);
    }
    private double getZeroCountProbability() {
        return zeroCount / iterations;
    }
    private double getOneCountProbability() {
        return oneCount / iterations;
    }
}
