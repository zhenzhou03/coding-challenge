import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VenmoGraph {
	private final Map<String, Map<String, Long>> nodes = new HashMap<>();//<actor, <target, timestamp>>

	private volatile long lastTimestamp;//unit in second
	private final List<Double> sampledMedians = new ArrayList<>();
	
	private final long duration;
	
	/**
	 * addPayment
	 * 
	 * @param actor String
	 * @param target String
	 * @param createdTime Date
	 * @return boolean, false if graph is not changed
	 */
	public boolean addPayment(final String actor, final String target, final Date createdTime) {
		if (actor == null || target == null || createdTime == null || actor.equals(target))
			return false;
		
		//1. got timestamp
		long timestamp = createdTime.getTime() / 1000;
		if (timestamp + duration < lastTimestamp) {
			sampledMedians.add(sampledMedians.get(sampledMedians.size() - 1));
			return false;
		}
		
		//2. update graph
		Map<String, Long> links = nodes.get(actor);
		if (links == null) {
			links = new HashMap<String, Long>();
			nodes.put(actor, links);
		}
		boolean result = false;
		Long ct = links.get(target);
		if (ct == null) {
			links.put(target, timestamp);
			result = true;
		} else if (ct < timestamp) {
			links.put(target, timestamp);
		}

		//3. remove old link
		if (timestamp > lastTimestamp) {
			lastTimestamp = timestamp;
			for (Map.Entry<String, Map<String, Long>> node : nodes.entrySet()) {
				if (actor.equals(node.getKey()))
					continue;

				Iterator<Map.Entry<String, Long>> it = node.getValue().entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<String, Long> link = it.next();
					if (link.getValue() + duration < lastTimestamp)
						it.remove();
				}
			}
		}
		
		//4. add smaple
		sampledMedians.add(getMedian());
		
		return result;
	}
	
	public VenmoGraph(final long duration) {
		this.duration = duration;
	}
	
	public List<Double> getSampleMedians() {
		return sampledMedians;
	}

	/**
	 * getMedian
	 * 
	 * @return double
	 */
	private double getMedian() {
		if (nodes.isEmpty())
			return Double.NaN;
		
		Map<String, Set<String>> reverseLinks = new HashMap<>();
		for (Map.Entry<String, Map<String, Long>> node : nodes.entrySet()) {
			for (String target : node.getValue().keySet()) {
				Set<String> reverseLing = reverseLinks.get(target);
				if (reverseLing == null) {
					reverseLing = new HashSet<String>();
					reverseLinks.put(target, reverseLing);
				}
				reverseLing.add(node.getKey());
			}
		}
		
		Map<String, Set<String>> allLinks = new HashMap<>(reverseLinks);
		for (Map.Entry<String, Map<String, Long>> node : nodes.entrySet()) {
			Set<String> links = allLinks.get(node.getKey());
			if (links == null){
				links = new HashSet<String>();
				allLinks.put(node.getKey(), links);
			}
			links.addAll(node.getValue().keySet());
		}		
		
		List<Integer> linkSizes = new ArrayList<>();
		for (Set<String> links : allLinks.values()) {
			if (!links.isEmpty())
				linkSizes.add(links.size());
		}
		
		
		Collections.sort(linkSizes);
		int middle = linkSizes.size() / 2;
		if (linkSizes.size() % 2 == 0) {
			return (double)(linkSizes.get(middle - 1) + linkSizes.get(middle)) / 2;
		} else {
			return linkSizes.get(middle);
		}
	}

	@Override
	public String toString() {
		return "VenmoGraph [nodes=" + nodes + ", lastTimestamp=" + lastTimestamp + ", sampledMedians=" + sampledMedians
				+ ", duration=" + duration + "]";
	}
}
