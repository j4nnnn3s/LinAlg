package linalg;

public class SmartNum {

    private NumberType bestType;
    private int intValue;
    private Frac fracValue;
    private double doubleValue;

    public SmartNum(int value){
        setInt(value);
    }

    public SmartNum(Frac value){
        setFrac(value);
    }

    public SmartNum(double value){
        setDouble(value);
    }

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

    public boolean tryToImprove(){
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

    private void setInt(Frac value){ //Set the current type to INTEGER from a Fraction of the form x/1
        intValue = value.getNumerator();
        fracValue = value;
        doubleValue = value.getNumerator();
        bestType = NumberType.INTEGER;
    }

    private void setInt(int value) {
        intValue = value;
        fracValue = new Frac(value);
        doubleValue = value;
        bestType = NumberType.INTEGER;
    }

    private void setDouble(double value) {
        doubleValue = value;
        bestType = NumberType.DOUBLE;
        tryToImprove();
    }

    private void setFrac(Frac frac) {
        fracValue = frac;
        doubleValue = fracValue.approx();
        bestType = NumberType.FRACTION;
        tryToImprove();
    }

    public SmartNum add(int summand){
        switch (bestType) {
            case INTEGER -> setInt(summand + intValue);
            case FRACTION -> setFrac(fracValue.add(new Frac(summand)));
            case DOUBLE -> setDouble(doubleValue + summand);
        }
        return this;
    }

    public SmartNum add(Frac summand){
        switch (bestType) {
            case INTEGER -> setFrac(summand.add(intValue));
            case FRACTION -> setFrac(fracValue.add(summand));
            case DOUBLE -> setDouble(doubleValue + summand.approx());
        }
        return this;
    }

    public SmartNum add(double summand){
        switch (bestType) {
            case INTEGER -> setDouble(summand + intValue);
            case FRACTION -> setDouble(fracValue.approx() + summand);
            case DOUBLE -> setDouble(summand + doubleValue);
        }
        return this;
    }

    public SmartNum add(SmartNum summand){
        switch (summand.bestType){
            case INTEGER -> add(summand.intValue);
            case FRACTION -> add(summand.fracValue);
            case DOUBLE -> add(summand.doubleValue);
        }
        return this;
    }

    public SmartNum subtract(int subtrahend){
        return add(-1 * subtrahend);
    }

    public SmartNum subtract(Frac subtrahend){
        return add(subtrahend.mult(-1));
    }

    public SmartNum subtract(double subtrahend){
        return add(subtrahend * -1);
    }

    public SmartNum subtract(SmartNum subtrahend){
        return add(subtrahend.mult(-1));
    }

    public SmartNum mult(int factor) {
        switch (bestType){
            case INTEGER -> setInt(factor * intValue);
            case FRACTION -> setFrac(fracValue.mult(factor));
            case DOUBLE -> setDouble(doubleValue * factor);
        }
        return this;
    }

    public SmartNum mult(Frac factor) {
        switch (bestType){
            case INTEGER -> setFrac(factor.mult(intValue));
            case FRACTION -> setFrac(fracValue.mult(factor));
            case DOUBLE -> setDouble(doubleValue * factor.approx());
        }
        return this;
    }

    public SmartNum mult(double factor) {
        switch (bestType){
            case INTEGER -> setDouble(factor * intValue);
            case FRACTION, DOUBLE -> setDouble(doubleValue * factor);
        }
        return this;
    }

    public SmartNum mult(SmartNum factor) {
        switch (factor.bestType){
            case INTEGER -> mult(factor.intValue);
            case FRACTION -> mult(factor.fracValue);
            case DOUBLE -> mult(factor.doubleValue);
        }
        return this;
    }

    public SmartNum divide(int value) {
        if (value == 0) throw new ArithmeticException("Division by zero");
        return mult(1.0d/value);
    }

    public SmartNum divide(Frac value) {
        if (value.getNumerator() == 0) throw new ArithmeticException("Division by zero");
        return mult(value.clone().invert());
    }

    public SmartNum divide(double value) {
        if (value == 0) throw new ArithmeticException("Division by zero");
        return mult(1.0d/value);
    }

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
    public String toString(){
        return switch (bestType) {
            case INTEGER -> String.valueOf(intValue);
            case FRACTION -> fracValue.toString();
            case DOUBLE -> String.valueOf(doubleValue);
        };
    }

    public static boolean canBeInt(double value){
        return (int) value == value;
    }

    public boolean isZero() {
        switch (bestType) {
            case INTEGER -> {
                return intValue == 0;
            }
            case FRACTION -> {
              return fracValue.getNumerator() == 0;
            }
            case DOUBLE -> {
                return doubleValue == 0.0d;
            }
        }
        return false;
    }

    private enum NumberType {
        INTEGER,
        FRACTION,
        DOUBLE
    }
}

