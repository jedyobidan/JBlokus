package jedyobidan.blokus.local;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.HashSet;

import jedyobidan.blokus.core.Piece;
import jedyobidan.blokus.core.Player;
import jedyobidan.ui.nanim.Actor;

public class Board extends Actor{
	public static final int X = 5, Y = 5, SIZE = 320;
	
	public Board(){
		zIndex = -1;
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
	
	public static Point getGridCoord(Point screenCoord){
		int x = (screenCoord.x-X)/16;
		int y = (screenCoord.y - Y)/16;
		return new Point(x,y);
	}
	
}
