package jedyobidan.blokus.core;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class BoardModel {
	public final HashSet<Point2D>[] corners;
	public final HashSet<Point2D>[] unusable;
	public final HashSet<Point2D>[] placedPoints;
	
	@SuppressWarnings("unchecked")
	public BoardModel(){
		corners = (HashSet<Point2D>[]) new HashSet[4];
		unusable = (HashSet<Point2D>[]) new HashSet[4];
		placedPoints = (HashSet<Point2D>[]) new HashSet[4];
		for(int i = 0; i < 4; i++){
			corners[i] = new HashSet<Point2D>();
			corners[i].add(getStartCorner(i));
			unusable[i] = new HashSet<Point2D>();
			placedPoints[i] = new HashSet<Point2D>();
		}
	}
	
	public void addPiece(Piece p){
		if(!canPlace(p)){
			throw new IllegalArgumentException("Illegal Move");
		}
		int pid = p.getPlayerID();
		for(Point2D point: p.getPlacedPoints()){
			for(int i = 0; i < 4; i++){
				addUnusable(i, point);
			}
			placedPoints[pid].add(point);
		}
		for(Point2D point: p.getPlacedEdges()){
			addUnusable(pid, point);
		}
		for(Point2D point: p.getPlacedCorners()){
			addCorner(pid, point);
		}
	}
	
	private void addUnusable(int pid, Point2D point){
		unusable[pid].add(point);
		corners[pid].remove(point);
	}
	
	private void addCorner(int pid, Point2D point){
		if(!unusable[pid].contains(point)){
			corners[pid].add(point);
		}
	}
	
	
	public boolean canPlace(Piece p){
		boolean cornerTouch = false;	
		for(Point2D point: p.getPlacedPoints()){
			if(!inBounds(point)) return false;
			if(unusable[p.getPlayerID()].contains(point)) return false;
			if(corners[p.getPlayerID()].contains(point)) cornerTouch = true;
		}
		return cornerTouch;
	}
	
	private Point getStartCorner(int player){
		switch(player){
		case 0: return new Point(0,0);
		case 1: return new Point(19,0);
		case 2: return new Point(19,19);
		case 3: return new Point(0,19);
		}
		return null;
	}
	
	private boolean inBounds(Point2D p){
		return p.getX() >=0 && p.getX() < 20 && p.getY() >= 0 && p.getY() < 20;
	}
	
	public String toString(){
		String ans = "";
		for(int y = 0; y <20; y++){
			for(int x = 0; x < 20; x++){
				char c = ' ';
				Point p = new Point(x,y);
				for(int i = 0; i < 4; i++){
					if(placedPoints[i].contains(p)){
						c = '#';
					}
					if(corners[i].contains(p) && c == ' '){
						c = (char) ('0'+i);
					}
					if(unusable[i].contains(p) && c == ' '){
						c = (char) ('a' + i);
					}
				}
				ans+= c;
			}
			ans+="\n";
		}
		return ans;
	}
	
}
