package linalg;

/**
 * Object to operate with two-dimensional matrices
 */
public class TwoDMatrix {

    private SmartNum[][] matrix;

    /**
     * Constructor for a 2D-matrix
     * @param matrix 2D-Array of SmartNum containing the matrix
     */
    public TwoDMatrix(SmartNum[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Converting the matrix into a string (values separated by whitespaces, rows by new lines and round brackets)
     * @return String with the matrix
     */
    @Override
    public String toString(){
        String returnString = new String();
        int[] longest = new int[matrix[0].length];
        for(SmartNum[] row : matrix){
            for(int i = 0; i < row.length; i++){
                int len = row[i].toString().length();
                if (len > longest[i]) longest[i] = len;
            }
        }
        for(SmartNum[] row : matrix){
            returnString += "(";
            for(int i = 0; i < row.length; i++){
                int len = row[i].toString().length();
                returnString += row[i].toString();
                for (int j = 0; j < longest[i] - len + 1; j++) {
                    returnString += " ";
                }
            }
            returnString = returnString.substring(0,returnString.length() - 1);
            returnString += ")\n";
        }
        return returnString;
    }

    /**
     * Function for getting the number of rows
     * @return Number of rows
     */
    public int numberOfRows() {
        return matrix.length;
    }

    /**
     * Function for getting the number of columns
     * @return Number of columns
     */
    public int numberOfColumns() {
        return matrix[0].length;
    }

    /**
     * This method updates a value inside the matrix
     * @param row ID of the target row (or "y-value")
     * @param column ID of the target column (or "x-value")
     * @param value New value
     */
    public void set(int row, int column, SmartNum value) {
        try {
            matrix[row][column] = value;
        } catch(ArrayIndexOutOfBoundsException e)  {
            e.printStackTrace();
        }
    }

    /**
     * Method to get a value in the matrix at a specific position
     * @param row ID of the target row (or "y-value")
     * @param column ID of the target column (or "x-value")
     * @return Value at the given position
     */
    public SmartNum get(int row, int column) {
        try {
            return matrix[row][column];
        } catch(ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function to delete a row
     * @param row ID of the target row
     */
    public void deleteRow(int row) {
        SmartNum[][] newMatrix = new SmartNum[numberOfRows()-1][numberOfColumns()];
        for (int i = 0; i <= newMatrix.length; i++) {
            if (i < row) {
                newMatrix[i] = matrix[i];
            } else if (i > row) {
                newMatrix[i-1] = matrix[i];
            }
        }
        matrix = newMatrix;

    }

    /**
     * Function to delete a column
     * @param column ID of the target column
     */
    public void deleteColumn(int column) {
        SmartNum[][] newMatrix = new SmartNum[numberOfRows()][numberOfColumns()-1];
        for (int j = 0; j < newMatrix.length; j++) {
            for (int i = 0; i <= newMatrix[0].length; i++) {
                if (i < column) {
                    newMatrix[j][i] = matrix[j][i];
                } else if (i > column) {
                    newMatrix[j][i - 1] = matrix[j][i];
                }
            }
        }
        matrix = newMatrix;
    }
}