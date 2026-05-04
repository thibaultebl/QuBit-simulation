package org.example.Noise;

import org.example.Math.Complex;
import org.example.Math.MathUtils;

public class KrausOperator {

    public Complex[][] computeKrausMatrix(Complex[][] K0Matrix, Complex[][] identityMatrix, int qBitConcerned, int totalQBit) {
        Complex[][] K0Full;

        if(qBitConcerned == 0) {
            K0Full = K0Matrix;
        } else {
            K0Full = identityMatrix;
        }

        for(int q = 1; q < totalQBit; q++) {
            if(qBitConcerned == q) {
                K0Full = MathUtils.kroneckerProduct(K0Full, K0Matrix);
            } else {
                K0Full = MathUtils.kroneckerProduct(K0Full, identityMatrix);
            }

        }
        return K0Full;
    }
    public Complex[][] getK04Damping(double dampingFactor){
        return new Complex[][]{
                {new Complex(1,0), new Complex(0,0)},
                {new Complex(0,0), new Complex(Math.sqrt(1-dampingFactor),0)},
        };
    }
    public Complex[][] getK14Damping(double dampingFactor){
        return new Complex[][]{
                {new Complex(0,0), new Complex(Math.sqrt(dampingFactor),0)},
                {new Complex(0,0), new Complex(0,0)},
        };
    }
}
