package jedyobidan.blokus.game;

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

	public void execute(GameStage stage){
		Piece piece = stage.getPiece(playerID, pieceType);
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
		if(stage.board.canPlace(piece, placement)){
			stage.board.place(piece, placement);
		} else {
			throw new IllegalArgumentException("Illegal Move");
		}
		piece.zIndex = 0;
	}
	
	public String toString(){
		return "<" + playerName + "> " + pieceType + transformations + "@(" + placement.x + ", " + placement.y + ")";
	}
}
