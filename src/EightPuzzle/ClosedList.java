package EightPuzzle;

import java.util.*;
/*
 * CLOSE list will be maintained by HashTable.
 * [We can also do the Queue for CLOSE List - But the Hash Table work better here]
 * There are four main functions for the CLOSE List:
 * - add(Board ): Add a board to CLOSE List
 * - remove(Board ): Remove a board from CLOSE list
 * - contains(Board ): Check if the board has been existed in the list
 * - getCost(Board ): get the f_value of the board on the CLOSE List
 */
public class ClosedList {
  protected Hashtable<Board, Board> nodes = new Hashtable<Board, Board>();
  
  /*
   * Class construction
   */
  public ClosedList() {}

  /*
   * add(Board ):
   * - If the board already existed on the list, then:
   * 	+ If f_value of the board is <= f_value of the existing board, 
   * 		then remove the board from the hashtable
   * 	+ If f_value of the board is > f_value of the existing board
   * 		then exchange their f_value, return.
   * - Put the board to the list
   * 
   */
  public void add(Board board) {
    nodes.put(board, board);
  }
		
  /*
   * Check if the board is in the CLOSE List or not.
   * Return True if it does, and False if does not.
   */
  public boolean contains(Board board) {
    return nodes.contains(board);
  }
	
  /*
   * Return the f_value of the board on Close List
   */
  public int getCost(Board board) {
    if (contains(board))
      return ((Board)nodes.get(board)).get_f_value();
    return -1;
  }
	
  /*
   * Remove the board from the CLOSE List
   */
  public void remove(Board board) {
    nodes.remove(board);
  }
	
}