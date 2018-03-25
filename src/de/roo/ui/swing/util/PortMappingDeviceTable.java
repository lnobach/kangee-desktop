package de.roo.ui.swing.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import de.roo.portmapping.IPortMappingDevice;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class PortMappingDeviceTable {

	TableMdl mdl = new TableMdl();
	JTable table;
	
	public PortMappingDeviceTable() {
		table = new JTable(mdl);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(this.new MouseAdapterImpl());
		table.addKeyListener(this.new KeyAdapterImpl());
		table.getSelectionModel().addListSelectionListener(this.new ListSelectionListenerImpl());
		
		TableColumn typeCol = table.getColumnModel().getColumn(1);
		typeCol.setPreferredWidth(100);
		typeCol.setMaxWidth(120);
		
	}
	
	List<IPortMappingDevice> portMappingDevices = Collections.emptyList();
	private boolean searching = false;
	private List<IPortMappingDeviceTableListener> listeners = new LinkedList<IPortMappingDeviceTableListener>();
	
	public void setSearching() {
		this.searching = true;
		refreshMap();
	}

	public void setPortMappingDeviceList(List<IPortMappingDevice> devices) {
		this.searching = false;
		this.portMappingDevices = devices;
		refreshMap();
	}
	
	private void refreshMap() {
		mdl.fireTableDataChanged();
	}
	
	public JTable getComponent() {
		return table;
	}
	
	class TableMdl extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4531112557158185388L;

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			if (searching) return 1;
			return portMappingDevices.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			if (searching)
				if (columnIndex == 0) return "Searching...";
				else return "";
			
			IPortMappingDevice dev = portMappingDevices.get(rowIndex);
			
			if (columnIndex == 0) return dev.getName();
			else return dev.getTypeName();
		}
		
		public String getColumnName(int column) {
			if (column == 0) return "Device name";
			else return "Type";
		}
		
	}

	public IPortMappingDevice getSelectedDevice() {
		int row = table.getSelectedRow();
		if (row < 0) return null;
		return portMappingDevices.get(row);
	}

	public List<IPortMappingDevice> getDeviceList() {
		return portMappingDevices;
	}
	
	class MouseAdapterImpl extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getClickCount() >= 2) selectionConfirmed(getSelectedDevice());
		}
		
	}
	
	class KeyAdapterImpl extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) selectionConfirmed(getSelectedDevice());
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			//Nothing to do
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			//Nothing to do
		}
		
	}
	
	class ListSelectionListenerImpl implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			selectionChanged(getSelectedDevice());
		}

		
	}
	
	protected void selectionChanged(IPortMappingDevice dev) {
		for (IPortMappingDeviceTableListener l : listeners) l.selectionChanged(dev);
	}
	
	protected void selectionConfirmed(IPortMappingDevice dev) {
		for (IPortMappingDeviceTableListener l : listeners) l.selectionConfirmed(dev);
	}
	
	public static interface IPortMappingDeviceTableListener {
		
		public void selectionChanged(IPortMappingDevice dev);
		
		public void selectionConfirmed(IPortMappingDevice dev);
		
	}
	
	public void addListener(IPortMappingDeviceTableListener l) {
		listeners.add(l);
	}
	
	public void removeListener(IPortMappingDeviceTableListener l) {
		listeners.remove(l);
	}
	
}
