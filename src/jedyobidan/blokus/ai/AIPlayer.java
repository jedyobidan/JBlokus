package jedyobidan.blokus.ai;

import java.util.ArrayList;

import jedyobidan.blokus.game.Move;
import jedyobidan.blokus.game.Player;

public abstract class AIPlayer extends Player implements Runnable{
	public AIPlayer(int pid) {
		super(pid, "");
		name = getClass().getSimpleName() + "_" + getColorName();
	}

	@Override
	public void requestMove() {
		Thread t = new Thread(this);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	@Override
	public void run() {
		ArrayList<Move> moves = getPossibleMoves(game.board);
		game.makeMove(selectMove(moves));
	}
	
	public abstract Move selectMove(ArrayList<Move> possibleMoves);
	
	public static AIPlayer createAI(String type, int pid){
		if(type.equals("Random")){
			return new RandomAI(pid);
		}
		return null;
	}
	
}
