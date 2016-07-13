package venmo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

//example of program that calculates the  median degree of a 
//venmo transaction graph
public class MedianDegree {
	private final VenmoGraph graph = new VenmoGraph(60);
	private final StringBuffer output = new StringBuffer();
	
	private final static String ACTOR = "actor";
	private final static String TARGET = "target";
	private final static String CREATED_TIME = "created_time";
	private final static SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	public static void main(final String[] args) {
		if (args == null || args.length != 2) {
			System.out.println("wrong number of arguments!");
			printlnUsage();
		    return;
		}
		
		MedianDegree md = new MedianDegree();
		if (md.processInput(args[0])) {
			try (OutputStream os = new FileOutputStream(args[1])){
				os.write(md.output.toString().getBytes());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				printlnUsage();
			}
		}
	}
	
	private static void printlnUsage() {
		System.out.println("Usage:");
		System.out.println("\tMedianDegree input output");
	}
	
	private boolean processInput(final String input) {
		if (input == null) {
			System.out.println("input is null!");
			printlnUsage();
			return false;
		}
		
		try (InputStream fis = new FileInputStream(input);
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			    BufferedReader br = new BufferedReader(isr)) {
			String line = null;
			while ((line = br.readLine()) != null) {
				JsonReader reader = Json.createReader(new StringReader(line));
				JsonObject obj = reader.readObject();
				String actor = obj.getString(ACTOR);
				String target = obj.getString(TARGET);
				String createdTime = obj.getString(CREATED_TIME); 
				if (actor != null && target != null && createdTime != null) {
					boolean result = addPayment(actor, target, createdTime);
					if (result)
						output.append(graph.getMedian()).append("\n");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			printlnUsage();
			return false;
		}
		return true;
	}
	
	private boolean addPayment(final String actor, final String target, final String createdTime) {
		if (actor == null || target == null || createdTime == null)
			return false;
		
		Date createdTimestamp = null;
		try {
			createdTimestamp = tf.parse(createdTime);
		} catch (Exception ex) {
			createdTimestamp = null;
		}
		
		return graph.addPayment(actor, target, createdTimestamp);
	}
}

