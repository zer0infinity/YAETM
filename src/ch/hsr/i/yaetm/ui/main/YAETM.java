package ch.hsr.i.yaetm.ui.main;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

import ch.hsr.i.yaetm.db.ActiveRecordManager;


public class YAETM {
	
	public static boolean DEBUG = true;

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main (String[] args) {

		// change look&feel, but not finished yet..
//		UIManager.put("Button.background", Color.LIGHT_GRAY);
//		UIManager.put("Button.select", Color.WHITE);
//		UIManager.put("Button.focus", Color.LIGHT_GRAY);
//		UIManager.put("Button.border", LineBorder.createBlackLineBorder());
//        UIManager.put("Table.focusCellHighlightBorder", LineBorder.createBlackLineBorder());
//        UIManager.put("Table.selectionBackground", Color.LIGHT_GRAY);
//        UIManager.put("TabbedPane.selected", Color.LIGHT_GRAY);
//        UIManager.put("TabbedPane.borderHightlightColor", Color.GRAY);
//        UIManager.put("TabbedPane.contentAreaColor", Color.LIGHT_GRAY);
//        UIManager.put("TabbedPane.focus", Color.LIGHT_GRAY);
//        UIManager.put("Table.selectionBackground", Color.LIGHT_GRAY);
//        UIManager.put("TextField.selectionBackground", Color.LIGHT_GRAY);
//        UIManager.put("ComboBox.border", LineBorder.createBlackLineBorder());
//        UIManager.put("ComboBox.selectionBackground", Color.GRAY);
//        UIManager.put("ComboBox.background", Color.LIGHT_GRAY);
//        UIManager.put("ComboBox.disabledBackground", Color.RED);
//        UIManager.put("OptionPane.border", LineBorder.createBlackLineBorder());

        
		createTablesIfMissing();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!DEBUG) {
					LoginFrame login = new LoginFrame();
					login.setVisible(true);
				} else {
					YAETMMainView.PRIVILEGE = YAETMMainView.ADMIN;
					YAETMMainView yaetmMainView = new YAETMMainView();
					yaetmMainView.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * create tables if not available
	 */
	private static void createTablesIfMissing() {
		String sql = "select * from Ersatzteil";
		try {
			ActiveRecordManager.execute(sql);
		} catch (SQLException e) {
			try {
				ActiveRecordManager.execute("drop table if exists Ersatzteil;");
				ActiveRecordManager.execute("create table Ersatzteil (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, preis NUMERIC, anzahl NUMERIC);");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		sql = "select * from Reservation";
		try {
			ActiveRecordManager.execute(sql);
		} catch (SQLException e) {
			try {
				ActiveRecordManager.execute("drop table if exists Reservation;");
				ActiveRecordManager.execute("create table Reservation (id INTEGER PRIMARY KEY AUTOINCREMENT, fk_Ersatzteil INTEGER, datum INTEGER, anzahl INTEGER, reservator TEXT);");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		sql = "select * from History";
		try {
			ActiveRecordManager.execute(sql);
		} catch (SQLException e) {
			try {
				ActiveRecordManager.execute("drop table if exists History;");
				ActiveRecordManager.execute("CREATE TABLE History (user TEXT, id INTEGER PRIMARY KEY, type TEXT, datum INTEGER, query TEXT, undoquery TEXT);");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
