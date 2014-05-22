package jedyobidan.blokus.core;

import java.awt.Point;
import java.util.List;

public class PieceStatistics {
	public List<Point> corners;
	public List<Point> edges;
	public PieceStatistics(Piece p){
		corners = computeCorners();
	}
	
	private List<Point> computeCorners(){
		return null;
	}
}
