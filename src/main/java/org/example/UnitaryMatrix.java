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
    public Complex[][] computeUnitaryFull(Complex[][] anyGate, Complex[][] identityGate, int controlQBit, int targetQBit, int totalQBit) {
        Complex[][] unitaryFull;

        if(controlQBit == 0 || targetQBit == 0) {
            unitaryFull = anyGate;
        } else {
            unitaryFull = identityGate;
        }

        for(int q = 1; q < totalQBit; q++) {
            if(controlQBit == q) {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, anyGate);
            } else if (targetQBit == q) {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, anyGate);
            } else {
                unitaryFull = MathUtils.kroneckerProduct(unitaryFull, identityGate);
            }

        }
        return unitaryFull;
    }
}
