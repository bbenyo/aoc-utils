package bb.aoc.utils;

import java.util.Optional;

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
	
	public static Direction turnRight90(Direction d) {
		switch(d) {
		case DOWN:
			return LEFT;
		case DOWN_LEFT:
			return UP_LEFT;
		case DOWN_RIGHT:
			return DOWN_LEFT;
		case LEFT:
			return UP;
		case RIGHT:
			return DOWN;
		case UP:
			return RIGHT;
		case UP_LEFT:
			return UP_RIGHT;
		case UP_RIGHT:
			return DOWN_RIGHT;
		default:
			break;		
		}
		return d;
	}
	
	public static Direction turnLeft90(Direction d) {
		switch(d) {
		case DOWN:
			return RIGHT;
		case DOWN_LEFT:
			return DOWN_RIGHT;
		case DOWN_RIGHT:
			return UP_RIGHT;
		case LEFT:
			return DOWN;
		case RIGHT:
			return UP;
		case UP:
			return LEFT;
		case UP_LEFT:
			return DOWN_LEFT;
		case UP_RIGHT:
			return UP_LEFT;
		default:
			break;		
		}
		return d;
	}
	
	public static Direction opposite(Direction d) {
		switch(d) {
		case DOWN:
			return UP;
		case DOWN_LEFT:
			return UP_RIGHT;
		case DOWN_RIGHT:
			return UP_LEFT;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case UP:
			return DOWN;
		case UP_LEFT:
			return DOWN_RIGHT;
		case UP_RIGHT:
			return DOWN_LEFT;
		default:
			break;		
		}
		return d;
	}
	
	public static Optional<Direction> fromChar(char c) {
		switch(c) {
		case '>' : return Optional.of(RIGHT);
		case '<' : return Optional.of(LEFT);
		case 'v' : return Optional.of(DOWN);
		case '^' : return Optional.of(UP);
		default:
			return Optional.empty();
		}
	}
}
