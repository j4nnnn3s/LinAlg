package linalg;

/**
 * Collection of manipulations for matrices
 */
public class MatrixManipulator {

    /**
     * Method to solve a system of linear equations represented as coefficient matrix via the gauss algorithm
     *
     * @param matrix Matrix to solve
     * @return Solved matrix or null if the matrix has no solutions
     */
    public static TwoDMatrix solveGauss(TwoDMatrix matrix) {
        try {
            // maxDiag is the maximal number, over which can be iterated without leaving the matrix
            int maxDiag = Math.min(matrix.numberOfColumns() - 1, matrix.numberOfRows());
            int movingFactor = 0; // It is possible, that the pivot might be 0. That's why movingFactor
            // Iterating over the matrix
            for (int diagPosition = 0; diagPosition < maxDiag; diagPosition++) {

                SmartNum pivot;

                // Getting the pivot element which should be the first element in the given row that isn't 0
                // TODO: Sort matrix before
                do {
                    pivot = matrix.get(diagPosition, diagPosition + movingFactor);
                    if (pivot.isZero() && movingFactor < matrix.numberOfColumns() - 3) movingFactor++;
                    else break;
                } while (pivot.isZero());
                // When the whole line is zero, continue
                // TODO: Here should the line already been deleted
                if (pivot.isZero()) continue;

                // Iterating over all other rows
                for (int row = 0; row < matrix.numberOfRows(); row++) {

                    if (row == diagPosition) {
                        continue;
                    }

                    // Factor which will be used to make all elements above and below the pivot zero
                    SmartNum factor = new SmartNum(-1).mult(matrix.get(row, diagPosition + movingFactor)).divide(pivot);
                    // Going over the row and adding the line of the pivot to the current with the factor
                    for (int column = 0; column < matrix.numberOfColumns(); column++) {
                        SmartNum currentManipulator = matrix.get(diagPosition, column).mult(factor);
                        SmartNum target = matrix.get(row, column);
                        target.add(currentManipulator);
                        matrix.set(row, column, target);
                    }

                    // If the line is zero after the calculations, the line has to be deleted
                    if (matrix.isRowZero(row)) {
                        matrix.deleteRow(row);
                        // As the matrix has changed, the algorithm has to be started again, to prevent issues
                        return solveGauss(matrix);
                    }
                }
            }

            // Matrix has been brought as close to a "diagonal" form as possible
            // Now divide all lines by the first element != 0, so it is prettier
            for (int diagPosition = 0; diagPosition < maxDiag; diagPosition++) {
                SmartNum pivot;
                movingFactor = 0;

                // Getting the pivot element
                do {
                    pivot = matrix.get(diagPosition, diagPosition + movingFactor);
                    if (pivot.isZero() && movingFactor < matrix.numberOfColumns() - 3) movingFactor++;
                    else break;
                } while (pivot.isZero());
                // If all elements are zero, continue (should not be possible)
                if (pivot.isZero()) continue;

                // Set pivot to 1 and divide all following numbers by the pivot
                matrix.set(diagPosition, diagPosition + movingFactor, new SmartNum(1));
                for (int column = diagPosition + movingFactor + 1; column < matrix.numberOfColumns(); column++) {
                    matrix.set(diagPosition, column, matrix.get(diagPosition, column).divide(pivot));
                }
            }
            return matrix;
        } catch (ArithmeticException e) {
            // As there has been an Arithmetic Exception, the matrix has either no solutions or (more likely) this implementation
            // of the algorithm has still some bugs
            System.out.println("The matrix has no solutions!");
            return null;
        }
    }

}
