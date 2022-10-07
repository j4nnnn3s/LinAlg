package linalg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

public class SimpleSymbol {

    public char symbol;
    public SmartNum factor;
    // TODO: Fix that *[a-z][A-Z] without number in the beginning isn't allowed
    private static final Pattern PATTERN = Pattern.compile("-?\\d*((\\.?|\\/?)\\d*)?\\s*\\*?\\s*[a-z]|[A-Z]");

    public SimpleSymbol(SmartNum factor, char symbol) {
        this.factor = factor;
        this.symbol = symbol;
    }

    public SimpleSymbol(String s) {
        parseSimpleSymbol(s);
    }

    private void parseSimpleSymbol(String s) {
        Matcher matcher = PATTERN.matcher(s);
        if(!matcher.matches()) {
            throw new NumberFormatException("Not a valid symbol");
        }
        if (s.contains("*")) s = s.replace("*", "");
        symbol = s.charAt(s.length() - 1);
        s = s.replace(String.valueOf(symbol), "");
        factor = new SmartNum(s);
    }

    public SimpleSymbol add(SimpleSymbol other) {
       if (other.symbol == symbol) {
           factor = factor.add(other.factor.clone());
           return this;
       }
       throw new ArithmeticException("Symbols are not the same");
    }

    public SimpleSymbol subtract(SimpleSymbol other) {
        if (other.symbol == symbol) {
            factor = factor.subtract(other.factor.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    public SimpleSymbol mult(SimpleSymbol other) {
        if (other.symbol == symbol) {
            factor = factor.mult(other.factor.clone());
            return this;
        }
        throw new ArithmeticException("Symbols are not the same");
    }

    public SimpleSymbol divide(SimpleSymbol other) {
       if (other.symbol == symbol)  {
           factor = factor.divide(other.factor.clone());
           return this;
       }
       throw new ArithmeticException("Symbols are not the same");
    }

    @Override
    public String toString() {
        return String.format("%s * %s", factor, symbol);
    }
}
