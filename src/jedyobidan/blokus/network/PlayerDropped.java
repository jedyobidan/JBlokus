package jedyobidan.blokus.network;

import jedyobidan.blokus.core.Player;
import jedyobidan.net.Message;

public class PlayerDropped extends Message{
	public final int pid;
	public final String name;
	public PlayerDropped(Player p) {
		super(0);
		this.pid = p.playerID;
		this.name = p.getName();
	}

}
