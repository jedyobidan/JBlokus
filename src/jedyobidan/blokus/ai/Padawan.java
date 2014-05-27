package jedyobidan.blokus.ai;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.PieceData;

public class Padawan extends AbstractAI {
	public Padawan(int pid) {
		super(pid, "Padawan_" + pid);
	}

	@Override
	public double score(Move m) {
		PieceData data = PieceData.getData(m.pieceType);
		int sizeFactor = data.basePoints.size();
		return sizeFactor;
	}

}
