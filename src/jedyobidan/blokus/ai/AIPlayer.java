package jedyobidan.blokus.ai;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;

public abstract class AIPlayer extends Player implements Runnable{
	public static final String[] AI_LEVELS = {"Random", "Easy"};
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
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		makeMove(selectMove());
	}
	
	public abstract Move selectMove();
	
	public static AIPlayer createAI(String type, int pid){
		switch(type){
		case "Random": return new RandomAI(pid);
		case "Easy": return new EasyAI(pid);
		}
		return null;
	}
	
}
