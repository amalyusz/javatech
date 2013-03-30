package Model;
public enum TableSize {
	LARGE	(20), // A legangyobb tábla oldalmérete
	MEDIUM	(15), // A közepes nagyságú távla oldalmérete
	SMALL	(10); // A legkisebb tábla oldalmérete

	private final int size;

	TableSize(int size) {
		this.size = size;
	}
	
	//Azért kell, ha az int érték kerülne felhasználásra pl. a tábla létrehozásánál!
	int getInt(){
		return size;
	}
}
