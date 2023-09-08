import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class BoardTest {
    int[][] grid;
    @Before
    public void setupImportGrid() {
        grid = new int[][]{
            {3, 0, 6, 5, 0, 8, 4, 0, 0},
            {5, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 8, 7, 0, 0, 0, 0, 3, 1},
            {0, 0, 3, 0, 1, 0, 0, 8, 0},
            {9, 0, 0, 8, 6, 3, 0, 0, 5},
            {0, 5, 0, 0, 9, 0, 6, 0, 0},
            {1, 3, 0, 0, 0, 0, 2, 5, 0},
            {0, 0, 0, 0, 0, 0, 0, 7, 4},
            {0, 0, 5, 2, 0, 6, 3, 0, 0}
        };
    }

    @Test
    public void newBoardTest() {
        Board board = new Board();

        assertEquals(0, board.getTile(0, 0));
    }

    @Test
    public void setTileTest() {
        Board board = new Board();

        board.updateTile(3, 5, 4);

        assertEquals(4, board.getTile(3, 5));
    }

    @Test
    public void importBoardTest() {
        Board board = new Board(grid);

        assertArrayEquals(grid, board.getState());
        assertEquals(8, board.getTile(2, 1));
        board.printBoard();
    }

    @Test
    public void rowColBoxSetsTest() {
        Board board = new Board(grid);

        HashSet<Integer> row22 = new HashSet<>();
        row22.add(8);
        row22.add(3);
        row22.add(1);
        HashSet<Integer> col22 = new HashSet<>();
        col22.add(6);
        col22.add(3);
        col22.add(5);
        HashSet<Integer> box22 = new HashSet<>();
        box22.add(3);
        box22.add(6);
        box22.add(5);
        box22.add(2);
        box22.add(8);

        assertEquals(row22, board.getRowSet(2, 2));
        assertEquals(col22, board.getColSet(2, 2));
        assertEquals(box22, board.getBoxSet(2, 2));
    }

    @Test
    public void legalMoveTest() {
        Board board = new Board(grid);

        assertTrue(board.legalMove(0, 1, 1)); //no 1 in row/col/box

        assertFalse(board.legalMove(3, 1, 1)); //1 already in row
        assertFalse(board.legalMove(3, 1, 2)); //2 already in col
        assertFalse(board.legalMove(3, 1, 9)); //9 already in box

        assertFalse(board.legalMove(0, 2, 1)); //no 1 anywhere near BUT already has 6 in it

    }

    @Test
    public void updateTileTest() {
        Board board = new Board(grid);

        assertTrue(board.legalMove(0, 1, 1)); //no 1 in row/col/box
        assertEquals(0, board.getTile(0, 1));

        board.updateTile(0, 1, 1);
        assertEquals(1, board.getTile(0, 1));
    }

}
