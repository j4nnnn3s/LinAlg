package linalg;

import static linalg.MathHelper.gcd;

public class Frac {
    private int numerator;
    private int denominator;

    public Frac(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.cancel();
    }

    public Frac(int numerator) {
        this.numerator = numerator;
        denominator = 1;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void cancel() {
        int gcd = gcd(this.numerator, this.denominator);
        numerator = numerator / gcd;
        denominator = denominator / gcd;
    }

    @Override
    public String toString() {
        return (isInteger())? String.valueOf(numerator) : String.format("%s/%s", numerator, denominator);
    }

    public boolean isInteger() {
        cancel();
        return denominator == 1;
    }

    public Frac invert() {
        int oldDenominator = denominator;
        denominator = numerator;
        numerator = oldDenominator;
        return this;
    }

    public Frac mult(Frac other) {
        denominator *= other.denominator;
        numerator *= other.numerator;
        cancel();
        return this;
    }

    public Frac mult(int number) {
        return mult(new Frac(number));
    }

    public Frac divide(Frac other) {
        Frac calcOther = new Frac(other.getNumerator(), other.getDenominator());
        mult(calcOther.invert());
        cancel();
        return this;
    }

    public Frac divide(int number) {
        return divide(new Frac(number));
    }

    public Frac add(Frac other) {
        numerator = other.getDenominator() * numerator + other.getNumerator() * denominator;
        denominator = denominator * other.getDenominator();
        cancel();
        return this;
    }

    public Frac add(int number) {
        return add(new Frac(number));
    }

    public Frac subtract(Frac other) {
        numerator = other.getDenominator() * numerator - other.getNumerator() * denominator;
        denominator = denominator * other.getDenominator();
        cancel();
        return this;
    }

    public Frac subtract(int number) {
        return subtract(new Frac(number));
    }

    public double approx() {
        return ((double) numerator / (double) denominator);
    }
}
