package org.example.Interface;

import org.example.Math.Complex;
import org.example.Component.DensityMatrix;
import org.example.Gates.Gates;
import org.example.Math.MathUtils;
import org.example.Noise.NoiseChannels;

public class QuantumState {
    private DensityMatrix densityMatrix;
    private final int qBitsCount;
    private final double epsilon = 1e-10;
    private final Gates gates;
    private final NoiseChannels noiseChannels;

    public QuantumState(DensityMatrix densityMatrix, int qBitsCount) {
        this.densityMatrix = densityMatrix;
        this.qBitsCount = qBitsCount;
        this.gates = new Gates();
        this.noiseChannels = new NoiseChannels();
    }

    // CORE GETTERS
    public int getQBitsCount() {
        return qBitsCount;
    }
    public int getDimension(){
        return (int) Math.pow(2, qBitsCount);
    }
    public DensityMatrix getDensityMatrix() {
        return densityMatrix;
    }

    // STATE DIAGNOSTICS
    public double getPurity(){ // measure the amount of coherence in the state
        return MathUtils.getTrace(MathUtils.innerProductSameDimensions(densityMatrix.getDensityMatrix(), densityMatrix.getDensityMatrix()));// system is pure if equal to 1
    }

    public double getEntropy(){
        double[] eigenValues = MathUtils.getEigenvalues(densityMatrix.getDensityMatrix());
        double sum = 0;

        for (double eigenValue : eigenValues) {
            if (eigenValue > epsilon) {
                sum += eigenValue * (Math.log(eigenValue) / Math.log(2));
            }
        }
        return -sum;
    }

    public double getTrace(){
        return MathUtils.getTrace(densityMatrix.getDensityMatrix());
    }

    // MEASUREMENT RELATED METHOD
    public double[] getProbabilities(){
        Complex[][] densMatrix = densityMatrix.getDensityMatrix();
        double[] probabilities = new double[densMatrix.length];
        for (int i = 0; i < densMatrix.length; i++) {
            probabilities[i] = densMatrix[i][i].getReal();
        }
        return probabilities;
    }

    // EVOLUTION METHODS
    public void applyHGate(int QBitConcerned){
        this.densityMatrix = new DensityMatrix(gates.applyHadamard(densityMatrix.getDensityMatrix(), QBitConcerned));
    }
    public void applyCNOT(int controlQBit, int targetQBit){
        this.densityMatrix = new DensityMatrix(gates.applyCNOT(densityMatrix.getDensityMatrix(), controlQBit, targetQBit));
    }
    public void applyPauliX(int QBitConcerned){
        this.densityMatrix = new DensityMatrix(gates.applyPauliX(densityMatrix.getDensityMatrix(), QBitConcerned));
    }
    public void applyPauliY(int QBitConcerned){
        this.densityMatrix = new DensityMatrix(gates.applyPauliY(densityMatrix.getDensityMatrix(), QBitConcerned));
    }
    public void applyPauliZ(int QBitConcerned){
        this.densityMatrix = new DensityMatrix(gates.applyPauliZ(densityMatrix.getDensityMatrix(), QBitConcerned));
    }

    // NOISE CHANNELS / DECOHERENCE
    public void bitFlip(int QBitConcerned, double noiseValue){
        this.densityMatrix = new DensityMatrix(noiseChannels.doBitFlip(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue));
    }

    public void phaseFlip(int QBitConcerned, double noiseValue){
        this.densityMatrix = new DensityMatrix(noiseChannels.doPhaseFlip(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue));
    }

    public void depolarizingChannel(int QBitConcerned, double noiseValue){
        this.densityMatrix = new DensityMatrix(noiseChannels.doDepolarizingChannel(densityMatrix.getDensityMatrix(), QBitConcerned, noiseValue));
    }

    public void amplitudeDamping(int QBitConcerned, double dampingRate){
        this.densityMatrix = new DensityMatrix(noiseChannels.doAmplitudeDamping(densityMatrix.getDensityMatrix(), QBitConcerned, dampingRate));
    }

    // COMPARISONS / FIDELITY
    public double calculateFidelity(DensityMatrix y){
        return MathUtils.fidelity(densityMatrix.getDensityMatrix(), y.getDensityMatrix());
    }

}
