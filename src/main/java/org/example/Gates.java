package org.example;

public class Gates {
    private Complex[][] hadamardGate;
    private double hadamardCoefficient;

    public Gates() {
        hadamardGate = new Complex[][]{
                {new Complex(1, 0), new Complex(1, 0)},
                {new Complex(1, 0), new Complex(-1, 0)}
        };
        hadamardCoefficient = 1/Math.sqrt(2);


    }

    public Complex[][] applyHadamard(Complex[][] input) {
        int dim = input[0].length;
        if(dim != hadamardGate.length) {
            Complex[][] hadamardExpended = expandGate(hadamardGate, dim);
        }
        Complex[][] result = new Complex[input.length][input[0].length];
    }
    private Complex[][] expandGate(Complex[][] anyGate, double dimension) {

    }
    private Complex[][] createIdentity(int dim) {
        Complex[][] identity = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            identity[i][i] = new Complex(1, 0);
        }
        return identity;
    }
}
