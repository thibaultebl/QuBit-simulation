package org.example;

public class DensityMatrix {
    private Complex[][] densityMatrix;
    private final double epsilon = 1e-10;

    public DensityMatrix(Complex[] vectorState, double d) {
        if(checkProb(vectorState)){
            buildDensityMatrix(vectorState);
            if(!validateDensityMatrix(densityMatrix)){throw new RuntimeException("Invalid density matrix, the trace doesn't sum to 1");}
        }
        else {
            System.out.println("The total probability doesn't sum to 1, no density matrix built");
        }
    }
    public DensityMatrix(Complex[][] mixtureState, double d) {}

    private void buildDensityMatrix(Complex[] vectorState) { // NEVER CALL FROM OUTSIDE, LET PRIVATE
        Complex[] conjugateMatrix = conjugateVector(vectorState);
        densityMatrix = outerProduct(vectorState, conjugateMatrix);
    }

    private boolean validateDensityMatrix(Complex[][] densityMatrix) { // check trace is equal to 1 for now, many others checking are required tho
        double sum = 0;
        for(int i = 0; i < densityMatrix.length; i++) {
            sum += densityMatrix[i][i].getReal();
        }
        return Math.abs(sum-1)<epsilon;
    }
    public double getTrace(Complex[][] densityMatrix) {
        double sum = 0;
        for(int i = 0; i < densityMatrix.length; i++) {
            sum += densityMatrix[i][i].getReal();
        }
        return sum;
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
                    outerMatrix[i][j] = vectorState[i].doMultiplication(conjugate[j]);
            }
        }
        return outerMatrix;
    }

    private boolean checkProb(Complex[] vectorState) {
        double sum = 0;
        for(int i = 0; i < vectorState.length; i++) {
            sum += vectorState[i].modulusNoSquared();
        }
        return Math.abs(sum-1)<epsilon;
    }
    // CORE GETTERS
    public Complex[][] getDensityMatrix() {
        return densityMatrix;
    }

}
