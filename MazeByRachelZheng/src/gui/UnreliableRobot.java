/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.assertAll;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Direction;

/**
 * Responsibilities:
 * 1)Rotate, move, jump
 * 2)Get and set battery level 
 * 3)Calculate distance traveled
 * 4)Get Location (exact position, in room, is exit)
 * 5)Account for failure and repairs
 * 
 * Collaborators: UnreliableSensor, Control, StatePlaying
 * @author Rachel Zheng
 *
 */
public class UnreliableRobot extends ReliableRobot{
	
	protected ReliableSensor forwardSensor;
	protected ReliableSensor backwardSensor;
	protected ReliableSensor leftSensor;
	protected ReliableSensor rightSensor;

	
	public UnreliableRobot() {
		// TODO Auto-generated constructor stub
		forwardSensor=null;
		backwardSensor=null;
		leftSensor=null;
		rightSensor=null;
	}
	
	/**
	 * Switch the given reliable sensor to an unreliable sensor
	 * @param sensor
	 */
	private void castReliableSensorToUnreliable(ReliableSensor sensor) {
		assert(sensor!=null);
		assert(sensor.mountedDirection!=null);
		switch (sensor.mountedDirection) {
		case FORWARD:
			this.forwardSensor=(UnreliableSensor)forwardSensor;
			break;
		case BACKWARD:
			this.backwardSensor=(UnreliableSensor)backwardSensor;
			break;
		case LEFT:
			this.leftSensor=(UnreliableSensor)leftSensor;
			break;
		case RIGHT:
			this.rightSensor=(UnreliableSensor)rightSensor;
			break;
		default:
			throw new IllegalArgumentException("Unexpected value");
		}
	}
	
	/**
	 * Switch to a direction that corresponds with a operational sensor. Don't do anything if the sensor in the given direction is operational.
	 * @param oldDirection the direction of the sensor you want to switch from
	 * @return direction of sensor to use
	 */
	private Direction switchToAnotherSensor(Direction oldDirection) {
		switch (oldDirection){
		case FORWARD: 
			//if given sensor is operational don't change anything
			if (forwardSensor.getOperational())
				return Direction.FORWARD;
			if(rightSensor.getOperational()) {
				//turn so that the right sensor is facing the direction the forward sensor was facing
				rotate(Turn.LEFT);
				return Direction.RIGHT;
			}
			if(leftSensor.getOperational()) {
				//turn so that the left sensor is facing the direction the forward sensor was facing
				rotate(Turn.RIGHT);
				return Direction.LEFT;
			}
			//last because turning around takes the most energy. turns so that the backward sensor is facing the direction the forward sensor was facing
			if(backwardSensor.getOperational()) {
				rotate(Turn.AROUND);
				return Direction.BACKWARD;
			}
			else {
				throw new Error("no sensors are operational");
			}
		case BACKWARD:
			//if given sensor is operational don't change anything
			if(backwardSensor.getOperational()) 
				return Direction.BACKWARD;
			if(rightSensor.getOperational()) {
				//turn so that the right sensor is facing the direction the backward sensor was facing
				rotate(Turn.RIGHT);
				return Direction.RIGHT;
			}
			if(leftSensor.getOperational()) {
				//turn so that the left sensor is facing the direction the backward sensor was facing
				rotate(Turn.LEFT);
				return Direction.LEFT;
			}
			//last because turning around takes the most energy. turns so that the forward sensor is facing the direction the backward sensor was facing
			if (forwardSensor.getOperational()) {
				rotate(Turn.AROUND);
				return Direction.FORWARD;
			}
			else {
				throw new Error("no sensors are operational");
			}
		case LEFT:
			//if given sensor is operational don't change anything
			if(leftSensor.getOperational()) 
				return Direction.LEFT;
			if(backwardSensor.getOperational()) {
				//turn so that the backward sensor is facing the direction the left sensor was facing
				rotate(Turn.RIGHT);
				return Direction.RIGHT;
			}
			if(forwardSensor.getOperational()) {
				//turn so that the forward sensor is facing the direction the left sensor was facing
				rotate(Turn.LEFT);
				return Direction.LEFT;
			}
			//last because turning around takes the most energy. turns so that the right sensor is facing the direction the left sensor was facing
			if (rightSensor.getOperational()) {
				rotate(Turn.AROUND);
				return Direction.FORWARD;
			}
			//only reaches this block if no sensors were operational. this should not be the case.
			else {
				throw new Error("no sensors are operational");
			}
		case RIGHT:
			//if given sensor is operational don't change anything
			if(rightSensor.getOperational()) 
				return Direction.RIGHT;
			if(forwardSensor.getOperational()) {
				//turn so that the forward sensor is facing the direction the right sensor was facing
				rotate(Turn.RIGHT);
				return Direction.RIGHT;
			}
			if(backwardSensor.getOperational()) {
				//turn so that the backward sensor is facing the direction the right sensor was facing
				rotate(Turn.LEFT);
				return Direction.LEFT;
			}
			//last because turning around takes the most energy. turns so that the left sensor is facing the direction the right sensor was facing
			if (leftSensor.getOperational()) {
				rotate(Turn.AROUND);
				return Direction.FORWARD;
			}
			else {
				throw new Error("no sensors are operational");
			}
		default:
			throw new IllegalArgumentException("Unexpected value: " + oldDirection);
		}		
	}
	
	/**
	 * Switch the cardinal direction of the robot from newCD to oldCD
	 * @param oldCD original cardinal direction of the robot before having to switch sensors
	 * @param newCD current cardinal direction of robot
	 */
	private void rotateBack(CardinalDirection oldCD, CardinalDirection newCD) {
		switch (oldCD){
		case North: 
			if (newCD==CardinalDirection.South) {
				rotate(Turn.AROUND);
				return;
			}
			if (newCD==CardinalDirection.East) {
				rotate(Turn.RIGHT);
				return;
			}
			if (newCD==CardinalDirection.West) {
				rotate(Turn.LEFT);
				return;
			}
		case East:
			if (newCD==CardinalDirection.West) {
				rotate(Turn.AROUND);
				return;
			}
			if (newCD==CardinalDirection.North) {
				rotate(Turn.LEFT);
				return;
			}
			if (newCD==CardinalDirection.South) {
				rotate(Turn.RIGHT);
				return;
			}
		case West:
			if (newCD==CardinalDirection.East) {
				rotate(Turn.AROUND);
				return;
			}
			if (newCD==CardinalDirection.North) {
				rotate(Turn.LEFT);
				return;
			}
			if (newCD==CardinalDirection.South) {
				rotate(Turn.RIGHT);
				return;
			}
		case South:
			if (newCD==CardinalDirection.North) {
				rotate(Turn.AROUND);
				return;
			}
			if (newCD==CardinalDirection.East) {
				rotate(Turn.LEFT);
				return;
			}
			if (newCD==CardinalDirection.West) {
				rotate(Turn.RIGHT);
				return;
			}
		default:
			throw new IllegalArgumentException("Unexpected value: " + oldCD);
		}
	}
	
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		CardinalDirection oldCD = getCurrentDirection(); //cardinal direction of robot when distanceToObstacle is called
		//switch to another sensor if sensor in the given direction in not operational
		direction=switchToAnotherSensor(direction); 
		//calculate distance to obstacle using new direction 
		int steps = super.distanceToObstacle(direction);
		CardinalDirection newCD = getCurrentDirection(); //cardinal direction of robot after rotation from switching sensors
		//rotate back to the original cardinal direction if robot has rotated
		if (!(oldCD==newCD))
			rotateBack(oldCD, newCD);
		return steps;
	}

	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		switch (direction) {
		case FORWARD: 
			forwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case BACKWARD:
			backwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case LEFT:
			leftSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case RIGHT:
			rightSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		switch (direction) {
		case FORWARD: 
			forwardSensor.stopFailureAndRepairProcess();
			break;
		case BACKWARD:
			backwardSensor.stopFailureAndRepairProcess();
			break;
		case LEFT:
			leftSensor.stopFailureAndRepairProcess();
			break;
		case RIGHT:
			rightSensor.stopFailureAndRepairProcess();
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
	}

}
