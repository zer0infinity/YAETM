package ch.hsr.i.yaetm.ui.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.border.DropShadowBorder;

import ch.hsr.i.yaetm.ui.utils.GUIComponents;


public class LoginFrame extends JFrame {
	
	private static final long serialVersionUID = 946119855591101991L;
	
	private JComboBox loginBox;
	private JPanel panel;

	/**
	 * Constructor
	 * Creates Dialog and show
	 */
	public LoginFrame() {
		setJFrame();
		
		buildGUI();
	}
	
	/**
	 * create and set gui components
	 */
	private void buildGUI() {
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(LineBorder.createGrayLineBorder());
		add(panel);
		
		addRootPaneListener();
		addLoginComp();
		addButtons();
		addTitle();
	}
	
	/**
	 * set programtitle on upperright
	 */
	private void addTitle() {
		JXPanel titlePanel = new JXPanel();
		titlePanel.setLayout(null);
		titlePanel.setSize(180, 60);
		titlePanel.setLocation(60, 10);
		titlePanel.setToolTipText("www.i.hsr.ch");
		titlePanel.setBorder(new DropShadowBorder(new Color(0, 0, 0), 7, 0.5f, 12, true, true, true, true));
		
		JLabel title = GUIComponents.createLabel(YAETMMainView.PROGRAM, new Point(45, 12));
		title.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		JLabel titleExtend = GUIComponents.createLabel(YAETMMainView.PROGRAMEXT, new Point(10, 33));
		titleExtend.setSize(200, 20);
		titleExtend.setFont(new Font("Arial", Font.PLAIN, 11));
		
		titlePanel.add(titleExtend);
		titlePanel.add(title);
		panel.add(titlePanel);
	}
	
	/**
	 * add login components
	 */
	private void addLoginComp() {
		// create label
		int x = 60;
		int y = 85;
		panel.add(GUIComponents.createLabel("Einloggen als", new Point(x, y)));
		
		//create Combobox
		loginBox = GUIComponents.createComboBox(new Point(x + 80, y));
		loginBox.addItem(YAETMMainView.USER);
		loginBox.addItem(YAETMMainView.ADMIN);
		panel.add(loginBox);
	}
	
	/**
	 * add buttons
	 */
	private void addButtons() {
		int x = 45;
		int y = 130;
		final String ok = "OK", exit = "Beenden";
		final String[] buttonName = { ok, exit };
		final Point[] buttonpoints = { new Point(x, y), new Point(x+105, y) };
		for (int i = 0; i < buttonName.length; i++) {
			JButton button = GUIComponents.createButton(buttonName[i], buttonpoints[i], 0);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == ok) {
						okEvent();
					} else {
						System.exit(0);
					}
				}
			});
			panel.add(button);
		}
	}
	
	/**
	 * escape = close window
	 * enter = confirm
	 */
	private void addRootPaneListener() {
		JRootPane rootPane = getRootPane();
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okEvent();
			}
		};
		final ActionListener[] listener = { cancelListener, okListener };
		final int[] keyEvent = { KeyEvent.VK_ESCAPE, KeyEvent.VK_ENTER };
		for (int i = 0; i < listener.length; i++) {
			rootPane.registerKeyboardAction(listener[i], KeyStroke.getKeyStroke(keyEvent[i], 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
		
		rootPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation() > 0) {
					loginBox.setSelectedIndex(1);
				} else {
					loginBox.setSelectedIndex(0);
				}
			}
		});
	}
	
	/**
	 * set jframe
	 */
	private void setJFrame() {
		setTitle(YAETMMainView.PROGRAM + " - Einloggen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 200);
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth())/2, (dim.height - getHeight())/2);
		setIconImage(new ImageIcon(getClass().getResource(YAETMMainView.UIIMAGELOCATION + "icon.gif")).getImage());
	}
	
	/**
	 * if option is approved, show main
	 */
	private void okEvent() {
		YAETMMainView.PRIVILEGE = (String)loginBox.getSelectedItem();
		showMain();
		closeDialog();
	}
	
	/**
	 * show main window
	 * 
	 * @param loginUser Object
	 */
	private void showMain() {
		YAETMMainView yaetm = new YAETMMainView();
		yaetm.setVisible(true);
	}
	
	/**
	 * close dialog
	 */
	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
