package ProjectOOP;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.File;
import ProjectOOP.Shape.Tetrominoes;
public class Board extends JPanel implements ActionListener{
    private static final long serialVersionUID = 1L;
    //Declare background image location
    private String BackgroundFolder = "Background" + File.separator;
    private String BackgroundPath = BackgroundFolder + "Background 1.png";
    final int BoardWidth = 10;
    final int BoardHeight = 22;
    Timer timer;
    boolean FallingFinished = false;
    boolean Start = false;
    boolean Pause = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    JLabel statusbar;
    Shape curPiece;
    Tetrominoes[] board;
    public Board(Tetris parent) {
        setFocusable(true);
        curPiece = new Shape();
        timer = new Timer(400, this);
        timer.start(); 
        statusbar =  parent.getStatusBar();
        board = new Tetrominoes[BoardWidth * BoardHeight];
        addKeyListener(new TAdapter());
        clearBoard();  
    }

    public void actionPerformed(ActionEvent e) {
        if (FallingFinished) {
            FallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    int squareWidth() { return (int) getSize().getWidth() / BoardWidth; }
    int squareHeight() { return (int) getSize().getHeight() / BoardHeight; }
    Tetrominoes shapeAt(int x, int y) {return board[(y * BoardWidth) + x];}

    //Initiate game start and continue the game when it is paused
    public void start(){
        if (Pause)
            return;
        Start = true;
        FallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
    }
    
    //Method to temporarily pause the game
    private void pause(){
        if (!Start)
            return;
        Pause = !Pause;
        if (Pause) {
            timer.stop();

            JOptionPane.showMessageDialog(null, "Game Paused", "Tetris", JOptionPane.INFORMATION_MESSAGE);
        } else {
            timer.start();
        }
        repaint();
    }
    
    public void paint(Graphics g){ 
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Tetrominoes.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                               boardTop + i * squareHeight(), shape);
            }
        }
        if (curPiece.getShape() != Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                           boardTop + (BoardHeight - y - 1) * squareHeight(),
                           curPiece.getShape());
            }
        }
    }
    
    //Set Background to the game panel
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        ImageIcon background = new ImageIcon(BackgroundPath);
        g.drawImage(background.getImage(),0,0,this.getWidth(),this.getHeight(),null);
    }

    //Display Game Over screen
    public void GameOver(){
        //GameOver screen formatting and display
        statusbar.setForeground(Color.RED);
        statusbar.setFont(new Font("Calibri", Font.BOLD, 50));
        statusbar.setText("GAME OVER");
        add(statusbar, BorderLayout.CENTER);
        int confirmed = JOptionPane.showConfirmDialog(null, "Thank you for playing!\n" + "Score: " 
        + String.valueOf(numLinesRemoved*100) + "\nWould you like to play again?" ,"Tetris", JOptionPane.YES_NO_OPTION);
    }


//Pieces' movement
    private boolean tryMove(Shape newPiece, int newX, int newY){
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != Tetrominoes.NoShape)
                return false;
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }
    //Method to clear a line when all the pieces are puzzled together
    private void removeFullLines(){
        int numFullLines = 0;
        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean isFull = true;
            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == Tetrominoes.NoShape) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
                
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText("Score: "+ String.valueOf(numLinesRemoved*100));
            Tetris.PlayClear();
            FallingFinished = true;
            curPiece.setShape(Tetrominoes.NoShape);
            repaint();
        }
     }
    //Pieces'configuration
    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape){
        Color colors[] = {
        new Color(0,0,0), new Color(204, 102, 102), new Color(0,255,255),
        new Color(102, 204, 102), new Color(102, 102, 204), new Color(0,255,128),
        new Color(204, 204, 102), new Color(204, 102, 204), new Color(255,255,0),
        new Color(102, 204, 204), new Color(218, 170, 0), new Color(255,51,153)
    };
        Color color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);
    }
