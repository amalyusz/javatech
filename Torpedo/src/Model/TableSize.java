package Model;
public enum TableSize {
	LARGE	(20), // A legangyobb t�bla oldalm�rete
	MEDIUM	(15), // A k�zepes nagys�g� t�vla oldalm�rete
	SMALL	(10); // A legkisebb t�bla oldalm�rete

	private final int size;

	TableSize(int size) {
		this.size = size;
	}
	
	//Az�rt kell, ha az int �rt�k ker�lne felhaszn�l�sra pl. a t�bla l�trehoz�s�n�l!
	int getInt(){
		return size;
	}
}
