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

	private DistanceSensor findReliable() {
		if (controller.sensorParameter.charAt(0)=='1') {
			System.out.println("what sensor switch");
			return forwardSensor;
		}
		if (leftSensor instanceof ReliableSensor) {
			System.out.println("left sensor switch");
			rotate(Turn.RIGHT);
			return leftSensor;
		}
		if (rightSensor instanceof ReliableSensor) {
			System.out.println("right sensor switch");
			rotate(Turn.LEFT);
			return rightSensor;
		}
		if (backwardSensor instanceof ReliableSensor) {
			System.out.println("back sensor switch");
			rotate(Turn.AROUND);
			return backwardSensor;
		}
		return null;
	}
	
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		float[] powersupply = {batteryLevel};
		switch (direction)	{	
		case FORWARD: 
			try {
				int steps = forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational" && forwardSensor!=null) {
					throw new UnsupportedOperationException("sensor not operational");
				}
				else {
					e.printStackTrace();
				}
			}
			//break;
		case BACKWARD:
			try {
				int steps = backwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational" && backwardSensor!=null)
					throw new UnsupportedOperationException("sensor not operational");
				e.printStackTrace();
			}
			break;
		case LEFT:
			try {
				int steps = leftSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational" && leftSensor!=null)
					throw new UnsupportedOperationException("sensor not operational");
				e.printStackTrace();
			}
			break;
		case RIGHT:
			try {
				int steps = rightSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;			
				} catch (Error|Exception e) {
				// TODO Auto-generated catch block
					if(e.getMessage()=="Sensor Failure: sensor not operational" && rightSensor!=null)
						throw new UnsupportedOperationException("sensor not operational");
				e.printStackTrace();
			}
			break;
		}
		throw new UnsupportedOperationException();
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
