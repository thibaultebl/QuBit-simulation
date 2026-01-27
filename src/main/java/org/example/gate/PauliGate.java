package org.example.gate;

import org.example.Qbit;

public class PauliGate {

    public void applyX(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAlphaR = (0 * aR) + (1 * bR);
        double newAlphaI = (0 * aI) + (1 * bI);
        double newBetaR  = (1 * aR) + (0 * bR);
        double newBetaI  = (1 * aI) + (0 * bI);

        qbit.setAlpha(newAlphaR, newAlphaI);
        qbit.setBeta(newBetaR, newBetaI);
    }
    public void applyY(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAlphaR = bI;
        double newAlphaI = -bR;
        double newBetaR = -aI;
        double newBetaI = aR;

        qbit.setAlpha(newAlphaR, newAlphaI);
        qbit.setBeta(newBetaR, newBetaI);
    }
    public void applyZ(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAlphaR = (1 * aR) + (0 * bR);
        double newAlphaI = (1 * aI) + (0 * bI);
        double newBetaR  = (0 * aR) + (-1 * bR);
        double newBetaI  = (0 * aI) + (-1 * bI);

        qbit.setAlpha(newAlphaR, newAlphaI);
        qbit.setBeta(newBetaR, newBetaI);
    }
}
