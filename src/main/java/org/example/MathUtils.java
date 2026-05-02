package org.example;
import org.ojalgo.array.Array1D;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.store.GenericStore;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.R064Store;
import org.ojalgo.scalar.ComplexNumber;


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
        Complex[][] result = new Complex[x.length*y.length][x[0].length*y[0].length];

        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < x[i].length; j++) {
                for(int k = 0; k < y.length; k++) {
                    for(int l = 0; l < y[k].length; l++) {
                        result[(i*y.length)+k][j*y[0].length+l] = x[i][j].doMultiplication(y[k][l]);
                    }
                }
            }
        }
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
        Complex[][] result = new Complex[x[0].length][x.length];

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
    public static Complex[] scaleVector(Complex[] x, double scale) {
        Complex[] result = new Complex[x.length];
        Complex scaleComplex = new Complex(scale, 0);

        for(int i = 0; i < result.length; i++) {
                result[i] = x[i].doMultiplication(scaleComplex);
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
    public static double fidelity(Complex[][] x, Complex[][] y) { // x for rho and y for sigma
        Complex[][] sqrtRho = getSquaredMatrix(x);

        Complex[][] temp = innerProductSameDimensions(sqrtRho, y);
        Complex[][] M = innerProductSameDimensions(temp, sqrtRho);

        GenericStore<ComplexNumber> storeM = GenericStore.C128.make(x.length, x.length);
        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < x.length; j++) {
                storeM.set(i, j, ComplexNumber.of(
                        M[i][j].getReal(),
                        M[i][j].getImag()
                ));
            }
        }

        Eigenvalue<ComplexNumber> eigen = Eigenvalue.C128.make(storeM, true);
        eigen.decompose(storeM);
        Array1D<ComplexNumber> eigenvalues = eigen.getEigenvalues();

        double sum = 0.0;
        for(int i = 0; i < x.length; i++) {
            double val = eigenvalues.get(i).getReal();
            sum += Math.sqrt(Math.max(val, 0.0));
        }
        return sum*sum;

    }
    private static Complex[][] getSquaredMatrix(Complex[][] x) {
        GenericStore<ComplexNumber> store = GenericStore.C128.make(x.length, x.length);
        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < x[i].length; j++) {
                store.set(i, j, ComplexNumber.of(
                        x[i][j].getReal(),
                        x[i][j].getImag()
                ));
            }
        }
        // eigen decomposition, the boolean indicate it is a hermitian matrix
        Eigenvalue<ComplexNumber> eigen = Eigenvalue.C128.make(store, true);
        eigen.decompose(store);

        Array1D<ComplexNumber> eigenvalues = eigen.getEigenvalues();
        MatrixStore<ComplexNumber> V = eigen.getV();

        // we build the vector of sqrt value of eigen values
        double[] sqrtLambda = new double[x.length];

        for(int i = 0; i < x.length; i++) {
            double val = eigenvalues.get(i).getReal(); // the eigen values of a hermitian matrix are always real
            sqrtLambda[i] = Math.sqrt(Math.max(val, 0.0)); // density matrices are positive (semi-definite), eigen values should be greater or equal than 0, but floating point can be slightly negative, we handle it here.
        }

        // We reconstruct sqrt rho = V . sqrt V . V^T

        Complex[][] result = new Complex[x.length][x[0].length];
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                double re = 0.0;
                double im = 0.0;
                for(int k = 0; k < x.length; k++) {
                    ComplexNumber vik = V.get(i, k);
                    ComplexNumber vjk = V.get(j, k);
                    double ac_bd = vik.getReal() * vjk.getReal() + vik.getImaginary() * vjk.getImaginary();
                    double bc_ad = vik.getImaginary() * vjk.getReal() - vik.getReal() * vjk.getImaginary();
                    re += sqrtLambda[k] * ac_bd;
                    im += sqrtLambda[k] * bc_ad;
                }
                result[i][j] = new Complex(re, im);
            }
        }
        return result;
    }
    public static double[] getEigenvalues(Complex[][] x) {
        GenericStore<ComplexNumber> store = GenericStore.C128.make(x.length, x.length);
        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < x[i].length; j++) {
                store.set(i, j, ComplexNumber.of(
                        x[i][j].getReal(),
                        x[i][j].getImag()
                ));
            }
        }
        Eigenvalue<ComplexNumber> eigen = Eigenvalue.C128.make(store, true);
        eigen.decompose(store);
        Array1D<ComplexNumber> eigenvalues = eigen.getEigenvalues();
        double[] result = new double[x.length];

        for(int i = 0; i < result.length; i++) {
            result[i] = eigenvalues.get(i).getReal();
        }
        return result;
    }

}
