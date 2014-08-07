
package ch.hsr.i.yaetm.ui.dialogs;


import ibxm.Player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import ch.hsr.i.yaetm.ui.main.YAETMMainView;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 9535632795379520L;
	
	private static final String URL = "www.i.hsr.ch";
	private static final String URLEXT = "HSR Hochschule fuer Technik Rapperswil";
	private static final String EMAIL1 = "marco.birchler@hsr.ch";
	private static final String NAME1 = "Marco Birchler";
	private static final String EMAIL2 = "david.tran@hsr.ch";
	private static final String NAME2 = "David Tran";
	
	private Player player;
	
	public AboutDialog() {
		try {
			player = new Player();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		playTune();
		setJDialog();
		buildGUI();
	}
	
	/**
	 * create and set gui components
	 */
	private void buildGUI() {
		addESCListener();
		addTextFields();
		
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon(getClass().getResource(YAETMMainView.UIIMAGELOCATION + "about.jpg"));
				g.drawImage(img.getImage(), 0, -30, null);
			}
		};
		panel.setOpaque(false);
		panel.setLayout(null);
		
		// Button: Close
		JButton button_close = new JButton();
		button_close.setText("Abbrechen");
		button_close.setBounds(280, 190, 100, 20);
		button_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		add(button_close);
		add(panel);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				stopTune();
			}
		});
	}
	
	/**
	 * textfield of url, name, email of author
	 */
	private void addTextFields() {
		final String[] text = { URL, EMAIL1, EMAIL2 };
		final String[] tooltip = { URLEXT, NAME1, NAME2 };
		final Rectangle[] bounds = { new Rectangle(10, 190, 265, 20), new Rectangle(185, 144, 195, 20), new Rectangle(185, 165, 195, 20) };
		for (int i = 0; i < bounds.length; i++) {
			add(createTextField(bounds[i], text[i], tooltip[i]));
		}
	}
	
	/**
	 * play a chiptunes
	 */
	private void playTune() {
		player.play();
	}
	
	/**
	 * stop playing tune
	 */
	private void stopTune() {
		if (player != null)
			player.stop();
	}
	
	/**
	 * esc = close dialog
	 */
	private void addESCListener() {
		GUIComponents.addESCListener(this);
	}
	
	/**
	 * set jdialog
	 */
	private void setJDialog() {
		setTitle(YAETMMainView.PROGRAM + " - Über...");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setSize(400, 250);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - 400) / 2, (dim.height - 350) / 2);
	}
	
	/**
	 * close dialog
	 */
	private void closeDialog() {
		stopTune();
		setVisible(false);
		dispose();
	}
	
	/**
	 * create textfield
	 * 
	 * @param rect setBounds
	 * @param text Label
	 * @param tooltip Tooltip
	 * @return TextField
	 */
	private JTextField createTextField(Rectangle rect, String text, String tooltip) {
		JTextField textField = new JTextField(text);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setBounds(rect);
		textField.setEditable(false);
		textField.setOpaque(false);
		textField.setToolTipText(tooltip);
		
		// PopupMenu on Rightclick
		JPopupMenu popupMenu = popupMenu_textField(textField);
		textField.setComponentPopupMenu(popupMenu);
		return textField;
	}

	/**
	 * PopupMenu TextField.
	 * 
	 * @param textField
	 * @return JPopupMenu
	 */
	private JPopupMenu popupMenu_textField(final JTextField textField) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.copy();
			}
		});
		popupMenu.add(copyMenuItem);
		return popupMenu;
	}
}