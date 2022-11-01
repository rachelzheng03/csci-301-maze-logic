/**
 * 
 */
package gui;

import static org.junit.Assert.assertTrue;

import generation.CardinalDirection;

/**
 * Responsibilities:
 * 1)Calculate distance to a wallboard based on direction of sensor. 
 * 2)Calculate energy used to get distance
 * 3)Accounts for the failure and repair of sensors while the robot is running
 * 4)Switches sensors when another one fails so that robot can continue doing its job
 * 
 * Collaborators: CardinalDirection, Maze
 * @author Rachel Zheng
 *
 */
public class UnreliableSensor extends ReliableSensor implements Runnable {
	
	private boolean operational; //is true if the sensor is in an operational state. is false if sensor is in a failed state
	private boolean hasStopped; 
	private int meanTimeBetweenFailures;
	private int meanTimeToRepair;
	protected Thread operationalState;
	private boolean startedFRP; // is true if startFailureAndRepairProcess has been called. is false otherwise
	
	public UnreliableSensor() {
		maze=null;
		mountedDirection=null;
		operational=true;
		hasStopped=false;
	}
	
	
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		if(!operational)
			throw new Error("Sensor Failure: sensor not operational");
		return super.distanceToObstacle(currentPosition, currentDirection, powersupply);
	}

	public void run() {
		System.out.println("starting thread"+mountedDirection);
		try {
			while(true){
				Thread.sleep(meanTimeBetweenFailures*1000);
				//fail sensor
				operational=false;
				System.out.println(operational+" "+" "+mountedDirection);
				Thread.sleep(meanTimeToRepair*1000);
				//sensor repaired
				operational=true;
				System.out.println(operational+" "+" "+mountedDirection);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			//e.printStackTrace();
			operationalState=null;
			return;
		}
	}
	
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		this.meanTimeBetweenFailures=meanTimeBetweenFailures;
		this.meanTimeToRepair=meanTimeToRepair;
		startedFRP=true;
		operationalState = new Thread(this);
		operationalState.start();
	}

	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		//return an UnsupportedOperationException if called with no running failure and repair process
		if(!startedFRP)
			throw new UnsupportedOperationException("Failure and repair process has not been started. There is nothing to stop.");
		//stop thread
		while (!hasStopped) {
			if (operational) {
				System.out.println("start stop" + mountedDirection);
				operationalState.interrupt();
				hasStopped=true;
			}
		}

	}
	
	public boolean getOperational() {
		return operational;
	}

}
