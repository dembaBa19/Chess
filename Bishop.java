package Chess;

public class Bishop extends Piece {

	Bishop(int rank, int file, String color) {
		super(rank, file, color);
		// TODO Auto-generated constructor stub
	}
	
	public int[][] movement(Piece board[][], boolean white_to_move,  int enpassantR, int enpassantF) {
		
		Bishop p=this;
		int rank=this.getRank();
		int file=this.getFile();
		
		int[][] moves = new int[64][2];
		int point = 0;
		
		int incrR[] = {1,1,-1,-1};
		int incrF[] = {1,-1,1,-1};
		
		int r,f;
		
		for(int i=0; i<4; i++) {
		
			r=rank+incrR[i];
			f=file+incrF[i];
			
			while(onBoard(r, f)) {
				if(board[r][f]!=null) {
					if(white_to_move) {
						if(board[r][f].color=="black") {
							moves[point][0] = r;
							moves[point][1] = f;
							
							r+=incrR[i];
							f+=incrF[i];
							point++;
						}
					} else {
						if(board[r][f].color=="white") {
							moves[point][0] = r;
							moves[point][1] = f;
							
							r+=incrR[i];
							f+=incrF[i];
							point++;
						}
					}
					break;
				}
				
				moves[point][0] = r;
				moves[point][1] = f;
				
				r+=incrR[i];
				f+=incrF[i];
				point++;
			}
		
		}
		
		return moves;
	}

}
