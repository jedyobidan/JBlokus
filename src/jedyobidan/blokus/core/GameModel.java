package jedyobidan.blokus.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameModel {
	private BoardModel model;
	private ArrayList<Player> players;
	private ArrayList<Player> alive;
	private HashSet<GameObserver> observers;
	private List<Move> moveLog;
	private int turnCt;
	private Player winner;
	private boolean stopped;
	
	public GameModel(){
		model = new BoardModel();
		observers = new HashSet<>();
		players = new ArrayList<>();
		alive = new ArrayList<>();
		moveLog = new ArrayList<>();
	}
	
	public void makeMove(Move m){
		players.get(turnCt%4).endTurn();
		System.out.println(m);
		m.execute(GameModel.this);
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
				p.setAlive(false);
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
		players.get(0).generatePossibleMoves();
		startTurn(players.get(0));
	}
	
	public void endGame(){
		int bestScore = 400;
		for(Player p: players){
			if(p.score() <= bestScore){
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
	
	public Piece getPiece(int pid, String type){
		return getPlayer(pid).getPiece(type);
	}
	
	public BoardModel getBoard(){
		return model;
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
