package jedyobidan.blokus.network;

import jedyobidan.net.Message;

public class DropRequest extends Message{
	private static final long serialVersionUID = -6085461774534147277L;

	public DropRequest(int origin) {
		super(origin);
	}
}
