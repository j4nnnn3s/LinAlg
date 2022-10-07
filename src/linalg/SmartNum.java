package linalg;

/**
 * Class that implements the datatype SmartNum, an object that can be used to represent numbers in calculations.
 * SmartNums will automatically choose their internal type between integer, fraction and double, depending on what preserves the most accuracy.
 */
public class SmartNum extends MathObject {

    private NumberType bestType;
    private int intValue;
    private Frac fracValue;
    private double doubleValue;

    /**
     * Constructor used to create a SmartNum from an int.
     * @param value int value of the number
     */
    public SmartNum(int value){
        setInt(value);}

    /**
     * Constructor used to create a SmartNum from a fraction.
     * @param value Frac value of the number
     */
    public SmartNum(Frac value){
        setFrac(value);
    }

    /**
     * Constructor used to create a SmartNum from a double.
     * @param value double value of the number
     */
    public SmartNum(double value){
        setDouble(value);
    }

    /**
     * Constructor to create a SmartNum from a String.
     * The String can be any representation of a number,
     *      either an integer,
     *      a fraction written as: "n/m" where n and m are both integers
     *      or a decimal number
     * @param value String value of the number
     * @throws NumberFormatException if the provided String is not of the correct shape
     */
    public SmartNum(String value){
        try { //Try to parse to INTEGER
            int parsedIntValue = Integer.parseInt(value);
            setInt(parsedIntValue);
        } catch (NumberFormatException e1) {
            try { //Try to parse to DOUBLE
                 double parsedDoubleValue = Double.parseDouble(value);
                 setDouble(parsedDoubleValue);
            } catch (NumberFormatException e2) {
                try { //Try to parse to FRAC
                    Frac castedFrac = Frac.parseFrac(value);
                    setFrac(castedFrac);
                } catch (NumberFormatException e3) {
                    throw new NumberFormatException("This number type is not available");
                }
            }
        }
        tryToImprove();
    }

    /**
     * Method that tries to improve the internal type of the SmartNum:
     *      Tries to make an int out of a fraction
     *      or a fraction or an int out of a double
     * This method will not do anything if the current internal type is int
     * @return success
     */
    private boolean tryToImprove(){
        switch (bestType){
            case INTEGER -> {
                return false;
            }
            case FRACTION -> {
                if(fracValue.isInteger()){
                    setInt(fracValue);
                }
                return true;
            }
            case DOUBLE -> {
                if(canBeInt(doubleValue)){
                    intValue = (int) doubleValue;
                    fracValue = new Frac(intValue);
                    bestType = NumberType.INTEGER;
                    return true;
                } else {
                    return tryToSetFracFromDouble(doubleValue);
                }
            }
        }
        return false;
    }

    /**
     * Method that tries to set the current type to a fraction from being provided a double
     * This is done by viewing the input value as a fraction of the type value/1, then multiplying numerator and denominator by 10, until both are int.
     * The internal type is only set to fraction, if the fraction that was obtained in this fashion can be canceled.
     * Ex:  0.25 -> 1/4
     *      0.1 -> 0.1
     * Also doubles with more than 10 digits after the decimal point will never be converted to a fraction.
     * @param value double value of the number
     * @return success
     */
    private boolean tryToSetFracFromDouble(double value) {
        int zeros = 0;
        while(!canBeInt(value)){
            if (zeros > 10) return false;
            value = value * 10;
            zeros++;
        }
        Frac frac = new Frac((int) value, (int) Math.pow(10, zeros));
        if (frac.getNumerator() != value) {
            setFrac(frac);
           return true;
        }
        return false;
    }

    /**
     * Method that sets the internal type to an integer
     * @param value value the SmartNum will be set to
     */
    private void setInt(int value) {
        intValue = value;
        fracValue = new Frac(value);
        doubleValue = value;
        bestType = NumberType.INTEGER;
    }

    /**
     * Method that sets the internal type to an integer
     * @param value value the SmartNum will be set to. Has to a fraction of the form n/1
     * @throws NumberFormatException if the fraction value is not of the correct form
     */
    private void setInt(Frac value){
        if(value.getDenominator() != 1){
            throw new NumberFormatException("The provided fraction is not of type n/1");
        }
        intValue = value.getNumerator();
        fracValue = value;
        doubleValue = value.getNumerator();
        bestType = NumberType.INTEGER;
    }

    /**
     * Method that sets the internal type to double
     * @param value value that SmartNum will be set to
     * @implNote only doubleValue will be correct if the internal type is double, fracValue and intValue should NOT be accessed!
     */
    private void setDouble(double value) {
        doubleValue = value;
        bestType = NumberType.DOUBLE;
        tryToImprove();
    }

    /**
     * Method that sets the internal type to a fraction
     * @param frac value that the SmartNum will be set to
     * @implNote only doubleValue and fracValue will be correct if the internal type is fraction, intValue should NOT be accessed!
     */
    private void setFrac(Frac frac) {
        fracValue = frac;
        doubleValue = fracValue.approx();
        bestType = NumberType.FRACTION;
        tryToImprove();
    }

    /**
     * Method that adds an integer to the value of this SmartNum
     * @param summand number to be added
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum add(int summand){
        switch (bestType) {
            case INTEGER -> setInt(summand + intValue);
            case FRACTION -> setFrac(fracValue.add(new Frac(summand)));
            case DOUBLE -> setDouble(doubleValue + summand);
        }
        return this;
    }

    /**
     * Method that adds a fraction to the value of this SmartNum
     * @param summand number to be added
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum add(Frac summand){
        switch (bestType) {
            case INTEGER -> setFrac(summand.add(intValue));
            case FRACTION -> setFrac(fracValue.add(summand));
            case DOUBLE -> setDouble(doubleValue + summand.approx());
        }
        return this;
    }

    /**
     * Method that adds a double to the value of this SmartNum
     * @param summand number to be added
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum add(double summand){
        switch (bestType) {
            case INTEGER -> setDouble(summand + intValue);
            case FRACTION -> setDouble(fracValue.approx() + summand);
            case DOUBLE -> setDouble(summand + doubleValue);
        }
        return this;
    }

    /**
     * Method that adds a SmartNum to the value of this SmartNum
     * @param summand number to be added
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum add(SmartNum summand){
        switch (summand.bestType){
            case INTEGER -> add(summand.intValue);
            case FRACTION -> add(summand.fracValue);
            case DOUBLE -> add(summand.doubleValue);
        }
        return this;
    }

    /**
     * Method that subtracts an int from the value of this SmartNum
     * @param subtrahend number to be subtracted
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum subtract(int subtrahend){
        return add(-1 * subtrahend);
    }

    /**
     * Method that subtracts a fraction from the value of this SmartNum
     * @param subtrahend number to be subtracted
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum subtract(Frac subtrahend){
        return add(subtrahend.mult(-1));
    }

    /**
     * Method that subtracts a double from the value of this SmartNum
     * @param subtrahend number to be subtracted
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum subtract(double subtrahend){
        return add(subtrahend * -1);
    }

    /**
     * Method that subtracts a SmartNum from the value of this SmartNum
     * @param subtrahend number to be subtracted
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum subtract(SmartNum subtrahend){
        return add(subtrahend.mult(-1));
    }

    /**
     * Method that multiplies this SmartNum by an int
     * @param factor number to be multiplied
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum mult(int factor) {
        switch (bestType){
            case INTEGER -> setInt(factor * intValue);
            case FRACTION -> setFrac(fracValue.mult(factor));
            case DOUBLE -> setDouble(doubleValue * factor);
        }
        return this;
    }

    /**
     * Method that multiplies this SmartNum by a fraction
     * @param factor number to be multiplied
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum mult(Frac factor) {
        switch (bestType){
            case INTEGER -> setFrac(factor.clone().mult(intValue));
            case FRACTION -> setFrac(fracValue.mult(factor));
            case DOUBLE -> setDouble(doubleValue * factor.approx());
        }
        return this;
    }

    /**
     * Method that multiplies this SmartNum by a double
     * @param factor number to be multiplied
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum mult(double factor) {
        switch (bestType){
            case INTEGER -> setDouble(factor * intValue);
            case FRACTION, DOUBLE -> setDouble(doubleValue * factor);
        }
        return this;
    }

    /**
     * Method that multiplies this SmartNum by a SmartNum
     * @param factor number to be multiplied
     * @return return the result in addition to changing the value inside the object this is called on.
     */
    public SmartNum mult(SmartNum factor) {
        switch (factor.bestType){
            case INTEGER -> mult(factor.intValue);
            case FRACTION -> mult(factor.fracValue);
            case DOUBLE -> mult(factor.doubleValue);
        }
        return this;
    }

    /**
     * Method that divides this SmartNum by an integer
     * @param value number to be divided by, cannot be 0
     * @return return the result in addition to changing the value inside the object this is called on.
     * @throws ArithmeticException if value is 0
     */
    public SmartNum divide(int value) {
        if (value == 0) throw new ArithmeticException("Division by zero");
        switch (bestType) {
            case INTEGER -> setFrac(new Frac(intValue, value));
            case FRACTION -> setFrac(fracValue.divide(value));
            case DOUBLE -> mult(1.0d/value);
        }
        return this;
    }

    /**
     * Method that divides this SmartNum by a fraction
     * @param value number to be divided by, cannot be 0
     * @return return the result in addition to changing the value inside the object this is called on.
     * @throws ArithmeticException if value is 0
     */
    public SmartNum divide(Frac value) {
        if (value.getNumerator() == 0) throw new ArithmeticException("Division by zero");
        return mult(value.clone().invert());
    }

    /**
     * Method that divides this SmartNum by a double
     * @param value number to be divided by, cannot be 0
     * @return return the result in addition to changing the value inside the object this is called on.
     * @throws ArithmeticException if value is 0
     */
    public SmartNum divide(double value) {
        if (value == 0) throw new ArithmeticException("Division by zero");
        return mult(1.0d/value);
    }

    /**
     * Method that divides this SmartNum by a SmartNum
     * @param value number to be divided by, cannot be 0
     * @return return the result in addition to changing the value inside the object this is called on.
     * @throws ArithmeticException if value is 0
     */
    public SmartNum divide(SmartNum value) {
        if (value.isZero()) throw new ArithmeticException("Division by zero");
        switch (value.bestType){
            case INTEGER -> divide(value.intValue);
            case FRACTION -> divide(value.fracValue);
            case DOUBLE -> divide(value.doubleValue);
        }
        return this;
    }

    @Override
    /**
     * Method that outputs the most accurate possible representation of this SmartNum as a string
     * @return the String representation
     */
    public String toString(){
        return switch (bestType) {
            case INTEGER -> String.valueOf(intValue);
            case FRACTION -> fracValue.toString();
            case DOUBLE -> String.valueOf(doubleValue);
        };
    }

    /**
     * Clones this SmartNum
     * @return a duplicate of this SmartNum
     * @implNote the values of the number types that are better than the current best ones (Ex. intValue if bestType is double) might be different.
     *           this shouldn´t be a problem though, since these values should never be accessed
     */
    public SmartNum clone() {
        return new SmartNum(toString());
    }

    /**
     * Method that return whether the provided double could accurately be represented as an int
     * @param value checked double value
     * @return whether the provided double could accurately be represented as an int
     */
    public static boolean canBeInt(double value){
        return (int) value == value;
    }

    /**
     * Method that checks if this SmartNum is 0
     * @return weather this Smart num is 0
     */
    public boolean isZero() {
        return doubleValue == 0d;
    }

    /**
     * Method that compares this SmartNum to another
     * @param s SmartNum to compare to
     * @return weather this SmartNum has the same value as the other
     */
    public boolean equals(SmartNum s) {
        return doubleValue == s.doubleValue;
    }

    private enum NumberType {
        INTEGER,
        FRACTION,
        DOUBLE
    }
}