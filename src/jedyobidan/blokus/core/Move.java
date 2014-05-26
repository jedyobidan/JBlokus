package jedyobidan.blokus.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the actions taken on a Piece to place it on the BoardModel.
 * Is responsible for manipulate Pieces.
 * @author Young
 *
 */
public class Move implements Serializable{
	private static final long serialVersionUID = 1L;
	public final String pieceType;
	public final ArrayList<String> transformations;
	public final Point placement;
	public final int playerID;
	public final String playerName;
	
	
	public Move(Piece piece, Point placement, Player player) {
		this(piece.data.type, piece.getTransformations(), placement, player);
	}
	
	public Move(String pieceType, List<String> transformations, Point place, Player player){
		this.pieceType = pieceType;
		this.transformations = new ArrayList<String>(transformations);
		this.placement = place;
		this.playerID = player.playerID;
		this.playerName = player.getName();
	}
	
	public void applyTransformation(Piece p){
		p.resetRotation();
		for(String s: transformations){
			if(s.equals("CW")){
				p.rotateCW();
			} else if (s.equals("CCW")){
				p.rotateCCW();
			} else if (s.equals("HF")){
				p.flipHorizontal();
			} else if (s.equals("VF")){
				p.flipVertical();
			}
		}
		p.place(placement.x, placement.y);
	}
	
	public boolean legal(BoardModel board){
		Piece piece = new Piece(PieceData.getData(pieceType), playerID);
		applyTransformation(piece);
		return board.canPlace(piece);
	}
	
	public Piece getNewPiece(){
		Piece p = new Piece(PieceData.getData(pieceType), playerID);
		applyTransformation(p);
		return p;
	}
	
	public String toString(){
		return "<" + playerName + "> " + pieceType + transformations + "@(" + placement.x + ", " + placement.y + ")";
	}
}
