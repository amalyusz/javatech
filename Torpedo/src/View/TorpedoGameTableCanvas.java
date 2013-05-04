package View;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.naming.directory.InvalidAttributeValueException;

import Model.Coordinate;
import Model.Game;
import Model.ShipCoordinates;

/**
 * Haj�k kirajzol�s��rt felel�s oszt�ly.
 * 
 * @author amalyusz
 */
@SuppressWarnings("serial")
public class TorpedoGameTableCanvas extends Canvas {
	// J�t�kt�r
	private Game ground;
	private ShipCoordinates shipCoordinates;
	
	// Konstruktor
	public TorpedoGameTableCanvas(Game ground, String gameTableSize) throws InvalidAttributeValueException
	{
		this.ground = ground;
		this.shipCoordinates = ShipCoordinates.createInstance(gameTableSize);
	}
	// Rajzol�st v�gz� f�ggv�ny
	public void paint(Graphics g)
	{		
		for (int i = 1; i < shipCoordinates.getFieldNumbers(); i++) {
			for (int j = 0; j < shipCoordinates.getFieldNumbers(); j++) {
				String field = ground.getGround(1).getGroundTable().getValue(new Coordinate(i,j)).toString();
				//Sz�nek be�ll�t�sa
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
				// Saj�t haj�k rajzol�sa
				g.fillRect(shipCoordinates.getUserWidth()[j], shipCoordinates.getHeight()[i], shipCoordinates.getFieldSize(), shipCoordinates.getFieldSize());
				
				field = ground.getGround(2).getGroundTable().getValue(new Coordinate(i,j)).toString();
				
				if(field.equals("_OUT"))
					g.setColor(Color.black);
				else if(field.equals("SCORE"))
					g.setColor(Color.red);
				else 
					g.setColor(Color.cyan);		
				// Ellenf�l haj�inak kirajzol�sa
				g.fillRect(shipCoordinates.getEnemyWidth()[j], shipCoordinates.getHeight()[i], shipCoordinates.getFieldSize(), shipCoordinates.getFieldSize());
			}
		}
	}
}
