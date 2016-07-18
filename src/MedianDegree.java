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
	private final static String ACTOR = "actor";
	private final static String TARGET = "target";
	private final static String CREATED_TIME = "created_time";
	private final static SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private final static int DEFAULT_DURATION = 60;//seconds
	
	public static void main(final String[] args) {
		if (args == null || args.length < 2 || args.length > 3) {
			System.out.println("wrong number of arguments!");
			printlnUsage();
		    return;
		}
		
		int duration = DEFAULT_DURATION;
		if (args.length == 3) {
			try {
				duration = Integer.valueOf(args[2]);
			} catch (Exception ex) {
				System.out.println("wrong duration format!");
				printlnUsage();
			    return;				
			}
		}
		
		MedianDegree md = new MedianDegree();
		
		VenmoGraph graph = md.processInput(args[0], duration);
		if (graph == null) {
			System.out.println("error to process input.");
			printlnUsage();
			return;
		}

		try (OutputStream os = new FileOutputStream(args[1])){
			for (Double d : graph.getSampleMedians()) {
				os.write(String.format("%.2f", d).getBytes());
				os.write(System.getProperty("line.separator").getBytes());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			printlnUsage();
		}
	}
	
	private static void printlnUsage() {
		System.out.println("Usage:");
		System.out.println("\tMedianDegree input output <duration: default 60 seconds>");
	}
	
	private VenmoGraph processInput(final String input, final int duration) {
		if (input == null) {
			System.out.println("input is null!");
			printlnUsage();
			return null;
		}
		
		VenmoGraph graph = new VenmoGraph(duration);
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
					Date createdTimestamp = null;
					try {
						createdTimestamp = tf.parse(createdTime);
					} catch (Exception ex) {
						createdTimestamp = null;
						continue;
					}
					
					graph.addPayment(actor, target, createdTimestamp);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			printlnUsage();
			return null;
		}
		return graph;
	}	
}

