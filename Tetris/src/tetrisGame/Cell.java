package tetrisGame;

import java.awt.image.BufferedImage;
/**
 * 格子类，组成各种形状的最小格子。
 * @author Administrator
 *
 */
public class Cell {
	private int row;
	private int col;
    //贴图
	private BufferedImage image;
	
	/**
	 * @param row 行
	 * @param col 列
	 * @param image 图片
	 */
	public Cell(int row, int col, BufferedImage image) {
		super();
		this.row = row;
		this.col = col;
		this.image = image;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	//下落
	public void softDrop(){
		row++;
	}
	//左移
	public void moveLeft(){
		col--;
	}
	//右移
	public void moveRigth(){
		col++;
	}
	
	@Override
	public String toString() {
		return "Cell [col=" + col + ", image=" + image + ", row=" + row + "]";
	}
	

}
