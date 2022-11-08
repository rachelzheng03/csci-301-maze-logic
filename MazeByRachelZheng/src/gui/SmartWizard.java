package gui;

import java.util.ArrayList;
import generation.CardinalDirection;
import generation.Distance;
import generation.Maze;
import gui.Robot.Direction;

/**
 * Responsibilities: drive to the exit and face the exit, keep track of pathlength and energy consumption
 * Collaborators:
 * @author rzhe2
 *
 */
public class SmartWizard extends Wizard implements RobotDriver {

	Distance mazeDist; //contains the distance of each cell to the exit
	
	public SmartWizard() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	private void setMazeDist() {
		assert(maze!=null);
		mazeDist=maze.getMazedists();
	}
	
	/**
	 * method sets maze and mazedist
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze=maze;
		setMazeDist();
	}
	
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		//at each position, get neighbor closer to exit
		//move to said neighbor, rotate if needed
//		if robot moves into wall, throw exception
//		if robot does not have sufficient energy, throw exception
//		deduct energy costs
//		repeat until robot is facing the exit
		while (!robot.isAtExit()) {
			System.out.println("running");
			drive1Step2Exit();
		}
		drive1Step2Exit();
		return true;
	}

	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		if (robot.isAtExit()) {
			return super.drive1Step2Exit();
		}
		System.out.println("not at exit");

		int[] position=robot.getCurrentPosition();
		int[] destination = maze.getNeighborCloserToExit(position[0], position[1]);
		//list of neighbors that exist in the maze
		ArrayList<int[]> neighborCandidates=new ArrayList<>();
		ArrayList<CardinalDirection> dirCandidates=new ArrayList<>();
		//check if east neighbor is in the maze
		if(maze.isValidPosition(position[0]+1, position[1])) {
			int[] position1= {position[0]+1, position[1]};
			neighborCandidates.add(position1);
			dirCandidates.add(CardinalDirection.East);
		}
		//check if west neighbor is in the maze
		if(maze.isValidPosition(position[0]-1, position[1])) {
			int[] position1= {position[0]-1, position[1]};
			neighborCandidates.add(position1);
			dirCandidates.add(CardinalDirection.West);
		}
		//check if south neighbor is in the maze
		if(maze.isValidPosition(position[0], position[1]+1)) {
			int[] position1= {position[0]-1, position[1]};
			neighborCandidates.add(position1);
			dirCandidates.add(CardinalDirection.South);
		}
		//check if north neighbor is in the maze
		if(maze.isValidPosition(position[0], position[1]-1)) {
			int[] position1= {position[0]-1, position[1]};
			neighborCandidates.add(position1);
			dirCandidates.add(CardinalDirection.North);
		}
		
		//find neighbor with the shortest distance to exit
		int[] tempShortNeighbor = neighborCandidates.get(0);
		CardinalDirection tempDirection=dirCandidates.get(0);
		for(int i=0; i<neighborCandidates.size(); i++) {
			if (mazeDist.getDistanceValue(tempShortNeighbor[0], tempShortNeighbor[1])>mazeDist.getDistanceValue(neighborCandidates.get(i)[0], neighborCandidates.get(i)[1])) {
					tempShortNeighbor=neighborCandidates.get(i);
					tempDirection=dirCandidates.get(i);
			}
		}
		//if the closest neighbor is separated by a wall, determine if it's more energy efficient to jump across the wall
		if (maze.hasWall(position[0], position[1], tempDirection)){
			int distanceDifference=mazeDist.getDistanceValue(position[0], position[1])- mazeDist.getDistanceValue(tempShortNeighbor[0], tempShortNeighbor[1]);
			System.out.println(distanceDifference);
			//6(steps)+3*(0.5*steps) [6 battery for each move forward and 3 battery for each rotation, amount of rotations estimated to be half the amount of steps]
			if(40<(7.5*distanceDifference)) {
				System.out.println("small enough");
				//turn to face neighbor
				switch (tempDirection) {
				case North: 
					turnToNorth(robot.getCurrentDirection());
					break;
				case East:
					turnToEast(robot.getCurrentDirection());
					break;
				case West:
					turnToWest(robot.getCurrentDirection());
					break;
				case South:
					turnToSouth(robot.getCurrentDirection());
					break;
				}
				if(robot.hasStopped())
					throw new Exception("Robot has stopped");
				//jump
				robot.jump();
			}
			else {
				super.drive1Step2Exit();
			}
			if(robot.hasStopped())
				throw new Exception("Robot has stopped");
		}
		//no difference between closest neighbor regardless of walls: do what wizard does
		else {
			System.out.println("othercase");

			super.drive1Step2Exit();
		}
		return robot.getCurrentPosition()==tempShortNeighbor;
		//return false;
	}

}
