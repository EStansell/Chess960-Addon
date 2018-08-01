package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class KnightTest extends ChessPieceTest {

	@Override
	protected final IChessPiece make(final Player p) {
		return new Knight(p);
	}

	@Override
	protected final Move getValidMove(final int row, final int col) {
		int newRow = row + 2;
		int newCol = col + 1;
		
		return new Move(row, col, newRow, newCol);
	}
	
	@Override 
	public final void canCaputre() throws Throwable {
		getBoard().set(getPiece(), 4, 4);
		getBoard().set(new Pawn(getPiece().player().next()), 2, 3);
		assertTrue(getPiece().isValidMove(new Move(4, 4, 2, 3), getBoard()));
	}
	
	@Test
	public final void canMoveOverPieces() throws Exception {
		getBoard().set(getPiece(), 4, 4);
		getBoard().set(new Pawn(Player.WHITE), 3, 3);
		getBoard().set(new Pawn(Player.BLACK), 3, 4);
		getBoard().set(new Pawn(Player.WHITE), 3, 5);
		assertTrue(getPiece().isValidMove(new Move(4, 4, 2, 3), getBoard()));
	}
	
	@Test
	public final void cantMoveInRow() throws Exception {
		getBoard().set(getPiece(), 4, 4);
		assertFalse(getPiece().isValidMove(new Move(4, 4, 4, 3), getBoard()));
	}

	@Test
	public final void cantMoveInCol() throws Exception {
		getBoard().set(getPiece(), 4, 4);
		assertFalse(getPiece().isValidMove(new Move(4, 4, 3, 4), getBoard()));
	}
}
