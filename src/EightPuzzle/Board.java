package EightPuzzle;
import java.util.*;

/*
 * Board of EightPuzzle.
 * - Board can be represented by one dimension array.
 * - There are four legal moves: LEFT, RIGHT, UP, DOWN
 * - corresponding to each moving position -1,1,-3,3 respectively.
 * - The index (Position) of the tile: (X,Y) where X is horizontal dimension, Y, vertical dimension.
 * - Blank position can be found by scanning value 0 on the puzzle.
 * - Board can be derived to find its successors.
 * - Board can be pre-evaluate to find its "f" value.
 * - The path length of current Board can be found by tracing through the path from initial state to current state.
 * - Representation of the Board as 2 dimension matrix.
 * - Predetermine if the board is solvable.
 * - 
 */

public class Board {
	
	protected int[] board;
	
	/*
	 * Define legalMoves of the tile on the board.
	 */
	protected static final int UP = -3;
	protected static final int DOWN = 3;
	protected static final int RIGHT = 1;
	protected static final int LEFT = -1;
	
	protected static final int[] legalMoves = {LEFT,RIGHT,UP,DOWN};
	
	protected Board parent;
	
	/*
	 * Board construction:
	 */
	public Board (int[] board, Board parent) {
		this.board = board;	
		this.parent = parent;
	}

	public Board (int[] board) {
		this(board,null);
	}

	public Board getParent() {
		return parent;
	}
	
	/*
	 * Redefine equals function for two object.
	 * Return true if two Boards are equal
	 */
	public boolean equals(Object aBoard) {
		return ( aBoard!= null
				&& aBoard instanceof Board
				&& Arrays.equals(board, ((Board)aBoard).board));
	}
	/* ----------------------------------------------------*/
	
	/*
	 * Determine if the Puzzle if solvable. 
	 * The eight puzzle is solvable if the number of inversions is even.
	 * This code is obtained from: www.cs.lmu.edu/~ray/classes/ai/aissignment/1
	 */
		
	public boolean isSolvable() {
		boolean solvable = true;
		for (int i = 0; i < board.length; i++) {
			if( board[i] == 0) continue;
			for(int j = i + 1; j < board.length; j++) {
				if(board[j] == 0 ) continue;
				if(board[i] > board[j]) solvable = !solvable;
			}
		}
		return solvable;
	}
	
	/*
	 * Find a location of a tile based on its (x,y) on Board
	 */
	public int getTileAt(int x, int y) {
		return y* 3 + x;
	}
	
	/*
	 * ---------------------------------------------------
	 * Find a BlankPosition, BlankPostion at X, BlankPosition at Y.
	 */
	public int getBlankPositionX() {
		return getBlankPosition() % 3;
	}
	
	public int getBlankPositionY() {
		return (int)Math.floor(getBlankPosition()/3);
	}
	
	public int tempBlankPosition = -1;
	public int getBlankPosition() {
		if(tempBlankPosition < 0) {	
			int i = -1;
			for(i = 0; i<board.length && board[i] != 0; i++) {
			}
			tempBlankPosition = i;
		}
		return tempBlankPosition;
	}
	
	/* ---------------------------------------------------- */
	
	/*
	 * Find successors of the Board
	 *  3 2 0						 3 0 2
	 *  1 4 5	-Move 0 to left- --> 1 4 5
	 *  8 7 6 						 8 7 6		
	 *  ---- newPosition = -1 + 2 = 1 ---- 
	 *  newBoard[blankPositon] = board[newPosition] = board[1] = 2.
	 *  newBoard[newPosition] = 0				
	 */
	public Board getSuccessors(int moveDirection) {
		int newPosition = moveDirection + getBlankPosition();
		if(newPosition < 0 || newPosition >= board.length) 
			return null;
		if((getBlankPositionX() == 0 && moveDirection == LEFT) || 
			(getBlankPositionX() == 2 && moveDirection == RIGHT))
			return null;
		int[] newBoard = new int[board.length];
		System.arraycopy(board, 0, newBoard, 0, board.length);
		newBoard[getBlankPosition()] = board[newPosition];
		newBoard[newPosition] = 0;
		
		return new Board(newBoard,this);
	}
	
	public Vector<Board> getSuccesors() {
		Vector<Board> vec = new Vector<Board>() ;
		for(int i = 0; i < legalMoves.length; i++) {
			Board successor = getSuccessors(legalMoves[i]);
			if(successor != null)
				vec.addElement(successor);
		}
		return vec;
	}
	
	/* ------------------------------------------------------------------*/
	
	/* 
	 * Get length of Path from Current Node to Start Node
	 */
	public Vector<Board> findPath;
	public Vector<Board> getPathFromStartNode() {
		if(findPath == null) {
			if(parent == null)
				findPath = new Vector<Board>();
			else 
				findPath = parent.getPathFromStartNode();
			findPath.add(this);
		}
		return (Vector<Board>)findPath.clone();
	}
	
	public int getPathLenth() {
		if(findPath == null) 
			getPathFromStartNode();
		return findPath.size() - 1;
	}
	/*--------------------------------------------------------------------------*/
	
	/*
	 * Get and set f_value
	 */
	public int f_value;
	
	public int get_f_value() {
		return f_value;
	}
	
	public void set_f_value(int f_value) {
		this.f_value = f_value;
	}
	
	/* -----------------------------------------------------------------------------------*/
	
	/*
	 * Representation of the puzzle
	 */
	
	public String toString() {
		return new Formatter().format("\n  %d %d %d  \n  %d %d %d \n  %d %d %d \n",
				board[0], board[1], board[2], board[3],board[4],
				board[5], board[6], board[7], board[8]).out().toString();
	}
	
	/*
	 * Redefine hasCode function
	 */
	
	public int hasCode() {
		return toString().hashCode();
	}
	
}
	

