/**
 * 
 */
package gui;

import static org.junit.jupiter.api.Assertions.assertAll;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * Responsibilities: getting out of the maze as quickly as possible
 *  
 * Collaborators: ReliableRobot, Maze
 * @author Rachel Zheng
 *
 */
public class Wizard implements RobotDriver {
	
	private Robot reliableRobot;
	private Maze maze;
	private float startBattery;
	
	//constructor
	public Wizard() {
		reliableRobot=null;
	}

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		reliableRobot=r;
		startBattery=reliableRobot.getBatteryLevel();
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze=maze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		//at each position, get neighbor closer to exit
		//move to said neighbor, rotate if needed
		//if robot moves into wall, throw exception
		//if robot does not have sufficient energy, throw exception
		//deduct energy costs
		//repeat until robot is facing the exit
		while (!reliableRobot.isAtExit()) {
			drive1Step2Exit();
		}
		drive1Step2Exit();
		return true;
	}

	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		if (reliableRobot.isAtExit()) {
			while(!reliableRobot.canSeeThroughTheExitIntoEternity(Direction.FORWARD))
				reliableRobot.rotate(Turn.LEFT);
				if(reliableRobot.hasStopped())
					throw new Exception("Robot has stopped");	
			return false;
		}
		//get neighbor closer to exit
		int[] position=reliableRobot.getCurrentPosition();
		int[] destination = maze.getNeighborCloserToExit(position[0], position[1]);
		assert(destination!=null);
		//rotate to face neighbor
		if (position[0]==destination[0]) {
			if (position[1]>destination[1])
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.North)
					reliableRobot.rotate(Turn.LEFT);
					if(reliableRobot.hasStopped())
						throw new Exception("Robot has stopped");
			else
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.South)
					reliableRobot.rotate(Turn.LEFT);
					if(reliableRobot.hasStopped())
						throw new Exception("Robot has stopped");
		}
		else if (position[1]==destination[1]) {
			if (position[0]>destination[0])
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.West)
					reliableRobot.rotate(Turn.LEFT);
				if(reliableRobot.hasStopped())
					throw new Exception("Robot has stopped");
			else
				while(reliableRobot.getCurrentDirection()!=CardinalDirection.East)
					reliableRobot.rotate(Turn.LEFT);
				if(reliableRobot.hasStopped())
					throw new Exception("Robot has stopped");
		}
		reliableRobot.move(1);
		if(reliableRobot.hasStopped())
			throw new Exception("Robot has stopped");
		return reliableRobot.getCurrentPosition()==destination;
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		//look to batterylevel of robot before and after
		float endBattery=reliableRobot.getBatteryLevel();
		return startBattery-endBattery;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		//look at odometer of robot object
		return reliableRobot.getOdometerReading();
	}

}
