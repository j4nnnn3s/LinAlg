package linalg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static linalg.MathHelper.gcd;

/**
 * Class to do calculations with fractions
 */
public class Frac {

    private int numerator;
    private int denominator;
    private static final Pattern PATTERN = Pattern.compile("-?\\d+\\/-?\\d+\\s*", Pattern.CASE_INSENSITIVE);

    /**
     * Constructor for Frac via numerator and denominator
     * @param numerator Numerator of the fraction
     * @param denominator Denominator of the fraction
     */
    public Frac(int numerator, int denominator) {
        if (denominator == 0) throw new ArithmeticException("Division by zero");
        this.numerator = numerator;
        this.denominator = denominator;
        this.cancel();
    }

    /**
     * Constructor for Frac with one int. Because only one number is given, the denominator will be 1
     * @param numerator Numerator of the fraction
     */
    public Frac(int numerator) {
        this.numerator = numerator;
        denominator = 1;
    }

    /**
     * Constructor for Frac by parsing a String
     * @param fractionString String to be parsed
     */
    public Frac(String fractionString){
        Frac tempFrac = parseFrac(fractionString);
        numerator = tempFrac.numerator;
        denominator = tempFrac.denominator;
        if (denominator == 0) throw new ArithmeticException("Division by zero");
        this.cancel();
    }

    /**
     * Parser from a string to a Frac
     * @param s The string that will be passed
     * @return The frac object equivalent to the content of the string
     * @throws NumberFormatException if the string is not a valid fraction
     */
    public static Frac parseFrac(String s){
        Matcher matcher = PATTERN.matcher(s);
        if(!matcher.matches()){
            throw new NumberFormatException("Not a valid fraction");
        }
        String[] parts = s.split("/");
        return new Frac(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
    }

    /**
     * Function to get the numerator
     * @return Numerator of the fraction
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Function to get the denominator
     * @return Denominator of the fraction
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Finding the greatest common divisor of the fraction to reduce it as much as possible
     */
    public void cancel() {
        int gcd = gcd(this.numerator, this.denominator);
        numerator = numerator / gcd;
        denominator = denominator / gcd;
    }

    /**
     * Converting the fraction into a string
     * @return String equivalent ot the fraction
     */
    @Override
    public String toString() {
        return (isInteger())? String.valueOf(numerator) : String.format("%s/%s", numerator, denominator);
    }

    /**
     * Cloning the Frac
     * @return New object with the same content of the current Frac
     */
    @Override
    public Frac clone() {
        return new Frac(numerator, denominator);
    }

    /**
     * Checking if the fraction is an integer
     * @return True if the fraction is an integer
     */
    public boolean isInteger() {
        cancel();
        return denominator == 1;
    }

    /**
     * Reversing the fraction
     * @return The reversed/inverted fraction
     */
    public Frac invert() {
        if (numerator == 0) throw new ArithmeticException("Division by zero");
        int oldDenominator = denominator;
        denominator = numerator;
        numerator = oldDenominator;
        return this;
    }

    /**
     * Multiplying the fraction with another fraction
     * @param other A second fraction to multiply with
     * @return Result of the multiplication
     */
    public Frac mult(Frac other) {
        denominator *= other.denominator;
        numerator *= other.numerator;
        cancel();
        return this;
    }

    /**
     * Multiplying the fraction with an integer
     * @param number Integer to multiply with
     * @return Result of the multiplication
     */
    public Frac mult(int number) {
        return mult(new Frac(number));
    }

    /**
     * Dividing a fraction by another fraction
     * @param other A second fraction to divide by
     * @return Result of the division
     * @throws ArithmeticException if the second fraction is equal to 0
     */
    public Frac divide(Frac other) {
        if (other.numerator == 0) throw new ArithmeticException("Division by zero");
        Frac calcOther = new Frac(other.getNumerator(), other.getDenominator());
        mult(calcOther.invert());
        cancel();
        return this;
    }

    /**
     * Dividing a fraction by an integer
     * @param number Integer to divide by
     * @return Result of the division
     * @throws ArithmeticException if the integer is equal to 0
     */
    public Frac divide(int number) {
        if (number == 0) throw new ArithmeticException("Division by zero");
        return divide(new Frac(number));
    }

    /**
     * Adding another fraction to the fraction
     * @param other The fraction to add
     * @return Result of the addition
     */
    public Frac add(Frac other) {
        numerator = other.getDenominator() * numerator + other.getNumerator() * denominator;
        denominator = denominator * other.getDenominator();
        cancel();
        return this;
    }

    /**
     * Adding an integer to this fraction
     * @param number Integer to add
     * @return Result of the addition
     */
    public Frac add(int number) {
        return add(new Frac(number));
    }

    /**
     * Subtract a fraction from this fraction
     * @param other The fraction to subtract
     * @return Result of the subtraction
     */
    public Frac subtract(Frac other) {
        numerator = other.getDenominator() * numerator - other.getNumerator() * denominator;
        denominator = denominator * other.getDenominator();
        cancel();
        return this;
    }

    /**
     * Subtract an integer from this fraction
     * @param number Integer to subtract
     * @return Result of the subtraction
     */
    public Frac subtract(int number) {
        return subtract(new Frac(number));
    }

    /**
     * Calculate a decimal value close to the fraction
     * @return The decimal value
     */
    public double approx() {
        return ((double) numerator / (double) denominator);
    }
}
