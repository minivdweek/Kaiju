package kanji.server.game;

import java.util.Observable;

import kanji.server.player.Player;

public class Game extends Observable {
	private static final int PLAYER_AMOUNT = 2;
	private Board board;
	private Player[] players;
	private int currentPlayer;
	private int consPasses;

	
	public Game(Player player1, Player player2, int boardsize) {
		this.board = new Board(boardsize);
		this.players = new Player[PLAYER_AMOUNT];
		players[0] = player1;
		players[1] = player2;
		players[0].setStone("BLACK");
		players[1].setStone("WHITE");
		reset();
	}
	
	private void reset() {
        currentPlayer = 0;
        consPasses = 0;
        board.reset();
    }
	
	public void nextPlayer() {
		if (currentPlayer == 0) {
			currentPlayer = 1;
		} else {
			currentPlayer = 0;
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}
	
	public Player getCurrentPlayer() {
		return players[currentPlayer];
	}
	
	public Player getWinner() {
		Stone winner = board.determineWinner();
		if (winner == Stone.BLACK) {
			return players[0];
		} else if (winner == Stone.WHITE) {
			return players[1];
		} else {
			return null;
		}
	}
	
	public void pass() {
		if (!(this.currentPlayer == 1 && this.consPasses == 0)) {
			this.consPasses += 1;
		}
	}
	
	public void resetPass() {
		this.consPasses = 0;
	}
	
	public int getPassCount() {
		return consPasses;
	}
}
