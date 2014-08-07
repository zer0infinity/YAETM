package ch.hsr.i.yaetm.ui.utils;

import java.util.Observable;
import java.util.Observer;

import org.jdesktop.swingx.JXLabel;

import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.ui.tables.ErsatzteileTable;



public class JLabelErsatzteilObserver extends JXLabel implements Observer {
	
	private static final long serialVersionUID = -2822361627179468314L;
	private ErsatzteileTable myErsatzteileTable;
	
	public JLabelErsatzteilObserver(String newString, Ersatzteilliste myErsatzteilListe, ErsatzteileTable myErsatzteileTable) {
		super(newString);
		myErsatzteilListe.addObserver(this);
		this.myErsatzteileTable = myErsatzteileTable;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if(myErsatzteileTable.getSelectedRowCount() > 0) {
			this.setText(myErsatzteileTable.getStringAt(myErsatzteileTable.getSelectedRow(),1).toString());
			int selectedTableRow = myErsatzteileTable.getSelectedRow();
			myErsatzteileTable.changeSelection(selectedTableRow, 1, false, false);
		}
	}
}
