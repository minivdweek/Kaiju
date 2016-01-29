package kanji.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kanji.server.game.Intersection;
import kanji.server.game.Stone;

public class IntersectionTest {
	private Intersection[][] intersections;
	private final int dim = 4;

	@Before
	public void setUp() throws Exception {
		intersections = new Intersection[dim][dim];
        for (int i = 0; i < dim; i++) {
        	for (int j = 0; j < dim; j++) {
        		intersections[i][j] = new Intersection();
        	}
        }
        for (int i = 0; i < dim; i++) {
        	for (int j = 0; j < dim; j++) {
        		if (i > 0) {
        			intersections[i][j].setUp(intersections[i - 1][j]);
        		} else {
        			intersections[i][j].setUp(null);
        		}
        		if (i < dim - 1) {
        			intersections[i][j].setDown(intersections[i + 1][j]);
        		} else {
        			intersections[i][j].setDown(null);
        		}
        		if (j > 0) {
        			intersections[i][j].setLeft(intersections[i][j - 1]);
        		} else {
        			intersections[i][j].setLeft(null);
        		}
        		if (j < dim - 1) {
        			intersections[i][j].setRight(intersections[i][j + 1]);
        		} else {
        			intersections[i][j].setRight(null);
        		}
        	}
        }
	}

	@Test
	public void testValue() {
		this.intersections[0][0].setValue(Stone.BLACK);
		assertEquals(intersections[0][0].getValue(), Stone.BLACK);
		assertEquals(intersections[1][0].getValue(), Stone.EMPTY);
	}
	
	@Test
	public void testNeighbors() {		
		this.intersections[2][2].setValue(Stone.BLACK);
		assertEquals(intersections[2][3].getLeft().getValue(), Stone.BLACK);
		assertEquals(intersections[3][2].getUp().getValue(), Stone.BLACK);
		assertEquals(intersections[2][1].getRight().getValue(), Stone.BLACK);
		assertEquals(intersections[1][2].getDown().getValue(), Stone.BLACK);
		assertEquals(intersections[2][2].getDown().getRight().getUp().getLeft().getValue(), 
						Stone.BLACK);
	}
	
	@Test
	public void testCheck() {
		assertFalse(this.intersections[2][2].isChecked());
		this.intersections[2][2].check();
		assertTrue(this.intersections[2][2].isChecked());
		this.intersections[2][2].uncheck();
		assertFalse(this.intersections[2][2].isChecked());
	}

}
