package com.example.jorisvandijk.kanjiapp;

import java.util.HashSet;
import java.util.Set;

/**.
 * The Board for a game of GO
 * @author joris.vandijk
 *
 */
public class Board {
	private final int dimension;
	private Intersection[][] intersections;
	private Set<String> oldBoards;
	private Set<Set<Intersection>> allFields;
	private int blackCaptives;
	private int whiteCaptives;

    
	/**
	 * Creates an empty Board.
	 */
    public Board(int dim) {
    	this.dimension = dim;
    	oldBoards = new HashSet<String>();
        setUpIntersections();
    }
    
    // ----- Getters ------------------------------------------------
    public int getDimension() {
    	return this.dimension;
    }
    
    public int[] getCoordinates(int index) {
    	int[] result = new int[2];
    	result[0] = index / dimension;
    	result[1] = index - ((index / dimension) * dimension);
    	return result;
    }
    
    public int getIndex(int row, int col) {
    	return col + (row * dimension);
    }
    
    /**.
     * checks if the given intersection exists
     * @param row
     * @param col
     * @return true if the coordinates are valid
     */
    public boolean isIntersection(int row, int col) {
        return 0 <= row && row < dimension && 0 <= col && col < dimension;
    }
    
    /**.
     * Gets the Intersection of a chosen field at these coordinates
     * @param row
     * @param col
     * @return an Intersection
     */
    public Intersection getIntersection(int row, int col) {
        return this.intersections[row][col];
    }
    
    /**.
     * check if the chosen field is not yet claimed
     * @param row
     * @param col
     * @return true if and only if this field is Stone.EMPTY
     */
    public boolean isEmptyIntersection(int row, int col) {
        return isIntersection(row, col) && intersections[row][col].getValue() == Stone.EMPTY;
    }
    
    /**.
     * Checks the move which the player wants to make. returns true if it is legal.
     * Also checks for the suicide rule.
     * @param row
     * @param col
     * @param m the stone to check the legality for
     * @return true is the move is legal
     */
    public boolean moveIsLegal(int row, int col, Stone m) {
    	if (this.isEmptyIntersection(row, col)) {
    		Board copy = deepCopy(this);
    		copy.setIntersection(row, col, m);
    		copy.checkStones(m.other());
    		if (copy.fieldHasLiberties(getField(copy.getIntersection(row, col)), m) && 
    						!koRule(copy.getStringRepresentation())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks the move which the player wants to make. returns true if it is legal.
     * Also checks for the suicide rule.
     * @param index
     * @param m the stone to check the legality for
     * @return true if the move is legal
     */
    public boolean moveIsLegal(int index, Stone m) {
    	int[] coord = getCoordinates(index);
    	return moveIsLegal(coord[0], coord[1], m);
    }
    
    /**.
     * checks if the current Board is full
     * @return true if no intersection is empty
     */
    public boolean isFull() {
    	for (Intersection[] i : intersections) {
    		for (Intersection j : i) {
    			if (j.getValue() == Stone.EMPTY) {
        			return false;
        		}	
    		}
    	}
        return true;
    }
    
    /**.
     * counts the amount of stones each player has on the board,
     * returns the color of the most common Stone
     * @return the Stone of the winner, if any:
     * Stone.WHITE || Stone.Black || Stone.EMPTY
     */
    public Stone determineWinner() {
    	int black = 0;
    	int white = 0;
    	for (Intersection[] i : intersections) {
    		for (Intersection j : i) {
    			if (j.getValue() == Stone.BLACK) {
        			black++;
        		} else if (j.getValue() == Stone.WHITE) {
        			white++;
        		}
    		}
    	}
    	black += getTerritorySize(Stone.BLACK);
    	white += getTerritorySize(Stone.WHITE);
    	white -= whiteCaptives;
    	black -= blackCaptives;
    	if (black > white) {
    		return Stone.BLACK;
    	} else if (black < white) {
    		return Stone.WHITE;
    	} else {
    		return Stone.EMPTY;
    	}
    } 
    
    /**
     * Check all fields. If the field is empty and has no connections 
     * with a stone of the opponents color, this field is a territory
     * of the chosen color. 
     * @param stone the stone to check the territories for
     * @return the combined size of all territories of this stone combined
     */
    public int getTerritorySize(Stone stone) {
    	int size = 0;
    	for (Set<Intersection> i : allFields) {
    		if (isTerritoryOf(i, stone)) {
    			size += i.size();
    		}
    	}
    	return size;
    }
    
    /**.
     * Check if this field is a territory of a given stone
     * @param i the field to check
     * @param stone the color of the stone to check for
     * @return true if this field is empty, and surrounded only with 
     * stones of the chosen color
     */
    public boolean isTerritoryOf(Set<Intersection> i, Stone stone) {
    	for (Intersection j : i) {
    		if (j.getValue() == Stone.EMPTY) {
    			if ((j.getUp() != null && j.getUp().getValue() == stone.other()) ||
    							(j.getRight() != null && 
    								j.getRight().getValue() == stone.other()) ||
    							(j.getDown() != null && j.getDown().getValue() == stone.other()) ||
    							(j.getLeft() != null && j.getLeft().getValue() == stone.other())) {
    				return false;
    			}
    		} else {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Get a String representation of this board, in a EEEEEWWWWWBBB...
     * format.
     * @return a string containing all stones on this board.
     */
    public String getStringRepresentation() {
    	String rep = "";
    	for (int i = 0; i <= dimension - 1; i++) {
    		for (int j = 0; j <= dimension - 1; j++) {
    			if (getIntersection(i, j).getValue() == Stone.BLACK) {
    				rep += "B";
    			} else if (getIntersection(i, j).getValue() == Stone.WHITE) {
    				rep += "W";
    			} else {
    				rep += "E"; 
    			}
    		}
    	}
    	return rep;
    }
    
    /**.
     * Get a String representation of this board, including all captives
     * since the beginning of the game
     * @return
     */
    public String getStringInclCaptives() {
    	return getStringRepresentation() + " " + blackCaptives + " " + whiteCaptives;
    }
    
    /**
     * if the board has never occurred, returns false. Returns true if the 
     * situation has been seen before.
     * @return true is the situation has happened before, 
     * false if it has not.
     */
    public boolean koRule(String code) {
    	if (oldBoards.contains(code)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**.
     * try to copy the board using .clone()
     * @return a clone of this Board
     */
    public Board deepCopy(Board b) {
    	Board result = new Board(b.dimension);
    	for (int i = 0; i <= dimension - 1; i++) {
    		for (int j = 0; j <= dimension - 1; j++) {
    			result.setIntersection(i, j, getIntersection(i, j).getValue());
    		}
    	}
    	return result;
    }
    
    /**.
     * Iterates all Intersections, searching for fields
     * @return a Set of Sets of Intersections
     * (so I heard you like Sets...)
     */
    public Set<Set<Intersection>> getFields() {
    	Set<Set<Intersection>> fields = new HashSet<>();
    	for (Intersection[] i : intersections) {
    		for (Intersection j : i) {
    			if (!j.isChecked()) {
    				fields.add(getField(j));
    			}
    		}
    	}
    	return fields;
    }
    
    /**.
     * Given an Intersection, returns the field it belongs to and checks 
     * all fields in that Set
     * @param i
     * @return a Set of Intersections
     */
    public Set<Intersection> getField(Intersection i) {
    	i.check();
    	Set<Intersection> field = new HashSet<>();
    	Set<Intersection> c = getConnections(i);
    	field.add(i);
    	if (c.size() != 0) {
    		for (Intersection j : c) {
    			if (!j.isChecked()) {
    				field.addAll(getField(j));
    			}
    		}
    	}
    	return field;
    }
    
    /**
     * creates a Set containing the connections of Intersection i. Two Intersections are
     * considered connected if they are adjacent and have the same colored Stone
     * @param i
     * @return a Set of Intersections
     */
    public Set<Intersection> getConnections(Intersection i) {
		Set<Intersection> connections = new HashSet<>();
		if (i.getUp() != null && i.getValue() == i.getUp().getValue()) {
			connections.add(i.getUp());
		}
		if (i.getRight() != null && i.getValue() == i.getRight().getValue()) {
			connections.add(i.getRight());
		}
		if (i.getDown() != null && i.getValue() == i.getDown().getValue()) {
			connections.add(i.getDown());
		}
		if (i.getLeft() != null && i.getValue() == i.getLeft().getValue()) {
			connections.add(i.getLeft());
		}
		return connections;
	}
    
    /**.
     * Checks whether or not a field has liberties
     * @param si the field; a Set of Intersections
     * @return true if and only if the field has at least one liberty
     */
    public boolean fieldHasLiberties(Set<Intersection> si, Stone stone) {
    	for (Intersection i : si) {
    		if (i.getValue() != stone || hasLiberties(i)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**.
     * checks if an Intersection has liberties
     * @param i
     * @return true if and only if the field has at least one liberty
     */
    public boolean hasLiberties(Intersection j) {
    	return (j.getUp() != null && j.getUp().getValue() == Stone.EMPTY) ||
				(j.getRight() != null && j.getRight().getValue() == Stone.EMPTY) ||
				(j.getDown() != null && j.getDown().getValue() == Stone.EMPTY) ||
				(j.getLeft() != null && j.getLeft().getValue() == Stone.EMPTY);
    }
    
    // ----- Commands --------------------------------------------
    
    /**.
     * Empties the intersections, so they all refer to Stone.EMPTY
     */
    public void reset() {
    	for (Intersection[] i : intersections) {
    		for (Intersection j : i) {
    			j.setValue(Stone.EMPTY);
    			j.uncheck();
    		}
    	}
    	blackCaptives = 0;
    	whiteCaptives = 0;
    }
    
    public void setIntersection(int i, Stone m) {
    	setIntersection(getCoordinates(i)[0], getCoordinates(i)[1], m);
    }
    
    /**.
     * Sets the Stone at an intersection to 'm'
     * @param row
     * @param col
     * @param m the value of the stone to be placed
     */
    public void setIntersection(int row, int col, Stone m) {
    	this.intersections[row][col].setValue(m);
    	updateFields();
    	checkStones(m.other());
    	this.updateOldBoards(this);
    }
    
    /**.
     * checks all fields on the board and removes them if it finds 
     * fields without liberties
     * increments the count of the captives by the amount of stones removed
     * @param stone the color of the fields you want to check 
     * and remove if they have no liberties
     */
    public void checkStones(Stone stone) {
    	this.updateFields();
    	if (allFields.size() > 1) {
    		for (Set<Intersection> si : allFields) {
    			if (!fieldHasLiberties(si, stone)) {
    				for (Intersection i : si) {
    					i.setValue(Stone.EMPTY);
    					if (stone.equals(Stone.BLACK)) {
    						blackCaptives += 1;
    					} else {
    						whiteCaptives += 1;
    					}
    				}
    			}
    		}
    		for (Intersection[] i : intersections) {
    			for (Intersection j : i) {
    				j.uncheck();
    			}
    		}
    	}
    }



    
    /**
     * Sets up the intersections, and links them to
     * each other.
     */
    public void setUpIntersections() {
    	intersections = new Intersection[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
        	for (int j = 0; j < dimension; j++) {
        		intersections[i][j] = new Intersection();
        	}
        }
        for (int i = 0; i < dimension; i++) {
        	for (int j = 0; j < dimension; j++) {
        		if (i > 0) {
        			intersections[i][j].setUp(intersections[i - 1][j]);
        		} else {
        			intersections[i][j].setUp(null);
        		}
        		if (i < dimension - 1) {
        			intersections[i][j].setDown(intersections[i + 1][j]);
        		} else {
        			intersections[i][j].setDown(null);
        		}
        		if (j > 0) {
        			intersections[i][j].setLeft(intersections[i][j - 1]);
        		} else {
        			intersections[i][j].setLeft(null);
        		}
        		if (j < dimension - 1) {
        			intersections[i][j].setRight(intersections[i][j + 1]);
        		} else {
        			intersections[i][j].setRight(null);
        		}
        	}
        }
        reset();
    }
    
    /**.
     * sets allFields to the current Set of Sets
     */
    public void updateFields() {
    	for (Intersection[] i : intersections) {
    		for (Intersection j : i) {
    			j.uncheck();
    		}
    	}
    	this.allFields = this.getFields();
    }
    
    /**.
     * add the String representation of the current board to the Set of old Boards
     */
    public void updateOldBoards(Board b) {
    	this.oldBoards.add(b.getStringRepresentation());
    }
    
    public void recreateBoardFromString(String str) {
    	String board = str.split(" ")[0].trim();
    	this.whiteCaptives = Integer.parseInt(str.split(" ")[2].trim());
    	this.blackCaptives = Integer.parseInt(str.split(" ")[1].trim());
    	String is;
    	for (int i = 0; i < board.length(); i++) {
    		is = String.valueOf(board.charAt(i));
    		if (is.equals("W")) {
    			this.setIntersection(i, Stone.WHITE);
    		} else if (is.equals("B")) {
    			this.setIntersection(i, Stone.BLACK);
    		} else {
    			this.setIntersection(i, Stone.WHITE);
    		}
    	}
    }
    
    
}
