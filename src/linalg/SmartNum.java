package linalg;

public class SmartNum {

    private NumberType bestType;
    private int intValue;
    private Frac fracValue;
    private double doubleValue;

    public SmartNum(int value){
        intValue = value;
        fracValue = new Frac(value);
        doubleValue = value;
        bestType = NumberType.INTEGER;
    }

    public SmartNum(Frac value){
        fracValue = value;
        doubleValue = value.approx();
        bestType = NumberType.FRACTION;
    }

    public SmartNum(double value){
        //TODO: Add some magic: Cast decimal representations of fractions to Frac (using the 10* trick and then canceling). Ex: 1.25 -> 5/4
        doubleValue = value;
        bestType = NumberType.DOUBLE;
    }

    public SmartNum(String value){
        try { //Try to parse to INTEGER
            int parsedValue = Integer.parseInt(value);
            intValue = parsedValue;
            fracValue = new Frac(parsedValue);
            doubleValue = parsedValue;
            bestType = NumberType.INTEGER;
            return;
        } catch (NumberFormatException e) {}
        try { //Try to parse to DOUBLE
            double castedDouble = Double.parseDouble(value);
            if(canBeInt(castedDouble)){
                intValue = (int)castedDouble;
                fracValue = new Frac((int)castedDouble);
                doubleValue = castedDouble;
                bestType = NumberType.INTEGER;
                return;
            }
            doubleValue = castedDouble;
            bestType = NumberType.DOUBLE;
            return;
        } catch (NumberFormatException e) {}
        try { //Try to parse to FRAC
            Frac castedFrac = Frac.parseFrac(value);
            fracValue = castedFrac;
            doubleValue = castedFrac.approx();
            bestType = NumberType.FRACTION;
            return;
        } catch (NumberFormatException e) {}
        throw new NumberFormatException("This number type is not available");
    }

    public void tryToImprove(){
        //TODO: Try to cast double to Frac once the constructor has a method that also does that
        switch (bestType){
            case INTEGER -> {}
            case FRACTION -> {
                if(fracValue.isInteger()){
                    setInt(fracValue);
                }
            }
            case DOUBLE -> {
                if(canBeInt(doubleValue)){
                    intValue = (int) doubleValue;
                    fracValue = new Frac(intValue);
                    bestType = NumberType.INTEGER;
                }
            }
        }
    }

    private void setInt(Frac value){ //Set the current type to INTEGER from a Fraction of the form x/1
        intValue = value.getNumerator();
        fracValue = value;
        doubleValue = value.getNumerator();
        bestType = NumberType.INTEGER;
    }

    public SmartNum add(int summand){
        switch (bestType) {
            case INTEGER -> {
                intValue += summand;
            }
            case FRACTION -> {
                fracValue = fracValue.add(new Frac(summand));
                if (fracValue.isInteger()) {
                    intValue = fracValue.getNumerator();
                    bestType = NumberType.INTEGER;
                }
            }
            case DOUBLE -> {
                double result = doubleValue + summand;
                doubleValue = result;
                if (canBeInt(result)) {
                    intValue = (int) result;
                    bestType = NumberType.INTEGER;
                }
            }
        }
        tryToImprove();
        return this;
    }

    public SmartNum add(Frac summand){
        if(summand.isInteger()){
            return add(summand.getNumerator());
        }
        switch (bestType) {
            case INTEGER -> {
                fracValue = new Frac(intValue).add(summand);
                bestType = NumberType.FRACTION;
                doubleValue = fracValue.approx();
            }
            case FRACTION -> {
                fracValue = fracValue.add(summand);
            }
            case DOUBLE -> {
                doubleValue += summand.approx();
            }
        }
        tryToImprove();
        return this;
    }

    public SmartNum add(double summand){
        switch (bestType) {
            case INTEGER -> {
                doubleValue = summand + intValue;
                bestType = NumberType.DOUBLE;
            }
            case FRACTION -> {
                doubleValue = fracValue.approx() + summand;
                bestType = NumberType.DOUBLE;
            }
            case DOUBLE -> {
                doubleValue += summand;
            }
        }
        tryToImprove();
        return this;
    }

    public SmartNum add(SmartNum summand){
        switch (summand.bestType){
            case INTEGER -> {
                add(summand.intValue);
            }
            case FRACTION -> {
                add(summand.fracValue);
            }
            case DOUBLE -> {
                add(summand.doubleValue);
            }
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
        //return add(subtrahend.mult(-1));
        //TODO: Implement multiplication so this works
        return new SmartNum("0");
    }

    //TODO: Implement multiplication and division

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

    private enum NumberType {
        INTEGER,
        FRACTION,
        DOUBLE
    }
}

