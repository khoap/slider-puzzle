package EightPuzzle;
import java.util.*;
/*
 * The OPEN list:
 * 6 main functions:
 * - add(Board): Add a board to the Open list
 * - contains(Board ): Check if the board has already existed on the list
 * - isEmpty(Board ): Check if the List is empty
 * - remove(Board ): To remove a specific board from the list
 * - removeFirst(): To remove the first element in the list
 * - getCost(Board ): Get the cost of a specific board on the list.
 */
public class OpenList {
    
	/*
	 * Class construction
	 */
    public OpenList() {}
    
    Vector <Board> queue = new Vector<Board>();
    
    /*
     * add(Board ) function.
     * - Check if the board has been existed in the list
     * 		+ If true, remove the existing board from list, then continue to add.
     * - If the queue is empty or board has biggest f_value just the board to list.
     * - Else we add the board to appropreate position.		
     */
    public void add(Board board) {
        if(contains(board) == true) {
            queue.remove(board);
        }
        if(queue.size() == 0 
           || board.get_f_value() >= ((Board)queue.lastElement()).get_f_value()) {
            queue.addElement(board);
        }
        else {
            for(int i = 0; i < queue.size(); i++) {
                if(board.get_f_value() <= ((Board)queue.elementAt(i)).get_f_value()) {
                    queue.insertElementAt(board,i);
                    break;
                }
            }
        }
        
    }
    
    /*
     * Check if the board is in the list yet.
     */
    public boolean contains(Board board) {
        return queue.contains(board);
    }
    
    /*
     * Check if the list is empty.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /*
     * Remove specific board from the list.
     */
    public void remove(Board board) {
        queue.remove(board);
    }
    
    /*
     * Remove the first element of the list
     */
    public Board removeFirst() {
        if(queue.size() == 0)
            return null;
        return (Board)queue.remove(0);
    }
    
    /*
     * Get the f_value of the board from the list.
     */
    public int getCost(Board board) {
        if(contains(board) == false) 
            return -1;
        int index = queue.indexOf(board);
        return ((Board)queue.elementAt(index)).get_f_value(); 
    }

}
