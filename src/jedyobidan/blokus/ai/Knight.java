package jedyobidan.blokus.ai;

import java.util.ArrayList;
import java.util.Random;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Piece;

public class Knight extends AbstractAI{
	private double defensiveness;
	public Knight(int pid) {
		super(pid, "Knight_"+pid, 0.7f);
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
		Piece p = m.getPiece(game);
		int dc = deltaCorners(p);
		int dec = blockedCorners(p);
		int size = p.getPlacedPoints().size();
		int corner = p.getPlacedCorners().size();
		double cornerEff = dc/(corner-1.0);
		return 180 + size*130 + dc*50*(1-defensiveness) + cornerEff*20 + 
				dec*35*(1+defensiveness);
	}

}
