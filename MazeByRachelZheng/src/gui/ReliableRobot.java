/**
 * 
 */
package gui;

import generation.CardinalDirection;
import generation.Maze;
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
	
	private Control controller;
	private float batteryLevel;
	private final static float ENERGY_FOR_FULL_ROTATION=12;
	private final static float ENERGY_FOR_STEP_FORWARD=12;
	private int odometer; //distance traveled
	private ReliableSensor forwardSensor;
	private ReliableSensor backwardSensor;
	private ReliableSensor leftSensor;
	private ReliableSensor rightSensor;
	private boolean hasStopped;
	
	//constructor 
	public ReliableRobot() {
		addDistanceSensor(forwardSensor, Direction.FORWARD);
		addDistanceSensor(backwardSensor, Direction.BACKWARD);
		addDistanceSensor(leftSensor, Direction.LEFT);
		addDistanceSensor(rightSensor, Direction.RIGHT);
		hasStopped=false;
		controller=null;
		odometer=0;
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
	}

	@Override
	public int[] getCurrentPosition() throws Exception {
		// TODO Auto-generated method stub
		//if position out of maze throw exception
		int[] position = controller.getCurrentPosition();
		assert(position!=null);
		assert(controller.getMaze()!=null);
		if (controller.getMaze().isValidPosition(position[0], position[1])) {
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
		if (level<=0)
			hasStopped=true;
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
		if (batteryLevel<=0)
			return;
		//check if energy is sufficient
		batteryLevel=batteryLevel-3;
		if (batteryLevel<0) {
			hasStopped=true;
			return;
		}
		
		//turn in direction indicated by parameter
		else if (turn==Turn.LEFT) {
			controller.handleKeyboardInput(UserInput.LEFT, 0);
		}
		else if (turn==Turn.RIGHT) {
			controller.handleKeyboardInput(UserInput.RIGHT, 0);
		}
		else if (turn==Turn.AROUND) {
			controller.handleKeyboardInput(UserInput.RIGHT, 0);
			batteryLevel=batteryLevel-3;
			if (batteryLevel<=0) 
				hasStopped=true;
			else {
				controller.handleKeyboardInput(UserInput.RIGHT, 0);
			}
		}
	}

	@Override
	public void move(int distance) {
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
		
		for(int i=0; i<distance; i++) {
			//check if any energy is available at all
			if (batteryLevel<=0)
				return;
			//check if energy is sufficient
			batteryLevel=batteryLevel-6;
			if (batteryLevel<0) {
				hasStopped=true;
				return;
			}
			//check if robot hits wall
			if(controller.getMaze().hasWall(xPosition, yPosition, getCurrentDirection())) {
				hasStopped=true;
				return;
			}
			//move 1 step in the forward direction
			else
				controller.handleKeyboardInput(UserInput.UP, 0);
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
		
		//if jump will take robot out of maze, then robot is stopped
		switch (controller.getCurrentDirection()) {
		case North: 
			if (yPosition==0)
				hasStopped=true;
				return;
		case East:
			if (xPosition==controller.getMaze().getWidth())
				hasStopped=true;
				return;
		case South:
			if (yPosition==controller.getMaze().getHeight())
				hasStopped=true;
				return;
		case West:
			if (xPosition==0)
				hasStopped=true;
				return;
		}
		
		//check if any energy is available at all
		if (batteryLevel<=0)
			return;
		//check if energy is sufficient
		batteryLevel=batteryLevel-40;
		if (batteryLevel<0) {
			hasStopped=true;
			return;
		}
		controller.handleKeyboardInput(UserInput.JUMP, 0);
		//check that energy is sufficient and not facing exterior wall
		//jump over 1 in the direction the robot is facing
	}

	@Override
	public boolean isAtExit() {
		// TODO Auto-generated method stub
		try {
			int[] position = getCurrentPosition();
			return position==controller.getMaze().getExitPosition();
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

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		float[] powersupply = {batteryLevel};
		switch (direction)	{	
		case FORWARD: 
			try {
				return forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case BACKWARD:
			try {
				return backwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case LEFT:
			try {
				return leftSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case RIGHT:
			try {
				return rightSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
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
			try {
				return forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case BACKWARD:
			try {
				return backwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case LEFT:
			try {
				return leftSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case RIGHT:
			try {
				return rightSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply)==Integer.MAX_VALUE;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
