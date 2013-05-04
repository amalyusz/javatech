package Model;

import javax.naming.directory.InvalidAttributeValueException;

public class ShipCoordinates {
	
	private static ShipCoordinates INSTANCE;

	private final int fieldNumbers;
	private final int fieldSize;
	private final int[] height;
	private final int[] userWidth;
	private final int[] enemyWidth;
	
	private ShipCoordinates(String tableSize) throws InvalidAttributeValueException {
		if (tableSize.equals("small")) {
			fieldNumbers = 10;
			fieldSize = 30;
			height = new int[] {40,75,110,145,180,215,250,285,320,355,390};
			userWidth = new int[] {60,95,130,165,200,235,270,305,340,375};
			enemyWidth = new int[] {560,595,630,665,700,735,770,805,840,875};
		} else if (tableSize.equals("medium")) {
			fieldNumbers = 15;
			fieldSize = 20;
			height = new int[] {40,65,90,115,140,165,190,215,240,265,290,315,340,365,390};
			userWidth = new int[] {50,75,100,125,150,175,200,225,250,275,300,325,350,375,400};
			enemyWidth = new int[] {560,585,610,635,660,685,710,735,760,785,810,835,860,885,910};
		} else if (tableSize.equals("large")) {
			fieldNumbers = 20;
			fieldSize = 15;
			height = new int[] {40,60,80,100,120,140,160,180,200,220,240,260,280,300,320,340,360,380,400,420};
			userWidth = new int[] {50,70,90,110,130,150,170,190,210,230,250,270,290,310,330,350,370,390,410,430};
			enemyWidth = new int[] {560,580,600,620,640,660,680,700,720,740,760,780,800,820,840,860,880,900,920,940};
		} else {
			throw new InvalidAttributeValueException("Not valid table size");
		}
	}
	
	public static ShipCoordinates createInstance(String tableSize) throws InvalidAttributeValueException {
		if (INSTANCE == null) {
			INSTANCE = new ShipCoordinates(tableSize);
		}
		
		return INSTANCE;
	}

	public int getFieldNumbers() {
		return fieldNumbers;
	}

	public int getFieldSize() {
		return fieldSize;
	}

	public int[] getHeight() {
		return height;
	}

	public int[] getUserWidth() {
		return userWidth;
	}

	public int[] getEnemyWidth() {
		return enemyWidth;
	}
}
