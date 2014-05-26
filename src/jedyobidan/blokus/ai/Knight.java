package jedyobidan.blokus.ai;

import java.util.ArrayList;
import java.util.Random;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;

public class Knight extends AbstractAI{
	private double defensiveness;
	public Knight(int pid) {
		super(pid, "Knight_"+pid, 0.65f);
		Random r = new Random();
		defensiveness = r.nextGaussian()/4;
		System.out.println(getName()+ " defense: " + String.format("%.2f", defensiveness));
	}

	public Opening selectOpening(){
		ArrayList<Opening> op = new ArrayList<>();
		for(Opening opening: Opening.getOpenings().values()){
			if(opening.name.startsWith("Advance")){
				op.add(opening);
				op.add(opening);
				op.add(opening);
				op.add(opening);
			} else {
				op.add(opening);
			}
		}
		Random r = new Random();
		return op.get(r.nextInt(op.size())).getCopy();
	}

	@Override
	public double score(Move m) {
		Piece p = m.getNewPiece();
		int dc = boardmetrics.deltaCorners(p);
		int dec = boardmetrics.blockedCorners(p);
		int size = p.getPlacedPoints().size();
		int corner = p.getPlacedCorners().size();
		double cornerEff = dc/(corner-1.0);
		int movCount = 21-pieces.size();
		double ccdist = boardmetrics.centerCornerDist(p);
		return 180 + size*130 + dc*50*(1-defensiveness) + cornerEff*20 + 
				dec*35*(1+defensiveness) + 300/ccdist*Math.pow(0.8, movCount);
	}

}
