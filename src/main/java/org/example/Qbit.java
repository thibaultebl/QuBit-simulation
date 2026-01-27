package org.example;

import static java.text.Normalizer.normalize;

public class Qbit {
    public double realAlpha;
    public double imagAlpha;
    public double realBeta;
    public double imagBeta;

    public Qbit() {
        this.realAlpha = 1.0;
        this.imagAlpha = 0.0;
        this.realBeta = 0.0;
        this.imagBeta = 0.0;
    }
    public int measure() {
        double p0 = getAlphaSquared();
        double rand = Math.random();

        if(rand < p0){
            collapse(0);
            return 0;
        } else {
            collapse(1);
            return 1;
        }
    }
    private void collapse(int outcome) {
        if (outcome == 0) {
            realAlpha = 1.0;
            imagAlpha = 0.0;
            realBeta  = 0.0;
            imagBeta  = 0.0;
        } else {
            realAlpha = 0.0;
            imagAlpha = 0.0;
            realBeta  = 1.0;
            imagBeta  = 0.0;
        }
    }
    public double getAlphaSquared() {
        return realAlpha * realAlpha + imagAlpha * imagAlpha;
    }
    public double getBetaSquared() {
        return realBeta * realBeta + imagBeta * imagBeta;
    }
    public double getRealAlpha() {
        return realAlpha;
    }
    public double getImagAlpha() {
        return imagAlpha;
    }
    public double getRealBeta() {
        return realBeta;
    }
    public double getImagBeta() {
        return imagBeta;
    }
    public void setAlpha(double real, double imag) {
        this.realAlpha = real;
        this.imagAlpha = imag;
    }
    public void setBeta(double real, double imag) {
        this.realBeta = real;
        this.imagBeta = imag;
    }


}