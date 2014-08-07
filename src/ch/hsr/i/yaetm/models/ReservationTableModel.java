package ch.hsr.i.yaetm.models;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.i.yaetm.db.ActiveRecordManager;
import ch.hsr.i.yaetm.logic.Ersatzteil;
import ch.hsr.i.yaetm.logic.Reservation;
import ch.hsr.i.yaetm.logic.Reservationliste;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;


public class ReservationTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 5245805312243333621L;
	
	private Reservationliste reservationListe;

	private static final String[] columns = { "ID", "Produkt", "Anzahl", "Datum", "Reservator" };
	
	public ReservationTableModel(Reservationliste reservationListe) {
		this.reservationListe = reservationListe;
		reservationListe.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return reservationListe.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (YAETMMainView.PRIVILEGE == YAETMMainView.ADMIN) {
			return 1 < columnIndex;
		}
		return false;
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Reservation reservation = reservationListe.get(rowIndex);
		switch(columnIndex) {
		case 1:
			reservation.setName((String)value);
			break;
		case 2:
			String aValue = (String)value;
			
			//Modifies Count of Ersatzteil
			Ersatzteil myTeil = ActiveRecordManager.getErsatzteilList("SELECT * FROM ersatzteil where id = " + reservation.getfk_ersatzteil()).get(0);
			try {
					ActiveRecordManager.execute("UPDATE ersatzteil set anzahl = " + (myTeil.getAnzahl() + Integer.parseInt((String) value)) + " WHERE id = " + reservation.getfk_ersatzteil());
			} catch (SQLException e) {
				System.out.println("Error while UPDATING ErsatzteilList\n" + e);
				e.printStackTrace();
			}
			
			
			if (aValue.isEmpty()) {
				reservation.setAnzahl(reservation.getAnzahl());
				break;
			}
			
			reservation.setAnzahl(Integer.valueOf(aValue));
			break;
		case 3:
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String date = sdf.format((Date)value);
			reservation.setDatum(date);
			break;
		case 4:
			reservation.setReservator((String)value);
			break;
		default:
			break;
		}
		//Save Reservation in DB and restore selection
		reservationListe.update(reservation);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Reservation reservation = reservationListe.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return reservation.getID();
		case 1:
			return reservation.getName();
		case 2:
			return reservation.getAnzahl();
		case 3:
			return reservation.getDatum();
		case 4:
			return reservation.getReservator();
		default:
			return null;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
	}
}
