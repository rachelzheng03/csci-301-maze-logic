/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.assertAll;

import generation.CardinalDirection;
import gui.UnreliableSensor;
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
	
	public UnreliableRobot(String parameter) {
		// TODO Auto-generated constructor stub
		forwardSensor=new ReliableSensor();
		backwardSensor=new ReliableSensor();
		leftSensor=new ReliableSensor();
		rightSensor=new ReliableSensor();
		hasStopped=false;
		controller=null;
		odometer=0;
		batteryLevel=3500;
		
	    if (parameter.charAt(0)=='0') {
	    	forwardSensor = new UnreliableSensor();
	    }
	    if (parameter.charAt(1)=='0') {
	    	leftSensor = new UnreliableSensor();
	    	
	    }
	    //right sensor is unreliable
	    if (parameter.charAt(2)=='0') {
	    	rightSensor = new UnreliableSensor();	    
	    }
	    //back sensor is unreliable
	    if (parameter.charAt(3)=='0') {
	    	backwardSensor = new UnreliableSensor();
	    }
	}

	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		switch (direction) {
		case FORWARD: 
			System.out.println("starting forward FRP");
			forwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case BACKWARD:
			System.out.println("starting backward FRP");
			backwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case LEFT:
			System.out.println("starting left FRP");
			leftSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case RIGHT:
			System.out.println("starting right FRP");
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
			System.out.println("front stop");
			forwardSensor.stopFailureAndRepairProcess();
			break;
		case BACKWARD:
			System.out.println("back stop");

			backwardSensor.stopFailureAndRepairProcess();
			break;
		case LEFT:
			System.out.println("left stop");

			leftSensor.stopFailureAndRepairProcess();
			break;
		case RIGHT:
			System.out.println("right stop");

			rightSensor.stopFailureAndRepairProcess();
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
	}

}
