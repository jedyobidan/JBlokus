package jedyobidan.blokus.setup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import jedyobidan.blokus.Gui;
import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.ui.nanim.actors.SlideSelector;

public class AISelector extends SlideSelector {
	public AISelector() {
		super(Gui.WIDTH - 125, 10, 100, 18);
		for(String ai: AIPlayer.AI_LEVELS){
			addOption(ai);
		}
	}
	
	public void render(Graphics2D g){
		g.setFont(Font.decode(null).deriveFont(Font.BOLD).deriveFont(12f));
		g.setColor(Color.black);
		g.drawString("AI Level:", Gui.WIDTH - 189, 23);
		super.render(g);
	}
}
