package kanji.server.game;

public class Intersection {
	private Stone value;
	private Intersection left;
	private Intersection right;
	private Intersection up;
	private Intersection down;
	private boolean checked;
	
	/**.
	 * creates a new Intersection
	 */
	public Intersection() {
		this.value = Stone.EMPTY;
		this.uncheck();
	}
	
	/**.
	 * returns the Stone of this Intersection
	 * @return
	 */
	public Stone getValue() {
		return value;
	}

	/**.
	 * sets the value of this Intersection
	 * @param value the Color of the Stone
	 */
	public void setValue(Stone value) {
		this.value = value;
	}

	public Intersection getLeft() {
		return left;
	}

	public void setLeft(Intersection left) {
		this.left = left;
	}

	public Intersection getRight() {
		return right;
	}

	public void setRight(Intersection right) {
		this.right = right;
	}

	public Intersection getUp() {
		return up;
	}

	public void setUp(Intersection up) {
		this.up = up;
	}

	public Intersection getDown() {
		return down;
	}

	public void setDown(Intersection down) {
		this.down = down;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void check() {
		this.checked = true;
	}
	
	public void uncheck() {
		this.checked = false;
	}
	
}
