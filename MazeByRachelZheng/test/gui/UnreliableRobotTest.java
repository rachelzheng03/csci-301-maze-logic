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
		robot=new UnreliableRobot("0000");
		controller=new Control();
		playingState=new StatePlaying();
		playingState.setMaze(maze);
		controller.setState(playingState);
		playingState.start(controller, null); //dry-run for testing
		robot.setController(controller);
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
		
		//set forward sensor in robot to a reliable sensor
		robot.addDistanceSensor(testReliableSensor, Direction.FORWARD);
		assertEquals(Direction.FORWARD, testReliableSensor.mountedDirection);
		assertEquals(maze, testReliableSensor.maze);
		
		UnreliableSensor testUnreliableSensor=new UnreliableSensor();
		
		//set forward sensor in robot to an unreliable sensor
		robot.addDistanceSensor(testUnreliableSensor, Direction.FORWARD);
		assertEquals(Direction.FORWARD, testUnreliableSensor.mountedDirection);
		assertEquals(maze, testUnreliableSensor.maze);
		
		//set left sensor in robot to a reliable sensor
		robot.addDistanceSensor(testReliableSensor, Direction.LEFT);
		assertEquals(Direction.LEFT, testReliableSensor.mountedDirection);
		assertEquals(maze, testReliableSensor.maze);
		
		//set left sensor in robot to an unreliable sensor
		robot.addDistanceSensor(testUnreliableSensor, Direction.LEFT);
		assertEquals(Direction.LEFT, testUnreliableSensor.mountedDirection);
		assertEquals(maze, testUnreliableSensor.maze);
		
		//set right sensor in robot to a reliable sensor
		robot.addDistanceSensor(testReliableSensor, Direction.RIGHT);		
		assertEquals(Direction.RIGHT, testReliableSensor.mountedDirection);
		assertEquals(maze, testReliableSensor.maze);
		
		//set right sensor in robot to an unreliable sensor
		robot.addDistanceSensor(testUnreliableSensor, Direction.RIGHT);
		assertEquals(Direction.RIGHT, testUnreliableSensor.mountedDirection);
		assertEquals(maze, testUnreliableSensor.maze);
		
		//set back sensor in robot to a reliable sensor
		robot.addDistanceSensor(testReliableSensor, Direction.BACKWARD);		
		assertEquals(Direction.BACKWARD, testReliableSensor.mountedDirection);
		assertEquals(maze, testReliableSensor.maze);
		
		//set back sensor in robot to an unreliable sensor
		robot.addDistanceSensor(testUnreliableSensor, Direction.BACKWARD);
		assertEquals(Direction.BACKWARD, testUnreliableSensor.mountedDirection);
		assertEquals(maze, testUnreliableSensor.maze);
	}
	
	@Test
	public void testGetCurrentPosition() {
		try {
			assertEquals(controller.getCurrentPosition()[0], robot.getCurrentPosition()[0]);
			assertEquals(controller.getCurrentPosition()[1], robot.getCurrentPosition()[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("current position is outside of maze");
		}
	}
	
	@Test
	public void testGetCurrentDirection() {
		assertEquals(controller.getCurrentDirection(), robot.getCurrentDirection());
	}
	
	@Test
	public void testGetAndSetBatteryLevel() {
		//default battery level
		assertEquals(3500, robot.getBatteryLevel());
		
		robot.setBatteryLevel(20);
		assertEquals(20, robot.getBatteryLevel());
		
		//parameter <=0: robot has stopped, battery level should not change
		robot.setBatteryLevel(0);
		assertTrue(robot.hasStopped());
		assertEquals(20, robot.getBatteryLevel());
		
		//when the robot has stopped, passing in a valid battery level does not make the robot now work
		robot.setBatteryLevel(40);
		assertTrue(robot.hasStopped());
		
	}
	
	@Test
	public void testGetEnergyForFullRotation() {
		assertEquals(12, robot.getEnergyForFullRotation());
	}
	
	@Test
	public void testGetEnergyForStepForward() {
		assertEquals(6, robot.getEnergyForStepForward());
	}

	
	@Test
	public void testResetOdometer() {
		//should turn odometer back to 0
		robot.jump();
		assert(robot.getOdometerReading()==1);
		robot.resetOdometer();
		assertEquals(0, robot.getOdometerReading());
	}
	
	@Test
	public void testRotate() {
		//IMPORTANT NOTE: north and south are backwards
		//robot starts at east
		
		//test rotate right and left from each cardinal direction
		//check that robot has rotated by comparing current direction before and after rotate
		//check battery has gone down 3
		//assert(robot.getCurrentDirection()==);
		
		//Testing rotate right:
		//from East
		robot.rotate(Turn.RIGHT);
		assertEquals(CardinalDirection.North, robot.getCurrentDirection()); 
		assertEquals(3497, robot.getBatteryLevel());
		//from North
		robot.rotate(Turn.RIGHT);
		assertEquals(CardinalDirection.West, robot.getCurrentDirection()); 
		assertEquals(3494, robot.getBatteryLevel());
		//from West
		robot.rotate(Turn.RIGHT);
		assertEquals(CardinalDirection.South, robot.getCurrentDirection()); 
		assertEquals(3491, robot.getBatteryLevel());
		//from South
		robot.rotate(Turn.RIGHT);
		assertEquals(CardinalDirection.East, robot.getCurrentDirection()); 
		assertEquals(3488, robot.getBatteryLevel());
		
		//Testing rotate left:
		//from East
		robot.rotate(Turn.LEFT);
		assertEquals(CardinalDirection.South, robot.getCurrentDirection()); 
		assertEquals(3485, robot.getBatteryLevel());
		//from South
		robot.rotate(Turn.LEFT);
		assertEquals(CardinalDirection.West, robot.getCurrentDirection()); 
		assertEquals(3482, robot.getBatteryLevel());
		//from West
		robot.rotate(Turn.LEFT);
		assertEquals(CardinalDirection.North, robot.getCurrentDirection()); 
		assertEquals(3479, robot.getBatteryLevel());
		//from North
		robot.rotate(Turn.LEFT);
		assertEquals(CardinalDirection.East, robot.getCurrentDirection()); 
		assertEquals(3476, robot.getBatteryLevel());
		
		//Testing turn around:
		//from East
		robot.rotate(Turn.AROUND);
		assertEquals(CardinalDirection.West, robot.getCurrentDirection()); 
		assertEquals(3470, robot.getBatteryLevel());
		//from West
		robot.rotate(Turn.AROUND);
		assertEquals(CardinalDirection.East, robot.getCurrentDirection()); 
		assertEquals(3464, robot.getBatteryLevel());
		robot.rotate(Turn.LEFT); //battery: 3461
		assert(robot.getCurrentDirection()==CardinalDirection.South);
		//from South
		robot.rotate(Turn.AROUND);
		assertEquals(CardinalDirection.North, robot.getCurrentDirection()); 
		assertEquals(3455, robot.getBatteryLevel());	
		//from North
		robot.rotate(Turn.AROUND);
		assertEquals(CardinalDirection.South, robot.getCurrentDirection()); 
		assertEquals(3449, robot.getBatteryLevel());	
		
		//Testing battery issue cases:
		//CASE 1: no battery to start with for left rotate (<0):
		robot.setBatteryLevel(3);
		robot.rotate(Turn.LEFT); //battery should now be at zero
		assert(robot.getBatteryLevel()==0);
		//current direction: west
		assert(robot.getCurrentDirection()==CardinalDirection.West);
		robot.rotate(Turn.LEFT); //direction should be the same, has stopped should be true
		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());
		assertEquals(0, robot.getBatteryLevel());
		//CASE 2: some battery but not enough for left rotate(<3):
		robot.resetHasStopped(false);
		robot.setBatteryLevel(2);
		robot.rotate(Turn.LEFT);
		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());
		assertEquals(-1, robot.getBatteryLevel());
		//CASE 3: no battery to start with for right rotate (<0)
		robot.setBatteryLevel(3);
		robot.rotate(Turn.LEFT); //battery should now be at zero
		assert(robot.getBatteryLevel()==0);
		//current direction: north
		assert(robot.getCurrentDirection()==CardinalDirection.North);
		robot.rotate(Turn.RIGHT); //direction should be the same, has stopped should be true
		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());
		assertEquals(0, robot.getBatteryLevel());
		//CASE 4: some battery but not enough for right rotate(<3):
		robot.resetHasStopped(false);
		robot.setBatteryLevel(2);
		robot.rotate(Turn.RIGHT);
		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());
		assertEquals(-1, robot.getBatteryLevel());
		//CASE 5: no battery at all for rotate around
		robot.setBatteryLevel(3);
		robot.rotate(Turn.LEFT); //battery should now be at zero
		assert(robot.getBatteryLevel()==0);
		//current direction: east
		assert(robot.getCurrentDirection()==CardinalDirection.East);
		robot.rotate(Turn.AROUND); //direction should be the same, has stopped should be true
		assertEquals(CardinalDirection.East, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());
		assertEquals(0, robot.getBatteryLevel());
		//CASE 6: not enough battery for a 90 degree turn
		robot.resetHasStopped(false);
		robot.setBatteryLevel(2);
		robot.rotate(Turn.AROUND);
		assertEquals(CardinalDirection.East, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());	
		assertEquals(-1, robot.getBatteryLevel());
		//CASE 7: just enough battery for 90 degree turn, but not enough for 180 degree
		robot.resetHasStopped(false);
		robot.setBatteryLevel(3);
		robot.rotate(Turn.AROUND); //coded as double right
		assertEquals(CardinalDirection.North, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());	
		assertEquals(0, robot.getBatteryLevel());
		//CASE 8: enough battery for 90 degree, and some leftover battery for next 90 degree turn but not enough
		robot.resetHasStopped(false);
		robot.setBatteryLevel(4);
		robot.rotate(Turn.AROUND); //coded as double right
		assertEquals(CardinalDirection.West, robot.getCurrentDirection());
		assertTrue(robot.hasStopped());	
		assertEquals(-2, robot.getBatteryLevel());
	}
	
	@Test
	public void testMove() throws Exception {
		//IMPORTANT NOTE: north and south are backwards
		//test move from each cardinal direction
		//check that robot has moved by comparing current position before and after move
		//check battery has gone down 6	
		
		//start position: {0,1}, start direction: east
		assert(robot.getCurrentPosition()[0]==0);
		assert(robot.getCurrentPosition()[1]==1);
		assert(robot.getCurrentDirection()==CardinalDirection.East);
		
		//test move 1, but hits wall: East
		robot.move(1);
		assertTrue(robot.hasStopped());
		assertEquals(3494, robot.getBatteryLevel());
		assertEquals(0, robot.getOdometerReading());
		
		//test move 1, but hits wall: West
		robot.resetHasStopped(false);
		robot.rotate(Turn.AROUND);
		robot.move(1);
		assertTrue(robot.hasStopped());
		assertEquals(3482, robot.getBatteryLevel());
		assertEquals(0, robot.getOdometerReading());
		
		//test move 1, but hits wall: South
		robot.resetHasStopped(false);
		robot.rotate(Turn.RIGHT);
		robot.move(1);
		assertTrue(robot.hasStopped());
		assertEquals(3473, robot.getBatteryLevel());
		assertEquals(0, robot.getOdometerReading());
		
		//test move 1 with north
		robot.resetHasStopped(false);
		robot.rotate(Turn.AROUND); //cd=North
		robot.move(1);
		assert(!robot.hasStopped());
		int [] curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(0, curPos[1]);
		assertEquals(3461, robot.getBatteryLevel());
		assertEquals(1, robot.getOdometerReading());
		
		//test move 1, but hits wall: North
		robot.move(1);
		assertTrue(robot.hasStopped());
		assertEquals(3455, robot.getBatteryLevel());
		assertEquals(1, robot.getOdometerReading());
		
		//test move 1 with east
		robot.resetHasStopped(false);
		robot.rotate(Turn.LEFT); //cd=East
		robot.move(1);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(0, curPos[1]);
		assertEquals(3446, robot.getBatteryLevel());
		assertEquals(2, robot.getOdometerReading());
		
		//test move 1 with south
		robot.rotate(Turn.LEFT); //cd=South
		robot.move(1);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3437, robot.getBatteryLevel());
		assertEquals(3, robot.getOdometerReading());
		
		//test move 1 with west
		robot.move(1);
		assert(robot.getCurrentPosition()[0]==1);
		assert(robot.getCurrentPosition()[1]==2);
		robot.rotate(Turn.LEFT); //cd=West
		robot.move(1);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(2, curPos[1]);
		assertEquals(3422, robot.getBatteryLevel());
		assertEquals(5, robot.getOdometerReading());
		
		//test move >1 with east
		robot.rotate(Turn.RIGHT); //cd: South
		robot.move(1);
		robot.rotate(Turn.RIGHT); // cd: east
		assert(robot.getCurrentDirection()==CardinalDirection.East);
		assert(robot.getCurrentPosition()[0]==0);
		assert(robot.getCurrentPosition()[1]==3);
		robot.move(3);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(3392, robot.getBatteryLevel());
		assertEquals(9, robot.getOdometerReading());
		
		//test move >1 with west
		robot.rotate(Turn.AROUND); //cd: west
		robot.move(3);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(3368, robot.getBatteryLevel());
		assertEquals(12, robot.getOdometerReading());
		
		//test move >1 with north
		robot.rotate(Turn.AROUND); //cd:east
		robot.move(3);
		robot.rotate(Turn.RIGHT); //cd: north
		robot.move(3);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(0, curPos[1]);
		assertEquals(3323, robot.getBatteryLevel());
		assertEquals(18, robot.getOdometerReading());
		
		//test move >1 with south
		robot.rotate(Turn.AROUND); //cd:south
		robot.move(3);
		assert(!robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(3299, robot.getBatteryLevel());
		assertEquals(21, robot.getOdometerReading());
		
		//test move >1, but hits wall
		robot.rotate(Turn.LEFT); //cd:west
		robot.move(4);
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(3272, robot.getBatteryLevel());
		assertEquals(24, robot.getOdometerReading());

		//Testing battery issues:
		//CASE 1: no battery
		robot.resetHasStopped(false);
		robot.rotate(Turn.AROUND);
		robot.setBatteryLevel(6);
		robot.move(1);
		assert(robot.getOdometerReading()==25);
		assert(!robot.hasStopped());
		robot.move(1);
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(0, robot.getBatteryLevel());
		assertEquals(25, robot.getOdometerReading());
		//CASE 2: some battery, not enough to move forward 1
		robot.resetHasStopped(false);
		robot.setBatteryLevel(5);
		robot.move(1);
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(-1, robot.getBatteryLevel());
		assertEquals(25, robot.getOdometerReading());
		//CASE 3: enough to move forward 1, but not 2
		robot.resetHasStopped(false);
		robot.setBatteryLevel(7);
		robot.move(2);
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(2, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(-5, robot.getBatteryLevel());
		assertEquals(26, robot.getOdometerReading());	
	}
	
	@Test
	public void testJump() throws Exception {
		//IMPORTANT NOTE: north and south are backwards

		//test jump from each cardinal direction
		//check that robot has moved by comparing current position before and after move
		//check battery has gone down 40
		
		assert(robot.getCurrentPosition()[0]==0);
		assert(robot.getCurrentPosition()[1]==1);
		assert(robot.getCurrentDirection()==CardinalDirection.East);
		
		//Testing normal jump
		//CASE 1: facing South
		robot.rotate(Turn.LEFT); //cd: south
		assert(robot.getCurrentDirection()==CardinalDirection.South);
		robot.jump();
		int [] curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(2, curPos[1]);
		assertEquals(3457, robot.getBatteryLevel());
		assertEquals(1, robot.getOdometerReading());
		//CASE 2: facing North
		robot.rotate(Turn.AROUND); //cd: north
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3411, robot.getBatteryLevel());
		assertEquals(2, robot.getOdometerReading());
		//CASE 3: facing East
		robot.rotate(Turn.LEFT); //cd: east
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3368, robot.getBatteryLevel());
		assertEquals(3, robot.getOdometerReading());
		//CASE 4: facing West
		robot.rotate(Turn.AROUND); //cd: west
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3322, robot.getBatteryLevel());
		assertEquals(4, robot.getOdometerReading());
		
		//Testing jump with no wall
		//CASE 1: east
		playingState.setCurrentPosition(1, 1);
		robot.rotate(Turn.AROUND); //cd:east
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(2, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3276, robot.getBatteryLevel());
		assertEquals(5, robot.getOdometerReading());
		//CASE 2: west
		robot.rotate(Turn.AROUND); //cd:west
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3230, robot.getBatteryLevel());
		assertEquals(6, robot.getOdometerReading());
		//CASE 3: north
		robot.rotate(Turn.LEFT); //cd:north
		assert(robot.getCurrentDirection()==CardinalDirection.North);
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(0, curPos[1]);
		assertEquals(3187, robot.getBatteryLevel());
		assertEquals(7, robot.getOdometerReading());
		//CASE 4: south
		robot.rotate(Turn.AROUND); //cd:south
		robot.jump();
		curPos=robot.getCurrentPosition();
		assertEquals(1, curPos[0]);
		assertEquals(1, curPos[1]);
		assertEquals(3141, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
		
		//Testing jump over border
		assert(!robot.hasStopped());
		//CASE 1: north
		playingState.setCurrentPosition(0, 0);
		robot.rotate(Turn.AROUND); //cd:North
		robot.jump();
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(0, curPos[1]);
		assertEquals(3095, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
		//CASE 2: west
		robot.resetHasStopped(false);
		robot.rotate(Turn.RIGHT); //cd:west
		robot.jump();
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(0, curPos[0]);
		assertEquals(0, curPos[1]);
		assertEquals(3052, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
		//CASE 3: east
		playingState.setCurrentPosition(3, 3);
		robot.resetHasStopped(false);
		robot.rotate(Turn.AROUND); //cd:east
		robot.jump();
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(3006, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
		//CASE 4: south
		robot.resetHasStopped(false);
		robot.rotate(Turn.LEFT); //cd:south
		robot.jump();
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(2963, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
		
		//Testing battery issues
		robot.resetHasStopped(false);
		//CASE 1: robot has no battery left
		robot.setBatteryLevel(3);
		robot.rotate(Turn.LEFT);
		assert(robot.getCurrentDirection()==CardinalDirection.West);
		robot.jump();
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(0, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
		//CASE 2: robot has some battery
		robot.resetHasStopped(false);
		robot.setBatteryLevel(1);
		robot.jump();
		assertTrue(robot.hasStopped());
		curPos=robot.getCurrentPosition();
		assertEquals(3, curPos[0]);
		assertEquals(3, curPos[1]);
		assertEquals(-39, robot.getBatteryLevel());
		assertEquals(8, robot.getOdometerReading());
	}
	
	@Test
	public void testIsAtExit() {
		//false case
		assertFalse(robot.isAtExit());
		//true case
		playingState.setCurrentPosition(2, 0);
		assertTrue(robot.isAtExit());
	}
	
	@Test
	public void testIsInsideRoom() {
		//false case - perfect maze should have no rooms
		for (int i=0; i<maze.getWidth(); i++) {
			for (int j=0; j<maze.getHeight(); j++){
				playingState.setCurrentPosition(i, j);
				assertFalse(robot.isInsideRoom());
			}
		}
		
		//true case
		order.setSkillLevel(1);
		order.setPerfect(false);
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		ReliableRobot robot2 = new ReliableRobot();
		Control controller2 = new Control();
		StatePlaying playingState2 = new StatePlaying();
		playingState2.setMaze(maze);
		controller2.setState(playingState2);
		playingState2.start(controller2, null); //dry-run for testing
		robot2.setController(controller2);
	}
	
	/**
	 * Test distanceToObstacle method with all unreliable sensors, but failure and repair process has not been started
	 */
	@Test
	public void testDistanceToObstacleWithUnreliableSensorsNoFRP() {
		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);		
		
		playingState.setCurrentPosition(1, 2);
		
		assert(robot.getCurrentDirection()==CardinalDirection.East);
		//test East facing robot, forward 
		assertEquals(0, robot.distanceToObstacle(Direction.FORWARD));
		assertEquals(3499, robot.getBatteryLevel());
		//test East facing robot, right 
		assertEquals(2, robot.distanceToObstacle(Direction.RIGHT));
		assertEquals(3498, robot.getBatteryLevel());
		//test East facing robot, left 
		assertEquals(0, robot.distanceToObstacle(Direction.LEFT));
		assertEquals(3497, robot.getBatteryLevel());
		//test East facing robot, backward 
		assertEquals(1, robot.distanceToObstacle(Direction.BACKWARD));
		assertEquals(3496, robot.getBatteryLevel());

		
		robot.rotate(Turn.RIGHT);
		assert(robot.getCurrentDirection()==CardinalDirection.North);
		assert(robot.getBatteryLevel()==3493);
		//test North facing robot, forward 
		assertEquals(2, robot.distanceToObstacle(Direction.FORWARD));
		assertEquals(3492, robot.getBatteryLevel());
		//test North facing robot, right 
		assertEquals(1, robot.distanceToObstacle(Direction.RIGHT));
		assertEquals(3491, robot.getBatteryLevel());
		//test North facing robot, left 
		assertEquals(0, robot.distanceToObstacle(Direction.LEFT));
		assertEquals(3490, robot.getBatteryLevel());
		//test North facing robot, backward 
		assertEquals(0, robot.distanceToObstacle(Direction.BACKWARD));
		assertEquals(3489, robot.getBatteryLevel());

		
		robot.rotate(Turn.RIGHT);
		assert(robot.getCurrentDirection()==CardinalDirection.West);
		assert(robot.getBatteryLevel()==3486);
		//test West facing robot, forward 
		assertEquals(1, robot.distanceToObstacle(Direction.FORWARD));
		assertEquals(3485, robot.getBatteryLevel());
		//test West facing robot, right 
		assertEquals(0, robot.distanceToObstacle(Direction.RIGHT));
		assertEquals(3484, robot.getBatteryLevel());
		//test West facing robot, left 
		assertEquals(2, robot.distanceToObstacle(Direction.LEFT));
		assertEquals(3483, robot.getBatteryLevel());
		//test West facing robot, backward 
		assertEquals(0, robot.distanceToObstacle(Direction.BACKWARD));
		assertEquals(3482, robot.getBatteryLevel());


		robot.rotate(Turn.RIGHT);
		assert(robot.getCurrentDirection()==CardinalDirection.South);
		assert(robot.getBatteryLevel()==3479);
		//test South facing robot, forward 
		assertEquals(0, robot.distanceToObstacle(Direction.FORWARD));
		assertEquals(3478, robot.getBatteryLevel());
		//test South facing robot, right 
		assertEquals(0, robot.distanceToObstacle(Direction.RIGHT));
		assertEquals(3477, robot.getBatteryLevel());
		//test South facing robot, left 
		assertEquals(1, robot.distanceToObstacle(Direction.LEFT));
		assertEquals(3476, robot.getBatteryLevel());
		//test South facing robot, backward 
		assertEquals(2, robot.distanceToObstacle(Direction.BACKWARD));
		assertEquals(3475, robot.getBatteryLevel());	
	}
	
	/**
	 * Test distanceToObstacle method with an unreliable front sensor. Failure and Repair Process started
	 */
	@Test
	public void testDistanceToObstacleWithUnreliableSensorsFRPStarted() {
		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);
		
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);		
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		playingState.setCurrentPosition(1, 2);
		
		assert(robot.getCurrentDirection()==CardinalDirection.East);
		//test East facing robot, forward 
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.FORWARD));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(2, robot.distanceToObstacle(Direction.RIGHT));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.LEFT));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(1, robot.distanceToObstacle(Direction.BACKWARD));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}


		robot.rotate(Turn.RIGHT);
		assert(robot.getCurrentDirection()==CardinalDirection.North);
		//test North facing robot, forward 
		try {
			assertEquals(2, robot.distanceToObstacle(Direction.FORWARD));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(1, robot.distanceToObstacle(Direction.RIGHT));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.LEFT));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.BACKWARD));
			fail("sensor is not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}


		
		robot.rotate(Turn.RIGHT);
		assert(robot.getCurrentDirection()==CardinalDirection.West);
		//test West facing robot, forward 
		try {
			assertEquals(1, robot.distanceToObstacle(Direction.FORWARD));
			fail("sensor is not operational");

		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.RIGHT));
			fail("sensor is not operational");

		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(2, robot.distanceToObstacle(Direction.LEFT));
			fail("sensor is not operational");

		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.BACKWARD));
			fail("sensor is not operational");

		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}


		robot.rotate(Turn.RIGHT);
		assert(robot.getCurrentDirection()==CardinalDirection.South);
		//test South facing robot, forward 
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.FORWARD));
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		try {
			assertEquals(0, robot.distanceToObstacle(Direction.RIGHT));
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		
		try {
			assertEquals(1, robot.distanceToObstacle(Direction.LEFT));
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		try {
			assertEquals(2, robot.distanceToObstacle(Direction.BACKWARD));
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
			assertTrue(e.getMessage()=="sensor not operational");
		}
		
		//test can see through infinity
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD);
			fail("sensor not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		
		
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.LEFT);
			fail("sensor not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
			fail("sensor not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT);
			fail("sensor not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, robot.distanceToObstacle(Direction.FORWARD));
		assertEquals(1, robot.distanceToObstacle(Direction.LEFT));
		assertEquals(2, robot.distanceToObstacle(Direction.BACKWARD));
		assertEquals(0, robot.distanceToObstacle(Direction.RIGHT));

		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		robot.stopFailureAndRepairProcess(Direction.RIGHT);
		robot.stopFailureAndRepairProcess(Direction.LEFT);
		robot.stopFailureAndRepairProcess(Direction.BACKWARD);
	}
	
	
	@Test
	public void testCanSeeThroughTheExitIntoExternity() {
		//test that method returns true if robot sensor direction is in direction of exit
		//true case
		//false case
		//test at exits facing all directions
		
		UnreliableSensor forwardSensor=new UnreliableSensor();

		robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
    	
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		playingState.setCurrentPosition(2, 0); //set current position to exit 
		assert(maze.getExitPosition()[0]==2);
		assert(maze.getExitPosition()[1]==0);
		
		try {
			robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
			fail("sensor not operational");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		return;		

	}
	
	@Test
	public void testStartFailureAndRepairProcess() {
		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);
		
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);		
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		assertFalse(robot.forwardSensor.getOperational());
		assertFalse(robot.backwardSensor.getOperational());
		assertFalse(robot.leftSensor.getOperational());
		assertFalse(robot.rightSensor.getOperational());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		assertTrue(robot.forwardSensor.getOperational());
		assertTrue(robot.backwardSensor.getOperational());
		assertTrue(robot.leftSensor.getOperational());
		assertTrue(robot.rightSensor.getOperational());
	}
	
	@Test
	public void testStopFailureAndRepairProcess() {
		robot.addDistanceSensor(robot.forwardSensor, Direction.FORWARD);
    	robot.addDistanceSensor(robot.backwardSensor, Direction.BACKWARD);
		robot.addDistanceSensor(robot.leftSensor, Direction.LEFT);
		robot.addDistanceSensor(robot.rightSensor, Direction.RIGHT);	
		
		try {
			robot.stopFailureAndRepairProcess(Direction.FORWARD);
			fail("no failure and repair process has been started");
		} catch (Exception e) {
			// TODO: handle exception
			assertTrue(e instanceof UnsupportedOperationException);
		}
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);
		robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
		robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		
		assertFalse(robot.forwardSensor.getOperational());
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		//sensor should end in an operational state
		assertTrue(robot.forwardSensor.getOperational());
		
		robot.stopFailureAndRepairProcess(Direction.LEFT);
		//sensor should end in an operational state
		assertTrue(robot.leftSensor.getOperational());
		
		robot.stopFailureAndRepairProcess(Direction.RIGHT);
		//sensor should end in an operational state
		assertTrue(robot.rightSensor.getOperational());
		
		robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		//sensor should end in an operational state
		assertTrue(robot.backwardSensor.getOperational());
		
	}
	
	
}
