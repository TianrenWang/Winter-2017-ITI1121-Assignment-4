//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

import java.io.*;
import java.util.Scanner;

/**
 * The class <b>FloodIt</b> launches the game
 *
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */
 
public class FloodIt {

   /**
     * <b>main</b> of the application. Creates the instance of  GameController 
     * and starts the game. If a game size (less than 10), a non integer, or mulriple 
     * paremeters are passed as parameter, 12 is 
     * used as the board size. Otherwise, the argument size is used as the board
	 * size.
     * 
     * @param args
     *            command line parameters
     */
    
	public static void main(String[] args) {
	        StudentInfo.display();
	        int size = 12;
	        if (args.length ==1){
		        try{
		            size = Integer.parseInt(args[0]);
		            if (size < 10){
		            	System.out.println("Cannot create a game of size smaller than 10.");
		            	System.out.println("Game of default size 12 created.");
		            	new GameController(12);
		        	}
		        	else{
		        		new GameController(size);
		        	}
		        }
		        catch(NumberFormatException e){
		            System.out.println("Invalid size");
		            System.out.println("Game of default size 12 created.");
		            new GameController(size);
		           
		        }
	    	}
	    	else{
	    	    System.out.println("Invalid size");
		        System.out.println("Game of default size 12 created.");
	            new GameController(size);
	        }
	   }
}

    