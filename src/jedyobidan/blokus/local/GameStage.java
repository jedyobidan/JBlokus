package jedyobidan.blokus.local;

import java.awt.Font;
import java.util.ArrayList;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.blokus.core.Dock;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.GameObserver;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.network.PlayerDropped;
import jedyobidan.blokus.setup.GameSetup;
import jedyobidan.net.Message;
import jedyobidan.net.MessageObserver;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;
import jedyobidan.ui.nanim.actors.FPSDisplay;
import jedyobidan.ui.nanim.actors.MessageBox;
import jedyobidan.ui.nanim.actors.TextFlyBanner;

public class GameStage extends Stage implements GameObserver, MessageObserver{
	public static boolean VERBOSE = true;
	private MessageBox messageBox;
	private ArrayList<Dock> docks;
	
	public GameStage(Display d) {
		super(d);
		this.addActor(new Board());
		docks = new ArrayList<Dock>();
		
		addActor(new FPSDisplay());
		
		messageBox = new MessageBox(
				Board.X, Board.Y + Board.SIZE + 5, Board.SIZE, 
				ClientLaunch.HEIGHT - (Board.Y + Board.SIZE + 5) - 5,
				Font.decode("Consolas-12"));
		this.addActor(messageBox);
		
	}
	
	public void processInput(Controller c){
		for(Dock d: docks){
			d.processInput(c);
		}
		c.consumeAll();
	}
	
	public void addDock(Dock d){
		docks.add(d);
		addActor(d);
	}
	
	
	@Override
	public void turnStart(Player p) {
		
	}

	@Override
	public void moveMade(Move m) {
		if(VERBOSE) messageBox.addMessage(m.toString());
	}

	@Override
	public void noMoves(Player p) {
		messageBox.addMessage(p.getName() + " has no moves!");
		
	}

	@Override
	public void gameEnd(GameModel game) {
		messageBox.addMessage("SCORE");
		for(Player p: game.getPlayers()){
			messageBox.addMessage(p.getName() + ": " + p.score());
		}
		addActor(new TextFlyBanner(2.5f, game.getWinner().getName() + " wins!"){
			@Override
			public void finish() {
				Display d = getStage().getDisplay();
				d.setStage("SETUP");
				((GameSetup) d.getStage()).reinitPlayers();
			}
		});
	}

	@Override
	public void gameStart() {
		addActor(new TextFlyBanner(1, "Game Start"){
					@Override
					public void finish() {
						
					}
		});
		
	}

	@Override
	public void messageRecieved(Message m) {
		if(m instanceof PlayerDropped){
			messageBox.addMessage(((PlayerDropped) m).name + " has left the game!");
		}
	}


	
}
