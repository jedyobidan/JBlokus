package jedyobidan.blokus.setup;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.blokus.core.Player;
import jedyobidan.ui.nanim.Actor;

public class PlayerInfo extends Actor{
	private Player player;
	private int y;
	
	public static final int HEIGHT = 40;
	public static final Font FONT = Font.decode(null).deriveFont(Font.BOLD).deriveFont(16f);
	
	public PlayerInfo(int y, Player p){
		this.y = y;
		player = p;
	}
	
	@Override
	public void onStep() {

	}

	@Override
	public void render(Graphics2D g) {
		float[] hsb = Color.RGBtoHSB(player.getColor().getRed(), player.getColor().getGreen(), player.getColor().getBlue(), null);
		g.setColor(Color.getHSBColor(hsb[0], hsb[1]/2, hsb[2]));
		g.fillRect(5, y, ClientLaunch.WIDTH-10, HEIGHT);
		g.setColor(player.getColor());
		Stroke old = g.getStroke();
		g.setStroke(new BasicStroke(2));
		g.drawRect(5, y, ClientLaunch.WIDTH-10, HEIGHT);
		g.setStroke(old);
		
		g.setColor(Color.white);
		g.setFont(FONT);
		g.drawString(player.getName(), 10, y+28);
		g.setFont(Font.decode(null));
		String code = player.getClass().getSimpleName();
		g.drawString(code, ClientLaunch.WIDTH-12-g.getFontMetrics().stringWidth(code), y+2+g.getFontMetrics().getAscent());
	}

	@Override
	public Shape getHitbox() {
		return null;
	}
}
