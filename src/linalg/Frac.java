package linalg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static linalg.MathHelper.gcd;

public class Frac {
    private int numerator;
    private int denominator;
    private static final Pattern PATTERN = Pattern.compile("-?\\d+\\/-?\\d+\\s*", Pattern.CASE_INSENSITIVE);

    public Frac(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.cancel();
    }

    public Frac(int numerator) {
        this.numerator = numerator;
        denominator = 1;
    }

    public Frac(String fractionString){
        Frac tempFrac = parseFrac(fractionString);
        numerator = tempFrac.numerator;
        denominator = tempFrac.denominator;
        this.cancel();
    }

    public static Frac parseFrac(String s){
        Matcher matcher = PATTERN.matcher(s);
        if(!matcher.matches()){
            throw new NumberFormatException("Not a valid fraction");
        }
        String[] parts = s.split("/");
        return new Frac(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
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

    @Override
    public Frac clone() {
        return new Frac(numerator, denominator);
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

    public Frac add(int number) {
        return add(new Frac(number));
    }

    public Frac add(Frac other) {
        numerator = other.getDenominator() * numerator + other.getNumerator() * denominator;
        denominator = denominator * other.getDenominator();
        cancel();
        return this;
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
