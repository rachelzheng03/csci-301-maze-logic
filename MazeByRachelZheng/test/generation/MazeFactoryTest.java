package generation;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.extensions.TestSetup;

public class MazeFactoryTest {
	
	// private variables
	protected DefaultOrder order; //(4x4)
	protected DefaultOrder order2; //(12x12)
	protected DefaultOrder order3; // (20x15)
	protected MazeFactory factory;
	protected Maze maze;
	protected Maze maze2;
	protected Maze maze3; 
	
	/**
	 * Initialize MazeBuilder objects before each test. (3 mazes: 4x4, 12x12 with rooms, 20x15)
	 */
	@BeforeEach
	public void setUp() {
		order = new DefaultOrder(0); //(4x4)
		order2 = new DefaultOrder(1); 
		order2.setPerfect(false); //maze with rooms
		order3 = new DefaultOrder(3); //(20x15)
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		factory.order(order2);
		factory.waitTillDelivered();
		maze2 = order2.getMaze();
		factory.order(order3);
		factory.waitTillDelivered();
		maze3 = order3.getMaze();

	}
	
	/**
	 * Tests if setUp created objects. Correct if not null.
	 */
	@Test
	public final void testMazeFactory() {
		assertNotNull(factory);
		assertNotNull(order);
		assertNotNull(order2);
		assertNotNull(order3);
		assertNotNull(maze);
		assertNotNull(maze2);
		assertNotNull(maze3);
		
	}
	
	/**
	 * Test if the maze objects from setUp are the right size
	 */
	@Test
	public void testMazeSize() {
		assertEquals(4, maze.getWidth());
		assertEquals(4, maze.getHeight());
		assertEquals(12, maze2.getWidth());
		assertEquals(12, maze2.getHeight());
		assertEquals(20, maze3.getWidth());
		assertEquals(15, maze3.getHeight());
	}
	
	/**
	 * Checks each position along border and determines how many exits are on the border.
	 * @param width
	 * @param height
	 * @param floorplan
	 * @return number of total exits in maze
	 */
	private int numExits(int width, int height, Floorplan floorplan){
		int northexit=0;
		int eastexit=0;
		int southexit=0;
		int westexit=0;
		//check every border wall on top and determine if it is an exit
		for(int w=0; w<width; w++) {
			if(floorplan.isExitPosition(w, 0)) {
				northexit=northexit+1;
			}
		}
		//check every border wall on bottom and determine if it is an exit
		for(int w1=0; w1<width; w1++) {
			if(floorplan.isExitPosition(w1, height-1)) {
				southexit=southexit+1;
			}
		}
		//check every border wall on the left and determine if it is an exit
		for(int h=0; h<height; h++) {
			if(floorplan.isExitPosition(0, h)) {
				westexit=westexit+1;
			}
		}
		//check every border wall on the right and determine if it is an exit
		for(int h1=0; h1<height; h1++) {
			if(floorplan.isExitPosition(width-1, h1)) {
				eastexit=eastexit+1;
			}
		}
		return northexit+eastexit+southexit+westexit;
	}
	
	/** 
	 * Tests if a maze only has one exit. The maze is correct if it only has one exit. If no exits or more than 1 exit, test fails. 
	 */
	@Test
	public void testOneExit() {
		//test for smallest possible maze (4x4)
		Floorplan floorplan = maze.getFloorplan();		
		assertEquals(1, numExits(maze.getWidth(), maze.getHeight(), floorplan));
		//test maze with rooms (12x12)
		Floorplan floorplan2 = maze2.getFloorplan();
		assertEquals(1, numExits(maze2.getWidth(), maze2.getHeight(), floorplan2));
		//test for not square (20x15)
		Floorplan floorplan3 = maze3.getFloorplan();
		assertEquals(1, numExits(maze3.getWidth(), maze3.getHeight(), floorplan3));
	}
	
	/**
	 * Check if the value of any cell of mazedist is equal to "INFINITY."
	 * @param width
	 * @param height
	 * @param mazedist
	 * @return true if mazedist has at least 1 "INFINITY"; false if mazedist contains no "INFINITY"
	 */
	private boolean hasInfinity(int width, int height, Distance mazedist){
		final int INFINITY = Integer.MAX_VALUE; 
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (mazedist.getDistanceValue(x, y)==INFINITY) {
					return true;
				}
			}
		}return false;
	}
	
	/**
	 * Tests if there is a way out of the maze from no matter where you are in the maze. Correct if mazedists contains a valid distance value for every cell.
	 */
	@Test
	public void testEveryCellHasPathToExit(){
		//test for smallest possible maze (4x4)
		Distance mazedist = maze.getMazedists();	
		assertFalse(hasInfinity(maze.getWidth(), maze.getHeight(), mazedist));
		//test maze with rooms (12x12)
		Distance mazedist2 = maze2.getMazedists();	
		assertFalse(hasInfinity(maze2.getWidth(), maze2.getHeight(), mazedist2));
		//test for not square (20x15)
		Distance mazedist3 = maze3.getMazedists();	
		assertFalse(hasInfinity(maze3.getWidth(), maze3.getHeight(), mazedist3));
	}
	
	/**
	 * Calculates the maximum amount of internal walls a maze can have
	 * @param width
	 * @param height
	 * @return maximum amount of internal walls
	 */
	private int maxInternalWalls(int width, int height) {
		return width*(height-1)+(width-1)*height;
	}
	
	private int numInternalWalls(Maze maze) {
		int width=maze.getWidth();
		int height=maze.getHeight();
		int walls=0;
		for (int w=0; w<width-1; w++) {
			for (int h=0; h<height-1; h++) {
				if (maze.hasWall(w, h, CardinalDirection.East)){
					walls++;
				}
				if (maze.hasWall(w, h, CardinalDirection.South)){
					walls++;
				}
				}	
			}
		for (int h=0; h<height-1; h++) {
			if (maze.hasWall(width-1, h, CardinalDirection.South)){
				walls++;
			}
		}
		for (int w=0; w<width-1; w++) {
			if (maze.hasWall(w, height-1, CardinalDirection.East)){
				walls++;
			}
		}
		return walls;
	}
	
	/**
	 * Tests if a perfect maze with n cells has the correct amount of walls.
	 */
	@Test
	public void testWallsInPerfectMaze() {
		//check that amount of internal walls in maze = maximum amount of internal walls-(amount of cells-1)
		//test for smallest possible maze (4x4)
		int maxWalls = maxInternalWalls(maze.getWidth(), maze.getHeight());
		int walls = numInternalWalls(maze);
		int cells = maze.getHeight()*maze.getWidth();
		assertEquals(maxWalls-(cells-1), walls);
		//test for not square (20x15)
		maxWalls = maxInternalWalls(maze3.getWidth(), maze3.getHeight());
		walls = numInternalWalls(maze3);
		cells = maze3.getHeight()*maze3.getWidth();
		assertEquals(maxWalls-(cells-1), walls);
	}
	


}


