package kanji.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kanji.server.game.Board;
import kanji.server.game.Stone;

public class BoardTest {
	private Board kanji;

	@Before
	public void setUp() throws Exception {
		kanji = new Board(9);
	}

	@Test
	public void testIsIntersection() {
		assertFalse(kanji.isIntersection(9, 9));
		assertFalse(kanji.isIntersection(-1, -1));
		assertFalse(kanji.isIntersection(0, -1));
		assertFalse(kanji.isIntersection(-1, 0));
		assertFalse(kanji.isIntersection(8, 9));
		assertFalse(kanji.isIntersection(9, 8));
		assertTrue(kanji.isIntersection(0, 0));
		assertTrue(kanji.isIntersection(8, 8));
	}
	
	@Test
	public void testIsEmptyIntersection() {
		kanji.setIntersection(5, 5, Stone.WHITE);
		assertTrue(kanji.isEmptyIntersection(4, 4));
		assertFalse(kanji.isEmptyIntersection(5, 5));
		assertFalse(kanji.isEmptyIntersection(-1, 0));
		assertFalse(kanji.isEmptyIntersection(5, 9));
	}
	
	@Test
	public void testGetIntersection() {
		kanji.setIntersection(3, 3, Stone.WHITE);
		kanji.setIntersection(9, Stone.BLACK);
		assertEquals(Stone.WHITE, kanji.getIntersection(3, 3).getValue());
		assertEquals(Stone.BLACK, kanji.getIntersection(1, 0).getValue());
	}
	
	@Test
	public void testIsFull() {
		assertFalse(kanji.isFull());
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				kanji.setIntersection(i, j, Stone.BLACK);
			}
		}
		assertTrue(kanji.isFull());
		kanji.setIntersection(0, 0, Stone.EMPTY);
		assertFalse(kanji.isFull());
		kanji.setIntersection(0, 0, Stone.BLACK);
		assertTrue(kanji.isFull());
	}
	
	@Test
	public void testGetTerritorySize() {
		kanji.setIntersection(0, 0, Stone.WHITE);
		kanji.updateFields();
		assertEquals(kanji.getTerritorySize(Stone.WHITE), (9 * 9) - 1);
		kanji.setIntersection(0, 1, Stone.BLACK);
		kanji.updateFields();
		assertEquals(kanji.getTerritorySize(Stone.WHITE), 0);
		kanji.setIntersection(1, 1, Stone.WHITE);
		kanji.setIntersection(2, 0, Stone.WHITE);
		kanji.updateFields();
		assertEquals(kanji.getTerritorySize(Stone.WHITE), 1);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		kanji.updateFields();
		assertEquals(kanji.getTerritorySize(Stone.WHITE), 1);
		assertEquals(kanji.getTerritorySize(Stone.BLACK), 1);
	}
	
	@Test
	public void testCheckStones() {
		kanji.setIntersection(0, 0, Stone.BLACK);
		kanji.setIntersection(0, 1, Stone.WHITE);
		kanji.setIntersection(1, 1, Stone.BLACK);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		kanji.setIntersection(2, 1, Stone.WHITE);
		kanji.setIntersection(5, 5, Stone.BLACK);
		kanji.setIntersection(4, 1, Stone.WHITE);
		kanji.setIntersection(7, 8, Stone.WHITE);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		assertEquals(kanji.getIntersection(7, 8).getValue(), Stone.EMPTY);
		kanji.setIntersection(1, 0, Stone.WHITE);
		assertEquals(kanji.getIntersection(0, 0).getValue(), Stone.EMPTY);
	}
	
	@Test
	public void testReset() {
		kanji.setIntersection(0, 0, Stone.WHITE);
		kanji.setIntersection(0, 1, Stone.BLACK);
		kanji.setIntersection(1, 1, Stone.WHITE);
		kanji.setIntersection(2, 0, Stone.WHITE);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		kanji.setIntersection(2, 1, Stone.WHITE);
		kanji.setIntersection(5, 5, Stone.BLACK);
		kanji.setIntersection(4, 1, Stone.WHITE);
		kanji.setIntersection(7, 8, Stone.WHITE);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		kanji.reset();
		assertEquals(kanji.getFields().size(), 1);
	}
	
	@Test
	public void testDetermineWinner() {
		kanji.setIntersection(0, 0, Stone.WHITE);
		kanji.setIntersection(0, 1, Stone.BLACK);
		kanji.setIntersection(1, 1, Stone.WHITE);
		kanji.setIntersection(2, 0, Stone.WHITE);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		kanji.setIntersection(2, 1, Stone.WHITE);
		kanji.setIntersection(4, 1, Stone.WHITE);
		kanji.setIntersection(2, 2, Stone.WHITE);
		kanji.updateFields();
		assertEquals(kanji.determineWinner(), Stone.WHITE);
		kanji.setIntersection(6, 7, Stone.BLACK);
		kanji.setIntersection(5, 7, Stone.BLACK);
		kanji.setIntersection(4, 8, Stone.BLACK);
		kanji.updateFields();
		assertEquals(kanji.determineWinner(), Stone.BLACK);
		kanji.setIntersection(3, 3, Stone.WHITE);
		kanji.setIntersection(2, 4, Stone.WHITE);
		kanji.updateFields();
		assertEquals(kanji.determineWinner(), Stone.EMPTY);
	}
	
	@Test
	public void testMoveIsLegal() {
		kanji.setIntersection(0, 0, Stone.WHITE);
		kanji.setIntersection(0, 1, Stone.BLACK);
		kanji.setIntersection(1, 1, Stone.WHITE);
		kanji.setIntersection(2, 0, Stone.WHITE);
		kanji.setIntersection(8, 8, Stone.BLACK);
		kanji.setIntersection(7, 7, Stone.BLACK);
		kanji.setIntersection(6, 8, Stone.BLACK);
		kanji.setIntersection(2, 1, Stone.WHITE);
		kanji.setIntersection(4, 1, Stone.WHITE);
		kanji.setIntersection(2, 2, Stone.WHITE);
		assertTrue(kanji.moveIsLegal(8, 7, Stone.WHITE));
		assertFalse(kanji.moveIsLegal(9, 8, Stone.WHITE));
		assertFalse(kanji.moveIsLegal(8, 8, Stone.WHITE));
		assertFalse(kanji.moveIsLegal(7, 8, Stone.WHITE));
		assertFalse(kanji.moveIsLegal(0, 0, Stone.EMPTY));
		kanji.setIntersection(0, 0, Stone.EMPTY);
		assertTrue(kanji.moveIsLegal(8, 7, Stone.WHITE));
		assertTrue(kanji.moveIsLegal(kanji.getIndex(8, 7), Stone.WHITE));
		assertFalse(kanji.moveIsLegal(0, 0, Stone.WHITE));
		assertFalse(kanji.moveIsLegal(0, Stone.WHITE));
	}
	
	@Test
	public void testGetStringRepinclCapt() {
		String testboard = "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE 0 0";
		String testboard2 = "BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE 0 0";
		assertEquals(kanji.getStringInclCaptives(), testboard);
		kanji.setIntersection(0, Stone.BLACK);
		assertEquals(kanji.getStringInclCaptives(), testboard2);
	}
	
	@Test
	public void testGetDimension() {
		assertEquals(9, kanji.getDimension());
	}

}
