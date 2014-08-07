package ch.hsr.i.yaetm.ui.tables.celleditor;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class PriceCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	private static final long serialVersionUID = -4471128060885726786L;
	
	private JTextField textfield;
	
	public PriceCellEditor() {
		textfield = new JTextField();
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, final Object value, boolean isSelected, int row, int column) {
		textfield.setText(String.valueOf(value));
		textfield.setHorizontalAlignment(JTextField.RIGHT);
		textfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int k = e.getKeyChar();
				/*
				 * 48-57: 0-9 AND 46: . <- point
				 */
				if ((!((47 < k && k < 58) || k == 46)) || (k==46 && textfield.getText().contains("."))) {
					e.setKeyChar((char) KeyEvent.VK_CLEAR);
				}
			}
		});
		textfield.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textfield.selectAll();
			}
		});
		return textfield;
	}

	@Override
	public Object getCellEditorValue() {
		return textfield.getText().trim();
	}
}