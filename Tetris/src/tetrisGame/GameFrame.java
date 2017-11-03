package tetrisGame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = -2323449708367876453L;
	private Tetris tetris;

	public GameFrame() {
		tetris = new Tetris();
		this.add(tetris);
		this.setSize(531, 580);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setTitle("俄罗斯方块");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			this.setIconImage(ImageIO.read(new File("image/bk.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GameFrame gameframe = new GameFrame();
		gameframe.setVisible(true);
		gameframe.tetris.action();
	}
}
