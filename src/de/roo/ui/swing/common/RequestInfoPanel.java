package de.roo.ui.swing.common;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.roo.srvApi.IRequest;
import de.roo.ui.swing.exLAF.UIConstants;
import de.roo.ui.swing.exLAF.common.RooScrollPaneUI;
import de.roo.ui.swing.exLAF.detail.RooDetailViewRequestDetailsUI;
import de.roo.util.Tuple;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RequestInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8331551667112362161L;
	
	JTextArea headersTable;
	
	public RequestInfoPanel(IRequest req) {
		
		this.setUI(new RooDetailViewRequestDetailsUI());
		
		headersTable = new JTextArea();
		headersTable.setFont(UIConstants.defaultFontMonospace);
		headersTable.setEditable(false);
		headersTable.setText(serializeRequest(req));
		headersTable.setLineWrap(false);
		
		this.setLayout(new BorderLayout());
		
		JScrollPane sp = new JScrollPane(headersTable);
		sp.setUI(new RooScrollPaneUI());
		
		this.add(sp);
		
	}
	
	private String serializeRequest(IRequest req) {
		
		InetAddress reqIP = req.getRequesterInfo().getRequesterIP();
		
		StringBuffer allBuf = new StringBuffer();
		List<Tuple<String, String>> reqMain = new ArrayList<Tuple<String,String>>();
		reqMain.add(new Tuple<String, String>("Hostname", reqIP.getHostName()));
		reqMain.add(new Tuple<String, String>("IP Address", reqIP.getHostName()));
		reqMain.add(new Tuple<String, String>("Method", String.valueOf(req.getReqMethod())));
		reqMain.add(new Tuple<String, String>("Version", String.valueOf(req.getVersion())));
		
		allBuf.append(fancySerializeTupleList(reqMain));
		allBuf.append("\n\n" + "=== HTTP headers sent by peer ===" + "\n\n");
		
		allBuf.append(serializeHeaders(req));
		
		return allBuf.toString();
		
	}
	
	private String serializeHeaders(IRequest req) {
		List<Tuple<String, String>> hList = Tuple.getTupleListFromMap(req.getHeaders());
		return fancySerializeTupleList(hList);
	}

	private String fancySerializeTupleList(List<Tuple<String, String>> hList) {
		int maxLength = 0;
		for (Tuple<String, String> h : hList) {
			int l = h.getA().length();
			if (maxLength < l) maxLength = l;
		}
		
		StringBuffer buf = new StringBuffer();
		
		for (Tuple<String, String> h : hList) {
			buf.append(h.getA());
			buf.append(": ");
			int l = maxLength - h.getA().length();
			for (int i=0; i < l; i++) buf.append(" ");
			buf.append(h.getB());
			buf.append("\n");
		}
		
		return buf.toString();
	}
	
}
 