package jedyobidan.blokus.ai;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.PieceData;

public class EasyAI extends AbstractAI {
	public EasyAI(int pid) {
		super(pid, "EasyAI_" + pid);
	}

	@Override
	public double score(Move m) {
		PieceData data = PieceData.getData(m.pieceType);
		int sizeFactor = data.basePoints.size();
		return sizeFactor;
	}

}
