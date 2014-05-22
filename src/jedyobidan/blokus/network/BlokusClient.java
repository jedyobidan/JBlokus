package jedyobidan.blokus.network;

import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.UnknownHostException;

import jedyobidan.blokus.TitleScreen;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.GameObserver;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.setup.GameSetup;
import jedyobidan.net.Client;
import jedyobidan.net.Message;
import jedyobidan.net.ServerQuit;
import jedyobidan.ui.nanim.Display;

public class BlokusClient extends Client implements GameObserver {
	private int playerNum;
	private Display display;
	public BlokusClient(String serverIp, int port, Display display) throws UnknownHostException,
			IOException {
		super(serverIp, port);
		this.display = display;
	}
	
	public void serverClosed(){
		((GameSetup)display.getStage("SETUP")).stopGame();
		
		display.setStage("TITLE");
		((TitleScreen)display.getStage("TITLE")).viewMessage("You have been disconnected\nfrom the server.");
	}
	
	public void setPlayerNum(int p){
		playerNum = p;
	}

	@Override
	public void turnStart(Player p) {
		
	}

	@Override
	public void moveMade(Move m) {
		if(m.playerID == playerNum){
			sendMessage(new MoveMessage(getClientID(), m));
		}
	}

	@Override
	public void noMoves(Player p) {
		
	}

	@Override
	public void gameEnd(GameModel game) {
		
	}

	@Override
	public void gameStart() {
		
	}

}
