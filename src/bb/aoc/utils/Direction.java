package bb.aoc.utils;

public enum Direction {
	LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;
	
	public static boolean isDiagonal(Direction d) {
		switch (d) {
		case DOWN_LEFT:
		case DOWN_RIGHT:
		case UP_LEFT:
		case UP_RIGHT:
			return true;
		default:
			return false;		
		}
	}
}
