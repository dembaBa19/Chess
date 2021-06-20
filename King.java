package Chess;

public class King extends Piece {

	King(int rank, int file, String color) {
		super(rank, file, color);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public int[][] movement(Piece board[][], boolean white_to_move,  int enpassantR, int enpassantF) {
		
		King p=this;
		int rank=this.getRank();
		int file=this.getFile();
		
		int[][] moves = new int[64][2];
		int point = 0;
		
		int incrR[] = {1, 0, -1};
		int incrF[] = {1, 0, -1};
		
		int r,f;
		
		for(int i=0; i<3; i++) {
			r=rank+incrR[i];
			for(int j=0; j<3; j++) {
				f=file+incrF[j];
				if(onBoard(r, f)) {
					if(board[r][f]==null) {
						moves[point][0] = r;
						moves[point][1] = f;
						point++;
					} else {
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
			}
		}
		
		if(!p.moved) {
			if(board[rank][1] instanceof Rook && !board[rank][1].moved && board[rank][2]==null &&  board[rank][3]==null &&  board[rank][4]==null) {
				moves[point][0] = rank;
				moves[point][1] = 3;
				point++;
			}
		}
		
		if(!p.moved) {
			if(board[rank][8] instanceof Rook && !board[rank][8].moved && board[rank][6]==null &&  board[rank][7]==null) {
				moves[point][0] = rank;
				moves[point][1] = 7;
				point++;
			}
		}
		
		return moves;
	}

}
