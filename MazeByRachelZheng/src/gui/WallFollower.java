/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * Responsibilities: getting out of the maze using the wall-following algorithm

 * Collaborators: RobotDriver (unreliable or reliable), Maze
 * 
 * @author Rachel Zheng
 *
 */
public class WallFollower implements RobotDriver {

	private Robot robot;
	private Maze maze;
	private float startBattery;
	private ArrayList<Integer> reliable;
	
	public WallFollower() {
		// TODO Auto-generated constructor stub
		robot=null;
		maze=null;
	}
	
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		this.robot=r;
		startBattery=robot.getBatteryLevel();
	//	assert(startBattery==3500);
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze=maze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		while (!robot.isAtExit()) {
			drive1Step2Exit();
		}
		turn2FaceExitAtExit();
		return true;
	}
	
	/**
	 * The method turns the robot to face the exit position when the robot is at the exit
	 * @throws Exception if robot has stopped from crashing or insufficient battery
	 */
	private void turn2FaceExitAtExit() throws Exception {
		assert(robot.isAtExit()); //only call method when robot is at the exit
		boolean canSeeExit=false;
		CardinalDirection oldCD = robot.getCurrentDirection();
		try {
			canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
			//find another sensor that is operational to find the distance to obstacle in the original direction
		}catch (Exception e) {
			// TODO: handle exception
			if(e instanceof UnsupportedOperationException && e.getMessage()==("sensor not operational")) {
				int whichSensor=findOperational();
				if (whichSensor==0) {
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
				}
				if (whichSensor==1) {
					robot.rotate(Turn.RIGHT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.LEFT);
				}
				if (whichSensor==2) {
					robot.rotate(Turn.LEFT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT);
				}
				if (whichSensor==3) {
					robot.rotate(Turn.RIGHT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD);
				}
			}
			else {
				e.printStackTrace();
			}
		}
		

	
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		CardinalDirection newCD = robot.getCurrentDirection();
		rotateBack(oldCD, newCD);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		if(!canSeeExit) {
//			if(robot.hasStopped())
//				throw new Exception("Robot has stopped");	
			int[] position = robot.getCurrentPosition(); //current position of robot - should be the exit
			//robot.rotate(Turn.LEFT);
			//exit is on south side
			if (!maze.isValidPosition(position[0], position[1]+1))	{
				turnToSouth(robot.getCurrentDirection());
			}
			//exit is on east side
			else if (!maze.isValidPosition(position[0]+1, position[1])){
				turnToEast(robot.getCurrentDirection());
			}
			//exit is on west side
			else if (!maze.isValidPosition(position[0]-1, position[1])){
				turnToWest(robot.getCurrentDirection());
			}
			//exit is on north side
			else if (!maze.isValidPosition(position[0], position[1]-1)){
				turnToNorth(robot.getCurrentDirection());
			}
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");	
		}
	}
	
	@Override
	public boolean drive1Step2Exit() throws Exception {
		
		// TODO Auto-generated method stub
		//Algorithm for a Perfect Maze:
		 	//If robot is at exit:
			 	//stop
			//If there is no left wall:
			 	//rotate left and move forward 1
			 	//stop
			//If there is no front wall:
			 	//move forward 1
			 	//stop
			//Else:
		 		//turn right 
		int steps=0;
		if (robot.isAtExit())
			return false;
		CardinalDirection oldCD = robot.getCurrentDirection();
		
		try {
			steps = robot.distanceToObstacle(Direction.LEFT);
		//find another sensor that is operational to find the distance to obstacle in the original direction
		}catch (Exception e) {
			// TODO: handle exception
			if(e instanceof UnsupportedOperationException && e.getMessage()==("sensor not operational")) {				
				int whichSensor=findOperational();
				if (whichSensor==0) {
					robot.rotate(Turn.LEFT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.FORWARD);
				}
				if (whichSensor==1) {
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.LEFT);
				}
				if (whichSensor==2) {
					robot.rotate(Turn.AROUND);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.RIGHT);
				}
				if (whichSensor==3) {
					robot.rotate(Turn.RIGHT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.BACKWARD);
				}
			}
		}
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		CardinalDirection newCD = robot.getCurrentDirection();
		rotateBack(oldCD, newCD);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		if (steps>0) {
		robot.rotate(Turn.LEFT);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		robot.move(1);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		return true;
		}
		oldCD=robot.getCurrentDirection();
		try {
			steps = robot.distanceToObstacle(Direction.FORWARD);
		//find another sensor that is operational to find the distance to obstacle in the original direction
		}catch (Exception e) {
			// TODO: handle exception
			if(e instanceof UnsupportedOperationException && e.getMessage()==("sensor not operational")) {
				int whichSensor=findOperational();
				if (whichSensor==0) {
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.FORWARD);
				}
				if (whichSensor==1) {
					robot.rotate(Turn.RIGHT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.LEFT);
				}
				if (whichSensor==2) {
					robot.rotate(Turn.LEFT);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.RIGHT);
				}
				if (whichSensor==3) {
					robot.rotate(Turn.AROUND);
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
					steps = robot.distanceToObstacle(Direction.BACKWARD);
				}
			}
			}
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		newCD=robot.getCurrentDirection();
		rotateBack(oldCD, newCD);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		if (steps>0) {
			robot.move(1);
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");
			return true;
		}
		else {
			robot.rotate(Turn.RIGHT);
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");
		}
		return false;
	
	}
	
	private int findOperational() {
		// TODO Auto-generated method stub
		UnreliableRobot testrobot = (UnreliableRobot)robot;
		if (testrobot.forwardSensor.getOperational())
			return 0;
		if (testrobot.leftSensor.getOperational())
			return 1;
		if (testrobot.rightSensor.getOperational())
			return 2;
		if (testrobot.backwardSensor.getOperational())
			return 3;
		return -1;
		
	}

	private int findReliable() {
		// TODO Auto-generated method stub
		UnreliableRobot robot2 = (UnreliableRobot) this.robot;
		String parameter = robot2.controller.sensorParameter;
		reliable=new ArrayList<>();
		if (parameter.charAt(0)=='1') {
			reliable.add(0);
		}
	    if (parameter.charAt(1)=='1') {
			reliable.add(1);
	    	//add left unreliable sensor to unreliable robot	
	    }
	    //right sensor is unreliable
	    if (parameter.charAt(2)=='1') {
			reliable.add(2);
	    }
	    //back sensor is unreliable
	    if (parameter.charAt(3)=='1') {
			reliable.add(3);
	    }
	    if (reliable.size()>=1) {
	    	return reliable.get(0);
	    }
	    return -1;
	}

	/**
	 * rotates from newCd to oldCd
	 * @param oldCD desired cardinal direction of robot
	 * @param newCD current cardinal direction of robot
	 */
	private void rotateBack(CardinalDirection oldCD, CardinalDirection newCD) {
		// TODO Auto-generated method stub
		switch (oldCD) {
		case North: 
			turnToNorth(newCD);
			break;
		case East:
			turnToEast(newCD);
			break;
		case West:
			turnToWest(newCD);
			break;
		case South:
			turnToSouth(newCD);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + oldCD);
		}
	}

	/**
	 * rotates robot to the north using the least amount of 90 degree rotations
	 * @param cd: current direction of robot
	 */
	private void turnToNorth(CardinalDirection cd) {
		switch (cd) {
		case North: //no need to rotate
			return;
		case South:
			robot.rotate(Turn.AROUND);
			break;
		case East:
			robot.rotate(Turn.RIGHT);
			break;
		case West:
			robot.rotate(Turn.LEFT);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + cd);
		}
		
	}
	
	/**
	 * rotates robot to the south using the least amount of 90 degree rotations
	 * @param cd: current direction of robot
	 */
	private void turnToSouth(CardinalDirection cd) {
		switch (cd) {
		case North: 
			robot.rotate(Turn.AROUND);
			break;
		case South: //no need to rotate
			return;
		case East:
			robot.rotate(Turn.LEFT);
			break;
		case West:
			robot.rotate(Turn.RIGHT);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + cd);
		}
	}
	
	/**
	 * rotates robot to the east using the least amount of 90 degree rotations
	 * @param cd: current direction of robot
	 */
	private void turnToEast(CardinalDirection cd) {
		switch (cd) {
		case North: 
			robot.rotate(Turn.LEFT);
			break;
		case South:
			robot.rotate(Turn.RIGHT);
			break;
		case East: //no need to rotate
			return;
		case West:
			robot.rotate(Turn.AROUND);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + cd);
		}
	}
	
	/**
	 * rotates robot to the west using the least amount of 90 degree rotations
	 * @param cd: current direction of robot
	 */
	private void turnToWest(CardinalDirection cd) {
		switch (cd) {
		case North: 
			robot.rotate(Turn.RIGHT);
			break;
		case South:
			robot.rotate(Turn.LEFT);
			break;
		case East:
			robot.rotate(Turn.AROUND);
			break;
		case West:
			return; //no need to rotate
		default:
			throw new IllegalArgumentException("Unexpected value: " + cd);
		}
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		float endBattery=robot.getBatteryLevel();
		return startBattery-endBattery;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return robot.getOdometerReading();
	}

}
