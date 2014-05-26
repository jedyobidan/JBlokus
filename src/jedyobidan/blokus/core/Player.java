package jedyobidan.blokus.core;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Represents a single Player
 * Responsible for making moves.
 */
public abstract class Player {
	public final int playerID;
	private String name;
	private Dock dock;
	protected ArrayList<Move> possibleMoves;
	protected ArrayList<Piece> pieces;
	protected GameModel game;
	public Player(int pid, String name){
		this.name = name;
		playerID = pid;
		pieces = new ArrayList<Piece>();
		for(PieceData p: PieceData.getAllPieces()){
			pieces.add(new Piece(p, playerID));
		}
		dock = new Dock(pieces, this);
	}
	
	public String getName(){
		return name;
	}
	
	public abstract String type();
	
	public Color getColor(){
		return getColor(playerID);
	}
	
	public String getColorName(){
		switch(playerID){
		case 0: return "red";
		case 1: return "blue";
		case 2: return "green";
		case 3: return "yellow";
		}
		return null;
	}
	
	public static Color getColor(int playerID){
		switch(playerID){
		case 0: return Color.red;
		case 1: return Color.blue;
		case 2: return Color.green;
		case 3: return new Color(240,240,0);
		}
		return null;
	}
	
	public void joinGame(GameModel g){
		game = g;
	}
	
	public void startTurn(){
		dock.openDock();
		requestMove();
	}
	
	public void endTurn(){
		dock.closeDock();
	}

	public abstract void requestMove();
	
	public Piece getPiece(String type){
		for(Piece p: pieces){
			if(p.data.type.equals(type)){
				return p;
			}
		} 
		return null;
	}
	
	public Dock getDock(){
		return dock;
	}
	

	
	public ArrayList<Move> generatePossibleMoves(){
		ArrayList<Move> ans = new ArrayList<>();
		for(Piece pc: pieces){
			if(pc.isFinalized()) continue;
			for(int x = 0; x < 20; x++){
				for(int y = 0; y < 20; y++){
					Point place = new Point(x,y);
					Piece p = pc.getCopy();
					p.place(x, y);
					for(int i = 0; i < p.data.rotations; i++){
						if(game.getBoard().canPlace(p)){
							ans.add(new Move(p, place, this));
						}
						p.rotateCW();
					}
					p.resetRotation();
					if(p.data.flip){
						p.flipHorizontal();
						for(int i = 0; i < p.data.rotations; i++){
							if(game.getBoard().canPlace(p)){
								ans.add(new Move(p, place, this));
							}
							p.rotateCW();
						}
						p.resetRotation();
					}
				}
			}
		}
		possibleMoves = ans;
		return ans;
	}
	
	public int score(){
		int score = 0;
		for(Piece p: pieces){
			if(p.isFinalized()) score += p.data.basePoints.size();
		}
		return score;
	}
	
	public GameModel getGame(){
		return game;
	}
	
	public void makeMove(Move m){
		game.makeMove(m);
	}

}
