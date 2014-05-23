package jedyobidan.blokus.network;

import jedyobidan.blokus.core.Move;
import jedyobidan.net.Message;

public class MoveMessage extends Message{
	private static final long serialVersionUID = -6085461774534147277L;
	public final Move move;
	public MoveMessage(int origin, Move m) {
		super(origin);
		this.move = m;
	}
	
	public String toString(){
		return move.toString();
	}

}
