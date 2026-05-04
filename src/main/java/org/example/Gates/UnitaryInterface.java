package org.example.Gates;

import org.example.Math.Complex;

public interface UnitaryInterface {
    public Complex[][] computeUnitaryFull(Complex[][] anyGate, Complex[][] identityGate, int qBitConcerned, int totalQBit);
    public Complex[][] computeUnitaryFull(Complex[][] anyGate, Complex[][] identityGate, int controlQBit, int targetQBit, int totalQBit);
}
