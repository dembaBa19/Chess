package chess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Chess extends JPanel implements MouseListener {

	//what are the available squares
	public int[][] findLegalMoves (Piece board[][], Piece figura, int rank, int file) {
		int moves[][] = new int[64][2];
		moves = figura.movement(board, white_to_move, enpassantR, enpassantF);
		
		return moves;
	}
	
	//mouse click action
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//if game is not over
		if(gameOn) {
			
			//what square is clicked?
			int x = e.getX();
			int y = e.getY();
			
			int row, column;
			row = (8-((y-80)/squareSize));
			column = (x-50)/squareSize + 1;
			
			//if no piece is already selected
			if(!piece_clicked) {
				if(row>=1 && row<=8 && column>=1 && column<=8) {
					if(piece[row][column]!=null) {
						//is the piece the right color?
						if((piece[row][column].color=="white" && white_to_move) || (piece[row][column].color=="black" && !white_to_move)) {
							//now we know a piece is selected
							piece_clicked=true;
							
							//get legal moves
							mr = row;
							mf = column;
							
							//place them in an array
							int [][] b = new int[64][2];
							b = findLegalMoves(piece, piece[row][column], row, column);		
											
							//false by default
							for(int i=1; i<=8; i++) {
								for(int j=1; j<=8; j++) {
									legal[i][j]=false;
								}
							}
							
							//legal ones become true
							for(int i=0; b[i][0]!=0; i++) {
								legal[b[i][0]][b[i][1]]=true;
							}
							
							//repaint everything so that legal moves are shown on board
							repaint();
						}
					}
				}
			} else {
				//piece has already been selected, we have to move it now
				if(legal[row][column]) {
					if(piece[row][column]==null && piece[mr][mf] instanceof Pawn && column!=mf) {
						//in case of en croissant
						//delete taken pawn
						if(white_to_move) {
							piece[row-1][column]=null;
						} else {
							piece[row+1][column]=null;
						}
						
						//move pawn to square
						piece[row][column]=piece[mr][mf];
						piece[mr][mf]=null;
						piece[row][column].setRank(row);
						piece[row][column].setFile(column);
						
						char a,b;
						a=(char) ('A' - 1 + mf);
						b=(char) ('A' - 1 + column);
						movesHistory+=(a + "" + mr + " -> " + b + row) + "\n";
						
						//change turn
						white_to_move=!white_to_move;
					} else {
						if(piece[mr][mf] instanceof King && column!=mf+1 && column!=mf-1 && column!=mf) {
							//in case of castle
							
							//move king
							piece[row][column]=null;
							piece[row][column]=piece[mr][mf];
							piece[mr][mf]=null;
	
							piece[row][column].setRank(row);
							piece[row][column].setFile(column);
							
							//move rook depending on whether its short or long castle
							if(column-mf==2) {
								piece[row][6]=null;
								piece[row][6]=piece[row][8];
								piece[row][8]=null;
								
								piece[row][6].setRank(row);
								piece[row][6].setFile(6);
								piece[row][6].moved=true;
							} else {
								piece[row][4]=null;
								piece[row][4]=piece[row][1];
								piece[row][1]=null;
								
								piece[row][4].setRank(row);
								piece[row][4].setFile(4);
								piece[row][4].moved=true;
							}
							
							char a,b;
							a=(char) ('A' - 1 + mf);
							b=(char) ('A' - 1 + column);
							movesHistory+=(a + "" + mr + " -> " + b + row) + "\n";
							
							white_to_move=!white_to_move;
						} else {
							//setting the possibility of en croissant to false by default
							enpassantR=-1;
							enpassantF=-1;
							possible_croissnat=false;
							
							//checking if it will be possible on next move
							if(piece[mr][mf] instanceof Pawn && (row-mr==2 || mr-row==2)) {
								if(white_to_move) {
									enpassantR=row-1;
									enpassantF=mf;
								} else {
									enpassantR=row+1;
									enpassantF=mf;
								}
								possible_croissnat=true;
							}
							
							//move the piece
							piece[row][column]=null;
							piece[row][column]=piece[mr][mf];
							piece[mr][mf]=null;
						
							piece[row][column].setRank(row);
							piece[row][column].setFile(column);
						
							char a,b;
							a=(char) ('A' - 1 + mf);
							b=(char) ('A' - 1 + column);
							movesHistory+=(a + "" + mr + " -> " + b + row) + "\n";
							
							white_to_move=!white_to_move;
						}
					}
					//piece has now moved
					piece[row][column].moved=true;
				}
				
				piece_clicked=false;
				if(piece[row][column]!=null && ((piece[row][column].color=="white" && white_to_move) || (piece[row][column].color=="black" && !white_to_move))) {
					//in case same color piece is selected we cange "piece_clicked" so we dont have to click twice
					piece_clicked=true;
					mr = row;
					mf = column;
					
					int [][] b = new int[64][2];
					b=findLegalMoves(piece, piece[row][column], row, column);	
					
					for(int i=1; i<=8; i++) {
						for(int j=1; j<=8; j++) {
							legal[i][j]=false;
						}
					}
					
					for(int i=0; b[i][0]!=0; i++) {
						legal[b[i][0]][b[i][1]]=true;
					}
					repaint();
				}
			}
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//is a char a number?
	public static boolean isNumeric(char l) {
	    if (!Character.isDigit(l)) return false;
	    return true;
	}
	
	//places the pieces on squares from FEN code
	public static void drawFEN(String fen) {
		int rank=8,file=1;
		int j=0;
		
		//fill the grid with pieces
		for(int i=0; i<fen.length(); i++) {
			if(fen.charAt(i)=='r') {
				piece[rank][file] = new Rook(rank,file,"black");
			} else {
				if(fen.charAt(i)=='R') {
					piece[rank][file] = new Rook(rank,file,"white");
				} else {
					if(fen.charAt(i)=='k') {
						piece[rank][file] = new King(rank,file,"black");
					} else {
						if(fen.charAt(i)=='K') {
							piece[rank][file] = new King(rank,file,"white");
						} else {
							if(fen.charAt(i)=='q') {
								piece[rank][file] = new Queen(rank,file,"black");
							} else {
								if(fen.charAt(i)=='Q') {
									piece[rank][file] = new Queen(rank,file,"white");
								} else {
									if(fen.charAt(i)=='n') {
										piece[rank][file] = new Knight(rank,file,"black");
									} else {
										if(fen.charAt(i)=='N') {
											piece[rank][file] = new Knight(rank,file,"white");
										} else {
											if(fen.charAt(i)=='b') {
												piece[rank][file] = new Bishop(rank,file,"black");
											} else {
												if(fen.charAt(i)=='B') {
													piece[rank][file] = new Bishop(rank,file,"white");
												} else {
													if(fen.charAt(i)=='p') {
														piece[rank][file] = new Pawn(rank,file,"black");
														if(rank!=7) {
															piece[rank][file].moved=true;
														}
													} else {
														if(fen.charAt(i)=='P') {
															piece[rank][file] = new Pawn(rank,file,"white");
															if(rank!=2) {
																piece[rank][file].moved=true;
															}
														} else {
															if(fen.charAt(i)=='/') {
																rank--;
																file=1;
																continue;
															} else {
																if(isNumeric(fen.charAt(i))) {
																	file+=Character.getNumericValue((fen.charAt(i)))-1;
																} else {
																	if(fen.charAt(i)==' ') {
																		j=i+1;
																		break;
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			file++;
		}
		
		//whose turn?
		if(fen.charAt(j)=='w') {
			white_to_move=true;
		} else {
			white_to_move=false;
		}
		
		if(piece[1][1]!=null) {
			piece[1][1].moved=true;
		}
		if(piece[8][1]!=null) {
			piece[8][1].moved=true;
		}
		if(piece[1][8]!=null) {
			piece[1][8].moved=true;
		}
		if(piece[8][8]!=null) {
			piece[8][8].moved=true;
		}
		
		//castling rights
		for(j=j+2; j<fen.length(); j++) {
			
			if(fen.charAt(j)=='k') {
				piece[8][8].moved=false;
			} else {
				if(fen.charAt(j)=='q') {
					piece[8][1].moved=false;
				} else {
					if(fen.charAt(j)=='K') {
						piece[1][8].moved=false;
					} else {
						if(fen.charAt(j)=='Q') {
							piece[1][1].moved=false;
						} else {
							if(fen.charAt(j)=='-') {
								//no castling rights
							} else {
								if(fen.charAt(j)==' ') {
									break;
								}
							}
						}
					}
				}
			}
		}
		
		for(j=j+2; j<fen.length(); j++) {
			
		}
	}
	
	//colors
	static Color dark = new Color(169, 122, 101);
	static Color light = new Color(241, 217, 192);
	static Color red = new Color(220,45,51);
	static Color bg = new Color(76,75,71);
	static Color white = new Color(255,255,255);
	
	//board
	static int squareSize=60;
	static Piece[][] piece = new Piece[9][9];
	
	static boolean piece_clicked = false;
	static int mr,mf;
	
	//is king alive
	static boolean white_alive;
	static boolean black_alive;
	static boolean gameOn;

	//whose turn
	static boolean white_to_move;
	
	//write moves in file
	static String movesHistory="";
	File movesFile = new File("moves.txt");
	
	//legal moves
	static boolean[][] legal = new boolean[9][9];
	static boolean[][] legal_moves = new boolean[9][9];
			
	//piece sells... but who's buying
	Piece p;
	
	//text "White/Black wins!"
	static JLabel whiteWinnerlbl = new JLabel();
	static JLabel blackWinnerlbl = new JLabel();
	
	//ranks and files
	static JLabel rankLabel[] = new JLabel[8];
	static JLabel fileLabel[] = new JLabel[8];
	
	//available squares images
	static ImageIcon black_dot;
	static JLabel black_dot_label[] = new JLabel[64];
	int black_dot_count=0;
	
	static ImageIcon black_frame;
	static JLabel black_frame_label[] = new JLabel[64];
	int black_frame_count=0;
	
	//is enpassant possible
	int enpassantR=-1, enpassantF=-1;
	boolean possible_croissnat=false;
	
	//pieces images (labels)
	static ImageIcon black_king;
	static JLabel black_king_label;
	static ImageIcon black_queen;
	static JLabel black_queen_label[] = new JLabel[9];
	int black_queen_count=0;
	static ImageIcon black_rook;
	static JLabel black_rook_label[] = new JLabel[10];
	int black_rook_count=0;
	static ImageIcon black_bishop;
	static JLabel black_bishop_label[] = new JLabel[10];
	int black_bishop_count=0;
	static ImageIcon black_knight;
	static JLabel black_knight_label[] = new JLabel[10];
	int black_knight_count=0;
	static ImageIcon black_pawn;
	static JLabel black_pawn_label[] = new JLabel[8];
	int black_pawn_count=0;
	
	static ImageIcon white_king;
	static JLabel white_king_label;
	static ImageIcon white_queen;
	static JLabel white_queen_label[] = new JLabel[9];
	int white_queen_count=0;
	static ImageIcon white_rook;
	static JLabel white_rook_label[] = new JLabel[10];
	int white_rook_count=0;
	static ImageIcon white_bishop;
	static JLabel white_bishop_label[] = new JLabel[10];
	int white_bishop_count=0;
	static ImageIcon white_knight;
	static JLabel white_knight_label[] = new JLabel[10];
	int white_knight_count=0;
	static ImageIcon white_pawn;
	static JLabel white_pawn_label[] = new JLabel[8];
	int white_pawn_count=0;
	
	
	public Chess() {
		setBackground(bg);
		this.setLayout(null);

		//winner labels
		whiteWinnerlbl.setBounds(180,120,300,300);
		blackWinnerlbl.setBounds(180,120,300,300);
		whiteWinnerlbl.setFont(new Font("Times New Roman", Font.PLAIN, 41));
		blackWinnerlbl.setFont(new Font("Times New Roman", Font.PLAIN, 41));
		whiteWinnerlbl.setForeground(red);
		blackWinnerlbl.setForeground(red);
		blackWinnerlbl.setVisible(false);
		whiteWinnerlbl.setVisible(false);
		this.add(whiteWinnerlbl);
		this.add(blackWinnerlbl);

		//New Game Button
		JButton ng = new JButton("New Game");
		ng.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		
		ng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//default values
				piece_clicked = false;
				white_to_move = true;		
				gameOn=true;
				blackWinnerlbl.setVisible(false);
				whiteWinnerlbl.setVisible(false);
				white_alive=true;
				black_alive=true;
				
				for(int i=1; i<=8; i++) {
					for(int j=1; j<=8; j++) {
						piece[i][j]=null;
					}					
				}

				
				//draws pieces
				drawFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
				//drawFEN("r3kb1r/pbqn1ppp/1pp2n2/4p3/6P1/1PB2N1P/P1PQPPB1/RN3RK1 b kq - 3 11");
				
				repaint();
			}
		});
		ng.setBounds(50, 550, 150, 50);
		ng.setBackground(white);
		this.add(ng);
		
		//connecting labels with images
		black_king = new ImageIcon(getClass().getResource("Chess_kdt60.png"));
		black_queen = new ImageIcon(getClass().getResource("Chess_qdt60.png"));
		black_rook = new ImageIcon(getClass().getResource("Chess_rdt60.png"));
		black_knight = new ImageIcon(getClass().getResource("Chess_ndt60.png"));
		black_bishop = new ImageIcon(getClass().getResource("Chess_bdt60.png"));
		black_pawn = new ImageIcon(getClass().getResource("Chess_pdt60.png"));
		
		white_king = new ImageIcon(getClass().getResource("Chess_klt60.png"));
		white_queen = new ImageIcon(getClass().getResource("Chess_qlt60.png"));
		white_rook = new ImageIcon(getClass().getResource("Chess_rlt60.png"));
		white_knight = new ImageIcon(getClass().getResource("Chess_nlt60.png"));
		white_bishop = new ImageIcon(getClass().getResource("Chess_blt60.png"));
		white_pawn = new ImageIcon(getClass().getResource("Chess_plt60.png"));
		
		black_dot = new ImageIcon(getClass().getResource("black_dot.png"));
		black_frame = new ImageIcon(getClass().getResource("black_frame.png"));
		
		//filling labels with many images of one thing if needed
		for(int i=0; i<9; i++) {
			white_queen_label[i] = new JLabel (white_queen);
		}
		for(int i=0; i<10; i++) {
			white_rook_label[i] = new JLabel (white_rook);
		}
		for(int i=0; i<10; i++) {
			white_bishop_label[i] = new JLabel (white_bishop);
		}
		for(int i=0; i<10; i++) {
			white_knight_label[i] = new JLabel (white_knight);
		}
		for(int i=0; i<8; i++) {
			white_pawn_label[i] = new JLabel (white_pawn);
		}
		for(int i=0; i<9; i++) {
			black_queen_label[i] = new JLabel (black_queen);
		}
		for(int i=0; i<10; i++) {
			black_rook_label[i] = new JLabel (black_rook);
		}
		for(int i=0; i<10; i++) {
			black_bishop_label[i] = new JLabel (black_bishop);
		}
		for(int i=0; i<10; i++) {
			black_knight_label[i] = new JLabel (black_knight);
		}
		for(int i=0; i<8; i++) {
			black_pawn_label[i] = new JLabel (black_pawn);
		}
		for(int i=0; i<64; i++) {
			black_dot_label[i] = new JLabel (black_dot);
		}
		for(int i=0; i<64; i++) {
			black_frame_label[i] = new JLabel (black_frame);
		}
	    
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//paint ranks and files on the side of the board
		for(int i=0; i<8; i++) {
			rankLabel[i] = new JLabel();
			rankLabel[i].setBounds(20,i*squareSize+50,squareSize,squareSize);
			rankLabel[i].setFont(new Font("Times New Roman", Font.PLAIN, 41));
			rankLabel[i].setForeground(white);
			rankLabel[i].setVisible(true);
			rankLabel[i].setText("" + (8-i));
			this.add(rankLabel[i]);
		}
		for(int i=0; i<8; i++) {
			fileLabel[i] = new JLabel();
			fileLabel[i].setBounds(i*squareSize + 67 ,0,squareSize,squareSize);
			fileLabel[i].setFont(new Font("Times New Roman", Font.PLAIN, 41));
			fileLabel[i].setForeground(white);
			fileLabel[i].setVisible(true);
			fileLabel[i].setText("" + ((char)('A' + i)));
			this.add(fileLabel[i]);
		}
		
		//no images of pieces by default
		if(black_king_label!=null) this.remove(black_king_label);
		if(white_king_label!=null) this.remove(white_king_label);
		
		for(int i=0; i<black_queen_count; i++) {
			this.remove(black_queen_label[i]);
		}
		for(int i=0; i<white_queen_count; i++) {
			this.remove(white_queen_label[i]);
		}
		for(int i=0; i<black_rook_count; i++) {
			this.remove(black_rook_label[i]);
		}
		for(int i=0; i<white_rook_count; i++) {
			this.remove(white_rook_label[i]);
		}
		for(int i=0; i<black_bishop_count; i++) {
			this.remove(black_bishop_label[i]);
		}
		for(int i=0; i<white_bishop_count; i++) {
			this.remove(white_bishop_label[i]);
		}
		for(int i=0; i<black_knight_count; i++) {
			this.remove(black_knight_label[i]);
		}
		for(int i=0; i<white_knight_count; i++) {
			this.remove(white_knight_label[i]);
		}
		for(int i=0; i<black_pawn_count; i++) {
			this.remove(black_pawn_label[i]);
		}
		for(int i=0; i<white_pawn_count; i++) {
			this.remove(white_pawn_label[i]);
		}
		
		for(int i=0; i<black_dot_count; i++) {
			this.remove(black_dot_label[i]);
		}
		for(int i=0; i<black_frame_count; i++) {
			this.remove(black_frame_label[i]);
		}
		
		//count how many of each thing we have
		black_queen_count=0;
		black_rook_count=0;
		black_bishop_count=0;
		black_knight_count=0;
		black_pawn_count=0;
		white_queen_count=0;
		white_rook_count=0;
		white_bishop_count=0;
		white_knight_count=0;
		white_pawn_count=0;
		
		black_dot_count=0;
		black_frame_count=0;
		
		//will change to true if kings are found
		white_alive=false;
		black_alive=false;
		
		for(int rank=8; rank>0; rank--) {
			for(int file=1; file<=8; file++) {
				//light or dark square?
				if(rank%2 == file%2) {
					g.setColor(dark);
				} else {
					g.setColor(light);
				}
				
				//painting the available squares to move if piece is currently clicked
				if(piece_clicked) {
					if(legal[rank][file]) {
						if(piece[rank][file]==null && !(rank==enpassantR && file==enpassantF)) {
							black_dot_label[black_dot_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
							this.add(black_dot_label[black_dot_count]);
							black_dot_count++;
						} else {
							black_frame_label[black_frame_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
							this.add(black_frame_label[black_frame_count]);
							black_frame_count++;
						}
					}
				}
				//fill squares
				g.fillRect((8-rank)*squareSize + 50, (file-1)*squareSize + 50, squareSize, squareSize);
				
				//if we gotta paint piece (square not empty)
				if(piece[rank][file]!=null) {
					//if piece == king, queen, ... etc.
					if(piece[rank][file] instanceof King) {
						if(piece[rank][file].color=="white") {
							white_king_label = new JLabel (white_king);
							white_king_label.setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
							this.add(white_king_label);
							//if kings are alive game doesnt end
							white_alive=true;
						} else {
							black_king_label = new JLabel (black_king);
							black_king_label.setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
							this.add(black_king_label);
							//if kings are alive game doesnt end
							black_alive=true;
						}
					} else {
						if(piece[rank][file] instanceof Queen) {
							if(piece[rank][file].color=="white") {
								white_queen_label[white_queen_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
								this.add(white_queen_label[white_queen_count]);
								//keep count of number of queens
								white_queen_count++;
								
							} else {
								black_queen_label[black_queen_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
								this.add(black_queen_label[black_queen_count]);
								black_queen_count++;
							}
						} else {
							//.....
							if(piece[rank][file] instanceof Rook) {
								if(piece[rank][file].color=="white") {
									white_rook_label[white_rook_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
									this.add(white_rook_label[white_rook_count]);
									white_rook_count++;
								} else {
									black_rook_label[black_rook_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
									this.add(black_rook_label[black_rook_count]);
									black_rook_count++;
								}
							} else {
								if(piece[rank][file] instanceof Bishop) {
									if(piece[rank][file].color=="white") {
										white_bishop_label[white_bishop_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
										this.add(white_bishop_label[white_bishop_count]);
										white_bishop_count++;
									} else {
										black_bishop_label[black_bishop_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
										this.add(black_bishop_label[black_bishop_count]);
										black_bishop_count++;
									}
								} else {
									if(piece[rank][file] instanceof Knight) {
										if(piece[rank][file].color=="white") {
											white_knight_label[white_knight_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
											this.add(white_knight_label[white_knight_count]);
											white_knight_count++;
										} else {
											black_knight_label[black_knight_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
											this.add(black_knight_label[black_knight_count]);
											black_knight_count++;
										}
									} else {
										if(piece[rank][file] instanceof Pawn) {
											if(piece[rank][file].color=="white") {
												if(rank==8) {
													white_queen_label[white_queen_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
													this.add(white_queen_label[white_queen_count]);
													white_queen_count++;
													piece[rank][file] = new Queen(rank, file, "white");
												} else {
													white_pawn_label[white_pawn_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
													this.add(white_pawn_label[white_pawn_count]);
													white_pawn_count++;
												}
											} else {
												if(rank==1) {
													black_queen_label[black_queen_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
													this.add(black_queen_label[black_queen_count]);
													black_queen_count++;
													piece[rank][file] = new Queen(rank, file, "black");
												} else {
													black_pawn_label[black_pawn_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
													this.add(black_pawn_label[black_pawn_count]);
													black_pawn_count++;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
			}
		}
		if(gameOn) {
			//check if king are alive
			if(!black_alive) {
				whiteWinnerlbl.setText("White wins!");
				whiteWinnerlbl.setVisible(true);
				gameOn=false;
				try {
					//write to file
					FileWriter fw = new FileWriter(movesFile, true);
					fw.write((movesHistory + "__________________\n"));
				    fw.close();
				    
				} catch (IOException e) {//an error occurred
				    e.printStackTrace();
				}
			}
			if(!white_alive) {
				blackWinnerlbl.setText("Black wins!");
				blackWinnerlbl.setVisible(true);
				gameOn=false;
				
				try {
					//write to file
					FileWriter fw = new FileWriter(movesFile,true);
					fw.write((movesHistory + "__________________\n"));
				    fw.close();
				    
				} catch (IOException e) {//an error occurred
				    e.printStackTrace();
				}
			}
		}
	}

	//create and show GUI
	private static void createAndShowGUI() {
		
		JFrame f=new JFrame("Chess");//creating a frame 
		f.setSize(700, 700);
        f.setLayout(null);
        f.setVisible(true);
        
        Chess c = new Chess();
        c.setOpaque(true); //content panes must be opaque
        f.setContentPane(c);
        
        f.addMouseListener(c);
        
        
	}
	
	public static void main(String[] args) {
		
	    //Create and show this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});		
	}

}
