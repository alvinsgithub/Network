/* Board.java */
package player; 
import list.*;

public class Board {

	protected Cell[][] grid;
	public static final int DIMENSION = 8;
	private static int numWhite;
	private static int numBlack;
	private static final int N = 1000, 
			E = 1001, 
			S = 1002, 
			W = 1003, 
			NE = 1004, 
			NW = 1005, 
			SE = 1006, 
			SW = 1007; 

	public Board() {
		grid = new Cell[DIMENSION][DIMENSION];
    	for(int i = 0; i < DIMENSION; i++) { 
      		for(int j = 0 ; j < DIMENSION; j++) { 
          		grid[i][j] = new Cell(Cell.EMPTY, i, j);
        	}
      	}
    }

	/*
	 * addChip() adds a chip into the board if this move is valid.
	 * @param color the color of the chip to be added
	 * @param x the x coordinate of the board
	 * @param y the y coordinate of the board
	 */
	private void addChip(int color, int x, int y) throws InvalidCellException {
		Move m = new Move(x, y);
		if (isValidMove(color, m)) {
			Cell c = getCell(x,y);  
			c.setC(color);
		}
		else {
			throw new InvalidCellException("addchip: InvalidCellException");
		}
	}

	/*
	 * removeChip() removes a chip from the board.
	 * @param x the x coordinate of the board
	 * @param y the y coordinate of the board
	 */
	private void removeChip(int x, int y) {
		grid[x][y].setC(Cell.EMPTY);
	}

	/*
	 * isValidMove checks whether a certain move is allowed to be executed
	 * @param color the color of the chip to be inspected
	 * @param move a Move to be inspected
	 */
	public boolean isValidMove(int color, Move move) {
		//checking if Move is in four corners
		//checking if a cell to be moved to is already occupied
		//checking if Move is in opponent's goal areas
		//checking if it's being placed in a clustered area of their color
		int x = move.x1, y = move.y1;
    	int x2 = move.x2, y2 = move.y2;
		try {
			if ( (x == 0 && y == 0) || (x == DIMENSION-1 && y == DIMENSION-1) || (x == 0 && y == DIMENSION-1) || (x == DIMENSION-1 && y == 0) ) {
				//System.out.println("can't place chip in four corners");
				return false;
			}
			else if ((getCell(x, y).getC() == Cell.WHITE) || (getCell(x, y).getC() == Cell.BLACK)) {
				//System.out.println("the new location is occupied");
				return false;
			}
			else if ((color == Cell.WHITE) && ((y == 0) || (y == 7))) {
				//System.out.println("white chip placing into black's goal areas" + ". The color is " + color);
				return false;
			}
			else if ((color == Cell.BLACK) && ((x == 0) || (x == 7))) {
				//System.out.println("black chip placing into white's goal areas");
				return false;
			}
			else if (inCluster(color, x, y, x2, y2)) {
				//System.out.println("black chip being placed in a black cluster");
				return false;
			}
			return true;
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Can't insert into outside board's dimensions. \n" + e); 
			return false; 
		} catch(InvalidCellException e) {System.out.println(e);
			return false;}
	}

	/*
	 * doMove with two parameters applies a move to a chip of a color if valid
	 * @param move the move to be executed
	 * @param color the color of the chip for the move
	 */
	public void doMove(Move move, int color) {
		int x = move.x1, y = move.y1;
		try {
			if (move.moveKind == Move.ADD) {
				//System.out.println("color from doMove: " + color);
				addChip(color, x, y);
				if (color == Cell.WHITE) {
					numWhite++;
					//System.out.println("White++: "+ numWhite); 
				}
				else {
					numBlack++;
					//System.out.println("Black++: "+ numBlack); 
				}
			}
			else if (move.moveKind == Move.STEP) {
				doMove(move);
			}
			else if (move.moveKind == Move.QUIT) {
				System.exit(0);
			}
		} catch(InvalidCellException e) {
			System.out.println("Invalid move.");
		}
	}

	/*
	 * doMove with one parameter applies a move only of the kind step if valid
	 * @param move the move to be executed
	 */
	public void doMove(Move move) {
		try {
			int color = grid[move.x1][move.y1].getC();
			addChip(color, move.x1, move.y1);
			removeChip(move.x2, move.y2);
		} catch(InvalidCellException e) {
			System.out.println("Invalid move: step.");
		}
	}

	public void undoMove(Move move, int color) {
        if (move.moveKind == Move.ADD) {
            removeChip(move.x1, move.y1);
            if (color == Cell.WHITE) {
                numWhite--;
            }
            if (color == Cell.BLACK) {
                numBlack--;
            }
        }
        if (move.moveKind == Move.STEP) {
            grid[move.x2][move.y2] = grid[move.x1][move.y1];
            removeChip(move.x1, move.y1);
        }
	}
	
	
	public Cell getCell(int x, int y) throws InvalidCellException {//try else if each
		if ( ((x == 0) && (y == 0))
				|| ((x == DIMENSION-1) && (y == DIMENSION-1)) 
				|| ((x == 0) && (y == DIMENSION-1))
				|| ((x == DIMENSION-1) && (y == 0)) ) {
			throw new InvalidCellException("Four corners cannot contain chips.");
		}
		//System.out.println("x: " + x + "y: " + y);
		try { 
			return grid[x][y];
		} catch (ArrayIndexOutOfBoundsException e){ 
			throw new InvalidCellException("Cell out of bound.");
		}
	}

	public int getWhite() {
		return numWhite;
	}
	public int getBlack() {
		return numBlack;
	}

	/*
	 * neighbors return an array of Cells that are the adjacent cell objects of the inspected cell
	 * @param x x-coordinate of the cell being inspected
	 * @param y y-coordinate of the cell being inspected
	 */
	private Cell[] neighbors(int x, int y) {
		Cell[] adj = new Cell[8];
		for (int i = 0; i < 8; i++){ 
			adj[i] = new Cell(Cell.EMPTY, 0, 0); 
		}
		try {
			adj[0] = getCell(x-1, y-1);
		} catch(InvalidCellException e) {} 
		try {
			adj[1] = getCell(x, y-1);
		} catch(InvalidCellException e) {}
		try {			
			adj[2] = getCell(x+1, y-1);
		} catch(InvalidCellException e) {}
		try {
			adj[3] = getCell(x-1, y);
		} catch(InvalidCellException e) {}
		try {
			adj[4] = getCell(x+1, y);
		} catch(InvalidCellException e) {}
		try {
			adj[5] = getCell(x-1, y+1);
		} catch(InvalidCellException e) {}
		try {
			adj[6] = getCell(x, y+1);
		} catch(InvalidCellException e) {}
		try {
			adj[7] = getCell(x+1, y+1);
		} catch(InvalidCellException e) {}
		/*System.out.println("printing neighbors");
		for (int i = 0; i < 8; i++){ 
			System.out.println(adj[i].getC());
		}*/
		return adj;
	}

	/*
	 * blackAndWhite returns an int array of two elements, where the first element contains the 
	 * amount of adjacent cells of x and y that is black, and the second element contains the 
	 * amount of adjacent cells that is white.
	 * @param x x-coordinate of the cell being inspected
	 * @param y y-coordinate of the cell being inspected
	 */
	private int[] blackAndWhite(int x, int y) {
		Cell[] n = neighbors(x, y); 
		int[] bnw = new int[2]; 
		for (int i = DIMENSION -1; i >= 0; i--) {
			if (n[i].getC() == Cell.BLACK) {
				bnw[0]++;
			}
			else if (n[i].getC() == Cell.WHITE) { 
				bnw[1]++; 
			}
		}
		//System.out.println("num black chip neighbors: " + bnw[0]);
		//System.out.println("num white chip neighbors: " + bnw[1]);
		return bnw; 
	}
	
	/*
	 * inCluster returns a boolean that tells whether placing a chip that is this color will be inside a cluster,
	 *  breaking one of the rules.
	 *  @param color the color of the chip to be inspected
	 *  @param x the x coordinate of the chip
	 *  @param y the y coordinate of the chip
	 */
	private boolean inCluster(int color, int x1, int y1, int x2, int y2) {
	//System.out.println("calling inCluster placing into " + x1 + " " + y1 + "from: " + x2 + " " + y2);
		int temp = grid[x2][y2].getC();
		removeChip(x2,y2);
		boolean result = false;
		int[] amt = blackAndWhite(x1, y1);
		if ((color == Cell.BLACK) && (amt[0] >= 2)) {
			//System.out.println("chip has >=2 black color neighbors.");
			result = true;
		}
		else if((color == Cell.WHITE) && (amt[1] >= 2)) {
			//System.out.println("chip has >=2 white color neighbors.");
			result = true;
		}
		else if ((amt[0] == 1 || amt[1] == 1)){
			//System.out.println("Should have just one neighbor: " + (amt[0] == 1 || amt[1] == 1));
			Cell[] n = neighbors(x1, y1);
			for (int i = DIMENSION -1 ; i >= 0; i--) {
				if(n[i].getC() == color) {//if this neighbor has the same color as inspecting chip
					int[] nei = blackAndWhite(n[i].getlocx(), n[i].getlocy());//
					if ((color == Cell.BLACK) && (nei[0] >= 1)) {
						//System.out.println("neighbor doesn't have same color neighbors: black");
						result = true;
					}
					else if((color == Cell.WHITE) && (nei[1] >= 1)) {
						//System.out.println("neighbor doesn't have same color neighbors: white");
						result = true;
					}
				}
			}
		}
		//System.out.println("loop ended");
		grid[x2][y2].setC(temp);
		return result;
	}

	private int up(int y) {
		return y - 1; 
	}
	private int down(int y) { 
		return y + 1; 
	}
	private int left(int x) {
		return x - 1; 
	}
	private int right(int x) {
		return x + 1; 
	}


	private Cell goDirection(DList path, int dir, int lastdir, Cell lastCell) throws InvalidCellException{ 
		//System.out.println("goDirection is called, going to dir: " + dir); 
		if (dir == lastdir) {
			//System.out.println("Coming from the same dir"); 
			throw new InvalidCellException(); 
		} 
			Cell c = lastCell; 
			int x = c.getlocx(); 
			int y = c.getlocy();
			Cell nc;
			int nx, ny; 
			switch (dir) {
				case N:  nx = x; 
				ny = up(y); 
				break; 
				case S:  nx = x; 
				ny = down(y); 
				break; 
				case W:  nx = left(x); 
				ny = y; 
				break; 
				case E:  nx = right(x); 
				ny = y; 
				break; 
				case NW:  nx = left(x); 
				ny = up(y); 
				break; 
				case NE:  nx = right(x); 
				ny = up(y); 
				break; 
				case SW:  nx = left(x); 
				ny = down(y); 
				break; 
				default: nx = right(x); 
				ny = down(y); 
				break; 
			}
			nc = getCell(nx, ny); 
			int cellcolor = 999;
			try {
				cellcolor = ((Cell) path.back().item()).getC();
				//System.out.println("Cell color is :" + cellcolor); 
			} catch (InvalidNodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//System.out.println("Fatal Error to get color");
			} 

			if (cellcolor == -nc.getC()) { 
				//System.out.println("Reached Opposite Chip"); 
				throw new InvalidCellException();
			} else if (nc.getC() == Cell.EMPTY){ 
				//System.out.println("Reached Empty Cell...keeps going");
        try {
          return goDirection(path, dir, lastdir, nc); 
        } catch (StackOverflowError e) {
          throw new InvalidCellException(); 
        }
			} else {
				assert cellcolor == nc.getC(); 
				try {
					//System.out.println("Reached Same Chip in "+ nc.toString()); 
					ListNode node = path.front(); 
				//checking if the new same color node is already in path
					for (int a = 0; a < path.length(); a++) {  
						//System.out.println("Checking path...");
						if (node.item().equals(nc)) {
							//System.out.println("Same chip exists in the path"); 
							throw new InvalidCellException(); 
						}
						node = node.next();
					}
				} catch(InvalidNodeException e) {}
				//connections++;
				//System.out.println("DONE! Returning nc by direction: " + dir);
				return nc;  
			} 		
	}

	public boolean findNetwork(int color, DList path, int dir) { 

			//Find if the end chip is in the goal area
			try {
				if (path.length() >= 5) {
					Cell last = (Cell) path.back().item(); 
					if (color == Cell.WHITE) {  
						if (last.getlocx() == DIMENSION - 1) {
							//System.out.println("Network Found!!"); 
							return validgoal(path); 
						}
						//return (last.getlocx() == DIMENSION - 1) || (findNetwork(color, path, dir)); 
					} else {  
						if (last.getlocy() == DIMENSION - 1) { 
							//System.out.println("Network Found!!"); 
							
							return validgoal(path); 
						}
						//return (last.getlocy() == DIMENSION - 1) || (findNetwork(color, path, dir)); 
					}
				}
			} catch(InvalidNodeException e) {}			
			
			//Find First Chip of the Path
			if (path.length() == 0) { 
				//System.out.println("findNetwork: initializing"); 
				//Check if the other end has a chip
				int endchips = 0; 
				for (int j = 0; j < DIMENSION; j++) {
					try {
						//System.out.println("findNetwork: checking if the other end has a chip"); 
						if (color == Cell.WHITE) { 
							if (getCell(DIMENSION - 1 , j).getC() == Cell.WHITE) { 
								//System.out.println("Found one: " + getCell(DIMENSION - 1 , j).toString());
								endchips++; 
							}
						} else { 
							if (getCell(j, DIMENSION - 1).getC() == Cell.BLACK) {
								//System.out.println("Found one: " + getCell(j, DIMENSION - 1).toString());
								endchips++; 
							}
						}
					}catch (InvalidCellException e){}
				}
				if (endchips == 0) {
					return false; 
				}
	
				//Find First Chip of the Path
				//System.out.println("findNetwork: finding the first chip"); 
				for (int i = 0; i < DIMENSION; i++) { 
					// WHITE
					if (color == Cell.WHITE) { 
						try {
							if (getCell(0, i).getC() == Cell.WHITE) { 
								//System.out.println("findNetwork: Found the first chip: " + getCell(0,i).toString()); 
								path = new DList(); 
								path.insertFront(getCell(0, i)); 
								return findNetwork(color, path, 0); 
							}
						} catch (InvalidCellException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					} else { // BLACK
						try {
							if (getCell(i, 0).getC() == Cell.BLACK) { 
								//System.out.println("findNetwork: Found the first chip: " + getCell(i,0).toString()); 
								path = new DList(); 
								path.insertFront(getCell(i, 0)); 
								return findNetwork(color, path, 0); 
							}
						} catch (InvalidCellException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						} 
					}
				}
			}
	
			//System.out.println("Main part of findnetwork is starting..."); 
			//How to Clone
			DList npath;
			
			boolean b = false; 
			for (int i = N; i <= SW; i++){ 
				try { 
					npath = path.clone(); 
					npath.insertBack(goDirection(npath, i, dir, (Cell) npath.back().item())); 
					b = findNetwork(color, npath, i); 
					if (b) { return true; }
				} catch (InvalidCellException e) {
					//System.out.println("You are hella stupid!");
				} catch (InvalidNodeException e) { 
					//System.out.println("You are stupid!"); 
				}
			}
			
		return false; 
	}
	
	private boolean validgoal(DList path) throws InvalidNodeException { 
		ListNode node = path.front(); 
		int count = 0; 
		for (int i = 0; i < path.length(); i++) {
			int x = ((Cell) node.item()).getlocx(); 
			int y = ((Cell) node.item()).getlocy();
			if (x == 0 || y == 0 || x == DIMENSION -1 || y == DIMENSION - 1){
				count++; 
			}
			if (i < path.length() - 1) {
				node = node.next();
			}
		}
		return (count == 2); 
	}
	
    /**
     *  listValidMoves returns a list of all possible valid moves, given the current board configuration 
     *  for the current player indicated by color.
     *  @param color color of the player.
     *  @return the list of all possible valid moves.
     */
    public List listValidMoves(int color) {
        Move move;
        //Cell cell;
        List list = new DList();
        if (((color == Cell.WHITE) && (numWhite < 10)) || ((color == Cell.BLACK) && (numBlack < 10))) {
            for(int j = 0; j < DIMENSION; j++) {
                for(int i = 0; i < DIMENSION; i++) {
                    //cell = new Cell(color, i, j);
                    move = new Move(i, j);
                    if (isValidMove(color, move)) {
                        list.insertBack(move);
                    }
                }
            }
        }
        if (((color == Cell.WHITE) && (numWhite >= 10)) || ((color == Cell.BLACK) && (numBlack >= 10))) {
            for(int j = 0; j < DIMENSION; j++) {
                for(int i = 0; i < DIMENSION; i++) {
                    if ((grid[i][j] != null) && (grid[i][j].getC() == color)) {
                        //cell = grid[i][j];
                        for(int l = 0; l < DIMENSION; l++) {
                            for(int k = 0; k < DIMENSION; k++) {
                                move = new Move(k, l, i, j);
                                if (isValidMove(color, move)) {
                                    list.insertBack(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
    
    public int findConnections(int color, int x, int y) { 
    	int connections = 0; 
    	for (int i = N; i < SW; i++) { 
    		try {
    			DList l = new DList();
    			Cell c = new Cell(color, x, y); 
    			l.insertBack(c); 
    			goDirection(l, i, 0, getCell(x,y));
    			connections++; 
    		} catch (InvalidCellException e) {}
    	}
    	
    	return connections; 
    }
}

