package Controller;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import View.Windows;


public class SocketNetwork {
	
	private ServerSocket s_server;
	private DataInputStream s_input;
    private DataOutputStream s_output;
    private Socket s_client;
    
    private Socket c_client;
	private DataOutputStream c_output;
	private DataInputStream c_input;
	
	private Windows window;
	
	public SocketNetwork(Windows window)
	{
		this.window = window;
	}
	
	
	public void sendBoardToEnemy()
	{
		//szerver vagyunk
		if(window.getCb_server().isSelected())
		{
			try {
				window.getGame().writeOutputStream(s_output, 2);
			} catch (IOException e) {
				window.getT_uzenet().setText("I/O hiba!");
			}
		}
		else //kliens vagyunk
		{
			try {
				window.getGame().writeOutputStream(c_output, 2);
			} catch (IOException e) {
				window.getT_uzenet().setText("I/O hiba!");
			}
		}
		
	}
	
	public void initKliensGround()
	{
		try {
			window.getGame().writeOutputStream(c_output, 1);			
		} catch (IOException e) {
			window.getT_uzenet().setText("I/O hiba!");
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
				Network th_network = new Network(s_input,"szerver", this.window, size);
				// Elindítjuk a szálat
				th_network.start();							
			}
			catch(IOException e)
	        {
	        	window.getT_uzenet().setText("Nem sikerült létrehozni a klienssel a kapcsolatot!");
	        	
	        	window.setState(0);
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
	            Network th_network = new Network(c_input,"kliens", this.window, size);
	            // Elindítjuk a szálat
				th_network.start();
			}
			catch(UnknownHostException e)
			{
				System.out.println("Don't know about host "+e.toString());
				window.getT_uzenet().setText("Nincs ilyen host!");
				
				window.setState(0);
				window.getCb_server().setEnabled(true);
				window.getCb_kliens().setEnabled(true);
				window.getCb_single().setEnabled(true);
				window.getCb_small().setEnabled(true);
				window.getCb_medium().setEnabled(true);
				window.getCb_large().setEnabled(true);
				window.getT_ip().setEditable(true);
				window.getT_port().setEditable(true);
			}
			catch(IOException e)
			{
				System.out.println("Couldn't get I/O for the connection to: " + e.toString());
				window.getT_uzenet().setText("Nincs elérhetõ I/O kapcsolat!");
				
				window.setState(0);
				window.getCb_server().setEnabled(true);
				window.getCb_kliens().setEnabled(true);
				window.getCb_single().setEnabled(true);
				window.getCb_small().setEnabled(true);
				window.getCb_medium().setEnabled(true);
				window.getCb_large().setEnabled(true);
				window.getT_ip().setEditable(true);
				window.getT_port().setEditable(true);
			}
		}
		
		window.connect(network, size);
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
			window.getT_uzenet().setText("Nem sikerült létrehozni a klienssel a kapcsolatot!");
        	
        	window.setState(0);
        }
		
	}
	
	public void close()
	{
		 try {
			 if(window.getCb_server().isSelected())
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
			e.printStackTrace();
		}
		 
	}


	public DataOutputStream getS_output() {
		return s_output;
	}


	public void setS_output(DataOutputStream s_output) {
		this.s_output = s_output;
	}
}
