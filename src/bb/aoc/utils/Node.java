package bb.aoc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A Star search node on a Grid of Locations
 * To use, override methods as needed
 * @author bbenyo
 *
 */
public class Node extends Location implements Comparable<Node> {
	static private Logger logger = Logger.getLogger(Node.class.getName());
	
	protected String label;
	
	protected int gScore = -1;
	protected int hScore = 0;
	protected int localCost = 1;
	
	protected Set<Node> neighbors;
	protected Node backPath;
		
	public Node(Location l1) {
		super(l1.getX(), l1.getY());
		this.label = l1.toString();
		neighbors = new HashSet<Node>();
		gScore = getWorstScore(l1);
	}
	
	public String getLabel() {
		return label;
	}
	
	public Location toLocation() {
		return new Location(this.getX(), this.getY());
	}
			
	public void setNeighbors(Set<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	// Heuristic, guess on the risk score for the path to the end
	//   We'll just do a right/down distance, and assume average risk of 5
	protected void computeHScore() {
		int dist = computeHeuristic();
		// Could calculate the actual risk for going this way, but this is fine, it's just a heuristic
		//   and we don't care about optimizing this search for speed today
		int h = (dist * 5);
		hScore = h;
	}

	// Compute the worst possible score from this node to initialize G
	// To be overridden by implementations, this is a default
	public int getWorstScore(Location l1) {
		return getGridSizeX() * getGridSizeY(); // Assume we have to visit every square
	}

	// Return a hueristic to compute the likely score from this node
	public int computeHeuristic() {
		int right = getGridSizeX() - x;
		int down = getGridSizeY() - y;
		return right + down;
	}
	
	// Get the maximum X value for a Location
	public int getGridSizeX() {
		return 10;
	}
	
	// Get the maximum Y value for a Location
	public int getGridSizeY() {
		return 10;
	}
	
	// The value/risk/cost of going to location cur
	public int getCost(Node cur) {
		return cur.localCost;
	}
	
	public int getF() {
		return gScore + hScore;
	}
	
	public int getG() {
		return gScore;
	}
	
	@Override
	public String toString() {
		return super.toString()+" g: "+gScore;
	}

	public int getBackPathLength() {
		int giveUp = getWorstScore(this);
		int steps = 0;
		Node current = this;
		while (current != null && steps < giveUp) {
			steps++;
			current = current.backPath;
		}
		return steps - 1; // Start doesn't count
	}
	
	public List<Node> getBackPath() {
		int giveUp = getWorstScore(this);
		List<Node> path = new ArrayList<>();
		Node current = this;
		int steps = 0;
		while (current != null && steps < giveUp) {
			path.add(current);
			current = current.backPath;
			steps++;
		}
		return path;
	}
		
	// Add neighbors based on where we can go from here.
	// Default implementation assumes a grid, and we can go up/down/left/right
	public void gatherNeighbors() {
		Location lleft = this.moveTo(Direction.LEFT, 1);
		Node nLeft = createNode(lleft, Direction.LEFT);
		List<Node> pNeighbors = new ArrayList<>();
		pNeighbors.add(nLeft);
		Location lup = this.moveTo(Direction.UP, 1);
		pNeighbors.add(createNode(lup, Direction.UP));
		Location lright = this.moveTo(Direction.RIGHT, 1);
		pNeighbors.add(createNode(lright, Direction.RIGHT));
		Location ldown = this.moveTo(Direction.DOWN, 1);
		pNeighbors.add(createNode(ldown, Direction.DOWN));
		
		for (Node n : pNeighbors) {
			if (isValidNode(n)) {
				addNeighbor(n);
			}
		}
	}
	
	protected boolean isBackwards(Node n) {
		if (backPath != null) {
			if (backPath.getX() == n.getX() &&
				backPath.getY() == n.getY()) {
				return true;
			}
		}
		return false;
	}
		
	// Superclasses to override
	public boolean isValidNode(Node n) {
		// Don't go backwards
		if (isBackwards(n)) {
			return false;
		}
		if (n.getX() > -1 && n.getX() < getGridSizeX() &&
			n.getY() > -1 && n.getY() < getGridSizeY()) {
			return true;
		}
		return false;
	}
	
	protected void addNeighbor(Node n) {
		Node uNode = nodes.get(n.getLabel());
		if (uNode != null) {
			neighbors.add(uNode);
		} else {
			uNode = n;
			neighbors.add(uNode);
			nodes.put(n.toString(), uNode);				
		}
	}
	
	// Subclasses may use direction, base version ignores it
	protected Node createNode(Location lup, Direction d) {
		Node n = new Node(lup);
		n.gScore = this.gScore + n.localCost;
		return n;
	}
			
	// Bottom right
	public boolean isEnd() {
		if ((getY() == getGridSizeY() - 1) &&
			(getX() == getGridSizeX() - 1)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(gScore, hScore, label, localCost);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return gScore == other.gScore && hScore == other.hScore && Objects.equals(label, other.label)
				&& localCost == other.localCost;
	}

	@Override
	public int compareTo(Node o2) {
		int f1 = getF();
		int f2 = o2.getF();
		if (f1 < f2) {
			return -1;
		} else if (f1 == f2) {
			return 0;
		}
		return 1;
	}
	
	public String printBackPath() {
		List<Node> path = getBackPath();
		StringBuffer sb = new StringBuffer(this.toString());
		sb.append("\n <- ");
		for (Node n : path) {
			sb.append(n.toString());
			sb.append("\n <- ");
		}
		return sb.toString();
	}
	
	protected static Map<String, Node> nodes = new HashMap<String, Node>();

	// A*
	static public Node search(Node start) {
		return search(start, null);
	}
	
	static public Node search(Node start, Node end) {
		List<Node> bests = searchAllMain(start, end, false);
		if (bests.isEmpty()) {
			return null;
		}
		return bests.get(0);
	}
	
	static public List<Node> searchAll(Node start) {
		return searchAllMain(start, null, true);
	}
	
	static public List<Node> searchAll(Node start, Node end) {
		return searchAllMain(start, end, true);
	}
	
	static public boolean isEnd(Node cur, Node end) {
		if (end != null) {
			return (end.getX() == cur.getX() &&
					end.getY() == cur.getY());			
		}
		return cur.isEnd();
	}
		
	static List<Node> searchAllMain(Node start, Node end, boolean all) {
		nodes.clear();
		List<Node> bestPaths = new ArrayList<>();
		
		PriorityQueue<Node> openQueue = new PriorityQueue<Node>();
		// Map for quick lookup
		Map<String, Node> openMap = new HashMap<>();
		openQueue.add(start);
		openMap.put(start.getLabel(), start);
		
		start.gScore = 0;
		
		Node bestEnd = null;
		
		long nodesSearched = 0;
		
		while (!openQueue.isEmpty()) {
			Node next = openQueue.remove();
			next.gatherNeighbors();
			nodesSearched++;
			logger.debug("Searching: "+next+" OpenSet: "+openQueue.size()+" Searched: "+nodesSearched);
			if (isEnd(next, end)) {
				logger.info("Found goal: "+next+" g: "+next.gScore);
				logger.info("Goal path: \n"+next.printBackPath());
				if (bestEnd == null) {
					bestEnd = next;
					bestPaths.add(next);
				} else if (bestEnd.gScore > next.gScore) {
					logger.info("Improvement over last goal: "+bestEnd.gScore);
					bestEnd = next;
					bestPaths.clear();
					bestPaths.add(next);
				} else if (bestEnd.gScore == next.gScore) {
					logger.info("Equal score to last goal: "+bestEnd.gScore);
					bestPaths.add(next);
				}
				continue;
			}
			// If we have a path to the end, but this node is already more expensive, we can stop here
			if (bestEnd != null && bestEnd.gScore < next.gScore) {
				logger.info("Pruning path already worse than the best end we found: "+next.gScore);
				continue;
			}
			for (Node n1 : next.neighbors) {
				int g = next.gScore + next.getCost(n1);
				// If the path from next to n1 is better than any other path we've found to n1:
				n1.gScore = g;
				Node old = openMap.get(n1.getLabel());
				if (old != null && (old.gScore < g ||
						(old.gScore == g && !all))) {
					// We can't prune nodes with equal g, since they may be different paths and we want all paths
					// Already have a better path (or equal path) to here, can skip
					logger.debug("  Skipping "+n1+" since "+old+" is better");
					continue;
				}
				n1.backPath = next;
				n1.computeHScore();
				openQueue.add(n1);
				openMap.put(n1.getLabel(), n1);
				logger.debug("  Adding Neighbor: "+n1);
			}
		}
		
		logger.info("A* complete, searched "+nodesSearched+" nodes, found "+bestPaths.size()+" best paths");
		return bestPaths;
	}
}