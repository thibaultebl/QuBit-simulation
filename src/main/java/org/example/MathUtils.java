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
    public static Complex[][] kroneckerProduct(Complex[][] x, Complex[][] y) { // = tensor product
        int rowDim = (x.length * y.length);
        int colDim = (x[0].length * y[0].length);
        Complex[][] result = new Complex[rowDim][colDim];
        // TO COMPLETE
        return result;
    }
    public static Complex[][] conjugate(Complex[][] x) {
        Complex[][] result = new Complex[x.length][x[0].length];

        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < x[i].length; j++) {
                result[i][j] = x[i][j].conjugate();
            }
        }
        return result;
    }
    public static Complex[][] transpose(Complex[][] x) {
        Complex[][] result = new Complex[x.length][x[0].length];

        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                result[j][i] = x[i][j];
            }
        }
        return result;
    }
    public static Complex[][] scaleMatrix(Complex[][] x, double scale) {
        Complex[][] result = new Complex[x.length][x[0].length];
        Complex scaleComplex = new Complex(scale, 0);

        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                result[i][j] = x[i][j].doMultiplication(scaleComplex);
            }
        }
        return result;
    }
    public static Complex[][] matrixAddition(Complex[][] x, Complex[][] y) {
        Complex[][] result = new Complex[x.length][x[0].length];

        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                result[i][j] = x[i][j].doAddition(y[i][j]);
            }
        }
        return result;
    }
}
