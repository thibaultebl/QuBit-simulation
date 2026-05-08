package org.example.Gates;

import org.example.Math.Complex;
import org.example.Math.MathUtils;

public class Gates {
    private final Complex[][] hadamardGate;
    private final Complex[][] identityGate;
    private final Complex[][] cNotGate;
    private final Complex[][] cNotGateInversed;
    private final Complex[][] pauliXGate;
    private final Complex[][] pauliYGate;
    private final Complex[][] pauliZGate;
    private final Complex[][] swapGate;
    private final UnitaryInterface unitaryInterface;

    public Gates() {
        hadamardGate = MathUtils.scaleMatrix(new Complex[][]{
                {new Complex(1, 0), new Complex(1, 0)},
                {new Complex(1, 0), new Complex(-1, 0)}
        }, 1.0 / Math.sqrt(2));
        identityGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(1, 0)}
        };
        cNotGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(1, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(0, 0), new Complex(1, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(1, 0), new Complex(0, 0)},
        };
        cNotGateInversed = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(0, 0), new Complex(1, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(1, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(1, 0), new Complex(0, 0), new Complex(0, 0)},
        };
        pauliXGate = new Complex[][]{
                {new Complex(0, 0), new Complex(1, 0)},
                {new Complex(1, 0), new Complex(0, 0)},
        };
        pauliYGate = new Complex[][]{
                {new Complex(0, 0), new Complex(0, -1)},
                {new Complex(0, 1), new Complex(0, 0)},
        };
        pauliZGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(-1, 0)},
        };
        swapGate = new Complex[][]{
                {new Complex(1, 0), new Complex(0, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(1, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(1, 0), new Complex(0, 0), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(0, 0), new Complex(0, 0), new Complex(1, 0)},
        };
        unitaryInterface = new UnitaryMatrix();
    }

    public Complex[][] applyHadamard(Complex[][] input, int qBitConcerned) {
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(hadamardGate, identityGate, qBitConcerned, (Integer.numberOfTrailingZeros(input[0].length)));
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, input), MathUtils.transpose(MathUtils.conjugate(unitaryFull))); // hadamard coefficient
    }

    public Complex[][] applyCNOT(Complex[][] input, int controlQBit, int targetQBit) {
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        int distance = Math.abs(controlQBit - targetQBit);
        Complex[][] output = input;

        if (distance > 1) {
            int current = controlQBit;
            int direction = controlQBit < targetQBit ? 1 : -1;

            for (int i = 0; i < distance - 1; i++) {
                int next = current + direction;
                output = applySwap(output, Math.min(current, next), Math.max(current, next));
                current = next;
            }

            Complex[][] unitaryFull;
            if (current < targetQBit) {
                unitaryFull = unitaryInterface.computeUnitaryFull(cNotGate, identityGate, current, targetQBit, totalQBits);
            } else {
                unitaryFull = unitaryInterface.computeUnitaryFull(cNotGateInversed, identityGate, current, targetQBit, totalQBits);
            }
            Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
            output = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, output), MathUtils.transpose(unitaryFullConjugate));

            for (int i = 0; i < distance - 1; i++) {
                current = current - direction;
                int next = current + direction;
                output = applySwap(output, Math.min(current, next), Math.max(current, next));
            }

        } else {
            Complex[][] unitaryFull;
            if (controlQBit < targetQBit) {
                unitaryFull = unitaryInterface.computeUnitaryFull(cNotGate, identityGate, controlQBit, targetQBit, totalQBits);
            } else {
                unitaryFull = unitaryInterface.computeUnitaryFull(cNotGateInversed, identityGate, controlQBit, targetQBit, totalQBits);
            }
            Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
            output = MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, output), MathUtils.transpose(unitaryFullConjugate));
        }

        return output;
    }

    private Complex[][] applySwap(Complex[][] input, int a, int b) {
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(swapGate, identityGate, a, b, totalQBits);
        Complex[][] unitaryFullConjugate = MathUtils.conjugate(unitaryFull);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, input), MathUtils.transpose(unitaryFullConjugate));
    }

    // Pauli gate are kinda hard coded, a generic method should be done later
    public Complex[][] applyPauliX(Complex[][] input, int qBitConcerned) {
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(pauliXGate, identityGate, qBitConcerned, totalQBits);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, input), MathUtils.transpose(MathUtils.conjugate(unitaryFull)));
    }

    public Complex[][] applyPauliY(Complex[][] input, int qBitConcerned) {
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(pauliYGate, identityGate, qBitConcerned, totalQBits);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, input), MathUtils.transpose(MathUtils.conjugate(unitaryFull)));
    }
    public Complex[][] applyPauliZ(Complex[][] input, int qBitConcerned) {
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] unitaryFull = unitaryInterface.computeUnitaryFull(pauliZGate, identityGate, qBitConcerned, totalQBits);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(unitaryFull, input), MathUtils.transpose(MathUtils.conjugate(unitaryFull)));
    }

    public Complex[][] applyRx(Complex[][] input, int qBitConcerned, double theta) {
        double c = Math.cos(theta / 2.0);
        double s = Math.sin(theta / 2.0);
        Complex[][] rx = new Complex[][]{
                {new Complex(c, 0), new Complex(0, -s)},
                {new Complex(0, -s), new Complex(c, 0)},
        };
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] U = unitaryInterface.computeUnitaryFull(rx, identityGate, qBitConcerned, totalQBits);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(U, input), MathUtils.transpose(MathUtils.conjugate(U)));
    }

    public Complex[][] applyRy(Complex[][] input, int qBitConcerned, double theta) {
        double c = Math.cos(theta / 2.0);
        double s = Math.sin(theta / 2.0);
        Complex[][] ry = new Complex[][]{
                {new Complex(c, 0), new Complex(-s, 0)},
                {new Complex(s, 0), new Complex(c, 0)},
        };
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] U = unitaryInterface.computeUnitaryFull(ry, identityGate, qBitConcerned, totalQBits);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(U, input), MathUtils.transpose(MathUtils.conjugate(U)));
    }

    public Complex[][] applyRz(Complex[][] input, int qBitConcerned, double theta) {
        double c = Math.cos(theta / 2.0);
        double s = Math.sin(theta / 2.0);
        Complex[][] rz = new Complex[][]{
                {new Complex(c, -s), new Complex(0, 0)},
                {new Complex(0, 0), new Complex(c, s)},
        };
        int totalQBits = Integer.numberOfTrailingZeros(input[0].length);
        Complex[][] U = unitaryInterface.computeUnitaryFull(rz, identityGate, qBitConcerned, totalQBits);
        return MathUtils.innerProductSameDimensions(MathUtils.innerProductSameDimensions(U, input), MathUtils.transpose(MathUtils.conjugate(U)));
    }

}
