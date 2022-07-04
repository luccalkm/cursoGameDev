package com.luccalkm.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.luccalkm.entities.*;
import com.luccalkm.graficos.Spritesheet;
import com.luccalkm.main.Game;

public class World {
	
	public static Tiles[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			tiles = new Tiles[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int i = 0; i < map.getWidth(); i++) {
				for(int j = 0; j < map.getHeight(); j++) {
					int pixelAtual = pixels[i + (j * map.getWidth())];
					tiles[i + (j * WIDTH)] = new FloorTile(i*16, j*16, Tiles.TILE_FLOOR);
					
					switch(pixelAtual) {
						case 0xFF000000:
							// Chão grama
							
							tiles[i + (j * WIDTH)] = new FloorTile(i*16, j*16, Tiles.TILE_FLOOR);
							break;
						case 0xFF4C4C4C:
							tiles[i + (j * WIDTH)] = new FloorTileMushroom(i*16, j*16, Tiles.TILE_FLOOR_MUSHROOM);
							break;
							
						case 0xFFFFFFFF:
							// Parede
							tiles[i + (j * WIDTH)] = new WallTile(i*16, j*16, Tiles.TILE_WALL);
							break;
	
						case 0xFF0026FF:
							// Player
							Game.player.setX(i*16);
							Game.player.setY(j*16);
							break;
						
						case 0xFFFF0000:
							// Enemy
							Enemy en = new Enemy(i*16, j*16, 16, 16, Entity.ENEMY);
							Game.entities.add(en);
							Game.enemies.add(en);
							break;
						
				 		case 0xFFFF6A00:
							// Weapon	
				 			Game.entities.add(new Weapon(i*16, j*16, 16, 16, Entity.WEAPON));
				 			break;
				 			
				 		case 0xFF4CFF00:
							// Life	
				 			Life pack = new Life(i*16, j*16, 16, 16, Entity.LIFEPACK);
				 			Game.entities.add(pack);
				 			break;
				 			
				 		case 0xFFFFD800:
							// Munição
				 			Game.entities.add(new Ammo(i*16, j*16, 16, 16, Entity.AMMO));
				 			break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheetZelda.png");
		Game.player = new Player(0,0,16,16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	public static boolean placeFree(int xn, int yn) {
		int x1 = xn / TILE_SIZE;
		int y1 = yn / TILE_SIZE;
		
		int x2 = (xn + TILE_SIZE-5) / TILE_SIZE;
		int y2 = yn / TILE_SIZE;
		
		int x3 = xn / TILE_SIZE;
		int y3 = (yn + TILE_SIZE-5) / TILE_SIZE;
		
		int x4 = (xn + TILE_SIZE-5) / TILE_SIZE;
		int y4 = (yn + TILE_SIZE-5) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for (int i = xstart; i <= xfinal; i++) {
			for (int j = ystart; j <= yfinal; j++) {
				if(i < 0 || j < 0 || i >= WIDTH || j >= HEIGHT)
					continue;
				Tiles tileNow = tiles[i + (j * WIDTH)];
				tileNow.render(g);
			}
		}
	}
}
