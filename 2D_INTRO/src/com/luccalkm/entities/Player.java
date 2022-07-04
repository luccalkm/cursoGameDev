package com.luccalkm.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.luccalkm.graficos.Spritesheet;
import com.luccalkm.main.Game;
import com.luccalkm.world.Camera;
import com.luccalkm.world.World;

public class Player extends Entity{

	public boolean right, up, left, down;
	public int rightdir = 0, updir = 1, leftdir = 1, downdir = 1;
	public int dir = rightdir;
	public double speed = 1;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage[] playerDamageRight, playerDamageLeft;
	private int damageFrames = 0;
	
	public boolean shot = false, mouseShot = false;
	public int mouseX = 0, mouseY = 0;
	
	public double life = 100, maxLife = 100;
	public int ammo = 0;
	
	public boolean IsDamaged = false;
	private boolean hasGun = false;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamageRight = new BufferedImage[4];
		playerDamageLeft = new BufferedImage[4];

		for(int i = 0; i < 4; i++) {
			playerDamageRight[i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
			playerDamageLeft[i] = Game.spritesheet.getSprite(32 + (i*16), 64, 16, 16);
		}
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void tick() {
		moved = false;
		if(right && World.placeFree((int)(x+speed), this.getY())) {
			moved = true;
			dir = rightdir;
			x+=speed;
		}
		else if(left && World.placeFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = leftdir;
			x-=speed;
		}
		
		if(up && World.placeFree(this.getX(), (int)(y-speed))) {
			moved = true;
			dir = updir;
			y-=speed;
			
		}
		else if(down && World.placeFree(this.getX(), (int)(y+speed))) {
			moved = true;
			dir = downdir;
			y+=speed;
			
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		checkGun();
		checkAmmo();
		checkLife();
		
		if(mouseShot) {
			
			mouseShot = false;
			double angle = Math.atan2(mouseY-(this.getY()+8  - Camera.y), mouseX-(this.getX()+8 - Camera.x));
			
			if(hasGun && ammo > 0){
			ammo--;
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			int px = 0;
			int py = 0;
			
			if(dir == rightdir) {
				px = 14;
				py = 0;
			}
			
			else if(dir == leftdir) {
				px = -14;
				py = 2;
			}
			
			AmmoShot bullet = new AmmoShot(this.getX() +px, getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			}
		}
		
		if(shot) {
			shot = false;
			if(hasGun && ammo > 0){
				
			ammo--;
			int dx = 0;
			int px = 0;
			int py = 0;
			if(dir == rightdir) {
				dx = 1;
				px = 14;
				py = 0;
			}
			
			else if(dir == leftdir) {
				dx = -1;
				px = -14;
				py = 2;
			}
			AmmoShot bullet = new AmmoShot(this.getX() +px, getY() + py, 3, 3, null, dx, 0);
			Game.bullets.add(bullet);
			
			}
		}
		
		if(IsDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 30) {
				this.damageFrames = 0;
				IsDamaged = false;
			}
			
		}
		
		if(life <= 0) {
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheetZelda.png");
			Game.player = new Player(0,0,16,16, Game.spritesheet.getSprite(32, 0, 16, 16));
			Game.entities.add(Game.player);
			Game.world = new World("/level1.png");
			return;
		}
		
		Camera.x = Camera.clamp(this.getX()-(Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY()-(Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkLife() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Life) {
				if (Entity.isColidding(this, e)){
					life += 15;
					if(life > 99) {
						life = 100;
						Game.entities.remove(i);
					}
					return;
				}
			}
		}
	}
	
	public void checkGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Weapon) {
				if (Entity.isColidding(this, e)){
					hasGun = true;
					
					Game.entities.remove(i);
					return;
					}
				}
			}
		}
	
	public void checkAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Ammo) {
				if (Entity.isColidding(this, e)){
					ammo+=5;
					Game.entities.remove(i);
					return;
					}
				}
			}
		}
	
	
	public void render(Graphics g) {
		if(!IsDamaged) {
			if(dir == rightdir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() +4 - Camera.y, null);
				}
			}
			
			else if(dir == leftdir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 8- Camera.x, this.getY() +4 - Camera.y, null);
				}
			}
		}
		else {
			if(dir == rightdir) {
				g.drawImage(playerDamageRight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() +4 - Camera.y, null);
				}
			
			}
			else if(dir == leftdir) {
				g.drawImage(playerDamageLeft[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 8- Camera.x, this.getY() +4 - Camera.y, null);
				}
			}
		}
	}
}
