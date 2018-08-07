package model;

import java.util.Random;

/********************************************************************
 * CIS 350 - 01
 * Chess
 *
 * Chess board that holds all the game pieces.
 *
 * @author John O'Brien
 * @author Louis Marzorati
 * @author Shane Higgins
 * @author Caleb Woods
 * @version Feb 24, 2014
 *******************************************************************/
public class ChessBoard implements IChessBoard {

	/** 2d array of ChessPieces to represent the board. */
	private IChessPiece[][] board;
	
	/** Tells who's turn it currently is (WHITE or BLACK). */
	private Player currentPlayer;
	
	/** Tells how many moves have been made. */
	private int numMoves;
	
	/** Location of the white King. */
	private int[] whiteKing;
	
	/** Location of the black King. */
	private int[] blackKing;
	private boolean is960 = false;
	
	/****************************************************************
	 * Constructor for ChessBoard.
	 * Default game has 32 pieces and WHITE goes first.
	 * 
	 * @param boardSize the size of an edge on the square board (8 default).
	 * @param placePieces if true, will place game pieces. 
	 * 			If false, will create an empty game board.
	 ***************************************************************/
	public ChessBoard(final int boardSize, final boolean placePieces, final boolean is960) {
		board = new ChessPiece[boardSize][boardSize];
		
		whiteKing = new int[2];
		blackKing = new int[2];
		
		/* Creates an empty board if placePieces is false */
		if (!placePieces) {
			for (int r = 0; r < boardSize; r++) {
				for (int c = 0; c < boardSize; c++) {
					board[r][c] = null;
				}
			}
		} else {
			if(is960) {
				setup960Board();
			} else {
				setupBoard();
			}
		}
		
		currentPlayer = Player.WHITE;
		numMoves = 0;
	}
	
	/****************************************************************
	 * Copy constructor.
	 * 
	 * @param other the ChessBoard to copy.
	 ***************************************************************/
	public ChessBoard(final ChessBoard other) {
		board = other.clone();
		
		currentPlayer = other.currentPlayer;
		numMoves = other.numMoves;
		whiteKing = other.whiteKing;
		blackKing = other.blackKing;
	}
	/****************************************************************
	 * Places the game pieces into random locations on the board
	 * following the following rules.
	 * 1) The bishops must be placed on opposite-color squares.
	 * 2) The king must be placed on a square between the rooks.
	 ***************************************************************/
	private void setup960Board() {
		/* Random generator*/
		Random random = new Random();
		/* Column Holder*/
		int[] usedCol = {-2, -2, -2, -2, -2, -2, -2, -2};
		
		/* Rows for the black pieces*/
		int rowPawns = 1; 
		int row = 0;
		
		// Column of the board where the pieces start
		final int kingCol = random.nextInt(6)+1;
		usedCol[0] = kingCol;
		int rook1Col = random.nextInt(kingCol);
		int rook2Col = random.nextInt(7 - kingCol) + kingCol + 1;
		usedCol[1] = rook1Col;
		usedCol[2] = rook2Col;
		int bishop1Col = -1;
		int bishop2Col = -1;
		while(bishop1Col == -1 || bishop2Col == -1) {
			int n = random.nextInt(8);
			if(notUsed(n, usedCol) && isEven(n)) {
				bishop1Col = n;
				usedCol[3] = bishop1Col;
			} else if (notUsed(n, usedCol) && !isEven(n)){
				bishop2Col = n;
				usedCol[4] = bishop2Col;
			}
		}	
		
		int knight1Col = -1;
		int queenCol = -1;
		int knight2Col = -1;
		while(knight1Col == -1 || queenCol == -1 || knight2Col == -1) {
			int n = random.nextInt(8);
			if(queenCol == -1 && notUsed(n, usedCol)) {
				queenCol = n;
				usedCol[5] = queenCol;
			} else if (knight1Col == -1 && notUsed(n, usedCol)) {
				knight1Col = n;
				usedCol[6] = knight1Col;
			} else if (knight2Col == -1 && notUsed(n, usedCol)) {
				knight2Col = n;
				usedCol[7] = knight2Col;
			}
		}
		
		
		/* Places both black and white pieces */
		for (Player p : Player.values()) {				
			board[row][rook1Col] = new Rook(p);
			board[row][knight1Col] = new Knight(p);
			board[row][bishop1Col] = new Bishop(p);
			board[row][queenCol] = new Queen(p);
			board[row][kingCol] = new King(p);
			board[row][bishop2Col] = new Bishop(p);
			board[row][knight2Col] = new Knight(p);
			board[row][rook2Col] = new Rook(p);
			
			/* Places pawns */
			for (int col = 0; col < numColumns(); col++) {
				board[rowPawns][col] = new Pawn(p);
			}
			
			// Records Kings location
			int[] loc = {row, kingCol};
			setKing(p, loc);
			
			row = numRows() - 1; 
			rowPawns = row - 1;
		}
	}
	private boolean isEven(int n) {
		return n == 0 || n == 2 || n == 4 || n == 6;
	}
	private boolean notUsed(int r, int[] arr) {
		for(int n : arr) {
			if(r == n) { return false; }
		}
		return true;
	}
	
	/****************************************************************
	 * Places the game pieces into their default locations on the board.
	 ***************************************************************/
	private void setupBoard() {
		
		/* Rows for the black pieces*/
		int rowPawns = 1; 
		int row = 0;
		
		// Column of the board where the pieces start
		// (only the pieces to the right of the left-most Bishop)
		final int queenCol = 3;
		final int bishopCol = 5;
		final int knightCol = 6;
		final int rookCol = 7;

		
		/* Places both black and white pieces */
		for (Player p : Player.values()) {				
			board[row][0] = new Rook(p);
			board[row][1] = new Knight(p);
			board[row][2] = new Bishop(p);
			board[row][queenCol] = new Queen(p);
			board[row][King.KING_STARTING_COL] = new King(p);
			board[row][bishopCol] = new Bishop(p);
			board[row][knightCol] = new Knight(p);
			board[row][rookCol] = new Rook(p);
			
			/* Places pawns */
			for (int col = 0; col < numColumns(); col++) {
				board[rowPawns][col] = new Pawn(p);
			}
			
			// Records Kings location
			int[] loc = {row, King.KING_STARTING_COL};
			setKing(p, loc);
			
			row = numRows() - 1; 
			rowPawns = row - 1;
		}

		/* Sets the rest to null */
		for (int r = 2; r < numRows() - 2; r++) {

			for (int c = 0; c < numColumns(); c++) {
				board[r][c] = null;
			}
		}
	}
	
	@Override
	public final int numRows() {
		return board.length;
	}

	@Override
	public final int numColumns() {
		return board[0].length;
	}

	@Override
	public final IChessPiece pieceAt(final int row, final int column) {
		return board[row][column];
	}

	@Override
	public final void move(final Move move) {
		
		/* Moves piece at the from location to the to location
		 * Sets from location to null. */
		IChessPiece movingPiece = pieceAt(move.fromRow(), 
				move.fromColumn());
		// Checks for and handles an en Passant and Casteling
		handleEnPassant(movingPiece, move);
		handleCastle(movingPiece, move);
		unset(move.fromRow(), move.fromColumn());
		set(movingPiece, move.toRow(), move.toColumn());
		
		// Switches turns
		currentPlayer = currentPlayer.next();
		
		/* If the piece being moved is a king, the location is recorded */
		if (movingPiece != null && movingPiece.is("King")) {
			updateKingLocation(move.toRow(), move.toColumn());
		}
		
		/* Increments the number of moves that have been made */
		numMoves++;
	}
	
	/****************************************************************
	 * If a piece performs enPassant, it will remove the piece
	 * that was attacked from the board.
	 * 
	 * @param movingPiece the Piece that is moving
	 * @param move the move being attempted 
	 ***************************************************************/
	private void handleEnPassant(final IChessPiece movingPiece,
			final Move move) {
		
		if (movingPiece == null) { return; }
		
		if (!movingPiece.is("Pawn")) { return; }
		
		Pawn p = (Pawn) movingPiece;
		
		/* Removes attacked pawn from the board */
		if (pieceAt(move.toRow(), move.toColumn()) == null 
				&& 
				p.isAttacking(move, this)) { 
			unset(move.fromRow(), move.toColumn());
		}
	}
	
	/****************************************************************
	 * If a piece performs a Castle, it will remove the piece
	 * that was attacked from the board.
	 * 
	 * @param movingPiece the Piece that is moving.
	 * @param m the move being attempted.
	 ***************************************************************/
	private void handleCastle(final IChessPiece movingPiece, final Move m) {
		
		if (movingPiece == null || !movingPiece.is("King")) { return; }
		
		
		int distance = m.toColumn() - m.fromColumn();
		int direction = distance > 0 ? 1 : -1;
		
		if (distance * direction != 2) { return; }

		int rookColumn = 0;
		
		if (direction == 1) {
			rookColumn = numColumns() - 1;
		} 

		Rook rook = (Rook) pieceAt(m.fromRow(), rookColumn);
		
		unset(m.fromRow(), rookColumn);
		set(rook, m.fromRow(), m.fromColumn() + direction);

	}

	/****************************************************************
	 * Updates the arrays keeping track of each king's location.
	 * 
	 * @param row row coordinate of the piece
	 * @param col column coordinate of the piece
	 ***************************************************************/
	private void updateKingLocation(final int row, final int col) {
		
		/* Checks which player owns the piece */
		if (pieceAt(row, col).player() == Player.WHITE) {
			whiteKing[0] = row;
			whiteKing[1] = col;
		} else {
			blackKing[0] = row;
			blackKing[1] = col;
		}
	}

	@Override
	public final void set(final IChessPiece piece, final int row,
			final int column) {
		board[row][column] = piece;
	}

	@Override
	public final void unset(final int row, final int column) {
		board[row][column] = null;
	}
	
	@Override
	public final int[] findKing(final Player p) {
		if (p == Player.WHITE) {
			return whiteKing;
		} else {
			return blackKing;
		}
	}
	
	/****************************************************************
	 * Sets the location of the given king.
	 * 
	 * @param p the player who owns the King.
	 * @param location the location of the given king.
	 ***************************************************************/
	private void setKing(final Player p, final int[] location) {
		if (p == Player.WHITE) {
			whiteKing = location;
		} else {
			blackKing = location;
		}
	}
	public boolean getIs960() {
		return is960;
	}
	public void setIs960(boolean Is960) {
		is960 = Is960;
	}
	
	@Override
	public final int getNumMoves() {
		return numMoves;
	}
	
	@Override
	public final Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public final IChessPiece[][] clone() {
		IChessPiece[][] b = new ChessPiece[numRows()][numColumns()];
		
		for (int r = 0; r < numRows(); r++) {
			for (int c = 0; c < numColumns(); c++) {
				
				assignProperPiece(b, r, c);
			}
		}
		return b;
	}
	
	/****************************************************************
	 * Places whatever piece was found on this board at the given 
	 * location on a given board.
	 * 
	 * This helper is used to replace the switch statement that was
	 * originally used in the clone method. The reason being, EclEmma
	 * doesn't properly cover switch statements. 
	 * 
	 * @param b the given board to have pieces placed on.
	 * @param r the row location of the piece.
	 * @param c the column location of the piece.
	 ***************************************************************/
	private void assignProperPiece(final IChessPiece[][] b, final int r,
			final int c) {
		IChessPiece p = pieceAt(r, c);
		
		if (p == null) { return; }
		
		Player plr = p.player();
		String type = p.type();
		
		if (type.equals("Pawn")) {
			b[r][c] = ((Pawn) p).clone();
		}

		if (type.equals("Rook")) {
			b[r][c] = ((Rook) p).clone();
		}

		if (type.equals("Bishop")) {
			b[r][c] = new Bishop(plr);
		}

		if (type.equals("Knight")) {
			b[r][c] = new Knight(plr);
		}

		if (type.equals("Queen")) {
			b[r][c] = new Queen(plr);
		}

		/* The king also records the location of the kings */
		if (type.equals("King")) {
			b[r][c] = ((King) p).clone();
			int [] location = {r, c};
			setKing(plr, location);
		}
	}
}
