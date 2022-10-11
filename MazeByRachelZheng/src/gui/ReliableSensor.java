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
 * 2)Set sensor direction
 * 3)Set maze
 * 4)Calculate energy used to get distance
 * 5)Accounts for the failure and repair of sensor
 * 
 * Collaborators: RobotDriver, CardinalDirection, Maze
 * @author Rachel Zheng
 *
 */
public class ReliableSensor implements DistanceSensor {

	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// TODO Auto-generated method stub
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
