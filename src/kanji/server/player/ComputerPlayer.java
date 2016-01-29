package kanji.server.player;

import java.util.Observable;
import java.util.Observer;

import kanji.server.ClientHandler;
import kanji.server.game.Board;
import kanji.server.game.Game;
import kanji.server.player.strategy.RandomStrategy;
import kanji.server.player.strategy.Strategy;

public class ComputerPlayer extends Player implements Observer {
	private Strategy strategy;
	private Game game;
	private ClientHandler handler;
	
	@Override
	public int determineMove(Board board) {
		return strategy.determineMove(board, this.getStone());
	}
	
	public ComputerPlayer(String name) {
		super(name);
		this.strategy = new RandomStrategy();	
	}
	
	public void setObservable(Game obsgame) {
		this.game = obsgame;
		this.game.addObserver(this);
	}
	
	public void setHandler(ClientHandler ch) {
		this.handler = ch;
	}

	@Override
	public void update(Observable o, Object arg) {
		Game ogame = (Game) o;
		if (ogame.getCurrentPlayer() == this) {
			int choice = -1;
			while (!ogame.getBoard().moveIsLegal(choice, this.getStone())) {
				choice = this.determineMove(ogame.getBoard());
			}
			makeMove(ogame.getBoard(), choice);
			int[] choicecoord = ogame.getBoard().getCoordinates(choice);
			ogame.resetPass();
			ogame.nextPlayer();
			handler.sendCommands("MOVE " + getStone().toString() 
							+ " " + choicecoord[0] + " " + choicecoord[1]);
		} 
	}

}
