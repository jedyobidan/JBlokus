package jedyobidan.blokus;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import jedyobidan.blokus.network.BlokusServer;
import jedyobidan.blokus.setup.LocalGameSetup;
import jedyobidan.blokus.setup.OnlineGameSetup;
import jedyobidan.io.JImageIO;
import jedyobidan.net.Client;
import jedyobidan.ui.nanim.Command;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;
import jedyobidan.ui.nanim.actors.Button;
import jedyobidan.ui.nanim.actors.Label;
import jedyobidan.ui.nanim.actors.Textbox;

public class TitleScreen extends Stage{
	private Button local, help, online;
	private Textbox ip, port;
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
		online = new Button(ClientLaunch.WIDTH/2 +9 , ClientLaunch.HEIGHT/2+41, 90, 19, "Online Game", new Command(){
			public void execute(){
				startOnlineGame();
			}
		});
		local = new Button(ClientLaunch.WIDTH/2 - 100, ClientLaunch.HEIGHT/2+65, 200, 30, "Local Game", new Command(){
			public void execute(){
				startLocalGame();
			}
		});
		help = new Button(ClientLaunch.WIDTH/2 - 100, ClientLaunch.HEIGHT/2 + 100, 200, 18, Color.gray, Color.white, "Help", new Command(){
			public void execute(){
				instructions.open();
			}
		});
		
		addActor(new Label("Server IP:", ClientLaunch.WIDTH/2-100, ClientLaunch.HEIGHT/2 + 20));
		addActor(ip = new Textbox(ClientLaunch.WIDTH/2 - 35, ClientLaunch.HEIGHT/2 + 19, 135, 20));
		addActor(new Label("Port:", ClientLaunch.WIDTH/2-100, ClientLaunch.HEIGHT/2 + 42));
		addActor(port = new Textbox(ClientLaunch.WIDTH/2 - 35, ClientLaunch.HEIGHT/2 + 41, 40, 20));
		addActor(online);
		addActor(local);
		addActor(help);
		port.setText(BlokusServer.DEFAULT_PORT+"");
		addActor(instructions);
		setBg(bg);
	}
	
	private void startLocalGame(){
		Display d = getDisplay();
		d.addStage("SETUP", new LocalGameSetup(d));
	}
	
	private void startOnlineGame(){
		try{
			Client client = new Client(ip.getText(), Integer.parseInt(port.getText()));
			Display d = getDisplay();
			d.addStage("SETUP", new OnlineGameSetup(d, client));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void processInput(Controller c){
		instructions.processInput(c);
		local.processInput(c);
		help.processInput(c);
		ip.processInput(c);
		port.processInput(c);
		online.processInput(c);
		c.consumeAll();
	}
	
}
