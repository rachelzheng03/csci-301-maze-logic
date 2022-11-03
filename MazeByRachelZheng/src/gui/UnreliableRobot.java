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
//		forwardSensor=new ReliableSensor();
//		backwardSensor=new ReliableSensor();
//		leftSensor=new ReliableSensor();
//		rightSensor=new ReliableSensor();
//		hasStopped=false;
//		controller=null;
//		odometer=0;
//		batteryLevel=3500;
		super();
		
		//change sensors to unreliable sensors if applicable:
		//forward sensor is unreliable
	    if (parameter.charAt(0)=='0') {
	    	forwardSensor = new UnreliableSensor();
	    }
	    
	    //left sensor is unreliable
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
