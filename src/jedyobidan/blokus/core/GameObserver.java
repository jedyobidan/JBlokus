package jedyobidan.blokus.core;

public interface GameObserver {
	void turnStart(Player p);
	void moveMade(Move m);
	void noMoves(Player p);
	void gameEnd(GameModel game);
	void gameStart();
}
