package ch.hsr.i.yaetm.ui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.SortOrder;

import ch.hsr.i.yaetm.logic.Historyliste;
import ch.hsr.i.yaetm.models.HistoryTableModel;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;
import ch.hsr.i.yaetm.ui.utils.GUIComponents;

public class HistoryTable extends JXTable {
	
	private static final long serialVersionUID = 1L;
	
	private Historyliste HistoryListe;
	
	public HistoryTable(HistoryTableModel historyModel, Historyliste HistoryListe) {
		super(historyModel);
		this.HistoryListe = HistoryListe;
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setComponentPopupMenu(createPopupMenu());
		setSortOrder(0, SortOrder.DESCENDING);
		getColumn(0).setPreferredWidth(3);
		setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));
		setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
		getColumnModel().getColumn(0).setPreferredWidth(40);
		getColumnModel().getColumn(1).setPreferredWidth(120);
		getColumnModel().getColumn(2).setPreferredWidth(90);
		getColumnModel().getColumn(3).setPreferredWidth(50);
		getColumnModel().getColumn(4).setPreferredWidth(100);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					refreshTable();
				} else if (e.getKeyChar() == KeyEvent.VK_DELETE) {
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
		final String remove = "löschen";
		final String[] itemLabel = { remove };
		for (int i = 0; i < itemLabel.length; i++) {
			if(i == 1 && YAETMMainView.PRIVILEGE == YAETMMainView.ADMIN) {
				popupMenu.addSeparator();
			}
			JMenuItem menuItem = new JMenuItem(itemLabel[i]);
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					if (event.getActionCommand() == remove) {
						remove();
					}
				}
			});
			popupMenu.add(menuItem);
		}
		return popupMenu;
	}
	
	/**
	 * refresh table
	 */
	private void refreshTable() {
		HistoryListe.ladeFromString("");
	}
	
	/**
	 * reservation löschen
	 */
	public void remove() {
		int row = getSelectedRow();
		if (0 <= row) {
			int result = GUIComponents.yesNoDialog(
					"Löschen bestätigen",
					"Soll " + (String) getValueAt(row, 1) + " gelöscht werden?");
			switch (result) {
			case JOptionPane.YES_OPTION:
				HistoryListe.remove((Integer)getValueAt(getSelectedRow(), 0));
				break;
			case JOptionPane.NO_OPTION:
				return;
			}
		} else {
			GUIComponents.noneSelectedDialog();
		}
	}
}
