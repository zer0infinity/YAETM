package ch.hsr.i.yaetm.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

import ch.hsr.i.yaetm.db.ActiveRecordManager;
import ch.hsr.i.yaetm.ui.utils.StatusMessage.StatusType;



public class Ersatzteilliste extends Observable {

	private static final long serialVersionUID = -5830421932706902564L;
	
	private ArrayList<Ersatzteil> ersatzteilListe = new ArrayList<Ersatzteil>();
	private Historyliste historyListe;
	
	public Ersatzteilliste(Historyliste historyListe) {
		loadFromDB();
		this.historyListe = historyListe;
	}
	
	/**
	 * load ersatzteilliste from db
	 */
	public void loadFromDB() {
		ersatzteilListe = ActiveRecordManager.getErsatzteilList("select * from Ersatzteil");
		setChanged();
		notifyObservers();
	}
	
	/**
	 * change anzahl of selected 
	 * 
	 * @param ersatzteil Ersatzteil
	 * @param value to change
	 */
	public void changeAnzahlOfSelected(Ersatzteil ersatzteil, int value) {
		try {
			String query = "Update Ersatzteil set anzahl = " + (ersatzteil.getAnzahl() + value) + " where id = " + ersatzteil.getID();
			ActiveRecordManager.execute(query);
			this.setAnzahlOf(ersatzteil.getID(), ersatzteil.getAnzahl() + value);
			historyListe.addCurrent(new History(0, "", "", "Ändere Anzahl von \"" + ersatzteil.getName() + "\" um " + value, query, ""));	
			setChanged();
			notifyObservers(StatusType.UPDATE_ERSATZTEIL);
		} catch (SQLException e) {
			System.err.println("Could not update Record");
			e.printStackTrace();
		} 
		
			
	}
	
	/**
	 * filter table
	 * 
	 * @param searchstring filter all entries with this string
	 */
	public void ladeFromString(String searchstring) {
		if(!searchstring.isEmpty()) {
			ersatzteilListe = ActiveRecordManager.getErsatzteilList("select * from Ersatzteil where name like'%" + searchstring + "%' or preis like '%" + searchstring + "%' or anzahl like '%" + searchstring + "%'");
		}
		else {
			ersatzteilListe = ActiveRecordManager.getErsatzteilList("select * from Ersatzteil");
		}
		
		setChanged();
		notifyObservers(StatusType.UPDATE_ERSATZTEIL);
	}
	
	/**
	 * add ersatzteil
	 * 
	 * @param ersatzteil
	 */
	public void add(Ersatzteil ersatzteil) {
		try {
			String query = "Insert into Ersatzteil (name, preis, anzahl) values ('" + ersatzteil.getName() + "', " + ersatzteil.getPreis() + ", " + ersatzteil.getAnzahl() + ")";
			ersatzteil.setID(ActiveRecordManager.executeInsert(query));
			historyListe.addCurrent(new History(0, "", "", "Hinzufügen von neuem Ersatzteil " + ersatzteil.getName() , query.replace("'", "##"), ""));
			ersatzteilListe.add(ersatzteil);
			setChanged();
			notifyObservers(StatusType.ADD_ERSATZTEIL);
		} catch (SQLException e) {
			System.err.println("Could not Insert new Ersatzteil in DB");
			e.printStackTrace();
		}
		
	}
	
	public void update(Ersatzteil ersatzteil) {
		try {
			String query = "UPDATE Ersatzteil  set name = '" + ersatzteil.getName() + "', preis = " + ersatzteil.getPreis() + ", anzahl = "  + ersatzteil.getAnzahl() + " WHERE id = " + ersatzteil.getID();
			ActiveRecordManager.executeInsert(query);
			historyListe.addCurrent(new History(0, "", "", "Update von Ersatzteil " + ersatzteil.getName() , query.replace("'", "##"), ""));
			setChanged();
			notifyObservers(StatusType.UPDATE_ERSATZTEIL);
		} catch (SQLException e) {
			System.err.println("Could not Insert new Ersatzteil in DB");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * remove ersatzteil
	 * 
	 * @param id of ersatzteil
	 */
	public void remove(int id) {
		try {
			String query = "DELETE FROM Ersatzteil WHERE id=" + id;
			ActiveRecordManager.execute(query);
			historyListe.addCurrent(new History(0, "", "", "Lösche Ersatzteil", query, ""));
			removeErsatzteil(id);
			setChanged();
			notifyObservers(StatusType.REMOVE_ERSATZTEIL);
		} catch (SQLException e) {
			System.err.println("Could not remove Record");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * remove ersatzteil of list
	 * 
	 * @param id of ersatzteil
	 */
	private void removeErsatzteil(int id) {
		for (int i = 0; i < ersatzteilListe.size(); i++) {
			if(ersatzteilListe.get(i).getID() == id) {
				ersatzteilListe.remove(i);
				break;
			}
		}
	}
	
	/**
	 * get Ersatzteil from list
	 * 
	 * @param position of the Object
	 * @return Ersatzteil
	 */
	public Ersatzteil get(int position) {
		return ersatzteilListe.get(position);
	}
	
	/**
	 * change anzahl value
	 * 
	 * @param id of ersatzteil
	 * @param value to change
	 */
	public void changeAnzahlOf(int id, int value) {
		int anzahl = 0;
		for (Ersatzteil et: ersatzteilListe) {
			if(et.getID() == id) {
				anzahl = et.getAnzahl();
				et.setAnzahl(anzahl+value);
				break;
			}
		}
		
		try {
			ActiveRecordManager.execute("Update Ersatzteil set anzahl = " + (anzahl+value) + " where id = " + id);
		} catch (SQLException e) {
			System.err.println("Could not update Record");
			e.printStackTrace();
		} 
		
		setChanged();
		notifyObservers(StatusType.UPDATE_ERSATZTEIL);
	}
	
	/**
	 * set anzahl of a ersatzteil 
	 * 
	 * @param id of ersatzteil
	 * @param value to change to
	 */
	private void setAnzahlOf(int id, int value) {
		for (Ersatzteil et: ersatzteilListe) {
			if(et.getID() == id) {
				et.setAnzahl(value);
				break;
			}
		}
		setChanged();
		notifyObservers(StatusType.UPDATE_ERSATZTEIL);
	}
	
	public int size() {
		return ersatzteilListe.size();
	}
}
