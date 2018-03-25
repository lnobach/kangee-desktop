package de.roo.ui.swing.menu.triggers;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import de.roo.configuration.IWritableConf;
import de.roo.engine.FileHandler;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class AddUpload extends Trigger {

	public static final ImageIcon ICON = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/buttons/AddUL.png"));
	
	private ILog log;
	private Component parent;
	private FileHandler hdlr;
	
	private static final String SETTINGS_KEY = "AddUploadCurrentDir";

	private IWritableConf conf;

	public AddUpload(FileHandler hdlr, ILog log, Component parent, IWritableConf conf) {
		this.log = log;
		this.parent = parent;
		this.hdlr = hdlr;
		this.conf = conf;
	}

	@Override
	public void execute() {
		final JFileChooser fc = new JFileChooser();

		String curDir = conf.getValueString(SETTINGS_KEY, null);
		if (curDir != null) fc.setCurrentDirectory(new File(curDir));
		
		int returnVal = fc.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            log.dbg(this, "File selected for opening: " + file);
            loadFile(file);
            saveSettings(fc);
        } else {
        	log.dbg(this, "Open command cancelled by user.");
        }
	}

	private void loadFile(File file) {
		hdlr.publishFile(file);
	}

	@Override
	public String getDesc() {
		return "Add Upload...";
	}
	
	private void saveSettings(JFileChooser fc) {
		conf.setValue(SETTINGS_KEY, fc.getCurrentDirectory().getAbsolutePath());
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

}
