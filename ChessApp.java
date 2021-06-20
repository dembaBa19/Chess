package Chess;

import javax.swing.JFrame;

public class ChessApp {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(800,800);
		f.setLayout(null);
		f.add(new Chess());
		f.setTitle("Chess");
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
}
