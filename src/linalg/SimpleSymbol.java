package linalg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSymbol {

    private char symbol;
    private SmartNum factor;
    private SmartNum power;

    // TODO: Fix that *[a-z][A-Z] without number in the beginning isn't allowed
    private static final Pattern PATTERN = Pattern.compile(
            "(-?\\d*((\\.?|\\/?)\\d*)?\\s*\\*?\\s*)(?:[a-z]|[A-Z])(?:\\s*\\^?-?\\d*((\\.?|\\/?)\\d*)?\\s*)"
    );

    public SimpleSymbol(SmartNum factor, char symbol) {
        this.factor = factor;
        this.symbol = symbol;
        this.power = new SmartNum(1);
    }

    public SimpleSymbol(SmartNum factor, char symbol, SmartNum power) {
        this.factor = factor;
        this.symbol = symbol;
        this.power = power;
    }

    public SimpleSymbol(String s) {
        parseSimpleSymbol(s);
    }

    public char getSymbol() {
        return symbol;
    }

    public SmartNum getFactor() {
        return factor;
    }

    public SmartNum getPower() {
        return power;
    }

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

    public boolean validSimpleSymbol(String s) {
        Matcher matcher = PATTERN.matcher(s);
        return PATTERN.matcher(s).matches();
    }

    public SimpleSymbol add(SimpleSymbol other) {
        if (other.symbol == symbol && other.power == power) {
            factor = factor.add(other.factor.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    public SimpleSymbol subtract(SimpleSymbol other) {
        if (other.symbol == symbol && other.power == power) {
            factor = factor.subtract(other.factor.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    public SimpleSymbol mult(SimpleSymbol other) {
        if (other.symbol == symbol) {
            factor = factor.mult(other.factor.clone());
            power = power.add(other.power.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

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
        if (power.equals(new SmartNum(1)) && factor.equals(new SmartNum(1)))
            return String.valueOf(symbol);
        else if (power.equals(new SmartNum(1)))
            return String.format("(%s) * %s", factor, symbol);
        else if (factor.equals(new SmartNum(1)))
            return String.format("%s ^ (%s)", symbol, power);
        else
            return String.format("(%s) * %s ^ (%s)", factor, symbol, power);
    }
}
