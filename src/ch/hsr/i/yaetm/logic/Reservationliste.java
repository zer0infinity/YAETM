package ch.hsr.i.yaetm.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

import ch.hsr.i.yaetm.db.ActiveRecordManager;
import ch.hsr.i.yaetm.ui.utils.StatusMessage.StatusType;


public class Reservationliste extends Observable {

	private static final long serialVersionUID = -5830421932706902564L;
	
	private ArrayList<Reservation> reservationListe = new ArrayList<Reservation>();

	private Historyliste historyListe;
	
	public Reservationliste(Historyliste historyListe) {
		loadFromDB();
		this.historyListe = historyListe;
	}
	
	/**
	 * load reservationliste from db
	 */
	public void loadFromDB() {
		reservationListe = ActiveRecordManager.getReservationList("select Ersatzteil.name as name, Reservation.anzahl as anzahl, datum, reservator, reservation.id as id, fk_ersatzteil from Ersatzteil, Reservation WHERE fk_ersatzteil = Ersatzteil.id");
		setChanged();
		notifyObservers();
	}
	
	/**
	 * add reservation
	 * 
	 * @param reservation
	 */
	public void add(Reservation reservation) {
		try {
			String query = "Insert into Reservation (fk_Ersatzteil, datum, anzahl, reservator) values ('" + reservation.getfk_ersatzteil() + "', '" + reservation.getDatum()+ "', '" + reservation.getAnzahl()+ "', '" + reservation.getReservator() + "')";
			reservation.setID(ActiveRecordManager.executeInsert(query));
			historyListe.addCurrent(new History(0, "", "", "Erstelle Reservation für \"" + reservation.getName() + "\"", query.replace("'", "##"), ""));
		} catch (SQLException e) {
			System.err.println("Could not Insert new Reservation in DB");
			e.printStackTrace();
		}
		reservationListe.add(reservation);
		
		loadFromDB();
		notifyObservers(StatusType.ADD_RESERVATION);
	}
	
	/**
	 * change anzahl of selected
	 * 
	 * @param reservation Reservation
	 * @param value to change
	 */
	public void changeAnzahlOfSelected(Reservation reservation, int value) {
		boolean iserror = false;
		try {
			ActiveRecordManager.execute("Update Reservation set anzahl = " + (reservation.getAnzahl() + value) + " where id = " + reservation.getID());
		} catch (SQLException e) {
			System.err.println("Could not update Record");
			e.printStackTrace();
			iserror = true;			
		} 
		if(!iserror){		
			this.setAnzahlOf(reservation.getID(), reservation.getAnzahl() + value);
			setChanged();
			notifyObservers(StatusType.UPDATE_RESERVATION);
		}
	}
	
	/**
	 * filter table
	 * 
	 * @param searchstring filter all entries with this string
	 */
	public void ladeFromString(String searchstring) {
		if(!searchstring.isEmpty()) {
			reservationListe = ActiveRecordManager.getReservationList("select Ersatzteil.name as name, Reservation.anzahl as anzahl, datum, reservator, reservation.id as id, fk_ersatzteil from Ersatzteil, Reservation WHERE fk_ersatzteil = Ersatzteil.id and (Ersatzteil.name like'%" + searchstring + "%' or Reservation.anzahl like '%" + searchstring + "%' or datum like '%" + searchstring + "%' or reservator like '%" + searchstring + "%')");
		}
		else {
			reservationListe = ActiveRecordManager.getReservationList("select Ersatzteil.name as name, Reservation.anzahl as anzahl, datum, reservator, reservation.id as id, fk_ersatzteil from Ersatzteil, Reservation WHERE fk_ersatzteil = Ersatzteil.id");
		}
		
		setChanged();
		notifyObservers(StatusType.UPDATE_RESERVATION);
		
	}
	
	/**
	 * remove reservation
	 * 
	 * @param id of reservation
	 */
	public void remove(int id) {
		try {
			String query = "DELETE FROM Reservation WHERE id=" + id;
			ActiveRecordManager.execute(query);
			historyListe.addCurrent(new History(0, "", "", "Lösche Reservation", query.replace("'", "##"), ""));
			removeReservation(id);
			setChanged();
			notifyObservers(StatusType.REMOVE_RESERVATION);
		} catch (SQLException e) {
			System.err.println("Could not remove Record");
			e.printStackTrace();
		}
	}
	
	/**
	 * remove Reservation of list
	 * 
	 * @param id of reservation
	 */
	private void removeReservation(int id) {
		for (int i = 0; i < reservationListe.size(); i++) {
			if(reservationListe.get(i).getID() == id) {
				reservationListe.remove(i);
				break;
			}
		}
	}
	
	/**
	 * set anzahl of a reservation 
	 * 
	 * @param id of reservation
	 * @param value to change to
	 */
	private void setAnzahlOf(int id, int value) {
		for (Reservation et: reservationListe) {
			if(et.getID() == id) {
				et.setAnzahl(value);
				break;
			}
		}
		
		setChanged();
		notifyObservers(StatusType.UPDATE_RESERVATION);
	}
	
	/**
	 * get Reservation from list
	 * 
	 * @param position of the Object
	 * @return Reservation
	 */
	public Reservation get(int position) {
		return reservationListe.get(position);
	}
	
	public int size() {
		return reservationListe.size();
	}

	public void update(Reservation reservation) {
		try {
			String query = "UPDATE Reservation  set anzahl = '" + reservation.getAnzahl() + "', datum = '"+reservation.getDatum()+"' WHERE id = " + reservation.getID();
			ActiveRecordManager.executeInsert(query);
			historyListe.addCurrent(new History(0, "", "", "Update von Reservation " + reservation.getName() , query.replace("'", "##"), ""));
			setChanged();
			notifyObservers(StatusType.UPDATE_RESERVATION);
		} catch (SQLException e) {
			System.err.println("Could not Update Reservation in DB");
			e.printStackTrace();
		}
		
	}
}
