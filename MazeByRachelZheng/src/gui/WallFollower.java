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
		boolean switched=false; //keeps track if the sensor has been switched/wallfollower has found a sensor that is operational; 
		CardinalDirection oldCD = robot.getCurrentDirection();
		while(!switched) {
			try {
				canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD);
				switched=true;
				break;
			}catch (UnsupportedOperationException e) {
				// TODO: handle exception
				if(e.getMessage()!=("sensor not operational")) 
					throw new Exception();
				e.printStackTrace();
			}
			//find another sensor that is operational to determine if robot is facing the exit
			//try switching to right sensor
			try {
				robot.rotate(Turn.LEFT);
				canSeeExit = robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT);
				switched=true;
				break;
			} catch (UnsupportedOperationException e1) {
				if(e1.getMessage()!=("sensor not operational"))
					throw new Exception();
				e1.printStackTrace();
			}
			//try switching to right sensor
			try {
				robot.rotate(Turn.LEFT);
				canSeeExit=robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD);
				switched=true;
				break;
			} catch (UnsupportedOperationException e2) {
				// TODO: handle exception
				if(e2.getMessage()!=("sensor not operational"))
					throw new Exception();
				e2.printStackTrace();
			}

			//try switching to back sensor
			try {
				robot.rotate(Turn.LEFT);
				canSeeExit=robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD);
				switched=true;
				break;
			} catch (UnsupportedOperationException e3) {
				// TODO: handle exception
				if(e3.getMessage()!=("sensor not operational"))
					throw new Exception();
				e3.printStackTrace();
			}

			//sleep a second before trying forward sensor again
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
			int[] position = robot.getCurrentPosition(); //current position of robot - should be the exit
			//exit is on south side
			if (!maze.isValidPosition(position[0], position[1]+1)&&!maze.hasWall(position[0], position[1], CardinalDirection.South)){
				System.out.println("Exit on south side");
				turnToSouth(robot.getCurrentDirection());
			}
			//exit is on east side
			else if (!maze.isValidPosition(position[0]+1, position[1])&&!maze.hasWall(position[0], position[1], CardinalDirection.East)){
				System.out.println("Exit on east side");
				turnToEast(robot.getCurrentDirection());
			}
			//exit is on west side

			else if (!maze.isValidPosition(position[0]-1, position[1])&&!maze.hasWall(position[0], position[1], CardinalDirection.West)){
				System.out.println("Exit on west side");
				turnToWest(robot.getCurrentDirection());
			}
			//exit is on north side
			else if (!maze.isValidPosition(position[0], position[1]-1)&&!maze.hasWall(position[0], position[1], CardinalDirection.North)){
				System.out.println("Exit on north side");
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
		
		//if robot is at exit, stop
		if (robot.isAtExit())
			return false;
		
		//keeps track of cardinal direction of robot before switching sensors
		CardinalDirection oldCD = robot.getCurrentDirection();
		
		//sets to an impossible step value so that one can tell if steps has been changed by calling distance to obstacle
		int steps=-1;
		while(steps==-1) {
			try {
				steps = robot.distanceToObstacle(Direction.LEFT);
				break;
				//find another sensor that is operational to find the distance to obstacle in the original direction
			}catch (UnsupportedOperationException e) {
				// TODO: handle exception
				if(e.getMessage()!=("sensor not operational")) 
					throw new Exception();
				e.printStackTrace();
			}
			
			//try switching to forward sensor
			try {
				robot.rotate(Turn.LEFT);
				steps = robot.distanceToObstacle(Direction.FORWARD);
				break;
			} catch (UnsupportedOperationException e1) {
				if(e1.getMessage()!=("sensor not operational"))
					throw new Exception();
				e1.printStackTrace();
			}
			
			//try switching to right sensor
			try {
				robot.rotate(Turn.LEFT);
				steps=robot.distanceToObstacle(Direction.RIGHT);
				break;
			} catch (UnsupportedOperationException e2) {
					// TODO: handle exception
				if(e2.getMessage()!=("sensor not operational"))
					throw new Exception();
				e2.printStackTrace();
			}
			
			//try switching to back sensor
			try {
				robot.rotate(Turn.LEFT);
				steps=robot.distanceToObstacle(Direction.BACKWARD);
				break;
			} catch (UnsupportedOperationException e3) {
				// TODO: handle exception
				if(e3.getMessage()!=("sensor not operational"))
					throw new Exception();
				e3.printStackTrace();
			}
			
			//sleep a second before trying forward sensor again
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		
		//current direction of robot
		CardinalDirection newCD = robot.getCurrentDirection();
		//turn back to the cardinal direction before sensors were switched
		rotateBack(oldCD, newCD);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		
		//If there is no left wall:
	 	//rotate left and move forward 1
	 	//stop
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
		steps=-1;
		while(steps==-1) {
			try {
				steps = robot.distanceToObstacle(Direction.FORWARD);
				break;
				//find another sensor that is operational to find the distance to obstacle in the original direction
			}catch (UnsupportedOperationException e) {
				// TODO: handle exception
				if(e.getMessage()!=("sensor not operational"))
					throw new Exception();
				e.printStackTrace();
			}
			
			//try right sensor
			try {
				robot.rotate(Turn.LEFT);
				steps = robot.distanceToObstacle(Direction.RIGHT);
				break;
			} catch (UnsupportedOperationException e1) {
				if(e1.getMessage()!=("sensor not operational"))
					throw new Exception();
				e1.printStackTrace();
			}
			
			//try switching to back sensor
			try {
				robot.rotate(Turn.LEFT);
				steps=robot.distanceToObstacle(Direction.BACKWARD);
				break;
			} catch (UnsupportedOperationException e2) {
					// TODO: handle exception
				if(e2.getMessage()!=("sensor not operational"))
					throw new Exception();
				e2.printStackTrace();
			}
			
			//try switching to left sensor
			try {
				robot.rotate(Turn.LEFT);
				steps=robot.distanceToObstacle(Direction.LEFT);
				break;
			} catch (UnsupportedOperationException e3) {
				// TODO: handle exception
				if(e3.getMessage()!=("sensor not operational"))
					throw new Exception();
				e3.printStackTrace();
			}
			
			//sleep a second before trying forward sensor again
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		newCD=robot.getCurrentDirection();
		
		//rotate back to the original direction before switching sensors
		rotateBack(oldCD, newCD);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		
		//If there is no front wall:
	 	//move forward 1
	 	//stop
		if (steps>0) {
			robot.move(1);
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");
			return true;
		}
		//else: turn right
		else {
			robot.rotate(Turn.RIGHT);
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");
		}
		return false;
	
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
