package kanji.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kanji.server.game.Board;
import kanji.server.game.Game;
import kanji.server.player.HumanPlayer;
import kanji.server.player.Player;

public class GameTest {
	private Game game;
	private Board board;
	private Player p1;
	private Player p2;

	@Before
	public void setUp() throws Exception {
		this.p1 = new HumanPlayer("p1");
		this.p2 = new HumanPlayer("p2");
		this.game = new Game(this.p1, this.p2, 9);
		this.board = game.getBoard();
	}

	@Test
	public void testGetWinner() {
		p1.makeMove(board, 0);
		board.updateFields();
		assertEquals(game.getWinner(), p1);
		p2.makeMove(board, 1);
		p2.makeMove(board, 3);
		board.updateFields();
		assertEquals(game.getWinner(), p2);
		p1.makeMove(board, 4);
		board.updateFields();
		assertNull(game.getWinner());
	}
	
	@Test
	public void testPassCount() {
		assertEquals(game.getPassCount(), 0);
		game.nextPlayer();
		game.pass();
		assertEquals(game.getPassCount(), 0);
		game.pass();
		assertEquals(game.getPassCount(), 0);
		game.nextPlayer();
		game.pass();
		assertEquals(game.getPassCount(), 1);
		game.nextPlayer();
		game.pass();
		assertEquals(game.getPassCount(), 2);
		game.resetPass();
		assertEquals(game.getPassCount(), 0);
	}
	
	@Test
	public void testPLayers() {
		assertEquals(game.getCurrentPlayer(), p1);
		game.nextPlayer();
		assertEquals(game.getCurrentPlayer(), p2);
		game.nextPlayer();
		assertEquals(game.getCurrentPlayer(), p1);
	}
	

}
