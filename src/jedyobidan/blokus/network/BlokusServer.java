package jedyobidan.blokus.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.game.*;
import jedyobidan.blokus.setup.ServerSetup;
import jedyobidan.net.Message;
import jedyobidan.net.Server;

public class BlokusServer extends Server {
	private Player[] players;
	private PlayerData[] playerData;
	private String aiLevel;
	private ServerSetup serverSetup;
	private int port;
	
	public static final int DEFAULT_PORT = 4031;
	
	public BlokusServer(String aiLevel, int port) throws IOException {
		super();
		this.aiLevel = aiLevel;
		this.port = port;
		players = new Player[4];
		playerData = new PlayerData[4];
		for(int i = 0; i < 4; i++){
			AIPlayer p = AIPlayer.createAI(aiLevel, i);
			players[i] = p;
			playerData[i] = new PlayerData(p.getName(), i, 0);
		}
		serverSetup = new ServerSetup(this);
		serverSetup.setVisible(true);
		System.out.println("SERVER: JBlockus Server initialized");
		this.acceptConnections(port);
	}
	
	public void clientJoined(ClientAgent agent){
		super.clientJoined(agent);
		for(PlayerData message: playerData){
			agent.sendMessage(message);
		}
	}
	
	public void clientQuit(ClientAgent agent){
		super.clientQuit(agent);
		dropPlayer(agent.getClientID());
	}
	
	public void messageRecieved(Message m){
		super.messageRecieved(m);
		if(m instanceof JoinRequest){
			if(!addRemotePlayer(((JoinRequest) m).playerName, m.origin)){
				sendMessage(m.origin, new JoinFailed());
			}
		} else if (m instanceof DropRequest){
			dropPlayer(m.origin);
		}
	}
	
	public boolean addRemotePlayer(String playerName, int clientID){
		int pid = getAvailable();
		if(pid < 0) return false;
		RemotePlayer player = new RemotePlayer(pid, playerName);
		addObserver(player);
		players[pid] = player;
		playerData[pid] = new PlayerData(playerName, pid, clientID);
		broadcastMessage(playerData[pid]);
		serverSetup.setPlayer(playerData[pid]);
		System.out.println("SERVER: Added " + playerData[pid]);
		return true;
	}
	
	public void dropPlayer(int clientID){
		for(int i = 0; i < 4; i++){
			if(playerData[i].clientID == clientID){
				removeObserver((RemotePlayer) players[i]);
				players[i] = AIPlayer.createAI(aiLevel, i);
				playerData[i] = new PlayerData(players[i].getName(), i, 0);
				broadcastMessage(playerData[i]);
				serverSetup.setPlayer(playerData[i]);
				System.out.println("SERVER: Player " + i + " dropped");
			}
		}
	}
	
	public void startGame(){
		
	}
	
	protected int getAvailable(){
		for(int i = 0; i < 4; i++){
			if(players[i] instanceof AIPlayer){
				return i;
			}
		}
		return -1;
	}
	
	public PlayerData[] getPlayerData(){
		return playerData;
	}
	
	public int getPort(){
		return port;
	}
	
	public String getAILevel(){
		return aiLevel;
	}

}
