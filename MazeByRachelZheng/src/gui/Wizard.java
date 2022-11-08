/**
 * 
 */
package gui;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * Responsibilities: getting out of the maze as quickly as possible
 *  
 * Collaborators: ReliableRobot, Maze
 * @author Rachel Zheng
 *
 */
public class Wizard implements RobotDriver {
	
	protected Robot robot;
	protected Maze maze;
	protected float startBattery;
	

	//constructor
	public Wizard() {
		robot=null;
	}

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		robot=r;
		startBattery=robot.getBatteryLevel();
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze=maze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		//at each position, get neighbor closer to exit
		//move to said neighbor, rotate if needed
//		if robot moves into wall, throw exception
//		if robot does not have sufficient energy, throw exception
//		deduct energy costs
//		repeat until robot is facing the exit
		while (!robot.isAtExit()) {
			drive1Step2Exit();
		}
		drive1Step2Exit();
		return true;
	}

	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		if (robot.isAtExit()) {
			while(!robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
				if(robot.hasStopped())
					throw new Exception("Robot has stopped");	
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
			return false;
		}
		//get neighbor closer to exit
		int[] position=robot.getCurrentPosition();
		int[] destination = maze.getNeighborCloserToExit(position[0], position[1]);
		assert(destination!=null);
		//rotate to face neighbor
		if (position[0]==destination[0]) {
			if (position[1]>destination[1]) {
				while(robot.getCurrentDirection()!=CardinalDirection.North) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToNorth(robot.getCurrentDirection());
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
			else {
				while(robot.getCurrentDirection()!=CardinalDirection.South) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToSouth(robot.getCurrentDirection());
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
		}
		else if (position[1]==destination[1]) {
			if (position[0]>destination[0]) {
				while(robot.getCurrentDirection()!=CardinalDirection.West) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToWest(robot.getCurrentDirection());
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
			else {
				while(robot.getCurrentDirection()!=CardinalDirection.East) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToEast(robot.getCurrentDirection());
					if(robot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
		}
		robot.move(1);
		if(robot.hasStopped())
			throw new Exception("Robot has stopped");
		return robot.getCurrentPosition()==destination;
	}
	
	/**
	 * rotates robot to the north using the least amount of 90 degree rotations
	 * @param cd: current direction of robot
	 */
	protected void turnToNorth(CardinalDirection cd) {
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
	protected void turnToSouth(CardinalDirection cd) {
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
	protected void turnToEast(CardinalDirection cd) {
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
	protected void turnToWest(CardinalDirection cd) {
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
		//look to batterylevel of robot before and after
		float endBattery=robot.getBatteryLevel();
		return startBattery-endBattery;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		//look at odometer of robot object
		return robot.getOdometerReading();
	}

}
