package jedyobidan.blokus.core;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BoardMetrics {
	public BoardModel board;
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
	
	public HashSet<HashSet<Point>> getAllZones(){
		HashSet<HashSet<Point>> zones = new HashSet<>();
		HashSet<Point> points = new HashSet<>();
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				points.add(new Point(x,y));
			}
		}
		points.removeAll(board.covered);
		while(points.size() > 0){
			Point start = points.iterator().next();
			HashSet<Point> zone = getZone(start.x, start.y, Integer.MAX_VALUE);
			zones.add(zone);
			points.removeAll(zone);
		}
		return zones;
	}
	
	public HashSet<Point> getZone(int x, int y, int d){
		HashMap<Point, Integer> distance = new HashMap<>();
		HashSet<Point> black = new HashSet<>();
		Queue<Point> gray = new LinkedList<>();
		Point start = new Point(x,y);
		gray.add(start);
		distance.put(start, 0);
		while(!gray.isEmpty()){
			Point p = gray.poll();
			if(distance.get(p) < d){
				ArrayList<Point> adj = new ArrayList<>();
				adj.add(new Point(p.x+1, p.y));
				adj.add(new Point(p.x-1, p.y));
				adj.add(new Point(p.x, p.y+1));
				adj.add(new Point(p.x, p.y-1));
				for(Point a: adj){
					if(BoardModel.inBounds(a) && !board.covered.contains(a) &&
							!gray.contains(a) && !black.contains(a)){
						distance.put(a, distance.get(p) + 1);
						gray.add(a);
					}
				}
			}
			black.add(p);
		}
		return black;
	}
	
	
	public double accessArea(){
		int accessArea = 0;
		for(HashSet<Point> zone: getAllZones()){
			int corners = 0;
			for(Point2D corner: board.corners[playerID]){
				if(zone.contains(corner)){
					accessArea+= zone.size()*Math.pow(0.5, corners++);
				}
			}
		}
		return accessArea;
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
