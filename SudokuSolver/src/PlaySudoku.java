public class PlaySudoku {
    static Board board;
    static SudokuGUI gui;

    @SuppressWarnings("checkstyle:RegexpSingleline")
    public static void main(String[] args) {
        board = new Board(new int[][]{
            {7, 0, 0, 0, 0, 0, 2, 0, 0},
            {4, 0, 2, 0, 0, 0, 0, 0, 3},
            {0, 0, 0, 2, 0, 1, 0, 0, 0},
            {3, 0, 0, 1, 8, 0, 0, 9, 7},
            {0, 0, 9, 0, 7, 0, 6, 0, 0},
            {6, 5, 0, 0, 3, 2, 0, 0, 1},
            {0, 0, 0, 4, 0, 9, 0, 0, 0},
            {5, 0, 0, 0, 0, 0, 1, 0, 6},
            {0, 0, 6, 0, 0, 0, 0, 0, 8}
        });

        new SudokuGUI(board);
    }
}
