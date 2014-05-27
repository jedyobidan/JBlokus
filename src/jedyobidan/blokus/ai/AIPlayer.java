package jedyobidan.blokus.ai;

import java.util.Random;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;

public abstract class AIPlayer extends Player implements Runnable{
	public static final String[] AI_LEVELS = {"???", "Monkey", "Padawan","Apprentice", "Knight", "Master"};
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
	
	public String type(){
		return "Computer AI";
	}
	
	public abstract Move selectMove();
	
	public static AIPlayer createAI(String type, int pid){
		switch(type){
		case "Monkey": return new Monkey(pid);
		case "Padawan": return new Padawan(pid);
		case "Apprentice": return new Apprentice(pid);
		case "Knight": return new Knight(pid);
		case "Master": return new Master(pid);
		case "???": 
			return createAI(AI_LEVELS[new Random().nextInt(AI_LEVELS.length-1)+1], pid);
		}
		throw new IllegalArgumentException("Not an AI type");
	}
	
}
