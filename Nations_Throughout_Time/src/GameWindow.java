
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import java.io.File;
import java.awt.*;  
import javax.swing.*;  
import javax.swing.Timer;
import java.io.Serializable;

public class GameWindow extends JFrame implements ActionListener
{
	//ints

	//timers
	private Timer t;

	//Panels
	private GamePanel gamePanel;


	//user objects

	//**************************************************************************constructor	
	public GameWindow(GamePanel gp)
	{
		super("NTT v_0.01a: pre-Alpha");
		
		int wx = 1600;
		int wy = 900;
		//set specifications for frame
		
		this.setResizable(true);
		setFocusable(true);		
		setSize(wx, wy);
		
		//sets user class objects

		//panels
		gamePanel = gp;
		gamePanel.setSize(wx, wy);
		gamePanel.setFocusable(true);
		addKeyListener(gamePanel);
		addMouseListener(gamePanel);
		addMouseMotionListener(gamePanel);
		setVisible(true);

		//adds panels to the frame with layout
		add(gamePanel);			

		//timers
		t = new Timer(1, this);
		t.start();	
	}
	//methods
	//save files
	public void saveGame()
	{

	}
	//loads files
	public void loadGame()
	{

	}
	//returns to main menu
	public void returnMainMenu()
	{

	}
	//new game
	public void newGame(){

	}
	//exit game
	public void exitGame()
	{
		System.exit(0);
	}

	//listeners
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub	
		
		gamePanel.repaint();		
	}	
}