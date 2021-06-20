package Chess;

import java.awt.Color;

public class Piece {
	String color;
	private int rank;
	private int file;
	boolean alive;
	boolean moved;
	
	String availableSquares[];
	
	public static boolean onBoard(int rank, int file) {
		if(rank<=8 && rank>=1 && file<=8 && file>=1) {
			return true;
		}
		return false;
	}
	
	public int[][] movement(Piece piece[][], boolean white_to_move, int enpassantR, int enpassantF) {
		int[][] moves = new int[64][2];
		
		return moves;
		
	}
	
	Piece(int rank, int file, String color) {
		this.setRank(rank);
		this.setFile(file);
		this.color=color;
		this.alive=true;
		this.moved=false;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getFile() {
		return file;
	}

	public void setFile(int file) {
		this.file = file;
	}
	
	
	
	
}
