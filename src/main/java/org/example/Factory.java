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
}
