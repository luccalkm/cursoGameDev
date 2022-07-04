package com.luccalkm.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.luccalkm.main.Game;
import com.luccalkm.world.Camera;

public class Entity {

	public static BufferedImage LIFEPACK = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage WEAPON = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	public static BufferedImage AMMO = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage ENEMY = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 16, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(128, 32, 16, 16);
	
	protected double x, y;
	protected int width, height;
	
	private BufferedImage sprite;
	
	private int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);

		return e1Mask.intersects(e2Mask);
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX() - Camera.x, getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() +maskx- Camera.x, getY()+masky - Camera.y, mwidth, mheight);
	}
	
}
