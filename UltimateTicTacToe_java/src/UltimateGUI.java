import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UltimateGUI implements ActionListener {
    private final Board board;
    private final JButton[][] tiles;
    private final JPanel[][] panels;
    private final JTextField textfield;
    private final JButton reset;
    private final Font font;
    private int player;
    private int nextBoxLinear;
    private Color p1Color = Color.BLUE;
    private Color p2Color = new Color(0, );

    public UltimateGUI() {
        this.player = 1;
        this.nextBoxLinear = -1;

        this.board = new Board();
        JFrame frame = new JFrame("Ultimate Tic Tac Toe");
        int buttonSideLength = 70;

        tiles = new JButton[9][9];
        font = new Font("Helvetica", Font.BOLD, 30);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(buttonSideLength * 9, buttonSideLength * 10);

        //=======================================
        //============TEXTFIELD==================
        //=======================================
        textfield = new JTextField();
        textfield.setBounds(buttonSideLength,
                9 * buttonSideLength,
                7 * buttonSideLength,
                buttonSideLength);
        textfield.setFont(font);
        textfield.setEditable(false);
        textfield.setText("O turn first");
        frame.add(textfield);
        frame.setLayout(null);

        //==================================
        //============TILES=================
        //==================================
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JButton currTile = new JButton("");
                currTile.addActionListener(this);
                currTile.setBackground(Color.RED);
                currTile.setFont(font);
                currTile.setFocusable(false);
                currTile.setBounds(j * buttonSideLength,
                        i * buttonSideLength,
                        buttonSideLength, buttonSideLength);
                tiles[i][j] = currTile;
                frame.add(tiles[i][j]);
            }
        }

        //===================================
        //==========RESET BUTTON=============
        //===================================
        reset = new JButton("RESET");
        reset.addActionListener(this);
        reset.setFont(new Font("Helvetica", Font.ITALIC, 15));
        reset.setForeground(Color.RED);
        reset.setFocusable(false);
        reset.setBounds(8 * buttonSideLength,
                9 * buttonSideLength,
                buttonSideLength, buttonSideLength);
        frame.add(reset);

        //==========================================================
        //========ADDING BOX PANELS (3x3 BACKGROUND COLOR)==========
        //==========================================================
        panels = new JPanel[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel p = new JPanel();
                p.setBounds(i * buttonSideLength * 3,
                        j * buttonSideLength * 3,
                        buttonSideLength * 3, buttonSideLength * 3);

                if ((i + j) % 2 == 0) {
                    p.setBackground(Color.BLACK);
                } else {
                    p.setBackground(Color.WHITE);
                }
                panels[i][j] = p;
                frame.add(panels[i][j]);
            }
        }

        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);

        frame.setVisible(true);


    }


    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (e.getSource() == tiles[i][j]) { //find the particular tile selected

                    if (board.inBox(i, j, nextBoxLinear) && board.getFullTile(i, j) == 0) {
                        System.out.println("i : " + i + ", j: " + j);

                        int nextBoxLinear = board.correspondingBoxLinear(i, j);

                        //Play move
                        board.updateFull(i, j, player);

                            //PLAYER 1: O
                        if (player == 1) {
                            tiles[i][j].setForeground(p1Color);
                            tiles[i][j].setText("0");

                            //PLAYER 2: X
                        } else {
                            tiles[i][j].setForeground(p2Color);
                            tiles[i][j].setText("X");
                        }
                        tiles[i][j].repaint();


                        //check for completed box
                        int boxVal = board.checkBox(i, j, true);
                        if (boxVal > 0) { //has been completed
                            int boxRow = board.boxLinearRow(board.fullToBoxLinear(i, j));
                            int boxCol = board.boxLinearCol(board.fullToBoxLinear(i, j));

                            board.updateBox(boxRow, boxCol, player);
                            if (player == 1) {
                                panels[boxRow][boxCol].setBackground(p1Color);
                            } else {
                                panels[boxRow][boxCol].setBackground(p2Color);
                            }
                        }


                        //---setup for next player---
                        //update next box and highlight it
                        int potentialNextBox = board.correspondingBoxLinear(i, j);
                            //confusing next line but basically it checks if
                            // the next box we WOULD go in is already completed
                            // if so, set nextBoxLinear to -1 (i.e. anywhere)
                            // if it is not completed (== 0 on boxState) then set it as normal
                        if (board.getBoxTile(board.boxLinearRow(potentialNextBox),
                                board.boxLinearCol(potentialNextBox)) == 0) {
                            this.nextBoxLinear = potentialNextBox;
                        } else {
                            this.nextBoxLinear = -1;
                        }


                        System.out.println("next box: " + nextBoxLinear);
                        highlightBox(nextBoxLinear);

                        //shift to next player, change text
                        if (player == 2) {
                            player = 1;
                            textfield.setText("O turn now");
                        } else {
                            player = 2;
                            textfield.setText("X turn now");
                        }


                    }
                }
            }
        }
    }

    public void highlightBox(int boxLinear) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tiles[i][j].setOpaque(board.inBox(i, j, boxLinear) && boxLinear >= 0);
                tiles[i][j].repaint();
            }
        }
    }


}

