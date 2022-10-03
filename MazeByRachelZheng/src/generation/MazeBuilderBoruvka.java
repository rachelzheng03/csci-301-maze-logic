package generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.text.ChangedCharSetException;
import javax.swing.text.Position;

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
	private MST[][] forest;

	
	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
	}
	
	private class MST{
		private boolean visited; //to be visited means the MST is part of a set with other MSTs
		private ArrayList<Wallboard> edges; //all wallboards that can be broken that borders the cells of the set
		private ArrayList<int[]> vertices; //list of cells {width, height} in set
		private boolean newest; //true if newest MST added to a set (representative for the set)
		
		/**
		 * Initialize the MST with the cell indicated by the parameters.
		 */	
		public MST(int x, int y) {
			edges = new ArrayList<>();
			vertices=new ArrayList<>();
			visited=false;
			newest=true;
			int[] cell = {x,y};
			addVertexToMST(cell);
			updateListOfWallboards(x, y, edges);
			
		}
		
		/**
		 * @return returns a array list of edges that are in the MST
		 */
		public ArrayList<Wallboard> getEdges() {
			return edges;
		}
		
		public void setEdges(ArrayList<Wallboard> edges) {
			this.edges=edges;
		}
		
		public void setVisited(boolean b) {
			visited=b;
		}
		
		public boolean getVisited(){
			return visited;
		}
		
//		public void setEdgeCandidates(ArrayList<Wallboard> updatedEdges){
//			edges=updatedEdges;
//		}
		
		public boolean getNewest() {
			return newest;
		}

		public void setNewest(boolean newest) {
			this.newest = newest;
		}
		
		public ArrayList<int[]> getVertices() {
			return vertices;
		}
		
		public void setVertices(ArrayList<int[]> newVertices) {
			this.vertices=newVertices;
		}
		
		public void addVertexToMST(int[] cell) {
			vertices.add(cell);
		}
		
		public void addEdgeToMST(Wallboard wb) {
			edges.add(wb);
		}
		
		/**
		 * Add the given cell (x,y) to the MST by marking it as visited and add its wallboards
		 * that lead to cells outside of the MST to the list of candidates (unless they are borderwalls).
		 * @param x the x coordinate of interest
		 * @param y the y coordinate of interest
		 * @param candidates the new elements should be added to, must not be null
		 */
//		public void addEdgeToMST(int x, int y, final ArrayList<Wallboard> candidates) {
//			updateListOfWallboards(x, y, candidates); // checks to see if it has wallboards to new cells, if it does it adds them to the list
//		}
		
		/**
		 * Updates a list of all wallboards that could be removed from the maze based on wallboards towards new cells.
		 * For the given x, y coordinates, one checks all four directions
		 * and for the ones where one can tear down a wallboard, a 
		 * corresponding wallboard is added to the list of wallboards.
		 * @param x the x coordinate of interest
		 * @param y the y coordinate of interest
		 * @param wallboards the new elements should be added to, must not be null
		 */
		//only for breaking into new frontier
		public void updateListOfWallboards(int x, int y, ArrayList<Wallboard> wallboards) {
			if (reusedWallboard == null) {
				reusedWallboard = new Wallboard(x, y, CardinalDirection.East) ;
			}
			for (CardinalDirection cd : CardinalDirection.values()) {
				reusedWallboard.setLocationDirection(x, y, cd);
				if (floorplan.canTearDown(reusedWallboard)) // 
				{
					wallboards.add(new Wallboard(x, y, cd));
				}
			}
		}
	
		// exclusively used in updateListOfWallboards
		Wallboard reusedWallboard; // reuse a wallboard in updateListOfWallboards to avoid repeated object instantiation
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
		forest=new MST[height][width];
		//initialize MSTs for each cells
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				if(!floorplan.isInRoom(i, j)) {
					MST tree = new MST(i, j);
					forest[j][i]=tree;
				}
			}
		}
		int forestSize=height*width;
		//while not one big forest (MST that contains all the cells)
		while (forestSize!=1) {
		//for each tree in forest:
			for (int w=0; w<width; w++) {
				for (int h=0; h<height; h++) {
					MST tree = forest[h][w];
					//if cell is in room or cell has been connected to another set, skip
					if (tree.getVisited()||floorplan.isInRoom(w, h))
						continue;
				
					ArrayList<Wallboard> candidates = tree.getEdges(); //wallboard candidates of given tree
					if (!candidates.isEmpty()) {
						//get cheapest wallboard of all cells and break it down
						Wallboard curWallboard = getMinEdge(candidates);
						floorplan.deleteWallboard(curWallboard);
						
						//merge trees (add connected cells and edges to same set)
						MST neighborTree = forest[curWallboard.getNeighborY()][curWallboard.getNeighborX()];
						ArrayList<Wallboard> edgesNeighbor = neighborTree.getEdges();
						ArrayList<int[]> verticesNeighbor = neighborTree.getVertices();
						ArrayList<int[]> verticesMe = tree.getVertices();
						if (neighborTree.getVisited()) {
							//MST newest = findNewest(verticesNeighbor);
							
							//update vertices and edges for all cells in set
							for (int e=0; e<edgesNeighbor.size(); e++) {
								tree.addEdgeToMST(edgesNeighbor.get(e));
							}
							for (int v=0; v<verticesNeighbor.size(); v++) {
								tree.addVertexToMST(verticesNeighbor.get(v));
							}
							for (int v=0; v<verticesNeighbor.size(); v++) {
								MST update=forest[verticesNeighbor.get(v)[1]][verticesNeighbor.get(v)[0]];
								update.setVertices(tree.getVertices());
								update.setEdges(tree.getEdges());
							}
							findNewest(verticesNeighbor).setNewest(false);
							tree.setNewest(true);
						}
						
						else { //new frontier or the "neighbor"
							//tree.updateListOfWallboards(curWallboard.getNeighborY(), curWallboard.getNeighborX(), candidates);

							for (int e=0; e<candidates.size(); e++) {
								neighborTree.addEdgeToMST(candidates.get(e));
							}
							for (int v=0; v<verticesMe.size(); v++) {
								neighborTree.addVertexToMST(verticesMe.get(v));
							}
							for (int v=0; v<verticesMe.size(); v++) {
								MST update=forest[verticesMe.get(v)[1]][verticesMe.get(v)[0]];
								update.setVertices(neighborTree.getVertices());
								update.setEdges(neighborTree.getEdges());
							}
							findNewest(verticesMe).setNewest(false);
							neighborTree.setNewest(true);
						}
						tree.setVisited(true);
						neighborTree.setVisited(true);
						forestSize--;
					}
		
				}
		
			}
			resetVisit();
		
		}
	}
	//}
	

	private MST findNewest(ArrayList<int[]> vertices){
		for (int v=0; v<vertices.size(); v++) {
			int[] cell = vertices.get(v);
			MST tempNewest=forest[cell[1]][cell[0]];
			if (tempNewest.getNewest()) {
				return tempNewest;
			}
		}
		return null;
	}
	
	public void resetVisit() {
		for (int w=0; w<width; w++) {
			for (int h=0; h<height; h++) {
				forest[h][w].setVisited(false);
			}
		}
	}
			
	/**
	 * Gets the wallboard with the minimum edge weight given a list of edge weights
	 * @param x
	 * @param y
	 * @return valid wallboard (has not been broken down and is not a border) with minimum edge weight
	 */
	private Wallboard getMinEdge(ArrayList<Wallboard> candidates) {
//		ArrayList<CardinalDirection> cdList= new ArrayList<>();
//		cdList.add(CardinalDirection.North);
//		cdList.add(CardinalDirection.East);
//		cdList.add(CardinalDirection.South);
//		cdList.add(CardinalDirection.West);
//		ArrayList<Wallboard> minCandidates = new ArrayList<>();
//		
//		//get list of valid wallboard candidates 
//		for (int i=0; i<4; i++) {
//			int val = getEdgeWeight(x, y, cdList.get(i));
//			if (floorplan.hasWall(x, y, cdList.get(i))&&val!=0){
//				Wallboard tempWB=new Wallboard(x, y, cdList.get(i));
//				minCandidates.add(tempWB);
//			}
//		}
//		
		Wallboard currentMin=candidates.get(0);
		int currentMinIdx=0;
		for (int i=0; i<candidates.size(); i++) {
			Wallboard compare=candidates.get(i);
			int currentMinEW=getEdgeWeight(currentMin.getX(), currentMin.getY(), currentMin.getDirection());
			int compareEW=getEdgeWeight(compare.getX(),compare.getY(),compare.getDirection());
			if (compareEW<currentMinEW) {
				currentMin=compare;
				currentMinIdx=i;
			}
		}
		assert(candidates.get(currentMinIdx).getX()==currentMin.getX());
		assert(candidates.get(currentMinIdx).getY()==currentMin.getY());
		assert(candidates.get(currentMinIdx).getDirection()==currentMin.getDirection());
		
		return candidates.remove(currentMinIdx);
			
//		//get the wallboard with the smallest edge weight out of valid candidates
//		Wallboard currentMin=minCandidates.get(0);
//		for (int n=0; n<minCandidates.size(); n++) {
//			Wallboard compare=minCandidates.get(n);
//			int currentMinEW=getEdgeWeight(currentMin.getX(), currentMin.getY(), currentMin.getDirection());
//			int compareEW=getEdgeWeight(compare.getX(),compare.getY(),compare.getDirection());
//			if (compareEW<currentMinEW) {
//				currentMin=compare;
//			}
//		
//
//		}
//		return currentMin;
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
		edgeWeightsEast = new int[height][width];
		edgeWeightsSouth = new int[height][width];
		edgeWeightsNorth = new int[height][width];
		edgeWeightsWest = new int[height][width];
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
					edgeWeightsEast[j][i]=weight;
					weightList.add(weight); 
				}
				
				//populate edgeWeightSouth
				weight=getUniqueWeight();
				if (!floorplan.isPartOfBorder(wbSouth)&&!floorplan.isInRoom(i, j)) {
					edgeWeightsSouth[j][i]=weight;
					weightList.add(weight);
				}
				
				//populate edgeWeightWest
				if (!floorplan.isPartOfBorder(wbWest)&&!floorplan.isInRoom(i, j)){
					edgeWeightsWest[j][i]=edgeWeightsEast[j][i-1];
				}
				
				//populate edgeWeightNorth
				if (!floorplan.isPartOfBorder(wbNorth)&&!floorplan.isInRoom(i, j)) {
					edgeWeightsNorth[j][i]=edgeWeightsSouth[j-1][i];
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
		while (weightList.contains(weight)) {
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
		//make sure cell exists in maze
		if (x<=width||y<=width||x>=0||y>=0) {
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
		LOGGER.warning("invalid input for x and/or y");
		return 0;
	}
	
}
