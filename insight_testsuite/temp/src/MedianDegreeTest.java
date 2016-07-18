

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.Test;

public class MedianDegreeTest {
	@Test
	public void testProcessInput() {
		try {
			MedianDegree md = new MedianDegree();
			Method processInput = md.getClass().getDeclaredMethod("processInput", String.class, int.class);
			assertNotNull(processInput);
			processInput.setAccessible(true);
			VenmoGraph graph = (VenmoGraph)processInput.invoke(md, "NOT EXIST", 10);
			assertNull(graph);
			
			System.out.println();
			graph = (VenmoGraph)processInput.invoke(md, "data-gen/venmo-trans.txt", 60);
			System.out.println("graph=" + graph);
			assertNotNull(graph);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}
	
//	@Test
	public void testMain() {
		MedianDegree.main(new String[] {"data-gen/venmo-trans.txt", "venmo_output/output.txt"});
	}
}
