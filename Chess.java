package Chess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Chess extends JPanel implements MouseListener {

	/*(public boolean isMoveLegal(Piece figura, int fromRank, int fromFile, int toRank, int toFile) {
		if(fromRank==toRank && fromFile==toFile) {
			return false;
		}
		return true;
	}*/
	
	public boolean canBeReached(int rank, int file, boolean white_turn) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(piece[i][j]!=null) {
					if(white_turn) {
						if(piece[i][j].color=="black") {
							findLegalMoves(piece[i][j], i, j);
							
						}
					} else {
						if(piece[i][j].color=="white") {
							findLegalMoves(piece[i][j], i, j);
						}
					}
				}
			}
		}
		
		if(legal[rank][file]) {
			return true;
		}
		return false;
	}
	
	public void findLegalMoves(Piece figura, int rank, int file) {
		int moves[][] = new int[64][2];
		moves = figura.movement(piece, white_to_move, enpassantR, enpassantF);
		
		for(int i=1; i<=8; i++) {
			for(int j=1; j<=8; j++) {
				legal[i][j]=false;
			}
		}
		
		for(int i=0; moves[i][0]!=0; i++) {
			legal[moves[i][0]][moves[i][1]]=true;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		int row, column;
		row = (8-((y-80)/squareSize));
		column = (x-50)/squareSize + 1;
		
		if(!piece_clicked) {
			if(row>=1 && row<=8 && column>=1 && column<=8) {
				if(piece[row][column]!=null) {
					if((piece[row][column].color=="white" && white_to_move) || (piece[row][column].color=="black" && !white_to_move)) {
						piece_clicked=true;
						mr = row;
						mf = column;
						findLegalMoves(piece[row][column], row, column);					
						repaint();
					}
				}
			}
		} else {
			if(legal[row][column]) {
				if(piece[row][column]==null && piece[mr][mf] instanceof Pawn && column!=mf) {
					//en croissant
					if(white_to_move) {
						piece[row-1][column]=null;
					} else {
						piece[row+1][column]=null;
					}
					
					piece[row][column]=piece[mr][mf];
					piece[mr][mf]=null;
					piece[row][column].setRank(row);
					piece[row][column].setFile(column);
					white_to_move=!white_to_move;
				} else {
					if(piece[mr][mf] instanceof King && column!=mf+1 && column!=mf-1 && column!=mf) {
						//move king						
						piece[row][column]=null;
						piece[row][column]=piece[mr][mf];
						piece[mr][mf]=null;

						piece[row][column].setRank(row);
						piece[row][column].setFile(column);
						
						
						//move rook
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
					
						white_to_move=!white_to_move;
					} else {
						enpassantR=-1;
						enpassantF=-1;
						possible_croissnat=false;
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
						piece[row][column]=null;
						piece[row][column]=piece[mr][mf];
						piece[mr][mf]=null;
					
						piece[row][column].setRank(row);
						piece[row][column].setFile(column);
					
						white_to_move=!white_to_move;
					}
				}
				piece[row][column].moved=true;
			}
			piece_clicked=false;
			if(piece[row][column]!=null && ((piece[row][column].color=="white" && white_to_move) || (piece[row][column].color=="black" && !white_to_move))) {
				piece_clicked=true;
				mr = row;
				mf = column;
				findLegalMoves(piece[row][column], row, column);					
				repaint();
			}
		}
		repaint();
		
		
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
	 
	public static boolean isNumeric(char l) {
	    if (!Character.isDigit(l)) return false;
	    return true;
	}
	
	public static void drawFEN(String fen) {
		int rank=8,file=1;
		int j=0;
		
		//Where pieces?
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
			//white to move
		} else {
			//black to move
		}
		
		//castling rights
		for(j=j+2; j<fen.length(); j++) {
			if(fen.charAt(j)=='k') {
				
			} else {
				if(fen.charAt(j)=='q') {
					
				} else {
					if(fen.charAt(j)=='K') {
						
					} else {
						if(fen.charAt(j)=='Q') {
							
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
	
	static Color dark = new Color(169, 122, 101);
	static Color light = new Color(241, 217, 192);
	static Color take_red = new Color(161,15,20);
	static Color move_red = new Color(220,45,51);
	static Color select_orange = new Color(203,146,12);
	static Color green = new Color(12,101,12);
	static Color white = new Color(255,255,255);
	
	static int squareSize=60;
	static String[][] board = new String[9][9];
	static Square[][] square = new Square[9][9];
	static Piece[][] piece = new Piece[9][9];
	
	boolean piece_clicked = false;
	int mr,mf;
	
	boolean white_to_move;
	
	static boolean[][] legal = new boolean[9][9];
			
	Piece p;
	
	static ImageIcon black_dot;
	static JLabel black_dot_label[] = new JLabel[64];
	int black_dot_count=0;
	
	static ImageIcon black_frame;
	static JLabel black_frame_label[] = new JLabel[64];
	int black_frame_count=0;
	
	int enpassantR=-1, enpassantF=-1;
	boolean possible_croissnat=false;
	
	//pieces
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
		setBackground(Color.gray);
		
		this.setLayout(null);
		
		JButton ng = new JButton("New Game");
		ng.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		
		ng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
								
				piece_clicked = false;
				white_to_move = true;

				drawFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
				//drawFEN("r3kb1r/pbqn1ppp/1pp2n2/4p3/6P1/1PB2N1P/P1PQPPB1/RN3RK1 b kq - 3 11");
				
				repaint();
			}
		});
		ng.setBounds(50, 550, 150, 50);
		ng.setBackground(white);
		this.add(ng);
		
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
		
		for(int rank=8; rank>0; rank--) {
			for(int file=1; file<=8; file++) {
				if(rank%2 == file%2) {
					g.setColor(dark);
				} else {
					g.setColor(light);
				}
				
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
				
				
				
				g.fillRect((8-rank)*squareSize + 50, (file-1)*squareSize + 50, squareSize, squareSize);
				
				/*if(square[rank][file]==null) {
					square[rank][file] = new Square((8-rank)*squareSize + 50, (file-1)*squareSize + 50);
				} else {
					square[rank][file].setX((8-rank)*squareSize + 50);
					square[rank][file].setY((file-1)*squareSize + 50);
				}*/
				
				if(piece[rank][file]!=null) {
					if(piece[rank][file] instanceof King) {
						if(piece[rank][file].color=="white") {
							white_king_label = new JLabel (white_king);
							white_king_label.setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
							this.add(white_king_label);
						} else {
							black_king_label = new JLabel (black_king);
							black_king_label.setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
							this.add(black_king_label);
						}
					} else {
						if(piece[rank][file] instanceof Queen) {
							if(piece[rank][file].color=="white") {
								white_queen_label[white_queen_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
								this.add(white_queen_label[white_queen_count]);
								white_queen_count++;
								
							} else {
								black_queen_label[black_queen_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
								this.add(black_queen_label[black_queen_count]);
								black_queen_count++;
							}
						} else {
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
												white_pawn_label[white_pawn_count].setBounds((file-1)*squareSize + 50,(8-rank)*squareSize + 50,squareSize,squareSize);
												this.add(white_pawn_label[white_pawn_count]);
												white_pawn_count++;
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

	
	private static void createAndShowGUI() {
		
		JFrame f=new JFrame("Chess");//creating a frame 
		f.setSize(800, 800);
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
