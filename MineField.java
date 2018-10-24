// Name:Dongyu Xiao
// USC NetID:dongyuxi@usc.edu
// CS 455 PA3
// Fall 2018


import java.util.Random;

/**
 * MineField
 * class with locations of mines for a game.
 * This class is mutable, because we sometimes need to change it once it's created.
 * mutators: populateMineField, resetEmpty
 * includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {

    // <put instance variables here>
/**
 * @param numRows mine field rows numbers
 * @param numCols mine field columns numbers
 * @param numMines mine field mine numbers
 * @param mineMap mineMap mine field mine map
 * @param FIRSTROW  minefiled first row
 */

    private int numRows;
    private int numCols;
    private int numMines;
    private boolean[][] mineMap;

    private final int FIRSTROW = 0;

    /**
     * Create a minefield with same dimensions as the given array, and populate it with the mines in the array
     * such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
     * this minefield will corresponds to the number of 'true' values in mineData.
     *
     * @param mineData the data for the mines; must have at least one row and one col.
     */
    public MineField(boolean[][] mineData) {
        mineMap = mineData;
        numRows = mineData.length;
        numCols = mineData[FIRSTROW].length;
        //count mine in mineMap
        for (int i = 0; i < mineData.length; i++) {
            for (int j = 0; j < mineData[FIRSTROW].length; j++) {
                if (mineData[i][j]) {
                    numMines++;
                }
            }
        }
    }


    /**
     * Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once
     * populateMineField is called on this object).  Until populateMineField is called on such a MineField,
     * numMines() will not correspond to the number of mines currently in the MineField.
     *
     * @param numRows  number of rows this minefield will have, must be positive
     * @param numCols  number of columns this minefield will have, must be positive
     * @param numMines number of mines this minefield will have,  once we populate it.
     *                 PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations).
     */
    public MineField(int numRows, int numCols, int numMines) {
        //pre numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations).
        int limitMine=numMines*3;
        assert numRows>0 && numCols>0 && 0<=numMines && numMines > limitMine;
        this.numRows = numRows;
        this.numCols = numCols;
        this.numMines = numMines;
        mineMap = new boolean[numRows][numCols];
    }


    /**
     * Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
     * ensuring that no mine is placed at (row, col).
     *
     * @param row the row of the location to avoid placing a mine
     * @param col the column of the location to avoid placing a mine
     *            PRE: inRange(row, col)
     */
    public void populateMineField(int row, int col) {
        if (!inRange(row, col)) {
            return;
        }
        resetEmpty();
        Random generator = new Random();
        int minesLeft = numMines;
        mineMap = new boolean[numRows][numCols];
        while (minesLeft > 0) {
            int number = generator.nextInt(numCols * numRows);
            //1D matrix to 2D matrix
            int rowIndex = number / numCols;
            int colIndex = number % numCols;
            if ((rowIndex == row && colIndex == col) || mineMap[rowIndex][colIndex] && !inRange(rowIndex, colIndex)) {
                continue;
            }
            mineMap[rowIndex][colIndex] = true;
            minesLeft--;
        }
    }


    /**
     * Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
     * Thus, after this call, the actual number of mines in the minefield does not match numMines().
     * Note: This is the state the minefield is in at the beginning of a game
     */
    public void resetEmpty() {
        mineMap = new boolean[numRows][numCols];
    }


    /**
     * Returns the number of mines adjacent to the specified mine location (not counting a possible
     * mine at (row, col) itself).
     * Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     *
     * @param row row of the location to check
     * @param col column of the location to check
     * @return the number of mines adjacent to the square at (row, col)
     * PRE: inRange(row, col)
     */
    public int numAdjacentMines(int row, int col) {
        if (!inRange(row, col)) {
            return 0;
        }
        int mineNum = 0;
        if (inRange(row - 1, col - 1) && mineMap[row - 1][col - 1]) {
            mineNum++;
        }
        if (inRange(row - 1, col) && mineMap[row - 1][col]) {
            mineNum++;
        }
        if (inRange(row - 1, col + 1) && mineMap[row - 1][col + 1]) {
            mineNum++;
        }
        if (inRange(row, col - 1) && mineMap[row][col - 1]) {
            mineNum++;
        }
        if (inRange(row, col + 1) && mineMap[row][col + 1]) {
            mineNum++;
        }
        if (inRange(row + 1, col - 1) && mineMap[row + 1][col - 1]) {
            mineNum++;
        }
        if (inRange(row + 1, col) && mineMap[row + 1][col]) {
            mineNum++;
        }
        if (inRange(row + 1, col + 1) && mineMap[row + 1][col + 1]) {
            mineNum++;
        }
        return mineNum;       // DUMMY CODE so skeleton compiles
    }


    /**
     * Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
     * start from 0.
     *
     * @param row row of the location to consider
     * @param col column of the location to consider
     * @return whether (row, col) is a valid field location
     */
    public boolean inRange(int row, int col) {
        return 0 <= row && row < numRows && 0 <= col && col < numCols;
    }


    /**
     * Returns the number of rows in the field.
     *
     * @return number of rows in the field
     */
    public int numRows() {
        return numRows;       // DUMMY CODE so skeleton compiles
    }


    /**
     * Returns the number of rows in the field.
     *
     * @return number of rows in the field
     */
    public int numCols() {
        return numCols;       // DUMMY CODE so skeleton compiles
    }


    /**
     * Returns whether there is a mine in this square
     *
     * @param row row of the location to check
     * @param col column of the location to check
     * @return whether there is a mine in this square
     * PRE: inRange(row, col)
     */
    public boolean hasMine(int row, int col) {
        if (!inRange(row, col)) {
            return false;
        }
        return mineMap[row][col];
    }


    /**
     * Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
     * some of the time this value does not match the actual number of mines currently on the field.  See doc for that
     * constructor, resetEmpty, and populateMineField for more details.
     *
     * @return
     */
    public int numMines() {
        return numMines;       // DUMMY CODE so skeleton compiles
    }


    // <put private methods here>


}

