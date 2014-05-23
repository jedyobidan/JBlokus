package jedyobidan.blokus.network;

import jedyobidan.blokus.core.Player;
import jedyobidan.net.Message;

public class PlayerDropped extends Message{
	private static final long serialVersionUID = -6085461774534147277L;
	public final int pid;
	public final String name;
	public PlayerDropped(Player p) {
		super(0);
		this.pid = p.playerID;
		this.name = p.getName();
	}

}
