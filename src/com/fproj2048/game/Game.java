package com.fproj2048.game;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, Runnable {

	
	private static final long serialVersionUID = 1L;
	//width of the screen
	public static final int WIDTH = 415;
	//Height of the screen
	public static final int HEIGHT = 530;
	//FONT 
	public static final Font main = new Font("Bebas Neue Regular", Font.PLAIN,28);
	private Thread game;
	private boolean running;
	private BufferedImage Image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private GameBoard board;
	
	private long StartTime;
	private long elapsed;
	private boolean set;
	
	
	public Game() {
		//allows key inputs
		setFocusable(true);
		//this will determine how big the frame is
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		board = new GameBoard(WIDTH/2 - GameBoard.BOARD_WIDTH/2, HEIGHT - GameBoard.BOARD_HEIGHT - 10);
		
	}
	
	private void update() {
		board.update();
		Keyboard.update();
	}
	
	private void render() {
		Graphics2D g = (Graphics2D) Image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		board.render(g);
		g.dispose();
		
		Graphics g2d = (Graphics2D) getGraphics();
		g2d.drawImage(Image, 0, 0, null);
		g.dispose();
	}
	
	public void run() {
		int fps = 0, updates =0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 100000000.0/60;
		
		//last update time in nanoseconds
		double then = System.nanoTime();
		double unprocessed = 0;
		//update queue
		while(running) {

			boolean shouldRender = false;
			double now = System.nanoTime();
			unprocessed += ( now - then ) / nsPerUpdate;
			then = now;
		while(unprocessed >= 1) {
			updates++;
			update();
			unprocessed--;
			shouldRender = true;
		}
		//render
		if(shouldRender) {
			fps++;
			render();
			shouldRender = false;
		}
		//for pausing the thread
		else {
			try {
				Thread.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		}
		//FPS Timer
		if(System.currentTimeMillis() - fpsTimer>1000) {
			System.out.printf("%d fps %d updates", fps, updates);
			System.out.println();
			fps =0;
			updates =0;
			fpsTimer +=1000;
		}		
	}
	public synchronized void start() {
		if(running) return;
		running = true;
		game = new Thread(this,"game");
		game.start();
	}
	public synchronized void stop() {
		if(running) return;
		running = false;
		System.exit(0);
		
	}
	
	public void keyPressed(KeyEvent e) {
		Keyboard.keyPressed(e);
	}
	public void keyReleased(KeyEvent e) {
		Keyboard.keyReleased(e);
	}
	public void keyTyped(KeyEvent e) {
		
	}
	
}
