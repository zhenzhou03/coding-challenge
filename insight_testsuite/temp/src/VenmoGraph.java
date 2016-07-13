import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VenmoGraph {
	private final Map<String, Set<String>> nodes = new ConcurrentHashMap<>();
	private volatile long lastTimestamp;//unit in second
	
	private final long duration;
	
	/**
	 * getMedian
	 * 
	 * @return double
	 */
	public double getMedian() {
		if (nodes.isEmpty())
			return Double.NaN;
		
		int total = 0;
		for (Set<String> links : nodes.values()) {
			total += links.size();
		}
		return ((double)total) / nodes.size();
	}
	
	/**
	 * addPayment
	 * 
	 * @param actor String
	 * @param target String
	 * @param createdTime Date
	 * @return boolean, false if graph is not changed
	 */
	public boolean addPayment(final String actor, final String target, final Date createdTime) {
		if (actor == null || target == null || createdTime == null)
			return false;
		
		long timestamp = createdTime.getTime() / 1000;
		if (timestamp + duration < lastTimestamp)
			return false;
		
		Set<String> links = nodes.get(actor);
		if (links == null) {
			links = new HashSet<String>();
			nodes.put(actor, links);
		}
		boolean result = links.add(target);
		if (timestamp > lastTimestamp)
			lastTimestamp = timestamp;
		return result;
	}
	
	public VenmoGraph(final long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "VenmoGraph [nodes=" + nodes + ", lastTimestamp=" + lastTimestamp + ", duration=" + duration + "]";
	}
}
