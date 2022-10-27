/**
 * 
 */
package gui;

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
public class UnreliableSensor extends ReliableSensor implements DistanceSensor {

}
