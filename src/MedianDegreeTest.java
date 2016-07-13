import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class MedianDegreeTest {
	@Test
	public void testAddPayment() {
		try {
			MedianDegree md = new MedianDegree();
			Method addPayment = md.getClass().getDeclaredMethod("addPayment", String.class, String.class, String.class);
			assertNotNull(addPayment);
			addPayment.setAccessible(true);
			boolean result = (Boolean)addPayment.invoke(md, "ANYTHING", "ANYTHING", "NOT EXIST");
			assertFalse(result);
			
			System.out.println();
			result = (Boolean)addPayment.invoke(md, "Amber-Sauer", "Raffi-Antilian", "2016-03-28T23:23:12Z");
			assertTrue(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	@Test
	public void testProcessInput() {
		try {
			MedianDegree md = new MedianDegree();
			Method processInput = md.getClass().getDeclaredMethod("processInput", String.class);
			assertNotNull(processInput);
			processInput.setAccessible(true);
			boolean result = (Boolean)processInput.invoke(md, "NOT EXIST");
			assertFalse(result);
			
			System.out.println();
			result = (Boolean)processInput.invoke(md, "data-gen/venmo-trans.txt");
			assertTrue(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			Date d = tf.parse("2016-03-28T23:23:12Z");
			System.out.println("date=" + d);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
//	@Test
	public void testMain() {
		MedianDegree.main(new String[] {"data-gen/venmo-trans.txt", "venmo_output/output.txt"});
	}
}
