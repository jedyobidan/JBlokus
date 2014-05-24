package jedyobidan.blokus.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import jedyobidan.blokus.core.Move;

public abstract class AbstractAI extends AIPlayer{
	public static final int MAX_WEIGHT = 100;
	protected float entropy;
	public AbstractAI(int pid, String name) {
		super(pid, name);
		entropy = 0.25f;
	}

	@Override
	public Move selectMove() {
		HashMap<Move, Double> scores = new HashMap<>();
		double bscore = -1;
		for(Move m: possibleMoves){
			double score = score(m);
			bscore = Math.max(bscore, score);
			scores.put(m, score);
		}
		ArrayList<Move> acceptableMoves = new ArrayList<>();
		for(Move m: scores.keySet()){
			int weight = (int)(weight(scores.get(m), bscore)*MAX_WEIGHT);
			for(int i = 0; i < weight; i++){
				acceptableMoves.add(m);
			}
		}
		Random r = new Random();
		return acceptableMoves.get(r.nextInt(acceptableMoves.size()));
	}
	
	//Returns a double from 0.0 to 1.0, representing the relative weight of the score.
	public double weight(double score, double bscore){
		return Math.pow(score/bscore, 1/entropy);
	}
	
	public abstract double score(Move m);

}
