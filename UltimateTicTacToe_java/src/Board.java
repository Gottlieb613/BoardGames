public class Board {

    //Int to player Key:
        //0 = empty
        //1 = X
        //2 = O

    private int[][] fullState;
    private int[][] boxState;

    public Board() {
        this.fullState = new int[9][9];
        this.boxState = new int[3][3];
    }

    public int[][] getFullState() {
        return this.fullState;
    }

    public int[][] getBoxState() {
        return this.boxState;
    }

    public int getFullTile(int row, int col) {
        return this.fullState[row][col];
    }

    public int getBoxTile(int boxRow, int boxCol) {
        return this.boxState[boxRow][boxCol];
    }

    public void updateFull(int row, int col, int player) {
        fullState[row][col] = player;
    }

    public void updateBox(int row, int col, int player) {
        boxState[row][col] = player;
    }

    //works for fullState and boxState
    public int checkBox(int row, int col, boolean full) {
        int[][] box;

        if (full) { // have to create a 3x3 array with only that box's contents
            row = (row / 3) * 3;
            col = (col / 3) * 3;
            //now row, col is top left of box

            box = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    box[i][j] = fullState[i + row][j + col];
                }
            }

        } else { //if we are working on boxState, then just use that instead of copying
            box = this.boxState;
        }

        for (int i = 0; i < 3; i++) {
            //checking columns
            int topOfCol = box[i][0];
            if (topOfCol != 0 &&
                    topOfCol == box[i][1] &&
                    topOfCol == box[i][2]) {
                return topOfCol;
            }

            //checking rows
            int leftInRow = box[0][i];
            if (leftInRow != 0 &&
                    leftInRow == box[1][i] &&
                    leftInRow == box[2][i]
                    ) {
                return leftInRow;
            }
        }

        //checking diagonals
        int middle = box[1][1];
        if (middle != 0 && (
                (middle == box[0][0] && middle == box[2][2]) ||
                (middle == box[2][0] && middle == box[0][2])
                )) {
            return middle;
        }

        //only return 0 if NO win has been found in any possible direction
        return 0;
    }

    public boolean completeBoxUpdate(int fullRow, int fullCol, int player) {
        if (player > 0) {
            int bl = fullToBoxLinear(fullRow, fullCol);
                //bl = box linear
            boxState[boxLinearRow(bl)][boxLinearCol(bl)] = player;
            return true;
        }

        return false;
    }

    /**
     *
     * @param fullRow the row we want to place in
     * @param fullCol the col we want to place in
     * @param boxLinear the box row we have to place in (always returns try if < 0)
     * @return boolean of whether that fullRow/Col is in the intended box
     */

    public boolean inBox(int fullRow, int fullCol, int boxLinear) {
        int boxRow = boxLinearRow(boxLinear);
        int boxCol = boxLinearCol(boxLinear);

        //if the player is allowed to go anywhere, just set boxRow or boxCol to -1
        if (boxLinear < 0) {
            return true;
        }
        return fullRow >= boxRow * 3 &&
                fullRow < (boxRow + 1) * 3 &&
                fullCol >= boxCol * 3 &&
                fullCol < (boxCol + 1) * 3;
    }

    public int correspondingBoxLinear(int fullRow, int fullCol) {
        int internalRow = fullRow % 3;
        int internalCol = fullCol % 3;

        return internalRow * 3 + internalCol;
    }


    public void printBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int p = fullState[i][j];
                String pSymbol = ".";
                if (p == 1) {
                    pSymbol = "x";
                }
                if (p == 2) {
                    pSymbol = "o";
                }
                System.out.print(pSymbol);
                if (j % 3 == 2) {
                    System.out.print("  ");
                }
            }
            System.out.println();
            if (i % 3 == 2) {
                System.out.println();
            }
        }
    }

    public void printBoxBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int p = boxState[i][j];
                String pSymbol = ".";
                if (p == 1) {
                    pSymbol = "x";
                }
                if (p == 2) {
                    pSymbol = "o";
                }
                System.out.print(pSymbol);
            }
            System.out.println();
        }
    }













    /** input: row and col of FULL state
     * output: linear coordinate of which box that row-col is in
     */
    public int fullToBoxLinear(int row, int col) {
        return row / 3 + col / 3;
    }

    /** input: linear of BOX state
     * output: row of that box
     */
    public int boxLinearRow(int boxLinear) {
        return boxLinear / 3;
    }

    /** input: linear of BOX state
     * output: col of that box
     */
    public int boxLinearCol(int boxLinear) {
        return boxLinear % 3;
    }

    /** input: row and col of BOX state
     * output: linear coordinate of top-left tile of that box in FULL
     */
    public int boxToFullLinear(int row, int col) {
        return 9 * row + col;
    }

    /** input: linear of tile in FULL state
     * output: row of that tile
     */
    public int fullLinearRow(int fullLinear) {
        return fullLinear / 9;
    }

    /** input: linear of tile in FULL state
     * output: col of that tile
     */
    public int fullLinearCol(int fullLinear) {
        return fullLinear % 9;
    }


}
