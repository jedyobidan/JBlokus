package jedyobidan.blokus.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import jedyobidan.io.IO;

public class PieceData {
	public final Set<Point> basePoints;
	public final Set<Point> corners;
	public final Set<Point> edges;
	public final Set<Point> unusable;
	public final String type;
	public final int rotations;
	public final boolean flip;
	public PieceData(String type, Collection<Point> points, int rotations, boolean flip){
		this.type = type;
		this.basePoints = new HashSet<Point>(points);
		this.rotations = rotations;
		this.flip = flip;
		edges = calculateEdges();
		unusable = new HashSet<Point>(basePoints);
		unusable.addAll(edges);
		corners = calculateCorners();
	}
	
	public static ArrayList<PieceData> getAllPieces(){
		ArrayList<PieceData> ans = new ArrayList<PieceData>();
		Scanner in = IO.getJarResource(Piece.class, "pieces.dat");
		while(in.hasNextLine()){
			String[] args = in.nextLine().split("\\s*;\\s*");
			String type = args[0];
			
			ArrayList<Point> points = new ArrayList<>();
			String[] pointString = Arrays.copyOfRange(args, 1, args.length - 2);
			for(String p: pointString){
				String[] xy = p.split("\\s*,\\s*");
				points.add(new Point(Integer.valueOf(xy[0]), Integer.valueOf(xy[1])));
			}
			int rotations = Integer.valueOf(args[args.length-2]);
			boolean flip = Boolean.valueOf(args[args.length-1]);
			PieceData data = new PieceData(type, points,rotations, flip);
			System.out.println(data);
			ans.add(data);
		}
		return ans;
	}
	
	public Set<Point> calculateEdges(){
		Set<Point> edges = new HashSet<Point>();
		for(Point p: basePoints){
			edges.add(new Point(p.x+1, p.y));
			edges.add(new Point(p.x-1, p.y));
			edges.add(new Point(p.x, p.y-1));
			edges.add(new Point(p.x, p.y+1));
		}
		edges.removeAll(basePoints);
		return edges;
	}
	
	public Set<Point> calculateCorners(){
		Set<Point> corners = new HashSet<Point>();
		for(Point p: basePoints){
			corners.add(new Point(p.x+1, p.y+1));
			corners.add(new Point(p.x-1, p.y+1));
			corners.add(new Point(p.x+1, p.y-1));
			corners.add(new Point(p.x-1, p.y-1));
		}
		corners.removeAll(unusable);
		return corners;
	}
	
	public String toString(){
		String ans = this.type + "\n";
		for(int y = -2; y <= 2; y++){
			for(int x = -3; x <= 3; x++){
				if(basePoints.contains(new Point(x,y))){
					ans+="X";
				} else if (edges.contains(new Point(x,y))){
					ans+=".";
				} else if (corners.contains(new Point(x,y))){
					ans+="*";
				} else {
					ans+= " ";
				}
				
			}
			ans+= "\n";
		}
		return ans;
	}
	
	public static void main(String[] args){
		getAllPieces();
	}
}
