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
		t.start();
	}

	@Override
	public void run() {
		makeMove(selectMove());
	}
	
	public abstract Move selectMove();
	
	public static AIPlayer createAI(String type, int pid){
		if(type.equals("Random")){
			return new RandomAI(pid);
		}
		return null;
	}
	
}
