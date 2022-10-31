/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;

/**
 * @author rzhe2
 *
 */
class UnreliableSensorTest{

	private UnreliableSensor forwardSensor;
	private UnreliableSensor backwardSensor;
	private UnreliableSensor leftSensor;
	private UnreliableSensor rightSensor;
	private DefaultOrder order;
	private MazeFactory factory;
	private Maze maze;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		forwardSensor=new UnreliableSensor(); //forward sensor
		backwardSensor=new UnreliableSensor(); //backward sensor
		leftSensor=new UnreliableSensor(); //left sensor
		rightSensor=new UnreliableSensor(); //right sensor
		order = new DefaultOrder(); //(4x4)
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
	}

	/**
	 * Tests if setUp created objects. Correct if not null.
	 */
	@Test
	public void testSetUpWorks() {
		assertNotNull(factory);
		assertNotNull(order);
		assertNotNull(maze);
		assertNotNull(backwardSensor);
		assertNotNull(forwardSensor);
		assertNotNull(leftSensor);
		assertNotNull(rightSensor);
	}
	
	@Test
	public void testDistanceToObject() {
		float[] power = {3500};
		int[] position = {1,2};
		float[] powerFail = {0};

		forwardSensor.setSensorDirection(Direction.FORWARD);
		backwardSensor.setSensorDirection(Direction.BACKWARD);
		leftSensor.setSensorDirection(Direction.LEFT);
		rightSensor.setSensorDirection(Direction.RIGHT);
		
		forwardSensor.setMaze(maze);
		backwardSensor.setMaze(maze);
		leftSensor.setMaze(maze);
		rightSensor.setMaze(maze);
		
		//test when at least one of the parameters passed in is null
		try {
			forwardSensor.distanceToObstacle(position, null, power);
			fail("Should have resulted in an IllegalArgumentException because one of the parameters is null");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		//test power failure exception
		try {
			forwardSensor.distanceToObstacle(position, CardinalDirection.North, powerFail);
			fail("should throw power failure because power is at 0");
		} catch (Error | Exception e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="Power Failure: power supply is insufficient for the operation");
		}
		
		try {
			//test North facing robot, forward sensor
			assertEquals(2, forwardSensor.distanceToObstacle(position, CardinalDirection.North, power));
			//test North facing robot, right sensor
			assertEquals(1, rightSensor.distanceToObstacle(position, CardinalDirection.North, power));
			//test North facing robot, left sensor
			assertEquals(0, leftSensor.distanceToObstacle(position, CardinalDirection.North, power));
			//test North facing robot, backward sensor
			assertEquals(0, backwardSensor.distanceToObstacle(position, CardinalDirection.North, power));
			
			//test East facing robot, forward sensor
			assertEquals(0, forwardSensor.distanceToObstacle(position, CardinalDirection.East, power));
			//test East facing robot, right sensor
			assertEquals(2, rightSensor.distanceToObstacle(position, CardinalDirection.East, power));
			//test East facing robot, left sensor
			assertEquals(0, leftSensor.distanceToObstacle(position, CardinalDirection.East, power));
			//test East facing robot, backward sensor
			assertEquals(1, backwardSensor.distanceToObstacle(position, CardinalDirection.East, power));

			//test South facing robot, forward sensor
			assertEquals(0, forwardSensor.distanceToObstacle(position, CardinalDirection.South, power));
			//test South facing robot, right sensor
			assertEquals(0, rightSensor.distanceToObstacle(position, CardinalDirection.South, power));
			//test South facing robot, left sensor
			assertEquals(1, leftSensor.distanceToObstacle(position, CardinalDirection.South, power));
			//test South facing robot, backward sensor
			assertEquals(2, backwardSensor.distanceToObstacle(position, CardinalDirection.South, power));

			//test West facing robot, forward sensor
			assertEquals(1, forwardSensor.distanceToObstacle(position, CardinalDirection.West, power));
			//test West facing robot, right sensor
			assertEquals(0, rightSensor.distanceToObstacle(position, CardinalDirection.West, power));
			//test West facing robot, left sensor
			assertEquals(2, leftSensor.distanceToObstacle(position, CardinalDirection.West, power));
			//test West facing robot, backward sensor
			assertEquals(0, backwardSensor.distanceToObstacle(position, CardinalDirection.West, power));

			//test that method return Integer.Max_Value if robot is facing the exit
			int[] exit={2,0};
			assertEquals(Integer.MAX_VALUE, backwardSensor.distanceToObstacle(exit, CardinalDirection.South, power));
			assertEquals(Integer.MAX_VALUE, rightSensor.distanceToObstacle(exit, CardinalDirection.East, power));
			assertEquals(Integer.MAX_VALUE,	leftSensor.distanceToObstacle(exit, CardinalDirection.West, power));
			assertEquals(Integer.MAX_VALUE, forwardSensor.distanceToObstacle(exit, CardinalDirection.North, power));
			
			order.setSeed(14);
			factory.order(order);
			factory.waitTillDelivered();
			maze = order.getMaze();
			rightSensor.setMaze(maze);
			int[] exit2=maze.getExitPosition();
			assertEquals(Integer.MAX_VALUE, rightSensor.distanceToObstacle(exit2, CardinalDirection.North, power));

			order.setSeed(-326469280);
			factory.order(order);
			factory.waitTillDelivered();
			maze = order.getMaze();
			leftSensor.setMaze(maze);
			int[] exit3 = maze.getExitPosition();
			assertEquals(Integer.MAX_VALUE, leftSensor.distanceToObstacle(exit3, CardinalDirection.North, power));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
		
		//test for sensor failure
		try {
			forwardSensor.startFailureAndRepairProcess(4, 2);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			assertEquals("Sensor Failure: sensor not operational", e.getMessage());
		}
	}
	
	@Test
	public void testGetEnergyConsumptionForSensing() {
		//should always be 1
		assertEquals(1, forwardSensor.getEnergyConsumptionForSensing());
		assertEquals(1, backwardSensor.getEnergyConsumptionForSensing());
		assertEquals(1, leftSensor.getEnergyConsumptionForSensing());
		assertEquals(1, rightSensor.getEnergyConsumptionForSensing());
	}
	
	
	@Test
	public void testStartFailureAndRepairProcess() {
		forwardSensor.setSensorDirection(Direction.FORWARD);
		forwardSensor.setMaze(maze);
		forwardSensor.startFailureAndRepairProcess(4, 2);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		assertFalse(forwardSensor.getOperational());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		assertTrue(forwardSensor.getOperational());
	}
	
	@Test
	public void testStopFailureAndRepairProcess() {
		forwardSensor.setSensorDirection(Direction.FORWARD);
		forwardSensor.setMaze(maze);
		try {
			forwardSensor.stopFailureAndRepairProcess();
			fail("no failure and repair process has been started");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage().equals("Failure and repair process has not been started. There is nothing to stop."));
		}
		forwardSensor.startFailureAndRepairProcess(4, 2);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		assertFalse(forwardSensor.getOperational());
		forwardSensor.stopFailureAndRepairProcess();
		//sensor should end in an operational state
		assertTrue(forwardSensor.getOperational());
		
	}

}
