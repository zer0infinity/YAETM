package ch.hsr.i.yaetm.ui.tables.celleditor;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.jdesktop.swingx.JXDatePicker;

public class DatumCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	private static final long serialVersionUID = 3647271006762734474L;
	
	private JXDatePicker datePicker;
	
	public DatumCellEditor() {
		datePicker = new JXDatePicker();
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date date = null;
		try {
			date = sdf.parse(String.valueOf(value));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		datePicker.setDate(date);
		if(value != null && value instanceof Date) {
			datePicker.setDate((Date) value);
		}
		return datePicker;
	}
	
	@Override
	public Object getCellEditorValue() {
		return datePicker.getDate();
	}
}
