package jedyobidan.blokus.core;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the board = some state of the game
 * Is responsible for various game metrics and legality.
 * @author Young
 *
 */
public class BoardModel {
	public final HashSet<Piece> pieces;
	public final HashSet<Point2D>[] corners;
	public final HashSet<Point2D>[] unusable;
	public final HashSet<Point2D>[] placedPoints;
	public final HashSet<HashSet<Point>> zones;
	
	@SuppressWarnings("unchecked")
	public BoardModel(){
		pieces = new HashSet<>();
		corners = (HashSet<Point2D>[]) new HashSet[4];
		unusable = (HashSet<Point2D>[]) new HashSet[4];
		placedPoints = (HashSet<Point2D>[]) new HashSet[4];
		for(int i = 0; i < 4; i++){
			corners[i] = new HashSet<Point2D>();
			corners[i].add(getStartCorner(i));
			unusable[i] = new HashSet<Point2D>();
			placedPoints[i] = new HashSet<Point2D>();
		}
		zones = new HashSet<>();
	}
	
	public BoardModel(BoardModel model){
		this();
		for(int i = 0; i < 4; i++){
			this.corners[i].addAll(model.corners[i]);
			this.placedPoints[i].addAll(model.placedPoints[i]);
			this.unusable[i].addAll(model.unusable[i]);
		}
	}
	
	public void addPiece(Piece p){
		if(!canPlace(p)){
			throw new IllegalArgumentException("Illegal Move");
		}
		pieces.add(p);
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
		if(!inBounds(point)) return;
		unusable[pid].add(point);
		corners[pid].remove(point);
	}
	
	private void addCorner(int pid, Point2D point){
		if(!inBounds(point)) return;
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
	
	public static boolean inBounds(Point2D p){
		return p.getX() >=0 && p.getX() < 20 && p.getY() >= 0 && p.getY() < 20;
	}
	
	public String toString(){
		String ans = "";
		for(int y = -1; y <21; y++){
			for(int x = -1; x < 21; x++){
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
