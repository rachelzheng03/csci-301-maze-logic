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
	
	private Robot reliableRobot;
	private Maze maze;
	private float startBattery;
	

	//constructor
	public Wizard() {
		reliableRobot=null;
	}

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		reliableRobot=r;
		startBattery=reliableRobot.getBatteryLevel();
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
		while (!reliableRobot.isAtExit()) {
			drive1Step2Exit();
		}
		drive1Step2Exit();
		return true;
	}

	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		if (reliableRobot.isAtExit()) {
			while(!reliableRobot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
				if(reliableRobot.hasStopped())
					throw new Exception("Robot has stopped");	
				int[] position = reliableRobot.getCurrentPosition(); //current position of robot - should be the exit
				//exit is on south side
				if (!maze.isValidPosition(position[0], position[1]+1)&&!maze.hasWall(position[0], position[1], CardinalDirection.South)){
					System.out.println("Exit on south side");
					turnToSouth(reliableRobot.getCurrentDirection());
				}
				//exit is on east side
				else if (!maze.isValidPosition(position[0]+1, position[1])&&!maze.hasWall(position[0], position[1], CardinalDirection.East)){
					System.out.println("Exit on east side");
					turnToEast(reliableRobot.getCurrentDirection());
				}
				//exit is on west side

				else if (!maze.isValidPosition(position[0]-1, position[1])&&!maze.hasWall(position[0], position[1], CardinalDirection.West)){
					System.out.println("Exit on west side");
					turnToWest(reliableRobot.getCurrentDirection());
				}
				//exit is on north side
				else if (!maze.isValidPosition(position[0], position[1]-1)&&!maze.hasWall(position[0], position[1], CardinalDirection.North)){
					System.out.println("Exit on north side");
					turnToNorth(reliableRobot.getCurrentDirection());
				}
				if(reliableRobot.hasStopped())
					throw new Exception("Robot has stopped");	
			}
			return false;
		}
		//get neighbor closer to exit
		int[] position=reliableRobot.getCurrentPosition();
		int[] destination = maze.getNeighborCloserToExit(position[0], position[1]);
		assert(destination!=null);
		//rotate to face neighbor
		if (position[0]==destination[0]) {
			if (position[1]>destination[1]) {
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.North) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToNorth(reliableRobot.getCurrentDirection());
					if(reliableRobot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
			else {
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.South) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToSouth(reliableRobot.getCurrentDirection());
					if(reliableRobot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
		}
		else if (position[1]==destination[1]) {
			if (position[0]>destination[0]) {
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.West) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToWest(reliableRobot.getCurrentDirection());
					if(reliableRobot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
			else {
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.East) {
					//reliableRobot.rotate(Turn.LEFT);
					turnToEast(reliableRobot.getCurrentDirection());
					if(reliableRobot.hasStopped())
						throw new Exception("Robot has stopped");
				}
			}
		}
		reliableRobot.move(1);
		if(reliableRobot.hasStopped())
			throw new Exception("Robot has stopped");
		return reliableRobot.getCurrentPosition()==destination;
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
			reliableRobot.rotate(Turn.AROUND);
			break;
		case East:
			reliableRobot.rotate(Turn.RIGHT);
			break;
		case West:
			reliableRobot.rotate(Turn.LEFT);
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
			reliableRobot.rotate(Turn.AROUND);
			break;
		case South: //no need to rotate
			return;
		case East:
			reliableRobot.rotate(Turn.LEFT);
			break;
		case West:
			reliableRobot.rotate(Turn.RIGHT);
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
			reliableRobot.rotate(Turn.LEFT);
			break;
		case South:
			reliableRobot.rotate(Turn.RIGHT);
			break;
		case East: //no need to rotate
			return;
		case West:
			reliableRobot.rotate(Turn.AROUND);
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
			reliableRobot.rotate(Turn.RIGHT);
			break;
		case South:
			reliableRobot.rotate(Turn.LEFT);
			break;
		case East:
			reliableRobot.rotate(Turn.AROUND);
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
		float endBattery=reliableRobot.getBatteryLevel();
		return startBattery-endBattery;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		//look at odometer of robot object
		return reliableRobot.getOdometerReading();
	}

}
