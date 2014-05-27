package jedyobidan.blokus;

import javax.swing.JFrame;

import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.local.GameStage;
import jedyobidan.blokus.local.LocalPlayer;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.ThreadFPSRunner;

public class AITest extends JFrame{
	public static Display display;
	public static final int WIDTH = 580, HEIGHT = 420;
	public AITest(){
		super("JBlokus");
		display = new Display(WIDTH, HEIGHT, new ThreadFPSRunner(60));
		TitleScreen title = new TitleScreen(display);
		display.addStage("TITLE", title);
		this.add(display);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		display.start();
		
	}
	public static void main(String[] args){
		AITest test = new AITest();
		
		GameModel game = new GameModel();
		game.addPlayer(AIPlayer.createAI("Master", 0));
		game.addPlayer(AIPlayer.createAI("Knight", 1));
		game.addPlayer(AIPlayer.createAI("Master", 2));
		game.addPlayer(AIPlayer.createAI("Knight", 3));
		
		GameStage stage = new GameStage(display);
		for(Player p: game.getPlayers()){
			stage.addDock(p.getDock());
		}
		game.addObserver(stage);
		
		display.addStage("GAME", stage);
		test.setVisible(true);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		game.startGame();
		
	}
}
