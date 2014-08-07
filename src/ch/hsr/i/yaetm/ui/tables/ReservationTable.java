package ch.hsr.i.yaetm.ui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.SortOrder;

import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.logic.Reservationliste;
import ch.hsr.i.yaetm.models.ReservationTableModel;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;
import ch.hsr.i.yaetm.ui.tables.celleditor.AnzahlCellEditor;
import ch.hsr.i.yaetm.ui.tables.celleditor.ComboBoxEditor;
import ch.hsr.i.yaetm.ui.tables.celleditor.DatumCellEditor;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;

public class ReservationTable extends JXTable {
	
	private static final long serialVersionUID = 1L;
	
	private Reservationliste reservationListe;
	private Ersatzteilliste ersatzteilListe;
	private static final String[] comboboxValues = { YAETMMainView.ADMIN, YAETMMainView.USER };
	
	public ReservationTable(ReservationTableModel reservationModel, Ersatzteilliste ersatzteilListe, Reservationliste reservationListe) {
		super(reservationModel);
		this.reservationListe = reservationListe;
		this.ersatzteilListe = ersatzteilListe;
		DefaultTableCellRenderer numberCellRenderer = new DefaultTableCellRenderer();
		numberCellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		DefaultTableCellRenderer datumCellRenderer = new DefaultTableCellRenderer();
		datumCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setComponentPopupMenu(createPopupMenu());
		setSortOrder(3, SortOrder.ASCENDING);
		getColumn(0).setMinWidth(0);
		getColumn(0).setMaxWidth(0);
		setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));
		TableColumn col = getColumnModel().getColumn(4);
		col.setCellEditor(new ComboBoxEditor(comboboxValues));
		col = getColumnModel().getColumn(3);
		col.setCellEditor(new DatumCellEditor());
		col.setCellRenderer(datumCellRenderer);
		col = getColumnModel().getColumn(2);
		col.setCellEditor(new AnzahlCellEditor());
		col.setCellRenderer(numberCellRenderer);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					refreshTable();
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					remove();
				}
			}
		});
	}
	
	/**
	 * Popupmenu
	 * 
	 * @return popupMenu
	 */
	private JPopupMenu createPopupMenu() {
		// PopupMenu on rightclick
		JPopupMenu popupMenu = new JPopupMenu();
		
		// PopupMenu Items
		final String fetched = "Auslösen", remove = "Löschen";
		final String[] itemLabel = { fetched, remove };
		for (int i = 0; i < itemLabel.length; i++) {
			JMenuItem menuItem = new JMenuItem(itemLabel[i]);
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					if (event.getActionCommand() == remove) {
						remove();
					} else if(event.getActionCommand() == fetched) {
						fetched();
					}
				}
			});
			if(YAETMMainView.PRIVILEGE == YAETMMainView.ADMIN) {
				popupMenu.add(menuItem);
			} else if(menuItem.getActionCommand() != remove) {
				popupMenu.add(menuItem);
			}
		}
		return popupMenu;
	}
	
	/**
	 * refresh table
	 */
	private void refreshTable() {
		reservationListe.ladeFromString("");
	}
	
	/**
	 * reservation aufheben
	 */
	public void remove() {
		if(!isSelected()) {
			return;
		}
		int result = GUIComponents.yesNoDialog(
				"Löschen bestätigen",
				"Soll " + (String) getValueAt(getSelectedRow(), 1) + " gelöscht werden?");
		switch (result) {
		case JOptionPane.YES_OPTION:
			int id = (Integer)getValueAt(getSelectedRow(), 0);
			int anzahl = (Integer)getValueAt(getSelectedRow(), 2);
			reservationListe.remove(id);
			ersatzteilListe.changeAnzahlOf(id, anzahl);
			break;
		case JOptionPane.NO_OPTION:
			return;
		}
	}
	
	/**
	 * check if row is selected
	 * 
	 * @return true if row is selected else false
	 */
	private boolean isSelected() {
		int row = getSelectedRow();
		if (row < 0) {
			GUIComponents.noneSelectedDialog();
			return false;
		}
		return true;
	}
	
	/**
	 * reservation löschen
	 */
	public void fetched() {
		if(!isSelected()) {
			return;
		}
		int result = GUIComponents.yesNoDialog(
				"Abholung bestätigen",
				"Soll die Reservation für " + (String) getValueAt(getSelectedRow(), 1) + " für den " + (String) getValueAt(getSelectedRow(), 3) + " ausgelöst werden?");
		switch (result) {
		case JOptionPane.YES_OPTION:
			int id = (Integer)getValueAt(getSelectedRow(), 0);
			reservationListe.remove(id);
			break;
		case JOptionPane.NO_OPTION:
			return;
		}
	}
}
