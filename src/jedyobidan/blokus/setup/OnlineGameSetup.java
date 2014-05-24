package jedyobidan.blokus.setup;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.SwingUtilities;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.local.GameStage;
import jedyobidan.blokus.local.LocalPlayer;
import jedyobidan.blokus.network.BlokusClient;
import jedyobidan.blokus.network.DropRequest;
import jedyobidan.blokus.network.GameStart;
import jedyobidan.blokus.network.JoinRequest;
import jedyobidan.blokus.network.PlayerData;
import jedyobidan.blokus.network.PlayerListRequest;
import jedyobidan.blokus.network.ReadyMessage;
import jedyobidan.blokus.network.RemotePlayer;
import jedyobidan.net.Message;
import jedyobidan.net.MessageObserver;
import jedyobidan.ui.nanim.Command;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.actors.Button;

public class OnlineGameSetup extends GameSetup implements MessageObserver{
	private JoinWidget join;
	private Button drop, readyButton;
	private BlokusClient client;
	public OnlineGameSetup(Display d, BlokusClient c) {
		super(d);
		client = c;
		client.addObserver(this);
		join = new JoinWidget(){
			@Override
			public void execute() {
				if(getJoinName().isEmpty()) return;
				client.sendMessage(new JoinRequest(client.getClientID(), getJoinName()));
				this.clearName();
			}
		};
		addActor(join);
		drop = new Button(10, ClientLaunch.HEIGHT - 28, 60, 18, new Color(192,0,0), Color.white, "Drop out", new Command(){
			@Override
			public void execute() {
				client.sendMessage(new DropRequest(client.getClientID()));
			}
		});
		addActor(drop);
		drop.setVisible(false);
		client.sendMessage(new PlayerListRequest(client.getClientID()));
		SwingUtilities.getWindowAncestor(d).addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		readyButton = new Button(ClientLaunch.WIDTH-70, ClientLaunch.HEIGHT - 28, 60, 18, "Ready!", new Command(){
			@Override
			public void execute() {
				client.sendMessage(new ReadyMessage(client.getClientID(), getPlayerNumber(), true));
			}
		});
		addActor(readyButton);
		readyButton.setVisible(false);
	}

	public void initializePlayers() {
		for(int i = 0; i < 4; i++){
			players[i] = new RemotePlayer(i, "Please Wait...");
		}
	}
	
	public void reinitPlayers(){
		client.sendMessage(new PlayerListRequest(client.getClientID()));
	}

	@Override
	public void messageRecieved(Message m) {
		if(m instanceof PlayerData){
			PlayerData data = (PlayerData) m;
			Player old = players[data.playerNum];
			if(old instanceof RemotePlayer){
				client.removeObserver((MessageObserver) old);
			} else if (old instanceof LocalPlayer){
				readyButton.setVisible(false);
				drop.setVisible(false);
				join.setVisible(true);
			}
			if(data.clientID == client.getClientID()){
				players[data.playerNum] = new LocalPlayer(data.playerNum, data.name);
				join.setVisible(false);
				drop.setVisible(true);
			} else {
				RemotePlayer p = new RemotePlayer(data.playerNum, data.name);
				client.addObserver(p);
				players[data.playerNum] = p;
			}
		} else if (m instanceof GameStart){
			startGame();
		} else if (m instanceof ReadyMessage){
			ReadyMessage rm = (ReadyMessage) m;
			ready[rm.pnum] = rm.ready;
			if(rm.pnum == getPlayerNumber()){
				if(((ReadyMessage) m).ready){
					readyButton.setVisible(false);
				} else {
					readyButton.setVisible(true);
				}
			}
		}
	}
	
	public GameModel getGameModel(){
		GameModel game = super.getGameModel();
		client.setPlayerNum(getPlayerNumber());
		game.addObserver(client);
		return game;
	}
	
	private int getPlayerNumber(){
		for(int i = 0; i < 4; i++){
			if(players[i] instanceof LocalPlayer){
				return i;
			}
		}
		return -1;
	}
	
	public GameStage getGameStage(GameModel game){
		GameStage stage = super.getGameStage(game);
		client.addObserver(stage);
		return stage;
	}
	
	public void processInput(Controller c){
		if(actors.contains(drop)){
			drop.processInput(c);
		}
		if(actors.contains(join)){
			join.processInput(c);
		}
		if(actors.contains(readyButton)){
			readyButton.processInput(c);
		}
		c.consumeAll();
	}

}
