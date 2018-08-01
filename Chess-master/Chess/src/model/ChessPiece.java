package model;

/********************************************************************
 * CIS 350 - 01
 * Chess
 *
 * A game piece in a game of chess. 
 *
 * @author John O'Brien
 * @author Louis Marzorati
 * @author Shane Higgins
 * @author Caleb Woods
 * @version Feb 24, 2014
 *******************************************************************/
public abstract class ChessPiece implements IChessPiece {

	/** The Player who this piece belongs to. */
	private Player owner;

	/****************************************************************
	 * Constructor sets the owner of this game piece.
	 * 
	 * @param color the owner of this piece.
	 ***************************************************************/
	protected ChessPiece(final Player color) {
		this.owner = color;
	}

	/****************************************************************
	 * Returns the player who owns this game piece.
	 * 
	 * @return the player who owns this game piece.
	 ***************************************************************/
	public final Player player() {
		return owner;
	}

	/****************************************************************
	 * Returns the name of this piece.
	 * Example: "King"
	 * 
	 * @return the name of this piece's type.
	 ***************************************************************/
	public abstract String type();

	@Override
	public boolean isValidMove(final Move move, final IChessBoard board) {
		int fR = move.fromRow(), fC = move.fromColumn();
		int tR = move.toRow(), tC = move.toColumn();
		
		IChessPiece from;
		IChessPiece to;
		
		/* Ensures that only moves contained within the bounds
		 * of the board are valid */
		try {
			from = board.pieceAt(fR, fC);
			to = board.pieceAt(tR, tC);
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}

		/* Ensures the piece is actually moving */
		if (fR == tR && fC == tC) { return false; }
		
		/* Ensures the piece at the from location is this piece */
		if (from != this) { 
			throw new IllegalArgumentException();
		}
		
		/* Checks that the piece is not trying to move onto one
		 * of its own pieces */
		if (to == null || to.player() != owner) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public final boolean is(final String type) {
		return type.equals(type());
	}
}
