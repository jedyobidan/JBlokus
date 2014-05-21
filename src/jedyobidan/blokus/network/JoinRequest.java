package jedyobidan.blokus.network;

import jedyobidan.net.Message;

public class JoinRequest extends Message {
	private static final long serialVersionUID = 6535042036187256313L;
	public String playerName;
	public JoinRequest(int origin, String playerName) {
		super(origin);
		this.playerName = playerName;
	}

}
