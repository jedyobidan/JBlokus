package jedyobidan.blokus.local;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;


public class LocalPlayer extends Player {
	public LocalPlayer(int pid, String name) {
		super(pid, name);
	}


	@Override
	public void requestMove() {
		
	}
	
	public String type(){
		return "Local Player";
	}
	
	public Move getHint(){
		return possibleMoves.get(possibleMoves.size()/2);
	}
}
