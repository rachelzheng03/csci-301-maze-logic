/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author rzhe2
 *
 */
class ReliableRobotTest {

	private ReliableRobot robot;
	private Control controller;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
	}

	/**
	 * Tests that objects created in setUp are not null
	 */
	@Test
	public void testReliableRobot() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSetController() {
		
	}
	
	@Test
	public void testAddDistanceSensor(){
		//check that mounted direction for sensor is correct
	}
	
	@Test
	public void testGetCurrentPosition() {
		
	}
	
	@Test
	public void testGetCurrentDirection() {
		
	}
	
	@Test
	public void testGetBatteryLevel() {
		
	}
	
	@Test
	public void testSetBatteryLevel() {
		
	}
	
	@Test
	public void testGetEnergyForFullRotation() {
		
	}
	
	@Test
	public void testGetEnergyForStepForward() {
		
	}
	
	@Test
	public void testGetOdometerReading() {
		
	}
	
	@Test
	public void testResetOdometer() {
		//should turn odometer back to 0
		
	}
	
	@Test
	public void testRotate() {
		//test rotate right and left from each cardinal direction
		//check that robot has rotated by comparing current direction before and after rotate
		//check battery has gone down 3
	}
	
	@Test
	public void testMove() {
		//test move from each cardinal direction
		//check that robot has moved by comparing current position before and after move
		//check battery has gone down 6	
	}
	
	@Test
	public void testJump() {
		//test jump from each cardinal direction
		//check that robot has moved by comparing current position before and after move
		//check battery has gone down 40
	
	}
	@Test
	public void testIsAtExit() {
		//true case
		//false case
		
	}
	
	@Test
	public void testIsInsideRoom() {
		//true case
		//false case
	}
	
	@Test
	public void testDistanceToObstacle() {
		
	}
	
	@Test
	public void testCanSeeThroughTheExitIntoExternity() {
		//true case
		//false case
		//test at exits facing all directions
	}
	
	@Test
	public void testStartFailureAndRepairProcess() {
		//test that method throws Unsupported Operation Error

	}
	
	@Test
	public void testStopFailureAndRepairProcess() {
		//test that method throws Unsupported Operation Error

	}

}
