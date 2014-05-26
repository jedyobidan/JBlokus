package jedyobidan.blokus.ai;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;
import jedyobidan.blokus.core.PieceData;

public class Apprentice extends AbstractAI{
	public Apprentice(int pid) {
		super(pid, "Apprentice_" + pid);
	}

	public Move selectMove(){
		Move m = super.selectMove();
		System.out.println(centerCornerDist(m.getNewPiece()));
		return m;
	}
	
	@Override
	public double score(Move m) {
		Piece p = m.getNewPiece();
		int dc = deltaCorners(p);
		int size = p.getPlacedPoints().size();
		int corner = p.getPlacedCorners().size();
		int movCount = 21-pieces.size();
		double ccdist = centerCornerDist(p);
		double cornerEff = dc/(corner-1.0);
		return 400+size*100 + dc* 50 + cornerEff*20 + 150/ccdist*Math.pow(0.6, movCount);
	}
	
	
}
