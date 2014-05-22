package jedyobidan.blokus.network;

import jedyobidan.net.Message;

public class ReadyMessage extends Message{
	public final int pnum;
	public final boolean ready;
	public ReadyMessage(int origin, int pnum, boolean ready) {
		super(origin);
		this.pnum = pnum;
		this.ready = ready;
	}

}
