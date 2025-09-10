package generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	/**
	 * The logger is used to track execution and report issues.
	 */
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());
	
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
		int num_vertices = width*height;
	
		int[] parent = new int[num_vertices];
        int[] rank = new int[num_vertices];
        int[] cheapest = new int[num_vertices];
        
        ArrayList<Wallboard> edges = initBoruvka(parent, rank);
        
        int num_components = 0;
        for (int v = 0; v < num_vertices; v++) {
            if (parent[v] != -1) {
            	num_components++;
            }
        }
        
        // Keep combining until only one component remains
        while (num_components > 1) {
            Arrays.fill(cheapest, -1);

            // Traverse all edges and update cheapest edges
            for (int i = 0; i < edges.size(); i++) {
            	Wallboard cur_edge = edges.get(i);
            	int src = calculateCellNum(cur_edge.getY(), cur_edge.getX(), width);
            	int dest;
            	if (cur_edge.getDirection() == CardinalDirection.East) {
            		dest = calculateCellNum(cur_edge.getY(), cur_edge.getX()+1, width);
            	}else {
            		dest = calculateCellNum(cur_edge.getY()+1, cur_edge.getX(), width);
            	}
           
                int set1 = find(parent, src);
                int set2 = find(parent, dest);

                if (set1 == set2) continue;
                
                int cur_edge_weight = getEdgeWeight(cur_edge.getX(), cur_edge.getY(), cur_edge.getDirection());
           
                if (cheapest[set1] == -1 || cur_edge_weight < getEdgeWeight(edges.get(cheapest[set1]).getX(), edges.get(cheapest[set1]).getY(), edges.get(cheapest[set1]).getDirection())) {
                    cheapest[set1] = i;
                }

                if (cheapest[set2] == -1 || cur_edge_weight < getEdgeWeight(edges.get(cheapest[set2]).getX(), edges.get(cheapest[set2]).getY(), edges.get(cheapest[set2]).getDirection())) {
                    cheapest[set2] = i;
                }
            }

            // Add the cheapest edges for each component
            for (int i = 0; i < num_vertices; i++) {
                if (cheapest[i] != -1) {
                	Wallboard cur_edge = edges.get(cheapest[i]);
                	int src = calculateCellNum(cur_edge.getY(), cur_edge.getX(), width);
                	int dest;
                	if (cur_edge.getDirection() == CardinalDirection.East) {
                		dest = calculateCellNum(cur_edge.getY(), cur_edge.getX()+1, width);
                	}else {
                		dest = calculateCellNum(cur_edge.getY()+1, cur_edge.getX(), width);
                	}
                	
                    int set1 = find(parent, src);
                    int set2 = find(parent, dest);

                    if (set1 != set2) {
                    	floorplan.deleteWallboard(edges.get(cheapest[i]));
                        union(parent, rank, set1, set2);
                        num_components--;
                    }
                }
            }
        }
	}
	
	// Find with path compression
    int find(int[] parent, int i) {
        if (parent[i] != i) {
            parent[i] = find(parent, parent[i]);
        }
        return parent[i];
    }

    // Union by rank
    void union(int[] parent, int[] rank, int x, int y) {
        int xroot = find(parent, x);
        int yroot = find(parent, y);

        if (xroot == yroot) return;

        if (rank[xroot] < rank[yroot]) {
            parent[xroot] = yroot;
        } else if (rank[xroot] > rank[yroot]) {
            parent[yroot] = xroot;
        } else {
            parent[yroot] = xroot;
            rank[xroot]++;
        }
    }
    
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		int seed = -1;
		switch (cd) {
		case North: {
			seed = 2*calculateCellNum(y, x, width+1);
			break;
		}
		case West: {
			seed = (2*calculateCellNum(y, x, width+1)) + 1;
			break;
		}
		case South: {
			seed = 2*calculateCellNum(y+1, x, width+1);
			break;
		}
		case East: {
			seed = (2*calculateCellNum(y, x+1, width+1)) + 1;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + cd);
		}
		
		// invalid edge
		if (seed==-1) {
			return  -1;
		}
		
		Random random = new Random(seed); 
		return random.nextInt(Integer.MAX_VALUE);
	}
	
	private ArrayList<Wallboard> initBoruvka(int[] parent, int[] rank) {
		ArrayList<Wallboard> wallboards = new ArrayList<Wallboard>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int cell_num = calculateCellNum(y, x, width);
				if (floorplan.isInRoom(x, y)) {
					// invalid cell to add
					parent[cell_num] = -1; 
					rank[cell_num] = -1;
				} else {
					parent[cell_num] = cell_num;
					rank[cell_num] = 0;
				}
				
				updateListOfWallboards(x, y, wallboards);
			}
		}
		
		// returns all the valid edges that can be torn down
		return wallboards;
	}
	
	private int calculateCellNum(int row, int col, int width) {
		return (row*width) + col;
	}
	
	/**
	 * Updates a list of all wallboards that could be removed from the maze based on wallboards towards new cells.
	 * For the given x, y coordinates, one checks all four directions
	 * and for the ones where one can tear down a wallboard, a 
	 * corresponding wallboard is added to the list of wallboards.
	 * @param x the x coordinate of interest
	 * @param y the y coordinate of interest
	 * @param wallboards the new elements should be added to, must not be null
	 */
	private void updateListOfWallboards(int x, int y, ArrayList<Wallboard> wallboards) {
		ArrayList<CardinalDirection> directions = new ArrayList<>();
		directions.add(CardinalDirection.South);
		directions.add(CardinalDirection.East);
		
		if (reusedWallboard == null) {
			reusedWallboard = new Wallboard(x, y, CardinalDirection.East) ;
		}
		for (CardinalDirection cd : directions) {
			reusedWallboard.setLocationDirection(x, y, cd);
			try {
				if (floorplan.canTearDown(reusedWallboard)) {
					wallboards.add(new Wallboard(x, y, cd));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
			
		}
	}
	// exclusively used in updateListOfWallboards
	Wallboard reusedWallboard; // reuse a wallboard in updateListOfWallboards to avoid repeated object instantiation
	
}	
	
	
