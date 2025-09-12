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
		factory = new MazeFactory();
		order = new DefaultOrder(0, Builder.Boruvka, true, 13); // 4x4
		order2 = new DefaultOrder(1, Builder.Boruvka, false, 13); // 12x12 with rooms
		order3 = new DefaultOrder(3, Builder.Boruvka, true, 13); //(20x15)
		factory.order(order);
		factory.waitTillDelivered();
		maze = order.getMaze();
		factory.order(order2);
		factory.waitTillDelivered();
		maze2 = order2.getMaze();
		factory.order(order3);
		factory.waitTillDelivered();
		maze3 = order3.getMaze();
	}
	
	@Test
	public void testGetEdgeWeight() {
		mbBoruvka=new MazeBuilderBoruvka();
		mbBoruvka.buildOrder(order);
		assertEquals(mbBoruvka.getEdgeWeight(0, 0, CardinalDirection.South), mbBoruvka.getEdgeWeight(0, 1, CardinalDirection.North));
		assertEquals(mbBoruvka.getEdgeWeight(0, 0, CardinalDirection.East), mbBoruvka.getEdgeWeight(1, 0, CardinalDirection.West));
		
		assertEquals(mbBoruvka.getEdgeWeight(3, 3, CardinalDirection.North), mbBoruvka.getEdgeWeight(3, 2, CardinalDirection.South));
		assertEquals(mbBoruvka.getEdgeWeight(3, 3, CardinalDirection.West), mbBoruvka.getEdgeWeight(2, 3, CardinalDirection.East));
		
		mbBoruvka2 = new MazeBuilderBoruvka();
		mbBoruvka2.buildOrder(order2);
		assertEquals(mbBoruvka2.getEdgeWeight(0, 0, CardinalDirection.South), mbBoruvka2.getEdgeWeight(0, 1, CardinalDirection.North));
		assertEquals(mbBoruvka2.getEdgeWeight(0, 0, CardinalDirection.East), mbBoruvka2.getEdgeWeight(1, 0, CardinalDirection.West));
		
		assertEquals(mbBoruvka2.getEdgeWeight(11, 10, CardinalDirection.South), mbBoruvka2.getEdgeWeight(11, 11, CardinalDirection.North));
		assertEquals(mbBoruvka2.getEdgeWeight(10, 11, CardinalDirection.East), mbBoruvka2.getEdgeWeight(11, 11, CardinalDirection.West));
	}
}
