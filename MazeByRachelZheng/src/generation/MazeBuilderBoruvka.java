package generation;

import java.util.logging.Logger;

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	/**
	 * The logger is used to track execution and report issues.
	 */
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());

	private int[][] edgeWeights; //2D array that contains all the edge weights
	
	
	public MazeBuilderBoruvka() {
	}
	
	/**
	 * This method generates pathways into the maze by using Boruvka's algorithm.
	 * The cells are the nodes of the graph and the spanning tree. An edge represents that one can move from one cell to an adjacent cell.
	 * So an edge implies that its nodes are adjacent cells in the maze and that there is no wallboard separating these cells in the maze. 
	 */
	@Override
	protected void generatePathways() {
		// TODO Auto-generated method stub
		//starting with all cells being their own "forest" and empty set of edges
		//for each cell in maze:
			//if cell in room or cell in set with other cells, go to next cell
			//get the edge weight of walls from all four directions (exclude border walls or no wall)
			//get cheaper wallboard and break it down
			//merge forest (add connected cells to same set)
		//for each set:
			//if set has been merged, skip
			//get the edge weight of walls from all four directions of each cell in set (exclude border walls or no wall)
			//get cheapest wallboard of ALL cells in set and break it down
			//merge forest (sets)
		//repeat above section until only one forest remains
		
		
	}
	
	/**
	 * Gives each internal wallboard an unique edge weight
	 * @return a 2D array that contains all the edge weights
	 */
	private int[][] setAllEdgeWeights() {
		return edgeWeights;
		//set random seed using random number generator
		//assign each internal wallboard an edge weight using random number generator. 
		//generate a new number if number is already an edge weight
		//store edge weight in an 2D array
	}
	
	/**
	 * Given a wallboard, the method returns the value of its edge weight.
	 * @param x
	 * @param y
	 * @param cd
	 * @return unique value for an internal wallboard with coordinates (x,y) and direction cd
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		return y;
		//return edge weight for wallboard by indexing 2D array
		
	}
	
}
