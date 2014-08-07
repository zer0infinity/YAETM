package ch.hsr.i.yaetm.ui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import ch.hsr.i.yaetm.models.ErsatzteileTableModel;
import ch.hsr.i.yaetm.ui.dialogs.ReservationDialog;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;
import ch.hsr.i.yaetm.ui.tables.celleditor.AnzahlCellEditor;
import ch.hsr.i.yaetm.ui.tables.celleditor.PriceCellEditor;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;

public class ErsatzteileTable extends JXTable {
	
	private static final long serialVersionUID = 1L;
	
	private Ersatzteilliste ersatzteilListe;
	private Reservationliste reservationListe;
	
	public ErsatzteileTable(ErsatzteileTableModel ersatzteilTableModel, final Ersatzteilliste ersatzteilListe, Reservationliste reservationListe) {
		super(ersatzteilTableModel);
		this.ersatzteilListe = ersatzteilListe;
		this.reservationListe = reservationListe;
		DefaultTableCellRenderer defaultCellRenderer = new DefaultTableCellRenderer();
		defaultCellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setComponentPopupMenu(createPopupMenu());
		setSortOrder(1, SortOrder.ASCENDING);
		getColumn(0).setMaxWidth(40);
		setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));
		TableColumn col = getColumnModel().getColumn(3);
		col.setCellEditor(new AnzahlCellEditor());
		col.setCellRenderer(defaultCellRenderer);
		getColumn(2).setCellRenderer(defaultCellRenderer);
		getColumn(2).setCellEditor(new PriceCellEditor());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2
						&& YAETMMainView.PRIVILEGE != YAETMMainView.ADMIN) {
					reservation();
				}
			}
		});
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
		final String reserv = "Reservieren", remove = "Löschen";
		final String[] itemLabel = { reserv, remove };
		for (int i = 0; i < itemLabel.length; i++) {
			if(i == 2) {
				popupMenu.addSeparator();
			}
			JMenuItem menuItem = new JMenuItem(itemLabel[i]);
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					if (event.getActionCommand() == reserv) {
						reservation();
					} else if (event.getActionCommand() == remove) {
						remove();
					}
				}
			});
			if(YAETMMainView.PRIVILEGE == YAETMMainView.ADMIN) {
				popupMenu.add(menuItem);
			} else if(!(menuItem.getActionCommand() == remove)){
				popupMenu.add(menuItem);
			}
		}
		return popupMenu;
	}
	
	/**
	 * refresh table
	 */
	private void refreshTable() {
		ersatzteilListe.ladeFromString("");
	}
	
	/**
	 * ersatzteil löschen
	 */
	public void remove() {
		int row = getSelectedRow();
		if (row < 0) {
			GUIComponents.noneSelectedDialog();
			return;
		}
		int result = GUIComponents.yesNoDialog(
				"Löschen bestätigen",
				"Soll " + (String) getValueAt(row, 1) + " gelöscht werden?");
		switch (result) {
		case JOptionPane.YES_OPTION:
			ersatzteilListe.remove((Integer)getValueAt(getSelectedRow(), 0));
			break;
		case JOptionPane.NO_OPTION:
			return;
		}
	}
	
	/**
	 * ersatzteil reservieren
	 */
	public void reservation() {
		int row = getSelectedRow();
		if (row < 0) {
			GUIComponents.noneSelectedDialog();
			return;
		}
		String productname = (String) getValueAt(row, 1);
		Integer ersatzteilid = (Integer) getValueAt(row, 0);
		
		ReservationDialog reservDialog = new ReservationDialog(productname, ersatzteilListe, reservationListe, ersatzteilid);
		reservDialog.setVisible(true);
	}
}
