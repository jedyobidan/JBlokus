package jedyobidan.blokus.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents one instance of the game.
 * Responsible for game logic and player state.
 * @author Young
 *
 */
public class GameModel {
	private BoardModel board;
	private ArrayList<Player> players;
	private ArrayList<Player> alive;
	private HashSet<GameObserver> observers;
	private List<Move> moveLog;
	private int turnCt;
	private Player winner;
	private boolean stopped;
	
	public GameModel(){
		board = new BoardModel();
		observers = new HashSet<>();
		players = new ArrayList<>();
		alive = new ArrayList<>();
		moveLog = new ArrayList<>();
	}
	
	public void makeMove(Move m){
		players.get(turnCt%4).endTurn();
		System.out.println(m);
		Piece p = this.getPlayer(m.playerID).getPiece(m.pieceType);
		m.applyTransformation(p);
		board.addPiece(p);
		p.finalize();
				
		moveLog.add(m);
		for(GameObserver o: observers){
			o.moveMade(m);
		}
		turnCt++;
		new Thread(){
			public void run(){
				tryStartTurn();
			}
		}.start();
	}
	
	private void tryStartTurn(){
		if(stopped) return;
		if(alive.size() == 0){
			endGame();
		} else {
			Player p = players.get(turnCt%4);
			if(alive.contains(p) && p.generatePossibleMoves().size()!= 0){
				startTurn(p);
			} else {
				for(GameObserver o: observers){
					o.noMoves(players.get(turnCt%4));
				}
				alive.remove(p);
				turnCt++;
				tryStartTurn();
			}
		}
	}
	
	private void startTurn(Player p){
		p.startTurn();
		for(GameObserver o: observers){
			o.turnStart(p);
		}
	}
	
	public void startGame(){
		for(GameObserver o: observers){
			o.gameStart();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tryStartTurn();
	}
	
	public void endGame(){
		int bestScore = 0;
		for(Player p: players){
			if(p.score() >= bestScore){
				winner = p;
				bestScore = p.score();
			}
		}
		for(GameObserver ob: observers){
			ob.gameEnd(this);
		}
		for(Player p: players){
			p.joinGame(null);
		}
	}
	
	public Player getWinner(){
		return winner;
	}
	
	public void addPlayer(Player p){
		players.add(p);
		alive.add(p);
		p.joinGame(this);
	}
	
	public Player getPlayer(int pid){
		return players.get(pid);
	}
	
	public BoardModel getBoard(){
		return board;
	}
	
	public Iterable<Player> getPlayers(){
		return players;
	}
	
	public void addObserver(GameObserver o){
		observers.add(o);
	}
	
	public void removeObserver(GameObserver o){
		observers.remove(o);
	}
	
	public void stop(){
		stopped = true;
	}

}
