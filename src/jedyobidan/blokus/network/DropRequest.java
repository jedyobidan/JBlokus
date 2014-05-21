package jedyobidan.blokus.network;

import jedyobidan.net.Message;

public class DropRequest extends Message{
	public DropRequest(int origin) {
		super(origin);
	}
}
