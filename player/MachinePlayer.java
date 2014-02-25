/* MachinePlayer.java */

package player;
import list.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

	private int myColor; 
	private int oppColor;
	private Board board; 
	private int addDepth = 3; 
	private int stepDepth = 1;
	 


  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
	board = new Board();
	//We changed the value of the color
	if (color == 0) { 
		myColor = Cell.BLACK; 
	} else if (color == 1){
		myColor = Cell.WHITE; 
	}
	oppColor = -myColor;
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
	this(color);
	this.addDepth = searchDepth;
	//System.out.println("addDepth: " + addDepth);
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
	  
	 //should MachinePlayer choose specific first few moves like in goal areas? it is allowed!
	  //System.out.println("firstturn: "+ firstturn); 
    Move best; 
    int random = (int)(Math.random()*5) + 1;
	if ((board.getWhite() == 0) || board.getWhite() == 1) { //it's my first move
		//System.out.println("this is my first move.");
		  /*System.out.println("getWhite" + board.getWhite());
		  System.out.println("getBlack" + board.getBlack());
		  System.out.println("Cell 3,5: " + (board.getCell(3, 5)).getC()); */
		best = new Move(random,0); 
		if (board.isValidMove(myColor, best)) {
	        board.doMove(best, myColor);
			return best;
		}
		/*else {
			random = (int)(Math.random()*5) + 1;
			best = new Move(random,0);
			if (board.isValidMove(myColor, best)) {
				board.doMove(best, myColor);
			}
		}*/
	}
	/*else if ((board.getWhite() == 2) || (board.getWhite() == 3)) { //it's my second move
		System.out.println("this is my first move.");
		random = (int)(Math.random()*5) + 1;
		best = new Move(random,7); 
		if (board.isValidMove(myColor, best)) {
	        board.doMove(best, myColor);
			return best;
		}
		else {
			random = (int)(Math.random()*5) + 1;
			best = new Move(random,0);
			if (board.isValidMove(myColor, best)) {
				board.doMove(best, myColor);
			}
		}
	  }*/
	 if (board.getWhite() < 10 && board.getBlack() < 10) {
		  Best minimax = aB(myColor, Integer.MIN_VALUE, Integer.MAX_VALUE, addDepth, 0); 
		  best = minimax.move;
		  //System.out.println("From chooseMove(): " + best.toString()); 
		  board.doMove(best, myColor);
		  
		  //System.out.println("I am printing from chooseMove[add]");
		  //boardstatus();
		  return best;
	 }
	 else {
		 Best minimax = aB(myColor, Integer.MIN_VALUE, Integer.MAX_VALUE, stepDepth, 0); 
		  best = minimax.move;
		  //System.out.println("From chooseMove(): " + best.toString()); 
		  board.doMove(best, myColor);
		  
		  //System.out.println("I am printing from chooseMovep[step]");
		  //boardstatus();
		  return best;
	 }
	 
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
	  //System.out.println("from oppMove " + m.x1 + " " + m.y1 + " " + m.x2 + " " + m.y2);
		if (board.isValidMove(oppColor, m)) {
	        board.doMove(m, oppColor);
	        
	        //System.out.println("opp adding numTurns: " + numTurns);
	        //System.out.println("Opponent has moved. His move is x: "+m.x1 + "y: " + m.y1);
	        //System.out.println("After opp's move, the cell is still empty?: " + (board.grid[m.x1][m.y1].getC() == Cell.EMPTY));
	        //boardstatus(); 
	        return true;
		}
		else {
			//System.out.println("Opponent move is invalid!!");
			return false;
		}
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
        //Cell c = new Cell(myColor, m.x1, m.y1);
        //System.out.println("forceMove: myColor " + myColor); 
        if (board.isValidMove(myColor, m)) {
            board.doMove(m, myColor);
            return true;
        } else {
        	//System.out.println("I am returning a false form forceMove.");
            return false;
        }
  }

	/*
	* aB determines the best move for this player
	* @param side the player
	* @param alpha the score this player can achieve
	* @param beta the score the opponent player can achieve
	* @param depth the maximum level that the tree will be searched to
	* @param level the current level in the tree
	* @return Best object that contains a move and a score
	*/
  public Best aB(int side, int alpha, int beta, int depth, int level) {
	  //System.out.println("aB is being called w/ side & depth: " + side + " " + depth); 
	  Best myBest = new Best(); // My best move Best reply; // OpponentÕs best reply
	  DList list = new DList();
	  Best reply = new Best();
	  /*if (side == Cell.BLACK) {
		  Move m = new Move(((int)(Math.random()*5) + 1), ((int)(Math.random()*8)));
		  while (!(board.isValidMove(myColor, m))) {
			  m = new Move(((int)(Math.random()*5) + 1), ((int)(Math.random()*8)));
		  }
		  myBest.move = m;
	  }*/
	  
	  //Testing 
	  /*if (alpha == -500 || beta == 500 ) { 
		  System.out.println("aplha, beta:" + alpha +", "+ beta);
		  System.exit(0); 
	  }*/
	  
	  if ((depth <= 0) || (board.findNetwork(side, new DList(), 0))) {
		  //System.out.println("Network is found in aB? for side? " + board.findNetwork(side, new DList(), 0) + " " + side); 
		  
		  myBest.score = evaluate(side, level);
		  /*if (myBest.score == -500) { 
			  System.out.println("Winning Network"); 
			  System.exit(0);
		  }
		  System.out.println("side, myBest score: " + side + " " + myBest.score + "level: " + level);
		  */
		  return myBest;
	  }
	  //System.out.println("I've reached past checking network exists or depth reached.");
	  if (side == myColor) {
		  myBest.score = alpha;
	  } else {
		  myBest.score = beta;
	  }
	  list = (DList)board.listValidMoves(side);
	  //System.out.println("done getting valid moves: " + list.length());
	  if (list.length() == 0) {
		  //System.out.println("side: " + side + " HAS 0 VALID MOVES at depth: " + depth);
	  }
	  ListNode curr = list.front();
	  try {
		  for (int i = 0; i < list.length(); i++, curr = curr.next()) {
			  board.doMove((Move)curr.item(), side); // Modifies this board
			  //System.out.println("just tried a move, now getting reply");
			  reply = aB(-side, alpha, beta, depth-1, level+1);
			  //System.out.println("about to undo move");
			  //if step move, must reduce searchDepth 
			 // boardstatus();
			 // if (board.grid[2][5].getC() == Cell.BLACK) {boardstatus();}
			  //System.out.println("reply from " + (-side) + reply.toString()); 
			  board.undoMove((Move)curr.item(), side); // Restores this board
			  if ((side == myColor) && (reply.score > myBest.score)) { 
				  //System.out.println("FOUND BETTER SCORE FOR: " + side);
				  myBest.move = (Move)curr.item();
				  myBest.score = reply.score;
				  alpha = reply.score;
			  } else if ((side == oppColor) && (reply.score <= myBest.score)) {
				  //System.out.println("FOUND BETTER SCORE FOR: " + side);
				  myBest.move = (Move)curr.item();
				  myBest.score = reply.score;
				  beta = reply.score;
			  }
			  if (alpha >= beta) { 
				  //System.out.println("I am returning a best with alpha = beta,");
				  //myBest.toString(); 
				  return myBest; 
			  }
		  }
	  } catch(InvalidNodeException e) {
		  //System.out.println("Nothing works in aB because of InvalidNodeException!!"); 
	  }
	  //System.out.println("I am returning a best");
	  return myBest;
  }
  
  /*
  * evaluate assigns a score to a board for a player
  * @param player the player
  * @param level the level of the tree
  * @return a score of the board
  */
  public int evaluate(int player, int level) {
	  // Testing
	  /*if (board.grid[2][5].getC() == Cell.BLACK) {
		  boardstatus(); 
		  //System.exit(0); 
	  }*/
		  
	  int score = 0;
	  int my = 0, opp =0;  
	  if((board.findNetwork(player, new DList(), 0))) {
		  //System.out.println("Evaluate: Found Network for: " + player);
		  //System.exit(0); 
		  return 500; //500 + (level*3);
	  }
	  if( (board.findNetwork(oppColor, new DList(), 0))) {
		  //System.exit(0); 
		  return -500; //-500 + (level*3);
	  }
  	for(int i = 1; i < board.DIMENSION - 1; i++) { 
  		for(int j = 1 ; j < board.DIMENSION -1 ; j++) { 
      		my += board.findConnections(player, i, j);
      		opp += board.findConnections(oppColor, i, j);
      		
    	}
  	}
  	//System.out.println("My and opp: " + my + ", " + opp); 
	  score = my - opp; 
	  //add points for having connections to goal areas-done
	  //assign a slightly higher score to a win in one move than to a win in three moves-done
	  return score;
  }

  //Testing
  public void doIhaveaN() { 
	  System.out.println("Do I have a Network? "  + board.findNetwork(myColor, new DList(), 0)); 
  }
  
  public void boardstatus() {
  	for(int i = 0; i < board.DIMENSION; i++, System.out.print("\n")) { 
  		for(int j = 0 ; j < board.DIMENSION; j++) { 
  			Cell c;
			try {
				//c = board.getCell(i, j); //method 1
				c = board.grid[j][i]; 
	      		if (c.getC() == Cell.EMPTY) {
	      			System.out.print("[ ]");
	      		} else if (c.getC() == Cell.BLACK) { 
	      			System.out.print("[B]");
	      		} else { 
	      			System.out.print("[W]"); 
	      		}
			//} catch (InvalidCellException e) { 
			} catch (ArrayIndexOutOfBoundsException e){
				//System.out.print("[ ]");
			}       		
      	}
    }
  }
  
}
