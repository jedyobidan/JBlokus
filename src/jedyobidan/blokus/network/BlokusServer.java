package jedyobidan.blokus.network;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.GameObserver;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.setup.ServerSetup;
import jedyobidan.net.Message;
import jedyobidan.net.Server;

public class BlokusServer extends Server implements GameObserver{
	private Player[] players;
	private PlayerData[] playerData;
	private boolean[] readyState;
	private String aiLevel;
	private ServerSetup serverSetup;
	private int port;
	private GameModel game;
	
	public static final int DEFAULT_PORT = 4031;
	
	public BlokusServer(String aiLevel, int port) throws IOException {
		super();
		this.aiLevel = aiLevel;
		this.port = port;
		this.writeToLog = true;
		players = new Player[4];
		playerData = new PlayerData[4];
		readyState = new boolean[4];
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
		} else if (m instanceof PlayerListRequest){
			for(PlayerData message: playerData){
				sendMessage(m.origin, message);
			}
			for(int i = 0; i < 4; i++){
				sendMessage(m.origin, new ReadyMessage(0, i, readyState[i]));
			}
		} else if (m instanceof ReadyMessage){
			readyState[((ReadyMessage) m).pnum] = ((ReadyMessage) m).ready;
			serverSetup.setReady(((ReadyMessage) m).pnum, ((ReadyMessage) m).ready);
			broadcastMessage(m);
			
			boolean start = true;
			for(int i = 0; i < 4; i++){
				if(!readyState[i] && !(players[i] instanceof AIPlayer)){
					start = false;
				}
			}
			if(start){
				startGame();
			}
		}
	}
	
	public void unready(){
		for(int i = 0; i < 4; i++){
			readyState[i] = false;
			serverSetup.setReady(i, false);
			broadcastMessage(new ReadyMessage(0,i, false));
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
		unready();
		return true;
	}
	
	public void dropPlayer(int clientID){
		for(int i = 0; i < 4; i++){
			if(playerData[i].clientID == clientID){
				RemotePlayer old = (RemotePlayer)players[i];
				removeObserver(old);
				players[i] = AIPlayer.createAI(aiLevel, i);
				playerData[i] = new PlayerData(players[i].getName(), i, 0);
				if(game==null) {
					broadcastMessage(playerData[i]);
					serverSetup.setPlayer(playerData[i]);
				} else {
					old.aiOverride();
					broadcastMessage(new PlayerDropped(old));
					serverSetup.appendMessage(old.getName() + " has left the game!");
				}
				System.out.println("SERVER: Player " + i + " dropped");
				unready();
			}
		}
	}
	
	public void startGame(){
		stopAccepting();
		serverSetup.disable();
		game = new GameModel();
		serverSetup.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent w){
				if(game!=null) game.stop();
			}
		});
		for(Player p: players){
			game.addPlayer(p);
		}
		game.addObserver(this);
		game.addObserver(serverSetup);
		
		
		game.startGame();
		broadcastMessage(new GameStart());
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

	@Override
	public void turnStart(Player p) {
		
	}

	@Override
	public void moveMade(Move m) {
		broadcastMessage(new MoveMessage(0, m));
	}

	@Override
	public void noMoves(Player p) {
		
	}

	@Override
	public void gameEnd(GameModel game) {
		acceptConnections(port);
		this.game = null;
		unready();
		for(int i = 0; i < players.length; i++){
			if(players[i] instanceof RemotePlayer){
				removeObserver((RemotePlayer)players[i]);
				RemotePlayer player = new RemotePlayer(i, players[i].getName());
				addObserver(player);
				players[i] = player;
			} else {
				players[i] = AIPlayer.createAI(aiLevel, i);
			}
		}
		for(PlayerData p: playerData){
			serverSetup.setPlayer(p);
		}
	}

	@Override
	public void gameStart() {
		
	}

}
