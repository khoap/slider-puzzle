package EightPuzzle;
import java.util.*;
import java.io.*;


/*
 * EightPuzzle class will consist of some important function to solve the Puzzle
 * - BFSSearch: Using the Best-First search technique to find the solution for the Puzzle
 * - ASearch: Using the A* search to find the solution of Puzzle
 * There are four main heuristic using for this puzzle: BFS_HEURISTIC, HAMMING_HEURISTIC
 * 	MANHATTAN_HEURISTC, CUSTOM_HEURISTIC. Each of them will be exploring later.
 * 
 */
public class EightPuzzle {

	/*
	 * Class construction
	 */
    public EightPuzzle() {}
    
    /*
     * Four main heuristic using to solve the puzzle.
     */
    protected static final int BFS_HEURISTIC = 0;	//f = h.
    protected static final int HAMMING_HEURISTIC = 1;	//f = g + h
    protected static final int MANHATTAN_HEURISTIC = 2;	//f = g + h
    protected static final int CUSTOM_HEURISTIC = 3;	//f = alpha*g + beta*h, where alpha = 1, beta = 2 
    
    /*
     * Setup the currentHeuristic in the main function.
     */
    public int currentHeuristic = 0;
    
    public long nodes = 0;	//To count the number of nodes visited through searching
    
    public Board currentBoard, childBoard, tempBoard;
    public int temp_f_value;
   
    /*
     * Define the goalState
     */
    public static final Board goalState = 
        new Board(new int[] {1,2,3,4,5,6,7,8,0});
    
    /*
     * Pseudo code for BFS:
     * Open list <- Keep unevaluated Board, sorted in order of f value
     * Closed list <- Keep the visited Board, prevent revisiting the evaluated board
     * Add the initial state to the Open List.
     * While(OpenList not empty) {
     * 	currentBoard <- First board from Open list
     * 	ClosedList.add(currentBoard)
     * 	if currentBoard is goal
     * 		return currentBoard, we are done.
     * 	else
     * 		get all the successors (children) of the current board.
     * 		for each successor
     * 			if successor is neither not in Close List, or the Open List
     * 				OpenList.add(successor)
     * 				continue;
     * 	return null if no solution found.
     * 
     */
    public Board BFS(Board startState) {
    
        OpenList Open = new OpenList();
        ClosedList Close = new ClosedList();
    
        Open.add(startState);
        while(Open.isEmpty() == false) {
            currentBoard = Open.removeFirst();
            if(currentBoard.getPathLenth() >=23) {	// Make sure it's not going to deep.
            	return null;
            }
            Close.add(currentBoard);
            if(currentBoard.equals(goalState)) {
                return currentBoard;
            }
            else {
                Vector<Board> successors = currentBoard.getSuccesors();
                for(int i = 0; i< successors.size();i++) {
                	nodes++;
                    childBoard = successors.get(i);
                    if(childBoard != null && Close.contains(childBoard)==false) {
                        childBoard.f_value = get_f_value(childBoard);
                        if(Open.contains(childBoard) != true) {
                            childBoard.parent = currentBoard;
                            Open.add(childBoard);
                        }
                    }
                }
            }
        }	  
    // If no solution found
    return null; 
    }
    
    /*
     * Open list <- Keep unevaluated Board, sorted in order of f value
     * Closed list <- Keep the visited Board, prevent revisiting the evaluated board
     * Add the initial state to the Open List.
     * While(OpenList not empty) {
     * 	currentBoard <- First board from Open list
     * 	ClosedList.add(currentBoard)
     * 	if currentBoard is goal
     * 		return currentBoard, we are done.
     * 	else
     * 		get all the successors (children) of the current board.
     * 		for each successor
     * 			if it's not in Close list, we evaluate the f value
     * 				check it's already in the Open list
     * 				if not, then just add it to the Open list
     * 				else, compare with the its value in the Open list, then add to appropriate place.
     * 			Otherwise, just add it to Open list if it is neither in closed list, or open list
     * 
     */
    public Board Asearch(Board startState) {
        
        OpenList Open = new OpenList();
        ClosedList Close = new ClosedList();

        Open.add(startState);			// Add initial Board to the OPEN list
        while (Open.isEmpty() == false) {		//Repeat until OPEN list empty
            currentBoard = Open.removeFirst();	//put the board on OPEN list to currentBoard
            
            if(currentBoard.getPathLenth() >=23) {	// Make sure it's not goint too deep.
            	return null;
            }	
            Close.add(currentBoard);				// immediately add the Board to CLOSEDLIST
            if(currentBoard.equals(goalState)) {	// Check to see if the currentBoard is goals?
                return currentBoard;				// If so, return the board;
            }
            else {
                Vector<Board> successors = currentBoard.getSuccesors();
                for(int i = 0; i< successors.size();i++) {
                	nodes++;
                    childBoard = successors.get(i);
                    if(childBoard != null && Close.contains(childBoard)==false) {
                        childBoard.f_value = get_f_value(childBoard);
                        if(Open.contains(childBoard) != true) {
                            childBoard.parent = currentBoard;
                            Open.add(childBoard);
                        }
                        else {
                            temp_f_value = Open.getCost(childBoard);
                            if(childBoard.f_value <= temp_f_value) {
                                childBoard.parent = currentBoard;
                                Open.add(childBoard);
                            }
                            else {
                                Close.add(childBoard);
                            } 
                        }
                    }
                }
            }
        }
    // return null if no solution was found
    return null;
    }
   
    /*
     * Return f_value for each type of Heuristic.
     */
    protected int get_f_value(Board board) {
        int hValue = 0;
        switch (currentHeuristic) {
            case BFS_HEURISTIC :										// Return h = h - g
                hValue = bfsHeuristic(board) - board.getPathLenth();	// So, f = g + h - g = h. 
                break;
            case HAMMING_HEURISTIC :
                hValue = HammingHeuristic(board);
                break;
            case MANHATTAN_HEURISTIC :
                hValue = ManhattanHeuristic(board);
                break;
            case CUSTOM_HEURISTIC :		//f = 1*g + 2 *h
                hValue = customHeuristic(board);
                break;
            default :
                System.err.println("Unrecognized heuristic!");
        }

        return board.getPathLenth() + hValue;
    }
    
    /*
     * Define BSFHeuristic where f = h.
     * Since the get_f_value() returns f = g + h.
     * We use BFSHeuristic to return h - g, then f = g + h - g = h.
     * 
     */
    protected int bfsHeuristic(Board board) {
        int result = 0;
        for(int y = 0; y < 3; y ++) 		// scan for misplace of tiles.
            for (int x = 0; x < 3; x++) {
                if(board.getTileAt(x, y) != 3*y + x + 1)
                result ++;					// whenever it found a misplace, increase result by 1.
            }
        return result;
    }

    /*
     * Hamming Heuristic or misplaced tiles heuristic.
     * Scanning the board to find the misplaced tiles.
     */
    protected int HammingHeuristic(Board board) {
        int result = 0;
        for(int y = 0; y < 3; y ++) 		// scan for misplace of tiles.
            for (int x = 0; x < 3; x++) {
                if(board.getTileAt(x, y) != 3*y + x + 1)
                result ++;	
            }
        return result;
    }
    
    /*
     * Manhattan Heuristic.
     * The total of Manhattan distances of the sum of vertical 
     * and horizontal distance from the blocks to their goal positions
     *  plus the number of moves derived so far to reach the current node.
     * 
     */
    protected int ManhattanHeuristic(Board board) { 
        int result = 0;
        int index = -1;
        for (int sy = 0; sy < 3; sy++) {
            for (int sx = 0; sx < 3; sx ++) {
                index ++;
                int tileValue = board.board[index];
                if(tileValue == board.getBlankPosition()) {
                    continue;
                }
                int sx_offby = tileValue % 3;
                int sy_offby = tileValue / 3;
                result += Math.abs(sy_offby - sy) + Math.abs(sx_offby - sx);
            }
        }
        return result;
    }

    /*
     * Custom Heuristic: f = 1*g + 2*h.
     */
    protected int customHeuristic(Board board) {
        int result = 0;
        for(int i = 0; i < 3; i ++) 
            for (int j = 0; j < 3; j++) {
                if(board.getTileAt(j, i) != 3*i + j + 1)
                    result ++;
            }
        return (int) Math.floor(result*2);
    }

    /*
     * Get the current Heuristic 
     */
    protected int getCurrentHeuristic() {
        return currentHeuristic;
    }

    /*
     * Set the current Heuristic.
     */
    protected void setCurrentHeuristic(int currentHeuristic) {
        this.currentHeuristic = currentHeuristic;
    }
    
    /*
     * Get the current Nodes Visited
     */
    public long getCurrentNodesVisited() {
    	return nodes;
    }
    
    /*
     * Set the current Nodes Visited
     */
    public void setCurrentNodesVistited(int nodes) {
    	this.nodes = nodes;
    }
    
    /*
     * The main function will read from file to test the input puzzle
     * Then output the solution to file based on the corresponding search method using.
     * 
     */
    public static void main(String[] args) throws IOException {
    	try {
    		
    		int inputIndex = 0;
    		String inputString = null;
    		EightPuzzle BFSpuzzle = new EightPuzzle();
    		BFSpuzzle.setCurrentHeuristic (1);
    		EightPuzzle Apuzzle = new EightPuzzle();
    		Apuzzle.setCurrentHeuristic(1);
    		
    		/*
    		 * -----------------------------------****
    		 * This is used to read number of input files which want to test
    		 * Change the inputIndex if want to.
    		 * The inputFile will scan from the path, and read all the file as name convention
    		 */
    		for(inputIndex = 1; inputIndex <= 2; inputIndex++) {
    			
    			BFSpuzzle.setCurrentNodesVistited(0);	// Set the Nodes Visited to 0
    			Apuzzle.setCurrentNodesVistited(0);		// Set the Nodes visisted to 0
    			
    			inputString = String.valueOf(inputIndex);
    			File inputFile = new File("/Users/khoapham/Desktop/HW2_KhoaP/Testcase/input"+inputString);
    			BufferedReader buffReader = new BufferedReader(new FileReader(inputFile));
    			
    			String inputLine = null;
    			int[] maps = new int[9];	// Scan the input file, and put to the array.
    			for(int i = 0; i < 9; i++) {
    				inputLine = buffReader.readLine();
    				StringTokenizer stk = new StringTokenizer(inputLine, "\n");
    		
    				while(stk.hasMoreElements()) {
    					maps[i] = Integer.parseInt(stk.nextToken());
    				}
    			}    	
    			// -----------------------------------------------
    			
    			/*
    			 * Create a new board from the array
    			 */
    			Board newBoard = new Board(maps);
    			
    			/*
    			 * If the solution if not found, 
    			 * Output the result to file.
    			 */
    			if(!newBoard.isSolvable()) {
    				ObjectOutputStream ObjOutput1 = new ObjectOutputStream(new FileOutputStream("/Users/khoapham/Desktop/HW2_KhoaP/Testcase/BFSoutput"+inputString));
    				ObjOutput1.writeObject("Heuristic using: " + BFSpuzzle.getCurrentHeuristic() + 
    						"\n Solution not found \n" );
    				
    				ObjectOutputStream ObjOutput2 = new ObjectOutputStream(new FileOutputStream("/Users/khoapham/Desktop/HW2_KhoaP/Testcase/Aoutput"+inputString));
    				ObjOutput2.writeObject("Heuristic using: \n" + Apuzzle.getCurrentHeuristic() +
    						"\n Solution not found \n");
    				return;
    			}
    			
    			/*
    			 * Else, we use 2 search methods to solve the puzzle
    			 * Then, put them to the files.
    			 */
    			else {
    				
    				Board BFSSolver = BFSpuzzle.BFS(newBoard);
    				ObjectOutputStream ObjOutput1 = new ObjectOutputStream(new FileOutputStream("/Users/khoapham/Desktop/HW2_KhoaP/Testcase/BFSoutput"+inputString));
    				ObjOutput1.writeObject("Heuristic using: " + BFSpuzzle.getCurrentHeuristic() + 
    						"\nAt depth: " + BFSSolver.getPathLenth() +
    						"\nNodes visited: " + BFSpuzzle.nodes +
    						"\nSolution: \n" + BFSSolver.getPathFromStartNode().toString());
    			
    				Board ASolver = Apuzzle.Asearch(newBoard);
    				ObjectOutputStream ObjOutput2 = new ObjectOutputStream(new FileOutputStream("/Users/khoapham/Desktop/HW2_KhoaP/Testcase/Aoutput"+inputString));
    				ObjOutput2.writeObject("Heuristic using: " + Apuzzle.getCurrentHeuristic() +
    						"\nAt depth: " + ASolver.getPathLenth() +
    						"\nNodes visited: " + Apuzzle.nodes +
    						"\n Solution: \n" + ASolver.getPathFromStartNode().toString());
    				
        	
    		/*
    		System.out.println("Heuristic using: "+ puzzle.getCurrentHeuristic());
        	System.out.println("Max depth: "+ ASolver.getPathLenth());
            System.out.println("best soln: \n: " + res.getPathFromStartNode());
            
            fw.writeObject(res.getPathFromStartNode().toString());
            
            System.out.println("Eslapsed time (milisec): " + ((System.nanoTime() - startTime))/1000000);
            */
    			}
    		}
    	}
    	catch(IOException e) {
    		System.err.print(e);
    	}
    }
}
