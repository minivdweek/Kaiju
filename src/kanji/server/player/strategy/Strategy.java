package kanji.server.player.strategy;

import kanji.server.game.Board;
import kanji.server.game.Stone;

public interface Strategy {

	public String getName();
	public int determineMove(Board b, Stone m);

}
