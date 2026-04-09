package org.example;

import java.util.Arrays;

public class Gates {
    private Complex[][] hadamardGate;
    private Complex[][] identityGate;
    private double hadamardCoefficient;
    private Complex[][] cNotGate;
    private Complex[][] pauliXGate;
    private Complex[][] pauliYGate;
    private Complex[][] pauliZGate;
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
        cNotGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(1, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(0, 0), new Complex(1, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(1, 0), new Complex(0, 0)},
        };
        pauliXGate = new Complex[][]{
                {new Complex(0, 0), new Complex(1, 0)},
                {new Complex(1, 0), new Complex(0, 0)},
        };
        pauliYGate = new Complex[][]{
                {new Complex(0, 0), new Complex(0, -1)},
                {new Complex(0, 1), new Complex(0, 0)},
        };
        pauliZGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(-1, 0)},
        };
    }

    public Complex[][] applyHadamard(Complex[][] input, int qBitConcerned) {
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(hadamardGate, identityGate, qBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));
        Complex[][] unitaryFullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(unitaryFull));
        return MathUtils.scaleMatrix(MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, input), unitaryFullConjugateTranspose), 1/2); // hadamard coefficient
    }

    public Complex[][] applyCNOT(Complex[][] input, int controlQBit, int targetQBit) {
        int totalQBits = (int) (Math.log(input[0].length) / Math.log(2));
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(cNotGate, identityGate, controlQBit, targetQBit, totalQBits);
        Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
        Complex[][] unitaryFullConjugateTranspose = MathUtils.transpose(unitaryFullConjugate);
        Complex[][] preResult = MathUtils.innerProductSameDimensions(unitaryFull, input);
        return MathUtils.innerProductSameDimensions(preResult, unitaryFullConjugateTranspose);
    }


    // Pauli gate are kinda hard coded, a generic method should be done later
    public Complex[][] applyPauliX(Complex[][] input, int qBitConcerned) {
        int totalQBits = (int) (Math.log(input[0].length) / Math.log(2));
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(pauliXGate, identityGate, qBitConcerned, totalQBits);
        Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
        Complex[][] unitaryFullConjugateTranspose = MathUtils.transpose(unitaryFullConjugate);
        Complex[][] preResult = MathUtils.innerProductSameDimensions(unitaryFull, input);
        return MathUtils.innerProductSameDimensions(preResult, unitaryFullConjugateTranspose);
    }

    public Complex[][] applyPauliY(Complex[][] input, int qBitConcerned) {
        int totalQBits = (int) (Math.log(input[0].length) / Math.log(2));
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(pauliYGate, identityGate, qBitConcerned, totalQBits);
        Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
        Complex[][] unitaryFullConjugateTranspose = MathUtils.transpose(unitaryFullConjugate);
        Complex[][] preResult = MathUtils.innerProductSameDimensions(unitaryFull, input);
        return MathUtils.innerProductSameDimensions(preResult, unitaryFullConjugateTranspose);
    }
    public Complex[][] applyPauliZ(Complex[][] input, int qBitConcerned) {
        int totalQBits = (int) (Math.log(input[0].length) / Math.log(2));
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(pauliZGate, identityGate, qBitConcerned, totalQBits);
        Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
        Complex[][] unitaryFullConjugateTranspose = MathUtils.transpose(unitaryFullConjugate);
        Complex[][] preResult = MathUtils.innerProductSameDimensions(unitaryFull, input);
        return MathUtils.innerProductSameDimensions(preResult, unitaryFullConjugateTranspose);
    }







}
