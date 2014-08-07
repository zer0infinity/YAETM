package ch.hsr.i.yaetm.ui.tables.celleditor;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ComboBoxEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 7304282969329040856L;
	
	private JComboBox comboBox;

	public ComboBoxEditor(String[] items) {
	      comboBox = new JComboBox(items);
    }

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		comboBox.setSelectedItem(String.valueOf(value));
		return comboBox;
	}

	@Override
	public Object getCellEditorValue() {
		return (String)comboBox.getSelectedItem();
	}
}
