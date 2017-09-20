//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computes the next step of the game, and  updates model and view.
 *
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class GameController implements ActionListener {
	
	/**
     * A reference to the game model
     */
	private GameModel model;
	
	/**
     * A reference to the game viewer
     */
	private GameView view;
	
	/**
     * The settings window
     */
	private JFrame settingsWindow;
	
	/**
     * Stack that keeps track of the undoable game models
     */
	private GenericLinkedStack<GameModel> undoableStates;
	
	/**
     * Stack that keeps track of the redoable game models
     */
	private GenericLinkedStack<GameModel> redoableStates;

	/**
     * settings panel
     */
	private JPanel options;

    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {
    	
    	// Loads saved game, if a saved game exists and the size argument passed is the same as that of the saved game
		try{ 
			File file = new File("savedGame.ser");
			FileInputStream savedGame = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(savedGame);
			model = (GameModel) in.readObject();

			// Verifies size of saved game is the same as the size argument passed
			if(model.getSize() == size){
				undoableStates = model.getUndoableStates();
				redoableStates = model.getRedoableStates();
			}
			// If size of saved game and size argument passed are not the same, creates new game
			else{
				model=new GameModel(size);
				undoableStates = new GenericLinkedStack<GameModel>();
				redoableStates = new GenericLinkedStack<GameModel>();
			}
			model.setUndoableStates(null);
			model.setRedoableStates(null);
			in.close();
			file.delete();
		}
		// If a saved game does not exist, creates new game
		catch(Exception ex){
			model=new GameModel(size);
			undoableStates = new GenericLinkedStack<GameModel>();
			redoableStates = new GenericLinkedStack<GameModel>();
		}
		// Creates the game view
		view = new GameView (model, this);

		if (undoableStates.size()!=0){
			view.setUndoable(true);
		}
		if (redoableStates.size()>1){
			view.setRedoable(true);
		}
		options = setupSettingsPanel();
    }

    /**
     * resets the game
     */
    public void reset(){
		model = new GameModel (model.getSize());
		view.setVisible(false);
		view.dispose();
		options = setupSettingsPanel();
		view = new GameView (model, this);
    }

    /**
     * Callback used when the user clicks a button 
	 * (reset, quit, undo, redo, settings)
     *
     * @param e
     *            the ActionEvent
     */

    public void actionPerformed(ActionEvent e) {
		String command; 
		command = e.getActionCommand();
		if (command.equals("Reset")) {
			undoableStates = new GenericLinkedStack<GameModel>();
			redoableStates = new GenericLinkedStack<GameModel>();
			reset();
		} 
		else if (command.equals("Quit") ) {
			// Saves the game
			try{ 
				model.setUndoableStates(undoableStates);
				model.setRedoableStates(redoableStates);
				FileOutputStream savedGame = new FileOutputStream("savedGame.ser");
				ObjectOutputStream out = new ObjectOutputStream(savedGame);
				out.writeObject(model);
				out.close();
			}
			catch(IOException ex){
			}
			quit();
		}
		else if (command.equals("Undo")) {
			redoableStates.push(undoableStates.peek());
			model = undoableStates.pop();
			view.setModel(model);
			view.update();
		}
		else if (command.equals("Redo")) {
			undoableStates.push(redoableStates.pop());
			model = redoableStates.peek();
			view.setModel(model);
			view.update();
		}
		else if (command.equals("Settings")) {
			JOptionPane.showMessageDialog(view, options);
		}
		else if (command.equals("Plane")) {
			model.setTorus(false);
		}
		else if (command.equals("Torus")) {
			model.setTorus(true);
		}
		else if (command.equals("Orthogonal")) {
			model.setDiagonal(false);
		}
		else if (command.equals("Diagonal")) {
			model.setDiagonal(true);
		}
		
		//If the player makes a move on the board
		else{
			//If the player hasn't selected an initial dot
			if (!model.gameStarted()){
				//Updates the undo and redo stacks
				try{
					undoableStates.push(model.clone());
				}
				catch(CloneNotSupportedException error){
					System.out.println("Attempting to clone an object that did not implement Cloneable");
				}
				model.startGame(((DotButton)e.getSource()).getRow(), ((DotButton)e.getSource()).getColumn());
				flood(model.getCurrentSelectedColor());
				view.update();
			}
			//Once the player selects a dot
			else{
				//capture dots
				//can only capture dots if the selected dot is not captured
				if (!model.isCaptured(((DotButton)e.getSource()).getRow(), ((DotButton)e.getSource()).getColumn())){
					selectColor(Integer.parseInt(command));
				}
			}
			
			//If the player makes a move on the board, then all of the previous redo states
			//are eliminated, and the current model is pushed onto the redo stack
			//the bottom of the redo stack will never be popped, it will only be peeked
			redoableStates = new GenericLinkedStack<GameModel>();
			try{
					redoableStates.push(model.clone());
			}
			catch(CloneNotSupportedException error){
				System.out.println("Attempting to clone an object that did not implement Cloneable");
			}
		}
		
		//This portion sets the enabled state of the undo and redo buttons
		//Set the button to disabled if the stack associated with it is empty
		if (undoableStates.isEmpty()){
			view.setUndoable(false);
		}
		else{
			view.setUndoable(true);
		}
		if (redoableStates.size() <= 1){
			view.setRedoable(false);
		}
		else{
			view.setRedoable(true);
		}
    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives two options: start a new game, or exit
     * @param color
     *            the newly selected color
     */
    public void selectColor(int color){
		//This only gets executed if the player performs a non-useless move
		//A move is considered useless if the color player selects the same color as
		//the currently selected color
		if (model.getCurrentSelectedColor() != color){
			//Updates the undo and redo stacks
			try{
				undoableStates.push(model.clone());
			}
			catch(CloneNotSupportedException error){
				System.out.println("Attempting to clone an object that did not implement Cloneable");
			}
			
			//Flood the board, increase the # of steps by 1, update the selected color, and
			//update the window
			flood(color);
			model.step();
			model.setCurrentSelectedColor(color);
			view.update();
		}
		if (model.isFinished()){
			//Create JOptionPane popup
			Object[] buttons = { "Reset", "Quit" };
			int input = JOptionPane.showOptionDialog(view, "Congratulation! You Completed in " + model.getNumberOfSteps() + " steps!", "Congratulation!", 
			JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);
			if(input == 0){
				reset();
			}
			else{
				quit();
			}
		}
    }

	/**
     * <b>capture</b> is the method that executes the capturing algorithm. Any
	 * dots of the selected color that are adjacent to already captured dots
	 * are captured.
     * @param color
     *            the newly selected color
     */
	private void flood(int color){
		/**
		* size of the model
		*/
		int size = model.getSize();
		
		/**
		* the stack used in the algorithm to capture dots
		*/
		GenericLinkedStack<DotInfo> stack = new GenericLinkedStack<DotInfo>();
		
		//Puts all of the captured dots on the stack
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (model.isCaptured(i,j)){
					stack.push(model.get(i,j));
				}
			}
		}
		
		//Continuously push and remove elements from the stack
		//to capture the appropriate dots
		while (!stack.isEmpty()){
			/**
			* the reference to the dot that was just popped
			*/
			DotInfo d = stack.pop();
			
			/**
			* x coordinate of the popped dot
			*/
			int x = d.getX();
			
			/**
			* y coordinate of the popped dot
			*/
			int y = d.getY();
			
			/**
			* keeps track of the torus state of the game
			*/
			boolean torus = model.isTorus();

			if (x > 0){
				checkCapturePush(stack, color, x-1, y);
			}
			else if (torus && x == 0){
				checkCapturePush(stack, color, size-1, y);
			}
			
			//Checks and captures the dot right of the popped dot and pushes it onto the stack
			//Takes into account of torus
			if (x < size - 1){
				checkCapturePush(stack, color, x+1, y);
			}
			else if (torus && x == size - 1){
				checkCapturePush(stack, color, 0, y);
			}
			
			//Checks and captures the dot above the popped dot and pushes it onto the stack
			//Takes into account of torus
			if (y > 0){
				checkCapturePush(stack, color, x, y - 1);
			}
			else if (torus && y == 0){
				checkCapturePush(stack, color, x, size - 1);
			}
			
			//Checks and captures the dot below the popped dot and pushes it onto the stack
			//Takes into account of torus
			if (y < size - 1){
				checkCapturePush(stack, color, x, y+1);
			}
			else if (torus && y == size - 1){
				checkCapturePush(stack, color, x, 0);
			}
			
			if (model.isDiagonal()){//This part will work if capturing DIAGONALLY
			
				/**
				* keeps track of the x coordinate of the target dot to be captured
				*/
				int xTarget;
				
				/**
				* keeps track of the y coordinate of the target dot to be captured
				*/
				int yTarget;
			
				//Checks and captures the dot top left of the popped dot and pushes it onto the stack
				//Takes into account of torus
				if (x > 0 && y > 0){
					checkCapturePush(stack, color, x-1, y-1);
				}
				else if (torus){
					xTarget = x - 1;
					yTarget = y - 1;
					if (xTarget < 0){
						xTarget = size-1;
					}
					if (yTarget < 0){
						yTarget = size-1;
					}
					checkCapturePush(stack, color, xTarget, xTarget);
				}
				
				//Checks and captures the dot bottom right of the popped dot and pushes it onto the stack
				//Takes into account of torus
				if (x < size - 1 && y < size - 1){
					checkCapturePush(stack, color, x+1, y+1);
				}
				else if (torus){
					xTarget = x + 1;
					yTarget = y + 1;
					if (xTarget == size){
						xTarget = 0;
					}
					if (yTarget == size){
						yTarget = 0;
					}
					checkCapturePush(stack, color, xTarget, xTarget);
				}
				
				//Checks and captures the dot top right of the popped dot and pushes it onto the stack
				//Takes into account of torus
				if (y > 0 && x < size - 1){
					checkCapturePush(stack, color, x+1, y-1);
				}
				else if (torus){
					xTarget = x + 1;
					yTarget = y - 1;
					if (xTarget == size){
						xTarget = 0;
					}
					if (yTarget < 0){
						yTarget = size - 1;
					}
					checkCapturePush(stack, color, xTarget, xTarget);
				}
				
				//Checks and captures the dot bottom left of the popped dot and pushes it onto the stack
				//Takes into account of torus
				if (x > 0 && y < size - 1){
					checkCapturePush(stack, color, x-1, y+1);
				}
				else if (torus){
					xTarget = x - 1;
					yTarget = y + 1;
					if (xTarget < 0){
						xTarget = size - 1;
					}
					if (yTarget == size){
						yTarget = 0;
					}
					checkCapturePush(stack, color, xTarget, xTarget);
				}
			}
		}
	}
	
	/**
     * [Checks] if the target capturing dot is not captured and if that dot has the same
	 * color as the selected color. If these two conditions are satisfied, the function
	 * [captures] the dot and [pushes] it onto the stack.
	 * @param stack
     *            the reference to the stack that is manipulated
	 * @param color
     *            the selected color
	 * @param x
     *            the x-coordinate of the dot interested in capturing
	 * @param y
     *            the y-coordinate of the dot interested in capturing
     */
	 private void checkCapturePush(GenericLinkedStack<DotInfo> stack, int color, int x, int y){
		 if (!model.isCaptured(x, y) && color == model.getColor(x, y)){
			model.capture(x, y);
			stack.push(model.get(x, y));
		 }
	 }
	
    /**
     * Sets up the settings model
     * 
     * @return the panel to be used by JOptionsPane when the settings button is pressed
     */  
	private JPanel setupSettingsPanel(){
		
		//Create and set up the panel
		/**
		* The settings panel
		*/
		options = new JPanel(new GridLayout(0, 1));
		
		//Create the radio buttons and add them to the options panel
		
		//Plane vs Torus
		/**
		* The button that let the player select planar-style board
		*/
		JRadioButton planeButton = new JRadioButton("Plane");
		planeButton.setActionCommand("Plane");
		planeButton.addActionListener(this);
		
		/**
		* The button that let the player select torus-style board
		*/
		JRadioButton torusButton = new JRadioButton("Torus");
		torusButton.setActionCommand("Torus");
		torusButton.addActionListener(this);
		
		/**
		* The ButtonGroup that controls the selection between planar or torus board
		*/
		ButtonGroup planarity = new ButtonGroup();
		planarity.add(planeButton);
		planarity.add(torusButton);
		planeButton.setSelected(!model.isTorus());
		torusButton.setSelected(model.isTorus());
		
		options.add(new JLabel("Play on plane or torus?"));
		options.add(planeButton);
		options.add(torusButton);
		
		//Orthogonal vs Diagonal
		/**
		* The button that let the player select orthogonal dot-capturing gameplay
		*/
		JRadioButton orthogonalButton = new JRadioButton("Orthogonal");
		orthogonalButton.setActionCommand("Orthogonal");
		orthogonalButton.addActionListener(this);
		
		/**
		* The button that let the player select diagonal dot-capturing gameplay
		*/
		JRadioButton diagonalButton = new JRadioButton("Diagonal");
		diagonalButton.setActionCommand("Diagonal");
		diagonalButton.addActionListener(this);
		
		/**
		* The ButtonGroup that controls the selection between orthogonal or diagonal capturing gameplay
		*/
		ButtonGroup directionality = new ButtonGroup();
		directionality.add(orthogonalButton);
		directionality.add(diagonalButton);
		orthogonalButton.setSelected(!model.isDiagonal());
		diagonalButton.setSelected(model.isDiagonal());
		
		//JLabel orthoOrDiag = new JLabel("Orthogonal or diagonal moves?");
		
		options.add(new JLabel("Orthogonal or diagonal moves?"));
		options.add(orthogonalButton);
		options.add(diagonalButton);

		return options;
	}
	
	/**
     * quits the game
     */
	public void quit() {
		System.exit(0);
    }
}