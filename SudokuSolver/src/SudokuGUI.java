/* TODO:
    .bugs
    .tidying
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI implements ActionListener, KeyListener {
    private final Board board;
    private final SolverGUI solverGUI;
    private final JButton[][] tiles;
    private final JButton[] nums;
    private final JButton pencil;
    private boolean pencilMode;
    private final JTextField textfield;
    private final JButton reset;
    private final JButton solve;
    private final Font numberFont;
    private final Font pencilFont;
    private final Font optionButtonFont;
    private int lastTileLinear = -1;

    public SudokuGUI(Board board) {
        this.board = board;
        JFrame frame = new JFrame("Sudoku");
        int buttonSideLength = 70;

        tiles = new JButton[9][9];
        nums = new JButton[9];
        numberFont = new Font("Helvetica", Font.BOLD, 30);
        pencilFont = new Font("Helvetica", Font.BOLD, 10);
        optionButtonFont = new Font("Helvetica", Font.BOLD, 14);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(buttonSideLength * 9, buttonSideLength * 12);

        //=======================================
        //============TEXTFIELD==================
        //=======================================
        textfield = new JTextField();
        textfield.setBounds(buttonSideLength,
                9 * buttonSideLength,
                6 * buttonSideLength,
                buttonSideLength);
        textfield.setFont(numberFont);
        textfield.setEditable(false);
        frame.add(textfield);
        frame.setLayout(null);

        //==================================
        //============NUMS==================
        //==================================
        for (int i = 0; i < 9; i++) {
            nums[i] = new JButton(String.valueOf(i + 1));
            nums[i].addActionListener(this);
            nums[i].setBackground(Color.DARK_GRAY);
                //this is for when we want to indicate that a number is completed (9x)
            nums[i].setFont(numberFont);
            nums[i].setFocusable(false);
            nums[i].setBounds(i * buttonSideLength,
                    10 * buttonSideLength,
                    buttonSideLength, buttonSideLength);
                //x, y, width, height
            frame.add(nums[i]);
        }

        //==================================
        //============TILES=================
        //==================================
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getTile(i, j) != 0) {
                    tiles[i][j] = new JButton("" + board.getTile(i, j));
                    tiles[i][j].setForeground(Color.BLUE);
                } else {
                    tiles[i][j] = new JButton("");
                }
                tiles[i][j].setBackground(Color.RED);
                tiles[i][j].addActionListener(this);
                tiles[i][j].setFont(numberFont);
                tiles[i][j].setFocusable(false);
                tiles[i][j].setBounds(j * buttonSideLength,
                        i * buttonSideLength,
                    buttonSideLength, buttonSideLength);
                    //x, y, width, height
                frame.add(tiles[i][j]);
            }
        }

        this.solverGUI = new SolverGUI(board, tiles);

        //===================================
        //==========RESET BUTTON=============
        //===================================
        reset = new JButton("RESET");
        reset.addActionListener(this);
        reset.setFont(optionButtonFont);
        reset.setForeground(Color.RED);
        reset.setFocusable(false);
        reset.setBounds(8 * buttonSideLength,
                9 * buttonSideLength,
                buttonSideLength, buttonSideLength);
        frame.add(reset);

        //===================================
        //==========SOLVE BUTTON=============
        //===================================
        solve = new JButton("SOLVE");
        solve.addActionListener(this);
        solve.setFont(optionButtonFont);
        solve.setForeground(Color.BLUE);
        solve.setFocusable(false);
        solve.setBounds(7 * buttonSideLength,
                9 * buttonSideLength,
                buttonSideLength, buttonSideLength);
        frame.add(solve);


        //===================================
        //============PENCIL=================
        //===================================
        pencil = new JButton("PENCIL");
        pencil.addActionListener(this);
        pencil.setFont(optionButtonFont);
        pencil.setForeground(Color.ORANGE);
        pencil.setFocusable(false);
        pencil.setBounds(0,
                9 * buttonSideLength,
                buttonSideLength, buttonSideLength);
        pencil.setBackground(Color.RED);
            //doesn't activate immediately since it is still not opaque
        frame.add(pencil);
        pencilMode = false;


        //==========================================================
        //========ADDING BOX PANELS (3x3 BACKGROUND COLOR)==========
        //==========================================================
        JPanel[][] boxes = new JPanel[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxes[i][j] = new JPanel();
                boxes[i][j].setBounds(i * buttonSideLength * 3,
                        j * buttonSideLength * 3,
                        buttonSideLength * 3, buttonSideLength * 3);

                if ((i + j) % 2 == 0) {
                    boxes[i][j].setBackground(new Color(0, 190, 160)); //blue-greenish
                } else {
                    boxes[i][j].setBackground(new Color(94, 94, 92)); //grayish
                }
                frame.add(boxes[i][j]);
            }
        }

        //=======Listening for keys======
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);

        frame.setVisible(true);

    }

    @Override
    //Note: for e.getKeyCode(),
        //← = 37
        //↑ = 38
        //→ = 39
        //↓ = 40
        //0 = 48
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed=" + KeyEvent.getKeyText(e.getKeyCode()) +
                "\n\t" + e.getKeyCode());

        int code = e.getKeyCode();

        //first, ensure that some tile is already selected
        if (lastTileLinear >= 0) {
            //========ARROW KEYS===========
            if (code >= 37 && code <= 40 && lastTileLinear >= 0) {
                int nextTileLinear = lastTileLinear;

                //left: make sure it is not in leftmost col
                if (code == 37 && lastTileLinear % 9 > 0) {
                    nextTileLinear -= 1;
                }
                //up: make sure it is not in top row
                if (code == 38 && lastTileLinear / 9 > 0) {
                    nextTileLinear -= 9;
                }
                //right: make sure it is not in rightmost col
                if (code == 39 && lastTileLinear % 9 < 8) {
                    nextTileLinear += 1;
                }
                //down: make sure it is not in bottom row
                if (code == 40 && lastTileLinear / 9 < 8) {
                    nextTileLinear += 9;
                }
                getTile(lastTileLinear).setOpaque(false);
                getTile(lastTileLinear).repaint();
                getTile(nextTileLinear).setOpaque(true);
                getTile(nextTileLinear).repaint();
                lastTileLinear = nextTileLinear;
            }

            //======SELECT NUMBER=======
            // code(1) = 49
            if (code >= 49 && code <= 57) {
                updateTile(code - 48, false);
            }
        }

        //P = 80 -> pencil mode
        if (code == 80) {
            pencilMode = !pencilMode;
            pencil.setOpaque(!pencil.isOpaque());
            pencil.repaint();
        }

        //R = 82 -> reset
        if (code == 82) {
            resetBoard();
        }

        //S = 83 -> solve
        if (code == 83) {
            solveBoard();
        }
    }

    public void checkNumComplete(int num) {
        if (board.numQuantity(num) == 9) {
            nums[num - 1].setForeground(Color.LIGHT_GRAY);
            nums[num - 1].setOpaque(true);
        } else {
            nums[num - 1].setForeground(Color.BLACK);
            nums[num - 1].setOpaque(false);
        }
        nums[num - 1].repaint();
    }

    public void checkNumCompleteAll() {
        for (int i = 1; i < 10; i++) {
            checkNumComplete(i);
        }
    }


    //Don't need the next two
    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pencil) {
            if (pencilMode) { //turn pencilMode off
                pencil.setOpaque(false);
                pencilMode = false;
            } else { //turn pencilMode on
                pencil.setOpaque(true);
                pencilMode = true;
            }
            return;
        }

        //Reset
        if (e.getSource() == reset) {
            resetBoard();
            return;
        }

        //Solve
        if (e.getSource() == solve) {
            solveBoard();
            return;

        //Selecting tiles or numbers
        } else {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {

                    //Selecting a tile
                    if (e.getSource() == tiles[i][j]) {
                        if (lastTileLinear >= 0) {
                            getTile(lastTileLinear).setOpaque(false);
                            getTile(lastTileLinear).repaint();
                                //not sure why but it doesn't always repaint fast enough
                                //with the setOpaque call so I put it manually to ensure
                        }

                        lastTileLinear = getLinear(i, j);
                        getTile(lastTileLinear).setOpaque(true);
                    }
                }


                //Selecting a number
                if (e.getSource() == nums[i] && lastTileLinear >= 0) {
                    updateTile(i + 1, true);
                }
            }
        }

        if (board.solved()) {
            textfield.setText("Congratulations!");
        }
    }

    private void resetBoard() {
        if (lastTileLinear >= 0) {
            getTile(lastTileLinear).setOpaque(false);
            getTile(lastTileLinear).repaint();
            lastTileLinear = -1;
        }

        solverGUI.resetBoard();
        textfield.setText("Reset");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getTile(i, j) != 0) {
                    tiles[i][j].setText("" + board.getTile(i, j));
                } else {
                    tiles[i][j].setText("");
                }
            }
        }
        checkNumCompleteAll();
    }

    private void solveBoard() {
        solverGUI.empties(false);
        textfield.setText("Solving...");
        if (solverGUI.solve()) {
            textfield.setText("Solved!");
        } else {
            textfield.setText("No solution. Reset to try again.");
        }
        checkNumCompleteAll();
    }

    private void updateTile(int newNum, boolean removeLastTile) {
        JButton tile = getTile(lastTileLinear);

        int row = lastTileLinear / 9;
        int col = lastTileLinear % 9;

        if (pencilMode) {
            //should not be able to pencil over blue (original) tiles
            if (tile.getForeground() == Color.BLUE) {
                textfield.setText("Cannot pencil on that tile");

            } else {
                tile.setForeground(Color.DARK_GRAY);
                tile.setFont(pencilFont);
                tile.setText(pencilUpdate(tile.getText(), newNum));
            }

        } else { //normal mode
            //Default tile
            if (tile.getForeground() == Color.BLUE) {
                textfield.setText("Cannot change that tile");


            //Removing a number
            } else if (board.getTile(row, col) == newNum) {
                if (lastTileLinear >= 0 && removeLastTile) {
                    tile.setOpaque(false);
                    lastTileLinear = -1;
                }
                board.removeTile(row, col);
                textfield.setText("Removed tile");
                tile.setText("");


            //Adding or overriding a number
            } else if (board.legalMove(row, col, newNum)) {
                if (lastTileLinear >= 0 && removeLastTile) {
                    getTile(lastTileLinear).setOpaque(false);
                    lastTileLinear = -1;
                }
                board.updateTile(row, col, newNum);
                textfield.setText("Good choice!");
                tile.setForeground(Color.BLACK);
                tile.setFont(numberFont); //reset in case it was just in pencilFont
                tile.setText("" + newNum);

            //Illegal move
            } else {
                textfield.setText("Try again!");
            }
        }
        checkNumComplete(newNum);
    }

    private int getLinear(int i, int j) {
        return i * 9 + j;
    }

    public JButton getTile(int linear) {
        return tiles[linear / 9][linear % 9];
    }

    String pencilUpdate(String previous, int newNum) {
        if (newNum < 1 || newNum > 9) {
            return previous;
        }

        char[] chars = previous.toCharArray();
        if (chars.length <= 1) {
            chars = "<html>     <br>     <br>     </html>".toCharArray();
        }

        char numAsChar = (char) (newNum + '0');
        if (chars[getNumIndex(newNum)] == ' ') { //if num is not pencilled yet
            chars[getNumIndex(newNum)] = numAsChar; //then add it
        } else { //else, num IS pencilled already
            chars[getNumIndex(newNum)] = ' '; //so remove it
        }

        return String.valueOf(chars);
    }

    private int getNumIndex(int num) {
        num = num - 1;
        int row = num / 3;
        int col = num % 3;
        if (row == 0) {
            return 6 + (col * 2);
        }
        if (row == 1) {
            return 15 + (col * 2);
        }
        return 24 + (col * 2);
    }
}
