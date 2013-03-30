package Model;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author zsidi
 * 
 *         T�bla oszt�ly. Itt lesznek p�ld�nyos�tva a haj�k???? Minden
 *         j�t�koshoz egy t�bla tartozik!
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
	// Megv�ltoztatjuk a dolgokat. Csak getterek �s setterek lesznek itt!
	
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
	 * Ez a f�ggv�ny felel�s a haj�k k�zi elhelyez�s��rt M�g a koordin�t�kat
	 * valahogy j�l meg kell csin�lni!
	 * 
	 * M�G NINCS KIPR�B�LVA!!!!
	 * 
	 * @param ship
	 * @param coordinates
	 * @return
	 */
/*	boolean placeShip(Ship ship, Coordinate coordinates) {
		int x = coordinates.getX();
		int y = coordinates.getY();
		if (ship.getOrientation() == 1) {
			// csak a haj�n kell v�gigmenni, ha nem free mez�t tal�lunk akkor
			// g�z van
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if (table.get(new Coordinate(x, y + i)) != FieldState.FREE) {
					return false;
				}
			}
			// Ez a for ciklus a haj� k�r�li lefoglalt mez�k miatt felel�s
			for (int i = -1; i <= 1; i++) {
				table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
				table.put(new Coordinate(x + i, y + ship.getSize()),
						FieldState.RSVR);
			}
			// Ez a for ciklus a haj�t helyezi el, valamint a k�t oldal�n l�v�
			// foglalt mez�ket
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
			// Ez a for ciklus a haj� k�r�li lefoglalt mez�k miatt felel�s
			for (int i = -1; i <= 1; i++) {
				table.put(new Coordinate(x - 1, y + i), FieldState.RSVR);
				table.put(new Coordinate(x + ship.getSize(), y + i),
						FieldState.RSVR);
			}
			// Ez a for ciklus a haj�t helyezi el, valamint a k�t oldal�n l�v�
			// foglalt mez�ket
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
	 * Ez random lerakja a param�terben megkapott haj�t. Elvileg �gyel a
	 * szab�lyok betart�s�ra!! Esetleg m�g tesztel�sre szorul!
	 * 
	 * @param ship
	 * @return
	 */
/*	boolean randPlaceShip(Ship ship) {
		Coordinate coord = new Coordinate(40, 40); // ilyen koordin�ta nincs
		boolean wouldFit = false;
		boolean newCoord = false;
		int falseCaseCounter = 0; // Ha t�l sok ideig nem tal�lunk j�
									// koordin�t�t,
									// akkor valszeg nem tudjuk elhelyezni a
									// haj�t!!
									// Ezt a k�vetkez� while() ciklusban
									// vizsg�ljuk jelenlegi k�sz�b�rt�k: 100

		while (wouldFit == false && falseCaseCounter <= 100) {
			// Az orient�ci�t is randomiz�ljuk
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
				// csak a haj�n kell v�gigmenni, ha nem free mez�t tal�lunk
				// akkor g�z van
				for (int i = ship.getSize() - 1; i >= 0; i--) {
					if (table.get(new Coordinate(x, y + i)) != FieldState.FREE) {
						wouldFit = false; // m�sik haj�ba �tk�zt�nk!
						newCoord = true; // �j koordin�t�kat k�r�nk!
						falseCaseCounter++;
						break; // Amint hib�t tal�lunk br�kel�nk!
					} else {
						wouldFit = true; // passzol a haj� a hely�re, m�r cska
										 // be kell rakni
						ship.setPlaced();// Be�ll�tjuk a haj�ban, hogy le lett rakva
					}
				}
				if (wouldFit == true) {
					// Ez a for ciklus a haj� k�r�li lefoglalt mez�k miatt
					// felel�s
					for (int i = -1; i <= 1; i++) {
						table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
						table.put(new Coordinate(x + i, y + ship.getSize()),
								FieldState.RSVR);
					}
					// Ez a for ciklus a haj�t helyezi el, valamint a k�t
					// oldal�n l�v� foglalt mez�ket
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
					// Ez a for ciklus a haj� k�r�li lefoglalt mez�k miatt
					// felel�s
					for (int i = -1; i <= 1; i++) {
						table.put(new Coordinate(x - 1, y + i), FieldState.RSVR);
						table.put(new Coordinate(x + ship.getSize(), y + i),
								FieldState.RSVR);
					}
					// Ez a for ciklus a haj�t helyezi el, valamint a k�t
					// oldal�n l�v� foglalt mez�ket
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						table.put(new Coordinate(x + i, y), ship.getId());
						table.put(new Coordinate(x + i, y - 1), FieldState.RSVR);
						table.put(new Coordinate(x + i, y + 1), FieldState.RSVR);
					}
					// wouldFit = false;
				}
			}
		}
		// jelezz�k, hogy mi miatt t�rt�nt a kil�p�s, hogy t�r�lni tudjuk a
		// t�bl�t �s �jrakezdeni a haj�k elhelyez�s�t
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
		//A haj�t kit�r�lj�k, dea k�r�l�tte l�v� RSVR mez�k maradnak, azokat k�l�n
		//vizsg�ljuk
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
		//Itt vizsg�ljuk az RSVR mez�ket
		if(ship.getOrientation() == 1){
			//Kiv�lasztjuk hogy melyik mez� szomsz�djait vizsg�ljuk
			for(int i = x-1; i <= x+1; i++){
				for(int j = y-1; j < y + ship.getSize()+2; j++){
					//Ha ez a mez� nem RSVR akkor nem is foglalkozunk vele
					if(table.get(new Coordinate(i, j)) == FieldState.RSVR){
						//V�gign�zz�k a kiv�laszotott mez� szomsz�djait
						for(int k = i-1; k <= i+1; k++){
							for(int l = j-1; l <= j+1; l++){
								//Ha valamit tal�lunk rajtuk, azt megjegyezz�k
								if(table.get(new Coordinate(k, l)) != FieldState.FREE &&
								table.get(new Coordinate(k, l)) != FieldState.RSVR &&
								table.get(new Coordinate(k, l)) != null){
									besideShip++;
								}
							}
						}
						//Ha semmi nem volt a k�rny�ken, t�r�lhet� a mez�, egy�bk�nt nem
						if(besideShip == 0){
							table.put(new Coordinate(i, j), FieldState.FREE);
						}
						besideShip = 0;
					}
				}
			}
		}else{
			//Kiv�lasztjuk hogy melyik mez� szomsz�djait vizsg�ljuk
			for(int i = y-1; i <= y+1; i++){
				for(int j = x-1; j < x + ship.getSize()+2; j++){
					//Ha ez a mez� nem RSVR akkor nem is foglalkozunk vele
					if(table.get(new Coordinate(j, i)) == FieldState.RSVR){
						//V�gign�zz�k a kiv�laszotott mez� szomsz�djait
						for(int k = i-1; k <= i+1; k++){
							for(int l = j-1; l <= j+1; l++){
								//Ha valamit tal�lunk rajtuk, azt megjegyezz�k
								if(table.get(new Coordinate(k, l)) != FieldState.FREE &&
								table.get(new Coordinate(k, l)) != FieldState.RSVR &&
								table.get(new Coordinate(k, l)) != null){
									besideShip++;
								}
							}
						}
						//Ha semmi nem volt a k�rny�ken, t�r�lhet� a mez�, egy�bk�nt nem
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
