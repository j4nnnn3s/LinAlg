package linalg;

public class MatrixManipulator {

    public static TwoDMatrix solveGauss(TwoDMatrix matrix) {
        try {
            int maxDiag = Math.min(matrix.numberOfColumns() - 1, matrix.numberOfRows());
            for (int diagPosition = 0; diagPosition < maxDiag; diagPosition++) {
                SmartNum pivot = matrix.get(diagPosition, diagPosition);
                for (int row = 0; row < matrix.numberOfRows(); row++) {
                    if (row == diagPosition) {
                        continue;
                    }
                    SmartNum factor = new SmartNum(-1).mult(matrix.get(row, diagPosition)).divide(pivot);
                    for (int column = 0; column < matrix.numberOfColumns(); column++) {
                        SmartNum currentManipulator = matrix.get(diagPosition, column).mult(factor);
                        SmartNum target = matrix.get(row, column);
                        target.add(currentManipulator);
                        matrix.set(row, column, target);
                    }
                }
            }
            for (int diagPosition = 0; diagPosition < maxDiag; diagPosition++) {
                SmartNum pivot = matrix.get(diagPosition, diagPosition);
                matrix.set(diagPosition, diagPosition, new SmartNum(1));
                SmartNum lastElement = matrix.get(diagPosition, matrix.numberOfColumns() - 1);
                lastElement.divide(pivot);
                matrix.set(diagPosition, matrix.numberOfColumns() - 1, lastElement);
            }
            return matrix;
        } catch (ArithmeticException e) {
            System.out.println("The matrix has no solutions!");
            return null;
        }
    }

}
