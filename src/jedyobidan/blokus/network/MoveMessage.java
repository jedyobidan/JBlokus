package jedyobidan.blokus.network;

import jedyobidan.blokus.core.Move;
import jedyobidan.net.Message;

public class MoveMessage extends Message{
	public final Move move;
	public MoveMessage(int origin, Move m) {
		super(origin);
		this.move = m;
	}
	
	public String toString(){
		return move.toString();
	}

}
