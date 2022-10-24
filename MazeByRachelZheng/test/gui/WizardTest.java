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
import gui.Robot.Turn;

/**
 * @author rzhe2
 *
 */
class WizardTest {

	private Wizard wizard;
	private DefaultOrder order;
	private MazeFactory factory;
	private Maze maze;
	private ReliableRobot robot;
	private Control controller;
	private StatePlaying playingState;

	@BeforeEach
	public void setUp(){
		order = new DefaultOrder(); //(4x4)
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		robot=new ReliableRobot();
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
		wizard=new Wizard();
		wizard.setRobot(robot);
		wizard.setMaze(maze);
	}

	@Test
	public void testDrive2Exit() throws Exception {
		//check that robot is at exit
		//check that robot is facing exit
		//check odometer
		//check battery
		wizard.drive2Exit();
		assertTrue(robot.isAtExit());
		assertEquals(3394, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(13, robot.getOdometerReading());
		
		//start at position (2,2), battery is currently at 3393
		playingState.setCurrentPosition(2, 2);
		assert(robot.getBatteryLevel()==3393);
		robot.resetOdometer();
		wizard.drive2Exit();
		assertTrue(robot.isAtExit());
		assertEquals(3296, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(12, robot.getOdometerReading());
		
		//facing exit, battery currently at 3295
		assert(robot.getBatteryLevel()==3295);
		robot.resetOdometer();
		wizard.drive2Exit();
		assertTrue(robot.isAtExit());
		assertEquals(3294, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robot.getOdometerReading());
		
		//test that robot stops when battery runs out
		playingState.setCurrentPosition(0, 3);
		robot.setBatteryLevel(16);
		try {
			wizard.drive2Exit();
			fail("method should throw exception because not enough battery to get to the exit");
		} catch (Exception e) {
			// TODO: handle exception
			assertEquals(2, robot.getCurrentPosition()[0]);
			assertEquals(3, robot.getCurrentPosition()[1]);
			assertTrue(robot.hasStopped());
			assertTrue(e.getMessage()=="Robot has stopped");
		}
		
	}
	
	@Test
	public void testDrive1Step2Exit() throws Exception {
		//start from various positions and direction and call drive1step2exitmethod
		//check that position and direction after method is called is correct
		//check odometer
		//check battery
		wizard.drive1Step2Exit();
		assertEquals(0, robot.getCurrentPosition()[0]);
		assertEquals(0, robot.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
		assertEquals(3491, robot.getBatteryLevel());
		assertEquals(1, robot.getOdometerReading());
		
		playingState.setCurrentPosition(3, 0);
		assert(robot.getBatteryLevel()==3491);
		assert(robot.getCurrentDirection()==CardinalDirection.North);
		robot.resetOdometer();
		wizard.drive1Step2Exit();
		assertEquals(2, robot.getCurrentPosition()[0]);
		assertEquals(0, robot.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
		assertEquals(3482, robot.getBatteryLevel());
		assertTrue(robot.isAtExit()); 
		assertEquals(1, robot.getOdometerReading());
		
		//at exit, facing exit (North)
		assert(robot.getBatteryLevel()==3482);
		robot.resetOdometer();
		wizard.drive1Step2Exit();
		assertEquals(2, robot.getCurrentPosition()[0]);
		assertEquals(0, robot.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
		assertTrue(robot.isAtExit());
		assertEquals(3478, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robot.getOdometerReading());
		
		//at west exit
		order.setSeed(14);
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		robot=new ReliableRobot();
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
		wizard=new Wizard();
		wizard.setRobot(robot);
		wizard.setMaze(maze);
		playingState.setCurrentPosition(0, 0);
		wizard.drive1Step2Exit();
		assertEquals(0, robot.getCurrentPosition()[0]);
		assertEquals(0, robot.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
		assertTrue(robot.isAtExit());
		assertEquals(3493, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robot.getOdometerReading());
		
		//at east exit
		order.setSeed(-326469280);
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		robot=new ReliableRobot();
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
		wizard=new Wizard();
		wizard.setRobot(robot);
		wizard.setMaze(maze);
		robot.rotate(Turn.RIGHT); //cd: north
		playingState.setCurrentPosition(3, 2);
		assert(robot.getBatteryLevel()==3497);
		wizard.drive1Step2Exit();
		assertEquals(3, robot.getCurrentPosition()[0]);
		assertEquals(2, robot.getCurrentPosition()[1]);
		assertEquals(CardinalDirection.East, robot.getCurrentDirection());
		assertTrue(robot.isAtExit());
		assertEquals(3493, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(0, robot.getOdometerReading());	
	}
	
	@Test
	public void testGetEnergyConsumption() throws Exception {
		wizard.drive2Exit();
		assertEquals(106, wizard.getEnergyConsumption());
	}
	
	@Test
	public void testGetPathLength() throws Exception {
		//test method after various steps to exit
		wizard.drive1Step2Exit();
		assertEquals(1, wizard.getPathLength());
		wizard.drive1Step2Exit();
		assertEquals(2, wizard.getPathLength());
		wizard.drive1Step2Exit();
		assertEquals(3, wizard.getPathLength());
		wizard.drive1Step2Exit();
		assertEquals(4, wizard.getPathLength());
		wizard.drive1Step2Exit();
		assertEquals(5, wizard.getPathLength());
		wizard.drive2Exit();
		assertEquals(13, wizard.getPathLength());
	}

}
