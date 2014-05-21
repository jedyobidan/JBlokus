package jedyobidan.blokus.core;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class BoardModel {
	private HashSet<Piece> pieces;
	public BoardModel(){
		pieces = new HashSet<>();
	}
	
	public void place(Piece p, Point gridCoord){
		p.place(gridCoord.x, gridCoord.y);
		pieces.add(p);
	}
	
	
	public boolean canPlace(Piece p, Point gridCoord){
		boolean cornerTouch = false;
		boolean primeCorner = false;
		
		for(Point2D point: p.getRotationPoints()){
			Point t = new Point((int)point.getX() + gridCoord.x, (int)point.getY() + gridCoord.y);
			if(!inBounds(t)) return false;
			if(intersectsOthers(t)) return false;
			if(adjColor(t, p.getColor())) return false;
			if(cornerColor(t, p.getColor())) cornerTouch = true;
			if(primeCorner(t, p.getColor())) primeCorner = true;
		}
		if(cornerTouch){
			return true;
		} else {
			return primeCorner;
		}
	}
	
	private boolean primeCorner(Point p, Color c){
		if(c.equals(Player.getColor(0))){
			return p.equals(new Point(0,0));
		}
		if(c.equals(Player.getColor(1))){
			return p.equals(new Point(19,0));
		}
		if(c.equals(Player.getColor(2))){
			return p.equals(new Point(19,19));
		}
		if(c.equals(Player.getColor(3))){
			return p.equals(new Point(0,19));
		}
		return false;
	}
	
	private boolean inBounds(Point p){
		return p.x >=0 && p.x < 20 && p.y >= 0 && p.y < 20;
	}
	
	private boolean intersectsOthers(Point point){
		for(Piece piece: pieces){
			for(Point2D p: piece.getPlacedPoints()){
				if(p.getX() == point.x && p.getY() == point.y){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean adjColor(Point point, Color c){
		for(Piece piece: pieces){
			if(!piece.getColor().equals(c)) continue;
			for(Point2D p: piece.getPlacedPoints()){
				if(Math.abs(p.getX() - point.x) + Math.abs(p.getY()- point.y) == 1){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean cornerColor(Point point, Color c){
		for(Piece piece: pieces){
			if(!piece.getColor().equals(c)) continue;
			for(Point2D p: piece.getPlacedPoints()){
				if(Math.abs(p.getX() - point.x) * Math.abs(p.getY()- point.y) == 1){
					return true;
				}
			}
		}
		return false;
	}
}
