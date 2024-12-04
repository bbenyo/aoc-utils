package bb.aoc.utils;

import java.util.Objects;

public class LocationDirection extends Location {

	Direction dir;
	
	public LocationDirection(int x, int y) {
		super(x,y);
		dir = null;
	}
	
	public LocationDirection(Location l, Direction d) {
		super(l.x, l.y);
		dir = d;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dir);
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
		LocationDirection other = (LocationDirection) obj;
		return dir == other.dir;
	}
	
}
