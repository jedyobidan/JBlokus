package jedyobidan.blokus.setup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;

import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.GameObserver;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.local.GameStage;
import jedyobidan.ui.nanim.Actor;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;
import jedyobidan.ui.nanim.actors.Label;

public abstract class GameSetup extends Stage implements GameObserver{
	protected Player[] players;
	protected GameModel game;
	public GameSetup(Display d) {
		super(d);
		addActor(new Label("Game Setup", 7, 10, Font.decode(null).deriveFont(Font.BOLD).deriveFont(18f), Color.black));
		players = new Player[4];
		initializePlayers();
	}
	
	
	public void beforeStep(){
		for(Actor a: actors){
			if(a instanceof PlayerInfo){
				removeActor(a);
			}
		}
		for(int i = 0; i < 4; i++){
			addActor(new PlayerInfo(40+45*i, players[i]));
		}
		super.beforeStep();
	}
	
	public void addListeners(GameModel game){
		
	}
	
	public void stopGame(){
		if(game!= null)
			game.stop();
	}
	
	public void startGame(){
		game = new GameModel();
		SwingUtilities.getWindowAncestor(getDisplay()).addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				stopGame();
			}
		});
		for(Player p: players){
			game.addPlayer(p);
		}
		game.addObserver(this);
		addListeners(game);
		
		GameStage stage = new GameStage(getDisplay());
		for(Player p: game.getPlayers()){
			p.getDock().addToStage(stage);
		}
		game.addObserver(stage);
		this.getDisplay().addStage("GAME", stage);
		
		new Thread(){
			public void run(){
				setPriority(Thread.MIN_PRIORITY);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.startGame();
			}
		}.start();
		
	}
	
	public abstract void initializePlayers();
	
	public abstract void reinitPlayers();
	
	protected int getAvailable(){
		for(int i = 0; i < 4; i++){
			if(players[i] instanceof AIPlayer){
				return i;
			}
		}
		return -1;
	}
	
	public void gameStart(){
		
	}


	@Override
	public void turnStart(Player p) {
		
	}


	@Override
	public void moveMade(Move m) {
		
	}


	@Override
	public void noMoves(Player p) {
		
	}


	@Override
	public void gameEnd(GameModel game) {
		game = null;
	}
	
	
	
}
