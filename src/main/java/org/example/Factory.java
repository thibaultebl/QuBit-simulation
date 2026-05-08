package org.example;

import org.example.Math.Complex;

public class Factory {

    public Factory() {}

    public Complex[] setGroundState(int numQBit){
        Complex[] groundState = new Complex[(int) Math.pow(2, numQBit)];
        groundState[0] = new Complex(1, 0);
        for(int i = 1; i < groundState.length; i++){
            groundState[i] = new Complex(0,0);
        }
        return groundState;
    }

    public Complex[] setGHZState(int numQBit){
        int dim = (int) Math.pow(2, numQBit);
        Complex[] ghz = new Complex[dim];
        double amp = 1.0 / Math.sqrt(2);
        for(int i = 0; i < dim; i++) ghz[i] = new Complex(0, 0);
        ghz[0] = new Complex(amp, 0);
        ghz[dim - 1] = new Complex(amp, 0);
        return ghz;
    }
}
