package player;

public class Cell {
	
	public static final int EMPTY = 0;
	public static final int BLACK = -1;
	public static final int WHITE = 1;
	
	private int content = EMPTY;
	private int locx, locy; 
	
	public Cell(int color) {
		content = color;
	}
	
	public Cell(int color, int x, int y) {
		this.content = color;
		locx = x;
		locy = y;
	}
	
	public int getC() {
		return content;
	}
  
	public int getlocx() {
		return locx;
	}

	public int getlocy() {
		return locy;
	}  
  
	public void setC(int c) {
		content = c;
	}
  
	public void setloc(int x, int y) {
		locx = x; 
		locy = y; 
	}
}