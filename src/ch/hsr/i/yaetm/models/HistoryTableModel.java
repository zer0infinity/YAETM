package ch.hsr.i.yaetm.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.i.yaetm.logic.History;
import ch.hsr.i.yaetm.logic.Historyliste;


public class HistoryTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 5245805312243333621L;
	
	private Historyliste historyListe;
	private static final String[] columns = { "ID", "Datum", "User", "Type", "Query" };
	
	public HistoryTableModel(Historyliste historyListe) {
		this.historyListe = historyListe;
		historyListe.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return historyListe.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		History history = historyListe.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return history.getId();
		case 1:
			return history.getDatum();
		case 2:
			return history.getUser();
		case 3:
			return history.getType();
		case 4:
			return history.getQuery();
		default:
			return null;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
	}
}
