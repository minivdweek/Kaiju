package kanji.test;

import kanji.server.game.Stone;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StoneTest {
	private Stone c0;
	private Stone c1;
	private Stone c2;

	@Before
	public void setUp() throws Exception {
		c1 = Stone.WHITE;
		c2 = Stone.BLACK;
		c0 = Stone.EMPTY;
	}

	@Test
	public void testother() {
		assertEquals(c1, c2.other());
		assertEquals(c1.other(), c2);
		assertEquals(c0, c0.other());
	}

}
