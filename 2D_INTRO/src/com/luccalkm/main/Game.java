package com.luccalkm.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.luccalkm.entities.AmmoShot;
import com.luccalkm.entities.Enemy;
import com.luccalkm.entities.Entity;
import com.luccalkm.entities.Player;
import com.luccalkm.graficos.Spritesheet;
import com.luccalkm.graficos.UI;
import com.luccalkm.world.Camera;
import com.luccalkm.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Janela
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 40*5;
	public static final int HEIGHT = 40*4;
	private final int SCALE = 6;
	
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	
	
	// Listar entidades
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<AmmoShot> bullets;
	
	// Spritesheet
	public static Spritesheet spritesheet;
	
	// Mapa
	public static World world;
	
	// Entidade player
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		InitFrame();
		// Inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<AmmoShot>();
		spritesheet = new Spritesheet("/spritesheetZelda.png");
		player = new Player(0,0,16,16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
	}
	
	public void InitFrame() {
		frame = new JFrame("CloneZelda");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e instanceof Player) {
				 //Tick no player
			}
			e.tick();
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		
		if(enemies.size() == 0) {
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			World.restartGame(newWorld);
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphic = image.getGraphics();
		graphic.setColor(new Color(0,0,0));
		graphic.fillRect(0, 0, WIDTH, HEIGHT); // Apagar o que tá na tela antes e depois atualizar
		
		/* Renderizar o Jogo */
		//Graphics2D g2 = (Graphics2D) graphic;
		world.render(graphic);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(graphic);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(graphic);
		}
		ui.render(graphic);
		/***/
		graphic.dispose();
		graphic = bs.getDrawGraphics();
		graphic.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up= true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
					e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up= false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shot = true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShot= true;
		player.mouseX = (e.getX()/SCALE);
		player.mouseY = (e.getY()/SCALE);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
