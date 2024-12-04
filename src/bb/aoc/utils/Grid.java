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
	
	public void parseRow(String row) {
		char[] r = row.trim().toCharArray();
		rows.add(r);
	}
	
	public Optional<Character> get(int x, int y) {
		if (rows.size() <= y) {
			return Optional.empty();
		}
		char[] row = rows.get(y);
		if (row.length <= x) {
			return Optional.empty();
		}
		return Optional.of(row[x]);
	}
	
	// Get the char at the cursor
	public Optional<Character> get() {
		return get(cursor.x, cursor.y);
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
		List<LocationDirection> nbrs = new ArrayList<>();
		for (Direction d : Direction.values()) {
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
	
}
