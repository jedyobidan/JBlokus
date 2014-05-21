package jedyobidan.blokus.network;

import jedyobidan.net.Message;

public class PlayerListRequest extends Message{
	private static final long serialVersionUID = -6884387171958598785L;
	public PlayerListRequest(int origin) {
		super(origin);
	}

}
