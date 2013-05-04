package Model;

/**
 * Egyes mez�k �llapot�nak jelz�se.
 * 
 * A FieldState egyben a haj�k azonos�t�s�ra szolg�l� kulcs is (Ship.id)!
 * 
 * @author amalyusz
 * 
 */
public enum FieldState {
	FREE, // az adott mez�re lehet haj�t helyezni
	RSVR, // az adott mez�n nincs haj�, de nem lehet haj�t elhelyezni
	_OUT, // az adott mez�re m�r l�ttek, �gy nem lehet oda l�ni
	SCORE, // az adott mez�n van haj� �s eltal�lt�k

	// Most k�vetkeznek a haj� kulcsok

	FOUR, THR1, THR2, TWO1, TWO2, TWO3, ONE1, ONE2, ONE3, ONE4;

	static FieldState intToFieldState(int i) {
		switch (i) {
		case 0:
			return FREE;
		case 1:
			return RSVR;
		case 2:
			return _OUT;
		case 3:
			return SCORE;
		case 4:
			return FOUR;
		case 5:
			return THR1;
		case 6:
			return THR2;
		case 7:
			return TWO1;
		case 8:
			return TWO2;
		case 9:
			return TWO3;
		case 10:
			return ONE1;
		case 11:
			return ONE2;
		case 12:
			return ONE3;
		case 13:
			return ONE4;
		default:
			return FREE;
		}
	}
}
