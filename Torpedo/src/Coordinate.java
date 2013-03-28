/*
 * Ez az osztály a trükk: elfedjük vele a kétdimenziós tömböt, hogy egyetlen
 * objektummal dolgozhassunk. Illetve saját equals és hashcode metódust írunk
 * bele, amivel elérjük, hogy a collectionben implementált bejárások, és hasonlító
 * függvények ne referencia szerint, hanem érték szerint hasonlítsák össze
 * ezeket a hashmapben kulcsként használt Coordinatákat.
 */

/**
 * 
 * @author medovarszki
 */

// A legels� koordin�ta 0,0!!!
public class Coordinate {
	private Integer x;
	private Integer y;

	public Coordinate(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate c = (Coordinate) obj;
		return (c.getX().equals(this.x) && c.getY().equals(this.y));
	}

	@Override
	public int hashCode() {
		int hash = (this.x * 10000) + this.y;
		return hash;
	}
}
