//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * In the application <b>FloodIt</b>, a <b>DotButton</b> is a specialized color of
 * <b>JButton</b> that represents a dot in the game. It can have one of six colors
 * 
 * The icon images are stored in a subdirectory ``data''. We have 3 sizes, ``normal'',
 * ``medium'' and ``small'', respectively in directory ``N'', ``M'' and ``S''.
 *
 * The images are 
 * ball-0.png - grey icon
 * ball-1.png - orange icon
 * ball-2.png - blue icon
 * ball-3.png - green icon
 * ball-4.png - purple icon
 * ball-5.png - red icon
 *
 *  <a href=
 * "http://developer.apple.com/library/safari/#samplecode/Puzzler/Introduction/Intro.html%23//apple_ref/doc/uid/DTS10004409"
 * >Based on Puzzler by Apple</a>.
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */

public class DotButton extends JButton {
	
	/**
     * The length of the side of the small sized dot in pixels
     */
	 private static final int SMALL_SIZE = 11;
	 
	 /**
     * The length of the side of the medium sized dot in pixels
     */
	 private static final int MEDIUM_SIZE = 28;
	 
	 /**
     * The length of the side of the normal sized dot in pixels
     */
	 private static final int NORMAL_SIZE = 40;
	 
	/**
     * row # of the dot
     */
	private int row;
	
	/**
     * column # of the dot
     */
	private int column;

	/**
     * color of the dot
     */
	private int color;
	
	/**
     * size of the dot icon. N = 0, M = 1, S = 2
     */
	private int iconSize;
	
    /**
     * Constructor used for initializing a cell of a specified color.
     * 
     * @param row
     *            the row of this Cell
     * @param column
     *            the column of this Cell
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */

    public DotButton(int row, int column, int color, int iconSize) {
		this.row = row;
		this.column = column;
		this.color = color;
		this.iconSize = iconSize;
		setActionCommand(color+"");
		
		//Modifying the aesthetics of the button
		setImageIcon(color);
		if (iconSize == 1){
			setPreferredSize(new Dimension(MEDIUM_SIZE, MEDIUM_SIZE));
		}
		else if (iconSize == 2){
			setPreferredSize(new Dimension(SMALL_SIZE, SMALL_SIZE));
		}
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.WHITE, 0));
	}
 
    /**
     * Changes the cell color of this cell. The image is updated accordingly.
     * 
     * @param color
     *            the color to set
     */

    public void setColor(int color) {
		if (this.color != color){
			this.color = color;
			setImageIcon(color);
		}
	}

    /**
     * Getter for color
     *
     * @return color
     */
    public int getColor(){
		return color;
    }
 
    /**
     * Getter method for the attribute row.
     * 
     * @return the value of the attribute row
     */

    public int getRow() {

		return row;

    }

    /**
     * Getter method for the attribute column.
     * 
     * @return the value of the attribute column
     */

    public int getColumn() {

		return column;

    }
	
	/**
     * Changes the image icon of this dot.
     * 
     * @param color
     *            the color to set
     */
	public void setImageIcon(int color){
		/**
		* Maintains the file path of the icons in the data subdirectory
		*/
		String filePath = "data";
		
		//Determines which of the icon size subdirectory the icon should be obtained from
		if (iconSize == 0){
			filePath = filePath + "/N/";
		}
		else if (iconSize == 1){
			filePath = filePath + "/M/";
		}
		else if (iconSize == 2){
			filePath = filePath + "/S/";
		}

		//Set the icon of the correct color
		setIcon(new ImageIcon(filePath + "ball-" + color + ".png"));
	}
}