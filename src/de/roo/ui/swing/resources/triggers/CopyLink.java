package de.roo.ui.swing.resources.triggers;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.roo.connectivity.ConnectivityToolkit;
import de.roo.model.ILocalModeProvider;
import de.roo.model.RooUploadResource;
import de.roo.model.uiview.IRooResource;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.ui.swing.resources.RooResourceView;
import de.roo.ui.swing.util.ClipboardToolkit;
import de.roo.util.HashToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class CopyLink extends Trigger {

	public static final ImageIcon ICON = new ImageIcon(RooResourceView.class.getResource("/de/roo/gfx/buttons/Copy.png"));
	
	private IRooResource res;
	private RooEngineGUI eng;
	private ILocalModeProvider loc;

	public CopyLink(IRooResource res, RooEngineGUI eng, ILocalModeProvider loc) {
		this.res = res;
		this.eng = eng;
		this.loc = loc;
	}
	
	@Override
	public void execute() {
		if (res instanceof RooUploadResource && eng.getConfiguration().getValueBoolean("copyHashToo", false)) {
			RooUploadResource ulRes = (RooUploadResource)res;
			String mdType = eng.getConfiguration().getValueString("hashDigestMethod", "MD5");
			String hash;
			try {
				hash = HashToolkit.convertToHex(ulRes.getHash(mdType, eng.getLog()), false);
			} catch (Exception e) {
				eng.getLog().error(this, "Error while computing hash for file " + res.getFile()
						+ " with digest method " + mdType, e);
				hash = "(error while hash was computed)";
			}
			ClipboardToolkit.copyToClipboard(getURL().toExternalForm() + "# " + mdType + ": " + hash);
		} else
			ClipboardToolkit.copyToClipboard(getURL());
	}
	
	//TODO: CONNREFRESH
	public URL getURL() {
		return ConnectivityToolkit.generateDownloadURL(
				eng.getConnStateProvider().getCurrentConnectivityInfo(), eng.getConfiguration(), res,
				eng.getLog(), loc.isLocalMode());
	}

	@Override
	public String getDesc() {
		return "Copy Link";
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

}
