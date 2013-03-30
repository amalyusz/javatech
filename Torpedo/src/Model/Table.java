package Model;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author zsidi
 * 
 *         Tábla osztály. Itt lesznek példányosítva a hajók???? Minden
 *         játékoshoz egy tábla tartozik!
 */

public class Table{

	private Map<Coordinate, FieldState> table = new HashMap<Coordinate, FieldState>();
	private TableSize size;

	Table(TableSize size) {
		this.size = size;

		for (int i = size.getInt() - 1; i >= 0; i--) {
			for (int j = size.getInt() - 1; j >= 0; j--) {
				table.put(new Coordinate(i, j), FieldState.FREE);
			}
		}
	}
	// Megváltoztatjuk a dolgokat. Csak getterek és setterek lesznek itt!
	
	TableSize getSize() {
		return size;
	}
	
	Map<Coordinate, FieldState> getTable(){
		return table;
	}
	
	public FieldState getValue(Coordinate key){
			return table.get(key);
	}
	
	boolean setValue(Coordinate coord, FieldState state){
		if(coord.getX()<=size.getInt() && coord.getY()<=size.getInt()
				&& coord.getX()>=0 && coord.getY()>=0){
			table.put(coord, state);
			return true;
		}
		return false;
	}

	
	/**
	 * ITT JONNEK A HAJOK LERAKASAERT FELELOS FUGGVENYEK
	 */
	
	
	/**
	 * Ez a függvény felelõs a hajók kézi elhelyezéséért Még a koordinátákat
	 * valahogy jól meg kell csinálni!
	 * 
	 * MÉG NINCS KIPRÓBÁLVA!!!!
	 * 
	 * @param ship
	 * @param coordinates
	 * @return
	 */
/*	boolean placeShip(Ship ship, Coordinate coordinates) {
		int x = coordinates.getX();
		int y = coordinates.getY();
		if (ship.getOrientation() == 1) {
			// csak a hajón kell végigmenni, ha nem free mezõt találunk akkor
			// gáz van
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if (table.get(new Coordinate(x, y + i)) != FieldState.FREE) {
					return false;
				}
			}
			// Ez a for ciklus a hajó körüli lefoglalt mezõk miatt felelõs
			for (int i = -1; i <= 1; i++) {
				table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
				table.put(new Coordinate(x + i, y + ship.getSize()),
						FieldState.RSVR);
			}
			// Ez a for ciklus a hajót helyezi el, valamint a két oldalán lévõ
			// foglalt mezõket
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				table.put(new Coordinate(x, y + i), ship.getId());
				table.put(new Coordinate(x - 1, y + i), FieldState.RSVR);
				table.put(new Coordinate(x + 1, y + i), FieldState.RSVR);
			}
			ship.setPlaced();
			return true;
		} else {
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if (table.get(new Coordinate(x + i, y)) != FieldState.FREE) {
					return false;
				}
			}
			// Ez a for ciklus a hajó körüli lefoglalt mezõk miatt felelõs
			for (int i = -1; i <= 1; i++) {
				table.put(new Coordinate(x - 1, y + i), FieldState.RSVR);
				table.put(new Coordinate(x + ship.getSize(), y + i),
						FieldState.RSVR);
			}
			// Ez a for ciklus a hajót helyezi el, valamint a két oldalán lévõ
			// foglalt mezõket
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				table.put(new Coordinate(x + i, y), ship.getId());
				table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
				table.put(new Coordinate(x + i, y + 1), FieldState.RSVR);
			}
			ship.setPlaced();
			return true;
		}
	}*/

	/**
	 * Ez random lerakja a paraméterben megkapott hajót. Elvileg ügyel a
	 * szabályok betartására!! Esetleg még tesztelésre szorul!
	 * 
	 * @param ship
	 * @return
	 */
/*	boolean randPlaceShip(Ship ship) {
		Coordinate coord = new Coordinate(40, 40); // ilyen koordináta nincs
		boolean wouldFit = false;
		boolean newCoord = false;
		int falseCaseCounter = 0; // Ha túl sok ideig nem találunk jó
									// koordinátát,
									// akkor valszeg nem tudjuk elhelyezni a
									// hajót!!
									// Ezt a következõ while() ciklusban
									// vizsgáljuk jelenlegi küszöbérték: 100

		while (wouldFit == false && falseCaseCounter <= 100) {
			// Az orientációt is randomizáljuk
			if ((int) (Math.random() * 2) >= 1) {
				ship.changeOrientation();
			}

			while (ship.setCoordinates(coord, size) == false
					|| newCoord == true) {
				coord.setX((int) (Math.random() * (size.getInt() - 1)));
				coord.setY((int) (Math.random() * (size.getInt() - 1)));
				newCoord = false;
			}
			System.out.println(coord.getX() + " " + coord.getY());
			int x = coord.getX();
			int y = coord.getY();
			if (ship.getOrientation() == 1) {
				// csak a hajón kell végigmenni, ha nem free mezõt találunk
				// akkor gáz van
				for (int i = ship.getSize() - 1; i >= 0; i--) {
					if (table.get(new Coordinate(x, y + i)) != FieldState.FREE) {
						wouldFit = false; // másik hajóba ütköztünk!
						newCoord = true; // új koordinátákat kérünk!
						falseCaseCounter++;
						break; // Amint hibát találunk brékelünk!
					} else {
						wouldFit = true; // passzol a hajó a helyére, már cska
										 // be kell rakni
						ship.setPlaced();// Beállítjuk a hajóban, hogy le lett rakva
					}
				}
				if (wouldFit == true) {
					// Ez a for ciklus a hajó körüli lefoglalt mezõk miatt
					// felelõs
					for (int i = -1; i <= 1; i++) {
						table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
						table.put(new Coordinate(x + i, y + ship.getSize()),
								FieldState.RSVR);
					}
					// Ez a for ciklus a hajót helyezi el, valamint a két
					// oldalán lévõ foglalt mezõket
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						table.put(new Coordinate(x, y + i), ship.getId());
						table.put(new Coordinate(x - 1, y + i), FieldState.RSVR);
						table.put(new Coordinate(x + 1, y + i), FieldState.RSVR);
					}
					// wouldFit = false;
				}
			} else {
				for (int i = ship.getSize() - 1; i >= 0; i--) {
					if (table.get(new Coordinate(x + i, y)) != FieldState.FREE) {
						wouldFit = false;
						newCoord = true;
						falseCaseCounter++;
						break;
					} else {
						wouldFit = true;
						ship.setPlaced();
					}
				}
				if (wouldFit == true) {
					// Ez a for ciklus a hajó körüli lefoglalt mezõk miatt
					// felelõs
					for (int i = -1; i <= 1; i++) {
						table.put(new Coordinate(x - 1, y + i), FieldState.RSVR);
						table.put(new Coordinate(x + ship.getSize(), y + i),
								FieldState.RSVR);
					}
					// Ez a for ciklus a hajót helyezi el, valamint a két
					// oldalán lévõ foglalt mezõket
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						table.put(new Coordinate(x + i, y), ship.getId());
						table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
						table.put(new Coordinate(x + i, y + 1), FieldState.RSVR);
					}
					// wouldFit = false;
				}
			}
		}
		// jelezzük, hogy mi miatt történt a kilépés, hogy törölni tudjuk a
		// táblát és újrakezdeni a hajók elhelyezését
		if (falseCaseCounter != 100) {
			return true;
		} else {
			return false;
		}
	}*/
	
	/**
	 * Torli a megkapott hajot a tablabol!
	 * A hajora vonatozo dolgok a GameGroundosztalyban vannak implementalva
	 * 
	 * @param ship
	 */
/*	void clearShip(Ship ship){
		int x = ship.getCoordinates().getX();
		int y = ship.getCoordinates().getY();
		//A hajót kitöröljük, dea körülötte lévõ RSVR mezõk maradnak, azokat külön
		//vizsgáljuk
		if(ship.getOrientation()==1){
			for(int i = y; i < y + ship.getSize(); i++){
				table.put(new Coordinate(x,i), FieldState.FREE);
			}
		}else{
			for(int i = x; i < x + ship.getSize(); i++){
				table.put(new Coordinate(i, y), FieldState.FREE);
			}
		}
		int besideShip = 0;
		//Itt vizsgáljuk az RSVR mezõket
		if(ship.getOrientation() == 1){
			//Kiválasztjuk hogy melyik mezõ szomszédjait vizsgáljuk
			for(int i = x-1; i <= x+1; i++){
				for(int j = y-1; j < y + ship.getSize()+2; j++){
					//Ha ez a mezõ nem RSVR akkor nem is foglalkozunk vele
					if(table.get(new Coordinate(i, j)) == FieldState.RSVR){
						//Végignézzük a kiválaszotott mezõ szomszédjait
						for(int k = i-1; k <= i+1; k++){
							for(int l = j-1; l <= j+1; l++){
								//Ha valamit találunk rajtuk, azt megjegyezzük
								if(table.get(new Coordinate(k, l)) != FieldState.FREE &&
								table.get(new Coordinate(k, l)) != FieldState.RSVR &&
								table.get(new Coordinate(k, l)) != null){
									besideShip++;
								}
							}
						}
						//Ha semmi nem volt a környéken, törölhetõ a mezõ, egyébként nem
						if(besideShip == 0){
							table.put(new Coordinate(i, j), FieldState.FREE);
						}
						besideShip = 0;
					}
				}
			}
		}else{
			//Kiválasztjuk hogy melyik mezõ szomszédjait vizsgáljuk
			for(int i = y-1; i <= y+1; i++){
				for(int j = x-1; j < x + ship.getSize()+2; j++){
					//Ha ez a mezõ nem RSVR akkor nem is foglalkozunk vele
					if(table.get(new Coordinate(j, i)) == FieldState.RSVR){
						//Végignézzük a kiválaszotott mezõ szomszédjait
						for(int k = i-1; k <= i+1; k++){
							for(int l = j-1; l <= j+1; l++){
								//Ha valamit találunk rajtuk, azt megjegyezzük
								if(table.get(new Coordinate(k, l)) != FieldState.FREE &&
								table.get(new Coordinate(k, l)) != FieldState.RSVR &&
								table.get(new Coordinate(k, l)) != null){
									besideShip++;
								}
							}
						}
						//Ha semmi nem volt a környéken, törölhetõ a mezõ, egyébként nem
						if(besideShip == 0){
							table.put(new Coordinate(i, j), FieldState.FREE);
						}
						besideShip = 0;
					}
				}
			}
		}
	}

	void printTable() {
		for (int i = 0; i < size.getInt(); i++) {
			for (int j = 0; j < size.getInt(); j++) {
				System.out.print(table.get(new Coordinate(j, i)) + " ");
			}
			System.out.print("\n");
		}
	}
	
	void clearTable(){
		for (int i = size.getInt() - 1; i >= 0; i--) {
			for (int j = size.getInt() - 1; j >= 0; j--) {
				table.put(new Coordinate(i, j), FieldState.FREE);
			}
		}
	}*/
	
	 /**
	 * SETTEREK, GETTEREK
	 **/

}
