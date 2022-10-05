package linalg;

public class TwoDMatrix {
    private Frac[][] matrix;

    public TwoDMatrix(Frac[][] matrix) {
        this.matrix = matrix;
    }

    public TwoDMatrix(int[][] matrix) {
        Frac[][] fracMatrix = new Frac[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++)
                fracMatrix[i][j] = new Frac(matrix[i][j]);
        this.matrix = fracMatrix;
    }

    @Override
    public String toString(){
        String returnString = new String();
        int[] longest = new int[matrix[0].length];
        for(Frac[] row : matrix){
            for(int i = 0; i < row.length; i++){
                int len = row[i].toString().length();
                if (len > longest[i]) longest[i] = len;
            }
        }
        for(Frac[] row : matrix){
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

    public int numberOfRows() {
        return matrix.length;
    }

    public int numberOfColumns() {
        return matrix[0].length;
    }

    public void set(int row, int column, Frac value) {
        try {
            matrix[row][column] = value;
        } catch(ArrayIndexOutOfBoundsException e)  {
            e.printStackTrace();
        }
    }

    public Frac get(int row, int column) {
        try {
            return matrix[row][column];
        } catch(ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteRow(int row) {
        Frac[][] newMatrix = new Frac[numberOfRows()-1][numberOfColumns()];
        for (int i = 0; i <= newMatrix.length; i++) {
            if (i < row) {
                newMatrix[i] = matrix[i];
            } else if (i > row) {
                newMatrix[i-1] = matrix[i];
            }
        }
        matrix = newMatrix;

    }

    public void deleteColumn(int column) {
        Frac[][] newMatrix = new Frac[numberOfRows()][numberOfColumns()-1];
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