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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import ch.hsr.i.yaetm.logic.Ersatzteil;
import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;

public class NeuDialog extends JDialog {
	
	private static final long serialVersionUID = 2180046928243262773L;
	
	private Ersatzteilliste ersatzteilListe;
	private JTextField produktField, preisField, anzahlField;
	private JButton okButton;
	private JPanel panel;
	
	private static final String OK = "OK", CANCEL = "Abbrechen";
	
	/**
	 * Constructor
	 * Creates Dialog and show
	 */
	public NeuDialog(final Ersatzteilliste liste) {
		this.ersatzteilListe = liste;
		
		setJDialog();
		buildGUI();
	}
	
	/**
	 * create and set gui elements
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
	 * add buttons
	 */
	private void addButtons() {
		// OK, cancel Buttons
		int x = 50;
		int y = 125;
		okButton = GUIComponents.createButton(OK, new Point(x, y), 0);
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!produktField.getText().isEmpty() && !preisField.getText().isEmpty()
						&& !anzahlField.getText().isEmpty())
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
		final int x = 120;
		final int y = 30;
		produktField = GUIComponents.createTextField(new Point(x, y));
		produktField.addKeyListener(new KeyAdapter() {
			JLabel textFieldEmptyLabel = createLabel(new Point(x+130, y));
			@Override
			public void keyReleased(KeyEvent e) {
				if (produktField.getText().isEmpty()) {
					textFieldEmptyLabel.setVisible(true);
				} else {
					textFieldEmptyLabel.setVisible(false);
				}
				okButton.setEnabled(enableButton());
			}
		});
		produktField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textfieldOnFocus(produktField);
			}
		});
		preisField = GUIComponents.createTextField(new Point(x, y + 30));
		preisField.addKeyListener(new KeyAdapter() {
			JLabel textFieldEmptyLabel = createLabel(new Point(x+130, y+30));
			@Override
			public void keyReleased(KeyEvent e) {
				if (preisField.getText().isEmpty()) {
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
				 * 46: dot
				 */
				if (!(45 < k && k < 58) || (k == 46 && preisField.getText().contains("."))) {
					e.setKeyChar((char) KeyEvent.VK_CLEAR);
				}
			}
		});
		preisField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textfieldOnFocus(preisField);
			}
		});
		anzahlField = GUIComponents.createTextField(new Point(x, y + 2*30));
		anzahlField.addKeyListener(new KeyAdapter() {
			JLabel textFieldEmptyLabel = createLabel(new Point(x+130, y+2*30));
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
		anzahlField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textfieldOnFocus(anzahlField);
			}
		});
		JTextField[] textFields = { produktField, preisField, anzahlField };
		for (JTextField textField: textFields) {
			panel.add(textField);
		}
	}
	
	/**
	 * textfield request focus and select value
	 * 
	 * @param textfield
	 */
	private void textfieldOnFocus(JTextField textfield) {
		textfield.requestFocus();
		textfield.selectAll();
	}
	
	/**
	 * check if textfields are empty
	 * 
	 * @return boolean
	 */
	private boolean enableButton() {
		return (!produktField.getText().isEmpty() && !preisField.getText().isEmpty() && !anzahlField.getText().isEmpty());
	}
	
	/**
	 * add label to dialog
	 */
	private void addLabels() {
		int x = 60;
		int y = 25;
		final String produkt = "Produkt", preis = "Preis", anzahl = "Anzahl";
		final String[] titles = { produkt, preis, anzahl };
		final Point[] points = { new Point(x, y), new Point(x, y+30), new Point(x, y+2*30) };
		for (int i = 0; i < titles.length; i++) {
			panel.add(GUIComponents.createLabel(titles[i], points[i]));
		}
	}
	
	/**
	 * create Label
	 * 
	 * @param position Label position
	 * @return JLabel
	 */
	private JLabel createLabel(Point position) {
		JLabel label = new JLabel("*");
		label.setSize(100, 20);
		label.setLocation(position);
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setForeground(Color.RED);
		panel.add(label);
		return label;
	}
	
	/**
	 * esc = close dialog
	 * enter = confirm
	 */
	private void addRootPaneListener() {
		GUIComponents.addESCListener(this);
		GUIComponents.addEnterListener(this, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okEvent();
			}
		});
	}
	
	/**
	 * set jdialog
	 */
	private void setJDialog() {
		setTitle("Ersatzteil erfassen");
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
		ersatzteilListe.add(new Ersatzteil(produktField.getText().trim(), preisField.getText().trim(), Integer.valueOf(anzahlField.getText().trim()), ersatzteilListe.size()+1));
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
