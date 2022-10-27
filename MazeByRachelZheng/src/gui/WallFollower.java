/**
 * 
 */
package gui;

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
		startBattery=r.getBatteryLevel();
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
		while(!robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");	
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
		if (robot.isAtExit())
			return false;
		if (robot.distanceToObstacle(Direction.LEFT)>0) {
			robot.rotate(Turn.LEFT);
			robot.move(1);
			return true;
		}
		if (robot.distanceToObstacle(Direction.FORWARD)>0) {
			robot.move(1);
			return true;
		}
		else {
			robot.rotate(Turn.RIGHT);
		}
		return false;
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
