package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Distance;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;
import gui.Robot.Turn;

class SmartWizardTest extends WizardTest {

	@BeforeEach
	public void setUp() {
		order = new DefaultOrder(); //(4x4)
		order.setSeed(86422);
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
		wizard=new SmartWizard();
		wizard.setRobot(robot);
		wizard.setMaze(maze);
	}

	@Override
	@Test
	public void testDrive2Exit() throws Exception {
		//check that robot is at exit
		//check that robot is facing exit
		//check odometer
		//check battery
		wizard.drive2Exit();
		assertTrue(robot.isAtExit());
		assertEquals(3450, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(1, robot.getOdometerReading());
		
		//start at position (2,2), battery is currently at 3393
		playingState.setCurrentPosition(2, 2);
		robot.resetOdometer();
		wizard.drive2Exit();
		assertTrue(robot.isAtExit());
		assertEquals(3390, robot.getBatteryLevel());
		assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)); //subtract 1 from battery
		assertEquals(3, robot.getOdometerReading());	
	}
	
	@Override
	@Test
	public void testDrive1Step2Exit() throws Exception {
	}
	
	@Override
	@Test
	public void testGetEnergyConsumption() throws Exception {
		wizard.drive2Exit();
		assertEquals(50, wizard.getEnergyConsumption());
	}
	
	@Override
	@Test
	public void testGetPathLength() throws Exception {
		//test method after various steps to exit
		wizard.drive1Step2Exit();
		assertEquals(1, wizard.getPathLength());
		wizard.drive1Step2Exit();
		assertEquals(1, wizard.getPathLength());
	}

}
