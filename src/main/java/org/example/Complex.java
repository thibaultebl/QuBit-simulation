package org.example;

public class Complex {
    private double real;
    private double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    public Complex doAddition(Complex y) {
        Complex result = new Complex(real + y.real, imag + y.imag);
        return result;
    }
    public Complex doSubstraction(Complex y) {
        Complex result = new Complex(real - y.real, imag - y.imag);
        return result;
    }
    public Complex doMultiplication(Complex y) {
        Complex result = new Complex(real*y.real - imag*y.imag, real*y.imag + imag*y.real);
        return result;
    }
    public Complex conjugate(){
        Complex result = new Complex(real, -imag);
        return result;
    }
    public double modulus() {
        double result = real * real + imag * imag;
        return Math.sqrt(result);
    }
    public double getProb() {
        double result = real * real + imag * imag;
        return result;
    }

    public double getReal() {
        return real;
    }
    public double getImag() {
        return imag;
    }
    public void setReal(double real) {
        this.real = real;
    }
    public void setImag(double imag) {
        this.imag = imag;
    }

    @Override
    public String toString() {
        return getReal() + " + " + getImag() + "i";
    }
}
