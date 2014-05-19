package jedyobidan.blokus.setup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ListIterator;

import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.game.GameStage;
import jedyobidan.blokus.game.Player;
import jedyobidan.ui.nanim.Actor;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;

public abstract class GameSetup extends Stage{
	protected Player[] players;
	public GameSetup(Display d) {
		super(d);
		addActor(new Actor(){
			public void onStep() {}
			public void render(Graphics2D g) {
				g.setFont(Font.decode(null).deriveFont(Font.BOLD).deriveFont(18f));
				g.setColor(Color.black);
				g.drawString("Game Setup", 7, 26);
			}
			public Shape getHitbox() {return null;}
			
		});
		players = new Player[4];
		initializePlayers();
	}
	
	public void beforeStep(){
		ListIterator<Actor> it = actors.listIterator();
		while(it.hasNext()){
			if(it.next() instanceof PlayerInfo){
				it.remove();
			}
		}
		for(int i = 0; i < 4; i++){
			addActor(new PlayerInfo(40+45*i, players[i]));
		}
		super.beforeStep();
	}
	
	public void gameStart(){
		GameStage stage = (GameStage)getDisplay().getStage("GAME");
		for(Player p: players){
			stage.addPlayer(p);
		}
		this.getDisplay().setStage("GAME");
		stage.startGame();
	}
	
	public abstract void initializePlayers();
	
	protected int getAvailable(){
		for(int i = 0; i < 4; i++){
			if(players[i] instanceof AIPlayer){
				return i;
			}
		}
		return -1;
	}
}
