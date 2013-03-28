/**
 * 
 * @author zsidi
 * 
 *         Hajó osztály. Minden hajót ez reprezentál!
 */

public class Ship{
	
	static enum Orientation {
		VERTICAL, // függõleges
		HORIZONTAL// vízszintes
	}; 

	private FieldState shipId; 	
	private int size;
	private int health;
	private boolean onGame;
	private Orientation orientation = Orientation.VERTICAL;
	private Coordinate coordinates = new Coordinate(0, 0);
	private boolean placed;

	/**
	 * Konstruktor
	 * 
	 * @param size
	 * @param id
	 */
	Ship(int size, FieldState shipId) {
		if (size > 0 && size < 5) {
			this.size = size;
			this.shipId = shipId;
			health = size;
			placed = false;
			onGame = true;
		}
	}

	/**
	 * EGYEB SPECIFIKUSFUGGVENYEK
	 */
	
			
	 /**
	 * SETTEREK, GETTEREK
	 */
	
	/**
	 * Beállítja a hajó koordinátáit. Figyel hogy ne lódjon ki! 
	 * 
	 * @param coordinates
	 * @param tableSize
	 * @return boolean 
	 */
	void setOnGmame(boolean bool){
		onGame = bool;
	}
	
	void setCoordinates(Coordinate coordinates) {
		this.coordinates = coordinates;
	}
	
	void setHealth(int health){
		this.health = health;
	}
	
	Coordinate getCoordinates() {
		return coordinates;
	}

	void changeOrientation() {
		if (orientation == Orientation.HORIZONTAL) {
			orientation = Orientation.VERTICAL;
		} else {
			orientation = Orientation.HORIZONTAL;
		}
	}
	
	int getOrientation() {
		if (orientation == Orientation.VERTICAL) {
			return 1;
		} else {
			return 0;
		}
	}

	void setPlaced(){
		placed = true;
	}
	
	void unsetPlaced(){
		placed = false;
	}
		
	boolean getPlaced(){
		return placed;
	}
	
	int getSize() {
		return size;
	}
	
	FieldState getId(){
		return shipId;
	}
	
	void clearCoordinates(){
		this.coordinates = new Coordinate(0,0);
	}
	
	int getHealth(){
		return health;
	}
	
	void lowerHealth(){
		this.health--;
	}
	
	boolean getOnGame(){
		return onGame;
	}
	
	void changeOnGame(){
		this.onGame = false;
	}
	/**
	 * A set miatt kellettfelülírni, implementálni kell az egyenlóséget, hogy
	 * rendesen mûködjön a collection
	 */
	
	@Override
	public boolean equals(Object obj) {
		Ship c = (Ship)obj;
		if(c.getId()==this.getId()){
			return true;
		}else{
			return false;
		}
	}
	
}
