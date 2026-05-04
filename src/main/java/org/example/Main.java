package org.example;

import org.example.Component.DensityMatrix;
import org.example.Interface.QuantumState;
import org.example.Math.Complex;

public class Main {

    public static void main(String[] args) {

        Factory factory = new Factory();
        Complex[] system = factory.setGroundState(20);

        DensityMatrix densityMatrix = new DensityMatrix(system);
        QuantumState GlobalState = new QuantumState(densityMatrix, Integer.numberOfTrailingZeros(densityMatrix.getDensityMatrix()[0].length));
        GlobalState.checkPurity();
        GlobalState.applyHGate(2);
        GlobalState.applyHGate(7);
        GlobalState.applyHGate(4);
        GlobalState.checkPurity();

    }
}
