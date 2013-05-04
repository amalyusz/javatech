package View;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.naming.directory.InvalidAttributeValueException;

import Model.Coordinate;
import Model.Game;
import Model.ShipCoordinates;

/**
 * Hajók kirajzolásáért felelõs osztály.
 * 
 * @author amalyusz
 */
@SuppressWarnings("serial")
public class TorpedoGameTableCanvas extends Canvas {
	// Játéktér
	private Game ground;
	private ShipCoordinates shipCoordinates;
	
	// Konstruktor
	public TorpedoGameTableCanvas(Game ground, String gameTableSize) throws InvalidAttributeValueException
	{
		this.ground = ground;
		this.shipCoordinates = ShipCoordinates.createInstance(gameTableSize);
	}
	// Rajzolást végzõ függvény
	public void paint(Graphics g)
	{		
		for (int i = 1; i < shipCoordinates.getFieldNumbers(); i++) {
			for (int j = 0; j < shipCoordinates.getFieldNumbers(); j++) {
				String field = ground.getGround(1).getGroundTable().getValue(new Coordinate(i,j)).toString();
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
				g.fillRect(shipCoordinates.getUserWidth()[j], shipCoordinates.getHeight()[i], shipCoordinates.getFieldSize(), shipCoordinates.getFieldSize());
				
				field = ground.getGround(2).getGroundTable().getValue(new Coordinate(i,j)).toString();
				
				if(field.equals("_OUT"))
					g.setColor(Color.black);
				else if(field.equals("SCORE"))
					g.setColor(Color.red);
				else 
					g.setColor(Color.cyan);		
				// Ellenfél hajóinak kirajzolása
				g.fillRect(shipCoordinates.getEnemyWidth()[j], shipCoordinates.getHeight()[i], shipCoordinates.getFieldSize(), shipCoordinates.getFieldSize());
			}
		}
	}
}
