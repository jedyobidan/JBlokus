package jedyobidan.blokus;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import jedyobidan.blokus.setup.LocalGameSetup;
import jedyobidan.io.JImageIO;
import jedyobidan.ui.nanim.Command;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;
import jedyobidan.ui.nanim.actors.Button;

public class TitleScreen extends Stage{
	private Button local, help;
	private Instructions instructions;
	private static BufferedImage bg;
	static{
		try {
			bg = JImageIO.readImage(TitleScreen.class, "title_screen.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public TitleScreen(Display d) {
		super(d);
		instructions = new Instructions();
		local = new Button(Gui.WIDTH/2 - 100, Gui.HEIGHT/2+30, 200, 30, "Local Play", new Command(){
			public void execute(){
				startLocalGame();
			}
		});
		help = new Button(Gui.WIDTH/2 - 100, Gui.HEIGHT/2 + 65, 200, 18, Color.gray, Color.white, "Help", new Command(){
			public void execute(){
				instructions.open();
			}
		});
		addActor(local);
		addActor(help);
		addActor(instructions);
		setBg(bg);
	}
	
	private void startLocalGame(){
		Display d = getDisplay();
		d.addStage("SETUP", new LocalGameSetup(d));
	}
	
	
	public void processInput(Controller c){
		instructions.processInput(c);
		local.processInput(c);
		help.processInput(c);
	}
	
}
