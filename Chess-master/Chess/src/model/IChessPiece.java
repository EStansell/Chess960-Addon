package model;

/********************************************************************
 * CIS 350 - 01
 * Chess
 *
 * Describes a piece for a game of chess (King, Queen, Rook, etc.).
 *
 * @author Zachary Kurmas
 * @author John O'Brien
 * @author Louis Marzorati
 * @author Shane Higgins
 * @author Caleb Woods
 * @version Feb 24, 2014
 *******************************************************************/
public interface IChessPiece {

	/****************************************************************
	 * Returns the player that owns this piece.
	 * 
	 * @return the player that owns this piece.
	 ***************************************************************/
	Player player();


	/****************************************************************
	 * Returns the name of this piece's type as a string.
	 * Example: "King"
	 * 
	 * @return the name of this piece's type.
	 ***************************************************************/
	String type();

	/****************************************************************
	 * Returns whether the piece at location [move.fromRow, move.fromColumn]
	 * is allowed to move to location [move.fromRow, move.fromColumn].
	 *
	 * @param move a Move object describing the move to be made.
	 * @param board the chess board in which this piece resides.
	 * @return true if the proposed move is valid, false otherwise.
	 ***************************************************************/
	boolean isValidMove(Move move, IChessBoard board);


	/****************************************************************
	 * Used to check what type of piece this is.
	 * 
	 * @param type the type of the piece being compared.
	 * @return true if this piece matches the parameter.
	 ***************************************************************/
	boolean is(String type);
}
