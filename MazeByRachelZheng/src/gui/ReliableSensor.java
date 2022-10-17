/**
 * 
 */
package gui;

import static org.junit.Assert.assertTrue;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.FixedWidth;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.UpperCase;

import generation.CardinalDirection;
import generation.Floorplan;
import generation.Maze;
import generation.Wallboard;
import gui.Robot.Direction;

/**
 * Responsibilities: 
 * 1)Calculate distance to a wallboard based on direction of sensor. 
 * 2)Calculate energy used to get distance
 * 
 * Collaborators: CardinalDirection, Maze
 * @author Rachel Zheng
 *
 */
public class ReliableSensor implements DistanceSensor {
	
	//private fields
	private static final float ENERGY_CONSUMPTION_SENSING=1; //how much energy it takes to sense in one direction 
	protected Maze maze;
	private Direction mountedDirection; //direction of sensor relative to the robot
	private float battery; //
	
	//constructor
	public void ReliableSensor() {
	}
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// TODO Auto-generated method stub
		//if any parameters are null, throw IllegalArgument Exception
		if (currentPosition==null||currentDirection==null||powersupply==null)
			throw new IllegalArgumentException();
		
		//if "x" of current position is out of bounds, throw IllegalArgument Exception
		if (currentPosition[0] < 0 || currentPosition[0] >= maze.getWidth()) 
			throw new IllegalArgumentException();
	
		//if "y" of current position is out of bounds, throw IllegalArgument Exception
		if(currentPosition[1] < 0 || currentPosition[1] >= maze.getHeight())
			throw new IllegalArgumentException();
		
		float currentPower=powersupply[0];
		//if sensor not operational, throw SensorFailure
		
		//if power is less than 0, throw IllegalArgumentException
		if(currentPower<0)
			throw new IllegalArgumentException();
		
		//if power at 0 or less, throw PowerFailure
		if (currentPower==0)
			throw new Error("Power Failure: power supply is insufficient for the operation");
		//count how many steps it takes to get to a wall in the direction of the sensor relative to the robot
		Floorplan floorplan=maze.getFloorplan();
		int step=0;
		assert(mountedDirection instanceof Direction);
				
		//case1: current direction is North.
		//1.a mounted direction is forward: decrement height from current height until wall is hit or position is exit
		//1.b mounted direction is backward: increment height from current height until wall is hit or position is exit
		//1.c mD is left: decrement width from current width until wall is hit or position is exit
		//1.d mD is right: increment width from current width until wall is hit or position is exit
		if (currentDirection==CardinalDirection.North) {
			switch (mountedDirection) {
			case FORWARD: 
				step=stepsNorthUntilWall(currentPosition, floorplan);
				break;
			case BACKWARD:
				step=stepsSouthUntilWall(currentPosition, floorplan);
				break;
			case LEFT:
				step=stepsWestUntilWall(currentPosition, floorplan);
				break;
			case RIGHT:
				step=stepsEastUntilWall(currentPosition, floorplan);
				break;
			}
		}
		
		//case2: current direction is East
		//2.a mD is forward: go east relative to floorplan until wall is hit or position is exit
		//2.b mD is backward: go west relative to floorplan until wall is hit or position is exit
		//2.c mD is left: go north relative to floorplan until wall is hit or position is exit
		//2.d mD is right: go south relative to floorplan until wall is hit or position is exit	
		else if (currentDirection==CardinalDirection.East) {
			switch (mountedDirection) {
			case FORWARD: 
				step=stepsEastUntilWall(currentPosition, floorplan);
				break;
			case BACKWARD:
				step=stepsWestUntilWall(currentPosition, floorplan);
				break;
			case LEFT:
				step=stepsNorthUntilWall(currentPosition, floorplan);
				break;
			case RIGHT:
				step=stepsSouthUntilWall(currentPosition, floorplan);
				break;
			}
		}
		
		//case3: current direction is South
		//3.a mD is forward: go south relative to floorplan until wall is hit or position is exit
		//3.b mD is backward: go north relative to floorplan until wall is hit or position is exit
		//3.c mD is left: go east relative to floorplan until wall is hit or position is exit
		//3.d mD is right: go west relative to floorplan until wall is hit or position is exit	
		else if (currentDirection==CardinalDirection.South) {
			switch (mountedDirection) {
			case FORWARD: 
				step=stepsSouthUntilWall(currentPosition, floorplan);
				break;
			case BACKWARD:
				step=stepsNorthUntilWall(currentPosition, floorplan);
				break;
			case LEFT:
				step=stepsEastUntilWall(currentPosition, floorplan);
				break;
			case RIGHT:
				step=stepsWestUntilWall(currentPosition, floorplan);
				break;
			}
		}
		
		//case4: current direction is West
		//4.a mD is forward: go west relative to floorplan until wall is hit or position is exit
		//4.b mD is backward: go east relative to floorplan until wall is hit or position is exit
		//4.c mD is left: go south relative to floorplan until wall is hit or position is exit
		//4.d mD is right: go north relative to floorplan until wall is hit or position is exit
		else if (currentDirection==CardinalDirection.West) {
			switch (mountedDirection) {
			case FORWARD: 
				step=stepsWestUntilWall(currentPosition, floorplan);
				break;
			case BACKWARD:
				step=stepsEastUntilWall(currentPosition, floorplan);
				break;
			case LEFT:
				step=stepsSouthUntilWall(currentPosition, floorplan);
				break;
			case RIGHT:
				step=stepsNorthUntilWall(currentPosition, floorplan);
				break;
			}
		}
		
		//decrement power by 1
		powersupply[0]=currentPower-ENERGY_CONSUMPTION_SENSING;
		
		//return number of steps to nearest wall in direction of robot or Integer.Max_Value if robot is facing exit
		return step; 

	}
	
	/**
	 * Finds how many steps it takes a robot to get to next wall in the direction, North
	 * @param currentPosition: coordinates (width, height) of where the robot is at on the maze
	 * @param floorplan
	 * @return number of steps or Integer.MAX_VALUE if robot facing exit
	 */
	private int stepsNorthUntilWall(int[] currentPosition, Floorplan floorplan){
		int step=0;
		for (int h=currentPosition[1]; h>=0;h--) {
			if (floorplan.hasWall(currentPosition[0], h, CardinalDirection.North))
				break;
			//if robot facing exit return Integer.MAX_VALUE
			if (floorplan.isExitPosition(currentPosition[0], h)&&floorplan.isPartOfBorder(new Wallboard(currentPosition[0], h, CardinalDirection.North)))
				return Integer.MAX_VALUE;
			if (floorplan.hasNoWall(currentPosition[0], h, CardinalDirection.North))
				step++;
		}
		return step;
	}
	
	/**
	 * Finds how many steps it takes a robot to get to next wall in the direction, East
	 * @param currentPosition: coordinates (width, height) of where the robot is at on the maze
	 * @param floorplan
	 * @return number of steps or Integer.MAX_VALUE if robot facing exit
	 */
	private int stepsEastUntilWall(int[] currentPosition, Floorplan floorplan){
		int step=0;
		for (int w=currentPosition[0]; w<maze.getWidth();w++) {
			if (floorplan.hasWall(w, currentPosition[1], CardinalDirection.East))
				break;
			//if robot facing exit return Integer.MAX_VALUE
			if (floorplan.isExitPosition(w, currentPosition[1])&&floorplan.isPartOfBorder(new Wallboard(w, currentPosition[1], CardinalDirection.East)))
				return Integer.MAX_VALUE;
			if (floorplan.hasNoWall(w, currentPosition[1], CardinalDirection.East))
				step++;
		}
		return step;
	}
	
	/**
	 * Finds how many steps it takes a robot to get to next wall in the direction, South
	 * @param currentPosition: coordinates (width, height) of where the robot is at on the maze
	 * @param floorplan
	 * @return number of steps or Integer.MAX_VALUE if robot facing exit
	 */
	private int stepsSouthUntilWall(int[] currentPosition, Floorplan floorplan){
		int step=0;
		for (int h=currentPosition[1]; h<maze.getHeight();h++) {
			if (floorplan.hasWall(currentPosition[0], h, CardinalDirection.South))
				break;
			//if robot facing exit return Integer.MAX_VALUE
			if (floorplan.isExitPosition(currentPosition[0], h)&&floorplan.isPartOfBorder(new Wallboard(currentPosition[0], h, CardinalDirection.South)))
				return Integer.MAX_VALUE;
			if (floorplan.hasNoWall(currentPosition[0], h, CardinalDirection.South))
				step++;
		}
		return step;
	}
	
	/**
	 * Finds how many steps it takes a robot to get to next wall in the direction, West
	 * @param currentPosition: coordinates (width, height) of where the robot is at on the maze
	 * @param floorplan
	 * @return number of steps or Integer.MAX_VALUE if robot facing exit
	 */
	private int stepsWestUntilWall(int[] currentPosition, Floorplan floorplan){
		int step=0;
		for (int w=currentPosition[0]; w>=0;w--) {
			if (floorplan.hasWall(w, currentPosition[1], CardinalDirection.West))
				break;
			//if robot facing exit return Integer.MAX_VALUE
			if (floorplan.isExitPosition(w, currentPosition[1])&&floorplan.isPartOfBorder(new Wallboard(w, currentPosition[1], CardinalDirection.West)))
				return Integer.MAX_VALUE;
			if (floorplan.hasNoWall(w, currentPosition[1], CardinalDirection.West))
				step++;
		}
		return step;
	}
	
	
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		if (maze==null||maze.getFloorplan()==null)
			throw new IllegalArgumentException();
		this.maze=maze;
	}

	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// TODO Auto-generated method stub
		if (mountedDirection==null)
			throw new IllegalArgumentException();
		this.mountedDirection=mountedDirection;

	}

	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		return ENERGY_CONSUMPTION_SENSING;
	}

	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("method not supported for reliable sensor");

	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("method not supported for reliable sensor");

	}

}
