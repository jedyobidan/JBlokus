package jedyobidan.blokus.ai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import jedyobidan.blokus.core.BoardModel;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;

public abstract class AbstractAI extends AIPlayer{
	public static final int MAX_WEIGHT = 40;
	protected float entropy;
	protected Opening opening;
	public AbstractAI(int pid, String name){
		this(pid, name, 0);
	}
	public AbstractAI(int pid, String name, float useOpening) {
		super(pid, name);
		entropy = 1f;
		if(Math.random() < useOpening){
			opening = selectOpening();
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
			if(m.legal(game)){
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
	public double weight(double score, double bscore){
		return Math.pow(score/bscore, 16/entropy);
	}
	
	public abstract double score(Move m);
	
	protected int blockedCorners(Piece p){
		int ct = 0;
		for(int i = 0; i < 4; i++){
			if(i== playerID) continue;
			HashSet<Point2D> corners = game.getBoard().corners[i];
			for(Point2D point: p.getPlacedPoints()){
				if(!BoardModel.inBounds(point)) continue;
				if(corners.contains(point)){
					ct++;
				}
			}
		}
		return ct;
	}
	
	protected int deltaCorners(Piece p){
		int ct = 0;
		HashSet<Point2D> corners = game.getBoard().corners[playerID];
		HashSet<Point2D> unusable = game.getBoard().unusable[playerID];
		for(Point2D corner: p.getPlacedCorners()){
			if(!BoardModel.inBounds(corner)) continue;
			if(!unusable.contains(corner) && !corners.contains(corner)){
				ct++;
			}
		}
		for(Point2D point: p.getPlacedUnusable()){
			if(!BoardModel.inBounds(point)) continue;
			if(corners.contains(point)){
				ct--;
			}
		}
		return ct;
	}
	
	protected double centerCornerDist(Piece p){
		double dist = 20;
		HashSet<Point2D> corners = game.getBoard().corners[playerID];
		HashSet<Point2D> unusable = game.getBoard().unusable[playerID];
		for(Point2D point: p.getPlacedCorners()){
			if(!BoardModel.inBounds(point)) continue;
			if(corners.contains(point) || unusable.contains(point)) continue;
			dist= Math.min(dist,Math.abs(9.5 - point.getX()) + Math.abs(9.5 - point.getY()));
		}
		return dist;
	}

}
