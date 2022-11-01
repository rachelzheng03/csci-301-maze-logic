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
	private ReliableSensor frontReliableSensor;
	private ReliableSensor leftReliableSensor;
	private ReliableSensor backReliableSensor;
	private ReliableSensor rightReliableSensor;

	@BeforeEach
	public void setUp(){
		order = new DefaultOrder(); //(4x4)
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		frontReliableSensor=new ReliableSensor();
		leftReliableSensor=new ReliableSensor();
		backReliableSensor=new ReliableSensor();
		rightReliableSensor=new ReliableSensor();
		robotReliable=new ReliableRobot();
		robotUnreliable=new UnreliableRobot("0000");
		controller=new Control();
		playingState=new StatePlaying();
		playingState.setMaze(maze);
		controller.setState(playingState);
		playingState.start(controller, null); //dry-run for testing
		robotReliable.setController(controller);
		robotUnreliable.setController(controller);
		wallfollower=new WallFollower();
		//wallfollower.setRobot(robot);
		wallfollower.setMaze(maze);
		
	}

	@Test
	public void testDrive2Exit() throws Exception {
		//check that robot is at exit
		//check that robot is facing exit
		//check odometer
		//check battery

		robotReliable.addDistanceSensor(rightReliableSensor, Direction.RIGHT);
		robotReliable.addDistanceSensor(frontReliableSensor, Direction.FORWARD);
		robotReliable.addDistanceSensor(leftReliableSensor, Direction.LEFT);
		robotReliable.addDistanceSensor(backReliableSensor, Direction.BACKWARD);

		wallfollower.setRobot(robotReliable);
		
		wallfollower.drive2Exit();
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(13, robotReliable.getOdometerReading());
		
		//start at position (2,2)
		playingState.setCurrentPosition(2, 2);
		robotReliable.resetOdometer();
		wallfollower.drive2Exit();
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(18, robotReliable.getOdometerReading());
		
		//facing exit
		robotReliable.resetOdometer();
		wallfollower.drive2Exit();
		assertTrue(robotReliable.isAtExit());
		assertTrue(robotReliable.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robotReliable.getOdometerReading());
		
		//test that robot stops when battery runs out
		playingState.setCurrentPosition(0, 3);
		robotReliable.setBatteryLevel(16);
		try {
			wallfollower.drive2Exit();
			fail("method should throw exception because not enough battery to get to the exit");
		} catch (Exception e) {
			// TODO: handle exception
			assertEquals(1, robotReliable.getCurrentPosition()[0]);
			assertEquals(3, robotReliable.getCurrentPosition()[1]);
			assertTrue(robotReliable.hasStopped());
			assertTrue(e.getMessage()=="Robot has stopped");
		}
		
	}
//	
//	@Test
//	public void testDrive1Step2Exit() throws Exception {
//		//start from various positions and direction and call drive1step2exitmethod
//		//check that position and direction after method is called is correct
//		//check odometer
//		//check battery
//		wallfollower.drive1Step2Exit();
//		assertEquals(0, robot.getCurrentPosition()[0]);
//		assertEquals(0, robot.getCurrentPosition()[1]);
//		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
//		assertEquals(1, robot.getOdometerReading());
//		
//		playingState.setCurrentPosition(3, 0);
//		assert(robot.getBatteryLevel()==3491);
//		assert(robot.getCurrentDirection()==CardinalDirection.North);
//		robot.resetOdometer();
//		wallfollower.drive1Step2Exit();
//		assertEquals(2, robot.getCurrentPosition()[0]);
//		assertEquals(0, robot.getCurrentPosition()[1]);
//		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
//		assertTrue(robot.isAtExit()); 
//		assertEquals(1, robot.getOdometerReading());
//		
//		//at exit, facing exit (North)
//		robot.resetOdometer();
//		wallfollower.drive1Step2Exit();
//		assertEquals(2, robot.getCurrentPosition()[0]);
//		assertEquals(0, robot.getCurrentPosition()[1]);
//		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
//		assertTrue(robot.isAtExit());
//		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
//		assertEquals(0, robot.getOdometerReading());
//		
//		//at west exit
//		order.setSeed(14);
//		factory.order(order);
//		factory.waitTillDelivered();
//		maze = order.getMaze();
//		robot=new ReliableRobot();
//		controller=new Control();
//		playingState=new StatePlaying();
//		playingState.setMaze(maze);
//		controller.setState(playingState);
//		playingState.start(controller, null); //dry-run for testing
//		robot.setController(controller);
//		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
//    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
//		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
//		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);
//		wallfollower=new wallfollower();
//		wallfollower.setRobot(robot);
//		wallfollower.setMaze(maze);
//		playingState.setCurrentPosition(0, 0);
//		wallfollower.drive1Step2Exit();
//		assertEquals(0, robot.getCurrentPosition()[0]);
//		assertEquals(0, robot.getCurrentPosition()[1]);
//		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
//		assertTrue(robot.isAtExit());
//		assertEquals(3493, robot.getBatteryLevel());
//		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
//		assertEquals(0, robot.getOdometerReading());
//		
//		//at east exit
//		order.setSeed(-326469280);
//		factory.order(order);
//		factory.waitTillDelivered();
//		maze = order.getMaze();
//		robot=new ReliableRobot();
//		controller=new Control();
//		playingState=new StatePlaying();
//		playingState.setMaze(maze);
//		controller.setState(playingState);
//		playingState.start(controller, null); //dry-run for testing
//		robot.setController(controller);
//		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
//    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
//		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
//		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);
//		wallfollower=new wallfollower();
//		wallfollower.setRobot(robot);
//		wallfollower.setMaze(maze);
//		robot.rotate(Turn.RIGHT); //cd: north
//		playingState.setCurrentPosition(3, 2);
//		assert(robot.getBatteryLevel()==3497);
//		wallfollower.drive1Step2Exit();
//		assertEquals(3, robot.getCurrentPosition()[0]);
//		assertEquals(2, robot.getCurrentPosition()[1]);
//		assertEquals(CardinalDirection.East, robot.getCurrentDirection());
//		assertTrue(robot.isAtExit());
//		assertEquals(3493, robot.getBatteryLevel());
//		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
//		assertEquals(0, robot.getOdometerReading());	
//	}
	
//	@Test
//	public void testGetEnergyConsumption() throws Exception {
//		wallfollower.drive2Exit();
//		assertEquals(106, wallfollower.getEnergyConsumption());
//	}
//	
	@Test
	public void testGetPathLength() throws Exception {
		//test method after various steps to exit
		robotReliable.addDistanceSensor(rightReliableSensor, Direction.RIGHT);
		robotReliable.addDistanceSensor(frontReliableSensor, Direction.FORWARD);
		robotReliable.addDistanceSensor(leftReliableSensor, Direction.LEFT);
		robotReliable.addDistanceSensor(backReliableSensor, Direction.BACKWARD);

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
		assertEquals(13, wallfollower.getPathLength());
	}

//}


}
