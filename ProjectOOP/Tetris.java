package ProjectOOP;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
public class Tetris extends JFrame{
    private static final long serialVersionUID = 1L;
	//Instantiate audio into the game 
	private static AudioPlayer audio = new AudioPlayer();	
	JLabel statusbar,score;
	//Setup the game GUI
    public Tetris() {
        statusbar = new JLabel("Score: 0");
		statusbar.setOpaque(true);//Enable background change for the score bar
		statusbar.setBackground(Color.BLACK);//Set background color for the score bar
		statusbar.setForeground(Color.red);//Set font color for the score bar
		statusbar.setFont(new Font("Calibri",Font.PLAIN,40));//Set font for the score bar
        add(statusbar, BorderLayout.NORTH);//Add score bar to desired position on the game interface
        Board board = new Board(this);//Create game interface
        JOptionPane.showMessageDialog(board,
        		"Welcome to Java Tetris! \n "
        		+ "Instructions for the game:\n"
        		+ " You have to eliminate rows of blocks by fitting them in the empty spaces. 100 points for each lines that are cleared\n"
        		+ " Keys : \n"
        		+ " Navigation:\n"
        		+ " 1. Left : LEFT\n"
        		+ " 2. Right: RIGHT\n"
        		+ " Rotation\n"
        		+ " 1. Left : UP\n"
        		+ " 2. Right: DOWN\n"
        		+ " Drop down : SPACE\n"
        		+ " Pause: Key P","Java Tetris",JOptionPane.INFORMATION_MESSAGE);
        add(board);//Add board to container for display
        board.start();//Start the game
        setTitle("Tetris");
		setSize(500,700);
        addWindowListener(new WindowAdapter() {
        	  @Override
        	  public void windowClosing(WindowEvent e) {
        	    int confirmed = JOptionPane.showConfirmDialog(null, 
				"Are you sure you want to exit the game?", "Tetris",JOptionPane.OK_CANCEL_OPTION);
        	    if (confirmed == JOptionPane.CANCEL_OPTION) {
					setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//Return to the game interface
        	    }
				else System.exit(1);
        	  }
        	});
   }
   public JLabel getStatusBar() {
       return statusbar;
   }
   public static void PlayClear(){
	   audio.PlayClearLine();//invoke PlayClearLine from class AudioPlayer to play the audio
   }
   public static void PlayGameOver(){
	   audio.PlayGameOver();//invoke PlayGameOver from class AudioPlayer to play the audio
   }
   //Deploy the game
    public static void main(String[] args) {
        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    } 
}

