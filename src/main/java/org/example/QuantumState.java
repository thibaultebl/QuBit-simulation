package org.example;

public class QuantumState {
    public DensityMatrix densityMatrix;
    public double qBitsCount;
    private final double epsilon = 1e-10;
    private Gates gates;

    public QuantumState(DensityMatrix densityMatrix, double qBitsCount) {
        this.densityMatrix = densityMatrix;
        this.qBitsCount = qBitsCount;
        this.gates = new Gates();
    }

    // CORE GETTERS
    public double getQBitsCount() {
        return qBitsCount;
    }
    public double getDimension(){
        return Math.pow(2, qBitsCount);
    }
    public DensityMatrix getDensityMatrix() {
        return densityMatrix;
    }

    // STATE DIAGNOSTICS
    public boolean checkPurity(DensityMatrix densityMatrix){ // measure the amount of coherence in the state
        Complex[][] result = MathUtils.innerProductSameDimensions(densityMatrix.getDensityMatrix(), densityMatrix.getDensityMatrix());
        double trace = MathUtils.getTrace(result);// system is pure if equal to 1
        return Math.abs(trace-1)<epsilon;
    }
    public double getTrace(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        return MathUtils.getTrace(densMatrix);
    }

    // MEASUREMENT RELATED METHOD
    public double[] getProbabilities(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        double[] probabilities = new double[densMatrix.length];
        for (int i = 0; i < densMatrix.length; i++) {
            probabilities[i] = densMatrix[i][i].getReal();
        }
        return probabilities;
    }

    // EVOLUTION METHODS
    public DensityMatrix applyHGate(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        Complex[][] gated = gates.applyHadamard(densMatrix, 0);
        DensityMatrix result = new DensityMatrix(gated, 2); // temporary d value
        return result;
    }
    public DensityMatrix applyCNOT(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        Complex[][] gated = gates.applyCNOT(densMatrix, 0, 1);
        DensityMatrix result = new DensityMatrix(gated, 2);
        return result;
    }
    public DensityMatrix applyPauliX(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        Complex[][] gated = gates.applyPauliX(densMatrix, 0);
        DensityMatrix result = new DensityMatrix(gated, 2);
        return result;
    }
    public DensityMatrix applyPauliY(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        Complex[][] gated = gates.applyPauliY(densMatrix, 0);
        DensityMatrix result = new DensityMatrix(gated, 2);
        return result;
    }
    public DensityMatrix applyPauliZ(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        Complex[][] gated = gates.applyPauliZ(densMatrix, 0);
        DensityMatrix result = new DensityMatrix(gated, 2);
        return result;
    }





}
