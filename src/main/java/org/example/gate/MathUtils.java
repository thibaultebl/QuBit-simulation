package org.example.gate;

import org.example.Complex;
import org.example.DensityMatrix;

import java.text.DecimalFormat;

public class MathUtils {

    public static DensityMatrix Multiplication(DensityMatrix a, DensityMatrix b) {
        Complex[][] aMatrix = a.getDensityMatrix();
        Complex[][] bMatrix = b.getDensityMatrix();
        Complex[][] result = new Complex[aMatrix.length][aMatrix[0].length]

        for (int i = 0; i < aMatrix.length; i++) {
            for (int j = 0; j < bMatrix[i].length; j++) {
                result[i][j] =
            }
        }
    }
}
