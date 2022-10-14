/**
 * 
 */
package gui;

import generation.CardinalDirection;

/**
 * Responsibilities:
 * 1)Rotate, move, jump
 * 2)Calculate energy used and energy leftover (battery)
 * 3)Calculate distance traveled
 * 4)Get Location (exact position, in room, is exit)
 * 5)Account for failure and repairs
 * 
 * Collaborators: ReliableSensor, Control, StatePlaying
 * @author Rachel Zheng
 *
 */
public class ReliableRobot implements Robot {
	
	private Control controller;
	private float batteryLevel;
	private final static float ENERGY_FOR_FULL_ROTATION=12;
	private final static float ENERGY_FOR_STEP_FORWARD=12;
	private int odomemeter; //distance traveled
	
	public void ReliableRobot() {
	}

	@Override
	public void setController(Control controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		// TODO Auto-generated method stub
		//initialize reliable sensor
		//set sensor's mounted direction

	}

	@Override
	public int[] getCurrentPosition() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBatteryLevel(float level) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getEnergyForFullRotation() {
		// TODO Auto-generated method stub
		//should be 12 for a full rotation
		return 0;
	}

	@Override
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		//should be 6
		return 0;
	}

	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotate(Turn turn) {
		// TODO Auto-generated method stub
		//check if energy is sufficient
		//turn in direction indicated by parameter
		//subtract 3 from batteryLevel

	}

	@Override
	public void move(int distance) {
		// TODO Auto-generated method stub
		//check if energy is sufficient and no wall
		//move "distance" steps in the direction of the robot
		//subtract 6 from batteryLevel

	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		//check that energy is sufficient and not facing exterior wall
		//jump over 1 in the direction the robot is facing
		//subtract 40 from batteryLevel
	}

	@Override
	public boolean isAtExit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInsideRoom() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasStopped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		//check direction robot is facing and location of robot
		//return infinity if facing exit
		return false;
	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
