package linalg;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Object to operate with two-dimensional matrices
 */
public class TwoDMatrix {

    private SmartNum[][] matrix;

    /**
     * Constructor for a 2D-matrix from a given SmartNum 2D-Array
     * @param matrix 2D-Array of SmartNum containing the matrix
     */
    public TwoDMatrix(SmartNum[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Constructor for a 2D-matrix, loading the matrix from a csv file. Values in the file should be separated by comma
     * @param path The path of the csv File
     * @throws FileNotFoundException If the File doesn't exist our couldn't be accessed
     */
    public TwoDMatrix(String path) throws FileNotFoundException {
        String row = "";
        int rows = 0;
        File file = new File(path);
        Scanner sc = new Scanner(file);

        while(sc.hasNext()) {
            rows++;
            row = sc.nextLine();
        }

        int columns = row.split(",").length;
        matrix = new SmartNum[rows][columns];
        sc.close();
        sc = new Scanner(file);

        int currentRow = 0;
        while(sc.hasNext()) {
            row = sc.next();
            String[] rowSplited = row.split(",");
            for (int column = 0; column < rowSplited.length; column++) {
                matrix[currentRow][column] = new SmartNum(rowSplited[column]);
            }
            currentRow++;
        }
        sc.close();
    }

    /**
     * Converting the matrix into a string (values separated by whitespaces, rows by new lines and round brackets)
     * @return String with the matrix
     */
    @Override
    public String toString(){
        StringBuilder returnString = new StringBuilder();
        int[] longest = new int[matrix[0].length];
        for(SmartNum[] row : matrix){
            for(int i = 0; i < row.length; i++){
                int len = row[i].toString().length();
                if (len > longest[i]) longest[i] = len;
            }
        }
        for(SmartNum[] row : matrix){
            returnString.append("(");
            for(int i = 0; i < row.length; i++){
                int len = row[i].toString().length();
                returnString.append(row[i].toString());
                returnString.append(" ".repeat(Math.max(0, longest[i] - len + 1)));
            }
            returnString = new StringBuilder(returnString.substring(0, returnString.length() - 1));
            returnString.append(")\n");
        }
        return returnString.toString();
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
            return new SmartNum(matrix[row][column].toString()); // Has to be a new SmartNum, otherwise it will be mutable
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

    /**
     * Write the 2D-Matrix into a csv file, values separated by commas
     * @param path Path of the csv file
     * @throws IOException When an error while writing occurs
     */
    public void exportToCsv(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
        for(int i = 0; i < numberOfRows(); i++) {
            for(int j = 0; j < numberOfColumns(); j++) {
                writer.append(get(i,j).toString());
                if (j != numberOfColumns() - 1) writer.append(",");
            }
            if (i != numberOfRows()) writer.append("\n");
        }
        writer.close();
    }

}