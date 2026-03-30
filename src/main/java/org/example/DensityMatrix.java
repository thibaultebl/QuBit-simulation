package org.example;

public class DensityMatrix {
    private Complex[][] densityMatrix;
    private double dimensions;
    private final double epsilon = 1e-10;

    public DensityMatrix(Complex[] vectorState, double d) {
        if(checkProb(vectorState)){

        }
        else {
            System.out.println("The total probability doesn't sum to 1, no density matrix built");
        }
    }
    public DensityMatrix(Complex[][] mixtureState, double d) {

    }






    public Complex[][] buildDensityMatrix(Complex[] vectorState) {
    }

    public boolean validateDensityMatrix() {
        return true;
    }







    private boolean checkProb(Complex[] vectorState) {
        double sum = 0;
        for(int i = 0; i < vectorState.length; i++) {
            sum += vectorState[i].modulus() * vectorState[i].modulus();
        }
        return Math.abs(sum-1)<epsilon;
    }

}
