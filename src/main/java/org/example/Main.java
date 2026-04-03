package org.example;

public class Main {

    public static void main(String[] args) {
        Complex[][] A = {
                { new Complex(1, 0), new Complex(2, 0) },
                { new Complex(3, 0), new Complex(4, 0) }
        };

        Complex[][] B = {
                { new Complex(1, 0), new Complex(0, 0) },
                { new Complex(0, 0), new Complex(1, 0) }  // Identity matrix
        };

        Complex[][] result = MathUtils.innerProductSameDimensions(A, B);

// A * I should equal A
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                System.out.println("result[" + i + "][" + j + "] = " + result[i][j].toString());

    }
}
