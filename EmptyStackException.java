//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

public class EmptyStackException extends RuntimeException{
	
	/**
	* Constructor
	*/
	public EmptyStackException(){
		super("Cannot pop or peek into an empty stack");
	}
}