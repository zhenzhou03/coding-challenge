import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class VenmoGraphTest {
	@Test
	public void testConstructor() {
		VenmoGraph graph = new VenmoGraph(60);
		assertNotNull(graph);
		System.out.println("graph=" + graph);
	}
	
	@Test
	public void testAddPayment() {
		VenmoGraph graph = new VenmoGraph(60);
		
		Date createdTime = new Date();
		assertTrue(graph.addPayment("actor1", "target1", createdTime));
		System.out.println("graph=" + graph);
		
		assertFalse(graph.addPayment("actor1", "target1", createdTime));
		System.out.println("graph=" + graph);
		
		assertFalse(graph.addPayment(null, "target1", createdTime));
		System.out.println("graph=" + graph);
		
		assertFalse(graph.addPayment("actor1", null, createdTime));
		System.out.println("graph=" + graph);
		
		assertFalse(graph.addPayment("actor1", "target1", null));
		System.out.println("graph=" + graph);
		
		assertFalse(graph.addPayment("actor1", "target2", new Date(createdTime.getTime() - 61*1000)));
		System.out.println("graph=" + graph);		

		assertTrue(graph.addPayment("actor1", "target2", new Date(createdTime.getTime() - 59*1000)));
		System.out.println("graph=" + graph);		
	}
	
	@Test
	public void testGetMedian() {
		VenmoGraph graph = new VenmoGraph(60);
		
		Date createdTime = new Date();
		graph.addPayment("actor1", "target1", createdTime);
		System.out.println("graph=" + graph);
		assertEquals(1.0, graph.getMedian(), 0);
		
		graph.addPayment("actor1", "target2", createdTime);
		System.out.println("graph=" + graph);
		assertEquals(2.0, graph.getMedian(), 0);
		
		graph.addPayment("actor2", "target3", new Date(createdTime.getTime() - 59*1000));
		System.out.println("graph=" + graph);
		assertEquals(1.5, graph.getMedian(), 0);
	}	
}
