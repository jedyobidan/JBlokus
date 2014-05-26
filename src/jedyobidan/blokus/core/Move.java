package jedyobidan.blokus.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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



	public Piece getPiece(){
		return getPiece(null);
	}
	private Piece getPiece(GameModel game){
		Piece piece;
		if(game!=null){
			piece = game.getPiece(playerID, pieceType);
		} else {
			piece = new Piece(PieceData.getData(pieceType),playerID);
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
		Piece piece = getPiece(game);
		game.getBoard().addPiece(piece);
		piece.zIndex = 0;
	}
	
	public boolean legal(GameModel game){
		Piece piece = getPiece(null);
		return game.getBoard().canPlace(piece);
	}
	
	public String toString(){
		return "<" + playerName + "> " + pieceType + transformations + "@(" + placement.x + ", " + placement.y + ")";
	}
}
