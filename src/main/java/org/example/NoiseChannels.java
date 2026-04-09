package org.example;

public class NoiseChannels {
    private Complex[][] identityMatrix;
    private Complex[][] xMatrix;
    private Complex[][] zMatrix;
    private Complex[][] yMatrix;
    private KrausOperator operator;
    public NoiseChannels() {
        identityMatrix = new Complex[][]{
                {new Complex(1,0), new Complex(0, 0)},
                {new Complex(0,0), new Complex(1, 0)},
        };
        xMatrix = new Complex[][]{
                {new Complex(0,0), new Complex(1, 0)},
                {new Complex(1,0), new Complex(0, 0)},
        };
        zMatrix = new Complex[][]{
                {new Complex(1,0), new Complex(0, 0)},
                {new Complex(0,0), new Complex(-1, 0)},
        };
        yMatrix = new Complex[][]{
                {new Complex(0,0), new Complex(0, -1)},
                {new Complex(0,1), new Complex(0, 0)},
        };

    }

    public Complex[][] doBitFlip(Complex[][] input, int QBitConcerned, double noiseValue) {

        Complex[][] K0Full =
                operator.computeKrausMatrix(k0(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K1Full =
                operator.computeKrausMatrix(k1BitFlip(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] firstTerm =
                MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K0Full, input ), MathUtils.transpose(MathUtils.conjugate(K0Full)));

        Complex[][] secondTerm =
                MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K1Full, input ), MathUtils.transpose(MathUtils.conjugate(K1Full)));

        return MathUtils.matrixAddition(firstTerm, secondTerm);
    }

    private Complex[][] k0(double noiseValue) {
        return MathUtils.scaleMatrix(identityMatrix, Math.sqrt(1-noiseValue));
    }

    private Complex[][] k1BitFlip(double noiseValue) {
        return MathUtils.scaleMatrix(xMatrix, Math.sqrt(noiseValue));
    }
    private Complex[][] k1PhaseFlip(double noiseValue) {
        return MathUtils.scaleMatrix(zMatrix, Math.sqrt(noiseValue));
    }

    public Complex[][] doPhaseFlip(Complex[][] input, int QBitConcerned, double noiseValue) {
        Complex[][] K0Full =
                operator.computeKrausMatrix(k0(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K1Full =
                operator.computeKrausMatrix(k1PhaseFlip(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] firstTerm =
                MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K0Full, input ), MathUtils.transpose(MathUtils.conjugate(K0Full)));

        Complex[][] secondTerm =
                MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K1Full, input ), MathUtils.transpose(MathUtils.conjugate(K1Full)));

        return MathUtils.matrixAddition(firstTerm, secondTerm);
    }

    public Complex[][] doDepolarizingChannel(Complex[][] input, int QBitConcerned, double noiseValue) {
        Complex[][] K0Full =
                operator.computeKrausMatrix(k0Depolarizing(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K1Full =
                operator.computeKrausMatrix(k1Depolarizing(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K2Full =
                operator.computeKrausMatrix(k2Depolarizing(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K3Full =
                operator.computeKrausMatrix(k3Depolarizing(noiseValue), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K0FullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(K0Full));
        Complex[][] K1FullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(K1Full));
        Complex[][] K2FullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(K2Full));
        Complex[][] K3FullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(K3Full));

        Complex[][] firstTerm = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K0Full, input), K0FullConjugateTranspose);
        Complex[][] secondTerm = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K1Full, input), K1FullConjugateTranspose);
        Complex[][] thirdTerm = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K2Full, input), K2FullConjugateTranspose);
        Complex[][] fourthTerm = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K3Full, input), K3FullConjugateTranspose);

        return MathUtils.matrixAddition(MathUtils.matrixAddition(firstTerm, secondTerm), MathUtils.matrixAddition(thirdTerm, fourthTerm));
    }

    public Complex[][] doAmplitudeDamping(Complex[][] input, int QBitConcerned, double dampingFactor) {
        Complex[][] K0Full = operator.computeKrausMatrix(operator.getK04Damping(dampingFactor), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));
        Complex[][] K1Full = operator.computeKrausMatrix(operator.getK14Damping(dampingFactor), identityMatrix, QBitConcerned, (int) (Math.log(input[0].length) / Math.log(2)));

        Complex[][] K0FullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(K0Full));
        Complex[][] K1FullConjugateTranspose = MathUtils.transpose(MathUtils.conjugate(K1Full));

        Complex[][] firstTerm = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K0Full, input), K0FullConjugateTranspose);
        Complex[][] secondTerm = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(K1Full, input), K1FullConjugateTranspose);

        return MathUtils.matrixAddition(firstTerm, secondTerm);
    }


    private Complex[][] k0Depolarizing(double noiseValue) {
        return MathUtils.scaleMatrix(identityMatrix, Math.sqrt(1-((3*noiseValue)/4)));
    }
    private Complex[][] k1Depolarizing(double noiseValue) {
        return MathUtils.scaleMatrix(xMatrix, Math.sqrt(noiseValue/4));
    }
    private Complex[][] k2Depolarizing(double noiseValue) {
        return MathUtils.scaleMatrix(yMatrix, Math.sqrt(noiseValue/4));
    }
    private Complex[][] k3Depolarizing(double noiseValue) {
        return MathUtils.scaleMatrix(zMatrix, Math.sqrt(noiseValue/4));
    }

}
