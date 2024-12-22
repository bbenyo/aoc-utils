package bb.aoc.utils;

import java.util.Optional;

import org.apache.log4j.Logger;


public class GridSearch {
	
	static private Logger logger = Logger.getLogger(GridSearch.class.getName());
	
	Grid map;
	
	Location startLoc;
	Location endLoc;
	
	public GridSearch(Grid g, char start, char end) {
		this.map = g;
		Optional<Location> startO = map.scan(start);
		if (startO.isEmpty()) {
			logger.error("Can't find Start location "+start);
			return;
		}
		map.setCursor(new Location(0,0));
		Optional<Location> endO = map.scan(end);
		if (endO.isEmpty()) {
			logger.error("Can't find End location "+end);
			return;
		}
		startLoc = startO.get();
		endLoc = endO.get();
	}
	
	public class GridNode extends Node {
		public GridNode(Location l1) {
			super(l1);
		}
		
		@Override
		public Node createNode(Location l) {
			return new GridNode(l);
		}
		
		@Override
		public boolean isValidNode(Node n) {
			if (isBackwards(n)) {
				return false;
			}
			Optional<Character> m = map.get(n);
			if (m.isEmpty() || m.get() == '#') {
				return false;
			}
			Node uNode = nodes.get(n.getLabel());
			if (uNode != null && uNode.getG() <= n.getG()) {
				return false;
			}
			return true;
		}
			
		@Override
		public int getGridSizeX() {
			return map.getColumnCount(0);
		}
		
		@Override
		public int getGridSizeY() {
			return map.getRowCount();
		}
		
		@Override
		protected void computeHScore() {
			hScore = computeHeuristic();
		}
		
		@Override
		public boolean isEnd() {
			if ((getY() == endLoc.getY()) &&
				(getX() == endLoc.getX())) {
				return true;
			}
			return false;
		}
	}
	
	public GridNode search() {
		GridNode startNode = new GridNode(startLoc);
		GridNode end = (GridNode)Node.search(startNode);
		return end;
	}
}
