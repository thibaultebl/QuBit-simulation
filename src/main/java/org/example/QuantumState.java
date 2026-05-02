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
    public boolean checkPurity(){ // measure the amount of coherence in the state
        double trace = MathUtils.getTrace(MathUtils.innerProductSameDimensions(densityMatrix.getDensityMatrix(), densityMatrix.getDensityMatrix()));// system is pure if equal to 1
        return Math.abs(trace-1)<epsilon;
    }
    public double getTrace(){
        return MathUtils.getTrace(densityMatrix.getDensityMatrix());
    }

    // MEASUREMENT RELATED METHOD
    public double[] getProbabilities(){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        double[] probabilities = new double[densityMatrix.getDensityMatrix().length];
        for (int i = 0; i < densMatrix.length; i++) {
            probabilities[i] = densMatrix[i][i].getReal();
        }
        return probabilities;
    }

    // EVOLUTION METHODS
    public DensityMatrix applyHGate(int QBitConcerned){
        return new DensityMatrix(gates.applyHadamard(densityMatrix.getDensityMatrix(), QBitConcerned));
    }
    public DensityMatrix applyCNOT(int controlQBit, int targetQBit){
        return new DensityMatrix(gates.applyCNOT(densityMatrix.getDensityMatrix(), controlQBit, targetQBit));
    }
    public DensityMatrix applyPauliX(int QBitConcerned){
        return new DensityMatrix(gates.applyPauliX(densityMatrix.getDensityMatrix(), QBitConcerned));
    }
    public DensityMatrix applyPauliY(int QBitConcerned){
        return new DensityMatrix(gates.applyPauliY(densityMatrix.getDensityMatrix(), QBitConcerned));
    }
    public DensityMatrix applyPauliZ(int QBitConcerned){
        return new DensityMatrix(gates.applyPauliZ(densityMatrix.getDensityMatrix(), QBitConcerned));
    }

    // NOISE CHANNELS / DECOHERENCE
    public DensityMatrix bitFlip(int QBitConcerned, double noiseValue){
        return new DensityMatrix(noiseChannels.doBitFlip(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue));
    }

    public DensityMatrix phaseFlip(int QBitConcerned, double noiseValue){
        return new DensityMatrix(noiseChannels.doPhaseFlip(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue));
    }

    public DensityMatrix depolarizingChannel(int QBitConcerned, double noiseValue){
        return new DensityMatrix(noiseChannels.doDepolarizingChannel(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue));
    }

    public DensityMatrix amplitudeDamping(int QBitConcerned, double dampingRate){
        return new DensityMatrix(noiseChannels.doAmplitudeDamping(densityMatrix.getDensityMatrix(), QBitConcerned, dampingRate));
    }

    // COMPARISONS / FIDELITY
    public double calculateFidelity(DensityMatrix y){
        return MathUtils.fidelity(densityMatrix.getDensityMatrix(), y.getDensityMatrix());
    }


}
