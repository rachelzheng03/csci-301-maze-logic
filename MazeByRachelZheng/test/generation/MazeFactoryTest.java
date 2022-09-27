package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class MazeFactoryTest {
	//private variables
	private MazeBuilder mazebuilder;
	/**
	 * Initialize a MazeBuilder object before each test.
	 */
	@Before
	public void setUp() {
	}
	
	
	/** 
	 * Tests if a maze only has one exit. The maze is correct if it only has one exit. If no exits or more than 1 exit, test fails. 
	 * */
	@Test
	public void testOneExit() {
		//check every border wall and determine if it is an exit. checks for amount of exits. (maybe private method)
		//test for smallest possible maze (4x4)
		//test for small width large height 
		//test for large width small height 
		//test for larger maze (60x60)
		//test for incorrect case: maze with more than 1 exit
		//test for incorrect case: maze with 0 exits
	}

	/**
	 * Tests if there is a way out of the maze from no matter where you are in the maze.
	 */
	@Test
	public void testEveryCellHasPathToExit(){
		//check that every cell of mazedists has a value that is not INFINITY
		//test for smallest possible maze (4x4)
		//test for small width large height 
		//test for large width small height 
		//test for larger maze (60x60)
		//test incorrect case: maze with no exit
		//test incorrect case: maze with blocked off cell
	}
	


}


