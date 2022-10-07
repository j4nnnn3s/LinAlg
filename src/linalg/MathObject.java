package linalg;

abstract class MathObject {

    public MathObject(String s) {
    }

    MathObject() {
    }

    public MathObject add(MathObject o) {
        return this;
    }

    public MathObject mult(MathObject o) {
        return this;
    }

    public MathObject divide(MathObject o) {
        return this;
    }

    public MathObject subtract(MathObject o) {
        return this;
    }

    public boolean equals(MathObject o) {
        return this == o;
    }

}
