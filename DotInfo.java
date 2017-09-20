//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

/**
 * The class <b>DotInfo</b> is a simple helper class to store the initial color and state
 * (captured or not) at the dot position (x,y)
 *
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */
import java.io.*;

public class DotInfo implements Cloneable, Serializable{

	/**
     * color of the dot represented using integer (1 - 6)
     */
	private int color;
	
	/**
     * state of the dot (whether it is captured or not)
     */
	private boolean captured;
	
	//In Java, x value increases from left to right, y value increases from top to bottom
	//(0, 0) is at the top left of the frame
	
	/**
     * x coordinate of the dot
     */
	private int x;
	
	/**
     * y coordinate of the dot
     */
	private int y;

    /**
     * Constructor 
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param color
     *            the initial color
     */
    public DotInfo(int x, int y, int color){
		this.x = x;
		this.y = y;
		this.color = color;
		captured = false;
    }

    /**
     * Getter method for the attribute x.
     * 
     * @return the value of the attribute x
     */
    public int getX(){
		return x;
    }
    
    /**
     * Getter method for the attribute y.
     * 
     * @return the value of the attribute y
     */
    public int getY(){
		return y;
    }
    
     /**
     * Setter for captured
     * @param captured
     *            the new value for captured
     */
    public void setCaptured(boolean captured) {
		this.captured = captured;
    }

    /**
     * Get for captured
     *
     * @return captured
     */
    public boolean isCaptured(){
		return captured;
    }

    /**
     * Get for color
     *
     * @return color
     */
    public int getColor() {
		return color;
    }
	
	/**
     * The method <b>clone</b> returns a deep copy clone of this dot
     *
     * @return deep copy clone of this dot
     */

	public DotInfo clone() throws CloneNotSupportedException{
		return (DotInfo) super.clone();
	}
 }