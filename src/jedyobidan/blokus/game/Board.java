package jedyobidan.blokus.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.HashSet;

import jedyobidan.ui.nanim.Actor;

public class Board extends Actor{
	public static final int X = 5, Y = 5, SIZE = 320;
	private HashSet<Piece> pieces;
	
	public Board(){
		zIndex = -1;
		pieces = new HashSet<>();
	}
	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				int renderX = X + i*16;
				int renderY = Y + j*16;
				g.setColor(Color.gray);
				g.fillRect(renderX-1, renderY-1, 17, 17);
				g.setColor(new Color(220,220,220));
				g.fillRect(renderX, renderY, 15, 15);
			}
		}
		g.setColor(Player.getColor(0));
		g.drawRect(X-1, Y-1, 16, 16);
		g.setColor(Player.getColor(1));
		g.drawRect(X+16*19-1, Y-1, 16, 16);
		g.setColor(Player.getColor(2));
		g.drawRect(X+16*19-1, Y+16*19-1, 16, 16);
		g.setColor(Player.getColor(3));
		g.drawRect(X-1, Y+16*19-1, 16, 16);
	}

	@Override
	public Shape getHitbox() {
		return null;
	}
	
	public Point getGridCoord(Point screenCoord){
		int x = (screenCoord.x-X)/16;
		int y = (screenCoord.y - Y)/16;
		return new Point(x,y);
	}
	
	public void place(Piece p, Point gridCoord){
		p.move(gridCoord.x*16+X+8, gridCoord.y*16+Y+8);
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
	
	public void canPlace(){
		
	}
}
