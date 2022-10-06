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
        tryToImprove();
    }

    public SmartNum(double value){
        doubleValue = value;
        bestType = NumberType.DOUBLE;
        tryToImprove();
    }

    public SmartNum(String value){
        try { //Try to parse to INTEGER
            int parsedValue = Integer.parseInt(value);
            intValue = parsedValue;
            fracValue = new Frac(parsedValue);
            doubleValue = parsedValue;
            bestType = NumberType.INTEGER;
        } catch (NumberFormatException e1) {
            try { //Try to parse to DOUBLE
                doubleValue = Double.parseDouble(value);
                bestType = NumberType.DOUBLE;
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

    private void setFrac(Frac frac) {
        fracValue = frac;
        doubleValue = fracValue.approx();
        bestType = NumberType.FRACTION;
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

