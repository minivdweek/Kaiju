package kanji.server.player;

import kanji.server.game.*;

public abstract class Player {
	// ----- Instance Variables ---------------------------------
	private String name;
	private Stone stone;
	
	// ----- Constructor ----------------------------------------	
	/**.
	 * Creates a new Player object
	 * @param name the name of this Player
	 * @param Stone this Players's Stone
	 */
	public Player(String name) {
		this.name = name;
	}
	
	// ----- Getters --------------------------------------------
	/**
	 * @return the Players name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return this Players Stone
	 */
	public Stone getStone() {
		return this.stone;
	}
	
	/**
     * Determines the field for the next move.
     * 
     * @param board the current game board
     * @return the player's choice
     */
	public abstract int determineMove(Board board);
	
	// ----- Commands -------------------------------------------
	
	
	/**
	 * Make a move on the current Board.
	 * @param board the current Board
	 */
	public void makeMove(Board board, int choice) {
		board.setIntersection(choice, this.stone);
    }
	
	/**.
	 * Sets this players Stone color
	 * @param stone
	 */
	public void setStone(String color) {
		if (color.equals("WHITE")) {
			this.stone = Stone.WHITE;
		} else {
			this.stone = Stone.BLACK;
		}
	}
}
