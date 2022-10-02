package generation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.Order.Builder;

class MazeBuilderBoruvkaTest extends MazeFactoryTest {


	private MazeBuilderBoruvka mbBoruvka;
	private MazeBuilderBoruvka mbBoruvka2;
	
	@BeforeEach
	public void setUp() {
		order=new DefaultOrder(0);
		order2=new DefaultOrder(1);
		order2.setPerfect(false); 
		order3=new DefaultOrder(3);
		mbBoruvka=new MazeBuilderBoruvka();
		mbBoruvka.buildOrder(order);
		mbBoruvka2=new MazeBuilderBoruvka();
		mbBoruvka2.buildOrder(order2);
		
		//factory=new MazeFactory();
		//factory.order(order);
		//factory.waitTillDelivered();
	}

	@Test
	public void testGetEdgeWeight() {
		//maze (4x4)
		mbBoruvka.floorplan.initialize();
		mbBoruvka.setAllEdgeWeights();
		//test that border wallboard edge weights are 0
		Random random=new Random();
		int x = random.nextInt(4);
		int y = random.nextInt(4);
		assertEquals(0, mbBoruvka.getEdgeWeight(0, x, CardinalDirection.West));
		assertEquals(0, mbBoruvka.getEdgeWeight(x, 3, CardinalDirection.South));
		assertEquals(0, mbBoruvka.getEdgeWeight(x, 0, CardinalDirection.North));
		assertEquals(0, mbBoruvka.getEdgeWeight(3, x, CardinalDirection.East));
		//test the edge weight of a wallboard from both directions. returned value should be equal
		assertEquals(mbBoruvka.getEdgeWeight(0, 0, CardinalDirection.East), mbBoruvka.getEdgeWeight(1, 0, CardinalDirection.West));
		assertEquals(mbBoruvka.getEdgeWeight(0, 0, CardinalDirection.South), mbBoruvka.getEdgeWeight(0, 1, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(3, 0, CardinalDirection.West), mbBoruvka.getEdgeWeight(2, 0, CardinalDirection.East));
		assertEquals(mbBoruvka.getEdgeWeight(3, 0, CardinalDirection.South), mbBoruvka.getEdgeWeight(3, 1, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(1, 3, CardinalDirection.West), mbBoruvka.getEdgeWeight(0, 3, CardinalDirection.East));
		assertEquals(mbBoruvka.getEdgeWeight(0, 2, CardinalDirection.South), mbBoruvka.getEdgeWeight(0, 3, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(2, 3, CardinalDirection.East), mbBoruvka.getEdgeWeight(3, 3, CardinalDirection.West));
		assertEquals(mbBoruvka.getEdgeWeight(3, 2, CardinalDirection.South), mbBoruvka.getEdgeWeight(3, 3, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(2, 2, CardinalDirection.North), mbBoruvka.getEdgeWeight(2, 1, CardinalDirection.South));
		assertEquals(mbBoruvka.getEdgeWeight(2, 2, CardinalDirection.South), mbBoruvka.getEdgeWeight(2, 3, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(2, 2, CardinalDirection.East), mbBoruvka.getEdgeWeight(3, 2, CardinalDirection.West));
		assertEquals(mbBoruvka.getEdgeWeight(2, 2, CardinalDirection.West), mbBoruvka.getEdgeWeight(1, 2, CardinalDirection.East));		
		//deterministic test: call method getEdgeWeight twice with the same input and make sure it returns the same value
		assertEquals(mbBoruvka.getEdgeWeight(x, y, CardinalDirection.North), mbBoruvka.getEdgeWeight(x, y, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(x, y, CardinalDirection.East), mbBoruvka.getEdgeWeight(x, y, CardinalDirection.East));
		assertEquals(mbBoruvka.getEdgeWeight(x, y, CardinalDirection.South), mbBoruvka.getEdgeWeight(x, y, CardinalDirection.South));
		assertEquals(mbBoruvka.getEdgeWeight(x, y, CardinalDirection.West), mbBoruvka.getEdgeWeight(x, y, CardinalDirection.West));
		
		//maze (20x15) with rooms
		mbBoruvka2.floorplan.initialize();
		mbBoruvka2.floorplan.markAreaAsRoom(3, 8, 1, 2, 4, 10);
		mbBoruvka2.setAllEdgeWeights();
		// edge weights of a cell that resides in a room should all be 0
		assertTrue(mbBoruvka2.floorplan.isInRoom(3,3));
		assertEquals(0, mbBoruvka2.getEdgeWeight(3, 3, CardinalDirection.North));
		assertEquals(0, mbBoruvka2.getEdgeWeight(3, 3, CardinalDirection.East));
		assertEquals(0, mbBoruvka2.getEdgeWeight(3, 3, CardinalDirection.South));
		assertEquals(0, mbBoruvka2.getEdgeWeight(3, 3, CardinalDirection.West));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 2, CardinalDirection.North));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 2, CardinalDirection.East));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 2, CardinalDirection.South));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 2, CardinalDirection.West));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 10, CardinalDirection.North));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 10, CardinalDirection.East));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 10, CardinalDirection.South));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 10, CardinalDirection.West));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 2, CardinalDirection.North));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 2, CardinalDirection.East));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 2, CardinalDirection.South));
		assertEquals(0, mbBoruvka2.getEdgeWeight(4, 2, CardinalDirection.West));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 10, CardinalDirection.North));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 10, CardinalDirection.East));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 10, CardinalDirection.South));
		assertEquals(0, mbBoruvka2.getEdgeWeight(1, 10, CardinalDirection.West));

	}

}
