package linalg;

/**
 * Collection of manipulations for matrices
 */
public class MatrixManipulator {

    /**
     * Method to solve a system of linear equations represented as coefficient matrix via the gauss algorithm
     * @param matrix Matrix to solve
     * @return Solved matrix or null if the matrix has no solutions
     */
    public static TwoDMatrix solveGauss(TwoDMatrix matrix) {
        try {
            int maxDiag = Math.min(matrix.numberOfColumns() - 1, matrix.numberOfRows());
            int movingFactor = 0; // It is possible, that the pivot might be 0. Thats why movingFactor
            for (int diagPosition = 0; diagPosition < maxDiag; diagPosition++) {
                SmartNum pivot;
                do {
                    pivot = matrix.get(diagPosition, diagPosition + movingFactor);
                    if(pivot.isZero() && movingFactor < matrix.numberOfColumns() - 3) movingFactor++;
                    else break;
                } while (pivot.isZero());
                if (pivot.isZero()) continue;

                for (int row = 0; row < matrix.numberOfRows(); row++) {
                    if (row == diagPosition) {
                        continue;
                    }
                    SmartNum factor = new SmartNum(-1).mult(matrix.get(row, diagPosition + movingFactor)).divide(pivot);
                    for (int column = 0; column < matrix.numberOfColumns(); column++) {
                        SmartNum currentManipulator = matrix.get(diagPosition, column).mult(factor);
                        SmartNum target = matrix.get(row, column);
                        target.add(currentManipulator);
                        matrix.set(row, column, target);
                    }
                    if (matrix.isRowZero(row)) {
                        matrix.deleteRow(row);
                        return solveGauss(matrix);
                    }
                }
            }
            for (int diagPosition = 0; diagPosition < maxDiag; diagPosition++) {
                SmartNum pivot;
                movingFactor = 0;
                do {
                    pivot = matrix.get(diagPosition, diagPosition + movingFactor);
                    if(pivot.isZero() && movingFactor < matrix.numberOfColumns() - 3) movingFactor++;
                    else break;
                } while (pivot.isZero());
                if (pivot.isZero()) continue;

                matrix.set(diagPosition, diagPosition + movingFactor, new SmartNum(1));
                for (int column = diagPosition + movingFactor + 1; column < matrix.numberOfColumns(); column++) {
                    matrix.set(diagPosition, column, matrix.get(diagPosition, column).divide(pivot));
                }
            }
            return matrix;
        } catch (ArithmeticException e) {
            System.out.println("The matrix has no solutions!");
            return null;
        }
    }

}
