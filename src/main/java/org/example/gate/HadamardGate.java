package org.example.gate;

import org.example.Qbit;

public class HadamardGate {
    public void applyGate(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAR = (aR + bR) / Math.sqrt(2);
        double newAI = (aI + bI) / Math.sqrt(2);
        double newBR = (aR - bR) / Math.sqrt(2);
        double newBI = (aI - bI) / Math.sqrt(2);

        qbit.setAlpha(newAR, newAI);
        qbit.setBeta(newBR, newBI);
    }
}
