package jedyobidan.blokus.ai;

import java.util.ArrayList;
import java.util.Random;

import jedyobidan.blokus.core.BoardMetrics;
import jedyobidan.blokus.core.BoardModel;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;

public class Master extends AbstractAI{
	public Master(int pid) {
		super(pid, "Master_" + pid, 0.65f);
		entropy = 0.7f;
	}
	
	public Opening selectOpening(){
		ArrayList<Opening> op = new ArrayList<>();
		for(Opening opening: Opening.getOpenings().values()){
			if(opening.name.startsWith("Advance") && playerID != 0){
				
			} else if (opening.name.startsWith("DAdvance") && playerID == 0){
				
			} else {
				op.add(opening);
			}
		}
		Random r = new Random();
		return op.get(r.nextInt(op.size())).getCopy();
	}
	
	public Move selectMove(){
		Move m = super.selectMove();
		Piece p = m.getNewPiece();
		BoardModel newBoard = new BoardModel(boardmetrics.board);
		newBoard.addPiece(p);
		return m;
	}

	@Override
	public double score(Move m) {
		Piece p = m.getNewPiece();
		int dc = boardmetrics.deltaCorners(p);
		int size = p.getPlacedPoints().size();
		int dec = boardmetrics.blockedCorners(p);
		int movCount = 21-pieces.size();
		double ccdist = boardmetrics.centerCornerDist(p);
		double access = boardmetrics.accessArea();
		int corner = p.getPlacedCorners().size();
		double cornerEff = dc/(corner-1.0);
		BoardModel newBoard = new BoardModel(boardmetrics.board);
		newBoard.addPiece(p);
		access = new BoardMetrics(newBoard, playerID).accessArea() - access;
		return 500 + size*130 + dc*50  + cornerEff*20 + 
				dec*35 + 500/ccdist*Math.pow(0.8, movCount) + Math.signum(access)*Math.sqrt(Math.abs(access))*40;
	}

}
