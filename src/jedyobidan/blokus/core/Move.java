package jedyobidan.blokus.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Move implements Serializable{
	private static final long serialVersionUID = 1L;
	public final String pieceType;
	public final ArrayList<String> transformations;
	public final Point placement;
	public final int playerID;
	public final String playerName;
	
	
	public Move(Piece piece, Point placement, Player player) {
		this.pieceType = piece.data.type;
		this.transformations = new ArrayList<String>(piece.getTransformations());
		this.placement = placement;
		this.playerID = player.playerID;
		this.playerName = player.getName();
	}
	
	private Piece getPiece(GameModel game, boolean copy){
		Piece piece = game.getPiece(playerID, pieceType);
		if(copy){
			piece = piece.getCopy();
		}
		piece.resetRotation();
		for(String s: transformations){
			if(s.equals("CW")){
				piece.rotateCW();
			} else if (s.equals("CCW")){
				piece.rotateCCW();
			} else if (s.equals("HF")){
				piece.flipHorizontal();
			} else if (s.equals("VF")){
				piece.flipVertical();
			}
		}
		piece.place(placement.x, placement.y);
		return piece;
	}

	public void execute(GameModel game){
		Piece piece = getPiece(game, false);
		game.getBoard().addPiece(piece);
		piece.zIndex = 0;
	}
	
	public boolean legal(GameModel game){
		Piece piece = getPiece(game, true);
		return game.getBoard().canPlace(piece);
	}
	
	public String toString(){
		return "<" + playerName + "> " + pieceType + transformations + "@(" + placement.x + ", " + placement.y + ")";
	}
}
