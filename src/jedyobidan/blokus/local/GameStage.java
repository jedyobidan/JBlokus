package jedyobidan.blokus.local;

import java.awt.Font;
import java.awt.Graphics2D;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.blokus.core.Dock;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.GameObserver;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.setup.GameSetup;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;
import jedyobidan.ui.nanim.actors.FPSDisplay;
import jedyobidan.ui.nanim.actors.MessageBox;
import jedyobidan.ui.nanim.actors.TextFlyBanner;

public class GameStage extends Stage implements GameObserver{
	public static boolean VERBOSE = true;
	private MessageBox messageBox;
	private Dock currentDock;
	
	public GameStage(Display d) {
		super(d);
		this.addActor(new Board());
		
		addActor(new FPSDisplay());
		
		messageBox = new MessageBox(
				Board.X, Board.Y + Board.SIZE + 5, Board.SIZE, 
				ClientLaunch.HEIGHT - (Board.Y + Board.SIZE + 5) - 5,
				Font.decode("Consolas-12"));
		this.addActor(messageBox);
	}
	
	public void processInput(Controller c){
		if(currentDock!=null){
			currentDock.processInput(c);
		}
		c.consumeAll();
	}
	
	
	@Override
	public void turnStart(Player p) {
		System.out.println(p.getName());
		currentDock = p.getDock();
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
		currentDock = null;
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
		addActor(new TextFlyBanner(100, 1, 
				Font.decode(null).deriveFont(24f).deriveFont(Font.BOLD), 
				"Game Start"){
					@Override
					public void finish() {
						
					}
		});
		
	}


	
}
