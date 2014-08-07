package ch.hsr.i.yaetm.ui.utils;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
public class GUIComponents {
	
	/**
	 * create combobox
	 * 
	 * @param position ComboBox position
	 * @return JComboBox
	 */
	public static JComboBox createComboBox(Point position) {
		JComboBox comboBox = new JComboBox();
		comboBox.setSize(105, 20);
		comboBox.setLocation(position);
		return comboBox;
	}
	
	/**
	 * create Button
	 * 
	 * @param title Button name
	 * @param mnemonic Button mnemonic
	 * @return JXButton
	 */
	public static JXButton createButton(String title, int mnemonic) {
		JXButton button = new JXButton(title);
		button.setSize(105, 20);
		button.setMnemonic(mnemonic);
		return button;
	}
	
	/**
	 * create Button
	 * 
	 * @param title Button name
	 * @param position Button position
	 * @return JXButton
	 */
	public static JXButton createButton(String title, Point position, int mnemonic) {
		JXButton button = createButton(title, mnemonic);
		button.setLocation(position);
		return button;
	}
	
	/**
	 * create button
	 * 
	 * @param title Button name
	 * @param mnemonic Button mnemonic
	 * @param tooltip Button tooltip
	 * @return JXButton
	 */
	public static JXButton createButton(String title, int mnemonic, String tooltip) {
		JXButton button = createButton(title, mnemonic);
		button.setToolTipText(tooltip);
		return button;
	}
	
	/**
	 * create Label
	 * 
	 * @param title Label name
	 * @param position Label position
	 * @return JXLabel
	 */
	public static JXLabel createLabel(String title, Point position) {
		JXLabel label = new JXLabel(title);
		label.setSize(130, 20);
		label.setLocation(position);
		return label;
	}
	
	/**
	 * create TextField
	 * 
	 * @param position TextField position
	 * @return JTextField
	 */
	public static JTextField createTextField(Point position) {
		final UndoManager um = new UndoManager();
		
		JTextField textfield = new JTextField();
		textfield.setSize(130, 20);
		textfield.setLocation(position);
		textfield.setComponentPopupMenu(createPopupMenu(textfield, um));
		textfield.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent evt) {
				um.addEdit(evt.getEdit());
			}
		});

		// Create an undo action and add it to the text component
		textfield.getActionMap().put("Undo", new AbstractAction("Undo") {
			private static final long serialVersionUID = -6314350175977114610L;
			public void actionPerformed(ActionEvent evt) {
				if (um.canUndo()) {
					um.undo();
				}
			}
		});
		textfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");

		// Create a redo action and add it to the text component
		textfield.getActionMap().put("Redo", new AbstractAction("Redo") {
			private static final long serialVersionUID = 4109714394118557896L;
			public void actionPerformed(ActionEvent evt) {
				if (um.canRedo()) {
					um.redo();
				}
			}
		});
		textfield.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "Redo");
		return textfield;
	}
	
	/**
	 * close dialog if pressed esc
	 * 
	 * @param dialog JDialog
	 */
	public static void addESCListener(final JDialog dialog) {
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		};
		setRootPane(dialog, cancelListener, KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * listener if pressed enter
	 * 
	 * @param dialog JDialog
	 * @param listener ActionListener
	 */
	public static void addEnterListener(JDialog dialog, ActionListener listener) {
		setRootPane(dialog, listener, KeyEvent.VK_ENTER);
	}
	
	/**
	 * add listener to register a keyboardaction
	 * 
	 * @param dialog JDialog
	 * @param listener ActionListener
	 * @param keyevent KeyStroke
	 */
	private static void setRootPane(JDialog dialog, ActionListener listener, int keyevent) {
		JRootPane rootPane = dialog.getRootPane();
		rootPane.registerKeyboardAction(listener, KeyStroke.getKeyStroke(keyevent, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	/**
	 * show a dialog with yes/ no button
	 * 
	 * @param title dialog title
	 * @param message messagetext
	 * @return int value of "yes"/ "no"
	 */
	public static int yesNoDialog(String title, String message) {
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
	}
	
	/**
	 * show a information dialog
	 */
	public static void noneSelectedDialog() {
		JOptionPane.showMessageDialog(null, "Wähle ein Produkt", "Kein Produkt gewählt", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Popupmenu
	 * 
	 * @param textField
	 * @return popupMenu
	 */
	private static JPopupMenu createPopupMenu(final JTextField textField, final UndoManager um) {
		// PopupMenu on rightclick
		JPopupMenu popupMenu = new JPopupMenu();
		
		// PopupMenu Items
		final String undo = "Rückgängig", cut = "Ausschneiden", copy = "Kopieren", paste = "Einfügen", clear = "Löschen";
		final String[] menuItems = { undo, cut, copy, paste, clear };
		
		ActionListener mouseListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == undo) {
					if(um.canUndo()) {
						um.undo();
					}
				} else if (event.getActionCommand() == cut) {
					textField.cut();
				} else if (event.getActionCommand() == copy) {
					textField.copy();
				} else if (event.getActionCommand() == paste) {
					textField.paste();
				} else if (event.getActionCommand() == clear) {
					textField.setText("");
				}
			}
		};
		for (int i = 0; i < menuItems.length; i++) {
			JMenuItem menuItem = new JMenuItem(menuItems[i]);
			if (i == 1 || i == 4) {
				popupMenu.addSeparator();
			}
			menuItem.addActionListener(mouseListener);
			popupMenu.add(menuItem);
		}
		return popupMenu;
	}
}
