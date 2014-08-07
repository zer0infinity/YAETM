package ch.hsr.i.yaetm.ui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.border.DropShadowBorder;

import ch.hsr.i.yaetm.logic.Ersatzteil;
import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.logic.Historyliste;
import ch.hsr.i.yaetm.logic.Reservation;
import ch.hsr.i.yaetm.logic.Reservationliste;
import ch.hsr.i.yaetm.models.ErsatzteileTableModel;
import ch.hsr.i.yaetm.models.HistoryTableModel;
import ch.hsr.i.yaetm.models.ReservationTableModel;
import ch.hsr.i.yaetm.ui.dialogs.AboutDialog;
import ch.hsr.i.yaetm.ui.dialogs.NeuDialog;
import ch.hsr.i.yaetm.ui.tables.ErsatzteileTable;
import ch.hsr.i.yaetm.ui.tables.HistoryTable;
import ch.hsr.i.yaetm.ui.tables.ReservationTable;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;
import ch.hsr.i.yaetm.ui.utils.JLabelErsatzteilObserver;
import ch.hsr.i.yaetm.ui.utils.StatusMessage;

public class YAETMMainView extends JFrame {

	private static final long serialVersionUID = 7058927965000540696L;

	public static final String PROGRAM = "YAETM", PROGRAMEXT = "Yet Another Ersatz-Teil Manager";
	public static final String WELCOME = "Willkomen zu YAETM - (C)2008 Marco Birchler, David Tran";
	public static final String UIIMAGELOCATION = "/ch/hsr/i/yaetm/ui/images/";
	public static final String ADMIN = "Administrator", USER = "Benutzer";
	public static String PRIVILEGE = USER;
	
	private static final String RESERV = "Reservieren", NEW = "Neu", REMOVE = "Löschen", FETCHED = "Auslösen",
						DECREASE = "Entnehmen", INCREASE = "Hinzufügen";

	private Reservationliste reservationListe;
	private Ersatzteilliste ersatzteilListe;
	private Historyliste historyListe;
	private ErsatzteileTableModel ersatzteileTableModel;
	private ReservationTableModel reservationTableModel;
	private HistoryTableModel historyTableModel;
	private ErsatzteileTable ersatzteileTable;
	private ReservationTable reservationTable;
	private HistoryTable historyTable;

	private JTextField suchfeld, anzahlFeld;
	private JXLabel anzahlLabel, anzahlDescription1, anzahlDescription2, statusLabel;
	private JTabbedPane tabPane;
	private JXPanel titlePanel, anzahlPanel, statusPanel;

	/*
	 * put buttons in a hashmap for easier handling and shortend code
	 */
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();


	public YAETMMainView() {
		historyListe = new Historyliste();
		ersatzteilListe = new Ersatzteilliste(historyListe);
		reservationListe = new Reservationliste(historyListe);
		
		ersatzteileTableModel = new ErsatzteileTableModel(ersatzteilListe);
		reservationTableModel = new ReservationTableModel(reservationListe);
		historyTableModel = new HistoryTableModel(historyListe);

		ersatzteileTable = new ErsatzteileTable(ersatzteileTableModel, ersatzteilListe, reservationListe);
		reservationTable = new ReservationTable(reservationTableModel, ersatzteilListe, reservationListe);
		historyTable = new HistoryTable(historyTableModel, historyListe);
		
		setJFrame();
		buildGUI();
	}

	/**
	 * create and set gui elements
	 */
	private void buildGUI() {
		addSearchComponents();
		addTabbedPane();
		addTableListener();
		addF5Listener();
		addButtons();
		addAnzahlComponents();
		addTitle();
		addStatusbar();
		
		// change size/location of components if window is resized
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				tabPane.setSize(getWidth() - 200, getHeight() - 180);
				titlePanel.setLocation(getWidth() - 190, 5);
				anzahlPanel.setLocation(getWidth()-175, 70);
				statusPanel.setBounds(-10, getHeight()-75, getWidth()+10, 25);
				buttons.get(NEW).setLocation(getWidth() - 230, getHeight() - 110);
				buttons.get(REMOVE).setLocation(getWidth() - 125, getHeight() - 110);
				buttons.get(RESERV).setLocation(10, getHeight() - 110);
				buttons.get(INCREASE).setLocation(getWidth() - 100 - buttons.get(INCREASE).getWidth()/2, 135);
				buttons.get(FETCHED).setLocation(120, getHeight() - 110);
				if (PRIVILEGE == ADMIN) {
					buttons.get(DECREASE).setLocation(getWidth() - 100 - buttons.get(DECREASE).getWidth()/2, 220);
					anzahlFeld.setLocation(getWidth() - 100 - anzahlFeld.getWidth()/2, 198);
					anzahlPanel.setSize(150, 230);
				} else {
					buttons.get(DECREASE).setLocation(getWidth() - 100 - buttons.get(DECREASE).getWidth()/2, 160);
					anzahlFeld.setLocation(getWidth() - 100 - anzahlFeld.getWidth()/2, 135);
					anzahlPanel.setSize(150, 170);
				}
				historyTable.getColumnModel().getColumn(3).setPreferredWidth(tabPane.getWidth()-375);
			}
		});
	}
	
	private void addStatusbar() {
		statusPanel = new JXPanel();
		statusPanel.setLayout(null);
		statusPanel.setBorder(new DropShadowBorder(new Color(0, 0, 0), 7, 0.5f, 12, true, false, false, false));
		
		statusLabel = GUIComponents.createLabel(WELCOME, new Point(20, 5));
		statusLabel.setSize(500, 20);
		
		new StatusMessage(statusLabel, ersatzteilListe, reservationListe, historyListe);
		
		statusPanel.add(statusLabel);
		add(statusPanel);
	}
	
	/**
	 * set programtitle on upperright
	 */
	private void addTitle() {
		titlePanel = new JXPanel();
		titlePanel.setLayout(null);
		titlePanel.setSize(180, 60);
		titlePanel.setToolTipText("www.i.hsr.ch");
		titlePanel.setBorder(new DropShadowBorder(new Color(0, 0, 0), 7, 0.5f, 12, true, false, true, false));
		
		JLabel title = GUIComponents.createLabel(PROGRAM, new Point(45, 12));
		title.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		JLabel titleExtend = GUIComponents.createLabel(PROGRAMEXT, new Point(10, 33));
		titleExtend.setSize(200, 20);
		titleExtend.setFont(new Font("Arial", Font.PLAIN, 11));
		
		titlePanel.add(titleExtend);
		titlePanel.add(title);
		add(titlePanel);
		setGuiEnabled();
	}

	/**
	 * listener, which get the selected row
	 * and set anzahl in anzahlfeld
	 */
	private void addTableListener() {
		ersatzteileTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedTableRow = ersatzteileTable.getSelectedRow();
				if (0 <= selectedTableRow) {
					Ersatzteil ersatzteil = getErsatzteilFromRow(selectedTableRow);
					anzahlFeld.setText(Integer.toString(ersatzteil.getAnzahl()));
					anzahlLabel.setText(ersatzteil.getName());
					ersatzteileTable.changeSelection(selectedTableRow, 1, false, false);
				}
				setGuiEnabled();
			}
		});
		reservationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedTableRow = reservationTable.getSelectedRow();
				if (0 <= selectedTableRow) {
					Reservation reservation = getReservationFromRow(selectedTableRow);
					anzahlFeld.setText(Integer.toString(reservation.getAnzahl()));
					anzahlLabel.setText(reservation.getName());
					reservationTable.changeSelection(selectedTableRow, 1, false, false);
				}
				setGuiEnabled();
			}
		});
	}
	
	/**
	 * get Ersatzteil Object from selected row 
	 * 
	 * @param selectedTableRow
	 * @return Ersatzteil
	 */
	private Ersatzteil getErsatzteilFromRow(int selectedTableRow) {
		int selectedModelRow = ersatzteileTable.convertRowIndexToModel(selectedTableRow);
		return ersatzteilListe.get(selectedModelRow);
	}
	
	/**
	 * get Reservation Object from selected row
	 * 
	 * @param selectedTableRow
	 * @return Reservation
	 */
	private Reservation getReservationFromRow(int selectedTableRow) {
		int selectedModelRow = reservationTable.convertRowIndexToModel(selectedTableRow);
		return reservationListe.get(selectedModelRow);
	}
	
	/**
	 * create buttons
	 */
	private void addButtons() {
		final String[] buttonLabel = { RESERV, NEW, REMOVE, INCREASE, DECREASE, FETCHED };
		final int[] keyEvent = { KeyEvent.VK_R, KeyEvent.VK_N, KeyEvent.VK_L, KeyEvent.VK_H, KeyEvent.VK_E, KeyEvent.VK_A };
		final String[] tooltips = { "Ersatzteil reservieren", "Ersatzteil erfassen", "Ersatzteil löschen",
				"Anzahl um 1 erhöhen", "Anzahl um 1 reduzieren", "Reservation auslösen" };
		for (int i = 0; i < buttonLabel.length; i++) {
			JButton button = GUIComponents.createButton(buttonLabel[i],	keyEvent[i], tooltips[i]);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == RESERV) {
						ersatzteileTable.reservation();
					} else if (e.getActionCommand() == NEW) {
						NeuDialog neuDialog = new NeuDialog(ersatzteilListe);
						neuDialog.setVisible(true);
					} else if (e.getActionCommand() == DECREASE) {
						decreaseAnzahl();
					} else if (e.getActionCommand() == INCREASE) {
						increaseAnzahl();
					} else if (e.getActionCommand() == REMOVE) {
						if (tabPane.getSelectedIndex() == 0) {
							ersatzteileTable.remove();
						} else if (tabPane.getSelectedIndex() == 1) {
							reservationTable.remove();
						}
					} else if(e.getActionCommand() == FETCHED) {
						reservationTable.fetched();
					}
				}
			});
			buttons.put(buttonLabel[i], button);
		}
		// disable fetched button at start
		buttons.get(FETCHED).setEnabled(false);

		// Set Icon for DECREASE Button
		buttons.get(DECREASE).setSize(105, 60);
		buttons.get(DECREASE).setIcon(new ImageIcon(getClass().getResource(UIIMAGELOCATION + "icon_decrease.png")));
		buttons.get(DECREASE).setVerticalTextPosition(SwingConstants.BOTTOM);
		buttons.get(DECREASE).setHorizontalTextPosition(SwingConstants.CENTER);

		// Set Icon for INCREASE Button
		buttons.get(INCREASE).setSize(105, 60);
		buttons.get(INCREASE).setIcon(new ImageIcon(getClass().getResource(UIIMAGELOCATION + "icon_increase.png")));
		buttons.get(INCREASE).setVerticalTextPosition(SwingConstants.BOTTOM);
		buttons.get(INCREASE).setHorizontalTextPosition(SwingConstants.CENTER);

		// add all componentes to jframe
		JComponent[] userComp = { buttons.get(RESERV), buttons.get(DECREASE), buttons.get(FETCHED) };
		for (JComponent comp : userComp) {
			add(comp);
		}
		if (PRIVILEGE == ADMIN) {
			JComponent[] adminComp = { buttons.get(NEW), buttons.get(REMOVE), buttons.get(INCREASE) };
			for (JComponent comp : adminComp) {
				add(comp);
			}
		}
	}
	
	/**
	 * is row selected?
	 * 
	 * @param table JXTable
	 * @return true if a row is selected, false if not
	 */
	private boolean isRowSelected(JXTable table) {
		if (table.getSelectedRow() < 0) {
			GUIComponents.noneSelectedDialog();
			return false;
		}
		return true;
	}

	/**
	 * increase anzahlfeld
	 */
	protected void increaseAnzahl() {
		if (tabPane.getSelectedIndex() == 0) {
			updateErsatzteilAnzahl(1);
		} else if (tabPane.getSelectedIndex() == 1) {
			updateReservationAnzahl(1);
		}
	}

	/**
	 * decrease anzahlfeld
	 */
	protected void decreaseAnzahl() {
		if (tabPane.getSelectedIndex() == 0) {
			updateErsatzteilAnzahl(-1);
		} else if (tabPane.getSelectedIndex() == 1) {
			updateReservationAnzahl(-1);
		}
	}
	
	/**
	 * increase/ decrease anzahl
	 * 
	 * @param anzahl
	 */
	private void updateErsatzteilAnzahl(int anzahl) {
		if(isRowSelected(ersatzteileTable)) {
			int selectedTableRow = ersatzteileTable.getSelectedRow();
			Ersatzteil ersatzteil = getErsatzteilFromRow(selectedTableRow);
			ersatzteilListe.changeAnzahlOfSelected(ersatzteil, anzahl);
			ersatzteileTable.changeSelection(selectedTableRow, 1, false, false);
		}
	}
	
	/**
	 * increase/ decrease anzahl
	 * 
	 * @param anzahl
	 */
	private void updateReservationAnzahl(int anzahl) {
		if(isRowSelected(reservationTable)) {
			int selectedTableRow = reservationTable.getSelectedRow();
			Reservation reservation = getReservationFromRow(selectedTableRow);
			
			reservationListe.changeAnzahlOfSelected(reservation, anzahl);
			ersatzteilListe.changeAnzahlOf(reservation.getfk_ersatzteil(),-1 * anzahl);
			
			reservationTable.changeSelection(selectedTableRow, 1, false, false);
		}
	}

	/**
	 * tabbedpane
	 */
	private void addTabbedPane() {
		tabPane = new JTabbedPane();
		tabPane.setLocation(10, 50);
		tabPane.addTab("Ersatzteile", new JScrollPane(ersatzteileTable));
		tabPane.addTab("Reservation", new JScrollPane(reservationTable));

		if(PRIVILEGE == ADMIN) tabPane.addTab("Verlauf", new JScrollPane(historyTable));

		tabPane.setBorder(new DropShadowBorder(new Color(0, 0, 0), 7, 0.5f, 12, false, false, true, false));
		tabPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setGuiEnabled();
			}
		});

		add(tabPane);
	}

	protected void setGuiEnabled() {
		if (tabPane.getSelectedIndex() == 0) {
			//Ersatzteile-Tab
			anzahlDescription1.setText("Lager-Anzahl von");
			anzahlPanel.setVisible(true);
			buttons.get(DECREASE).setVisible(true);
			anzahlFeld.setVisible(true);
			buttons.get(NEW).setEnabled(true);
			buttons.get(RESERV).setEnabled(true);
			buttons.get(FETCHED).setEnabled(false);
			buttons.get(REMOVE).setToolTipText("Ersatzteil löschen");
			anzahlFeld.setEnabled(true);
			anzahlLabel.setEnabled(true);
			suchfeld.setEnabled(true);
			if(ersatzteileTable.getSelectedRowCount() == 0) {
				anzahlDescription1.setEnabled(false);
				anzahlDescription2.setEnabled(false);
				buttons.get(INCREASE).setEnabled(false);
				buttons.get(DECREASE).setEnabled(false);
				anzahlLabel.setText("Bitte ein Teil wählen");
			} else {
				anzahlDescription1.setEnabled(true);
				anzahlDescription2.setEnabled(true);
				
				buttons.get(INCREASE).setEnabled(true);
				buttons.get(DECREASE).setEnabled(true);
				anzahlLabel.setText(ersatzteileTable.getStringAt(ersatzteileTable.getSelectedRow(),1));
			}
		} else if (tabPane.getSelectedIndex() == 1) {
			//Reservations-Tab
			anzahlDescription1.setText("Reservationszahl von");
			buttons.get(NEW).setEnabled(false);
			buttons.get(RESERV).setEnabled(false);
			buttons.get(INCREASE).setEnabled(true);
			buttons.get(FETCHED).setEnabled(true);
			buttons.get(REMOVE).setToolTipText("Reservation löschen");
			suchfeld.setEnabled(true);
			anzahlLabel.setEnabled(true);
			anzahlDescription1.setEnabled(true);
			anzahlDescription2.setEnabled(true);
			if(reservationTable.getSelectedRowCount() != 0 && PRIVILEGE == ADMIN) {
				anzahlDescription1.setEnabled(true);
				anzahlDescription2.setEnabled(true);
				buttons.get(INCREASE).setEnabled(true);
				buttons.get(DECREASE).setEnabled(true);
				anzahlFeld.setEnabled(true);
				anzahlLabel.setText(reservationTable.getStringAt(reservationTable.getSelectedRow(),1));
			} else {
				if(PRIVILEGE != ADMIN){
					anzahlPanel.setVisible(false);
					buttons.get(DECREASE).setVisible(false);
					anzahlFeld.setVisible(false);
				}
				anzahlDescription1.setEnabled(false);
				anzahlDescription2.setEnabled(false);
				buttons.get(INCREASE).setEnabled(false);
				buttons.get(DECREASE).setEnabled(false);
				anzahlLabel.setText("Reservation wählen");
				anzahlFeld.setEnabled(false);
				
			}
		} else if (tabPane.getSelectedIndex() == 2) {
			//History-Tab
			buttons.get(NEW).setEnabled(false);
			buttons.get(RESERV).setEnabled(false);
			buttons.get(INCREASE).setEnabled(false);
			buttons.get(DECREASE).setEnabled(false);
			buttons.get(FETCHED).setEnabled(false);
			anzahlFeld.setEnabled(false);
			suchfeld.setEnabled(false);
			anzahlLabel.setEnabled(false);
			anzahlDescription1.setEnabled(false);
			anzahlDescription2.setEnabled(false);
		}
		
	}

	/**
	 * search field
	 */
	private void addSearchComponents() {
		JLabel suchLabel = GUIComponents.createLabel("Filter Ersatzteil: ", new Point(10, 15));
		suchLabel.setSize(150, 20);

		suchfeld = GUIComponents.createTextField(new Point(110, 15));
		suchfeld.setSize(200, 20);
		suchfeld.setToolTipText("Suchbegriff eingeben");
		suchfeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ersatzteilListe.ladeFromString(suchfeld.getText().trim());
			}
		});
		suchfeld.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent arg0) {}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				ersatzteilListe.ladeFromString(suchfeld.getText().trim());
				reservationListe.ladeFromString(suchfeld.getText().trim());
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				ersatzteilListe.ladeFromString(suchfeld.getText().trim());
				reservationListe.ladeFromString(suchfeld.getText().trim());
			}
		});
		suchfeld.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				suchfeld.requestFocus();
				suchfeld.selectAll();
			}
		});

		JComponent[] userComp = { suchfeld, suchLabel };
		for (JComponent comp : userComp) {
			add(comp);
		}
	}

	/**
	 * anzahl field
	 */
	private void addAnzahlComponents() {
		anzahlPanel = new JXPanel();
		anzahlPanel.setLayout(null);
		anzahlPanel.setBorder(new DropShadowBorder(new Color(0, 0, 0), 7, 0.5f, 12, true, true, true, true));
		
		anzahlDescription1 = GUIComponents.createLabel("", new Point(10, 10));
		anzahlDescription1.setHorizontalAlignment(JLabel.CENTER);
//		anzahlLabel = new JXLabel("Bitte ein Teil wählen");
		anzahlLabel = new JLabelErsatzteilObserver("Bitte ein Teil wählen",ersatzteilListe, ersatzteileTable);
		anzahlLabel.setSize(170, 50);
		anzahlLabel.setForeground(Color.RED);
		anzahlLabel.setLocation(-10, 10);
		anzahlLabel.setHorizontalAlignment(JLabel.CENTER);
		
		anzahlDescription2 = GUIComponents.createLabel("bearbeiten", new Point(10, 40));
		anzahlDescription2.setHorizontalAlignment(JLabel.CENTER);
		
		JComponent[] components = { anzahlDescription1, anzahlLabel, anzahlDescription2 };
		for (JComponent comp : components) {
			anzahlPanel.add(comp);
		}
		
		anzahlFeld = GUIComponents.createTextField(new Point(getWidth() - 125, 130));
		anzahlFeld.setSize(105, 20);
		anzahlFeld.setHorizontalAlignment(JTextField.CENTER);
		anzahlFeld.setToolTipText("Gewünschte Anzahl eingeben und mit Eingabe bestätigen");
		anzahlFeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabPane.getSelectedIndex() == 0) {
					if(isRowSelected(ersatzteileTable)) {
						int selectedRow = ersatzteileTable.getSelectedRow();
						Ersatzteil ersatzteil = getErsatzteilFromRow(selectedRow);
						int anzahl = Integer.valueOf(anzahlFeld.getText().trim()) - ersatzteil.getAnzahl();
						// if not admin and input is larger than stock, stock = maximum
						if (PRIVILEGE != ADMIN && ersatzteil.getAnzahl() < Integer.valueOf(anzahlFeld.getText().trim())) {
							anzahlFeld.setText(String.valueOf(ersatzteil.getAnzahl()));
							return;
						}
						ersatzteilListe.changeAnzahlOfSelected(ersatzteil, anzahl);
						ersatzteileTable.changeSelection(selectedRow, 1, false, false);
					}
				} else if (tabPane.getSelectedIndex() == 1) {
					if(isRowSelected(reservationTable)) {
						int selectedRow = reservationTable.getSelectedRow();
						Reservation reservation = getReservationFromRow(selectedRow);
						int anzahl = Integer.valueOf(anzahlFeld.getText().trim()) - reservation.getAnzahl();
						// if not admin and input is larger than stock, stock = maximum
						if (PRIVILEGE != ADMIN && reservation.getAnzahl() < Integer.valueOf(anzahlFeld.getText().trim())) {
							anzahlFeld.setText(String.valueOf(reservation.getAnzahl()));
							return;
						}
						reservationListe.changeAnzahlOfSelected(reservation, anzahl);
						reservationTable.changeSelection(selectedRow, 1, false, false);
					}
				}
			}
		});
		anzahlFeld.addMouseListener(new MouseAdapter() {
			 @Override
			 public void mouseEntered(MouseEvent e) {
				  anzahlFeld.requestFocus();
				  anzahlFeld.selectAll();
			 }
		});
		anzahlFeld.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int k = e.getKeyChar();
				/*
				 * 48-57: 0-9
				 */
				if (!(47 < k && k < 58)) {
					e.setKeyChar((char) KeyEvent.VK_CLEAR);
				}
			}
		});
		
		JComponent[] userComp = { anzahlFeld, anzahlPanel };
		for (JComponent comp : userComp) {
			add(comp);
		}
	}

	/**
	 * refresh tables if pressed f5
	 */
	private void addF5Listener() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshTable();
			}
		};
		getRootPane().registerKeyboardAction(listener, KeyStroke.getKeyStroke("F5"), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	/**
	 * set jframe
	 */
	private void setJFrame() {
		setTitle(PROGRAM + " - " + PROGRAMEXT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setMinimumSize(new Dimension(800, 600));
		setSize(800, 600);
		setIconImage(new ImageIcon(getClass().getResource(UIIMAGELOCATION + "icon.gif")).getImage());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
		setJMenuBar(createMenuBar());
	}

	/**
	 * refresh tables
	 */
	private void refreshTable() {
		ersatzteilListe.ladeFromString("");
		reservationListe.ladeFromString("");
	}

	/**
	 * MenuBar
	 * 
	 * @return JMenuBar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		final String file = "Datei", help = "Hilfe";
		final String[] menuItems = { file, help };
		int[] keyEvent;
		if(PRIVILEGE != ADMIN) {
			keyEvent = new int[] { KeyEvent.VK_D, KeyEvent.VK_H };
		} else {
			keyEvent = new int[] { KeyEvent.VK_D, 0 };
		}
		for (int i = 0; i < menuItems.length; i++) {
			final JMenu menu = new JMenu(menuItems[i]);
			menu.setMnemonic(keyEvent[i]);
			if (menuItems[i] == file) {
				addFileItems(menu);
			} else {
				addHelpItems(menu);
			}
			menuBar.add(menu);
		}
		
		// glue = right alignment
		menuBar.add(Box.createHorizontalGlue());

		// show logged in
		menuBar.add(new JLabel("Eingeloggt als:  "));
		
		JLabel loginLabel = new JLabel(PRIVILEGE + "  ");
		menuBar.add(loginLabel);
		
		// log out
		final JMenu logOutItem = new JMenu("Ausloggen");
		logOutItem.setBorder(LineBorder.createGrayLineBorder());
		logOutItem.setToolTipText("Ausloggen");
		logOutItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				closeFrame();
				showLogin();
			}
			@Override
		    public void mouseEntered(MouseEvent e) {
				logOutItem.setBorder(LineBorder.createBlackLineBorder());
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				logOutItem.setBorder(LineBorder.createGrayLineBorder());
			}
		});
		menuBar.add(logOutItem);
		return menuBar;
	}

	/**
	 * help menu items
	 * 
	 * @param helpMenu
	 */
	private void addHelpItems(JMenu helpMenu) {
		JMenuItem aboutItem = new JMenuItem("Über...");
		aboutItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog about = new AboutDialog();
				about.setVisible(true);
			}
		});
		helpMenu.add(aboutItem);
	}

	/**
	 * filemenu items
	 * 
	 * @param fileMenu JMenu
	 */
	private void addFileItems(JMenu fileMenu) {
		final String logout = "Ausloggen...", exit = "Beenden";
		final String[] fileTitles = { logout, exit };
		final KeyStroke[] keyStrokes = { KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), null };
		for (int i = 0; i < fileTitles.length; i++) {
			if (i == 1) {
				fileMenu.addSeparator();
			}
			JMenuItem fileItem = new JMenuItem();
			fileItem.setText(fileTitles[i]);
			fileItem.setAccelerator(keyStrokes[i]);
			fileItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == logout) {
						closeFrame();
						showLogin();
					} else if (e.getActionCommand() == exit){
						System.exit(0);
					}
				}
			});
			fileMenu.add(fileItem);
		}
	}
	
	/**
	 * close frame
	 */
	private void closeFrame() {
		setVisible(false);
		dispose();
	}
	
	/**
	 * show login window
	 */
	private void showLogin() {
		LoginFrame login = new LoginFrame();
		login.setVisible(true);
	}
}
