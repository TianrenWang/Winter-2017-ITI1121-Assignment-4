//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the followiung information:
 * - the state of all the ``dots'' on the board (color, captured or not)
 * - the size of the board
 * - the number of steps since the last reset
 * - the current color of selection
 *
 * The model provides all of this informations to the other classes trough 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */

import java.io.*;
public class GameModel implements Cloneable, Serializable{


    /**
     * predefined values to capture the color of a DotInfo
     */
    public static final int COLOR_0           = 0;
    public static final int COLOR_1           = 1;
    public static final int COLOR_2           = 2;
    public static final int COLOR_3           = 3;
    public static final int COLOR_4           = 4;
    public static final int COLOR_5           = 5;
    public static final int NUMBER_OF_COLORS  = 6;

	/** 
	* maintains the reference to each dot in the game
	*/
	private DotInfo [][] dots;
	
	/** 
	* the size of the board
	*/
	private int size;
	
	/** 
	* current number of steps the player has taken
	*/
	private int steps;
	
	/** 
	* current number of dots the player captured
	*/
	private int dotsCaptured;
	
	/** 
	* the current selected color
	*/
	private int selectedColor;
	
	/** 
	* whether the player has chosen the starting dot
	*/
	private boolean gameStarted;
	
	/**
     * If true, the board is played as torus. If false, the board is planar
     */
	private boolean torus;
	
	/**
     * If true, the game captures dots diagonally. If false, captures orthogonally
     */
	private boolean diagonal;
    /** 
    * Stores a reference to the undoable states when saving the game
    */
    private GenericLinkedStack<GameModel> undoable;
    /** 
    * Stores a reference to the redoable states when saving the game
    */
    private GenericLinkedStack<GameModel> redoable;
  
    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {
		this.size = size;
		torus = false;
		diagonal = false;
		
		dots = new DotInfo[size][size];
		
		reset();
    }


    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){
		for (int i = 0; i < size; i++){
			for (int j = 0; j <size; j++){
				/** 
				* a random value between 0 - 5
				*/
				int random = (int) Math.floor(Math.random() * 6);
				
				dots[i][j] = new DotInfo(i, j, random);
			}
		}
		gameStarted = false;
		steps = 0;
		dotsCaptured = 0;
    }


    /**
     * Getter method for the size of the game
     * 
     * @return the value of the attribute sizeOfGame
     */   
    public int getSize(){
		return size;
    }
	
	/**
     * Getter method for the torus state of the game
     * 
     * @return the value of class instance variable "torus"
     */   
    public boolean isTorus(){
		return torus;
    }
	
	/**
     * Setter method for the torus state of the game
     * 
     * @param t
     *            the boolean state of the torus
     */   
    public void setTorus(boolean t){
		torus = t;
    }
	
	/**
     * Getter method for the capturing style of the game
     * 
     * @return the value of class instance variable "diagonal"
     */   
    public boolean isDiagonal(){
		return diagonal;
    }
	
	/**
     * Setter method for the capturing style of the game
     * 
     * @param d
     *            the boolean state of whether dots are captured diagonally
     */   
    public void setDiagonal(boolean d){
		diagonal = d;
    }

    /**
     * returns the current color of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public int getColor(int i, int j){
		return dots[i][j].getColor();
    }

    /**
     * returns true is the dot is captured, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isCaptured(int i, int j){
		return dots[i][j].isCaptured();
    }

    /**
     * Sets the status of the dot at coordinate (i,j) to captured
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void capture(int i, int j){
		dots[i][j].setCaptured(true);
        dotsCaptured++;
	}


    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){
		return steps;
    }

    /**
     * Setter method for currentSelectedColor
     * 
     * @param val
     *            the new value for currentSelectedColor
    */   
    public void setCurrentSelectedColor(int val) {
		selectedColor = val;
    }

    /**
     * Getter method for currentSelectedColor
     * 
     * @return currentSelectedColor
     */   
    public int getCurrentSelectedColor() {
		return selectedColor;
    }


    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {
		return dots[i][j];
    }
	
	/**
     * Getter method for whether the player has chosen the initial dot
     *
     * @return gameStarted
     */   
    public boolean gameStarted() {
		return gameStarted;
    }
	
	/**
	 * Sets gameStarted to true, capture the initial dot, and set the
	 * current selected color to the color of the initial dot
     *
	 * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void startGame(int i, int j) {
		gameStarted = true;
		capture(i, j);
		selectedColor = dots[i][j].getColor();
    }


   /**
     * The method <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the payer selected a new color.
     */
     public void step(){
		 steps++;
    }
 
   /**
     * The metod <b>isFinished</b> returns true iff the game is finished, that
     * is, all the dots are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){
        return dotsCaptured == size*size;
    }
	
	/**
     * The method <b>clone</b> returns a deep copy clone of this game model
	 * state.
     *
     * @return deep copy clone of this game model
     */

	public GameModel clone() throws CloneNotSupportedException{
		GameModel clone = (GameModel) super.clone();
		clone.dots = new DotInfo[size][size];
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				clone.dots[i][j] = dots[i][j].clone();
			}
		}
		return clone;
	}

    /**
     * Setter method for the undoable states of the game to be saved
     * 
     * @param undoable
     *            the undoable states of the game to be saved
     */   
    public void setUndoableStates(GenericLinkedStack<GameModel> undoable){
        this.undoable = undoable;
    }
    /**
     * Setter method for the redoable states of the game to be saved
     * 
     * @param redoable
     *            the redoable states of the game to be saved
     */  
    public void setRedoableStates(GenericLinkedStack<GameModel> redoable){
        this.redoable = redoable;
    }
    /**
     * Getter method for the undoable states of the saved game
     * 
     * @return the undoable states
     */  
    public GenericLinkedStack<GameModel> getUndoableStates(){
       return undoable;
    }
     /**
     * Getter method for the redoable states of the saved game
     * 
     * @return the redoable states
     */  
    public GenericLinkedStack<GameModel> getRedoableStates(){
       return redoable;
    }
	
       /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){
        StringBuffer b = new StringBuffer();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                b.append(getColor(i, j));
                if (dots[i][j].isCaptured()){
					b.append("C ");
				}
				else{
					b.append("  ");
				}
            }
            b.append("\n");
        }
        return b.toString();
    }
}
