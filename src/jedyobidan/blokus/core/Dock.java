package jedyobidan.blokus.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.blokus.local.Board;
import jedyobidan.blokus.local.LocalPlayer;
import jedyobidan.ui.nanim.Actor;
import jedyobidan.ui.nanim.AdvancedKey;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Stage;

/**
 * Represents a Player's hand.
 * Responsible for the connection between LocalPlayer and the GUI; 
 * aidls LocalPlayer in making moves.
 * @author Young
 *
 */
public class Dock extends Actor{
	private ArrayList<Piece> pieces;
	private Piece selectedPiece;
	private Player player;
	private double x;
	private int dx;
	
	private double timePassed;
	
	public static final int X = Board.X + Board.SIZE + 5, WIDTH = 250;
	
	public Dock(Collection<Piece> pieces, Player player){
		this.pieces = new ArrayList<Piece>(pieces);
		this.player = player;
		x = ClientLaunch.WIDTH + 10;
	}

	
	protected void setStage(Stage s){
		super.setStage(s);
		if(s == null) return;
		for(Piece p: pieces){
			s.addActor(p);
		}
	}
	
	public void openDock(){
		dx = -2000;
	}
	
	public void closeDock(){
		dx = 2000;
	}
	
	public void processInput(Controller c){
		if(x != X || !(player instanceof LocalPlayer)) return;
		ListIterator<Point> ms = c.getMousePressed().listIterator();
		while(ms.hasNext()){
			Point p = ms.next();
			if(p.x < X + 10) continue;
			ms.remove();
			int px = (int) ((p.getX()-x-10)/80);
			int py = (int) ((p.getY()-5))/56;
			int i = px*7+py;
			if(!pieces.get(i).isFinalized()){
				selectedPiece = pieces.get(i); 
				selectedPiece.zIndex = 1;
			}
		}
		if(selectedPiece == null) return;
		selectedPiece.offset(c.getMousePosition().x, c.getMousePosition().y);	
		while(!c.getKeysPressed().isEmpty()){
			AdvancedKey k = c.getKeysPressed().remove(0);
			switch(k.keyCode){
			case KeyEvent.VK_Q: 
				selectedPiece.rotateCW(); break;
			case KeyEvent.VK_E: 
				selectedPiece.rotateCCW(); break;
			case KeyEvent.VK_A: case KeyEvent.VK_D: 
				selectedPiece.flipHorizontal(); break;
			case KeyEvent.VK_W: case KeyEvent.VK_S: 
				selectedPiece.flipVertical(); break;
			}
		}
		while(!c.getMouseReleased().isEmpty()){
			mouseReleased(c.getMouseReleased().remove(0));
		}
		
	}
	
	private void mouseReleased(Point p){
		Move m = new Move(selectedPiece, Board.getGridCoord(p), player);
		if(m.legal(player.getGame().getBoard())){
			player.makeMove(m);
		}
		selectedPiece = null;
	}
	
	@Override
	public void onStep() {
		if(x == X){
			timePassed += getStage().getDeltaSeconds();
		}
		if(selectedPiece != null || x != X){
			timePassed = 0;
		}
		x+= dx * getStage().getDeltaSeconds();
		if(x < X){
			x = X;
			dx = 0;
		}
		if(x > ClientLaunch.WIDTH + 10){
			x = ClientLaunch.WIDTH + 10;
			dx = 0;
		}

		int i = 0;
		for(int col = 0; col < 3; col++){
			for(int row = 0; row < 7; row++){
				if(!pieces.get(i).isFinalized() && pieces.get(i) != selectedPiece){
					pieces.get(i).resetRotation();
					pieces.get(i).offset((int)x+10+col*80 + 40, 5+row*56 + 28);
				}
				i++;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		Color color = player.getColor();
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		hsb[1] *= 0.25;
		
		Color bg = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		g.setColor(bg);
		g.fillRect((int)x, 0, WIDTH, ClientLaunch.HEIGHT);
		g.setColor(color);
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(2));
		g.drawLine((int)x, 0, (int)x, ClientLaunch.HEIGHT);
		g.setStroke(s);
		
		if(player instanceof LocalPlayer && timePassed > 5 && timePassed%1 < 0.5){
			Move m = ((LocalPlayer)player).getHint();
			Piece p = m.getNewPiece();
			p.render(g, new Color(210,210,210));
		}
	}
	

	@Override
	public Shape getHitbox() {
		return null;
	}

}
