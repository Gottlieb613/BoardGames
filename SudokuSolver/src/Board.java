import java.util.Arrays;
import java.util.HashSet;

public class Board {
    private int[][] state;
    private int unsolved;
    private int[] quantities;
        //array where each index corresp. to the quantity of that # on the board
        //meaning if quantities[3] == 4 then there are four 3's on the baord
        //we ignore the 0 index

    public Board() {
        state = new int[9][9];
        quantities = new int[10];
        unsolvedAndQuantities();
    }

    public Board(int[][] newBoard) {
        state = newBoard;
        quantities = new int[10];
        unsolvedAndQuantities();
    }

    public void setState(int[][] newBoard) {
        this.state = newBoard;
    }

    public void unsolvedAndQuantities() {
        this.unsolved = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.state[i][j] == 0) {
                    this.unsolved++;
                }
                //increment 'quantities' for each number already on the board
                quantities[state[i][j]]++;
            }
        }

    }

    public void setUnsolved(int n) {
        unsolved = n;
    }

    public int[][] getState() {
        return this.state;
    }

    public int getTile(int row, int col) {
        return state[row][col];
    }

    public void removeTile(int row, int col) {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("board index is out of bounds");
        }

        if (state[row][col] != 0) {
            this.unsolved++;
            quantities[state[row][col]]--;
        }

        state[row][col] = 0;
    }


    public boolean updateTile(int row, int col, int num) {
        if (num < 0 || num > 9) {
            throw new IllegalArgumentException("num is out of bounds");
        }
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("board index is out of bounds");
        }


        if (legalMove(row, col, num)) {
            quantities[state[row][col]]--;
            quantities[num]++;
            state[row][col] = num;
            this.unsolved--;


            return true;
        }
        return false;
    }

    public boolean legalMove(int row, int col, int num) {
        //'num' cannot already be in the same row, col, or box
        return state[row][col] != num && !(getRowSet(row, col).contains(num)
                || getColSet(row, col).contains(num)
                || getBoxSet(row, col).contains(num));
    }

    public boolean solved() {
        return this.unsolved == 0;
    }

    public int numQuantity(int n) {
        return quantities[n];
    }

    public HashSet<Integer> getRowSet(int row, int col) {
        HashSet<Integer> theRow = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            //exclude 0's and the particular tile itself
            if (i != col && state[row][i] != 0) {
                theRow.add(state[row][i]);
            }
        }
        return theRow;
    }

    public HashSet<Integer> getColSet(int row, int col) {
        HashSet<Integer> theCol = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            //exclude 0's and the particular tile itself
            if (i != row && state[i][col] != 0) {
                theCol.add(state[i][col]);
            }
        }
        return theCol;
    }

    public HashSet<Integer> getBoxSet(int row, int col) {
        HashSet<Integer> theBox = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            int boxRowStart = (row / 3) * 3;
            int boxColStart = (col / 3) * 3;
            for (int j = 0; j < 3; j++) {
                int rowCurr = boxRowStart + i;
                int colCurr = boxColStart + j;
                //exclude 0's and the particular tile itself
                if ((rowCurr != row || colCurr != col) && state[rowCurr][colCurr] != 0) {
                    theBox.add(state[rowCurr][colCurr]);
                }
            }
        }
        return theBox;
    }

    //Note: this assumes
    // that you are looking at the first row
    // as 1, meaning the rows go from 1-9
    // even though the backend goes from 0-8
    @SuppressWarnings({"checkstyle:RegexpSingleline"})
    public void printBoard() {

        System.out.print("  c:");
        for (int i = 1; i < 10; i++) {
            if (i % 3 != 0) {
                System.out.print(i + " ");
            } else {
                System.out.print(i + "   ");
            }
        }
        System.out.println("\nr:  - - -   - - -   - - -");

        for (int i = 0; i < 9; i++) {

            if (i > 0 && i % 3 == 0) {
                System.out.println("    ------+-------+------");
            }
            System.out.print((i + 1) + "|  ");
            for (int j = 0; j < 9; j++) {
                String tile = "" + state[i][j];
                if (state[i][j] == 0) { //"•" instead of "0" for display
                    tile = "•";
                }

                if (j % 3 == 2 && j < 8) {
                    System.out.print(tile + " | ");
                } else {
                    System.out.print(tile + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean equals(Board other) {
        return Arrays.deepEquals(this.state, other.getState());
    }
}
