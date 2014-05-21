package jedyobidan.blokus.network;

import jedyobidan.net.Message;


public class PlayerData extends Message{
	private static final long serialVersionUID = 1L;
	public final String name;
	public final int playerNum;
	public final int clientID;
	public PlayerData(String name, int pNum, int clientID){
		super(0);
		this.name = name;
		this.playerNum = pNum;
		this.clientID = clientID;
	}
	
	public String toString(){
		return "PlayerData("+name+"," + playerNum + "," + clientID + ")";
	}
}
