package tetrisGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class Tetris extends JPanel {
	private static final long serialVersionUID = 719837830802293717L;
	// 分数
	private int score;
	// 行数
	private int line;
	// 时间
	private String time = "00:00:00   000";
	public static final int ROWS = 20;
	public static final int COLS = 10;
	// 方块墙
	private Cell[][] wall = new Cell[ROWS][COLS];
	// 正在下落的方块
	private Tetromino tetromino;
	// 下一个出场的方块
	private Tetromino nextOne;
	private Object keyListener;
    private Timer timer;// 定时器
    private Timer counter;// 计时器
    private int interval = 600; //下落间隔时间500ms
    
    private int state; //游戏状态
    private int[] scoreTable = {0, 10, 20, 35, 100};
    public static final int RUNNING = 0;
	public static final int PAUSE = 1;
	public static final int GAME_OVER = 2;
	/*
	 * 软件的启动方法action：动作，活动
	 */
	private static BufferedImage background,gameOver;
	public static BufferedImage J, T, L, O, Z, S, I,Bomb;
	/* 静态代码块会在类加载期间执行 */
	/* 利用java的API将图片读为内存对象 */
	static {
		try {
			background = ImageIO.read(new FileInputStream("image/tetris2.png"));
			gameOver = ImageIO.read(new FileInputStream("image/game-over.png"));
			O = ImageIO.read(new FileInputStream("image/O.png"));
			T = ImageIO.read(new FileInputStream("image/T.png"));
			Z = ImageIO.read(new FileInputStream("image/Z.png"));
			I = ImageIO.read(new FileInputStream("image/I.png"));
			S = ImageIO.read(new FileInputStream("image/S.png"));
			L = ImageIO.read(new FileInputStream("image/L.png"));
			J = ImageIO.read(new FileInputStream("image/J.png"));
			Bomb = ImageIO.read(new FileInputStream("image/Bomb.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void action() {
		// randomOne 工厂方法，用于生产方块
		// 用于创建对象的方法
		tetromino = Tetromino.randomOne();
		nextOne = Tetromino.randomOne();
		keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_Q) {
					System.exit(0);
				}
				switch (state) {
				    case GAME_OVER:
				    	if(key == KeyEvent.VK_S) {//重新开始 初试话新的方块墙、分数、状态等等
				    		wall = new Cell[ROWS][COLS];
				    		score = line = 0;
							state = RUNNING;
				    	}
				    	return;
				    case PAUSE:
				    	if(key == KeyEvent.VK_C) {
				    		state = RUNNING;
				    	}
				    	return;
				}
				
				switch (key) {
					case KeyEvent.VK_RIGHT:
						moveRigthAction();
						break;
					case KeyEvent.VK_LEFT:
						moveLeftAction();
						break;
					case KeyEvent.VK_DOWN:
//						softDropAction();
						hardDropAction();
						break;
					case KeyEvent.VK_UP:
						rotateRigthAction();
						break;
					case KeyEvent.VK_P:
						state = PAUSE;
						break;
				}
				repaint();

			}
		};
		this.addKeyListener((KeyListener) keyListener);
		this.setFocusable(true);
		// 请求Focus 焦点
		this.requestFocus();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(state == RUNNING) {
					softDropAction();
				}
				repaint();
			}
		}, 0,interval);
		
		counter = new Timer();
		counter.schedule(new TimerTask() {
			long i=0;
			@Override
			public void run() {
				if(state == RUNNING) {
					time = format(i++);
				}
				repaint();
			}}, 0,1);

	}
	//时间格式化
	private String format(long elapsed) {    
        int hour, minute, second, milli;    
        //毫秒
        milli = (int) (elapsed % 1000);    
        elapsed = elapsed / 1000;    
        //秒
        second = (int) (elapsed % 60);    
        elapsed = elapsed / 60;    
        //分
        minute = (int) (elapsed % 60);    
        elapsed = elapsed / 60;    
        //小时
        hour = (int) (elapsed % 60);    
 
        return String.format("%02d:%02d:%02d %03d", hour, minute, second, milli);    
    }    
	
	//慢下落运动
	public void softDropAction() {
		if (canDrop()) {
			tetromino.softDrop();
		} else {
			if(this.tetromino.cells[0].getImage() != Tetris.Bomb) {//如果方块不是炸弹
				landIntoWall();
				int lines = destroyLines();
				line+=lines;
				score+=scoreTable[lines];
				if (isGameOver()) {
					state = GAME_OVER;
				} else {
					tetromino = nextOne; //将下一个出场的赋给当前方块
					nextOne = Tetromino.randomOne();
				}
			}else {
				int row = tetromino.cells[3].getRow();
				System.out.println(row);
				//摧毁炸弹底部接触所在下面的2行
				if(row <= ROWS-3) {
					for(int i=1;i<3;i++) {
						deleteRow(row+i);
					}
				}
				if (isGameOver()) {
					state = GAME_OVER;
				} else {
					tetromino = nextOne; //将下一个出场的赋给当前方块
					nextOne = Tetromino.randomOne();
				}
			}
		}
	}
	
	//直接下落
	public void hardDropAction() {
		while(canDrop()) {
			tetromino.softDrop();
		}
	}

	/*
	 * Tetris类中,利用重写修改JPanel的paint()方法 修改原有的paint()方法 g 引用了绑定到当前面板上的画笔
	 */
	public void paint(Graphics g) {
		// 修改原有的绘制,变成自定义绘制
		g.drawImage(background, 0, 0, null);
		g.translate(15, 15);
		// paintWall会在同一个面板上绘制墙
		paintWall(g);
		//画当前方块
		paintTetromino(g);
		//画下一个模块
		paintnextOne(g);
		//画出分数
		paintScore(g);
		//画游戏状态图
		paintState(g);
		//绘制时间
		paintTime(g);

	}
	//==================================绘制界面上显示元素START=====================================
	//绘制分数
	private void paintScore(Graphics g) {
		int x = 290;
		int y = 160;
		Font f = new Font(Font.SANS_SERIF,Font.BOLD,30);
		g.setFont(f);
		g.setColor(new Color(255, 255, 255));
		g.drawString("行数:"+line, x, y);
		y+=56;
		g.setColor(new Color(255, 127, 0));
		g.drawString("得分:"+score, x, y);
	}
	//根据不同的游戏状态绘制不同提示语
	private void paintState(Graphics g) {
		int x = 290;
		int y = 160+56+56;
		g.setColor(new Color(192, 255, 62));
		switch(state){
		case RUNNING:
			g.drawString("[P]暂停", x, y);	break;
		case PAUSE:
			g.drawString("[C]继续游戏", x, y);	break;
		case GAME_OVER:
			g.drawString("[S]重新开始", x, y);	
			g.drawImage(gameOver, -15, -15, null);
		}
	}
	
	//绘制时间
	private void paintTime(Graphics g) {
		int x = 290;
		int y = 160+56+56+56;
		Font f = new Font(Font.SANS_SERIF,Font.BOLD,23);
		g.setFont(f);
		g.setColor(new Color(0, 255, 0));
		g.drawString("时间:"+time, x, y);
	}
		
	// 画出下一个出场的方块
	private void paintnextOne(Graphics g) {
		if(nextOne==null){	
			return;
		}
		Cell[] cells = nextOne.cells;
		for (Cell cell : cells) {
			Cell c = cell;
			int col = c.getCol() + 10;
			int row = c.getRow() + 1;
			int x = col * CELL_SIZE;
			int y = row * CELL_SIZE;
			g.drawImage(c.getImage(), x, y+2, null);

		}
	}
    //画出当前出场的方块
	private void paintTetromino(Graphics g) {
		if(tetromino==null){	return;	}
		// cells 引用了正在下落方块的4个格子数组
		Cell[] cells = tetromino.cells;
		for (Cell cell: cells) {
			Cell c = cell;
			int col = c.getCol();
			int row = c.getRow();
			int x = col * CELL_SIZE;
			int y = row * CELL_SIZE;
			g.drawImage(c.getImage(), x, y, null);
		}
	}
	
    // 一个小格子的长度
	public static final int CELL_SIZE = 26;
    // 绘制方格墙体
	private void paintWall(Graphics g) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cell cell = wall[row][col];
				int x = col * CELL_SIZE;
				int y = row * CELL_SIZE;
				if (cell == null) {
					Color c = g.getColor();
					g.setColor(new Color(255, 255, 255));
					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
					g.setColor(c);
				} else {
					g.drawImage(cell.getImage(), x, y, null);
				}
			}
		}

	}
	//====================================END====================================

    //================================方块是否合法运动状态判断START======================
	// coincide 重合，下落的某个格子与墙
	private boolean conicide() {
		Cell[] cells = tetromino.cells;
		for (Cell cell:cells) {
			int row = cell.getRow();
			int col = cell.getCol();
			if (row>=0 && row<ROWS && 
				col>=0 && col<=COLS &&
				wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}
	// 遍历每个方块是否出界
		private boolean outOfBounds() {
			Cell[] cells = tetromino.cells;
			for (Cell c:cells) {
				if (c.getCol() >= COLS || c.getRow() >= ROWS || c.getCol() < 0 || c.getRow() < 0) {
					return true;
				}
			}
			return false;
		}
			
	//判断能否下落
	private boolean canDrop() {
		Cell[] cells = tetromino.cells;
		 //遍历有一个格子在最底层就不能下落
		for (Cell c : cells) {
			int row = c.getRow();
			if (row == (ROWS - 1)) {
				return false;
			}
		}
		//遍历当一个格子下面有其它格子就不能下落
		for (Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if (wall[row + 1][col] != null) { 
				return false;
			}
		}
		return true;
	}
	//================================END=============================

	/**
     * 游戏状态判断
     * @return
     */
	private boolean isGameOver() {
		// 开始的墙上位置如果有格子, 就游戏结束
		Cell[] cells = nextOne.cells;
		for (Cell cell : cells) {
			int row = cell.getRow();
			int col = cell.getCol();
			if (wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}
	
    // 满行销毁
	private int destroyLines() {
		int lines = 0;
		for (int row = 0; row < ROWS; row++) {
			if (fullCells(row)) {
				deleteRow(row);
				lines++;
			}
		}
		return lines;
	}
	private void deleteRow(int row) {
		for (int i = row; i >= 1; i--) {
			System.arraycopy(wall[i - 1], 0, wall[i], 0, COLS);
		}
		Arrays.fill(wall[0], null);
	}
	
	//判断是否满行
	private boolean fullCells(int row) {
		Cell[] line = wall[row];
		for (int i = 0; i < line.length; i++) {
			if (line[i] == null) {
				return false;
			}
		}
		return true;
	}
	
    //方块落在墙上
	private void landIntoWall() {
		Cell[] cells = tetromino.cells;
		for (Cell cell:cells) {
			int row = cell.getRow();
			int col = cell.getCol();
			wall[row][col] = cell;
		}

	}
	
	//=========================方块状态变化操作方法START===========================
	//右移
	public void moveRigthAction() {
		tetromino.moveRigth();
		if (outOfBounds()||conicide()) {
			tetromino.moveLeft();
		}
	}
	//左移
	public void moveLeftAction() {
		tetromino.moveLeft();
		if (outOfBounds()||conicide()) {
			tetromino.moveRigth();
		}
	}
	
	//旋转
	public void rotateRigthAction() {
		if(this.tetromino.cells[0].getImage() != this.Bomb) {
			tetromino.rotateRigth();
			if(outOfBounds()||conicide()) {
				tetromino.rotateLeft();
			}
		}
	}
	//=================================END====================================
}
