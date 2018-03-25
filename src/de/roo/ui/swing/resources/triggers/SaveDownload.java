package de.roo.ui.swing.resources.triggers;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;
import de.roo.model.uiview.IRooDownloadResource;
import de.roo.ui.swing.menu.triggers.AddUpload;
import de.roo.ui.swing.menu.triggers.Trigger;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SaveDownload extends Trigger {

	public static final ImageIcon ICON = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/buttons/Save.png"));
		
		private ILog log;
		private Component parent;
		private IRooDownloadResource res;

		private IWritableConf conf;
		
		private static final String SETTINGS_KEY = "SaveDownloadCurrentDir";

		public SaveDownload(IRooDownloadResource res, ILog log, Component parent, IWritableConf conf) {
			this.log = log;
			this.parent = parent;
			this.res = res;
			this.conf = conf;
		}

		@Override
		public void execute() {
			final JFileChooser fc = new JFileChooser();
			File sugg = getSuggestedFilename();
			log.dbg(this, "Suggesting file path " + sugg + " for saving.");
			
			fc.setSelectedFile(sugg);
			
			String curDir = conf.getValueString(SETTINGS_KEY, null);
			if (curDir != null) fc.setCurrentDirectory(new File(curDir));
			
			int returnVal = fc.showSaveDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            log.dbg(this, "File selected for saving: " + file);
	            saveToFile(file);
	            saveSettings(fc);
	        } else {
	        	log.dbg(this, "Open command cancelled by user.");
	        }
		}
		
		public File getSuggestedFilename() {
			return new File(new File("."), res.getPlainFileName());
		}

		private void saveToFile(File target) {
			res.saveResourceTo(target, log);
		}

		@Override
		public String getDesc() {
			return "Save Download...";
		}
		
		private void saveSettings(JFileChooser fc) {
			conf.setValue(SETTINGS_KEY, fc.getCurrentDirectory().getAbsolutePath());
		}

		@Override
		public Icon getIcon() {
			return ICON;
		}


}
