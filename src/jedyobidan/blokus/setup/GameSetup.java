package jedyobidan.blokus.setup;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	private GameModel game;
	protected boolean[] ready;
	public GameSetup(Display d) {
		super(d);
		addActor(new Label("Game Setup", 7, 10, Font.decode(null).deriveFont(Font.BOLD).deriveFont(18f), Color.black));
		players = new Player[4];
		ready = new boolean[4];
		initializePlayers();
	}
	
	
	public void beforeStep(){
		for(Actor a: actors){
			if(a instanceof PlayerInfo){
				removeActor(a);
			}
		}
		for(int i = 0; i < 4; i++){
			addActor(new PlayerInfo(40+55*i, players[i], ready[i]));
		}
		super.beforeStep();
	}
	
	public GameModel getGameModel(){
		GameModel game = new GameModel();
		SwingUtilities.getWindowAncestor(getDisplay()).addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				stopGame();
			}
		});
		for(Player p: players){
			game.addPlayer(p);
		}
		game.addObserver(this);
		return game;
	}
	
	public GameStage getGameStage(GameModel game){
		GameStage stage = new GameStage(getDisplay());
		for(Player p: game.getPlayers()){
			stage.addDock(p.getDock());
		}
		game.addObserver(stage);
		return stage;
	}
	
	public void stopGame(){
		if(game!= null)
			game.stop();
	}
	
	public void startGame(){
		game = getGameModel();
		this.getDisplay().addStage("GAME", getGameStage(game));
		
		new Thread(){
			public void run(){
				setPriority(Thread.MIN_PRIORITY);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.startGame();
			}
		}.start();
		
	}
	
	public abstract void initializePlayers();
	
	public abstract void reinitPlayers();
	
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
		this.game = null;
	}
	
	
	
}
