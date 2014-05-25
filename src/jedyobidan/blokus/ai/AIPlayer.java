package jedyobidan.blokus.ai;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;

public abstract class AIPlayer extends Player implements Runnable{
	public static final String[] AI_LEVELS = {"Monkey", "Padawan","Apprentice", "Knight"};
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
		case "Monkey": return new Monkey(pid);
		case "Padawan": return new Padawan(pid);
		case "Apprentice": return new Apprentice(pid);
		case "Knight": return new Knight(pid);
		}
		throw new IllegalArgumentException("Not an AI type");
	}
	
}
