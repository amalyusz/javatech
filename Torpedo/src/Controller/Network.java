package Controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import Model.Coordinate;
import View.TorpedoFrame;

/**
 * Hálózaton érkezõ üzentek lekezlését végzõ osztály
 * 
 * @author amalyusz
 */
public class Network extends Thread{
	// Ahonnan az üzenetet be kell olvasni
	DataInputStream input = null;
	// Szerver vagy kliens vagyunk
	String type;
	TorpedoFrame window;
	int size;
	
	Coordinate tipp;
	// Konstruktor
	Network(DataInputStream input, String type, TorpedoFrame window, String size)
	{
		this.input = input;
		this.type = type;
		this.window = window;
		this.tipp = new Coordinate(0,0);
		if(size.equals("small")) {
			this.size = 10;
		} else if(size.equals("medium")) {
			this.size = 15;
		} else {
			this.size = 20;
		}
			
	}
	// Szál futása
	public void run()
	{
		
		// Ha szerver vagyunk
		if(type.equals("szerver"))
		{
			// megkapjuk a kliens tábláját									
			try {
				
				ArrayList<Integer> list1 = new ArrayList<Integer>();
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				ArrayList<Integer> list3 = new ArrayList<Integer>();
				ArrayList<Boolean> list4 = new ArrayList<Boolean>();
							
				for(int i = 0; i< size; i++)
				{
					for(int j=0; j < size; j++)
					{
						int state = input.readInt();
						list1.add(state);
					}									
				}
				System.out.println("2");
				for(int i=0; i<10; i++)
				{
					int ordinal = input.readInt();					
					int health = input.readInt();					
					boolean getongame = input.readBoolean();
					list2.add(ordinal);
					list3.add(health);
					list4.add(getongame);
					
				}
			} catch (IOException e1) {				
				System.out.println("Hiba: "+e1.toString());
				window.getT_uzenet().setText("I/O hiba!");
			} catch (Exception e1) {
				window.getT_uzenet().setText("I/O hiba!");
			}
			
			// klienstõl visszakapott táblánk
			while(true)
			{				
				try {
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					ArrayList<Integer> list2 = new ArrayList<Integer>();
					ArrayList<Integer> list3 = new ArrayList<Integer>();
					ArrayList<Boolean> list4 = new ArrayList<Boolean>();
									
					for(int i = 0; i< size; i++)
					{
						for(int j=0; j < size; j++)
						{
							int state = input.readInt();
							list1.add(state);
						}									
					}
					
					for(int i=0; i<10; i++)
					{
						int ordinal = input.readInt();						
						int health = input.readInt();						
						boolean getongame = input.readBoolean();						
						list2.add(ordinal);
						list3.add(health);
						list4.add(getongame);
						
					}					
					
					tipp = window.getGame().readInputStream(1, list1, list2, list3, list4);
				} catch (Exception e) {
					window.getT_uzenet().setText("I/O hiba!");
				}
				
				//eredménykezelõ fv hívása
				window.resultOnMyBoard(tipp);
			}	
		}
		// Ha kliens vagyunk
		else
		{
			System.out.println("1");
			//megkapjuk a tábla méretét
			String tableSize;
			try {
//				tableSize = input.readLine();
				tableSize = input.readUTF();
				
				if(tableSize.equals("small")) {
					size = 10;
				} else if(tableSize.equals("medium")) {
					size = 15;
				} else {
					size = 20;
				}
				window.initKliensGround(tableSize);
			} catch (IOException e1) {
				window.getT_uzenet().setText("I/O hiba!");
			}

			// megkapjuk a szerver tábláját
			try {
				
				ArrayList<Integer> list1 = new ArrayList<Integer>();
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				ArrayList<Integer> list3 = new ArrayList<Integer>();
				ArrayList<Boolean> list4 = new ArrayList<Boolean>();
				int k = 0;
				System.out.println("2.1");
				for(int i = 0; i< size; i++)
				{
					for(int j=0; j < size; j++)
					{
						int state = input.readInt();						
						list1.add(state);
						System.out.println("state: "+state+"   k: "+k);
						k++;
					}									
				}
				
				System.out.println("2.2");
				for(int i=0; i<10; i++)
				{
					int ordinal = input.readInt();						
					int health = input.readInt();						
					boolean getongame = input.readBoolean();						
					list2.add(ordinal);
					list3.add(health);
					list4.add(getongame);
					
				}
				
			} catch (IOException e1) {
				window.getT_uzenet().setText("I/O hiba!");
			} catch (Exception e1) {
				window.getT_uzenet().setText("I/O hiba!");
			}
			
			// szervertõl visszakapott táblánk
			while(true)
			{System.out.println("Kliens");
				try 
				{
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					ArrayList<Integer> list2 = new ArrayList<Integer>();
					ArrayList<Integer> list3 = new ArrayList<Integer>();
					ArrayList<Boolean> list4 = new ArrayList<Boolean>();
									
					for(int i = 0; i < size; i++)
					{
						for(int j=0; j < size; j++)
						{
							int state = input.readInt();
							list1.add(state);
						}									
					}
					
					for(int i=0; i<10; i++)
					{
						int ordinal = input.readInt();						
						int health = input.readInt();						
						boolean getongame = input.readBoolean();						
						list2.add(ordinal);
						list3.add(health);
						list4.add(getongame);
						
					}
					
					
					tipp = window.getGame().readInputStream(1, list1, list2, list3, list4);					
				} 
				catch (IOException e) {
					e.printStackTrace();
					window.getT_uzenet().setText("I/O hiba!");
				} catch (Exception e) {
					e.printStackTrace();
				}			
				
				//eredménykezelõ fv hívása
				window.resultOnMyBoard(tipp);
			}	
		}
	}
}
