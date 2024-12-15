package bb.aoc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Grid {
	
	List<char[]> rows;
	Location cursor;
	
	public Grid() {
		rows = new ArrayList<>();
		cursor = new Location(0,0);
	}
	
	public void setCursor(Location l) {
		cursor = l;
	}
	
	public void initialize(int x, int y, char def) {
		for (int i=0; i<y; ++i) {
			char[] row = new char[x];
			for (int j=0; j<x; ++j) {
				row[j] = def;
			}
			addRow(row);
		}
	}
	
	public void parseRow(String row) {
		char[] r = row.trim().toCharArray();
		rows.add(r);
	}
	
	public void addRow(char[] row) {
		rows.add(row);
	}
	
	public int getRowCount() {
		return rows.size();
	}
	
	public int getColumnCount(int r) {
		if (r < 0 || rows.size() <= r) {
			return 0;
		}
		char[] row = rows.get(r);
		return row.length;
	}
	
	public Optional<Character> get(int x, int y) {
		if (y < 0 || rows.size() <= y) {
			return Optional.empty();
		}
		char[] row = rows.get(y);
		if (x < 0 || row.length <= x) {
			return Optional.empty();
		}
		return Optional.of(row[x]);
	}
	
	// Get the char at the cursor
	public Optional<Character> get() {
		return get(cursor.x, cursor.y);
	}
	
	public Optional<Character> get(Location l) {
		return get(l.x, l.y);
	}
	
	public Optional<Character> get(Optional<Location> l) {
		if (l.isEmpty()) {
			return Optional.empty();
		}
		return get(l.get());
	}
	
	public void set(int x, int y, char c) {
		if (y < 0 || rows.size() <= y) {
			return;
		}
		char[] row = rows.get(y);
		if (x < 0 || row.length <= x) {
			return;
		}
		row[x] = c;
	}
	
	public void set(Location l, char c) {
		set(l.x, l.y, c);
	}
	// Move cursor right+down, return the true if the next cursor location is good 
	//    or empty if at the end of the grid
	public boolean nextRightDown() {
		if (rows.size() <= cursor.y) {
			return false;
		}
		char[] row = rows.get(cursor.y);
		if (row.length > (cursor.x+1)) {
			cursor.x = cursor.x + 1;
			return true;
		}
		cursor.y = cursor.y + 1;
		cursor.x = 0;
		if (rows.size() <= cursor.y) {
			return false;
			// End of grid
		}
		return true;	
	}
	
	// Search the grid left, down for the next c
	public Optional<Location> scan(char c) {
		boolean keepLooking = true;
		while (keepLooking) {
			if (!nextRightDown()) {
				keepLooking = false;
				break;
			}
			Optional<Character> cVal = get();
			if (cVal.isEmpty()) {
				keepLooking = false;
			} else if (cVal.get().equals(c)) {
				return Optional.of(cursor);
			}	
		}
		return Optional.empty();
	}
	
	public boolean onGrid(Location l) {
		if (l.y < 0 || l.y >= rows.size()) {
			return false;
		}
		char[] row = rows.get(l.y);
		if (l.x < 0 || l.x >= row.length) {
			return false;
		}
		return true;
	}
	
	// return Cursor location up+left of cursor, or empty if off grid
	public Optional<Location> upLeft(Location c) {
		Location c2 = new Location(c.x - 1, c.y - 1);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}
	
	public Optional<Location> downLeft(Location c) {
		Location c2 = new Location(c.x - 1, c.y + 1);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}

	public Optional<Location> upRight(Location c) {
		Location c2 = new Location(c.x + 1, c.y - 1);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}
	
	public Optional<Location> downRight(Location c) {
		Location c2 = new Location(c.x + 1, c.y + 1);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}
	
	public Optional<Location> left(Location c) {
		Location c2 = new Location(c.x - 1, c.y);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}
	
	public Optional<Location> up(Location c) {
		Location c2 = new Location(c.x, c.y - 1);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}

	public Optional<Location> right(Location c) {
		Location c2 = new Location(c.x + 1, c.y);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}

	public Optional<Location> down(Location c) {
		Location c2 = new Location(c.x, c.y + 1);
		if (onGrid(c2)) {
			return Optional.of(c2);
		}
		return Optional.empty();
	}
	
	public Optional<Location> getLocAt(Location loc, Direction d) {
		switch (d) {
		case DOWN:
			return down(loc);
		case DOWN_LEFT:
			return downLeft(loc);
		case DOWN_RIGHT:
			return downRight(loc);
		case LEFT:
			return left(loc);
		case RIGHT:
			return right(loc);
		case UP:
			return up(loc);
		case UP_LEFT:
			return upLeft(loc);
		case UP_RIGHT:
			return upRight(loc);
		default:
			break;
		}
		return Optional.empty();
	}

	// Find all neighboring locations with value = c
	public List<LocationDirection> findNeighbors(char c, Location loc) {
		return findNeighbors(c, loc, true);
	}
	
	public List<LocationDirection> findNeighbors(char c, Location loc, boolean diagonal) {
		List<LocationDirection> nbrs = new ArrayList<>();
		for (Direction d : Direction.values()) {
			if (Direction.isDiagonal(d) && !diagonal) {
				continue;
			}
			Optional<Location> nbr = getLocAt(loc, d);
			if (nbr.isPresent()) {
				Location nLoc = nbr.get();
				Optional<Character> nVal = this.get(nLoc.x, nLoc.y);
				if (nVal.isPresent() && nVal.get().equals(c)) {
					nbrs.add(new LocationDirection(nLoc, d));
				}				
			}
		}
		return nbrs;
	}
	
	/** Get a sub-grid centered a loc with offset w (x-y to x+w)
	 *  Offset of 1 gets you a 3x3 grid (x-1 to x+1)
	 *   Offset of 2 gets you a 5x5 grid, etc.
	 *  Return empty if we can't make a grid at this location because we go off edge
	 **/ 
	public Optional<Grid> createSubGrid(Location loc, int offset) {
		if (get(loc).isEmpty()) {
			// loc is off the grid!
			return Optional.empty();
		}
		Location c2 = new Location(loc);
		c2.moveUp(offset);
		c2.moveLeft(offset);
		Grid subGrid = new Grid();
		int sgSize = (offset * 2) + 1;
		
		for (int y=c2.y; y<c2.y + sgSize; ++y) {
			char[] row = new char[sgSize];
			for (int x=c2.x; x<c2.x + sgSize; ++x) {
				Optional<Character> nextC = get(x, y);
				if (nextC.isEmpty()) {
					// Off grid!
					return Optional.empty();
				}
				row[x] = nextC.get();
			}
			subGrid.addRow(row);			
		}
		return Optional.of(subGrid);
	}
	
	public int count(String cs) {
		int count = 0;
		for (int y = 0; y<rows.size(); ++y) {
			char[] row = rows.get(y);
			for (char cell : row) {
				if (cs.indexOf(cell) > -1) {
					count++;
				}
			}
		}
		return count;
	}
	
	public void reset(String cs, char base) {
		for (int y = 0; y<rows.size(); ++y) {
			char[] row = rows.get(y);
			for (int x = 0; x<row.length; ++x) {
				char cell = row[x];
				if (cs.indexOf(cell) > -1) {
					row[x] = '.';
				}
			}
		}
	}
	
	public String print() {
		StringBuffer sb = new StringBuffer();
		sb.append(System.lineSeparator());
		for (int y = 0; y<rows.size(); ++y) {
			char[] row = rows.get(y);
			sb.append(row);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
