package jedyobidan.blokus.ai;

import java.util.ArrayList;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;

public abstract class AIPlayer extends Player implements Runnable{
	public static final String[] AI_LEVELS = {"Random"};
	public AIPlayer(int pid, String name) {
		super(pid, name);
	}

	@Override
	public void requestMove() {
		Thread t = new Thread(this);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	@Override
	public void run() {
		ArrayList<Move> moves = getPossibleMoves();
		makeMove(selectMove(moves));
	}
	
	public abstract Move selectMove(ArrayList<Move> possibleMoves);
	
	public static AIPlayer createAI(String type, int pid){
		if(type.equals("Random")){
			return new RandomAI(pid);
		}
		return null;
	}
	
}
