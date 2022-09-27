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
		//test for small width large height (4x240)
		//test for large width small height (300x4)
		//test for largest possible maze (300x240)
		//test for incorrect case: maze with more than 1 exit
		//test for incorrect case: maze with 0 exits
	}



}


