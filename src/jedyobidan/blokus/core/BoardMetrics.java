package jedyobidan.blokus.core;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;

public class BoardMetrics {
	private BoardModel board;
	private int playerID;
	public BoardMetrics(BoardModel b, int pid){
		this.board = b;
		this.playerID = pid;
	}
	
	public HashSet<Point2D> plusCorners(Piece p){
		HashSet<Point2D> ans = new HashSet<>();
		HashSet<Point2D> corners = board.corners[playerID];
		HashSet<Point2D> unusable = board.unusable[playerID];
		for(Point2D corner: p.getPlacedCorners()){
			if(!BoardModel.inBounds(corner)) continue;
			if(!unusable.contains(corner) && !corners.contains(corner)){
				ans.add(corner);
			}
		}
		return ans;
	}
	
	public HashSet<Point2D> minusCorners(Piece p){
		HashSet<Point2D> ans = new HashSet<>();
		HashSet<Point2D> corners = board.corners[playerID];
		for(Point2D point: p.getPlacedUnusable()){
			if(!BoardModel.inBounds(point)) continue;
			if(corners.contains(point)){
				ans.add(point);
			}
		}
		return ans;
	}
	
	
	public int deltaCorners(Piece p){
		return plusCorners(p).size() - minusCorners(p).size();
	}
	
	
	public int blockedCorners(Piece p){
		int ct = 0;
		for(int i = 0; i < 4; i++){
			if(i == playerID) continue;
			HashSet<Point2D> corners = board.corners[i];
			for(Point2D point: p.getPlacedPoints()){
				if(corners.contains(point)){
					ct++;
				}
			}
		}
		return ct;
	}
	
	
	
	public double centerCornerDist(Piece p){
		double dist = 20;
		HashSet<Point2D> corners = board.corners[playerID];
		HashSet<Point2D> unusable = board.unusable[playerID];
		for(Point2D point: p.getPlacedCorners()){
			if(!BoardModel.inBounds(point)) continue;
			if(corners.contains(point) || unusable.contains(point)) continue;
			dist= Math.min(dist,Math.abs(9.5 - point.getX()) + Math.abs(9.5 - point.getY()));
		}
		return dist;
	}
}
