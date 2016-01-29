package kanji.server.player.strategy;

import kanji.server.game.Board;
import kanji.server.game.Stone;

public class RandomStrategy implements Strategy {

	private String name = "Random";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int determineMove(Board b, Stone m) {		
		return chooserandomnumber(b);
	}
	
	public int chooserandomnumber(Board b) {
		int randomnumber = (int) Math.round(Math.random() * (b.getDimension() * b.getDimension()));
//		int[] rowcol = b.getCoordinates(randomnumber);
//		if (b.isEmptyIntersection(rowcol[0], rowcol[1])) {
//			return randomnumber;
//		} else {
//			randomnumber = chooserandomnumber(b);
//		}
		return randomnumber;
	}
}
