import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* Hálózaton érkezõ üzentek lekezlését végzõ szál */
public class Network extends Thread{
	// Ahonnan az üzenetet be kell olvasni
	DataInputStream _input = null;
	// Szerver vagy kliens vagyunk
	String _type;
	Windows _window;
	int _size;
	
	Coordinate tipp;
	// Konstruktor
	Network(DataInputStream input, String type, Windows window, String size)
	{
		_input = input;
		_type = type;
		_window = window;
		tipp = new Coordinate(0,0);
		if(size.equals("small"))
			_size = 10;
		else if(size.equals("medium"))
			_size = 15;
		else
			_size = 20;
			
	}
	// Szál futása
	public void run()
	{
		
		// Ha szerver vagyunk
		if(_type.equals("szerver"))
		{
			// megkapjuk a kliens tábláját									
			try {
				
				ArrayList<Integer> list1 = new ArrayList<Integer>();
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				ArrayList<Integer> list3 = new ArrayList<Integer>();
				ArrayList<Boolean> list4 = new ArrayList<Boolean>();
							
				for(int i = 0; i< _size; i++)
				{
					for(int j=0; j < _size; j++)
					{
						int state = _input.readInt();
						list1.add(state);
					}									
				}
				System.out.println("2");
				for(int i=0; i<10; i++)
				{
					int ordinal = _input.readInt();					
					int health = _input.readInt();					
					boolean getongame = _input.readBoolean();
					list2.add(ordinal);
					list3.add(health);
					list4.add(getongame);
					
				}
				
				Coordinate tmp = _window.game.readInputStream(2, list1, list2, list3, list4);							
						
			} catch (IOException e1) {				
				System.out.println("Hiba: "+e1.toString());
				_window.t_uzenet.setText("I/O hiba!");
			} catch (Exception e1) {
				_window.t_uzenet.setText("I/O hiba!");
			}
			
			// klienstõl visszakapott táblánk
			while(true)
			{				
				try {
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					ArrayList<Integer> list2 = new ArrayList<Integer>();
					ArrayList<Integer> list3 = new ArrayList<Integer>();
					ArrayList<Boolean> list4 = new ArrayList<Boolean>();
									
					for(int i = 0; i< _size; i++)
					{
						for(int j=0; j < _size; j++)
						{
							int state = _input.readInt();
							list1.add(state);
						}									
					}
					
					for(int i=0; i<10; i++)
					{
						int ordinal = _input.readInt();						
						int health = _input.readInt();						
						boolean getongame = _input.readBoolean();						
						list2.add(ordinal);
						list3.add(health);
						list4.add(getongame);
						
					}					
					
					tipp = _window.game.readInputStream(1, list1, list2, list3, list4);
				} catch (Exception e) {
					_window.t_uzenet.setText("I/O hiba!");
				}
				
				//eredménykezelõ fv hívása
				_window.resultOnMyBoard(tipp);
			}	
		}
		// Ha kliens vagyunk
		else
		{
			System.out.println("1");
			//megkapjuk a tábla méretét
			String size;
			try {
				size = _input.readLine();
				if(size.equals("small"))
					_size = 10;
				else if(size.equals("medium"))
					_size = 15;
				else
					_size = 20;
				_window.initKliensGround(size);
			} catch (IOException e1) {
				_window.t_uzenet.setText("I/O hiba!");
			}

			// megkapjuk a szerver tábláját
			try {
				
				ArrayList<Integer> list1 = new ArrayList<Integer>();
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				ArrayList<Integer> list3 = new ArrayList<Integer>();
				ArrayList<Boolean> list4 = new ArrayList<Boolean>();
				int k = 0;
				System.out.println("2.1");
				for(int i = 0; i< _size; i++)
				{
					for(int j=0; j < _size; j++)
					{
						int state = _input.readInt();						
						list1.add(state);
						System.out.println("state: "+state+"   k: "+k);
						k++;
					}									
				}
				
				System.out.println("2.2");
				for(int i=0; i<10; i++)
				{
					int ordinal = _input.readInt();						
					int health = _input.readInt();						
					boolean getongame = _input.readBoolean();						
					list2.add(ordinal);
					list3.add(health);
					list4.add(getongame);
					
				}
				
				Coordinate tmp = _window.game.readInputStream(2, list1, list2, list3, list4);
				
			} catch (IOException e1) {
				_window.t_uzenet.setText("I/O hiba!");
			} catch (Exception e1) {
				_window.t_uzenet.setText("I/O hiba!");
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
									
					for(int i = 0; i < _size; i++)
					{
						for(int j=0; j < _size; j++)
						{
							int state = _input.readInt();
							list1.add(state);
						}									
					}
					
					for(int i=0; i<10; i++)
					{
						int ordinal = _input.readInt();						
						int health = _input.readInt();						
						boolean getongame = _input.readBoolean();						
						list2.add(ordinal);
						list3.add(health);
						list4.add(getongame);
						
					}
					
					
					tipp = _window.game.readInputStream(1, list1, list2, list3, list4);					
				} 
				catch (IOException e) {
					e.printStackTrace();
					_window.t_uzenet.setText("I/O hiba!");
				} catch (Exception e) {
					e.printStackTrace();
				}			
				
				//eredménykezelõ fv hívása
				_window.resultOnMyBoard(tipp);
			}	
		}
				
	}

}
