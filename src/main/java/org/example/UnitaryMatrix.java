package org.example;

public class UnitaryMatrix implements UnitaryInterface{

    @Override
    public Complex[][] computeUnitaryFull(Complex[][] anyGate, Complex[][] identityGate , int qBitConcerned, int totalQBit) {
        Complex[][] unitaryFull;

        if(qBitConcerned == 0) {
            unitaryFull = anyGate;
        } else {
            unitaryFull = identityGate;
        }

        for(int q = 1; q < totalQBit; q++) {
            if(qBitConcerned == q) {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, anyGate);
            } else {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, identityGate);
            }

        }
        return unitaryFull;
    }

    @Override
    public Complex[][] computeUnitaryFull(Complex[][] cNot, Complex[][] identityGate, int controlQBit, int targetQBit, int totalQBit) {
        Complex[][] unitaryFull;

        if(Math.abs(controlQBit - targetQBit) != 1) {
            throw new IllegalArgumentException("both Qbit aren't adjacent");
        }
        int p = 0;
        if(controlQBit == 0 || targetQBit == 0) {
            unitaryFull = cNot;
            p+=2;
        } else {
            unitaryFull = identityGate;
            p++;
        }

        for(int q = p; q < totalQBit; q++) {
            if(controlQBit == q) {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, cNot);
                q++;
            } else {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, identityGate);
            }
        }
        return unitaryFull;
    }
}
