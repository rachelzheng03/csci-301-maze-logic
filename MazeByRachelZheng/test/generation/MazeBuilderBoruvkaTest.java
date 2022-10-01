package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.Order.Builder;

class MazeBuilderBoruvkaTest extends MazeFactoryTest {

	private DefaultOrder order;
	private MazeFactory factory;
	
	@Override
	@BeforeEach
	public void setUp() {
		order = new DefaultOrder(0);
		order.setBuilder(Builder.Boruvka);
		factory=new MazeFactory();
		factory.order(order);
		factory.waitTillDelivered();
	}

	@Test
	public void testGetEdgeWeight() {
		//test the edge weight of a wallboard from both directions. returned value should be equal
		//test corners of maze 
		//deterministic test: call method getEdgeWeight twice with the same input and make sure it returns the same value
		
	}

}
