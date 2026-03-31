package org.example;

public class QuantumState {
    public DensityMatrix densityMatrix;
    public double qBitsCount;

    public QuantumState(DensityMatrix densityMatrix, double qBitsCount) {
        this.densityMatrix = densityMatrix;
        this.qBitsCount = qBitsCount;
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

    }

}
