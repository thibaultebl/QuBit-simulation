package org.example;

import java.util.Arrays;

public class Gates {
    private Complex[][] hadamardGate;
    private Complex[][] identityGate;
    private double hadamardCoefficient;
    private UnitaryInterface unitaryInterface;

    public Gates() {
        hadamardGate = new Complex[][]{
                {new Complex(1, 0), new Complex(1, 0)},
                {new Complex(1, 0), new Complex(-1, 0)}
        };
        hadamardCoefficient = 1/Math.sqrt(2);
        identityGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(1, 0)}
        };

    }

    public Complex[][] applyHadamard(Complex[][] input, int qBitConcerned) {
        int totalQBits = (int) (Math.log(input[0].length) / Math.log(2));
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(hadamardGate, identityGate, qBitConcerned, totalQBits);
        Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
        Complex[][] unitaryFullConjugateTranspose = MathUtils.transpose(unitaryFullConjugate);
        Complex[][] preResult = MathUtils.innerProductSameDimensions(unitaryFull, input);
        Complex[][] result = MathUtils.innerProductSameDimensions(preResult, unitaryFullConjugateTranspose);
        return MathUtils.scaleMatrix(result, 1/2); // hadamard coefficient
    }

    public Complex[][] applyCNOT(Complex[][] input, int controlQBit, int targetQBit) {
        int totalQBits = (int) (Math.log(input[0].length) / Math.log(2));
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull()
    }







}
