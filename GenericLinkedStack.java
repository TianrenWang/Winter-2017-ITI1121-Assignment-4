//Student1 Name: Tianren Wang
//Student1 ID: 6040795
//Student2 Name: Allie LaCompte
//Student2 ID: 5270100
//ITI 1121-A00
//Assignment 4

/**
 * Implementation of the stack class used for the flooding strategy
 * This is a linked stack data type.
 *
 * @author Frank Tianren Wang and Allie LaCompte, University of Ottawa
 */

import java.io.*;

public class GenericLinkedStack<E> implements Stack<E>, Serializable {
	
	/**
     * An internal class to define the node structure
     */
    private static class Elem<T> implements Serializable{
		
		/**
		* The value stored by each node
		*/
        private T value;
		
		/**
		* The reference to the next node in the direction of the top of stack
		*/
        private Elem<T> next;
		
		/**
		* The reference to the next node in the direction of the bottom of stack
		*/
		private Elem<T> previous;
		
		/**
		 * Constructor
		 *
		 * @param value the value stored by the node
		 * @param next the node that is next to the constructed node towards the top direction of stack
		 */
        private Elem(T value, Elem<T> next) {
            this.value = value;
            this.next = next;
			this.previous = null;
        }
    }
	
	/**
     * The reference to the bottom node of the stack
     */
    private Elem<E> bottom;
	
	/**
     * The reference to the top node of the stack
     */
    private Elem<E> top;
	
	/**
     * The size of the stack
     */
	private int size;

	/**
	* Constructor
	*/
	public GenericLinkedStack () {
		size = 0;
		bottom = null;
		top = null;
    }
	
	/**
     * Returns the size of the stack.
     *
     * @return The size of the stack.
     */
    public int size() {
        return size;
    }
	
	/**
     * Puts an element onto the top of this stack.
     *
     * @param value the value of the node to be put onto the top of this stack.
     */
    public void push(E value) {
		
		//Exception
		if (value == null){
			throw new NullPointerException("cannot push a node without a value");
		}
		
		//Logic of the method
        Elem<E> newElem;
        newElem = new Elem<E>(value, null );

        if (top == null) {
            bottom = top = newElem;
        } else {
            top.next = newElem;
			newElem.previous = top;
            top = newElem;
        }
		size++;
    }
	
	/**
     * Removes and returns the element at the top of this stack.
     *
     * @return The top element of this stack.
     */
    public E pop() {
		
		//Exception
		if (isEmpty()){
			throw new EmptyStackException();
		}
		
		//Logic of the method
        E result = top.value;
        if (size == 1) {
            top = bottom = null;
        } else {
            top = top.previous;
			top.next = null;
        }
		size--;
        return result;
    }
	
	/**
     * Returns a reference to the top element; does not change
     * the state of this Stack.
     *
     * @return The top element of this stack without removing it.
     */
    public E peek(){
		
		if (isEmpty()){
			throw new EmptyStackException();
		}
		
		return top.value;
	}
	
	/**
     * Tests if this Stack is empty.
     *
     * @return true if this Stack is empty; and false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

}