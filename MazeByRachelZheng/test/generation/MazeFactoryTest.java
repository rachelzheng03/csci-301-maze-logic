package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MazeFactoryTest {
	
	// private variables
	private DefaultOrder order; //(4x4)
	private DefaultOrder order2; //(20x15)
	private DefaultOrder order3; //(25x25)
	private MazeFactory factory; 
	
	/**
	 * Initialize MazeBuilder objects before each test. (3 mazes: 4x4, 25x25, 20x15)
	 */
	@BeforeEach
	public void setUp() {
		order = new DefaultOrder(0); //(4x4)
		order2 = new DefaultOrder(3); //(20x15)
		order3 = new DefaultOrder(5); //(25x25)
		factory=new MazeFactory();
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
	}
	
	/** 
	 * Tests if a maze only has one exit. The maze is correct if it only has one exit. If no exits or more than 1 exit, test fails. 
	 */
	@Test
	public void testOneExit() {
		//check every border wall and determine if it is an exit. checks for amount of exits. (maybe private method)
		//test for smallest possible maze (4x4)
		//test for not square (20x15)
		//test for larger maze (25x25)
		//test for incorrect case: maze with more than 1 exit
		//test for incorrect case: maze with 0 exits
	}

	/**
	 * Tests if there is a way out of the maze from no matter where you are in the maze. Correct if mazedists contains a valid distance value for every cell.
	 */
	@Test
	public void testEveryCellHasPathToExit(){
		//check that every cell of mazedists has a value that is not INFINITY
		//test for smallest possible maze (4x4)
		//test for not square (20x15)
		//test for larger maze (25x25)
		//test incorrect case: maze with no exit
		//test incorrect case: maze with closed off cell
	}
	/**
	 * Tests if a perfect maze with n cells and no rooms has the correct amount of walls.
	 */
	@Test
	public void testWallsInPerfectMaze() {
		//make sure maze is perfect or create a perfect maze
		//get maximum amount of internal walls a maze of size (nxm) can have
		//get amount of cells
		//get amount of internal walls in maze
		//check that amount of internal walls in maze = maximum amount of internal walls-(amount of cells-1)
		//test for smallest possible maze (4x4)
		//test for not square (20x15)
		//test for larger maze (25x25)
		//test incorrect case: maze with closed off cell
	}
	


}


