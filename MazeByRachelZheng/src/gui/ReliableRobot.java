/**
 * 
 */
package gui;

import generation.CardinalDirection;
import gui.Constants.UserInput;

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
	
	protected Control controller;
	protected float batteryLevel;
	private final static float ENERGY_FOR_FULL_ROTATION=12;
	private final static float ENERGY_FOR_STEP_FORWARD=6;
	protected int odometer; //distance traveled
	protected ReliableSensor forwardSensor;
	protected ReliableSensor backwardSensor;
	protected ReliableSensor leftSensor;
	protected ReliableSensor rightSensor;
	protected boolean hasStopped;
	
	//constructor 
	public ReliableRobot() {
		forwardSensor=new ReliableSensor();
		backwardSensor=new ReliableSensor();
		leftSensor=new ReliableSensor();
		rightSensor=new ReliableSensor();
		hasStopped=false;
		controller=null;
		odometer=0;
		batteryLevel=3500;
	}

	@Override
	public void setController(Control controller) {
		// TODO Auto-generated method stub
		this.controller=controller;

	}

	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		// TODO Auto-generated method stub
		//set sensor's mounted direction
		sensor.setSensorDirection(mountedDirection);
		assert(controller.getMaze()!=null);
		sensor.setMaze(controller.getMaze());
	}

	@Override
	public int[] getCurrentPosition() throws Exception {
		// TODO Auto-generated method stub
		//if position out of maze throw exception
		int[] position = controller.getCurrentPosition();
		assert(position!=null);
		assert(controller.getMaze()!=null);
		if (!controller.getMaze().isValidPosition(position[0], position[1])) {
			throw new Exception("position is out of maze");
		}
		return position;
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
		CardinalDirection direction = controller.getCurrentDirection();
		assert(direction!=null);
		return direction;
	}

	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		if (batteryLevel<=0)
			hasStopped=true;
		return batteryLevel;
	}

	@Override
	public void setBatteryLevel(float level) {
		// TODO Auto-generated method stub
		if (level<=0) {
			hasStopped=true;
			return;
		}
		this.batteryLevel=level;
		

	}

	@Override
	public float getEnergyForFullRotation() {
		// TODO Auto-generated method stub
		//should be 12 for a full rotation
		return ENERGY_FOR_FULL_ROTATION;
	}

	@Override
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		//should be 6
		return ENERGY_FOR_STEP_FORWARD;
	}

	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return odometer;
	}

	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub
		odometer=0;

	}

	@Override
	public void rotate(Turn turn) {
		// TODO Auto-generated method stub
		//check if any energy is available at all
		if (batteryLevel<=0) {
			hasStopped=true;
			return;
		}
		//check if energy is sufficient
		batteryLevel=batteryLevel-(ENERGY_FOR_FULL_ROTATION/4);
		if (batteryLevel<0) {
			hasStopped=true;
			return;
		}
		
		//turn in direction indicated by parameter
		else if (turn==Turn.LEFT) {
			controller.currentState.handleUserInput(UserInput.LEFT, 0);
		}
		else if (turn==Turn.RIGHT) {
			controller.currentState.handleUserInput(UserInput.RIGHT, 0);
		}
		else if (turn==Turn.AROUND) {
			controller.currentState.handleUserInput(UserInput.RIGHT, 0);
			if (batteryLevel<=0) {
				hasStopped=true;
				return;
			}
			batteryLevel=batteryLevel-(ENERGY_FOR_FULL_ROTATION/4);
			if (batteryLevel<=0) 
				hasStopped=true;
			else {
				controller.currentState.handleUserInput(UserInput.RIGHT, 0);
			}
		}
	}

	@Override
	public void move(int distance) {
		// TODO Auto-generated method stub
		int xPosition;
		int yPosition;

		
		for(int i=0; i<distance; i++) {
			try {
				xPosition = getCurrentPosition()[0];
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

			try {
				yPosition = getCurrentPosition()[1];
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			//check if any energy is available at all
			if (batteryLevel<=0) {
				hasStopped=true;
				return;
			}
			//check if energy is sufficient
			batteryLevel=batteryLevel-6;
			if (batteryLevel<0) {
				hasStopped=true;
				return;
			}
			//check if robot hits wall
			if(controller.getMaze().hasWall(xPosition, yPosition, getCurrentDirection())) {
				System.out.println("robot hit a wall");
				hasStopped=true;
				return;
			}
			//move 1 step in the forward direction
			else
				controller.currentState.handleUserInput(UserInput.UP, 0);
				//increase odometer (number of 1 cell steps) by 1
				odometer+=1;
		}
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		int xPosition;
		try {
			xPosition = getCurrentPosition()[0];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int yPosition;
		try {
			yPosition = getCurrentPosition()[1];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		//check if any energy is available at all
		if (batteryLevel<=0) {
			hasStopped=true;
			return;
		}
		//check if energy is sufficient
		batteryLevel=batteryLevel-40;
		if (batteryLevel<0) {
			hasStopped=true;
			return;
		}
		
		//if jump will take robot out of maze, then robot is stopped
		switch (controller.getCurrentDirection()) {
		case North: 
			if (yPosition==0) {
				hasStopped=true;
				return;
			}
			break;
		case East:
			if (xPosition==controller.getMaze().getWidth()-1) {
				hasStopped=true;
				return;
			}
			break;
		case South:
			if (yPosition==controller.getMaze().getHeight()-1) {
				hasStopped=true;
				return;
			}
			break;
		case West:
			if (xPosition==0) {
				hasStopped=true;
				return;
			}
			break;
		}
		controller.currentState.handleUserInput(UserInput.JUMP, 0);
		//increase odometer (number of 1 cell steps) by 1
		odometer+=1;
	}

	@Override
	public boolean isAtExit() {
		// TODO Auto-generated method stub
		try {
			int[] position = getCurrentPosition();
			int x = position[0];
			int y = position[1];
			return x==controller.getMaze().getExitPosition()[0]&&y==controller.getMaze().getExitPosition()[1];
		} catch (Exception e) { //if current position is out of maze
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean isInsideRoom() {
		// TODO Auto-generated method stub
		try {
			int[] position = getCurrentPosition();
			return controller.getMaze().isInRoom(position[0], position[1]);
		} catch (Exception e) { //if current position is out of maze
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean hasStopped() {
		// TODO Auto-generated method stub
		return hasStopped;
	}
	
	/**
	 * Resets hasStopped. For TESTING purposes.
	 */
	public void resetHasStopped(boolean stopped) {
		hasStopped=stopped;
	}
	
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		float[] powersupply = {batteryLevel};
		switch (direction)	{	
		case FORWARD: 
			if (forwardSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				int steps = forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e.getMessage()=="Sensor Failure: sensor not operational" && forwardSensor!=null)
					throw new UnsupportedOperationException("sensor not operational");
				if(e.getMessage()=="Power Failure: power supply is insufficient for the operation") {
					hasStopped=true;
				}
			}
			break;
		case BACKWARD:
			if (backwardSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				int steps = backwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e.getMessage()=="Sensor Failure: sensor not operational" && backwardSensor!=null)
					throw new UnsupportedOperationException("sensor not operational");
				if(e.getMessage()=="Power Failure: power supply is insufficient for the operation") {
					hasStopped=true;
				}
			}
			break;
		case LEFT:
			if (leftSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				int steps = leftSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e.getMessage()=="Sensor Failure: sensor not operational" && leftSensor!=null)
					throw new UnsupportedOperationException("sensor not operational");
				if(e.getMessage()=="Power Failure: power supply is insufficient for the operation") {
					hasStopped=true;
				}
			}
			break;
		case RIGHT:
			if (rightSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				int steps = rightSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
				batteryLevel=batteryLevel-1;
				return steps;			
				} catch (Error|Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
					if(e.getMessage()=="Sensor Failure: sensor not operational" && rightSensor!=null)
						throw new UnsupportedOperationException("sensor not operational");
					if(e.getMessage()=="Power Failure: power supply is insufficient for the operation") {
						hasStopped=true;
					}
			}
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
		throw new UnsupportedOperationException();
		
	}

	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		//check direction robot is facing and location of robot
		//return infinity if facing exit		
		float[] powersupply = {batteryLevel};
		switch (direction)	{	
		case FORWARD: 
			if (forwardSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				if (forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE)
					batteryLevel-=1;
				return forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational")
					throw new UnsupportedOperationException("sensor not operational");
				else {
					e.printStackTrace();
				}			}
			break;
		case BACKWARD:
			if (backwardSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				if (backwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE)
					batteryLevel-=1;
				return backwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational")
					throw new UnsupportedOperationException("sensor not operational");
				else {
					e.printStackTrace();
				}			}
			break;
		case LEFT:
			if (leftSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				if (leftSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE)
					batteryLevel-=1;
				return leftSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational")
					throw new UnsupportedOperationException("sensor not operational");
				else {
					e.printStackTrace();
				}			}
			break;
		case RIGHT:
			if (rightSensor==null)
				throw new UnsupportedOperationException("sensor is null");
			try {
				if (rightSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE)
					batteryLevel-=1;
				return rightSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Error|Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage()=="Sensor Failure: sensor not operational")
					throw new UnsupportedOperationException("sensor not operational");
				else {
					e.printStackTrace();
				}
			}
			break;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("method not supported for reliable robot");
	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("method not supported for reliable robot");
	}

}
