package ch.hsr.i.yaetm.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

import ch.hsr.i.yaetm.db.ActiveRecordManager;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;
import ch.hsr.i.yaetm.ui.utils.StatusMessage.StatusType;

public class Historyliste extends Observable {

	private static final long serialVersionUID = -5830421932706902564L;
	private String username = YAETMMainView.PRIVILEGE;
	private ArrayList<History> historyListe = new ArrayList<History>();
	
	public Historyliste() {
		loadFromDB();
	}
	
	/**
	 * load historyliste from db
	 */
	public void loadFromDB() {
		historyListe = ActiveRecordManager.getHistoryList("select id, datum, user, type, query, undoquery from History");
		setChanged();
		notifyObservers();
	}
	
	/**
	 * add history
	 * 
	 * @param history History
	 */
	public void add(History history) {
		try {
			history.setId(ActiveRecordManager.executeInsert("Insert into History (datum, user, type, query, undoquery) values (" + history.getDatum() + ", '" + history.getUser()+ "', '" + history.getType() + "', '" + history.getQuery()+ "', '" + history.getUndoquery() + "')"));
		} catch (SQLException e) {
			System.err.println("Could not Insert new History in DB");
			e.printStackTrace();
		}
		historyListe.add(history);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * add current history
	 * 
	 * @param history History
	 */
	public void addCurrent(History history) {
		try {
			history.setId(ActiveRecordManager.executeInsert("Insert into History (datum, user, type, query, undoquery) values (CURRENT_TIMESTAMP, '" + username + "', '" + history.getType() + "', '" + history.getQuery()+ "', '" + history.getUndoquery() + "')"));
		} catch (SQLException e) {
			System.err.println("Could not Insert new History in DB");
			e.printStackTrace();
		}
		loadFromDB();
	}
	
	/**
	 * filter table
	 * 
	 * @param searchstring filter all entries with this string
	 */
	public void ladeFromString(String searchstring) {
		if(!searchstring.isEmpty()) {
			historyListe = ActiveRecordManager.getHistoryList("select id, datum, user, type, query, undoquery from History");
		}
		else {
			historyListe = ActiveRecordManager.getHistoryList("select id, datum, user, type, query, undoquery from History");
		}
		
		setChanged();
		notifyObservers();
		
	}
	
	/**
	 * remove History
	 * 
	 * @param id of history
	 */
	public void remove(int id) {
		try {
			ActiveRecordManager.execute("DELETE FROM History WHERE id=" + id);
		} catch (SQLException e) {
			System.err.println("Could not remove History Record");
			e.printStackTrace();
		}
		removeHistory(id);
		
		setChanged();
		notifyObservers(StatusType.REMOVE_HISTORY);
	}
	
	/**
	 * remove history of list
	 * 
	 * @param id of history
	 */
	private void removeHistory(int id) {
		for (int i = 0; i < historyListe.size(); i++) {
			if(historyListe.get(i).getId() == id) {
				historyListe.remove(i);
				break;
			}
		}
	}
	
	/**
	 * get Reservation from list
	 * 
	 * @param position of the Object
	 * @return Reservation
	 */
	public History get(int position) {
		return historyListe.get(position);
	}
	
	public int size() {
		return historyListe.size();
	}
}
