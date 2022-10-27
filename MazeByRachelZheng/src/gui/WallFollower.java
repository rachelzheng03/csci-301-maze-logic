/**
 * 
 */
package gui;

import generation.Maze;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * Responsibilities: getting out of the maze using the wall-following algorithm

 * Collaborators: RobotDriver (unreliable or reliable), Maze
 * 
 * @author Rachel Zheng
 *
 */
public class WallFollower implements RobotDriver {

	private Robot robot;
	private Maze maze;
	private float startBattery;
	
	public WallFollower() {
		// TODO Auto-generated constructor stub
		robot=null;
		maze=null;
	}
	
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		this.robot=r;
		startBattery=r.getBatteryLevel();
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze=maze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		while (!robot.isAtExit()) {
			drive1Step2Exit();
		}
		return false;
	}
	
	/**
	 *Algorithm for a Perfect Maze:
	 *	If robot is at exit:
	 *		return
	 *	If there is no left wall:
	 *		rotate left and move forward 1
	 *		return
	 *	If there is no front wall:
	 *		move forward 1
	 *		return
	 *	Else:
	 *		turn right 	
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		if (robot.isAtExit())
			return false;
		if (robot.distanceToObstacle(Direction.LEFT)>0) {
			robot.rotate(Turn.LEFT);
			robot.move(1);
			return true;
		}
		if (robot.distanceToObstacle(Direction.FORWARD)>0) {
			robot.move(1);
			return true;
		}
		else {
			robot.rotate(Turn.RIGHT);
		}
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
