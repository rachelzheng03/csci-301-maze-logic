/**
 * 
 */
package gui;

import generation.CardinalDirection;
import generation.Maze;
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
	private Maze maze;
	private Direction mountedDirection; //direction of sensor relative to the robot
	private float battery; //
	
	private void ReliableSensor() {
	}
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// TODO Auto-generated method stub
		//if sensor not operational, throw SensorFailure
		//if power at 0 or less, throw PowerFailure
		
		//count how many steps it takes to get to a wall in the direction of the sensor relative to the robot
		//case1: current direction is North.
			//1.a mounted direction is forward: decrement height from current height until wall is hit or position is exit
			//1.b mounted direction is backward: increment height from current height until wall is hit or position is exit
			//1.c mD is left: decrement width from current width until wall is hit or position is exit
			//1.d mD is right: increment width from current width until wall is hit or position is exit
		//case2: current direction is East
			//2.a mD is forward: go east relative to floorplan until wall is hit or position is exit
			//2.b mD is backward: go west relative to floorplan until wall is hit or position is exit
			//2.c mD is left: go north relative to floorplan until wall is hit or position is exit
			//2.d mD is right: go south relative to floorplan until wall is hit or position is exit	
		//case3: current direction is South
			//3.a mD is forward: go south relative to floorplan until wall is hit or position is exit
			//3.b mD is backward: go north relative to floorplan until wall is hit or position is exit
			//3.c mD is left: go east relative to floorplan until wall is hit or position is exit
			//3.d mD is right: go west relative to floorplan until wall is hit or position is exit	
		//case4: current direction is West
			//4.a mD is forward: go west relative to floorplan until wall is hit or position is exit
			//4.b mD is backward: go east relative to floorplan until wall is hit or position is exit
			//4.c mD is left: go south relative to floorplan until wall is hit or position is exit
			//4.d mD is right: go north relative to floorplan until wall is hit or position is exit
		
		//decrement power by 1
		
		//return number of steps it took to reach a wall or Integer.Max_Value if exit
		return 0;
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
