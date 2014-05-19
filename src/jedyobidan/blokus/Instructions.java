package jedyobidan.blokus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;

import jedyobidan.ui.nanim.Actor;
import jedyobidan.ui.nanim.Controller;

public class Instructions extends Actor{
	private int dh;
	private int height;
	final int MAX_HEIGHT = 100, WIDTH = 250;
	public Instructions(){
		zIndex = 1;
	}
	@Override
	public void onStep() {
		height+= dh * getStage().getDeltaSeconds();
		if(dh > 0 && height > MAX_HEIGHT){
			height = MAX_HEIGHT;
			dh = 0;
		}
		if(dh < 0 && height < 0){
			height = 0;
			dh = 0;
		}
	}
	
	public void open(){
		dh = 750;
	}
	
	public void close(){
		dh = -1000;
	}
	
	public void processInput(Controller c){
		if(height == 0) return;
		if(!c.getMousePressed().isEmpty()){
			close();
		}
		c.consumeAll();
	}

	@Override
	public void render(Graphics2D g) {
		int y = Gui.HEIGHT/2 - height/2;
		int x = Gui.WIDTH/2-WIDTH/2;
		g.setClip(x, y, WIDTH, height);
		g.setColor(new Color(0,0,0,192));
		g.fillRect(x, y, WIDTH, height);
		g.setFont(Font.decode(null));
		g.setColor(Color.white);
		int strHeight = g.getFontMetrics().getHeight();
		y+= g.getFontMetrics().getAscent()+2;
		for(String s: getText().split("\\n")){
			g.drawString(s, x+2, y);
			y+= strHeight;
		}
		y = Gui.WIDTH/2 - MAX_HEIGHT/2 + g.getFontMetrics().getAscent();
		g.setClip(null);
	}
	
	public String getText(){
		return "Controls:\n"
				+ "Click and Drag to move pieces\n"
				+ " - Q/E while holding to rotate\n"
				+ " - A/D while holding to flip horizontally\n"
				+ " - W/S while holding to flip vertically\n"
				+ "Release mouse to place piece";
	}

	@Override
	public Shape getHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

}
