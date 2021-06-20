package Chess;

public class Pawn extends Piece {

	Pawn(int rank, int file, String color) {
		super(rank, file, color);
		// TODO Auto-generated constructor stub
	}
	
	public int[][] movement(Piece board[][], boolean white_to_move, int enpassantR, int enpassantF) {
		
		Pawn p=this;
		int rank=this.getRank();
		int file=this.getFile();
		
		int[][] moves = new int[64][2];
		int point = 0;
		
		int incrR;
		if(white_to_move) {
			incrR=1;
		} else {
			incrR=-1;
		}
		
		int r=rank,f=file;
		
		r+=incrR;
		
		if(onBoard(r,f)) {
			if(board[r][f]==null) {
				
				moves[point][0] = r;
				moves[point][1] = f;
				point++;
				
				r+=incrR;
				
				
				if(onBoard(r,f)) {
					if(board[r][f]==null) {
						if(!p.moved) {
							moves[point][0] = r;
							moves[point][1] = f;
							point++;
						}
					}
				}
			}
		}
		
		
		
		r=rank+incrR;
		f=file-1;
		
		if(onBoard(r,f)) {
			if(r==enpassantR && f==enpassantF) {
				moves[point][0] = r;
				moves[point][1] = f;
				point++;
			}
			if(board[r][f]!=null) {
				if(white_to_move) {
					if(board[r][f].color=="black") {
						moves[point][0] = r;
						moves[point][1] = f;
						point++;
					}
				} else {
					if(board[r][f].color=="white") {
						moves[point][0] = r;
						moves[point][1] = f;
						point++;
					}
				}
			}
		}
		
		r=rank+incrR;
		f=file+1;
		
		if(onBoard(r,f)) {
			if(r==enpassantR && f==enpassantF) {
				moves[point][0] = r;
				moves[point][1] = f;
				point++;
			}
			if(board[r][f]!=null) {
				if(white_to_move) {
					if(board[r][f].color=="black") {
						moves[point][0] = r;
						moves[point][1] = f;
						point++;
					}
				} else {
					if(board[r][f].color=="white") {
						moves[point][0] = r;
						moves[point][1] = f;
						point++;
					}
				}
			}
		}
		
		return moves;
	}
	
}
