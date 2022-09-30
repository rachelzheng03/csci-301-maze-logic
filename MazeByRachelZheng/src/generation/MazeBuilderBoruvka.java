package generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	/**
	 * The logger is used to track execution and report issues.
	 */
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());

	private int[][] edgeWeightsEast; //contains the edge weight for wallboards to the east of each cell
	private int[][] edgeWeightsNorth; //contains the edge weight for wallboards to the north of each cell
	private int[][] edgeWeightsSouth; //contains the edge weight for wallboards to the south of each cell
	private int[][] edgeWeightsWest; //contains the edge weight for wallboards to the west of each cell
	private ArrayList<Integer> edgeList;
	
	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
	}
	
	/**
	 * This method generates pathways into the maze by using Boruvka's algorithm.
	 * The cells are the nodes of the graph and the spanning tree. An edge represents that one can move from one cell to an adjacent cell.
	 * So an edge implies that its nodes are adjacent cells in the maze and that there is no wallboard separating these cells in the maze. 
	 */
	@Override
	protected void generatePathways() {
		// TODO Auto-generated method stub
		setAllEdgeWeights();
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
	private void setAllEdgeWeights() {
		Random rand = new Random();
		int seed = rand.nextInt();
		int weight;
		//set random seed 
		SingleRandom.setSeed(seed);
		edgeWeightsEast = new int[height][width];
		edgeWeightsSouth = new int[height][width];
		edgeList.add(0); //add 0 to edgeList so that 0 cannot be a edge weight for any wallboard
		
		//assign each internal wallboard an edge weight using random number generator. 
		//generate a new number if number is already an edge weight
		//store edge weight in 2D arrays
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				Wallboard wbEast = new Wallboard(i, j, CardinalDirection.East);
				Wallboard wbSouth = new Wallboard(i, j, CardinalDirection.South);
				Wallboard wbWest = new Wallboard(i, j, CardinalDirection.West);
				Wallboard wbNorth = new Wallboard(i, j, CardinalDirection.North);
				
				//populate edgeWeightEast 
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbEast)) {
					edgeWeightsEast[j][i]=weight;
					edgeList.add(weight); 
				}
				
				//populate edgeWeightSouth
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbSouth)) {
					edgeWeightsSouth[j][i]=weight;
					edgeList.add(weight);
				}
				
				//populate edgeWeightWest
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbWest)) {
					edgeWeightsWest[j][i]=edgeWeightsEast[j][i-1];
				}
				
				//populate edgeWeightNorth
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbNorth)) {
					edgeWeightsWest[j][i]=edgeWeightsEast[j-1][i];
				}			
			}
		}
	}
	
	/**
	 * Gets edge weight value that has not been used
	 * @return a integer that has not been used as an edge weight
	 */
	private int getUniqueWeight() {
		int weight = random.nextInt();
		while (edgeList.contains(weight)) {
			weight=random.nextInt();
		}
		return weight;
	}
	
	/**
	 * Given a wallboard, the method returns the value of its edge weight.
	 * @param x
	 * @param y
	 * @param cd
	 * @return unique value for an internal wallboard with coordinates (x,y) and direction cd
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		//return edge weight for wallboard by indexing 2D array
		switch(cd) {
		case East:
			return edgeWeightsEast[y][x];
		case South:
			return edgeWeightsSouth[y][x];
		case West:
			return edgeWeightsWest[y][x];
		case North:
			return edgeWeightsNorth[y][x];
		default:
			LOGGER.warning("Missing cardinal direction in parameters");
			return 0;
		}
	}
	
}
