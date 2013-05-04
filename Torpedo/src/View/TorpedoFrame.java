package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.naming.directory.InvalidAttributeValueException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.SocketNetwork;
import Model.Coordinate;
import Model.Game;
import Model.ShipCoordinates;
import Model.TableSize;

/**
 * Torpedo felhaszn�l�i fel�let megjelen�t�se �s esem�nyek kezel�se.
 * 
 * @author amalyusz
 */
@SuppressWarnings("serial")
public class TorpedoFrame extends Frame {

	private boolean isPlayerTurn;
	private int state = 0;
	private TextField t_uzenet;
	private JButton b_network;
	private JButton b_player;
	private TextField t_port;
	private TextField t_ip;

	private static Game game;

	private TorpedoGameTableCanvas gameTableCanvas;

	private int own_score = 0;
	private int enemy_score = 0;

	private JCheckBox cb_server;
	private JCheckBox cb_kliens;
	private JCheckBox cb_single;
	private JCheckBox cb_small;
	private JCheckBox cb_medium;
	private JCheckBox cb_large;

	private TextField t_tippx;
	private TextField t_tippy;
	private JButton b_tipp;
	private JButton b_start;

	private String GameGroundSize = "";

	private SocketNetwork socketnetwork;

	public TorpedoFrame() {
		setTitle("Torpedo j�t�k");
		setSize(1024, 600);
		setLayout(new BorderLayout());
		
		isPlayerTurn = true;

		JLabel l_sc = new JLabel("Szerver/kliens: ");
		cb_server = new JCheckBox("Szerver");
		cb_kliens = new JCheckBox("Kliens");
		cb_single = new JCheckBox("Egyj�t�kos");

		ButtonGroup group1 = new ButtonGroup();
		group1.add(cb_server);
		group1.add(cb_kliens);
		group1.add(cb_single);

		// Port be�r�s�hoz
		JLabel l_port = new JLabel("Portsz�m:");
		t_port = new TextField("", 5);

		// Port be�r�s�hoz
		JLabel l_ip = new JLabel("IP c�m:");
		t_ip = new TextField("localhost", 15);

		// P�lyam�ret be�ll�t�s�hoz
		JLabel l_table = new JLabel("P�lyam�ret:");
		cb_small = new JCheckBox("Kicsi");
		cb_medium = new JCheckBox("K�zepes");
		cb_large = new JCheckBox("Nagy");
		ButtonGroup group2 = new ButtonGroup();
		group2.add(cb_small);
		group2.add(cb_medium);
		group2.add(cb_large);

		// J�t�k elind�t�s�hoz
		b_start = new JButton("Start");
		// Panelek l�trehoz�sa
		JPanel control = new JPanel(new FlowLayout());

		control.add(l_sc);
		control.add(cb_server);
		control.add(cb_kliens);
		control.add(cb_single);

		// Ir�ny�t� panelhez hozz�adjuk a komponenseket
		control.add(l_port);
		control.add(t_port);
		control.add(l_ip);
		control.add(t_ip);
		control.add(l_table);
		control.add(cb_small);
		control.add(cb_medium);
		control.add(cb_large);
		control.add(b_start);
		// Panelt hozz�adjuk az ablakhoz
		add(control, BorderLayout.NORTH);

		// Tippel�shez, rendszer�zenetekhez sz�ks�ges komponensek
		JLabel l_tipp = new JLabel("Tippelt mez� (y/x):");
		t_tippx = new TextField("", 1);
		t_tippy = new TextField("", 1);
		b_tipp = new JButton("Tipp");
		b_tipp.setEnabled(false);
		JLabel l_ures = new JLabel("    ");
		JLabel l_rendszer = new JLabel("Rendszer�zenet:");
		t_uzenet = new TextField(
				"�ll�tsa be az adatokat, majd kattintson a Start gombra!", 50);
		t_uzenet.setEditable(false);
		b_network = new JButton("Nincs kapcsolat!");
		b_network.setEnabled(false);
		b_network.setVisible(true);
		b_network.setBackground(Color.red);
		b_player = new JButton("Nincs j�t�k");
		b_player.setEnabled(false);
		b_player.setBackground(Color.gray);

		// Komponenske t�rol�s�hoz
		JPanel gamePanel = new JPanel();
		gamePanel.add(b_network);
		gamePanel.add(l_rendszer);
		gamePanel.add(t_uzenet);
		gamePanel.add(l_ures);
		gamePanel.add(l_tipp);
		gamePanel.add(t_tippx);
		gamePanel.add(t_tippy);
		gamePanel.add(b_tipp);
		gamePanel.add(b_player);

		// Panelt hozz�adjuk az ablakhoz
		add(gamePanel, BorderLayout.SOUTH);

		socketnetwork = new SocketNetwork(this);
		
		// Ablak bez�r�sa
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent r) {
				// Ha m�r van kapcsolat, bez�rjuk azokat is
				if (state == 2) {
					if (!cb_single.isSelected())
						socketnetwork.close();
				}
				// Alkalmaz�s bez�r�sa
				r.getWindow().dispose();
			}
		});

		cb_kliens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				if (cb_kliens.isSelected()) {
					cb_small.setEnabled(false);
					cb_medium.setEnabled(false);
					cb_large.setEnabled(false);
					t_port.setEditable(true);
					t_ip.setEditable(true);
				}

			}
		});

		cb_server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				if (cb_server.isSelected()) {
					cb_small.setEnabled(true);
					cb_medium.setEnabled(true);
					cb_large.setEnabled(true);
					t_port.setEditable(true);
					t_ip.setEditable(false);
				}

			}
		});

		cb_single.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				if (cb_single.isSelected()) {
					cb_small.setEnabled(true);
					cb_medium.setEnabled(true);
					cb_large.setEnabled(true);
					t_port.setEditable(false);
					t_ip.setEditable(false);
				}

			}
		});

		// Start gomb esem�nykezel� f�ggv�nye
		b_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				// Ha m�g nincs kapcsolat l�trehozva
				if (state == 0) {
					// Beviteli mez�k elt�rol�s�hoz
					String network = "";
					String size = "";
					String port = "";
					String ip = "";

					// Ha a szerver vagy kliens ki van v�lasztva
					if (cb_server.isSelected() | cb_kliens.isSelected()
							| cb_single.isSelected()) {
						// Elt�roljuk, hogy melyik van kiv�lasztva
						if (cb_server.isSelected())
							network = "server";
						else if (cb_kliens.isSelected())
							network = "kliens";
						else
							network = "single";

						if (network.equals("kliens")) {
							port = t_port.getText();
							ip = t_ip.getText();
							// Ha �rt be portot, ipt, akkor rendben
							if (!port.equals("") & !ip.equals("")) {
								// �t�ll�tjuk az �llapotot
								state = 1;
								// komponensek �ll�that�s�g�nak tilt�sa
								cb_server.setEnabled(false);
								cb_kliens.setEnabled(false);
								cb_single.setEnabled(false);
								cb_small.setEnabled(false);
								cb_medium.setEnabled(false);
								cb_large.setEnabled(false);
								t_ip.setEditable(false);
								t_port.setEditable(false);

								t_uzenet.setText("Csatlakoz�s!");
								socketnetwork.connect(network, size, port, ip);
							}
						} // kliens v�ge
						else {
							if (cb_small.isSelected() | cb_medium.isSelected()
									| cb_large.isSelected()) {
								// Elt�roljuk, hogy melyik m�ret van
								if (cb_small.isSelected())
									size = "small";
								else if (cb_medium.isSelected())
									size = "medium";
								else
									size = "large";

								// Ha a felhaszn�l� a szervert v�lasztotta
								if (network.equals("server")) {
									port = t_port.getText();
									// Ha �rt be portot akkor rendben
									if (!port.equals("")) {
										// �t�ll�tjuk az �llapotot
										state = 1;
										// komponensek �ll�that�s�g�nak tilt�sa
										cb_server.setEnabled(false);
										cb_kliens.setEnabled(false);
										cb_single.setEnabled(false);
										cb_small.setEnabled(false);
										cb_medium.setEnabled(false);
										cb_large.setEnabled(false);
										t_ip.setEditable(false);
										t_port.setEditable(false);

										t_uzenet.setText("V�rakoz�s a kliens csatlakoz�s�ra!");
										socketnetwork.connect(network, size,
												port, ip);
									}
									// M�sk�l�nben ezt jelezz�k, hogy tegye meg
									else
										t_uzenet.setText("�ll�tson be portsz�mot!");
								}
								// Egyj�t�kos m�d
								else {
									// komponensek �ll�that�s�g�nak tilt�sa
									cb_server.setEnabled(false);
									cb_kliens.setEnabled(false);
									cb_single.setEnabled(false);
									cb_small.setEnabled(false);
									cb_medium.setEnabled(false);
									cb_large.setEnabled(false);
									t_ip.setEditable(false);
									t_port.setEditable(false);

									// J�t�kt�r fel�p�t�se
									if (size.equals("small"))
										game = new Game(TableSize.SMALL);
									else if (size.equals("medium"))
										game = new Game(TableSize.MEDIUM);
									else
										game = new Game(TableSize.LARGE);

									GameGroundSize = size;

									// Haj�k random elhelyez�se
									game.randPlaceShips(1);
									game.randPlaceShips(2);

									// Haj�k kirajzol�sa
									try {
										gameTableCanvas = new TorpedoGameTableCanvas(game, size);
									} catch (InvalidAttributeValueException e1) {
										e1.printStackTrace();
									}
									add(gameTableCanvas);

									setVisible(false);
									setVisible(true);

									gameTableCanvas.invalidate();
									gameTableCanvas.repaint();
									
									gameTableCanvas.addMouseListener(new MouseAdapter() {
										@Override
										public void mousePressed(MouseEvent e) {
											if (isPlayerTurn && state == 2) {
												isPlayerTurn = false;
												int x = e.getX();
												int y = e.getY();
												
												int[] fireCoordinate = new int[2];
												try {
													fireCoordinate = getFireCoordinate(x, y);
												} catch (InvalidAttributeValueException e1) {
													e1.printStackTrace();
												}
												
												if (fireCoordinate[0] != -1 && fireCoordinate[1] != -1) {
													b_player.setText("Ellenf�l l�p�se!");
													b_player.setBackground(Color.red);
													resultOnEnemyBoard(fireCoordinate[0], fireCoordinate[1]);
												} else {
													isPlayerTurn = true;
												}
											}
										}

										private int[] getFireCoordinate(int x, int y) throws InvalidAttributeValueException {
											int[] result = new int[] {-1, -1};
											ShipCoordinates shipCoordinates = ShipCoordinates.createInstance(GameGroundSize);
											
											for (int i = 0; i < shipCoordinates.getFieldNumbers(); i++) {
												for (int j = 0; j < shipCoordinates.getFieldNumbers(); j++) {
													if (shipCoordinates.getEnemyWidth()[i] < x && shipCoordinates.getEnemyWidth()[i] + shipCoordinates.getFieldSize() > x) {
														if (shipCoordinates.getHeight()[j] < y && shipCoordinates.getHeight()[j] + shipCoordinates.getFieldSize() > y) {
															result[0] = j;
															result[1] = i;
														}
													}
													
												}
											}
											
											return result;
										}
									});

									b_start.setEnabled(false);
									b_tipp.setEnabled(true);
									b_player.setText("J�t�k!");
									b_player.setBackground(Color.green);
									// Be�ll�tjuk az �llapotot
									state = 2;
								}
							}
							// Ha nincs p�lyam�ret be�ll�tva
							else
								t_uzenet.setText("�ll�tson be p�lyam�retet!");
						} // ha nem kliens v�ge
					} // if(cb_server.isSelected() | cb_kliens.isSelected() |
						// cb_single.isSelected())

					// Ha nics szerve/kliens kiv�lasztva
					else
						t_uzenet.setText("�ll�tsa be, hogy szerver vagy kliens lesz!");
				}
			}
		});

		// Tipp gomb esem�nykezel�se, tipp elk�ld�se
		b_tipp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {

				if (state == 2) {

					// Beolvassuk a tipp mez�k �rt�k�t
					String tippx = t_tippx.getText();
					String tippy = t_tippy.getText();

					// �talak�tjuk �ket
					int i_tippx = Integer.parseInt(tippx);
					int i_tippy = Integer.parseInt(tippy);

					boolean wrong = false;
					// Megn�zz�k, hogy a k�t �rt�k nem nagyobb-e, mint amekkore
					// a p�lya alapj�n lehet
					if (GameGroundSize.equals("small")) {
						if (i_tippx > 9 | i_tippy > 9)
							wrong = true;
					} else if (GameGroundSize.equals("medium")) {
						if (i_tippx > 14 | i_tippy > 14)
							wrong = true;
					} else // GameGroundSize.equals("large")
					{
						if (i_tippx > 19 | i_tippy > 19)
							wrong = true;
					}
					// Ha nincs megadva valamely mez� �rt�ke
					if (tippx.equals("") | tippy.equals(""))
						t_uzenet.setText("Mindk�t mez� �rt�k�t adja meg!");
					// Ha nagyobb valamelyik mez� �rt�ke a megengedetn�l
					else if (wrong) {
						t_uzenet.setText("Valamelyik mez� �rt�ke t�l nagy!!");
					}
					// Minden rendben
					else {
						b_tipp.setEnabled(false);
						b_player.setText("Ellenf�l l�p�se!");
						b_player.setBackground(Color.red);
						resultOnEnemyBoard(i_tippx, i_tippy);

					} // else Minden rendben
				} else
					t_uzenet.setText("Nincs j�t�k elkezdve!");

				// �jrarajzoltatjuk a p�ly�t
				gameTableCanvas.invalidate();
				gameTableCanvas.repaint();
			}
		});
	}

	/**
	 * Eredm�ny kisz�m�t�sa a j�t�kos t�bl�j�n.
	 * 
	 * @param tipp az ellenf�l tippje
	 */
	public void resultOnMyBoard(Coordinate tipp) {

		int tipp_x = tipp.getX();
		int tipp_y = tipp.getY();

		String s_tipp = game.getGround(1).getGroundTable().getValue(tipp)
				.toString();

		if (s_tipp.toLowerCase().equals("score")) {
			if (cb_server.isSelected()) {
				t_uzenet.setText("Kliens tippje: " + tipp_x + " " + tipp_y
						+ ". Eredm�nye: Tal�lat! �rja be a tippj�t!");
			} else
				t_uzenet.setText("Szerver tippje: " + tipp_x + " " + tipp_y
						+ ". Eredm�nye: Tal�lat! �rja be a tippj�t!");

			enemy_score++;

			if (enemy_score == 20) {
				t_uzenet.setText("J�t�k v�ge! �n vesztett!");

				socketnetwork.close();
			}

		} else {
			if (cb_server.isSelected()) {
				t_uzenet.setText("Kliens tippje: " + tipp_x + " " + tipp_y
						+ ". Eredm�nye: Nem tal�lt! �rja be a tippj�t!");
			} else
				t_uzenet.setText("Szerver tippje: " + tipp_x + " " + tipp_y
						+ ". Eredm�nye: Nem tal�lt! �rja be a tippj�t!");
		}

		b_tipp.setEnabled(true);
		b_player.setText("J�t�kos l�p�se!");
		b_player.setBackground(Color.green);
		gameTableCanvas.invalidate();
		gameTableCanvas.repaint();

	}

	/**
	 * Eredm�ny kisz�m�t�sa az ellenf�l t�bl�j�n.
	 * 
	 * @param i_tippx 
	 * @param i_tippy
	 */
	public void resultOnEnemyBoard(int i_tippx, int i_tippy) {
		if (cb_server.isSelected() || cb_kliens.isSelected()) {
			// tipp alapj�n l�v�nk az ellenf�l t�bl�j�ba
			boolean res_me = game.shoot(new Coordinate(i_tippx, i_tippy), 2);

			// vagy
			// FieldState fs_tipp =
			// game.getGround(2).getGroundTable().getValue(new
			// Coordinate(i_tippx,i_tippy));

			if (res_me) {
				if (cb_server.isSelected())
					t_uzenet.setText("Tippj�nek eredm�nye: Tal�lat! V�rakoz�s a kliens tippj�re.");
				else
					t_uzenet.setText("Tippj�nek eredm�nye: Tal�lat! V�rakoz�s a szerver tippj�re.");

				own_score++;
			} else {
				if (cb_server.isSelected())
					t_uzenet.setText("Tippj�nek eredm�nye: Nincs tal�lat! V�rakoz�s a kliens tippj�re.");
				else
					t_uzenet.setText("Tippj�nek eredm�nye: Nincs tal�lat! V�rakoz�s a szerver tippj�re.");

			}

			socketnetwork.sendBoardToEnemy();

			if (own_score == 20) {
				t_uzenet.setText("J�t�k v�ge! �n nyert!");

				socketnetwork.close();
			}

			gameTableCanvas.invalidate();
			gameTableCanvas.repaint();

		}

		// egyj�t�kos m�d
		else // cb_single.isSelected()
		{
			// Tipp alapj�n l�v�nk a g�p t�bl�j�ba

			// !!!
			// Shoot fv-ek visszat�r�si �rt�ke haszn�lhat� legyen! kell, hogy mi
			// a tipp eredm�nye!!!

			boolean res_me = game.shoot(new Coordinate(i_tippx, i_tippy), 2);

			// G�p tippel�se
			boolean res_comp = game.randShoot(1);

			String s_res_me;
			String s_res_comp;
			// Saj�t eredm�ny�nk
			if (res_me) {
				s_res_me = "Tal�lt";
				own_score++;
			}

			else
				s_res_me = "Nem tal�lt";
			// G�p eredm�nye
			if (res_comp) {
				s_res_comp = "Tal�lt";
				enemy_score++;
			} else
				s_res_comp = "Nem tal�lt";

			// Be�ll�tjuk a saj�t illetve g�p tal�lat�t
			t_uzenet.setText("J�t�kos eredm�nye: " + s_res_me
					+ ". G�p eredm�nye: " + s_res_comp);

			if (own_score == 20) {
				t_uzenet.setText("J�t�kos nyert! �j j�t�khoz nyomja meg a start gombot!");
				b_tipp.setEnabled(false);
				b_start.setEnabled(true);
				b_player.setText("J�t�k v�ge!");
				b_player.setBackground(Color.gray);
				state = 0;
			} else if (enemy_score == 20) {
				t_uzenet.setText("G�p nyert! �j j�t�khoz nyomja meg a start gombot!");
				b_tipp.setEnabled(false);
				b_start.setEnabled(true);
				b_player.setText("J�t�k v�ge!");
				b_player.setBackground(Color.gray);
				state = 0;
			}

			b_tipp.setEnabled(true);
			b_player.setText("J�t�kos l�p�se!");
			b_player.setBackground(Color.green);
			isPlayerTurn = true;

			gameTableCanvas.invalidate();
			gameTableCanvas.repaint();

		}
	}

	/**
	 * A h�l�zati kapcs�l�d�st kezel� f�ggv�ny.
	 * 
	 * @param network
	 * @param size
	 * @return
	 */
	public boolean connect(String network, String size) {
		// Ha a szervert v�lasztottuk ki
		if (network.equals("server")) {

			// socketnetwork.connect(network, size, port, ip);

			try {

				// Port megnyit�sa a kliens sz�m�ra
				// s_server = new ServerSocket(Integer.parseInt(port));
				// Rendszeruzenet be�ll�t�sa
				// t_uzenet.setText("V�rakoz�s a kliens csatlakoz�s�ra!");
				// Kliens kapcsol�d�s�nak elfogad�sa
				// s_client = s_server.accept();
				// �llapot gomb �t�ll�t�sa
				b_network.setText("Kapcsol�dva!");
				b_start.setEnabled(false);
				b_network.setBackground(Color.green);
				// J�t�kos gomb be�ll�t�sa
				b_player.setText("Ellenf�l l�p�se!");
				b_player.setBackground(Color.red);
				// Bemenet/kimenet be�ll�t�sa
				// s_input = new DataInputStream(s_client.getInputStream());
				// s_output = new DataOutputStream(s_client.getOutputStream());
				// Rendszer�zenet be�ll�t�sa
				t_uzenet.setText("A kliens csatlakozott! V�rakoz�s a kliens l�p�s�re!");

				// L�trehozzuk a sz�lat, ami a h�l�zaton �rkez� �zeneteket
				// fogadja �s kezeli
				// Network th_network = new Network(s_input,"szerver", this);
				// Elind�tjuk a sz�lat
				// th_network.start();

				// J�t�kt�r fel�p�t�se, kliens inform�l�sa a p�lyam�retr�l
				if (size.equals("small")) {
					// ground = new GameGround(TableSize.SMALL);
					game = new Game(TableSize.SMALL);
					// s_output.println("small");
					socketnetwork.initClient(size);
					// s_output.writeBytes("small\n");
				} else if (size.equals("medium")) {
					// ground = new GameGround(TableSize.MEDIUM);
					game = new Game(TableSize.MEDIUM);
					// s_output.println("medium");
					socketnetwork.initClient(size);
					// s_output.writeBytes("medium\n");
				} else {
					// ground = new GameGround(TableSize.LARGE);
					game = new Game(TableSize.LARGE);
					// s_output.println("large");
					socketnetwork.initClient(size);
					// s_output.writeBytes("large\n");
				}

				GameGroundSize = size;

				// Haj�k random elhelyez�se
				// ground.randPlaceShips();
				game.randPlaceShips(1);

				game.writeOutputStream(socketnetwork.getS_output(), 1);
				// socketnetwork.s_output.writeBytes("\n");

				// Haj�k kirajzol�sa
				try {
					gameTableCanvas = new TorpedoGameTableCanvas(game, size);
				} catch (InvalidAttributeValueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				add(gameTableCanvas);

				setVisible(false);
				setVisible(true);

				gameTableCanvas.invalidate();
				gameTableCanvas.repaint();
				// SZerver eset�n kikapcsoljuk a tipp gombot
				b_tipp.setEnabled(false);
				// Be�ll�tjuk az �llapotot
				state = 2;
				
				isPlayerTurn = false;
			} catch (IOException e) {
				System.out.println(e.toString());
				t_uzenet.setText("Nem siker�lt l�trehozni a klienssel a kapcsolatot!");

				state = 0;
			}

		}
		// Ha a klienst v�lasztottuk ki
		else {
			// Rendszer�zenet be�ll�t�sa
			// t_uzenet.setText("Kapcsol�d�s a szerverhez!");
			// Kapcsol�d�s a szerverhez
			// c_client = new Socket(ip, Integer.parseInt(port));
			// Be�ll�tjuk, hogy a j�t�kos j�n
			b_player.setText("J�t�kos l�p�se!");
			b_player.setBackground(Color.green);

			b_start.setEnabled(false);

			// Rendszer�zenet be�ll�t�sa
			t_uzenet.setText("A kapcsolat l�trej�tt! �rja be a l�p�st!");
			// c_output = new DataOutputStream(c_client.getOutputStream());
			// c_input = new DataInputStream(c_client.getInputStream());
			// �llapot gomb �t�ll�t�sa
			b_network.setText("Kapcsol�dva!");
			b_network.setBackground(Color.green);
			// J�t�kos gomb be�ll�t�sa
			b_player.setText("J�t�kos l�p�se!");
			b_player.setBackground(Color.green);
			// L�trehozzuk a sz�lat, ami a h�l�zaton �rkez� �zeneteket fogadja
			// �s kezeli
			// Network th_network = new Network(c_input,"kliens", this);
			// Elind�tjuk a sz�lat
			// th_network.start();
			isPlayerTurn = true;
		}

		return true;
	}

	/**
	 * P�lya inicializ�l�sa kliens oldalon, a szerver �ltal megadott p�lyam�retben.
	 * 
	 * @param size a p�lya m�rete
	 */
	public void initKliensGround(String size) {
		// J�t�kt�r fel�p�t�se
		if (size.equals("small"))
			game = new Game(TableSize.SMALL);
		else if (size.equals("medium"))
			game = new Game(TableSize.MEDIUM);
		else
			game = new Game(TableSize.LARGE);

		// Haj�k random elhelyez�se
		game.randPlaceShips(1);
		// Haj�k kirajzol�sa
		try {
			gameTableCanvas = new TorpedoGameTableCanvas(game, size);
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(gameTableCanvas);

		setVisible(false);
		setVisible(true);
		// Be�ll�tjuk az �llapotot
		state = 2;
		b_tipp.setEnabled(true);

		GameGroundSize = size;

		// elk�sz�lt a t�bl�nk, �tk�ldj�k a szervernek
		socketnetwork.initKliensGround();

	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		TorpedoFrame.game = game;
	}

	public TextField getT_uzenet() {
		return t_uzenet;
	}

	public void setT_uzenet(TextField t_uzenet) {
		this.t_uzenet = t_uzenet;
	}

	public JCheckBox getCb_server() {
		return cb_server;
	}

	public void setCb_server(JCheckBox cb_server) {
		this.cb_server = cb_server;
	}

	public JCheckBox getCb_kliens() {
		return cb_kliens;
	}

	public void setCb_kliens(JCheckBox cb_kliens) {
		this.cb_kliens = cb_kliens;
	}

	public JCheckBox getCb_single() {
		return cb_single;
	}

	public void setCb_single(JCheckBox cb_single) {
		this.cb_single = cb_single;
	}

	public JCheckBox getCb_small() {
		return cb_small;
	}

	public void setCb_small(JCheckBox cb_small) {
		this.cb_small = cb_small;
	}

	public JCheckBox getCb_medium() {
		return cb_medium;
	}

	public void setCb_medium(JCheckBox cb_medium) {
		this.cb_medium = cb_medium;
	}

	public JCheckBox getCb_large() {
		return cb_large;
	}

	public void setCb_large(JCheckBox cb_large) {
		this.cb_large = cb_large;
	}

	public TextField getT_ip() {
		return t_ip;
	}

	public void setT_ip(TextField t_ip) {
		this.t_ip = t_ip;
	}

	public TextField getT_port() {
		return t_port;
	}

	public void setT_port(TextField t_port) {
		this.t_port = t_port;
	}
}
