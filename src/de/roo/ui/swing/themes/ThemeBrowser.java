package de.roo.ui.swing.themes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.roo.BuildConstants;
import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;
import de.roo.themes.ITheme;
import de.roo.themes.ITheme.ThemeDesc;
import de.roo.themes.Themes;
import de.roo.ui.swing.HTMLRichTextPane;
import de.roo.ui.swing.util.DropBoxLayout;
import de.roo.util.stream.StreamCopy;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ThemeBrowser {

	
	static final String confKey = "Presentation_Template";
	
	Map<ITheme, ThemeView> themeToView = new HashMap<ITheme, ThemeView>();
	private List<ITheme> themesList;
	
	ThmBrPane pane;

	private ThemeView curSelThemeView = null;

	private ILog log;
	
	public ThemeBrowser(IConf conf, ILog log) {
		this.log = log;
		themesList = Themes.getThemesAsList();
		pane = new ThmBrPane(themesList);
		load(conf);
	}

	public void load(IConf conf) {
		String themeTag = conf.getValueString(confKey, BuildConstants.DEFAULT_PRESENTATION_THEME);
		ITheme theme = Themes.getThemesAsMap().get(themeTag);
		if (curSelThemeView != null) {
			curSelThemeView.getSelector().setSelected(false);
		}
		if (theme != null) {
			curSelThemeView = themeToView.get(theme);
			curSelThemeView.getSelector().setSelected(true);
		} else {
			curSelThemeView = null;
		}
	}
	
	public void commit(IWritableConf conf) {
		
		if (curSelThemeView != null) {
			String tplKey = Themes.getDesc(curSelThemeView.getTheme()).key();
			conf.setValue(confKey, tplKey);
		}
		
	}

	public JComponent getComponent() {
		return pane;
	}
	
	class ThmBrPane extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7761477796379657435L;

		public ThmBrPane(List<ITheme> themes) {
			DropBoxLayout l = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
			l.setGap(2, 2);
			this.setLayout(l);
			
			ButtonGroup bg = new ButtonGroup();
			for (ITheme thm : themes) {
				try {
					ThemeView view = new ThemeView(thm);
					bg.add(view.getSelector());
					this.add(view);
					themeToView.put(thm, view);
				} catch (Exception ex) {
					log.error(this, "Could not add the theme " + thm + " to the theme list.", ex);
				}
			}
			
		}
		
	}
	
	class ThemeView extends JPanel {
	
		static final int MAX_PREVIEW_IMG_WIDTH = 120;
		
		JRadioButton selector = new JRadioButton();

		private final ITheme thm;
		
		public ThemeView(final ITheme thm) {
			
			this.thm = thm;
			
			selector.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					curSelThemeView = ThemeView.this;
				}
			});
			
			JLabel prevImgLbl = new JLabel();
			ImageIcon img = getImage(thm.getPreviewImage());
			if (img != null) prevImgLbl.setIcon(img);
			else prevImgLbl.setText("[No image]");
			
			ThemeDesc desc = Themes.getDesc(thm);
			
			HTMLRichTextPane descView = new HTMLRichTextPane(log);
			descView.setText("<html><h3>" + desc.name() + "</h3>"
					+ "<p>Author: " + desc.author() + "</p>"
					+ "<p>" + desc.desc() + "</p>");
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0;
			c.weighty = 1;
			this.add(selector, c);
			
			c.gridx = 1;
			this.add(prevImgLbl, c);
			
			c.gridx = 2;
			c.weightx = 1;
			this.add(descView, c);
			
		}
		
		private ImageIcon getImage(InputStream is) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				new StreamCopy().copy(is, os);
			} catch (IOException e) {
				log.error(this, "Could not load theme preview", e);
				return null;
			}
			return new ImageIcon(os.toByteArray());
		}

		public ITheme getTheme() {
			return thm;
		}
		
		public JRadioButton getSelector() {
			return selector;
		}

		private static final long serialVersionUID = 1309651134949874259L;
	
		
	
	}
	
}
