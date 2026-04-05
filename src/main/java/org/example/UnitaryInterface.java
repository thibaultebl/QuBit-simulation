package org.example;

public interface UnitaryInterface {
    public Complex[][] computeUnitaryFull(Complex[][] anyGate, Complex[][] identityGate, int qBitConcerned, int totalQBit);
    public Complex[][] computeUnitaryFull(Complex[][] anyGate, Complex[][] identityGate, int controlQBit, int targetQBit, int totalQBit);
}
