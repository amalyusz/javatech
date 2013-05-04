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
 * Torpedo felhasználói felület megjelenítése és események kezelése.
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
		setTitle("Torpedo játék");
		setSize(1024, 600);
		setLayout(new BorderLayout());
		
		isPlayerTurn = true;

		JLabel l_sc = new JLabel("Szerver/kliens: ");
		cb_server = new JCheckBox("Szerver");
		cb_kliens = new JCheckBox("Kliens");
		cb_single = new JCheckBox("Egyjátékos");

		ButtonGroup group1 = new ButtonGroup();
		group1.add(cb_server);
		group1.add(cb_kliens);
		group1.add(cb_single);

		// Port beírásához
		JLabel l_port = new JLabel("Portszám:");
		t_port = new TextField("", 5);

		// Port beírásához
		JLabel l_ip = new JLabel("IP cím:");
		t_ip = new TextField("localhost", 15);

		// Pályaméret beállításához
		JLabel l_table = new JLabel("Pályaméret:");
		cb_small = new JCheckBox("Kicsi");
		cb_medium = new JCheckBox("Közepes");
		cb_large = new JCheckBox("Nagy");
		ButtonGroup group2 = new ButtonGroup();
		group2.add(cb_small);
		group2.add(cb_medium);
		group2.add(cb_large);

		// Játék elindításához
		b_start = new JButton("Start");
		// Panelek létrehozása
		JPanel control = new JPanel(new FlowLayout());

		control.add(l_sc);
		control.add(cb_server);
		control.add(cb_kliens);
		control.add(cb_single);

		// Irányító panelhez hozzáadjuk a komponenseket
		control.add(l_port);
		control.add(t_port);
		control.add(l_ip);
		control.add(t_ip);
		control.add(l_table);
		control.add(cb_small);
		control.add(cb_medium);
		control.add(cb_large);
		control.add(b_start);
		// Panelt hozzáadjuk az ablakhoz
		add(control, BorderLayout.NORTH);

		// Tippeléshez, rendszerüzenetekhez szükséges komponensek
		JLabel l_tipp = new JLabel("Tippelt mezõ (y/x):");
		t_tippx = new TextField("", 1);
		t_tippy = new TextField("", 1);
		b_tipp = new JButton("Tipp");
		b_tipp.setEnabled(false);
		JLabel l_ures = new JLabel("    ");
		JLabel l_rendszer = new JLabel("Rendszerüzenet:");
		t_uzenet = new TextField(
				"Állítsa be az adatokat, majd kattintson a Start gombra!", 50);
		t_uzenet.setEditable(false);
		b_network = new JButton("Nincs kapcsolat!");
		b_network.setEnabled(false);
		b_network.setVisible(true);
		b_network.setBackground(Color.red);
		b_player = new JButton("Nincs játék");
		b_player.setEnabled(false);
		b_player.setBackground(Color.gray);

		// Komponenske tárolásához
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

		// Panelt hozzáadjuk az ablakhoz
		add(gamePanel, BorderLayout.SOUTH);

		socketnetwork = new SocketNetwork(this);
		
		// Ablak bezárása
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent r) {
				// Ha már van kapcsolat, bezárjuk azokat is
				if (state == 2) {
					if (!cb_single.isSelected())
						socketnetwork.close();
				}
				// Alkalmazás bezárása
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

		// Start gomb eseménykezelõ függvénye
		b_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				// Ha még nincs kapcsolat létrehozva
				if (state == 0) {
					// Beviteli mezõk eltárolásához
					String network = "";
					String size = "";
					String port = "";
					String ip = "";

					// Ha a szerver vagy kliens ki van választva
					if (cb_server.isSelected() | cb_kliens.isSelected()
							| cb_single.isSelected()) {
						// Eltároljuk, hogy melyik van kiválasztva
						if (cb_server.isSelected())
							network = "server";
						else if (cb_kliens.isSelected())
							network = "kliens";
						else
							network = "single";

						if (network.equals("kliens")) {
							port = t_port.getText();
							ip = t_ip.getText();
							// Ha írt be portot, ipt, akkor rendben
							if (!port.equals("") & !ip.equals("")) {
								// Átállítjuk az állapotot
								state = 1;
								// komponensek állíthatóságának tiltása
								cb_server.setEnabled(false);
								cb_kliens.setEnabled(false);
								cb_single.setEnabled(false);
								cb_small.setEnabled(false);
								cb_medium.setEnabled(false);
								cb_large.setEnabled(false);
								t_ip.setEditable(false);
								t_port.setEditable(false);

								t_uzenet.setText("Csatlakozás!");
								socketnetwork.connect(network, size, port, ip);
							}
						} // kliens vége
						else {
							if (cb_small.isSelected() | cb_medium.isSelected()
									| cb_large.isSelected()) {
								// Eltároljuk, hogy melyik méret van
								if (cb_small.isSelected())
									size = "small";
								else if (cb_medium.isSelected())
									size = "medium";
								else
									size = "large";

								// Ha a felhasználó a szervert választotta
								if (network.equals("server")) {
									port = t_port.getText();
									// Ha írt be portot akkor rendben
									if (!port.equals("")) {
										// Átállítjuk az állapotot
										state = 1;
										// komponensek állíthatóságának tiltása
										cb_server.setEnabled(false);
										cb_kliens.setEnabled(false);
										cb_single.setEnabled(false);
										cb_small.setEnabled(false);
										cb_medium.setEnabled(false);
										cb_large.setEnabled(false);
										t_ip.setEditable(false);
										t_port.setEditable(false);

										t_uzenet.setText("Várakozás a kliens csatlakozására!");
										socketnetwork.connect(network, size,
												port, ip);
									}
									// Máskülönben ezt jelezzük, hogy tegye meg
									else
										t_uzenet.setText("Állítson be portszámot!");
								}
								// Egyjátékos mód
								else {
									// komponensek állíthatóságának tiltása
									cb_server.setEnabled(false);
									cb_kliens.setEnabled(false);
									cb_single.setEnabled(false);
									cb_small.setEnabled(false);
									cb_medium.setEnabled(false);
									cb_large.setEnabled(false);
									t_ip.setEditable(false);
									t_port.setEditable(false);

									// Játéktér felépítése
									if (size.equals("small"))
										game = new Game(TableSize.SMALL);
									else if (size.equals("medium"))
										game = new Game(TableSize.MEDIUM);
									else
										game = new Game(TableSize.LARGE);

									GameGroundSize = size;

									// Hajók random elhelyezése
									game.randPlaceShips(1);
									game.randPlaceShips(2);

									// Hajók kirajzolása
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
													b_player.setText("Ellenfél lépése!");
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
									b_player.setText("Játék!");
									b_player.setBackground(Color.green);
									// Beállítjuk az állapotot
									state = 2;
								}
							}
							// Ha nincs pályaméret beállítva
							else
								t_uzenet.setText("Állítson be pályaméretet!");
						} // ha nem kliens vége
					} // if(cb_server.isSelected() | cb_kliens.isSelected() |
						// cb_single.isSelected())

					// Ha nics szerve/kliens kiválasztva
					else
						t_uzenet.setText("Állítsa be, hogy szerver vagy kliens lesz!");
				}
			}
		});

		// Tipp gomb eseménykezelése, tipp elküldése
		b_tipp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {

				if (state == 2) {

					// Beolvassuk a tipp mezõk értékét
					String tippx = t_tippx.getText();
					String tippy = t_tippy.getText();

					// Átalakítjuk õket
					int i_tippx = Integer.parseInt(tippx);
					int i_tippy = Integer.parseInt(tippy);

					boolean wrong = false;
					// Megnézzük, hogy a két érték nem nagyobb-e, mint amekkore
					// a pálya alapján lehet
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
					// Ha nincs megadva valamely mezõ értéke
					if (tippx.equals("") | tippy.equals(""))
						t_uzenet.setText("Mindkét mezõ értékét adja meg!");
					// Ha nagyobb valamelyik mezõ értéke a megengedetnél
					else if (wrong) {
						t_uzenet.setText("Valamelyik mezõ értéke túl nagy!!");
					}
					// Minden rendben
					else {
						b_tipp.setEnabled(false);
						b_player.setText("Ellenfél lépése!");
						b_player.setBackground(Color.red);
						resultOnEnemyBoard(i_tippx, i_tippy);

					} // else Minden rendben
				} else
					t_uzenet.setText("Nincs játék elkezdve!");

				// Újrarajzoltatjuk a pályát
				gameTableCanvas.invalidate();
				gameTableCanvas.repaint();
			}
		});
	}

	/**
	 * Eredmény kiszámítása a játékos tábláján.
	 * 
	 * @param tipp az ellenfél tippje
	 */
	public void resultOnMyBoard(Coordinate tipp) {

		int tipp_x = tipp.getX();
		int tipp_y = tipp.getY();

		String s_tipp = game.getGround(1).getGroundTable().getValue(tipp)
				.toString();

		if (s_tipp.toLowerCase().equals("score")) {
			if (cb_server.isSelected()) {
				t_uzenet.setText("Kliens tippje: " + tipp_x + " " + tipp_y
						+ ". Eredménye: Találat! Írja be a tippjét!");
			} else
				t_uzenet.setText("Szerver tippje: " + tipp_x + " " + tipp_y
						+ ". Eredménye: Találat! Írja be a tippjét!");

			enemy_score++;

			if (enemy_score == 20) {
				t_uzenet.setText("Játék vége! Ön vesztett!");

				socketnetwork.close();
			}

		} else {
			if (cb_server.isSelected()) {
				t_uzenet.setText("Kliens tippje: " + tipp_x + " " + tipp_y
						+ ". Eredménye: Nem talált! Írja be a tippjét!");
			} else
				t_uzenet.setText("Szerver tippje: " + tipp_x + " " + tipp_y
						+ ". Eredménye: Nem talált! Írja be a tippjét!");
		}

		b_tipp.setEnabled(true);
		b_player.setText("Játékos lépése!");
		b_player.setBackground(Color.green);
		gameTableCanvas.invalidate();
		gameTableCanvas.repaint();

	}

	/**
	 * Eredmény kiszámítása az ellenfél tábláján.
	 * 
	 * @param i_tippx 
	 * @param i_tippy
	 */
	public void resultOnEnemyBoard(int i_tippx, int i_tippy) {
		if (cb_server.isSelected() || cb_kliens.isSelected()) {
			// tipp alapján lövünk az ellenfél táblájába
			boolean res_me = game.shoot(new Coordinate(i_tippx, i_tippy), 2);

			// vagy
			// FieldState fs_tipp =
			// game.getGround(2).getGroundTable().getValue(new
			// Coordinate(i_tippx,i_tippy));

			if (res_me) {
				if (cb_server.isSelected())
					t_uzenet.setText("Tippjének eredménye: Találat! Várakozás a kliens tippjére.");
				else
					t_uzenet.setText("Tippjének eredménye: Találat! Várakozás a szerver tippjére.");

				own_score++;
			} else {
				if (cb_server.isSelected())
					t_uzenet.setText("Tippjének eredménye: Nincs találat! Várakozás a kliens tippjére.");
				else
					t_uzenet.setText("Tippjének eredménye: Nincs találat! Várakozás a szerver tippjére.");

			}

			socketnetwork.sendBoardToEnemy();

			if (own_score == 20) {
				t_uzenet.setText("Játék vége! Ön nyert!");

				socketnetwork.close();
			}

			gameTableCanvas.invalidate();
			gameTableCanvas.repaint();

		}

		// egyjátékos mód
		else // cb_single.isSelected()
		{
			// Tipp alapján lövünk a gép táblájába

			// !!!
			// Shoot fv-ek visszatérési értéke használható legyen! kell, hogy mi
			// a tipp eredménye!!!

			boolean res_me = game.shoot(new Coordinate(i_tippx, i_tippy), 2);

			// Gép tippelése
			boolean res_comp = game.randShoot(1);

			String s_res_me;
			String s_res_comp;
			// Saját eredményünk
			if (res_me) {
				s_res_me = "Talált";
				own_score++;
			}

			else
				s_res_me = "Nem talált";
			// Gép eredménye
			if (res_comp) {
				s_res_comp = "Talált";
				enemy_score++;
			} else
				s_res_comp = "Nem talált";

			// Beállítjuk a saját illetve gép találatát
			t_uzenet.setText("Játékos eredménye: " + s_res_me
					+ ". Gép eredménye: " + s_res_comp);

			if (own_score == 20) {
				t_uzenet.setText("Játékos nyert! Új játékhoz nyomja meg a start gombot!");
				b_tipp.setEnabled(false);
				b_start.setEnabled(true);
				b_player.setText("Játék vége!");
				b_player.setBackground(Color.gray);
				state = 0;
			} else if (enemy_score == 20) {
				t_uzenet.setText("Gép nyert! Új játékhoz nyomja meg a start gombot!");
				b_tipp.setEnabled(false);
				b_start.setEnabled(true);
				b_player.setText("Játék vége!");
				b_player.setBackground(Color.gray);
				state = 0;
			}

			b_tipp.setEnabled(true);
			b_player.setText("Játékos lépése!");
			b_player.setBackground(Color.green);
			isPlayerTurn = true;

			gameTableCanvas.invalidate();
			gameTableCanvas.repaint();

		}
	}

	/**
	 * A hálózati kapcsólódást kezelõ függvény.
	 * 
	 * @param network
	 * @param size
	 * @return
	 */
	public boolean connect(String network, String size) {
		// Ha a szervert választottuk ki
		if (network.equals("server")) {

			// socketnetwork.connect(network, size, port, ip);

			try {

				// Port megnyitása a kliens számára
				// s_server = new ServerSocket(Integer.parseInt(port));
				// Rendszeruzenet beállítása
				// t_uzenet.setText("Várakozás a kliens csatlakozására!");
				// Kliens kapcsolódásának elfogadása
				// s_client = s_server.accept();
				// Állapot gomb átállítása
				b_network.setText("Kapcsolódva!");
				b_start.setEnabled(false);
				b_network.setBackground(Color.green);
				// Játékos gomb beállítása
				b_player.setText("Ellenfél lépése!");
				b_player.setBackground(Color.red);
				// Bemenet/kimenet beállítása
				// s_input = new DataInputStream(s_client.getInputStream());
				// s_output = new DataOutputStream(s_client.getOutputStream());
				// Rendszerüzenet beállítása
				t_uzenet.setText("A kliens csatlakozott! Várakozás a kliens lépésére!");

				// Létrehozzuk a szálat, ami a hálózaton érkezõ üzeneteket
				// fogadja és kezeli
				// Network th_network = new Network(s_input,"szerver", this);
				// Elindítjuk a szálat
				// th_network.start();

				// Játéktér felépítése, kliens informálása a pályaméretrõl
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

				// Hajók random elhelyezése
				// ground.randPlaceShips();
				game.randPlaceShips(1);

				game.writeOutputStream(socketnetwork.getS_output(), 1);
				// socketnetwork.s_output.writeBytes("\n");

				// Hajók kirajzolása
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
				// SZerver esetén kikapcsoljuk a tipp gombot
				b_tipp.setEnabled(false);
				// Beállítjuk az állapotot
				state = 2;
				
				isPlayerTurn = false;
			} catch (IOException e) {
				System.out.println(e.toString());
				t_uzenet.setText("Nem sikerült létrehozni a klienssel a kapcsolatot!");

				state = 0;
			}

		}
		// Ha a klienst választottuk ki
		else {
			// Rendszerüzenet beállítása
			// t_uzenet.setText("Kapcsolódás a szerverhez!");
			// Kapcsolódás a szerverhez
			// c_client = new Socket(ip, Integer.parseInt(port));
			// Beállítjuk, hogy a játékos jön
			b_player.setText("Játékos lépése!");
			b_player.setBackground(Color.green);

			b_start.setEnabled(false);

			// Rendszerüzenet beállítása
			t_uzenet.setText("A kapcsolat létrejött! Írja be a lépést!");
			// c_output = new DataOutputStream(c_client.getOutputStream());
			// c_input = new DataInputStream(c_client.getInputStream());
			// Állapot gomb átállítása
			b_network.setText("Kapcsolódva!");
			b_network.setBackground(Color.green);
			// Játékos gomb beállítása
			b_player.setText("Játékos lépése!");
			b_player.setBackground(Color.green);
			// Létrehozzuk a szálat, ami a hálózaton érkezõ üzeneteket fogadja
			// és kezeli
			// Network th_network = new Network(c_input,"kliens", this);
			// Elindítjuk a szálat
			// th_network.start();
			isPlayerTurn = true;
		}

		return true;
	}

	/**
	 * Pálya inicializálása kliens oldalon, a szerver által megadott pályaméretben.
	 * 
	 * @param size a pálya mérete
	 */
	public void initKliensGround(String size) {
		// Játéktér felépítése
		if (size.equals("small"))
			game = new Game(TableSize.SMALL);
		else if (size.equals("medium"))
			game = new Game(TableSize.MEDIUM);
		else
			game = new Game(TableSize.LARGE);

		// Hajók random elhelyezése
		game.randPlaceShips(1);
		// Hajók kirajzolása
		try {
			gameTableCanvas = new TorpedoGameTableCanvas(game, size);
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(gameTableCanvas);

		setVisible(false);
		setVisible(true);
		// Beállítjuk az állapotot
		state = 2;
		b_tipp.setEnabled(true);

		GameGroundSize = size;

		// elkészült a táblánk, átküldjük a szervernek
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
