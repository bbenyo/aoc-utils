package bb.aoc.utils;

import java.util.Objects;

public class LocationFacing extends Location {
		
	protected Direction facing;

	public LocationFacing(int x, int y) {
		super(x, y);
		facing = Direction.UP;
	}
	
	public LocationFacing(LocationFacing clone) {
		super(clone.getX(), clone.getY());
		facing = clone.facing;
	}


	public Direction getFacing() {
		return facing;
	}
	
	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	public String toString() {
		return x+","+y+" "+getFacingChar();
	}
	
	public char getFacingChar() {
		switch (facing) {
		case UP: return '^';
		case DOWN: return 'v';
		case LEFT: return '<';
		case RIGHT: return '>';
		default:
			return '?';
		}
	}
	
	public void turnRight() {
		switch (facing) {
		case UP: facing = Direction.RIGHT; break;
		case DOWN: facing = Direction.LEFT; break;
		case LEFT: facing = Direction.UP; break;
		case RIGHT: facing = Direction.DOWN; break;
		default:
			break;
		}
	}
	
	// Move forward 1 step.  If we hit the top/left/bottom/right rows:
	//   If wrap is true, we wrap around, if false, we're stuck
	public boolean forward(Location topLeft, Location bottomRight, boolean wrap) {
		int nx = x;
		int ny = y;
		boolean fail = false;
		switch (facing) {
		case DOWN:
			ny ++; 
			if (ny > bottomRight.getY()) {
				if (wrap) {
					ny = topLeft.getY();
				} else {
					ny = y;
					fail = true;
				}
			}
			break;
		case LEFT:
			nx --; 
			if (nx < topLeft.getX()) {
				if (wrap) {
					nx = bottomRight.getX();
				} else {
					nx = x;
					fail = true;
				}
			}
			break;
		case RIGHT:
			nx ++; 
			if (nx > bottomRight.getX()) {
				if (wrap) {
					nx = topLeft.getX();
				} else {
					nx = x;
					fail = true;
				}
			}
			break;
		case UP:
			ny --; 
			if (ny < topLeft.getY()) {
				if (wrap) {
					ny = bottomRight.getY();
				} else {
					ny = y;
					fail = true;
				}
			}
			break;
		default:
			break;		
		}
		setX(nx);
		setY(ny);
		return !fail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(facing);
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
		LocationFacing other = (LocationFacing) obj;
		return facing == other.facing;
	}

}
