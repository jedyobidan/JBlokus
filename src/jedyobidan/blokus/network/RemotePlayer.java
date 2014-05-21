package jedyobidan.blokus.network;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.net.Message;
import jedyobidan.net.MessageObserver;

public class RemotePlayer extends Player implements MessageObserver{
	private Move move;
	private boolean waiting;
	public RemotePlayer(int pid, String name) {
		super(pid, name);
	}
	@Override
	public void messageRecieved(Message m) {
		if(m instanceof MoveMessage){
			MoveMessage moveMessage = (MoveMessage) m;
			if(moveMessage.move.playerID == this.playerID){
				move = moveMessage.move;
				tryMove();
			}
		}
	}
	
	public boolean tryMove(){
		if(move != null && waiting){
			waiting = false;
			move = null;
			makeMove(move);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void requestMove() {
		waiting = true;
		tryMove();
	}

}
