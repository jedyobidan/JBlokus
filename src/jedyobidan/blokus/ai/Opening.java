package jedyobidan.blokus.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.io.IO;

public class Opening {
	private static HashMap<String, Opening> openings;
	private Queue<MoveTemplate> moves;
	public final String name;
	public Opening(Collection<MoveTemplate> moves, String name){
		this.moves = new LinkedList<>(moves);
		this.name = name;
	}
	public boolean inOpening(){
		return moves.size() > 0;
	}
	
	public Move getMove(Player player){
		return moves.poll().getMove(player);
	}
	
	public Opening getCopy(){
		return new Opening(moves, name);
	}
	
	public void abandon(){
		moves.clear();
	}
	
	public static HashMap<String,Opening> getOpenings(){
		if(openings == null){
			openings = new HashMap<String, Opening>();
			Scanner in = IO.getJarResource(Opening.class, "openings.dat");
			while(in.hasNextLine()){
				String[] args = in.nextLine().split("-");
				String name =  args[0];
				int moveNum = Integer.parseInt(args[1]);
				ArrayList<MoveTemplate> moves = new ArrayList<>();
				for(int i = 0; i < moveNum; i++){
					moves.add(decode(in.nextLine()));
				}
				openings.put(name, new Opening(moves, name));
			}
		}
		return openings;
	}
	
	private static MoveTemplate decode(String input){
		Pattern p = Pattern.compile("(.*)\\[(.*)\\]\\@\\((\\d+),(\\d+)\\)");
		Matcher m = p.matcher(input);
		if(m.find()){
			Point place = new Point(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
			List<String> trans = Arrays.asList(m.group(2).split(","));
			String type = m.group(1);
			return new MoveTemplate(place, type, trans);
		} else {
			throw new IllegalArgumentException(input + ": Did not match format");
		}
	}
	
	private static class MoveTemplate{
		public Point placement;
		public String piece;
		public List<String> transforms;
		
		public MoveTemplate(Point placement, String piece,
				List<String> transforms) {
			super();
			this.placement = placement;
			this.piece = piece;
			this.transforms = transforms;
		}

		
		public Move getMove(Player player){
			List<String> newTransforms = new ArrayList<>(transforms);
			for(int i = 0; i < player.playerID; i++){
				newTransforms.add("CW");
			}
			Point newPlace = new Point(convertX(placement.x, player.playerID), convertY(placement.y, player.playerID));
			return new Move(piece, newTransforms, newPlace, player);
		}
		
		private int convertX(int x, int pnum){
			if(pnum == 1 || pnum == 2){
				return 19-x;
			} else {
				return x;
			}
		}
		
		private int convertY(int y, int pnum){
			if(pnum == 2 || pnum == 3){
				return 19-y;
			} else {
				return y;
			}
		}
		
		public String toString(){
			return piece + transforms + "@(" + placement.x + ", " + placement.y + ")";
		}
	}
	
	public static void main(String[] args){
		getOpenings();
	}
}
