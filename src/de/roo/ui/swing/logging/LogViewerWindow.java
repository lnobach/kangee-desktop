package de.roo.ui.swing.logging;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import de.roo.configuration.IWritableConf;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.HistoryLog;
import de.roo.logging.ObjectLog.LogEntry;
import de.roo.logging.ObjectLog.LogType;
import de.roo.ui.swing.RooFrame;
import de.roo.ui.swing.util.ConfToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
class LogViewerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 320162383738342273L;
	
	public static final ImageIcon ICON_DBG = new ImageIcon(RooFrame.class.getResource("/de/roo/gfx/notifications/Info.png"));
	public static final ImageIcon ICON_WARN = new ImageIcon(RooFrame.class.getResource("/de/roo/gfx/notifications/Warning.png"));
	public static final ImageIcon ICON_ERROR = new ImageIcon(RooFrame.class.getResource("/de/roo/gfx/notifications/Error.png"));
	
	JTable t;
	JScrollPane tsp;

	private LogViewer viewer;
	LogEntryViewer enViewer;
	LogViewerTableModel mdl;
	private IWritableConf conf;
	JButton saveToBtn;

	private HistoryLog log;
	
	static final double LIST_WEIGHT = 1.2d;

	private static final String WINDOW_ID = "LogViewerWindow";
	
	public LogViewerWindow(HistoryLog log, LogViewer viewer, IWritableConf conf) {
		this.viewer = viewer;
		this.conf = conf;
		this.log = log;
		
		this.addWindowListener(this.new WindowListenerImpl());
		this.setTitle("Log Browser");
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		this.setLayout(new BorderLayout());
		
		if (conf == null || !ConfToolkit.loadSettings(this, conf, WINDOW_ID, log));
			this.setSize(800, 600);
		
		mdl = this.new LogViewerTableModel(log);
		
		t = new JTable(mdl);
		t.setDefaultRenderer(Object.class, this.new LogTableCellRenderer());
		t.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = t.getSelectedRow();
				if (index >= 0) showDetails(mdl.getEntryAt(index));
			}
			
		});
		arrangeColumns();
		tsp = new JScrollPane(t);
		
		enViewer = new LogEntryViewer(log, null);
		
		JSplitPane spMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tsp, new JScrollPane(enViewer));
		spMain.setDividerLocation(300);
		
		this.add(spMain, BorderLayout.CENTER);
		
		JPanel btnPane = new JPanel();
		btnPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		saveToBtn = new JButton("Save log...");
		saveToBtn.addActionListener(this.new LogSaver());
		btnPane.add(saveToBtn);
		this.add(btnPane, BorderLayout.SOUTH);
		
		this.setVisible(true);
		adjustScrollbars();
	}

	private void arrangeColumns() {
		TableColumn colIcon = t.getColumnModel().getColumn(0);
		colIcon.setMinWidth(10);
		colIcon.setMaxWidth(20);
		TableColumn colSource = t.getColumnModel().getColumn(1);
		colSource.setMinWidth(100);
		colSource.setMaxWidth(500);
	}
	
	class LogViewerTableModel extends AbstractTableModel {

		final List<LogEntry> entries;

		public LogViewerTableModel(HistoryLog log) {
			Collection<LogEntry> src = log.getEntryHistory();
			entries = new ArrayList<LogEntry>(src.size()*2);
			entries.addAll(src);
		}
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2294497224261147408L;

		@Override
		public String getColumnName(int column) {
			if (column == 0) return null;
			if (column == 1) return "Source";
			return "Message";
		}
		
		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return entries.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			LogEntry e = entries.get(rowIndex);
			if (columnIndex == 0) return getIconFor(e);
			if (columnIndex == 1) return e.getSource();
			return e.getMessage();
		}

		public void addEntry(LogEntry e) {
			entries.add(e);
			int updIndex = entries.size() -1;
			LogViewerTableModel.this.fireTableRowsInserted(updIndex, updIndex);
		}
		
		public LogEntry getEntryAt(int i) {
			return entries.get(i);
		}
		
	}
	
	public class LogTableCellRenderer extends DefaultTableCellRenderer {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 2912842386217124103L;

		public Component getTableCellRendererComponent(JTable table, Object value,
	                boolean isSelected, boolean hasFocus, int row, int column) {

			if (value instanceof ImageIcon) return new JLabel((ImageIcon) value);
			
	        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	    }
	}

	
	public void addEntry(LogEntry e) {
		mdl.addEntry(e);
		adjustScrollbars();
	}
	
	ImageIcon getIconFor(LogEntry e) {
		if (e.getType() == LogType.Debug) return ICON_DBG;
		if (e.getType() == LogType.Warn) return ICON_WARN;
		return ICON_ERROR;
	}
	
	class WindowListenerImpl extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			viewer.windowClosed();
		}
		
	}
	
	public synchronized void showDetails(LogEntry e) {
		enViewer.showEntry(e);
	}
	
	int lastPosDiff = -1;
	
	public void adjustScrollbars() {
		t.scrollRectToVisible(t.getCellRect(t.getRowCount()-1, 0, true));
	}
	
	public void saveSettings() {
		//System.out.println("Saving settings to config " + conf);
		if (conf != null) ConfToolkit.saveSettings(this, conf, WINDOW_ID);
	}
	
	class LogSaver implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == saveToBtn) openSaveDlg();
		}
		
		void openSaveDlg() {
			final JFileChooser fc = new JFileChooser();
//			fc.setCurrentDirectory(new File(Config.getValue(CFG_LAST_PATH, "")));
			fc.setSelectedFile(new File("kangee.log"));
			int returnVal = fc.showSaveDialog(LogViewerWindow.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            saveFile(file);
	        }
		}

		private void saveFile(File file) {
			try {
				FileWriter wr = new FileWriter(file);
				BufferedWriter buf = new BufferedWriter(wr);
				log.writeTo(buf);
				buf.close();
				wr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
}
