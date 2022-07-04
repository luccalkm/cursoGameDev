package com.luccalkm.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.luccalkm.main.Game;

public class UI {

	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(8, 4, 50, 8);
		g.setColor(new Color(0,204,0));
		g.fillRect(8, 4, (int)((Game.player.life/Game.player.maxLife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("jazz let", Font.BOLD,8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife,20,11);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.setColor(Color.white);
		g.drawString("Ammo: "+ Game.player.ammo, Game.WIDTH-40, 10);
	}
	
}
