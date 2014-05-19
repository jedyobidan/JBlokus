package jedyobidan.blokus.game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jedyobidan.blokus.Gui;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.Stage;
import jedyobidan.ui.nanim.actors.FPSDisplay;
import jedyobidan.ui.nanim.actors.MessageBox;
import jedyobidan.ui.nanim.actors.TextFlyBanner;

public class GameStage extends Stage{
	public static boolean VERBOSE = true;
	
	public Board board;
	private Move response;
	private MessageBox messageBox;
	private List<Move> moveLog;
	private int turnCt;
	private ArrayList<Player> players;
	private ArrayList<Player> alive;
	
	public GameStage(Display d) {
		super(d);
		board = new Board();
		this.addActor(board);
		
		addActor(new FPSDisplay());
		
		messageBox = new MessageBox(
				Board.X, Board.Y + Board.SIZE + 5, Board.SIZE, 
				Gui.HEIGHT - (Board.Y + Board.SIZE + 5) - 5,
				Font.decode("Consolas-12"));
		this.addActor(messageBox);
		
		moveLog = new LinkedList<>();
		players = new ArrayList<>();
		alive = new ArrayList<>();
	}
	
	public void makeMove(Move m){
		response = m;
	}
	
	public void beforeStep(){
		if(response!= null){
			handleResponse();
		}
		super.beforeStep();
	}
	
	private void handleResponse(){
		System.out.println(response);
		response.execute(this);
		moveLog.add(response);
		if(VERBOSE) messageBox.addMessage(response.toString());
		players.get(response.playerID).getDock().closeDock();
		turnCt++;
		response = null;
		startTurn();
	}
	
	public void startTurn(){
		if(alive.size() == 0){
			endGame();
		} else {
			new Thread(){
				{
					this.setPriority(MIN_PRIORITY);
				}
				public void run(){
					Player p = players.get(turnCt%4);
					if(alive.contains(p) && p.getPossibleMoves(board).size()!= 0){
						players.get(turnCt%4).getDock().openDock();
						players.get(turnCt%4).requestMove();
					} else {
						messageBox.addMessage(p.name + " has no moves!");
						alive.remove(p);
						p.setAlive(false);
						turnCt++;
						startTurn();
					}
				}
			}.start();
		}
	}
	
	public void endGame(){
		messageBox.addMessage("SCORE");
		int bestScore = 400;
		String best = null;
		for(Player p: players){
			messageBox.addMessage(p.getName() + ": " + p.score());
			if(p.score() <= bestScore){
				best = p.getName();
				bestScore = p.score();
			}
		}
		addActor(new TextFlyBanner(2.5f, best + " wins!"){
			@Override
			public void finish() {
				Display d = getStage().getDisplay();
				d.setStage("SETUP");
			}
		});
	}
	
	public void startGame(){
		addActor(new TextFlyBanner(100, 1, 
				Font.decode(null).deriveFont(24f).deriveFont(Font.BOLD), 
				"Game Start"){
					@Override
					public void finish() {
						startTurn();
					}
		});
	}
	
	public void processInput(Controller c){
		Player curr = players.get(turnCt%4);
		if(curr instanceof LocalPlayer){
			curr.getDock().processInput(c);
		}
		c.consumeAll();
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
	
}
