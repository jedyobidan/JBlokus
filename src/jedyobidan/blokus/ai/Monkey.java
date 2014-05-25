package jedyobidan.blokus.ai;

import java.util.Random;

import jedyobidan.blokus.core.Move;

public class Monkey extends AIPlayer {
	public Monkey(int pid) {
		super(pid, "Monkey_" + pid);
	}

	@Override
	public Move selectMove() {
		Random r = new Random();
		return possibleMoves.get(r.nextInt(possibleMoves.size()));
	}

}
