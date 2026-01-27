package org.example.gate;

import org.example.Qbit;

public class PhasesGate {
    public void applySGate(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAR = aR;
        double newAI = aI;
        double newBR = -bI;
        double newBI = bR;

        qbit.setAlpha(newAR, newAI);
        qbit.setBeta(newBR, newBI);
    }
    public void applyTGate(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAR = aR;
        double newAI = aI;
        double newBR = (bR - bI) / Math.sqrt(2);
        double newBI = (bR + bI) / Math.sqrt(2);

        qbit.setAlpha(newAR, newAI);
        qbit.setBeta(newBR, newBI);
    }
}
