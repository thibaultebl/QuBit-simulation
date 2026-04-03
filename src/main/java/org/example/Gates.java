package org.example;

public class Gates {
    private Complex[][] hadamardGate;
    private Complex[][] identityGate;
    private double hadamardCoefficient;

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
        Complex[][] unitaryFull = computeUnitaryFull(hadamardGate, qBitConcerned, totalQBits);
        Complex[][] unitaryFullConjugate = computeConjugate(unitaryFull);
        Complex[][] preResult = MathUtils.innerProductSameDimensions(unitaryFull, input);
        Complex[][] result = MathUtils.innerProductSameDimensions(preResult, unitaryFullConjugate);
        return result;
    }
    private Complex[][] computeUnitaryFull(Complex[][] anyGate, int qBitConcerned, int totalQBit) {
        int dimension = (int) Math.pow(2, totalQBit);
        Complex[][] unitaryFull = new Complex[dimension][dimension];
        for(int q = 0; q < totalQBit; q++) {
            if(qBitConcerned == q) {

            }
        }

    }

}
