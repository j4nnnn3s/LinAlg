package linalg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class, holding a symbol and the affiliated power and factor
 */
public class SimpleSymbol {

    private char symbol;
    private SmartNum factor;
    private SmartNum power;

    // TODO: Fix that *[a-z][A-Z] without number in the beginning isn't allowed
    private static final Pattern PATTERN = Pattern.compile(
            "(-?\\d*((\\.?|\\/?)\\d*)?\\s*\\*?\\s*)(?:[a-z]|[A-Z])(?:\\s*\\^?-?\\d*((\\.?|\\/?)\\d*)?\\s*)"
    );

    /**
     * Constructor for the SimpleSymbol with factor and symbol. Power will automatically be set to 1
     * @param factor Factor of the SimpleSymbol
     * @param symbol Character of the SimpleSymbol
     */
    public SimpleSymbol(SmartNum factor, char symbol) {
        this.factor = factor;
        this.symbol = symbol;
        this.power = new SmartNum(1);
    }

    /**
     * Constructor for the SimpleSymbol, where all properties are set manual
     * @param factor Factor of the SimpleSymbol
     * @param symbol Character of the SimpleSymbol
     * @param power Power of the SimpleSymbol
     */
    public SimpleSymbol(SmartNum factor, char symbol, SmartNum power) {
        this.factor = factor;
        this.symbol = symbol;
        this.power = power;
    }

    /**
     * Constructing a SimpleSymbol by parsing a string
     * @param s String to be parsed
     */
    public SimpleSymbol(String s) {
        parseSimpleSymbol(s);
    }

    /**
     * Get the character of the symbol
     * @return Character of the symbol
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Get the factor of the symbol
     * @return Factor of the symbol
     */
    public SmartNum getFactor() {
        return factor;
    }

    /**
     * Get the power of the symbol
     * @return Power of the symbol
     */
    public SmartNum getPower() {
        return power;
    }

    /**
     * Function that converts a string of on of the following spellings into a SimpleSymbol
     * "[SmartNumber] * a"
     * "[SmartNumber] * a ^ [SmartNumber]"
     * " a ^ [SmartNumber]"
     * "a"
     * @param s String to be parsed
     * @throws NumberFormatException When the string is not a valid SimpleSymbol
     */
    private void parseSimpleSymbol(String s) {
        s = s.replace(" ", "");
        if (!validSimpleSymbol(s)) {
            throw new NumberFormatException("Not a valid symbol");
        }
        if (s.contains("*")) s = s.replace("*", "");
        for (String element : s.split("")) {
            if (element.matches("[a-z]|[A-Z]")) {
                symbol = element.charAt(0);
                s = s.replace(String.valueOf(symbol), "");
                break;
            }
        }
        s = s.replace(String.valueOf(symbol), "");
        if (s.isEmpty()) {
            factor = new SmartNum(1);
            power = new SmartNum(1);
        } else if (s.contains("^")) {
            String elements[] = s.split("\\^");
            factor = new SmartNum(elements[0]);
            power = new SmartNum(elements[1]);
        } else {
            factor = new SmartNum(s);
            power = new SmartNum(1);
        }
    }

    /**
     * Function checks, if a string is a vaild SimpleSymbol and can be parsed
     * @param s String to check
     * @return Boolean, weather string can be parsed or not
     */
    public boolean validSimpleSymbol(String s) {
        return PATTERN.matcher(s).matches();
    }

    /**
     * Add two SimpleSymbols together. This is only possible, when both SimpleSymbols have the same symbol and is
     * accomplished by adding the two factors.
     * @param other SimpleSymbol to be added
     * @return Result of the addition
     * @throws ArithmeticException When both SimpleSymbols don't have the same symbol
     */
    public SimpleSymbol add(SimpleSymbol other) {
        if (other.symbol == symbol && other.power == power) {
            factor = factor.add(other.factor.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    /**
     * Subtract a SimpleSymbol from the current one. This is only possible, when both SimpleSymbols have the same symbol
     * and is accomplished by subtracting the other factor from the one of this factor.
     * @param other SimpleSymbol to subtract from this one.
     * @return Result of the subtraction
     * @throws ArithmeticException When both SimpleSymbols don't have the same symbol
     */
    public SimpleSymbol subtract(SimpleSymbol other) {
        if (other.symbol == symbol && other.power == power) {
            factor = factor.subtract(other.factor.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    /**
     * Multiply two SimpleSymbols. This is only possible, when both SimpleSymbols have the same symbol. This is accomplished
     * by multiplying the factors and adding the powers.
     * @param other SimpleSymbol to multiply with.
     * @return Result of the multiplication
     * @throws ArithmeticException When both SimpleSymbols don't have the same symbol
     */
    public SimpleSymbol mult(SimpleSymbol other) {
        if (other.symbol == symbol) {
            factor = factor.mult(other.factor.clone());
            power = power.add(other.power.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    /**
     * Dividing the current SimpleSymbol by a given one. This is only possible, when both SimpleSymbol have the same
     * symbol and accomplished by dividing the factors and subtracting the given power from the current one.
     * @param other SimpleSymbol to divide by.
     * @return Result of the division
     * @throws ArithmeticException When both SimpleSymbols don't have the same symbol
     */
    public SimpleSymbol divide(SimpleSymbol other) {
        if (other.symbol == symbol) {
            factor = factor.divide(other.factor.clone());
            power = power.subtract(other.power.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    @Override
    public String toString() {
        if (factor.equals(new SmartNum(0)))
            return "0";
        else if (power.equals(new SmartNum(1)) && factor.equals(new SmartNum(1)))
            return String.valueOf(symbol);
        else if (power.equals(new SmartNum(1)))
            return String.format("(%s) * %s", factor, symbol);
        else if (factor.equals(new SmartNum(1)))
            return String.format("%s ^ (%s)", symbol, power);
        else
            return String.format("(%s) * %s ^ (%s)", factor, symbol, power);
    }
}
