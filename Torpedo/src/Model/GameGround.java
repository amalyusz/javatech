package Model;
import java.util.Set;
import java.util.HashSet;



public class GameGround {
	
	static private int instanceNum = 1;
	
	private Set<Ship> ships = new HashSet<Ship>();
	private Table table;
	private int plyrId;
	
	/**
	 * A konstruktorban feltöltjük a set-et hajókkal.
	 * Meg meggondolando az exception dobas mert akkor csak try blokkban lehet machinálni
	 * az osztallyal.
	 * 
	 * @param id
	 * @param size
	 */
	public GameGround(TableSize size)/*throws Exception*/{
		plyrId = instanceNum++;
		table = new Table(size);
		ships.add(new Ship(4, FieldState.FOUR));
		ships.add(new Ship(3, FieldState.THR1));
		ships.add(new Ship(3, FieldState.THR2));
		ships.add(new Ship(2, FieldState.TWO1));
		ships.add(new Ship(2, FieldState.TWO2));
		ships.add(new Ship(2, FieldState.TWO3));
		ships.add(new Ship(1, FieldState.ONE1));
		ships.add(new Ship(1, FieldState.ONE2));
		ships.add(new Ship(1, FieldState.ONE3));
		ships.add(new Ship(1, FieldState.ONE4));
	}
	
	int GetPlyrId(){
		return plyrId;
	}
	
	public Table getGroundTable(){
		return table;
	}
	
	Set<Ship> retSetOfShips(){
		return ships;
	}
	
	
	/**
	 * Egy hajó elhelyezése a táblán. Kiválasztjuk a megadott ID-jû hajót a listából
	 * 
	 * @param coordinates
	 * @param shipId
	 * @return
	 */
	
	Ship selectShip(FieldState shipId) throws Exception{
		for(Ship actShip: ships){
			if(actShip.getId() == shipId){
				return actShip;
			}
		}
		throw new Exception("Invlaid ShipId!");
	}
	
/*	boolean placeShip(Coordinate coordinates, Ship actShip) throws Exception{
		if (actShip.setCoordinates(coordinates, table.getSize()) == false) {
			throw new Exception("can't place ship; out of table range!");
		}
		if(actShip.getPlaced() == false && table.placeShip(actShip, coordinates) == true){
			return true;
		}
		throw new Exception("can't place ship; it is alredy placed");
	}*/
	
	/**
	 * Az összes hajót random módon elhelyezi.
	 * 
	 * @return
	 */
/*	boolean randPlaceShips(){
		for(Ship ship : ships){
			if(table.randPlaceShip(ship) == false){
				return false;
			}
		}
		return true;
	}
	
	boolean clearShip(Ship actShip) throws Exception{
		if(actShip.getPlaced()==false){
			throw new Exception("the selected ship is not placed yet!");
		}else{
			table.clearShip(actShip);
			actShip.unsetPlaced();
			actShip.clearCoordinates();
			if(actShip.getOrientation()==0){
				actShip.changeOrientation();
			}
			return true;
		}
	}
	}*/
	
	
};