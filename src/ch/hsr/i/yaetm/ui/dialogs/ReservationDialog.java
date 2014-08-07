package ch.hsr.i.yaetm.ui.dialogs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;

import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.logic.Reservation;
import ch.hsr.i.yaetm.logic.Reservationliste;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;


public class ReservationDialog extends JDialog {
	
	private static final long serialVersionUID = 8230072278959771268L;
	
	private JTextField anzahlField;
	private JXDatePicker datePicker;
	private JButton okButton;
	private String produktLabel;
	private int produktID;
	private Reservationliste reservationListe;
	private Ersatzteilliste ersatzteilListe;
	private JPanel panel;

	/**
	 * Constructor
	 */
	public ReservationDialog(String produktLabel, Ersatzteilliste ersatzteilListe, Reservationliste reservationListe, int produktID) {
		this.produktLabel = produktLabel;
		this.ersatzteilListe = ersatzteilListe;
		this.reservationListe = reservationListe;
		this.produktID = produktID;
		
		setJDialog();
		buildGUI();
	}
	
	/**
	 * create gui elements
	 */
	private void buildGUI() {
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(LineBorder.createGrayLineBorder());
		add(panel);
		
		addRootPaneListener();
		addLabels();
		addTextFields();
		addButtons();
	}
	
	/**
	 * add buttons to dialog
	 */
	private void addButtons() {
		int x = 50;
		int y = 125;
		final String OK = "OK", CANCEL = "Abbrechen";
		okButton = GUIComponents.createButton(OK, new Point(x, y), 0);
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!anzahlField.getText().isEmpty() && datePicker.getDate() != null)
					okEvent();
			}
		});
		JButton cancelButton = GUIComponents.createButton(CANCEL, new Point(x+okButton.getWidth(), y), 0);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		JComponent[] components = { okButton, cancelButton};
		for (JComponent comp: components) {
			panel.add(comp);
		}
	}
	
	/**
	 * add textfields to dialog
	 */
	private void addTextFields() {
		final int x = 60;
		final int y = 25;
		anzahlField = GUIComponents.createTextField(new Point(x+60, y+30));
		anzahlField.addKeyListener(new KeyAdapter() {
			JLabel textFieldEmptyLabel = createStartLabel(new Point(x+190, y+30));
			@Override
			public void keyReleased(KeyEvent e) {
				if (anzahlField.getText().isEmpty()) {
					textFieldEmptyLabel.setVisible(true);
				} else {
					textFieldEmptyLabel.setVisible(false);
				}
				okButton.setEnabled(enableButton());
			}
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
		
		datePicker = new JXDatePicker();
		datePicker.setBounds(x+60, y+2*30, 130, 20);
		datePicker.getMonthView().setDayForeground(Calendar.SUNDAY, Color.RED);
		datePicker.getMonthView().setDayForeground(Calendar.SATURDAY, Color.GRAY);
		datePicker.getMonthView().setSelectionMode(SelectionMode.SINGLE_SELECTION);
		datePicker.getMonthView().setShowingWeekNumber(true);
		datePicker.getMonthView().setLowerBound(new Date());
		datePicker.addActionListener(new ActionListener() {
			JLabel textFieldEmptyLabel = createStartLabel(new Point(x+190, y+2*30));
			@Override
			public void actionPerformed(ActionEvent e) {
				if (datePicker.getDate() == null) {
					textFieldEmptyLabel.setVisible(true);
				} else {
					textFieldEmptyLabel.setVisible(false);
				}
				okButton.setEnabled(enableButton());
			}
		});
		
		JComponent[] components = { anzahlField, datePicker};
		for (JComponent comp: components) {
			panel.add(comp);
		}
	}
	
	/**
	 * check if textfields are empty
	 * 
	 * @return boolean
	 */
	private boolean enableButton() {
		return (!anzahlField.getText().isEmpty() && datePicker.getDate() != null);
	}
	
	/**
	 * create Label
	 * 
	 * @param position Label position
	 * @return JLabel
	 */
	private JLabel createStartLabel(Point position) {
		JLabel label = new JLabel("*");
		label.setSize(100, 20);
		label.setLocation(position);
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setForeground(Color.RED);
		panel.add(label);
		return label;
	}
	
	/**
	 * add labels to dialog
	 */
	private void addLabels() {
		int x = 60;
		int y = 25;
		final String[] titles = { "Produkt", "Anzahl", "Datum" };
		final Point[] points = { new Point(x, y), new Point(x, y+30), new Point(x, y+2*30) };
		for (int i = 0; i < titles.length; i++) {
			panel.add(GUIComponents.createLabel(titles[i], points[i]));
		}
		panel.add(GUIComponents.createLabel(produktLabel, new Point(x + 60, y)));
	}
	
	/**
	 * esc = close window
	 * enter = confirm
	 */
	private void addRootPaneListener() {
		GUIComponents.addESCListener(this);
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okEvent();
			}
		};
		GUIComponents.addEnterListener(this, listener);
	}
	
	/**
	 * set jdialog
	 */
	private void setJDialog() {
		setTitle("Reservation erfassen");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(320, 200);
		setModal(true);
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth())/2, (dim.height - getHeight())/2);
	}
	
	/**
	 * if option is approved, get all content of textfields
	 */
	private void okEvent() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String date = sdf.format(datePicker.getDate());
		int anzahl = Integer.valueOf(anzahlField.getText().trim());
		reservationListe.add(new Reservation(produktLabel, anzahl, date, YAETMMainView.PRIVILEGE, reservationListe.size()+1, produktID));
		ersatzteilListe.changeAnzahlOf(produktID, -anzahl);
		closeDialog();
	}
	
	/**
	 * close dialog
	 */
	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
