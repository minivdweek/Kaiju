package com.example.jorisvandijk.kanjiapp;

/**
 * Represents the color of the stone on an intersection.
 * is EMPTY if there is no stone on the intersection.
 * @author joris.vandijk
 *
 */
public enum Stone {
	WHITE, BLACK, EMPTY;
	
	/**
	 * returns the opposite value of this stone.
	 * If this stone is white, returns black
	 * and vice versa. If this stone is Empty,
	 * it stays empty.
	 * @return WHITE, BLACK of EMPTY
	 */
	public Stone other() {
        if (this == WHITE) {
            return BLACK;
        } else if (this == BLACK) {
            return WHITE;
        } else {
            return EMPTY;
        }
    }
	
	public static Stone toStone(String s) {
		if (s.equals("WHITE")) {
			return Stone.WHITE;
		} else if (s.equals("BLACK")) {
			return Stone.BLACK;
		} else {
			return Stone.EMPTY;
		}
	}
}
