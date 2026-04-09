package org.example;

public class QuantumState {
    public DensityMatrix densityMatrix;
    public double qBitsCount;
    private final double epsilon = 1e-10;
    private Gates gates;
    private NoiseChannels noiseChannels;

    public QuantumState(DensityMatrix densityMatrix, double qBitsCount) {
        this.densityMatrix = densityMatrix;
        this.qBitsCount = qBitsCount;
        this.gates = new Gates();
        this.noiseChannels = new NoiseChannels();
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
        double trace = MathUtils.getTrace(MathUtils.innerProductSameDimensions(densityMatrix.getDensityMatrix(), densityMatrix.getDensityMatrix()));// system is pure if equal to 1
        return Math.abs(trace-1)<epsilon;
    }
    public double getTrace(DensityMatrix densityMatrix){
        return MathUtils.getTrace(densityMatrix.getDensityMatrix());
    }

    // MEASUREMENT RELATED METHOD
    public double[] getProbabilities(DensityMatrix densityMatrix){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        double[] probabilities = new double[densityMatrix.getDensityMatrix().length];
        for (int i = 0; i < densMatrix.length; i++) {
            probabilities[i] = densMatrix[i][i].getReal();
        }
        return probabilities;
    }

    // EVOLUTION METHODS
    public DensityMatrix applyHGate(DensityMatrix densityMatrix, int QBitConcerned){
        return new DensityMatrix(gates.applyHadamard(densityMatrix.getDensityMatrix(), QBitConcerned), 2);
    }
    public DensityMatrix applyCNOT(DensityMatrix densityMatrix, int controlQBit, int targetQBit){
        return new DensityMatrix(gates.applyCNOT(densityMatrix.getDensityMatrix(), controlQBit, targetQBit), 2);
    }
    public DensityMatrix applyPauliX(DensityMatrix densityMatrix, int QBitConcerned){
        return new DensityMatrix(gates.applyPauliX(densityMatrix.getDensityMatrix(), QBitConcerned), 2);
    }
    public DensityMatrix applyPauliY(DensityMatrix densityMatrix, int QBitConcerned){
        return new DensityMatrix(gates.applyPauliY(densityMatrix.getDensityMatrix(), QBitConcerned), 2);
    }
    public DensityMatrix applyPauliZ(DensityMatrix densityMatrix, int QBitConcerned){
        return new DensityMatrix(gates.applyPauliZ(densityMatrix.getDensityMatrix(), QBitConcerned), 2);
    }

    // NOISE CHANNELS / DECOHERENCE
    public DensityMatrix bitFlip(DensityMatrix densityMatrix, int QBitConcerned, double noiseValue){
        return new DensityMatrix(noiseChannels.doBitFlip(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue), 2);
    }

    public DensityMatrix phaseFlip(DensityMatrix densityMatrix, int QBitConcerned, double noiseValue){
        return new DensityMatrix(noiseChannels.doPhaseFlip(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue), 2);
    }

    public DensityMatrix depolarizingChannel(DensityMatrix densityMatrix, int QBitConcerned, double noiseValue){
        return new DensityMatrix(noiseChannels.doDepolarizingChannel(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue), 2);
    }

    public DensityMatrix amplitudeDamping(DensityMatrix densityMatrix, int QBitConcerned, double dampingRate){
        return new DensityMatrix(noiseChannels.doAmplitudeDamping(densityMatrix.getDensityMatrix(), QBitConcerned, dampingRate), 2);
    }

    // COMPARISONS / FIDELITY
    public


}
