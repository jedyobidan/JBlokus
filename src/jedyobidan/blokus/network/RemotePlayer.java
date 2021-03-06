package jedyobidan.blokus.network;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.net.Message;
import jedyobidan.net.MessageObserver;

public class RemotePlayer extends Player implements MessageObserver{
	private Move move;
	private boolean waiting;
	private boolean aiOverride;
	public RemotePlayer(int pid, String name) {
		super(pid, name);
	}
	
	public void aiOverride(){
		aiOverride = true;
		if(waiting){
			generatePossibleMoves();
			move = aiOverrideMove();
		}
		tryMove();
	}
	@Override
	public void messageRecieved(Message m) {
		if(aiOverride) return;
		if(m instanceof MoveMessage){
			MoveMessage moveMessage = (MoveMessage) m;
			if(moveMessage.move.playerID == this.playerID){
				move = moveMessage.move;
				tryMove();
			}
		}
	}
	
	public Move aiOverrideMove(){
		Random r = new Random();
		return possibleMoves.get(r.nextInt(possibleMoves.size()));
	}
	
	public void tryMove(){
		if(move != null && waiting){
			makeMove(move);
			waiting = false;
			move = null;
		}
	}

	@Override
	public void requestMove() {
		waiting = true;
		if(aiOverride){
			final Timer t = new Timer();
			t.schedule(new TimerTask(){
				@Override
				public void run() {
					move = aiOverrideMove();
					tryMove();
					t.cancel();
				}
			}, 500);
		}
		tryMove();
	}
	
	public String type(){
		return "Remote Player";
	}

}
