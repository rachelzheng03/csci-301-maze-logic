package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;
import gui.Robot.Turn;

class WallFollowerTest {
	
	private DefaultOrder order;
	private MazeFactory factory;
	private Maze maze;
	private ReliableRobot robotReliable;
	private UnreliableRobot robotUnreliable;
	private Control controller;
	private StatePlaying playingState;
	private WallFollower wallfollower;

	@BeforeEach
	public void setUp(){
		order = new DefaultOrder(); //(4x4)
		order.setSeed(-235114766); //maze where wallfollower has a different path than wizard 
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered(); 
		maze = order.getMaze();
		robotReliable=new ReliableRobot();
		robotUnreliable=new UnreliableRobot("0000");
		controller=new Control();
		playingState=new StatePlaying();
		playingState.setMaze(maze);
		controller.setState(playingState);
		playingState.start(controller, null); //dry-run for testing
		robotReliable.setController(controller);
		robotUnreliable.setController(controller);
		robotReliable.addDistanceSensor(robotReliable.forwardSensor, Direction.FORWARD);
    	robotReliable.addDistanceSensor(robotReliable.backwardSensor, Direction.BACKWARD);
		robotReliable.addDistanceSensor(robotReliable.leftSensor, Direction.LEFT);
		robotReliable.addDistanceSensor(robotReliable.rightSensor, Direction.RIGHT);
		robotUnreliable.addDistanceSensor(robotUnreliable.forwardSensor, Direction.FORWARD);
    	robotUnreliable.addDistanceSensor(robotUnreliable.backwardSensor, Direction.BACKWARD);
		robotUnreliable.addDistanceSensor(robotUnreliable.leftSensor, Direction.LEFT);
		robotUnreliable.addDistanceSensor(robotUnreliable.rightSensor, Direction.RIGHT);
		wallfollower=new WallFollower();
		wallfollower.setMaze(maze);
		
	}

	@Test
	public void testDrive2ExitWithReliableRobot() throws Exception {
		//check that robot is at exit
		//check that robot is facing exit
		//check odometer

		wallfollower.setRobot(robotReliable);
		wallfollower.drive2Exit();
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(16, wallfollower.getPathLength());
		
		//start at position (0,11)
		playingState.setCurrentPosition(1, 1);
		robotReliable.rotate(Turn.RIGHT);
		robotReliable.resetOdometer();
		wallfollower.drive2Exit();
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); 
		assertEquals(8, robotReliable.getOdometerReading());
		
		//facing exit
		robotReliable.resetOdometer();
		wallfollower.drive2Exit();
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robotReliable.getOdometerReading());
		
		//test that robot stops when battery runs out
		playingState.setCurrentPosition(0, 0);
		robotReliable.setBatteryLevel(18);
		try {
			wallfollower.drive2Exit();
			fail("method should throw exception because not enough battery to get to the exit");
		} catch (Exception e) {
			// TODO: handle exception
			assertEquals(2, robotReliable.getCurrentPosition()[0]);
			assertEquals(0, robotReliable.getCurrentPosition()[1]);
			assertTrue(robotReliable.hasStopped());
			assertTrue(e.getMessage()=="Robot has stopped");
		}
		
		//at west exit
		order.setSeed(14);
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		robotReliable=new ReliableRobot();
		controller=new Control();
		playingState=new StatePlaying();
		playingState.setMaze(maze);
		controller.setState(playingState);
		playingState.start(controller, null); //dry-run for testing
		robotReliable.setController(controller);
		robotReliable.addDistanceSensor(robotReliable.forwardSensor, Direction.FORWARD);
		robotReliable.addDistanceSensor(robotReliable.backwardSensor, Direction.BACKWARD);
		robotReliable.addDistanceSensor(robotReliable.leftSensor, Direction.LEFT);
		robotReliable.addDistanceSensor(robotReliable.rightSensor, Direction.RIGHT);
		wallfollower=new WallFollower();
		wallfollower.setRobot(robotReliable);
		wallfollower.setMaze(maze);
		playingState.setCurrentPosition(0, 0);
		wallfollower.drive2Exit();
		assertEquals(0, robotReliable.getCurrentPosition()[0]);
		assertEquals(0, robotReliable.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.West, robotReliable.getCurrentDirection());
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robotReliable.getOdometerReading());
		
		//at north exit
		order.setSeed(13);
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		robotReliable=new ReliableRobot();
		controller=new Control();
		playingState=new StatePlaying();
		playingState.setMaze(maze);
		controller.setState(playingState);
		playingState.start(controller, null); //dry-run for testing
		robotReliable.setController(controller);
		robotReliable.addDistanceSensor(robotReliable.forwardSensor, Direction.FORWARD);
		robotReliable.addDistanceSensor(robotReliable.backwardSensor, Direction.BACKWARD);
		robotReliable.addDistanceSensor(robotReliable.leftSensor, Direction.LEFT);
		robotReliable.addDistanceSensor(robotReliable.rightSensor, Direction.RIGHT);
		wallfollower=new WallFollower();
		wallfollower.setRobot(robotReliable);
		wallfollower.setMaze(maze);
		assert(robotReliable.getCurrentDirection()==CardinalDirection.East);
		playingState.setCurrentPosition(2, 0);
		wallfollower.drive2Exit();
		assertEquals(2, robotReliable.getCurrentPosition()[0]);
		assertEquals(0, robotReliable.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.North, robotReliable.getCurrentDirection());
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robotReliable.getOdometerReading());	

	}
	
	@Test
	public void testDrive2ExitWithUnreliableRobot() throws Exception {
		//tests that method works with failure and repair process of sensors
		
		//check that robot is at exit
		//check that robot is facing exit
		//check odometer

		wallfollower.setRobot(robotUnreliable);
		robotUnreliable.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);

		Thread.sleep(4000);
		
		wallfollower.drive2Exit();
		assertTrue(robotUnreliable.isAtExit());
		assertTrue(robotUnreliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(16, wallfollower.getPathLength());

		robotUnreliable.stopFailureAndRepairProcess(Direction.FORWARD);
		robotUnreliable.stopFailureAndRepairProcess(Direction.LEFT);
		robotUnreliable.stopFailureAndRepairProcess(Direction.RIGHT);
		robotUnreliable.stopFailureAndRepairProcess(Direction.BACKWARD);
	}
	
	@Test
	public void testDrive1Step2ExitWithReliableRobot() throws Exception {
		//start from various positions and direction and call drive1step2exitmethod
		//check that position and direction after method is called is correct
		//check odometer
		wallfollower.setRobot(robotReliable);
		wallfollower.drive1Step2Exit();
		assertEquals(1, robotReliable.getCurrentPosition()[0]);
		assertEquals(0, robotReliable.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.East, robotReliable.getCurrentDirection());
		assertEquals(1, robotReliable.getOdometerReading());
		
		playingState.setCurrentPosition(2, 3);
		assert(robotReliable.getCurrentDirection()==CardinalDirection.East);
		robotReliable.resetOdometer();
		wallfollower.drive1Step2Exit();
		assertEquals(3, robotReliable.getCurrentPosition()[0]);
		assertEquals(3, robotReliable.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.East, robotReliable.getCurrentDirection());
		assertTrue(robotReliable.isAtExit()); 
		assertEquals(1, robotReliable.getOdometerReading());
		
		//at exit, facing exit (east)
		robotReliable.resetOdometer();
		wallfollower.drive1Step2Exit();
		assertEquals(3, robotReliable.getCurrentPosition()[0]);
		assertEquals(3, robotReliable.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.East, robotReliable.getCurrentDirection());
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robotReliable.getOdometerReading());
	}
	
	@Test
	public void testDrive1Step2ExitWithUneliableRobot() throws Exception {
		//check that position and direction after method is called is correct
		//check odometer
		wallfollower.setRobot(robotUnreliable);
		robotUnreliable.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);

		Thread.sleep(4000);
		
		wallfollower.drive1Step2Exit();

		assertEquals(1, robotUnreliable.getCurrentPosition()[0]);
		assertEquals(0, robotUnreliable.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.East, robotUnreliable.getCurrentDirection());
		assertEquals(1, wallfollower.getPathLength());
		
		robotUnreliable.stopFailureAndRepairProcess(Direction.FORWARD);
		robotUnreliable.stopFailureAndRepairProcess(Direction.LEFT);
		robotUnreliable.stopFailureAndRepairProcess(Direction.RIGHT);
		robotUnreliable.stopFailureAndRepairProcess(Direction.BACKWARD);
	}
	
	@Test
	public void testGetEnergyConsumption() throws Exception {
		wallfollower.setRobot(robotReliable);
		wallfollower.drive2Exit();
		assertEquals(157, wallfollower.getEnergyConsumption());
	}
	
	@Test
	public void testGetPathLengthWithReliableRobot() throws Exception {
		//test method after various steps to exit
		wallfollower.setRobot(robotReliable);
		wallfollower.drive1Step2Exit();
		assertEquals(1, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(2, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(3, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(4, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(5, wallfollower.getPathLength());
		wallfollower.drive2Exit();
		assertEquals(16, wallfollower.getPathLength());
	}
	
	@Test
	public void testGetPathLengthWithUnreliableRobot() throws Exception {
		//test method after various steps to exit with unreliable sensors
		wallfollower.setRobot(robotUnreliable);
		robotUnreliable.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robotUnreliable.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);

		Thread.sleep(4000);
		
		wallfollower.drive1Step2Exit();
		assertEquals(1, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(2, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(3, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(4, wallfollower.getPathLength());
		wallfollower.drive1Step2Exit();
		assertEquals(5, wallfollower.getPathLength());
		wallfollower.drive2Exit();
		assertEquals(16, wallfollower.getPathLength());
		
		robotUnreliable.stopFailureAndRepairProcess(Direction.FORWARD);
		robotUnreliable.stopFailureAndRepairProcess(Direction.LEFT);
		robotUnreliable.stopFailureAndRepairProcess(Direction.RIGHT);
		robotUnreliable.stopFailureAndRepairProcess(Direction.BACKWARD);
	}



}
