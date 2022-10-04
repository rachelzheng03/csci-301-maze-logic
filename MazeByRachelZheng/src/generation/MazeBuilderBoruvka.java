package generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
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
	private ArrayList<Integer> weightList;
	private int[][] forest;
	private int[][] visited;
	
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
		visited=new int[width][height]; //marks 1 if the cell at position (width, height) has been visited in that cycle. otherwise 0.
		forest=new int[width][height];
		
		//assign each cell a number (to show they belong in different sets)
		int initialAssignment=1;
		for (int w=0; w<width; w++) {
			for (int h=0; h<height; h++) {
				forest[w][h]=initialAssignment;
				initialAssignment++;
			}
		}
		int forestSize=width*height;
		while(!allMerged()) {
			for (int w=0; w<width; w++) {
				for (int h=0; h<height; h++) {
		
					if (visited[w][h]==1||floorplan.isInRoom(w, h)) {
						continue;
					}
					int newAssignment=forest[w][h]; //set marker
				
					//get smallest edge weight of set and delete
					Wallboard currWallboard=getMinEdge(newAssignment);
					floorplan.deleteWallboard(currWallboard);
				
					//merge by assigning neighbor to same number
					int oldAssignment=forest[currWallboard.getNeighborX()][currWallboard.getNeighborY()];
					mergeCells(oldAssignment, newAssignment);
				
					//mark all cells in the set as visited
					markVisited(newAssignment);
				}
			}
		resetVisited();
		}
	}
	
	private boolean allMerged() {
		int assignment=forest[0][0];
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				if(forest[x][y]!=assignment) {
					return false;
				}
			}
		}
		return true;
			
	}
	
	private void markVisited(int assignment) {
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				if (forest[x][y]==assignment) {
					visited[x][y]=1;
				}
			}			
		}
	}
	
	private void resetVisited() {
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				visited[x][y]=0;
			}
		}
	}
	
	private void mergeCells(int oldAssignment, int newAssignment) {
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				if (forest[x][y]==oldAssignment)
					forest[x][y]=newAssignment;
			}
		}
	}
	
	private void updateListOfWallboards(int x, int y, ArrayList<Wallboard> wallboards) {
		if (reusedWallboard == null) {
			reusedWallboard = new Wallboard(x, y, CardinalDirection.East) ;
		}
		for (CardinalDirection cd : CardinalDirection.values()) {
			reusedWallboard.setLocationDirection(x, y, cd);
			if (floorplan.hasWall(x, y, cd)&&!floorplan.isPartOfBorder(reusedWallboard)) // 
				wallboards.add(new Wallboard(x, y, cd));
			
			}
		}
	
	// exclusively used in updateListOfWallboards
	Wallboard reusedWallboard; // reuse a wallboard in updateListOfWallboards to avoid repeated object instantiation

	private Wallboard getMinEdge(int marker) {
		ArrayList<Wallboard> candidates=new ArrayList<>();
		for (int w=0; w<width; w++) {
			for (int h=0; h<height; h++) {
				if (forest[w][h]==marker) {
					candidates.add(getMinEdge(w, h)); //min edge weight of each cell in set becomes a candidate
				}
			}
		}
		Wallboard minWB=candidates.get(0);
		int minEW=getEdgeWeight(minWB.getX(), minWB.getY(), minWB.getDirection());
		for (int i=0; i<candidates.size(); i++) {
			Wallboard currentWB = candidates.get(i);
			if(getEdgeWeight(currentWB.getX(), currentWB.getY(), currentWB.getDirection())<minEW) {
				minWB=currentWB;
			}
		}	
		return minWB;
	}
	
	private Wallboard getMinEdge(int x, int y) {
		ArrayList<Wallboard> candidates=new ArrayList<>();
		updateListOfWallboards(x, y, candidates);
		Wallboard minWB=candidates.get(0);
		int minEW=getEdgeWeight(x, y, minWB.getDirection());
		for (int i=0; i<candidates.size(); i++) {
			Wallboard currentWB = candidates.get(i);
			if(getEdgeWeight(x, y, currentWB.getDirection())<minEW) {
				minWB=currentWB;
			}
		}	
		
		return minWB;
	}
	
	/**
	 * Gives each internal wallboard an unique edge weight
	 * @return a 2D array that contains all the edge weights
	 */
	protected void setAllEdgeWeights() {
		Random rand = new Random();
		int seed = rand.nextInt();
		int weight;
		//set random seed 
		SingleRandom.setSeed(seed);
		edgeWeightsEast = new int[width][height];
		edgeWeightsSouth = new int[width][height];
		edgeWeightsNorth = new int[width][height];
		edgeWeightsWest = new int[width][height];
		weightList=new ArrayList<>();
		weightList.add(0); //add 0 to weightList so that 0 cannot be a edge weight for any wallboard
		
		//assign each internal wallboard an edge weight using random number generator. 
		//generate a new number if number is already an edge weight
		//store edge weight in 2D arrays
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				Wallboard wbNorth=new Wallboard(i, j, CardinalDirection.North);
				Wallboard wbSouth=new Wallboard(i, j, CardinalDirection.South);
				Wallboard wbEast=new Wallboard(i, j, CardinalDirection.East);
				Wallboard wbWest=new Wallboard(i, j, CardinalDirection.West);
				
				//populate edgeWeightEast 
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbEast)&&!floorplan.isInRoom(i, j)) {
					edgeWeightsEast[i][j]=weight;
					weightList.add(weight); 
				}
				
				//populate edgeWeightSouth
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbSouth)&&!floorplan.isInRoom(i, j)) {
					edgeWeightsSouth[i][j]=weight;
					weightList.add(weight);
				}
				
				//populate edgeWeightWest
				if (!floorplan.isPartOfBorder(wbWest)&&!floorplan.isInRoom(i, j)){
					edgeWeightsWest[i][j]=edgeWeightsEast[i-1][j];
				}
				
				//populate edgeWeightNorth
				if (!floorplan.isPartOfBorder(wbNorth)&&!floorplan.isInRoom(i, j)) {
					edgeWeightsNorth[i][j]=edgeWeightsSouth[i][j-1];
				}			
			}
		}
	}
	
	/**
	 * Gets edge weight value that has not been used
	 * @return a integer that has not been used as an edge weight
	 */
	private int getUniqueWeight() {
		int weight = random.nextIntWithinInterval(1, 50000000);
		while (weightList.contains(weight)) {
			weight=random.nextIntWithinInterval(1, 50000000);
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
		//make sure cell exists in maze
		if (x<=width||y<=width||x>=0||y>=0) {
			//return edge weight for wallboard by indexing 2D array
			switch(cd) {
			case East:
				return edgeWeightsEast[x][y];
			case South:
				return edgeWeightsSouth[x][y];
			case West:
				return edgeWeightsWest[x][y];
			case North:
				return edgeWeightsNorth[x][y];
			default:
				LOGGER.warning("Missing cardinal direction in parameters");
				return 0;
			}
		}
		LOGGER.warning("invalid input for x and/or y");
		return 0;
	}
	
}
