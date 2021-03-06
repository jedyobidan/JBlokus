package jedyobidan.blokus.setup;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.local.LocalPlayer;
import jedyobidan.ui.nanim.Command;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.actors.Button;

public class LocalGameSetup extends GameSetup{
	private AISelector aiLevel;
	private JoinWidget join;
	private Button reset, start;
	private String currAILevel;
	public LocalGameSetup(Display d) {
		super(d);
		aiLevel = new AISelector();
		addActor(aiLevel);
		join = new JoinWidget(){
			@Override
			public void execute() {
				if(getJoinName().isEmpty()) return;
				int playerID = getAvailable();
				if(playerID < 0) return;
				players[playerID] = new LocalPlayer(playerID, getJoinName());
				clearName();
			}
		};
		addActor(join);
		
		start = new Button(ClientLaunch.WIDTH-70, ClientLaunch.HEIGHT - 28, 60, 18, "Start", new Command(){
			@Override
			public void execute() {
				startGame();
			}
		});
		addActor(start);
		
		reset = new Button(ClientLaunch.WIDTH-140, ClientLaunch.HEIGHT - 28, 60, 18, new Color(192,0,0), Color.white, "Reset", new Command(){
			@Override
			public void execute() {
				initializePlayers();
			}			
		});
		addActor(reset);
		
		currAILevel = getAILevel();
	}
	
	public void beforeStep(){
		if(!currAILevel.equals(getAILevel())){
			for(int i = 0; i < 4; i++){
				if(players[i] instanceof AIPlayer){
					players[i] = AIPlayer.createAI(getAILevel(), i);
				}
			}
			currAILevel = getAILevel();
		}
		super.beforeStep();
	}
	
	public void processInput(Controller c){
		join.processInput(c);
		aiLevel.processInput(c);
		start.processInput(c);
		reset.processInput(c);
		c.consumeAll();
	}

	@Override
	public void initializePlayers() {
		for(int i = 0; i < 4; i++){
			players[i] = AIPlayer.createAI(getAILevel(), i);
		}
	}
	
	public void reinitPlayers(){
		for(int i = 0; i < 4; i++){
			if(players[i] instanceof AIPlayer){
				players[i] = AIPlayer.createAI(getAILevel(), i);
			} else {
				players[i] = new LocalPlayer(i, players[i].getName());
			}
		}
	}
	
	public String getAILevel(){
		if(aiLevel!= null) return aiLevel.getOption();
		else return AIPlayer.AI_LEVELS[0];
	}
	
	private int getAvailable(){
		ArrayList<Integer> av = new ArrayList<>();
		for(int i = 0; i < 4; i++){
			if(players[i] instanceof AIPlayer){
				av.add(i);
			}
		}
		if(av.size() > 0){
			return av.get(new Random().nextInt(av.size()));
		} else {
			return -1;
		}
	}
	
}

