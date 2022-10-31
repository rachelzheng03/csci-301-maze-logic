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
class UnreliableRobotTest {
	
	private UnreliableRobot robot;
	private Control controller;
	private StatePlaying playingState;
	private DefaultOrder order;
	private MazeFactory factory;
	private Maze maze;
	
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
		robot=new UnreliableRobot();
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
	 * Tests that objects created in setUp are not null and set up correctly
	 */
	@Test
	public void testSetUpWorks() {
		assertNotNull(order);
		assertNotNull(factory);
		assertNotNull(maze);
		assertNotNull(robot);
		assertNotNull(controller);
		assertNotNull(playingState);
	}
	
	@Test
	public void testAddDistanceSensor(){
		//check that mounted direction for sensor is correct
		ReliableSensor testReliableSensor = new ReliableSensor();
		robot.addDistanceSensor(testReliableSensor, Direction.FORWARD);
		assertNotNull(testReliableSensor);
		assertEquals(Direction.FORWARD, testReliableSensor.mountedDirection);
		assertEquals(maze, testReliableSensor.maze);
		UnreliableSensor testUnreliableSensor=new UnreliableSensor();
		robot.addDistanceSensor(testUnreliableSensor, Direction.FORWARD);
		assertNotNull(testUnreliableSensor);
		assertEquals(Direction.FORWARD, testUnreliableSensor.mountedDirection);
		assertEquals(maze, testUnreliableSensor.maze);
	}
}
