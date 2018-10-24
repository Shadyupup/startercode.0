// Name:Dongyu Xiao
// USC NetID:dongyuxi@usc.edu
// CS 455 PA3
// Fall 2018


import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashSet;

/**
 * VisibleField class
 * This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
 * user can see about the minefield), Client can call getStatus(row, col) for any square.
 * It actually has data about the whole current state of the game, including
 * the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
 * It also has mutators related to moves the player could do (resetGameDisplay(), cycleGuess(), uncover()),
 * and changes the game state accordingly.
 * <p>
 * It, along with the MineField (accessible in mineField instance variable), forms
 * the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
 * It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from
 * outside this class via the getMineField accessor.
 */
public class VisibleField {
    // ----------------------------------------------------------
    // The following public constants (plus numbers mentioned in comments below) are the possible states of one
    // location (a "square") in the visible field (all are values that can be returned by public method
    // getStatus(row, col)).

    // Covered states (all negative values):
    public static final int COVERED = -1;   // initial value of all squares
    public static final int MINE_GUESS = -2;
    public static final int QUESTION = -3;

    // Uncovered states (all non-negative values):

    // values in the range [0,8] corresponds to number of mines adjacent to this square

    public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
    public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
    public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
    // ----------------------------------------------------------

    // <put instance variables here>
    /**
     * @param mineField restore mine field
     * @param statusTable restore status
     * @param FIRSTROW first row of the matrix
     * @param mineLeft mine left number
     * @param FIRSTROW  is game Over?
     */
    private MineField mineField;
    private int[][] statusTable;
    private final int FIRSTROW = 0;
    private int mineLeft;

    private int safeCell;

    private boolean isOver = false;

    /**
     * Create a visible field that has the given underlying mineField.
     * The initial state will have all the mines covered up, no mines guessed, and the game
     * not over.
     *
     * @param mineField the minefield to use for for this VisibleField
     * initial parameter
     */
    public VisibleField(MineField mineField) {
        this.mineField = mineField;
        mineLeft = mineField.numMines();
        safeCell = mineField.numRows() * mineField.numCols() - mineLeft;

        statusTable = new int[mineField.numRows()][mineField.numCols()];
        for (int row = FIRSTROW; row < statusTable.length; row++) {
            for (int col = FIRSTROW; col < statusTable[FIRSTROW].length; col++) {
                statusTable[row][col] = COVERED;
            }
        }
    }


    /**
     * Reset the object to its initial state (see constructor comments), using the same underlying MineField.
     */
    public void resetGameDisplay() {
        statusTable = new int[mineField.numRows()][mineField.numCols()];
        for (int row = FIRSTROW; row < statusTable.length; row++) {
            for (int col = FIRSTROW; col < statusTable[FIRSTROW].length; col++) {
                statusTable[row][col] = COVERED;
            }
        }
        mineLeft = mineField.numMines();
        safeCell = mineField.numRows() * mineField.numCols() - mineLeft;
        isOver = false;

    }


    /**
     * Returns a reference to the mineField that this VisibleField "covers"
     *
     * @return the minefield
     */
    public MineField getMineField() {
        return mineField;       // DUMMY CODE so skeleton compiles
    }


    /**
     * get the visible status of the square indicated.
     *
     * @param row row of the square
     * @param col col of the square
     * @return the status of the square at location (row, col).  See the public constants at the beginning of the class
     * for the possible values that may be returned, and their meanings.
     * PRE: getMineField().inRange(row, col)
     */
    public int getStatus(int row, int col) {
        if (getMineField().inRange(row, col)) {
            return statusTable[row][col];
        }
        return 0;       // return empty
    }


    /**
     * Return the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
     * or not.  Just gives the user an indication of how many more mines the user might want to guess.  So the value can
     * be negative, if they have guessed more than the number of mines in the minefield.
     *
     * @return the number of mines left to guess.
     */
    public int numMinesLeft() {
        return mineLeft;       // DUMMY CODE so skeleton compiles

    }


    /**
     * Cycles through covered states for a square, updating number of guesses as necessary.  C+re
     * changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
     * changes it to COVERED again; call on an uncovered square has no effect.
     *
     * @param row row of the square
     * @param col col of the square
     *            PRE: getMineField().inRange(row, col)
     */
    public void cycleGuess(int row, int col) {
        if (!getMineField().inRange(row, col)) {
            return;
        }
        int status = statusTable[row][col];
        if (status == COVERED) {
            statusTable[row][col] = MINE_GUESS;
            mineLeft--;
        } else if (status == MINE_GUESS) {
            statusTable[row][col] = QUESTION;
            mineLeft++;
        } else if (status == QUESTION) {
            statusTable[row][col] = COVERED;
        }
    }


    /**
     * Uncovers this square and returns false iff you uncover a mine here.
     * If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in
     * the neighboring area that are also not next to any mines, possibly uncovering a large region.
     * Any mine-adjacent squares you reach will also be uncovered, and form
     * (possibly along with parts of the edge of the whole field) the boundary of this region.
     * Does not uncover, or keep searching through, squares that have the status MINE_GUESS.
     *
     * @param row of the square
     * @param col of the square
     * @return false   iff you uncover a mine at (row, col)
     * PRE: getMineField().inRange(row, col)
     */
    public boolean uncover(int row, int col) {
        MineField mineFieldEntity = getMineField();
        if (!getMineField().inRange(row, col)) {
            return true;
        }
        if (mineFieldEntity.hasMine(row, col)) {
            isOver = true;
            statusTable[row][col] = EXPLODED_MINE;
            return false;       // DUMMY CODE so skeleton compiles
        } else {
            if (mineFieldEntity.numAdjacentMines(row, col) > 0) {
                statusTable[row][col] = mineFieldEntity.numAdjacentMines(row, col);
            } else {
                dfs(mineFieldEntity, row, col);
            }
            return true;
        }

    }


    /**
     * Returns whether the game is over.
     *
     * @return whether game over
     * loop the status table
     * no safe cell left,then win
     * get mine,then explode,and lose
     * update according status table
     */
    public boolean isGameOver() {
        int safeCellLeft = safeCell;
        if (!isOver) {
            for (int i = FIRSTROW; i < statusTable.length; i++) {
                for (int j = FIRSTROW; j < statusTable[0].length; j++) {
                    if (statusTable[i][j] > COVERED && !getMineField().hasMine(i, j)) {
                        safeCellLeft--;
                    }
                }
            }
            if (safeCellLeft == 0) {
                isOver = true;
            }
        }
        if (isOver) {
            for (int i = 0; i < statusTable.length; i++) {
                for (int j = 0; j < statusTable[0].length; j++) {
                    if (statusTable[i][j] == MINE_GUESS && !getMineField().hasMine(i, j)) {
                        statusTable[i][j] = INCORRECT_GUESS;
                    }
                    if (safeCellLeft != 0) {
                        if (statusTable[i][j] == COVERED || statusTable[i][j] == QUESTION) {
                            if (getMineField().hasMine(i, j)) {
                                statusTable[i][j] = MINE;
                            }
                        }
                    } else {
                        if (statusTable[i][j] == COVERED || statusTable[i][j] == QUESTION) {
                            if (getMineField().hasMine(i, j)) {
                                statusTable[i][j] = MINE_GUESS;
                            }
                        }
                    }
                }
            }
        }
        return isOver;       // DUMMY CODE so skeleton compiles
    }


    /**
     * Return whether this square has been uncovered.  (i.e., is in any one of the uncovered states,
     * vs. any one of the covered states).
     *
     * @param row of the square
     * @param col of the square
     * @return whether the square is uncovered
     * PRE: getMineField().inRange(row, col)
     */
    public boolean isUncovered(int row, int col) {
        if (getMineField().inRange(row, col) && statusTable[row][col] > COVERED) {
            return true;
        }
        return false;      // DUMMY CODE so skeleton compiles
    }


    // <put private methods here>
    //use dfs to open space,the direction is left top,top,right top,left,right,left bottom,bottom,right bottom
    private void dfs(MineField mineFieldEntity, int row, int col) {
        if ((!getMineField().inRange(row, col) || getMineField().hasMine(row, col)) || statusTable[row][col] > COVERED || statusTable[row][col] == MINE_GUESS) {
            return;
        }
        if (mineFieldEntity.numAdjacentMines(row, col) > 0) {
            statusTable[row][col] = mineFieldEntity.numAdjacentMines(row, col);
            return;
        }
        statusTable[row][col] = mineFieldEntity.numAdjacentMines(row, col);
        dfs(mineFieldEntity, row - 1, col - 1);
        dfs(mineFieldEntity, row - 1, col);
        dfs(mineFieldEntity, row - 1, col + 1);
        dfs(mineFieldEntity, row, col - 1);
        dfs(mineFieldEntity, row, col + 1);
        dfs(mineFieldEntity, row + 1, col - 1);
        dfs(mineFieldEntity, row + 1, col);
        dfs(mineFieldEntity, row + 1, col + 1);

    }


}
