package jedyobidan.blokus.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import jedyobidan.blokus.core.BoardMetrics;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.Move;

public abstract class AbstractAI extends AIPlayer{
	public static final int MAX_WEIGHT = 40;
	protected float entropy;
	protected Opening opening;
	protected BoardMetrics boardmetrics;
	public AbstractAI(int pid, String name){
		this(pid, name, 0);
	}
	public AbstractAI(int pid, String name, float useOpening) {
		super(pid, name);
		entropy = 1f;
		if(Math.random() < useOpening){
			opening = selectOpening();
			if(opening!=null)
				System.out.println(name + " using " + opening.name);
		}
	}
	
	public Opening selectOpening(){
		return null;
	}

	@Override
	public Move selectMove() {
		if(opening!=null && opening.inOpening()){
			Move m = opening.getMove(this);
			if(m.legal(game.getBoard())){
				return m;
			} else {
				opening.abandon();
			}
		}
		HashMap<Move, Double> scores = new HashMap<>();
		double bscore = Double.NEGATIVE_INFINITY;
		for(Move m: possibleMoves){
			double score = score(m);
			if(score < 0){
				throw new IllegalArgumentException("Score less than 0:" + score);
			}
			bscore = Math.max(bscore, score);
			scores.put(m, score);
		}
		ArrayList<Move> acceptableMoves = new ArrayList<>();
		for(Move m: scores.keySet()){
			if(scores.get(m) < bscore*0.7) continue;
			int weight = (int)(weight(scores.get(m), bscore)*MAX_WEIGHT);
			for(int i = 0; i < weight; i++){
				acceptableMoves.add(m);
			}
		}
		Random r = new Random();
		Move m = acceptableMoves.get(r.nextInt(acceptableMoves.size()));
		System.out.println("bscore=" + bscore + " score=" + scores.get(m));
		return m;
	}
	
	//Returns a double from 0.0 to 1.0, representing the relative weight of the score.
	private double weight(double score, double bscore){
		return Math.pow(score/bscore, 16/entropy);
	}
	
	public abstract double score(Move m);
	
	public void joinGame(GameModel game){
		super.joinGame(game);
		if(game!= null)
			boardmetrics = new BoardMetrics(game.getBoard(), playerID);
	}
}
