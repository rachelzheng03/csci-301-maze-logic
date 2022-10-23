/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;

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
	public void testDrive2Exit() {
		//check that robot is at exit
		//check that robot is facing exit
		//check odometer
		//check battery
	}
	
	@Test
	public void testDrive1Step2Exit() {
		//start from various positions and direction and call drive1step2exitmethod
		//check that position and direction after method is called is correct
		//check odometer
		//check battery
	}
	
	@Test
	public void testGetEnergyConsumption() {
		
	}
	
	@Test
	public void testGetPathLength() {
		//test method after various steps to exit
	}

}
