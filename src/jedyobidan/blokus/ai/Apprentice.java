package jedyobidan.blokus.ai;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;

public class Apprentice extends AbstractAI{
	public Apprentice(int pid) {
		super(pid, "Apprentice_" + pid);
	}

	
	
	@Override
	public double score(Move m) {
		Piece p = m.getPiece(game);
		int dc = deltaCorners(p);
		int size = p.getPlacedPoints().size();
		int corner = p.getPlacedCorners().size();
		double cornerEff = dc/(corner-1.0);
		return 200+size*100 + dc* 50 + cornerEff*20;
	}
	
	
}
