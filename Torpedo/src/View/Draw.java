package View;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import Model.Coordinate;
import Model.Game;

/* Hajók kirajzolásáért felelõs osztály */
public class Draw extends Canvas{
	// Játéktér
	Game _ground;
	// Méret
	String _size;
	// Konstruktor
	Draw(Game ground, String size)
	{
		_ground = ground;
		_size = size;
	}
	// Rajzolást végzõ függvény
	public void paint(Graphics g)
	{		
		// Ha kicsi a pálya
		if(_size.equals("small"))
		{
			// Hajók helyei
			int[] x = {10,45,80,115,150,185,220,255,290,325};
			int[] y = {0,35,70,105,140,175,210,245,280,315};
			// Index értékek
			String[] field_number = {"0","1","2","3","4","5","6","7","8","9"};
			// Indexek kirajzolása
			int l = 0;
			for(int k = 10; k < 335; k+=35)
			{
				g.drawString(field_number[l], k+60, 60);
				g.drawString(field_number[l], k+560, 60);
				l++;
			}
			// Indexek kirajzolása
			l = 0;
			for(int k = 10; k < 335; k+=35)
			{
				g.drawString(field_number[l], 40, k+80);
				g.drawString(field_number[l], 540, k+80);
				l++;
			}
			
			// Hajók kirajzolása
			for(int i = 0; i < 10; i++)
			{
				for(int j = 0; j < 10; j++)
				{
					//String field = _ground.Tablevalue(new Coordinate(i,j));
					String field = _ground.getGround(1).getGroundTable().getValue(new Coordinate(i,j)).toString();
					//Színek beállítása
					if(field.equals("FREE"))						
						g.setColor(Color.blue);
					else if(field.equals("RSVR"))
						g.setColor(Color.LIGHT_GRAY);
					else if(field.equals("_OUT"))
						g.setColor(Color.black);
					else if(field.equals("SCORE"))
						g.setColor(Color.red);
					else
						g.setColor(Color.green);
					// Saját hajók rajzolása
					g.fillRect(x[j]+50, y[i]+70, 30, 30);
					
					//field = _ground.Enemyvalue(new Coordinate(i,j));
					field = _ground.getGround(2).getGroundTable().getValue(new Coordinate(i,j)).toString();
					//Színek beállítása														
					if(field.equals("_OUT"))
						g.setColor(Color.black);
					else if(field.equals("SCORE"))
						g.setColor(Color.red);
					else 
						g.setColor(Color.cyan);		
					// Ellenfél hajóinak kirajzolása
					g.fillRect(x[j]+550, y[i]+70, 30, 30);
					
				}
			}
		}
		// Ha közepes a pálya
		else if(_size.equals("medium"))
		{
			// Hajók helyei
			int[] x = {10,35,60,85,110,135,160,185,210,235,260,285,310,335,360};
			int[] y = {0,25,50,75,100,125,150,175,200,225,250,275,300,325,350};			
			// Index értékek
			String[] field_number = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
			// Indexek kirajzolása
			int l = 0;
			for(int k = 10; k < 370; k+=25)
			{
				g.drawString(field_number[l], k+45, 30);
				g.drawString(field_number[l], k+555, 30);
				l++;
			}
			// Indexek kirajzolása
			l = 0;
			for(int k = 10; k < 370; k+=25)
			{
				g.drawString(field_number[l], 30, k+45);
				g.drawString(field_number[l], 540, k+45);
				l++;
			}
			
			for(int i = 0; i < 15; i++)
			{
				for(int j = 0; j < 15; j++)
				{
					//String field = _ground.Tablevalue(new Coordinate(i,j));
					String field = _ground.getGround(1).getGroundTable().getValue(new Coordinate(i,j)).toString();
					//Színek beállítása
					if(field.equals("FREE"))						
						g.setColor(Color.blue);
					else if(field.equals("RSVR"))
						g.setColor(Color.LIGHT_GRAY);
					else if(field.equals("_OUT"))
						g.setColor(Color.black);
					else if(field.equals("SCORE"))
						g.setColor(Color.red);
					else
						g.setColor(Color.green);
					// Saját hajók rajzolása
					g.fillRect(x[j]+40, y[i]+40, 20, 20);
					
					//field = _ground.Enemyvalue(new Coordinate(i,j));
					field = _ground.getGround(2).getGroundTable().getValue(new Coordinate(i,j)).toString();
					//Színek beállítása
					if(field.equals("_OUT"))
						g.setColor(Color.black);
					else if(field.equals("SCORE"))
						g.setColor(Color.red);
					else 
						g.setColor(Color.cyan);
					// Ellenfél hajóinak kirajzolása
					g.fillRect(x[j]+550, y[i]+40, 20, 20);
				}
			}
		}
		// Ha nagy a pálya
		else
		{
			// Hajók helyei
			int[] x = {10,30,50,70,90,110,130,150,170,190,210,230,250,270,290,310,330,350,370,390};
			int[] y = {0,20,40,60,80,100,120,140,160,180,200,220,240,260,280,300,320,340,360,380};			
			// Index értékek
			String[] field_number = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
			// Indexek kirajzolása
			int l = 0;
			for(int k = 10; k < 400; k+=20)
			{
				g.drawString(field_number[l], k+40, 30);
				g.drawString(field_number[l], k+550, 30);
				l++;
			}
			// Indexek kirajzolása
			l = 0;
			for(int k = 10; k < 400; k+=20)
			{
				g.drawString(field_number[l], 30, k+40);
				g.drawString(field_number[l], 540, k+40);
				l++;
			}
			
			for(int i = 0; i < 20; i++)
			{
				for(int j = 0; j < 20; j++)
				{
					//String field = _ground.Tablevalue(new Coordinate(i,j));
					String field = _ground.getGround(1).getGroundTable().getValue(new Coordinate(i,j)).toString();
					//Színek beállítása
					if(field.equals("FREE"))						
						g.setColor(Color.blue);
					else if(field.equals("RSVR"))
						g.setColor(Color.LIGHT_GRAY);
					else if(field.equals("_OUT"))
						g.setColor(Color.black);
					else if(field.equals("SCORE"))
						g.setColor(Color.red);
					else
						g.setColor(Color.green);
					// Saját hajók rajzolása
					g.fillRect(x[j]+40, y[i]+40, 15, 15);
					
					//field = _ground.Enemyvalue(new Coordinate(i,j));
					field = _ground.getGround(2).getGroundTable().getValue(new Coordinate(i,j)).toString();
					//Színek beállítása
					if(field.equals("_OUT"))
						g.setColor(Color.black);
					else if(field.equals("SCORE"))
						g.setColor(Color.red);
					else 
						g.setColor(Color.cyan);	
					// Ellenfél hajóinak kirajzolása
					g.fillRect(x[j]+550, y[i]+40, 15, 15);
				}
			}
		}			
	}

}
