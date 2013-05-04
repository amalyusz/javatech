package Model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * J�t�k logik�ja
 * 
 * @author amalyusz
 */
public class Game {
	static private int state = 0;
	
	private GameGround ground1;
	private GameGround ground2;
	
	public Game(TableSize tableSize){
		ground1 = new GameGround(tableSize);
		ground2 = new GameGround(tableSize);
	}

	
	public GameGround getGround(int plyrId){
		if(plyrId == ground1.GetPlyrId()){
			return ground1;
		}else{
			return ground2;
		}
	}
	
	void setState(int i){
		state = i;
	}
	
	int getState(){
		return state;
	}
	
	/**
	 * Amelyik gameground-ot fel�l akarjuk �rni annak az id-j�t kell megadni a setterben
	 * 
	 * @param ground
	 * @param plyrId
	 */
	void setGround(GameGround ground, int plyrId){
		if(plyrId == ground1.GetPlyrId()){
			ground1 = ground;
		}else{
			ground2 = ground;
		}
	}
	
	/**
	 * 	Egy haj� lerak�s�rt felel�s f�ggv�ny.
	 * 
	 * @param ship
	 * @param coordinates
	 * @param plyrId
	 * @return
	 */
	boolean placeShip(Ship ship, Coordinate coordinates, int plyrId){
		int x = coordinates.getX();
		int y = coordinates.getY();
		if (ship.getOrientation() == 1) {
			// El fog f�rni a haj� a t�bl�n?
			if(x + ship.getSize() <= ground1.getGroundTable().getSize().getInt()){
				ship.setCoordinates(coordinates);
			}else{
				return false;
			}
			// csak a haj�n kell v�gigmenni, ha nem free mez�t tal�lunk akkor
			// g�z van
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if(plyrId == ground1.GetPlyrId()){
					if (ground1.getGroundTable().getValue(new Coordinate(x, y + i)) != FieldState.FREE) {
						return false;
					}
				}else{
					if (ground2.getGroundTable().getValue(new Coordinate(x, y + i)) != FieldState.FREE) {
						return false;
					}
				}
			}
			// Ez a for ciklus a haj� k�r�li lefoglalt mez�k miatt felel�s
			for (int i = -1; i <= 1; i++) {
				if(plyrId == ground1.GetPlyrId()){
					ground1.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
					ground1.getGroundTable().setValue(new Coordinate(x + i, y + ship.getSize()), FieldState.RSVR);
				}else{
					ground2.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
					ground2.getGroundTable().setValue(new Coordinate(x + i, y + ship.getSize()), FieldState.RSVR);
				}
			}
			// Ez a for ciklus a haj�t helyezi el, valamint a k�t oldal�n l�v�
			// foglalt mez�ket
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if(plyrId == ground1.GetPlyrId()){
					ground1.getGroundTable().setValue(new Coordinate(x, y + i), ship.getId());
					ground1.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
					ground1.getGroundTable().setValue(new Coordinate(x + 1, y + i), FieldState.RSVR);
				}else{
					ground2.getGroundTable().setValue(new Coordinate(x, y + i), ship.getId());
					ground2.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
					ground2.getGroundTable().setValue(new Coordinate(x + 1, y + i), FieldState.RSVR);
				}
			}
			ship.setPlaced();
			return true;
		} else {
			// El fog f�rni a haj� a t�bl�n?
			if(y + ship.getSize() <= ground1.getGroundTable().getSize().getInt()){
				ship.setCoordinates(coordinates);
			}else{
				return false;
			}
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if(plyrId == ground1.GetPlyrId()){
					if (ground1.getGroundTable().getValue(new Coordinate(x+i, y)) != FieldState.FREE)
						return false;
				}else{
					if (ground2.getGroundTable().getValue(new Coordinate(x+i, y)) != FieldState.FREE)
						return false;
				}
			}
			// Ez a for ciklus a haj� k�r�li lefoglalt mez�k miatt felel�s
			for (int i = -1; i <= 1; i++) {
				if(plyrId == ground1.GetPlyrId()){
					ground1.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
					ground1.getGroundTable().setValue(new Coordinate(x + ship.getSize(), y + i),
							FieldState.RSVR);
				}else{
					ground2.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
					ground2.getGroundTable().setValue(new Coordinate(x + ship.getSize(), y + i),
							FieldState.RSVR);
				}
			}
			for (int i = ship.getSize() - 1; i >= 0; i--) {
				if(plyrId == ground1.GetPlyrId()){
					ground1.getGroundTable().setValue(new Coordinate(x + i, y), ship.getId());
					ground1.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
					ground1.getGroundTable().setValue(new Coordinate(x + i, y + 1), FieldState.RSVR);
				}else{
					ground2.getGroundTable().setValue(new Coordinate(x + i, y), ship.getId());
					ground2.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
					ground2.getGroundTable().setValue(new Coordinate(x + i, y + 1), FieldState.RSVR);
					
				}
			}
			ship.setPlaced();
			return true;
		}
	}

	/**
	 * 	Ransom elhelyez egy haj�t a t�bl�n.
	 * 	Csak a saj�t haj�szet�nkben ny�lk�ljunk!
	 * 
	 * @param ship
	 * @param plyrId
	 * @return
	 * 
	 */
	boolean randPlaceShip(Ship ship, int plyrId){
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

			while (wouldFit == false || newCoord == true) {
				if(plyrId == ground1.GetPlyrId()){
					coord.setX((int) (Math.random() * (ground1.getGroundTable().getSize().getInt() - 1)));
					coord.setY((int) (Math.random() * (ground1.getGroundTable().getSize().getInt() - 1)));
					if(ship.getOrientation() == 1){
						if(coord.getY()+ship.getSize() <= ground1.getGroundTable().getSize().getInt()){
							wouldFit = true;
						}else{
							wouldFit = false;
						}
					}else{
						if(coord.getX()+ship.getSize() <= ground1.getGroundTable().getSize().getInt()){
							wouldFit = true;
						}else{
							wouldFit = false;
						}
					}
				}else{
					coord.setX((int) (Math.random() * (ground2.getGroundTable().getSize().getInt() - 1)));
					coord.setY((int) (Math.random() * (ground2.getGroundTable().getSize().getInt() - 1)));
					if(ship.getOrientation() == 1){
						if(coord.getY()+ship.getSize() <= ground2.getGroundTable().getSize().getInt()){
							wouldFit = true;
						}else{
							wouldFit = false;
						}
					}else{
						if(coord.getX()+ship.getSize() <= ground2.getGroundTable().getSize().getInt()){
							wouldFit = true;
						}else{
							wouldFit = false;
						}
					}

				}
				newCoord = false;
			}
			int x = coord.getX();
			int y = coord.getY();
			if(plyrId == ground1.GetPlyrId()){
			if (ship.getOrientation() == 1) {
				// csak a haj�n kell v�gigmenni, ha nem free mez�t tal�lunk
				// akkor g�z van
				for (int i = ship.getSize() - 1; i >= 0; i--) {
					if (ground1.getGroundTable().getValue(new Coordinate(x, y + i)) != FieldState.FREE) {
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
						ground1.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
						ground1.getGroundTable().setValue(new Coordinate(x + i, y + ship.getSize()),
								FieldState.RSVR);
					}
					// Ez a for ciklus a haj�t helyezi el, valamint a k�t
					// oldal�n l�v� foglalt mez�ket
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						ground1.getGroundTable().setValue(new Coordinate(x, y + i), ship.getId());
						ground1.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
						ground1.getGroundTable().setValue(new Coordinate(x + 1, y + i), FieldState.RSVR);
					}
				}
			} else {
				for (int i = ship.getSize() - 1; i >= 0; i--) {
					if (ground1.getGroundTable().getValue(new Coordinate(x + i, y)) != FieldState.FREE) {
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
						ground1.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
						ground1.getGroundTable().setValue(new Coordinate(x + ship.getSize(), y + i),
								FieldState.RSVR);
					}
					// Ez a for ciklus a haj�t helyezi el, valamint a k�t
					// oldal�n l�v� foglalt mez�ket
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						ground1.getGroundTable().setValue(new Coordinate(x + i, y), ship.getId());
						ground1.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
						ground1.getGroundTable().setValue(new Coordinate(x + i, y + 1), FieldState.RSVR);
					}
				}
			}
			}else{
				if (ship.getOrientation() == 1) {
					// csak a haj�n kell v�gigmenni, ha nem free mez�t tal�lunk
					// akkor g�z van
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						if (ground2.getGroundTable().getValue(new Coordinate(x, y + i)) != FieldState.FREE) {
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
							ground2.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
							ground2.getGroundTable().setValue(new Coordinate(x + i, y + ship.getSize()),
									FieldState.RSVR);
						}
						// Ez a for ciklus a haj�t helyezi el, valamint a k�t
						// oldal�n l�v� foglalt mez�ket
						for (int i = ship.getSize() - 1; i >= 0; i--) {
							ground2.getGroundTable().setValue(new Coordinate(x, y + i), ship.getId());
							ground2.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
							ground2.getGroundTable().setValue(new Coordinate(x + 1, y + i), FieldState.RSVR);
						}
					}
				} else {
					for (int i = ship.getSize() - 1; i >= 0; i--) {
						if (ground2.getGroundTable().getValue(new Coordinate(x + i, y)) != FieldState.FREE) {
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
							ground2.getGroundTable().setValue(new Coordinate(x - 1, y + i), FieldState.RSVR);
							ground2.getGroundTable().setValue(new Coordinate(x + ship.getSize(), y + i),
									FieldState.RSVR);
						}
						// Ez a for ciklus a haj�t helyezi el, valamint a k�t
						// oldal�n l�v� foglalt mez�ket
						for (int i = ship.getSize() - 1; i >= 0; i--) {
							ground2.getGroundTable().setValue(new Coordinate(x + i, y), ship.getId());
							ground2.getGroundTable().setValue(new Coordinate(x + i, y - 1), FieldState.RSVR);
							ground2.getGroundTable().setValue(new Coordinate(x + i, y + 1), FieldState.RSVR);
						}
					}
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
	}
	
	/**
	 * Amelyik ground-nak az id-j�t kapja arra random lerakja a haj�kat
	 * 
	 * @param plyrId
	 * @return
	 * 
	 */
	public boolean randPlaceShips(int plyrId){
		if(ground1.GetPlyrId() == plyrId){
			for(Ship ship : ground1.retSetOfShips()){
				if(this.randPlaceShip(ship, 1) == false){
					return false;
				}
			}
			return true;
		}else{
			for(Ship ship : ground2.retSetOfShips()){
				if(this.randPlaceShip(ship, 2) == false){
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Egy haj� t�rl�s�t v�gzi.
	 * Csak a saj�t haj�szet�nkben ny�lk�ljunk!
	 * 
	 * @param ship
	 * @param plyrId
	 * 
	 */
	void clearShip(Ship ship, int plyrId){
		int x = ship.getCoordinates().getX();
		int y = ship.getCoordinates().getY();
		//A haj�t kit�r�lj�k, de a k�r�l�tte l�v� RSVR mez�k maradnak, azokat k�l�n
		//vizsg�ljuk
		if(plyrId == ground1.GetPlyrId()){
		if(ship.getOrientation()==1){
			for(int i = y; i < y + ship.getSize(); i++){
				ground1.getGroundTable().setValue(new Coordinate(x,i), FieldState.FREE);
			}
		}else{
			for(int i = x; i < x + ship.getSize(); i++){
				ground1.getGroundTable().setValue(new Coordinate(i, y), FieldState.FREE);
			}
		}
		ship.unsetPlaced();
		int besideShip = 0;
		//Itt vizsg�ljuk az RSVR mez�ket
		if(ship.getOrientation() == 1){
			//Kiv�lasztjuk hogy melyik mez� szomsz�djait vizsg�ljuk
			for(int i = x-1; i <= x+1; i++){
				for(int j = y-1; j < y + ship.getSize()+2; j++){
					//Ha ez a mez� nem RSVR akkor nem is foglalkozunk vele
					if(ground1.getGroundTable().getValue(new Coordinate(i, j)) == FieldState.RSVR){
						//V�gign�zz�k a kiv�laszotott mez� szomsz�djait
						for(int k = i-1; k <= i+1; k++){
							for(int l = j-1; l <= j+1; l++){
								//Ha valamit tal�lunk rajtuk, azt megjegyezz�k
								if(ground1.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.FREE &&
										ground1.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.RSVR &&
												ground1.getGroundTable().getValue(new Coordinate(k, l)) != null){
									besideShip++;
								}
							}
						}
						//Ha semmi nem volt a k�rny�ken, t�r�lhet� a mez�, egy�bk�nt nem
						if(besideShip == 0){
							ground1.getGroundTable().setValue(new Coordinate(i, j), FieldState.FREE);
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
					if(ground1.getGroundTable().getValue(new Coordinate(j, i)) == FieldState.RSVR){
						//V�gign�zz�k a kiv�laszotott mez� szomsz�djait
						for(int k = i-1; k <= i+1; k++){
							for(int l = j-1; l <= j+1; l++){
								//Ha valamit tal�lunk rajtuk, azt megjegyezz�k
								if(ground1.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.FREE &&
										ground1.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.RSVR &&
												ground1.getGroundTable().getValue(new Coordinate(k, l)) != null){
									besideShip++;
								}
							}
						}
						//Ha semmi nem volt a k�rny�ken, t�r�lhet� a mez�, egy�bk�nt nem
						if(besideShip == 0){
							ground1.getGroundTable().setValue(new Coordinate(i, j), FieldState.FREE);
						}
						besideShip = 0;
					}
				}
			}
		}
		}else{
			if(ship.getOrientation()==1){
				for(int i = y; i < y + ship.getSize(); i++){
					ground2.getGroundTable().setValue(new Coordinate(x,i), FieldState.FREE);
				}
			}else{
				for(int i = x; i < x + ship.getSize(); i++){
					ground2.getGroundTable().setValue(new Coordinate(i, y), FieldState.FREE);
				}
			}
			ship.unsetPlaced();
			int besideShip = 0;
			//Itt vizsg�ljuk az RSVR mez�ket
			if(ship.getOrientation() == 1){
				//Kiv�lasztjuk hogy melyik mez� szomsz�djait vizsg�ljuk
				for(int i = x-1; i <= x+1; i++){
					for(int j = y-1; j < y + ship.getSize()+2; j++){
						//Ha ez a mez� nem RSVR akkor nem is foglalkozunk vele
						if(ground2.getGroundTable().getValue(new Coordinate(i, j)) == FieldState.RSVR){
							//V�gign�zz�k a kiv�laszotott mez� szomsz�djait
							for(int k = i-1; k <= i+1; k++){
								for(int l = j-1; l <= j+1; l++){
									//Ha valamit tal�lunk rajtuk, azt megjegyezz�k
									if(ground2.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.FREE &&
											ground2.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.RSVR &&
													ground2.getGroundTable().getValue(new Coordinate(k, l)) != null){
										besideShip++;
									}
								}
							}
							//Ha semmi nem volt a k�rny�ken, t�r�lhet� a mez�, egy�bk�nt nem
							if(besideShip == 0){
								ground2.getGroundTable().setValue(new Coordinate(i, j), FieldState.FREE);
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
						if(ground2.getGroundTable().getValue(new Coordinate(j, i)) == FieldState.RSVR){
							//V�gign�zz�k a kiv�laszotott mez� szomsz�djait
							for(int k = i-1; k <= i+1; k++){
								for(int l = j-1; l <= j+1; l++){
									//Ha valamit tal�lunk rajtuk, azt megjegyezz�k
									if(ground2.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.FREE &&
											ground2.getGroundTable().getValue(new Coordinate(k, l)) != FieldState.RSVR &&
													ground2.getGroundTable().getValue(new Coordinate(k, l)) != null){
										besideShip++;
									}
								}
							}
							//Ha semmi nem volt a k�rny�ken, t�r�lhet� a mez�, egy�bk�nt nem
							if(besideShip == 0){
								ground2.getGroundTable().setValue(new Coordinate(i, j), FieldState.FREE);
							}
							besideShip = 0;
						}
					}
				}
			}

		}
	}
	
	/**
	 * T�blakirajzol�s konzolra
	 * 
	 * @param plyrId
	 * 
	 */
	void printTable(int plyrId) {
		if(plyrId == ground1.GetPlyrId()){
			for (int i = 0; i < ground1.getGroundTable().getSize().getInt(); i++) {
				for (int j = 0; j < ground1.getGroundTable().getSize().getInt(); j++) {
					System.out.print(ground1.getGroundTable().getValue(new Coordinate(j, i)) + " ");
				}
				System.out.print("\n");
			}
		}else{
			for (int i = 0; i < ground2.getGroundTable().getSize().getInt(); i++) {
				for (int j = 0; j < ground2.getGroundTable().getSize().getInt(); j++) {
					System.out.print(ground2.getGroundTable().getValue(new Coordinate(j, i)) + " ");
				}
				System.out.print("\n");
			}
		}
	}
	
	/**
	 * T�rli a teljes t�bl�t
	 * 
	 * @param plyrId
	 */
	void clearTable(int plyrId){
		if(plyrId == ground1.GetPlyrId()){
			for (int i = ground1.getGroundTable().getSize().getInt() - 1; i >= 0; i--) {
				for (int j = ground1.getGroundTable().getSize().getInt() - 1; j >= 0; j--) {
					ground1.getGroundTable().setValue(new Coordinate(i, j), FieldState.FREE);
				}
			}
		}else{
			for (int i = ground2.getGroundTable().getSize().getInt() - 1; i >= 0; i--) {
				for (int j = ground2.getGroundTable().getSize().getInt() - 1; j >= 0; j--) {
					ground2.getGroundTable().setValue(new Coordinate(i, j), FieldState.FREE);
				}
			}

		}
	}
	
	/**
	 * Akire l�v�nk annak az ID-j�t kell megadni!
	 * 
	 * @param coord
	 * @param plyrId
	 * @return
	 */
	public boolean shoot(Coordinate coord, int plyrId){
		if(plyrId == ground1.GetPlyrId()){
			switch(ground1.getGroundTable().getValue(coord)){
				case FOUR : try{
					if(ground1.selectShip(FieldState.FOUR).getOnGame() == true){
						ground1.selectShip(FieldState.FOUR).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.FOUR).getHealth() == 0){
							ground1.selectShip(FieldState.FOUR).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case THR1 : try{
					if(ground1.selectShip(FieldState.THR1).getOnGame() == true){
						ground1.selectShip(FieldState.THR1).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.THR1).getHealth() == 0){
							ground1.selectShip(FieldState.THR1).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case THR2 : try{
					if(ground1.selectShip(FieldState.THR2).getOnGame() == true){
						ground1.selectShip(FieldState.THR2).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.THR2).getHealth() == 0){
							ground1.selectShip(FieldState.THR2).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case TWO1 :  try{
					if(ground1.selectShip(FieldState.TWO1).getOnGame() == true){
						ground1.selectShip(FieldState.TWO1).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.TWO1).getHealth() == 0){
							ground1.selectShip(FieldState.TWO1).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case TWO2 : try{
					if(ground1.selectShip(FieldState.TWO2).getOnGame() == true){
						ground1.selectShip(FieldState.TWO2).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.TWO2).getHealth() == 0){
							ground1.selectShip(FieldState.TWO2).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case TWO3 : try{
					if(ground1.selectShip(FieldState.TWO3).getOnGame() == true){
						ground1.selectShip(FieldState.TWO3).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.TWO3).getHealth() == 0){
							ground1.selectShip(FieldState.TWO3).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case ONE1 : try{
					if(ground1.selectShip(FieldState.ONE1).getOnGame() == true){
						ground1.selectShip(FieldState.ONE1).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.ONE1).getHealth() == 0){
							ground1.selectShip(FieldState.ONE1).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case ONE2 : try{
					if(ground1.selectShip(FieldState.ONE2).getOnGame() == true){
						ground1.selectShip(FieldState.ONE2).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.ONE2).getHealth() == 0){
							ground1.selectShip(FieldState.ONE2).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case ONE3 : try{
					if(ground1.selectShip(FieldState.ONE3).getOnGame() == true){
						ground1.selectShip(FieldState.ONE3).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.ONE3).getHealth() == 0){
							ground1.selectShip(FieldState.ONE3).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case ONE4 : try{
					if(ground1.selectShip(FieldState.ONE4).getOnGame() == true){
						ground1.selectShip(FieldState.ONE4).lowerHealth();
						ground1.getGroundTable().setValue(coord, FieldState.SCORE);
						if(ground1.selectShip(FieldState.ONE4).getHealth() == 0){
							ground1.selectShip(FieldState.ONE4).changeOnGame();
						}
						return true;
					}else{
						return false;
					}
				} catch(Exception e){
					System.out.print(e);
				}
				case _OUT  : return false;
				case SCORE : return false;
				default   : ground1.getGroundTable().setValue(coord, FieldState._OUT);
							return false;
			}
		}else{
			switch(ground2.getGroundTable().getValue(coord)){
			case FOUR : try{
				if(ground2.selectShip(FieldState.FOUR).getOnGame() == true){
					ground2.selectShip(FieldState.FOUR).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.FOUR).getHealth() == 0){
						ground2.selectShip(FieldState.FOUR).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case THR1 : try{
				if(ground2.selectShip(FieldState.THR1).getOnGame() == true){
					ground2.selectShip(FieldState.THR1).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.THR1).getHealth() == 0){
						ground2.selectShip(FieldState.THR1).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case THR2 : try{
				if(ground2.selectShip(FieldState.THR2).getOnGame() == true){
					ground2.selectShip(FieldState.THR2).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.THR2).getHealth() == 0){
						ground2.selectShip(FieldState.THR2).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case TWO1 :  try{
				if(ground2.selectShip(FieldState.TWO1).getOnGame() == true){
					ground2.selectShip(FieldState.TWO1).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.TWO1).getHealth() == 0){
						ground2.selectShip(FieldState.TWO1).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case TWO2 : try{
				if(ground2.selectShip(FieldState.TWO2).getOnGame() == true){
					ground2.selectShip(FieldState.TWO2).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.TWO2).getHealth() == 0){
						ground2.selectShip(FieldState.TWO2).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case TWO3 : try{
				if(ground2.selectShip(FieldState.TWO3).getOnGame() == true){
					ground2.selectShip(FieldState.TWO3).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.TWO3).getHealth() == 0){
						ground2.selectShip(FieldState.TWO3).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case ONE1 : try{
				if(ground2.selectShip(FieldState.ONE1).getOnGame() == true){
					ground2.selectShip(FieldState.ONE1).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.ONE1).getHealth() == 0){
						ground2.selectShip(FieldState.ONE1).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case ONE2 : try{
				if(ground2.selectShip(FieldState.ONE2).getOnGame() == true){
					ground2.selectShip(FieldState.ONE2).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.ONE2).getHealth() == 0){
						ground2.selectShip(FieldState.ONE2).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case ONE3 : try{
				if(ground2.selectShip(FieldState.ONE3).getOnGame() == true){
					ground2.selectShip(FieldState.ONE3).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.ONE3).getHealth() == 0){
						ground2.selectShip(FieldState.ONE3).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case ONE4 : try{
				if(ground2.selectShip(FieldState.ONE4).getOnGame() == true){
					ground2.selectShip(FieldState.ONE4).lowerHealth();
					ground2.getGroundTable().setValue(coord, FieldState.SCORE);
					if(ground2.selectShip(FieldState.ONE4).getHealth() == 0){
						ground2.selectShip(FieldState.ONE4).changeOnGame();
					}
					return true;
				}else{
					return false;
				}
			} catch(Exception e){
				System.out.print(e);
			}
			case _OUT  : return false;
			case SCORE : return false;
			default   : ground2.getGroundTable().setValue(coord, FieldState._OUT);
						return false;
		}
		}
	}

	/**
	 * Random l� a f�ggv�ny. Ahhoz hogy intelligensebb legyen m�g kicsit basz�dni kell
	 * 
	 * @param plyrId
	 * @return
	 * 
	 */
	public boolean randShoot(int plyrId){
		Coordinate coord = new Coordinate(40, 40); // ilyen koordin�ta nincs
		double x;
		double y;
		
		if(plyrId == ground1.GetPlyrId()){
			while (ground1.getGroundTable().getValue(coord) == FieldState._OUT || ground1.getGroundTable().getValue(coord) == null){
				x = Math.random() * ground1.getGroundTable().getSize().getInt();
				y = Math.random() * ground1.getGroundTable().getSize().getInt();
				
				while (x == ground1.getGroundTable().getSize().getInt()){
					x = Math.random() * ground1.getGroundTable().getSize().getInt();
				}
				while (y == ground1.getGroundTable().getSize().getInt()){
					y = Math.random() * ground1.getGroundTable().getSize().getInt();
				}
				
				coord.setX( (int)(x) );
				coord.setY( (int)(y) );
				
				//coord.setX((int) (Math.random() * (ground1.getGroundTable().getSize().getInt() )));
				//coord.setY((int) (Math.random() * (ground1.getGroundTable().getSize().getInt() )));
			}
			System.out.println(coord.getX() + " " + coord.getY());
			return this.shoot(coord,1);
		}else{
			while (ground2.getGroundTable().getValue(coord) == FieldState._OUT || ground1.getGroundTable().getValue(coord) == null){
				x = Math.random() * ground2.getGroundTable().getSize().getInt();
				y = Math.random() * ground2.getGroundTable().getSize().getInt();
				
				while (x == ground1.getGroundTable().getSize().getInt()){
					x = Math.random() * ground2.getGroundTable().getSize().getInt();
				}
				while (y == ground1.getGroundTable().getSize().getInt()){
					y = Math.random() * ground2.getGroundTable().getSize().getInt();
				}
				
				coord.setX( (int)(x) );
				coord.setY( (int)(y) );
				
				//coord.setX((int) (Math.random() * (ground2.getGroundTable().getSize().getInt() )));
				//coord.setY((int) (Math.random() * (ground2.getGroundTable().getSize().getInt() )));
			}
			System.out.println(coord.getX() + " " + coord.getY());
			return this.shoot(coord,1);
		}
	}
	
	boolean isGameOver(){
		int numOfKilled1 = 0;
		int numOfKilled2 = 0;
		for(Ship actShip : ground1.retSetOfShips()){
			if(actShip.getOnGame()==false){
				numOfKilled1++;
			}
		}
		for(Ship actShip : ground2.retSetOfShips()){
			if(actShip.getOnGame()==false){
				numOfKilled2++;
			}
		}
		if(numOfKilled1 >= 10){
			return true;
		}else if(numOfKilled2 >= 10){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Adat stream-re rakjuk
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeOutputStream(DataOutputStream out, int plyrId) throws IOException{
		// A t�bla �llapot�t k�ldj�k el
		GameGround ground;
		if(plyrId == ground1.GetPlyrId())
			ground = ground1;
		else
			ground = ground2;
		for(int i=0; i < ground.getGroundTable().getSize().getInt(); i++){
			for(int j=0; j < ground.getGroundTable().getSize().getInt(); j++){
				//out.write(i);
				//out.write(j);				
				out.writeInt(ground.getGroundTable().getValue(new Coordinate(i,j)).ordinal());	
			}
		}
		// A haj�k �llapot�t k�ld�k el
		for(Ship actShip : ground.retSetOfShips()){
			out.writeInt(actShip.getId().ordinal());
			out.writeInt(actShip.getHealth());
			out.writeBoolean(actShip.getOnGame());
		}
	}
	
	/**
	 * Adat stream-r�l olvasunk
	 * 
	 * @param in
	 * @throws IOException
	 * @throws Exception
	 */
	public Coordinate readInputStream(int plyrId, ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Boolean> list4) throws IOException, Exception{
		Coordinate score = new Coordinate(0,0);
		FieldState state = FieldState.FREE;
		GameGround ground;
		if(plyrId == ground1.GetPlyrId()){
		ground = ground1;
		}else{
		ground = ground2;
		}
		
		int l = 0;
		
		for(int i=0; i < ground.getGroundTable().getSize().getInt(); i++)
		{
			for(int j=0; j < ground.getGroundTable().getSize().getInt(); j++)
			{				
				state = FieldState.intToFieldState(list1.get(l));
				l++;
				
				if(ground.getGroundTable().getValue(new Coordinate(i,j))!=state){
					score.setX(i);
					score.setY(j);
				}
				ground.getGroundTable().setValue(new Coordinate(i, j), state);
			}	
		}
		FieldState shipId;
		for(int i=0; i<10; i++){
			shipId = FieldState.intToFieldState(list2.get(i));
			ground.selectShip(shipId).setHealth(list3.get(i));
			ground.selectShip(shipId).setOnGmame(list4.get(i));
		}
		return score;
		}
	
	/*
	void readInputStream(DataInputStream in, int plyrId) throws IOException, Exception{
		int x=0, y=0;
		FieldState state = FieldState.FREE;
		GameGround ground;
		if(plyrId == ground1.GetPlyrId())
			ground = ground1;
		else
			ground = ground2;
		for(int i=0; i < ground.getGroundTable().getSize().getInt(); i++){
			for(int j=0; j < ground.getGroundTable().getSize().getInt(); j++){
				x = in.readInt();
				y = in.readInt();
				state = FieldState.intToFieldState(in.readInt());
				ground.getGroundTable().setValue(new Coordinate(x, y), state);
			}
		}
		FieldState shipId;
		for(int i=0; i<11; i++){
			shipId = FieldState.intToFieldState(in.readInt());
			ground.selectShip(shipId).setHealth(in.readInt());
			ground.selectShip(shipId).setOnGmame(in.readBoolean());
		}
	}*/
}
