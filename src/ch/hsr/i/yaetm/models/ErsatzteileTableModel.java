package ch.hsr.i.yaetm.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import ch.hsr.i.yaetm.logic.Ersatzteil;
import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;


public class ErsatzteileTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 5245805312243333621L;
	
	private Ersatzteilliste ersatzteilListe;
	private static final String[] columns = { "ID", "Produkt", "Preis (CHF)", "Anzahl" };
	
	public ErsatzteileTableModel(Ersatzteilliste ersatzteilListe) {
		this.ersatzteilListe = ersatzteilListe;
		ersatzteilListe.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public int getRowCount() {
		return ersatzteilListe.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(YAETMMainView.PRIVILEGE == YAETMMainView.ADMIN) {
			return 0 < columnIndex;
		}
		return false;
	}
	
	@Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Ersatzteil ersatzteil = ersatzteilListe.get(rowIndex);
		switch(columnIndex) {
		case 1:
			ersatzteil.setName((String)value);
			break;
		case 2:
			ersatzteil.setPreis((String)value);
			break;
		case 3:
			String aValue = (String)value;
			if (aValue.isEmpty()) {
				ersatzteil.setAnzahl(ersatzteil.getAnzahl());
				break;
			}
			ersatzteil.setAnzahl(Integer.valueOf(aValue));
			break;
		default:
			break;
		}
		ersatzteilListe.update(ersatzteil);
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Ersatzteil ersatzteil = ersatzteilListe.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return ersatzteil.getID();
		case 1:
			return ersatzteil.getName();
		case 2:
			return ersatzteil.getPreis();
		case 3:
			return ersatzteil.getAnzahl();
		default:
			return null;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
	}
}
