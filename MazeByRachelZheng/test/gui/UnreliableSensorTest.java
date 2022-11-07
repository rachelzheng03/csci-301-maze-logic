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
class UnreliableSensorTest extends ReliableSensorTest{

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
	
	@Override
	@Test
	public void testDistanceToObject() {
		super.testDistanceToObject();
		((UnreliableSensor) forwardSensor).setOperational(false);
		try {
			int[] position= {1,2};
			float[] powersupply= {3500};
			forwardSensor.distanceToObstacle(position, CardinalDirection.North, powersupply);
			fail("should throw Error because sensor is not operational");
		} catch (Error|Exception e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="Sensor Failure: sensor not operational");
		}
	}
	
	
	@Override
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
		forwardSensor.stopFailureAndRepairProcess();
	}
	
	@Override
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
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sensor should end in an operational state
		assertTrue(forwardSensor.getOperational());
		
	}


}
