package org.example.gate;

import org.example.Qbit;

public class IdentityGate {

    public void apply(Qbit qbit) {
        double aR = qbit.getRealAlpha();
        double aI = qbit.getImagAlpha();
        double bR = qbit.getRealBeta();
        double bI = qbit.getImagBeta();

        double newAR = (1 * aR) + (0 * bR);
        double newAI = (1 * aI) + (0 * bI);
        double newBR = (0 * aR) + (1 * bR);
        double newBI = (0 * aI) + (1 * bI);

        qbit.setAlpha(newAR, newAI);
        qbit.setBeta(newBR, newBI);
    }
}
