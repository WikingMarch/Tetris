package tetrisGame;

import java.util.Random;

public abstract class Tetromino {
	// 4格方块，有4个格子 留给子类使用的属性
	protected Cell[] cells = new Cell[4];
	// 创建格子数组
	protected State[] states;
	protected int index = 10000;

	// 内部类（方块旋转变化的各种状态）
	protected class State {
		int row0, col0, row1, col1, row2, col2, row3, col3;

		public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
			super();
			this.row0 = row0;
			this.col0 = col0;
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
			this.row3 = row3;
			this.col3 = col3;
		}

	}

	/**
	 * 静态工厂方法 用Tetromino类型接受其子类的对象，是面向对象多态的使用
	 * 
	 * @return Tetromino
	 */
	public static Tetromino randomOne() {
		Random rand = new Random();
//		int type = rand.nextInt(8);
		int type = 2;
		switch (type) {
		case 0:
			return new T();
		case 1:
			return new J();
		case 2:
			return new I();
		case 3:
			return new O();
		case 4:
			return new L();
		case 5:
			return new Z();
		case 6:
			return new S();
		case 7:
			return new Bomb();
		}
		return null;
	}

	/*
	 * Tetromino 4格方块下落 这个方法是与当前对象有关的方法 是“一个4个方块对象”下落一步的功能
	 */
	public void softDrop() {
		for (Cell cell : cells) {
			cell.softDrop();
		}
	}

	/*
	 * 4个格子左移
	 **/
	public void moveLeft() {
		/*
		 * for(int i=0;i<cells.length;i++){ this.cells[i].moveLeft(); }
		 */
		for (Cell cell : cells) {
			cell.moveLeft();
		}
	}

	/*
	 * 4个格子右移
	 */
	public void moveRigth() {
		for (Cell cell : cells) {
			cell.moveRigth();
		}
	}

	/*
	 * 4个方块右旋转的算法
	 */
	public void rotateRigth() {
		index++;
		State s = states[index % states.length];
		System.out.println(s.row0+","+s.col0);
		System.out.println(s.row1+","+s.col1);
		System.out.println(s.row2+","+s.col2);
		System.out.println(s.row3+","+s.col3);
		Cell o = this.cells[0];
		int row = o.getRow();
		int col = o.getCol();
		// cell[1]
		cells[1].setCol(col + s.col1);
		cells[1].setRow(row + s.row1);
		cells[2].setCol(col + s.col2);
		cells[2].setRow(row + s.row2);
		cells[3].setCol(col + s.col3);
		cells[3].setRow(row + s.row3);
	}
	//方块左旋转
	public void rotateLeft(){
		index--;
		State s = states[index % states.length];
		Cell o = cells[0];
		int row = o.getRow();
		int col = o.getCol();
		cells[1].setRow(row+s.row1);
		cells[1].setCol(col+s.col1);
		cells[2].setRow(row+s.row2);
		cells[2].setCol(col+s.col2);
		cells[3].setRow(row+s.row3);
		cells[3].setCol(col+s.col3);
	}
	
}



class T extends Tetromino {
	public T() {
		cells[0] = new Cell(0, 4, Tetris.T);
		cells[1] = new Cell(0, 3, Tetris.T);
		cells[2] = new Cell(0, 5, Tetris.T);
		cells[3] = new Cell(1, 4, Tetris.T);
/*		cells[0] = new Cell(2, 4, Tetris.T);
		cells[1] = new Cell(2, 3, Tetris.T);
		cells[2] = new Cell(2, 5, Tetris.T);
		cells[3] = new Cell(3, 5, Tetris.T);*/
		states = new State[4];
		states[0] = new State(0, 0, 0, -1, 0, 1, 1, 0);// S0
		states[1] = new State(0, 0, -1, 0, 1, 0, 0, -1);// S1
		states[2] = new State(0, 0, 0, 1, 0, -1, -1, 0);// S2
		states[3] = new State(0, 0, 1, 0, -1, 0, 0, 1);// S3
	}

}

class L extends Tetromino {
	public L() {
		cells[0] = new Cell(0, 4, Tetris.L);
		cells[1] = new Cell(0, 3, Tetris.L);
		cells[2] = new Cell(0, 5, Tetris.L);
		cells[3] = new Cell(1, 3, Tetris.L);
		states = new State[4];
		states[0] = new State(0, 0, 0, 1, 0, -1, -1, 1);
		states[1] = new State(0, 0, 1, 0, -1, 0, 1, 1);
		states[2] = new State(0, 0, 0, -1, 0, 1, 1, -1);
		states[3] = new State(0, 0, -1, 0, 1, 0, -1, -1);
	}

}

class O extends Tetromino {
	public O() {
		cells[0] = new Cell(0, 4, Tetris.O);
		cells[1] = new Cell(0, 5, Tetris.O);
		cells[2] = new Cell(1, 4, Tetris.O);
		cells[3] = new Cell(1, 5, Tetris.O);
		states = new State[2];
		states[0] = new State(0, 0, 0, 1, 1, 0, 1, 1);
		states[1] = new State(0, 0, 0, 1, 1, 0, 1, 1);
	}

}

class S extends Tetromino {
	public S() {
		cells[0] = new Cell(0, 4, Tetris.S);
		cells[1] = new Cell(0, 5, Tetris.S);
		cells[2] = new Cell(1, 3, Tetris.S);
		cells[3] = new Cell(1, 4, Tetris.S);
		states = new State[2];
		states[0] = new State(0, 0, 0, -1, -1, 0, -1, 1);
		states[1] = new State(0, 0, -1, 0, 0, 1, 1, 1);
	}

}

class Z extends Tetromino {
	public Z() {
		cells[0] = new Cell(1, 4, Tetris.Z);
		cells[1] = new Cell(0, 3, Tetris.Z);
		cells[2] = new Cell(0, 4, Tetris.Z);
		cells[3] = new Cell(1, 5, Tetris.Z);
		states = new State[2];
		states[0] = new State(0, 0, -1, -1, -1, 0, 0, 1);
		states[1] = new State(0, 0, -1, 1, 0, 1, 1, 0);
	}

}

class J extends Tetromino {
	public J() {
		cells[0] = new Cell(0, 4, Tetris.J);
		cells[1] = new Cell(0, 3, Tetris.J);
		cells[2] = new Cell(0, 5, Tetris.J);
		cells[3] = new Cell(1, 5, Tetris.J);
		states = new State[4];
		states[0] = new State(0, 0, 0, -1, 0, 1, 1, 1);
		states[1] = new State(0, 0, -1, 0, 1, 0, 1, -1);
		states[2] = new State(0, 0, 0, 1, 0, -1, -1, -1);
		states[3] = new State(0, 0, 1, 0, -1, 0, -1, 1);
	}

}

class I extends Tetromino {
	public I() {
		cells[0] = new Cell(0, 4, Tetris.I);
		cells[1] = new Cell(0, 3, Tetris.I);
		cells[2] = new Cell(0, 5, Tetris.I);
		cells[3] = new Cell(0, 6, Tetris.I);
		states = new State[2];
		states[0] = new State(0, 0, 0, -1, 0, 1, 0, 2);
		states[1] = new State(0, 0, -1, 0, 1, 0, 2, 0);
	}

}

class Bomb extends Tetromino{
	public Bomb() {
		cells[0] = new Cell(0, 3, Tetris.Bomb);
		cells[1] = new Cell(0, 5, Tetris.Bomb);
		cells[2] = new Cell(1, 4, Tetris.Bomb);
		cells[3] = new Cell(2, 4, Tetris.Bomb);
	}
}
