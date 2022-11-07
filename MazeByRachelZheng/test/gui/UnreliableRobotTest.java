/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.DefaultOrder;
import generation.MazeFactory;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * @author rzhe2
 *
 */
class UnreliableRobotTest extends ReliableRobotTest {
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		order = new DefaultOrder(); //(4x4)
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		robot=new UnreliableRobot("0000");
		controller=new Control();
		playingState=new StatePlaying();
		playingState.setMaze(maze);
		controller.setState(playingState);
		playingState.start(controller, null); //dry-run for testing
		robot.setController(controller);
		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);
	}
	
	/**
	 * Tests that UnreliableRobot changes its sensors to unreliable sensors when indicated by its string parameter
	 */
	@Test
	public void testUnreliableRobotContructor(){
		assertTrue(robot.forwardSensor instanceof UnreliableSensor);
		assertTrue(robot.rightSensor instanceof UnreliableSensor);
		assertTrue(robot.rightSensor instanceof UnreliableSensor);
		assertTrue(robot.backwardSensor instanceof UnreliableSensor);
		UnreliableRobot robot2 = new UnreliableRobot("1001");
		assertTrue(robot2.forwardSensor instanceof ReliableSensor);
		assertTrue(robot2.rightSensor instanceof UnreliableSensor);
		assertTrue(robot2.rightSensor instanceof UnreliableSensor);
		assertTrue(robot2.backwardSensor instanceof ReliableSensor);
	}
	
	@Override
	@Test
	public void testAddDistanceSensor(){
		//check that mounted direction and maze for sensor is correct
		super.testAddDistanceSensor();
		//test for adding an unreliable sensor
		UnreliableSensor testSensor2=new UnreliableSensor();
		robot.addDistanceSensor(testSensor2, Direction.BACKWARD);
		assertEquals(Direction.BACKWARD, testSensor2.mountedDirection);
		assertEquals(maze, testSensor2.maze);
	}
	
	@Override
	@Test
	public void testDistanceToObstacle() {
		super.testDistanceToObstacle();
		try {
			robot.setBatteryLevel(3);
			robot.rotate(Turn.RIGHT); 
			robot.distanceToObstacle(Direction.RIGHT);
			fail("should result in power failure error");
		} catch (Error|Exception e) {
			// TODO: handle exception
			assertTrue(robot.hasStopped);

		}
		try {
			robot.setBatteryLevel(3);
			robot.rotate(Turn.RIGHT); 
			robot.distanceToObstacle(Direction.LEFT);
			fail("should result in power failure error");
		} catch (Error|Exception e) {
			// TODO: handle exception
			assertTrue(robot.hasStopped);

		}
		try {
			robot.setBatteryLevel(3);
			robot.rotate(Turn.RIGHT);
			robot.distanceToObstacle(Direction.BACKWARD);
			fail("should result in power failure error");
		} catch (Error|Exception e) {
			// TODO: handle exception
			assertTrue(robot.hasStopped);

		}
		
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//all sensors should be not operational
		assertFalse(robot.forwardSensor.getOperational());
		assertFalse(robot.leftSensor.getOperational());
		assertFalse(robot.rightSensor.getOperational());
		assertFalse(robot.backwardSensor.getOperational());
		
		//distanceToObstacle should throw Unsupported Operation Exception for all directions since sensors are not operational
		try {
			robot.distanceToObstacle(Direction.FORWARD);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			robot.distanceToObstacle(Direction.LEFT);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			robot.distanceToObstacle(Direction.BACKWARD);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			robot.distanceToObstacle(Direction.RIGHT);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		robot.stopFailureAndRepairProcess(Direction.LEFT);
		robot.stopFailureAndRepairProcess(Direction.RIGHT);
		robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		

		//test for null forward sensor
		robot.forwardSensor=null;
		try {
			robot.distanceToObstacle(Direction.FORWARD);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}
		
		//test for null left sensor
		robot.leftSensor=null;
		try {
			robot.distanceToObstacle(Direction.LEFT);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}
		
		//test for null right sensor
		robot.rightSensor=null;
		try {
			robot.distanceToObstacle(Direction.RIGHT);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}
		
		//test for null backward sensor
		robot.backwardSensor=null;
		try {
			robot.distanceToObstacle(Direction.BACKWARD);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}
	}
	
	@Override
	@Test
	public void testCanSeeThroughTheExitIntoExternity() {
		super.testCanSeeThroughTheExitIntoExternity();
		
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//all sensors should be not operational
		assertFalse(robot.forwardSensor.getOperational());
		assertFalse(robot.leftSensor.getOperational());
		assertFalse(robot.rightSensor.getOperational());
		assertFalse(robot.backwardSensor.getOperational());
		
		//method should throw Unsupported Operation Exception for all directions since sensors are not operational
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.LEFT);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT);
			fail("sensor not operational, should throw UnsupportedOperationException");
		}catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		robot.stopFailureAndRepairProcess(Direction.LEFT);
		robot.stopFailureAndRepairProcess(Direction.RIGHT);
		robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		
		//test for null forward sensor
		robot.forwardSensor=null;
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}

		//test for null left sensor
		robot.leftSensor=null;
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.LEFT);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}

		//test for null right sensor
		robot.rightSensor=null;
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}

		//test for null backward sensor
		robot.backwardSensor=null;
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD);
			fail("sensor is null, should throw Unsupported Operation Exception");
		} catch (UnsupportedOperationException e) {
			// TODO: handle exception
			assertTrue(e.getMessage()=="sensor is null");
		}
	}
	
	@Override
	@Test
	public void testStartFailureAndRepairProcess() {
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//all sensors should be not operational
		assertFalse(robot.forwardSensor.getOperational());
		assertFalse(robot.leftSensor.getOperational());
		assertFalse(robot.rightSensor.getOperational());
		assertFalse(robot.backwardSensor.getOperational());	
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(robot.forwardSensor.getOperational());
		assertTrue(robot.leftSensor.getOperational());
		assertTrue(robot.rightSensor.getOperational());
		assertTrue(robot.backwardSensor.getOperational());	
		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		robot.stopFailureAndRepairProcess(Direction.LEFT);
		robot.stopFailureAndRepairProcess(Direction.RIGHT);
		robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		
		
	}
	
	@Override
	@Test
	public void testStopFailureAndRepairProcess() {
		//test that method fails when failure and repair process has not been started
		try {
			robot.stopFailureAndRepairProcess(Direction.FORWARD);
			fail("FailureAndRepairProcess has not been started");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage().equals("Failure and repair process has not been started. There is nothing to stop."));
		}
		
		try {
			robot.stopFailureAndRepairProcess(Direction.BACKWARD);
			fail("FailureAndRepairProcess has not been started");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage().equals("Failure and repair process has not been started. There is nothing to stop."));
		}
		
		try {
			robot.stopFailureAndRepairProcess(Direction.LEFT);
			fail("FailureAndRepairProcess has not been started");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage().equals("Failure and repair process has not been started. There is nothing to stop."));
		}
		
		try {
			robot.stopFailureAndRepairProcess(Direction.RIGHT);
			fail("FailureAndRepairProcess has not been started");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage().equals("Failure and repair process has not been started. There is nothing to stop."));
		}
		
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//all sensors should be not operational
		assertFalse(robot.forwardSensor.getOperational());
		assertFalse(robot.leftSensor.getOperational());
		assertFalse(robot.rightSensor.getOperational());
		assertFalse(robot.backwardSensor.getOperational());	
		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		robot.stopFailureAndRepairProcess(Direction.LEFT);
		robot.stopFailureAndRepairProcess(Direction.RIGHT);
		robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//all sensors should be left in an operational state when stop is called
		assertTrue(robot.forwardSensor.getOperational());
		assertTrue(robot.leftSensor.getOperational());
		assertTrue(robot.rightSensor.getOperational());
		assertTrue(robot.backwardSensor.getOperational());	
	}
	
	
}
	
