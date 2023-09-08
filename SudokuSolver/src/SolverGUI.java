import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SolverGUI {
    Board board;
    ArrayList<Integer> emptiesOriginal;
    ArrayList<Integer> emptiesCurrent;
    JButton[][] tiles;

    public SolverGUI(Board b, JButton[][] buttonTiles) {
        this.board = b;
        this.tiles = buttonTiles;
        empties(true);
    }

    public Board getBoard() {
        return this.board;
    }

    public void resetBoard() {
        int row, col;
        for (int emp : emptiesOriginal) {
            row = getRow(emp);
            col = getCol(emp);
            board.removeTile(row, col);
        }
        board.setUnsolved(emptiesOriginal.size());
    }

    //the instance variable 'emptiesOriginal'
    // holds the (single-int) coordinates of every single
    // empty tile on the sudoku board
    // meaning if (0,4) and (4,5) were both empty,
    // then emptiesOriginal = {4, 41}
    //see comment above 'getLinear()' for explanation
    public void empties(boolean original) {
        emptiesCurrent = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getState()[i][j] == 0) {
                    emptiesCurrent.add(getLinear(i, j));
                }
            }
        }

        if (original) {
            emptiesOriginal = emptiesCurrent;
        }
    }

    public boolean solve() {
        if (emptiesCurrent.isEmpty()) {
            return true;
        }
        return backtrack(0);
    }

    /*
        This solves the sudoku puzzle via the recursive backtracking algorithm
        Which goes through each empty tile, starting at the top left,
        and will try every number. Upon finding a number that works
        for that tile, it will go to the next empty tile and do the same
        If it discovers that a particular tile has no possible option,
        it goes back to the prior tile that it worked on and finds another
        possibility.
        It solves the entire thing when it gets to the final empty tile
        and can find a valid option for it.
        Otherwise, it will return false if no solution can be found
     */
    public boolean backtrack(int emptiesInd) {
        int row = getRow(emptiesCurrent.get(emptiesInd));
        int col = getCol(emptiesCurrent.get(emptiesInd));

        //base case: the last element in 'emptiesOriginal',
        // so if we find a valid option for this,
        // then we solved the entire thing
        //therefore straight up return true if we get one, and
        // false if none
        if (emptiesInd == emptiesCurrent.size() - 1) {
            //check all #s to insert into current empty tile
            // from 1-9
            for (int i = 1; i < 10; i++) {
                //Only consider legal moves for a given tile
                if (board.legalMove(row, col, i)) {
                    //Update backend board object
                    // then update the GUI button with new number
                    board.updateTile(row, col, i);
                    tiles[row][col].setText("" + i);

                    //reset font + color in case it was already in
                    // pencilled-in state
                    tiles[row][col].setFont(new Font("Helvetica", Font.BOLD, 30));
                    tiles[row][col].setForeground(Color.BLACK);
                    return true;
                }
                //otherwise, simply go to the next one
            }
            //no solution found from 1-9
            // so update the tile back to 0
            // and return false to go 'backwards' in the recursion
            board.removeTile(row, col);
            tiles[row][col].setText("");

            return false;
        }
        //(no need for else because the if above will definitely return something)

        //check all #s to insert into current empty tile
        // from 1-9
        for (int i = 1; i < 10; i++) {
            //Only consider legal moves for a given tile
            if (board.legalMove(row, col, i)) {
                //Update backend board object
                // then update the GUI button with new number
                board.updateTile(row, col, i);
                tiles[row][col].setText("" + i);

                //reset font + color in case it was already in
                // pencilled-in state
                tiles[row][col].setFont(new Font("Helvetica", Font.BOLD, 30));
                tiles[row][col].setForeground(Color.BLACK);


                //Now go to next empty tile and recurse from there
                if (backtrack(emptiesInd + 1)) {
                    return true; //this IS the solution
                }
                //else, then simply increment 'i' to try again
            }
        }
        //this means that no option 'i' worked for this tile
        // that lead to any valid solution all the way down to the last
        // so this tile is unsolvable
        //before returning false, make the current tile 0
        // because otherwise it would stay as a 9
        // when backtracking which would retroactively throw off
        // previous tiles
        board.removeTile(row, col);
        tiles[row][col].setText("");

        return false;
    }

    //next 3 are helpers
    // i wanted to encode a single integer into a particular
    // tile on the sudoku board, so for example 18 is row 2 column 0
    // these helpers allow me to go from that 'linear' single-int val
    // to the row-col designation or backwards
    private int getLinear(int row, int col) {
        return row * 9 + col;
    }

    private int getRow(int linear) {
        return linear / 9;
    }

    private int getCol(int linear) {
        return linear % 9;
    }
}
