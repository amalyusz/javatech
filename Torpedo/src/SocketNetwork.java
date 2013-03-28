import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class SocketNetwork {
	
	ServerSocket s_server;
	DataInputStream s_input;
    DataOutputStream s_output;
    Socket s_client;
    
    Socket c_client;
	DataOutputStream c_output;
	DataInputStream c_input;
	
	Windows _window;
	
	public SocketNetwork(Windows window)
	{
		_window = window;
	}
	
	
	public void sendBoardToEnemy()
	{
		//szerver vagyunk
		if(_window.cb_server.isSelected())
		{
			try {
				_window.game.writeOutputStream(s_output, 2);
			} catch (IOException e) {
				_window.t_uzenet.setText("I/O hiba!");
			}
		}
		else //kliens vagyunk
		{
			try {
				_window.game.writeOutputStream(c_output, 2);
			} catch (IOException e) {
				_window.t_uzenet.setText("I/O hiba!");
			}
		}
		
	}
	
	public void initKliensGround()
	{
		try {
			_window.game.writeOutputStream(c_output, 1);			
		} catch (IOException e) {
			_window.t_uzenet.setText("I/O hiba!");
		}
	}
	
	public void connect(String network, String size, String port, String ip)
	{
		if(network.equals("server"))
		{
			try
			{				
				// Port megnyitása a kliens számára
				s_server = new ServerSocket(Integer.parseInt(port));
				s_client = s_server.accept();
				
				System.out.println("accept után");
				
				
				s_input = new DataInputStream(s_client.getInputStream());
		        s_output = new DataOutputStream(s_client.getOutputStream());
		        
		        // Létrehozzuk a szálat, ami a hálózaton érkezõ üzeneteket fogadja és kezeli
				Network th_network = new Network(s_input,"szerver", this._window, size);
				// Elindítjuk a szálat
				th_network.start();							
			}
			catch(IOException e)
	        {
	        	_window.t_uzenet.setText("Nem sikerült létrehozni a klienssel a kapcsolatot!");
	        	
	        	_window.state = 0;
	        }	
		}
		else
		{
			try
			{
				c_client = new Socket(ip, Integer.parseInt(port));
				c_output = new DataOutputStream(c_client.getOutputStream());
	            c_input = new DataInputStream(c_client.getInputStream());
	            
	            // Létrehozzuk a szálat, ami a hálózaton érkezõ üzeneteket fogadja és kezeli
	            Network th_network = new Network(c_input,"kliens", this._window, size);
	            // Elindítjuk a szálat
				th_network.start();
			}
			catch(UnknownHostException e)
			{
				System.out.println("Don't know about host "+e.toString());
				_window.t_uzenet.setText("Nincs ilyen host!");
				
				_window.state = 0;
				_window.cb_server.setEnabled(true);
				_window.cb_kliens.setEnabled(true);
				_window.cb_single.setEnabled(true);
				_window.cb_small.setEnabled(true);
				_window.cb_medium.setEnabled(true);
				_window.cb_large.setEnabled(true);
				_window.t_ip.setEditable(true);
				_window.t_port.setEditable(true);
			}
			catch(IOException e)
			{
				System.out.println("Couldn't get I/O for the connection to: " + e.toString());
				_window.t_uzenet.setText("Nincs elérhetõ I/O kapcsolat!");
				
				_window.state = 0;
				_window.cb_server.setEnabled(true);
				_window.cb_kliens.setEnabled(true);
				_window.cb_single.setEnabled(true);
				_window.cb_small.setEnabled(true);
				_window.cb_medium.setEnabled(true);
				_window.cb_large.setEnabled(true);
				_window.t_ip.setEditable(true);
				_window.t_port.setEditable(true);
			}
		}
		
		_window.connect(network, size);
	}
	
	public void initClient(String size)
	{
		try
		{
			if(size.equals("small"))
			{
				s_output.writeBytes("small\n");
			}					
			else if(size.equals("medium"))
			{
				s_output.writeBytes("medium\n");
			}					
			else
			{
				s_output.writeBytes("large\n");
			}				
		}
		catch(IOException e)
        {
			_window.t_uzenet.setText("Nem sikerült létrehozni a klienssel a kapcsolatot!");
        	
        	_window.state = 0;
        }
		
	}
	
	public void close()
	{
		 try {
			 if(_window.cb_server.isSelected())
			 {
				 s_server.close();
				 s_client.close();
				 s_output.close();
				 s_input.close();
			 }
			 else
			 {
				 c_client.close();
				 c_output.close();
				 c_input.close(); 
			 }	  
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
}
