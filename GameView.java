//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

import java.awt.*;
import javax.swing.*;

/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out the actual game and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */
import javax.swing.JFrame;

public class GameView extends JFrame {
	
	/**
     * A reference to the game model 
     */
	private GameModel model;
	
	/**
     * A reference to the game controller
     */
	private GameController gameController;
	
	/**
     * An array used to keep track of the color of the dots
     */
	private DotButton [][] dots;
	
	/**
     * The text that will show the number of steps
     */
	private JLabel steps;
	
	/**
	* The button that let the player undo move
	*/
	private JButton undo;
	
	/**
	* The button that let the player redo move
	*/
	private JButton redo;

    /**
     * Constructor used for initializing the Frame
     * 
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel model, GameController gameController) {
		this.model = model;
		this.gameController = gameController;
		
		setTitle("Flood It -- the ITI 1121 version");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//creation and addition of panels to frame
		/**
		* The panel that contains the undo, redo, and setting
		*/
		JPanel gameplay = new JPanel();
		
		/**
		* The panel that contains the board
		*/
		JPanel board = new JPanel();
		
		/**
		* The panel that contains reset and quit buttons, and the number of steps
		* the player has taken
		*/
		JPanel gameRelated = new JPanel();
		
		this.add(gameplay, BorderLayout.NORTH);
		this.add(board);
		this.add(gameRelated, BorderLayout.SOUTH);
		
		//Setting the background of panels to white
		gameplay.setBackground(Color.WHITE);
		board.setBackground(Color.WHITE);
		gameRelated.setBackground(Color.WHITE);
		
		//Content of the gameplay manipulation panel
		
		/**
		* The button that let the player control the setting of the game
		*/
		JButton settings = new JButton("Settings");
		
		undo = new JButton ("Undo");
		redo = new JButton ("Redo");
		
		undo.setEnabled(false);
		redo.setEnabled(false);
		settings.addActionListener(gameController);
		undo.addActionListener(gameController);
		redo.addActionListener(gameController);
		settings.setBackground(Color.WHITE);
		undo.setBackground(Color.WHITE);
		redo.setBackground(Color.WHITE);
		gameplay.add(undo);
		gameplay.add(redo);
		gameplay.add(settings);
		
		//Content of the board panel
		/**
		* Size of the game model
		*/
		int size = this.model.getSize();

		
		//Keeps game within screen
		if ((size <= 25 && size >= 22)|| size>=55){
	    	board.setPreferredSize(new Dimension(610,610));
		}
		/**
		* Size of DotButtons
		*/
		int buttonSize;
		
		dots = new DotButton [size][size];
		board.setLayout(new GridLayout(size, size));
		board.setBorder(BorderFactory.createEmptyBorder(10,15,8,15));
		
		if (size > 25){buttonSize = 2;}
		else{buttonSize = 1;}
		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				dots[i][j] = new DotButton(i, j, this.model.getColor(i, j), buttonSize);
				board.add(dots[i][j]);
				dots[i][j].addActionListener(gameController);
			}
		}
		
		//Content of the gameRelated panel
		steps = new JLabel("Select initial dot");
		JButton reset = new JButton ("Reset");
		JButton quit = new JButton ("Quit");
		reset.addActionListener(gameController);
		quit.addActionListener(gameController);
		reset.setBackground(Color.WHITE);
		quit.setBackground(Color.WHITE);
		gameRelated.add(steps);
		gameRelated.add(reset);
		gameRelated.add(quit);
		
		setResizable(false); 
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		if (model.gameStarted()){
			this.update();
		}
    }
	
	/**
     * Set the enabled state of undo button
	 *
	 * @param state
     *            the enabled state to set to
     */
	 
	public void setUndoable(boolean state){
		undo.setEnabled(state);
	}
	
	/**
     * Set the enabled state of redo button
	 *
	 * @param state
     *            the enabled state to set to
     */
	 
	public void setRedoable(boolean state){
		redo.setEnabled(state);
	}
	
	/**
     * Reset the reference to the model to the one from undo or redo
	 *
	 * @param model
     *            the refernce to the model to be replaced with 
     */
	 
	public void setModel(GameModel model){
		this.model = model;
	}

    /**
     * update the status of the board's DotButton instances and the number of steps
	 * based on the current game model
     */
	
    public void update(){
		
		/**
		* Size of the game model
		*/
		int size = model.getSize();
		
		/**
		* Currently selected color of the model
		*/
		int selectedColor = model.getCurrentSelectedColor();
		
		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (model.isCaptured(i, j)){
					dots[i][j].setColor(selectedColor);
				}
			}
		}
		
		if (model.gameStarted()){
			steps.setText("Number of steps: " + model.getNumberOfSteps());
		}
		else{
			steps.setText("Select initial dot");
		}
    }

}