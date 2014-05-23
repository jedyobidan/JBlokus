package jedyobidan.blokus.ai;

import java.util.Random;

import jedyobidan.blokus.core.Move;

public class RandomAI extends AIPlayer {
	public RandomAI(int pid) {
		super(pid, "RandomAI_" + pid);
	}

	@Override
	public Move selectMove() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random r = new Random();
		return possibleMoves.get(r.nextInt(possibleMoves.size()));
	}

}
