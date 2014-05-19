package jedyobidan.blokus.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jedyobidan.ui.nanim.Actor;

public class Piece extends Actor{
	public final PieceData data;
	private AffineTransform rotation;
	private ArrayList<String> transformations;
	private Point placed;
	private final Color color;
	private int x;
	private int y;
	
	public Piece(PieceData data, Color c){
		this.data = data;
		rotation = new AffineTransform();
		transformations = new ArrayList<String>();
		this.color = c;
	}
	
	public Piece getCopy(){
		return new Piece(data, color);
	}
	
	@Override
	public void render(Graphics2D g) {
		for(Point2D p: getRotationPoints()){
			int renderX, renderY;
			renderX = (int)(x + p.getX() * 16-8);
			renderY = (int)(y + p.getY() *16-8);
			g.setColor(color.darker());
			g.drawRect(renderX-1, renderY-1, 16, 16);
			Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 191);
			g.setColor(fill);
			g.fillRect(renderX, renderY, 15, 15);
		}
		
	}
	
	
	public Set<Point2D> getRotationPoints(){
		AffineTransform transform = new AffineTransform();
		transform.concatenate(rotation);
		
		HashSet<Point2D> ans = new HashSet<Point2D>();
		for(Point2D p: data.basePoints){
			ans.add(transform.transform(p, null));
		}
		return ans;
	}
	
	public Set<Point2D> getPlacedPoints(){
		AffineTransform transform = AffineTransform.getTranslateInstance(placed.x, placed.y);
		transform.concatenate(rotation);
		
		HashSet<Point2D> ans = new HashSet<Point2D>();
		for(Point2D p: data.basePoints){
			ans.add(transform.transform(p, null));
		}
		return ans;
	}
	
	public void move(int x, int y){
		if(placed!= null){
			throw new IllegalStateException("Piece has been placed and cannot be moved");
		}
		this.x = x;
		this.y = y;
	}

	
	void place(int xcoord, int ycoord){
		placed = new Point(xcoord, ycoord);
	}
	

	//ROTATION methods
	public void rotateCW(){
		AffineTransform t = AffineTransform.getRotateInstance(-Math.PI/2);
		t.concatenate(rotation);
		rotation = t;
		transformations.add("CW");
	}
	
	public void rotateCCW(){
		AffineTransform t = AffineTransform.getRotateInstance(Math.PI/2);
		t.concatenate(rotation);
		rotation = t;
		transformations.add("CCW");
	}
	
	public void flipHorizontal(){
		AffineTransform t = AffineTransform.getScaleInstance(-1, 1);
		t.concatenate(rotation);
		rotation = t;
		transformations.add("HF");
	}
	
	public void flipVertical(){
		AffineTransform t = AffineTransform.getScaleInstance(1, -1);
		t.concatenate(rotation);
		rotation = t;
		transformations.add("VF");
	}
	
	public void resetRotation(){
		rotation = new AffineTransform();
		transformations.clear();
	}
	
	
	public boolean isPlaced(){
		return placed!= null;
	}
	
	public String longString(){
		String ans = data.type + ":\n";
		for(int i = -2; i <= 2; i++){
			for(int j = -2; j <= 2; j++){
				if(data.basePoints.contains(new Point(j, i))){
					ans+="X";
				} else {
					ans+=" ";
				}
			}
			ans+= "\n";
		}
		return ans;
	}
	
	public String toString(){
		return data.type;
	}
	
	@Override
	public void onStep() {

	}

	@Override
	public Shape getHitbox() {
		return null;
	}
	
	public Color getColor(){
		return color;
	}

	public ArrayList<String> getTransformations() {
		return transformations;
	}
	
}
