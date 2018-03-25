package de.roo.ui.swing.common;

import java.awt.Component;
import java.net.InetAddress;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;
import de.roo.util.InetAddressToolkit;
import de.roo.util.Tuple;
import de.roo.util.collections.CollConvert;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LanAddressChooser extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4627947298089819876L;
	
	static final String CONF_KEY = "Lan_Address_Seq_Pref";

	private IWritableConf conf;
	
	static final Object PendingObj = new Object();
	
	public LanAddressChooser(ILog log, IWritableConf conf, boolean threaded) {
		super();
		addItems(log, threaded);
		this.conf = conf;
		this.setRenderer(this.new ListCellRendererImpl());
	}
	
	public void addItems(ILog log, boolean threaded) {
		if(threaded) {
			new Thread(new AddressSeeker(log)).start();
			addItem(PendingObj);
		} else {
			for (Object addr : getChoicesRaw(log)) {
				addItem(addr);
			}
		}
			
	}
	
	public static List<CachedInetAddress> getChoicesRaw(ILog log) {
		
		return CollConvert.convertList(InetAddressToolkit.acquireAllAddrs(log), new CollConvert.Converter<Tuple<InetAddress, String>, CachedInetAddress>() {

			@Override
			public CachedInetAddress convert(Tuple<InetAddress, String> inObj) {
				return new CachedInetAddress(inObj);
			}
			
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Tuple<InetAddress, String> getSelectedLanAddr() {
		return (Tuple)this.getSelectedItem();
	}
	
	public void refreshSettings() {
		try {
			this.setSelectedIndex(Integer.parseInt(conf.getValueString(CONF_KEY, "auto")));
		} catch (NumberFormatException e) {
			//Nothing to do
		} catch (IllegalArgumentException e) {
			//Nothing to do
		}
	}
	
	public void commitChanges(boolean deselect) {
		if (deselect) {
			conf.setValue(CONF_KEY, "auto");
			return;
		}
		if (this.getItemCount() > 0 && this.getItemAt(0) != PendingObj) 
			conf.setValue(CONF_KEY, this.getSelectedIndex());
	}
	
	class ListCellRendererImpl extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -970256718692552115L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			if (value == PendingObj) 
				return super.getListCellRendererComponent(list, "Searching for addresses...", index, isSelected, cellHasFocus);
			
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
		}
		
	}
	
	static class CachedInetAddress {

		private Tuple<InetAddress, String> addr;

		String hostname;
		
		CachedInetAddress(Tuple<InetAddress, String> addr) {
			this.addr = addr;
			hostname = addr.getA().getHostName();
		}
		
		public String toString() {
			return hostname;
		}
		
		public Tuple<InetAddress, String> getAddress() {
			return addr;
		}
		
	}
	
	class AddressSeeker implements Runnable {
		
		private ILog log;

		AddressSeeker(ILog log) {
			this.log = log;
		}
		
		@Override
		public void run() {
			
			long beginTime = System.currentTimeMillis();
			log.dbg(this, "Starting separate thread for seeking available addresses...");
			List<CachedInetAddress> addrs = getChoicesRaw(log);
			long duration = System.currentTimeMillis() - beginTime;
			log.dbg(this, "Seeking of available addresses ended. Duration: " + duration + " msec.");
			LanAddressChooser.this.removeAllItems();
			for (Object o : addrs) {
				LanAddressChooser.this.addItem(o);
			}
			LanAddressChooser.this.refreshSettings();
		}
	}

}
