package org.example;


public class MathUtils {

    public static Complex[][] innerProductSameDimensions(Complex[][] x, Complex[][] y) {
        Complex[][] result = new Complex[x.length][x[0].length];

        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                Complex sum = new Complex(0, 0);
                for(int k = 0; k < x.length; k++) {
                                 sum = sum.doAddition(x[i][k].doMultiplication(y[k][j]));
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static double getTrace(Complex[][] densM) {
        double sum = 0;
        for(int i = 0; i < densM.length; i++) {
            sum += densM[i][i].getReal();
        }
        return sum;
    }
    public static Complex[][] innerProductDifferentDimensions(Complex[][] x, Complex[][] y) {
        Complex[][] result = new Complex[x.length][x[0].length];
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                Complex sum = new Complex(0, 0);
                for(int k = 0; k < x.length; k++) {

                }
            }
        }
    }
}
