

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
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
		
		assertFalse(graph.addPayment("actor1", "actor1", createdTime));
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
		try {
			Method getMedian = VenmoGraph.class.getDeclaredMethod("getMedian");
			assertNotNull(getMedian);
			getMedian.setAccessible(true);
			
			VenmoGraph graph = new VenmoGraph(60);

			Date createdTime = new Date();
			graph.addPayment("actor1", "target1", createdTime);
			System.out.println("graph=" + graph);
			assertEquals(1.0, (Double)getMedian.invoke(graph), 0);

			graph.addPayment("actor1", "target2", createdTime);
			System.out.println("graph=" + graph);
			assertEquals(1.0, (Double)getMedian.invoke(graph), 0);

			graph.addPayment("actor1", "target3", new Date(createdTime.getTime() - 59*1000));
			System.out.println("graph=" + graph);
			assertEquals(1.0, (Double)getMedian.invoke(graph), 0);
			
			graph.addPayment("target1", "target2", new Date(createdTime.getTime() - 59*1000));
			System.out.println("graph=" + graph);
			assertEquals(2.0, (Double)getMedian.invoke(graph), 0);
			
			graph.addPayment("actor2", "target2", createdTime);
			System.out.println("graph=" + graph);
			assertEquals(2.0, (Double)getMedian.invoke(graph), 0);

			graph.addPayment("target1", "actor2", createdTime);
			System.out.println("graph=" + graph);
			assertEquals(3.0, (Double)getMedian.invoke(graph), 0);
			
			graph.addPayment("actor3", "target3", createdTime);
			System.out.println("graph=" + graph);
			assertEquals(2.5, (Double)getMedian.invoke(graph), 0);			
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}
}
