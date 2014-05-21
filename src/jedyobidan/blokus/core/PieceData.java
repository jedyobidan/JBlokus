package jedyobidan.blokus.core;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.*;

import jedyobidan.io.IO;

public class PieceData {
	public final Set<Point2D> basePoints;
	public final String type;
	public final int rotations;
	public final boolean flip;
	public PieceData(String type, Collection<Point2D> points, int rotations, boolean flip){
		this.type = type;
		this.basePoints = new HashSet<Point2D>(points);
		this.rotations = rotations;
		this.flip = flip;
	}
	
	public static ArrayList<PieceData> getAllPieces(){
		ArrayList<PieceData> ans = new ArrayList<PieceData>();
		Scanner in = IO.getJarResource(Piece.class, "pieces.dat");
		while(in.hasNextLine()){
			String[] args = in.nextLine().split("\\s*;\\s*");
			String type = args[0];
			
			ArrayList<Point2D> points = new ArrayList<>();
			String[] pointString = Arrays.copyOfRange(args, 1, args.length - 2);
			for(String p: pointString){
				String[] xy = p.split("\\s*,\\s*");
				points.add(new Point(Integer.valueOf(xy[0]), Integer.valueOf(xy[1])));
			}
			int rotations = Integer.valueOf(args[args.length-2]);
			boolean flip = Boolean.valueOf(args[args.length-1]);
			ans.add(new PieceData(type, points,rotations, flip));
		}
		return ans;
	}
}
