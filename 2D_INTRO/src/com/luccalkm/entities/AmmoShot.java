package com.luccalkm.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.luccalkm.main.Game;
import com.luccalkm.world.Camera;

public class AmmoShot extends Entity{
	
	private double dx, dy;
	private double spd = 4;
	public BufferedImage gunshot;
	
	private int life = 10, curLife = 0;

	public AmmoShot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, null);
		this.dx = dx;
		this.dy = dy;
		gunshot = Game.spritesheet.getSprite(128+16,0,16,16);
		
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		if(curLife == life) {
			Game.bullets.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(gunshot, this.getX() - Camera.x, getY() - Camera.y, null);
	}
	
}
