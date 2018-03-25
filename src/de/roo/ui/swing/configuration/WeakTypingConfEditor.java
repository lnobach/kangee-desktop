package de.roo.ui.swing.configuration;

import java.awt.Component;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import de.roo.util.CollectionUtils;
import de.roo.util.Tuple;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class WeakTypingConfEditor {

	private IConfigurationContext ctx;
	private Map<String, Class<?>> types;
	private List<Tuple<String, String>> confSer;
	
	JTable table;
	JScrollPane sp;
	
	int restartReqs = 0;

	public WeakTypingConfEditor(IConfigurationContext ctx, Map<String, Class<?>> types) {
		this.ctx = ctx;
		this.types = types;
		refresh();
		
		table = new JTable(new TableModelImpl());
		table.setDefaultEditor(Object.class, this.new TableCellEditorImpl());
		sp = new JScrollPane(table);
		
	}
	
	public void refresh() {
		confSer = Tuple.getTupleListFromMap(ctx.getConf().getAsMap());
	}
	
	public JComponent getComponent() {
		return sp;
	}
	
	
	class TableModelImpl extends AbstractTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1754090988189133385L;

		@Override
	    public String getColumnName(int column) {
	    	if (column == 0) return "Name";
	    	else return "Value";
	    }
		
		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return confSer.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Tuple<String, String> tpl = confSer.get(rowIndex);
			
			if (columnIndex == 0) return tpl.getA();
			else return tpl.getB();
		}
		
		@Override
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return columnIndex != 0;
	    }
		
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			//System.out.println("Setting value to " + aValue);
			Tuple<String, String> tpl = confSer.get(rowIndex);
			tpl.setB((String)aValue);
			ctx.getConf().setValue(tpl.getA(), (String)aValue);
			
	    }
		
	}
	
	class TableCellEditorImpl implements TableCellEditor {
		
		TableCellEditor currentEditor = null;
		CellEditorListener currentListener = null;
		Class<?> currentType;
		
		private List<CellEditorListener> listeners = new LinkedList<CellEditorListener>();
		
		final JComboBox trueFalseCombobox = new JComboBox(new String[] {"true", "false"});
		
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			//System.out.println("Calling getTableCellEditorComponent(" + table + ", " + value + ", " + isSelected + ", " + row + ", " + column + ")");
			
			Tuple<String, String> t = confSer.get(row);
			currentType = types.get(t.getA());
			
			currentEditor = getNewEditorFor(currentType);
			currentListener = new CellEditorListener() {
				
				@Override
				public void editingStopped(ChangeEvent e) {
					if (mayStopEditing()) {
						for (CellEditorListener l : CollectionUtils.copy(listeners)) l.editingStopped(e);
						//disposeCurrentEditor();
					}
				}
				
				@Override
				public void editingCanceled(ChangeEvent e) {
					for (CellEditorListener l : CollectionUtils.copy(listeners)) l.editingCanceled(e);
					disposeCurrentEditor();
				}
			};
			currentEditor.addCellEditorListener(currentListener);
			return currentEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		private TableCellEditor getNewEditorFor(Class<?> type) {
			//System.out.println("Calling getNewEditorFor(" + type + ")");
			
			if (type == Boolean.class) return new DefaultCellEditor(trueFalseCombobox);
			return new DefaultCellEditor(new JTextField());
		}

		@Override
		public void addCellEditorListener(CellEditorListener l) {
			//System.out.println("Calling addCellEditorListener(" + l + ")");
			listeners.add(l);
		}

		@Override
		public void cancelCellEditing() {
			//System.out.println("Calling cancelCellEditing()");
			currentEditor.cancelCellEditing();
			disposeCurrentEditor();
		}
		
		private void disposeCurrentEditor() {
			//System.out.println("Calling disposeCurrentEditor()");
			currentEditor.removeCellEditorListener(currentListener);
			currentEditor = null;
		}

		@Override
		public Object getCellEditorValue() {
			//System.out.println("Calling getCellEditorValue()");
			return currentEditor.getCellEditorValue();
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			//System.out.println("Calling isCellEditable(" + anEvent + ")");
			//return currentEditor.isCellEditable(anEvent);
			return true;
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {
			//System.out.println("Calling removeCellEditorListener(" + l + ")");
			listeners.remove(l);
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			//System.out.println("Calling shouldSelectCell(" + anEvent + ")");
			return true;
		}

		@Override
		public boolean stopCellEditing() {
			//System.out.println("Calling stopCellEditing()");
			if (!mayStopEditing()) return false;
			if (currentEditor.stopCellEditing()) {
				disposeCurrentEditor();
				return true;
			}
			return false;
		}
		
		boolean mayStopEditing() {
			try {
				if (currentType == Integer.class) Integer.parseInt((String)currentEditor.getCellEditorValue());
			} catch (NumberFormatException ex) {
				return false;
			}
			return true;
		}
		
	}

	public int getRestartReqs() {
		int restartReqsToReturn = restartReqs;
		restartReqs = 0;
		return restartReqsToReturn;
	}
	
}
