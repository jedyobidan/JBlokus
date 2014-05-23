package jedyobidan.blokus.network;

import jedyobidan.net.Message;

public class ReadyMessage extends Message{
	private static final long serialVersionUID = -6085461774534147277L;
	public final int pnum;
	public final boolean ready;
	public ReadyMessage(int origin, int pnum, boolean ready) {
		super(origin);
		this.pnum = pnum;
		this.ready = ready;
	}

}
