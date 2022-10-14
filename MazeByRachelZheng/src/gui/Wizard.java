/**
 * 
 */
package gui;

import generation.Maze;

/**
 * Responsibilities: getting out of the maze as quickly as possible
 *  
 * Collaborators: ReliableRobot, Maze
 * @author Rachel Zheng
 *
 */
public class Wizard implements RobotDriver {
	
	private Robot reliableRobot;
	
	//constructor
	public void Wizard() {
	}

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		//at each position, get neighbor closer to exit
		//jump or move to said neighbor, rotate if needed
		//if robot moves into wall, throw exception
		//if robot does not have sufficient energy, throw exception
		//deduct energy costs
		//repeat until robot is facing the exit
		return false;
	}

	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		//if facing exit, return false
		//get neighbor closer to exit
		//jump or move to said neighbor, rotate if needed
		//if robot moves into wall, throw exception
		//if robot does not have sufficient energy, throw exception
		//deduct energy costs
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		//look to batterylevel of robot before and after
		return 0;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		//look at odometer of robot object
		return 0;
	}

}
