package org.example.Component;

import org.example.Math.Complex;
import org.example.Math.MathUtils;

public class DensityMatrix {
    private final Complex[][] densityMatrix;
    private final double epsilon = 1e-10;

    public DensityMatrix(Complex[] vectorState) {
        if(checkProb(vectorState)){
            densityMatrix = buildDensityMatrix(vectorState);
            if(!validateDensityMatrix(densityMatrix) || !checkHermiticity(densityMatrix) || !checkPSD(densityMatrix)){
                throw new RuntimeException("Invalid density matrix that didn't filled up the conditions");}
        }
        else {
            throw new RuntimeException("The total probability doesn't sum to 1, no density matrix built");
        }
    }
    public DensityMatrix(Complex[][] densM) {
        if(validateDensityMatrix(densM) && checkHermiticity(densM) && checkPSD(densM)){
            this.densityMatrix = densM;
        }
        else {
            throw new RuntimeException("Invalid density matrix that didn't filled up the conditions");
        }
    }
    public DensityMatrix(Complex[][] vectorState, double[] vectorProbability) { // this is mixed state, so a pair or chain of different state with each a probability attached to it
        if(checkProb2(vectorProbability)){
            densityMatrix = buildDensityMatrix(vectorState, vectorProbability);
            if(!validateDensityMatrix(densityMatrix) || !checkHermiticity(densityMatrix) || !checkPSD(densityMatrix)){
                throw new RuntimeException("Invalid density matrix that didn't filled up the conditions");
            }
        }
        else {
            throw new RuntimeException("The total probability doesn't sum to 1, no density matrix built");
        }
    }

    private Complex[][] buildDensityMatrix(Complex[] vectorState) { //NEVER CALL FROM OUTSIDE, LET PRIVATE
        return outerProduct(vectorState, conjugateVector(vectorState));
    }
    private Complex[][] buildDensityMatrix(Complex[][] vectorState, double[] vectorProbability) { // building density matrix from mixed state.
        Complex[][] result = new Complex[vectorState[0].length][vectorState[0].length];

        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[i].length; j++)
                result[i][j] = new Complex(0, 0);

        for(int i = 0; i < vectorState.length; i++) {
            result = MathUtils.matrixAddition(MathUtils.scaleMatrix(buildDensityMatrix(vectorState[i]), vectorProbability[i]), result);
        }
        return result;
    }

    private boolean validateDensityMatrix(Complex[][] densityMatrix) {
        return Math.abs(MathUtils.getTrace(densityMatrix)-1) < epsilon;
    }

    private boolean checkHermiticity(Complex[][] densityMatrix) {
        return checkEquality(densityMatrix, MathUtils.transpose(MathUtils.conjugate(densityMatrix)));
    }

    private boolean checkEquality(Complex[][] densityMatrix, Complex[][] conjTransMatrix) {
        double rest = 0;

        for(int i = 0; i < densityMatrix.length; i++) {
            for(int j = 0; j < densityMatrix[i].length; j++) {
                rest = 0;
                rest += Math.abs(densityMatrix[i][j].getReal() - conjTransMatrix[i][j].getReal());
                rest += Math.abs(densityMatrix[i][j].getImag() - conjTransMatrix[i][j].getImag());
                if (rest > epsilon) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkPSD(Complex[][] densityMatrix) {
        double[] eigenValues = MathUtils.getEigenvalues(densityMatrix);

        for(int i = 0; i < eigenValues.length; i++) {
            if (eigenValues[i] < -epsilon) {
                return false;
            }
        }
        return true;
    }

    private Complex[] conjugateVector(Complex[] vectorState) {
        Complex[] conjugateMatrix = new Complex[vectorState.length];
        for(int i = 0; i < vectorState.length; i++){
            conjugateMatrix[i] = vectorState[i].conjugate();
        }
        return conjugateMatrix;
    }
    private Complex[][] outerProduct(Complex[] vectorState, Complex[] conjugate) {
        Complex[][] outerMatrix = new Complex[vectorState.length][vectorState.length];
        for(int i = 0; i < outerMatrix.length; i++){
            for(int j = 0; j < outerMatrix[i].length; j++){
                    outerMatrix[i][j] = vectorState[i].doMultiplication(conjugate[j]); // outer product
            }
        }
        return outerMatrix;
    }

    private boolean checkProb(Complex[] vectorState) {
        double sum = 0;
        for (Complex complex : vectorState) {
            sum += complex.getProb();
        }
        return Math.abs(sum-1)<epsilon;
    }
    private boolean checkProb2(double[] vectorState) {
        double sum = 0;
        for (double v : vectorState) {
            if (v < -epsilon) {
                return false;
            }
            sum += v;
        }
        return Math.abs(sum-1)<epsilon;
    }

    // CORE GETTERS
    public Complex[][] getDensityMatrix() {
        return densityMatrix.clone();
    }

}
