package de.roo.ui.swing.util;

import java.awt.Component;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;

/**
 * DropBoxLayout
 * 
 * @author Leo Nobach
 */
public class DropBoxLayout implements LayoutManager {

	/**
	 * Directs the flow of objects horizontally
	 */
	public static final int ORIENTATION_HORIZONTAL = 0;
	
	/**
	 * Directs the flow of objects vertically
	 */
	public static final int ORIENTATION_VERTICAL = 1;
	
	/**
	 * Aligns the objects from top to bottom or from left to right
	 */
	public static final int ALIGNMENT_LEADING = 2;
	
	/**
	 * Aligns the objects from bottom to top or from right to left
	 */
	public static final int ALIGNMENT_TRAILING = 3;
	
	/**
	 * If there is no more flow space, the line of objects is wrapped
	 */
	public static final int MODE_WRAP = 4;
	
	/**
	 * The objects do not wrap and fill the entire space that would 
	 * be left to wrap.
	 */
	public static final int MODE_FILL = 5;
	
	protected final int mode;
	protected final int orientation;
	protected final int alignment;
	Dimension gap = new Dimension(0,0);

	private boolean forceFillSz;
	
	/**
	 * Creates a new DropBoxLayout with the given mode, orientation and alignment
	 * @param mode
	 * @param orientation
	 * @param alignment
	 */
	public DropBoxLayout(final int mode, final int orientation, final int alignment) {
		
		if (orientation != 0 && orientation != 1) throw new IllegalArgumentException("orientation must be ORIENTATION_HORIZONTAL = 0 or ORIENTATION_VERTICAL = 1");
		if (alignment != 2 && alignment != 3) throw new IllegalArgumentException("alignment must be ALIGNMENT_LEADING = 2 or ALIGNMENT_TRAILING = 3");		
		if (mode != 4 && mode != 5) throw new IllegalArgumentException("mode must be MODE_WRAP = 4 or MODE_FILL = 5");
		
		this.mode = mode;
		this.orientation = orientation;
		this.alignment = alignment;
	}
	
	/**
	 * Creates a new DropBoxLayout with the given mode, orientation and ALIGNMENT_LEADING
	 * @param mode
	 * @param orientation
	 */
	public DropBoxLayout(final int mode, final int orientation) {
		this(mode, orientation, ALIGNMENT_LEADING);
	}
	
	/**
	 * Creates a new DropBoxLayout with the given mode, ORIENTATION_HORIZONTAL and ALIGNMENT_LEADING
	 * 
	 * @param mode
	 */
	public DropBoxLayout(final int mode) {
		this(mode, ORIENTATION_HORIZONTAL, ALIGNMENT_LEADING);
	}

	/**
	 * Creates a new DropBoxLayout with MODE_WRAP, ORIENTATION_HORIZONTAL and ALIGNMENT_LEADING
	 */
	public DropBoxLayout() {
		this(MODE_WRAP, ORIENTATION_HORIZONTAL, ALIGNMENT_LEADING);
	}
	
	public void setGap(Dimension gap) {
		this.gap = gap;
	}
	
	public void setGap(int width, int height) {
		this.gap = new Dimension(width, height);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		if (mode == MODE_WRAP) return getSizeWrapped(parent);
		return getSizeNoWrap(parent);
	}

	@Override
	public void layoutContainer(Container parent) {
		if (mode == MODE_WRAP) generateLayoutWrapped(parent);
		else generateLayoutNoWrap(parent);
	}
	
	public void generateLayoutNoWrap(Container parent) {
		synchronized (parent.getTreeLock()) {
			FlowDimension gap = new FlowDimension(this.gap);
			FlowDimension parentSz = getParentDims(parent);
			
			int flowOffset = gap.getFlowDir();
			
			int count = parent.getComponentCount();
			
			int maxPrefSize = 0;
			for (int i = 0; i<count; i++) {
				Component comp = parent.getComponent(i);
				if (comp.isVisible()) {
					FlowDimension pref = new FlowDimension(comp.getPreferredSize());
					if (pref.getWrapDir() > maxPrefSize) maxPrefSize = pref.wrapDir;
				}
			}
			
			int suggSz = parentSz.getWrapDir()-2*gap.getWrapDir();
			
			int wrapSz = forceFillSz?suggSz:Math.max(suggSz,maxPrefSize);
			
			for (int i = 0; i<count; i++) {
				Component comp = parent.getComponent(i);
				if (comp.isVisible()) {
					FlowDimension pref = new FlowDimension(comp.getPreferredSize());
					Dimension size = new FlowDimension(pref.getFlowDir(), wrapSz).toDimension();
					comp.setSize(size);
					Point location = new FlowPosition(flowOffset, gap.getWrapDir()).toLocation(comp, parent);
					comp.setLocation(location);
					flowOffset += pref.getFlowDir() + gap.getFlowDir();
				}
			}
		}
	}
	
	public void setForceFillSize(boolean forceFillSz) {
		this.forceFillSz = forceFillSz;
	}
	
	public Dimension getSizeNoWrap(Container parent) { 
		synchronized (parent.getTreeLock()) {
			FlowDimension gap = new FlowDimension(this.gap);
			//FlowDimension insetDims = getInsetDims(parent);
			
			int flowOffset = gap.getFlowDir();
			
			int count = parent.getComponentCount();
			
			int maxPrefSize = 0;
			for (int i = 0; i<count; i++) {
				Component comp = parent.getComponent(i);
				if (comp.isVisible()) {
					FlowDimension pref = new FlowDimension(comp.getPreferredSize());
					if (pref.getWrapDir() > maxPrefSize) maxPrefSize = pref.wrapDir;
					flowOffset += pref.getFlowDir() + gap.getFlowDir();
				}
			}
			
			Dimension result = new FlowDimension(flowOffset, maxPrefSize+2*gap.getWrapDir()).toDimension();
			Insets insets = parent.getInsets();
			result.height += insets.top + insets.bottom;
			result.width += insets.left + insets.right;
			return result;
		}
	}
	
	public void generateLayoutWrapped(Container parent) {
		synchronized(parent.getTreeLock()) {
			FlowDimension gap = new FlowDimension(this.gap);
			//FlowDimension insetDims = getInsetDims(parent);
			FlowDimension parentSz = getParentDims(parent);
			
			int flowOffset = 0;
			int wrapOffset = gap.getWrapDir();
			int largestWrapSz = 0;
			int currentRowStartElement = 0;
			int largestTotalFlowSz = 0;
			
			int count = parent.getComponentCount();
			boolean firstComponentInRow = true;
			for (int i = 0; i<count; i++) {
				Component comp = parent.getComponent(i);
				if (comp.isVisible()) {
					FlowDimension dim = new FlowDimension(comp.getPreferredSize());
					int flowSz = dim.getFlowDir();
					if (flowOffset + flowSz + 2*gap.getFlowDir() > parentSz.getFlowDir() && !firstComponentInRow) {
		
						positionComponents(parent, currentRowStartElement, i, largestWrapSz, gap, wrapOffset);
						
						//wrap around
						flowOffset = 0;
						wrapOffset += largestWrapSz + gap.getWrapDir();
						
						currentRowStartElement = i;
						//firstComponentInRow = true;
						if (largestTotalFlowSz < flowOffset) largestTotalFlowSz = flowOffset;
					}
					firstComponentInRow = false;
					int wrapSz = dim.getWrapDir();
					if (largestWrapSz < wrapSz) largestWrapSz = wrapSz;
					flowOffset += flowSz + gap.getFlowDir();
				}
			}
			
			positionComponents(parent, currentRowStartElement, count, largestWrapSz, gap, wrapOffset);
		}
	}
	
	public Dimension getSizeWrapped(Container parent) {
		synchronized(parent.getTreeLock()) {
			FlowDimension gap = new FlowDimension(this.gap);
			//FlowDimension insetDims = getInsetDims(parent);
			FlowDimension parentSz = getParentDims(parent);
			
			int flowOffset = 0;
			int wrapOffset = gap.getWrapDir();
			int largestWrapSz = 0;
			int largestTotalFlowSz = 0;
			
			int count = parent.getComponentCount();
			boolean firstComponentInRow = true;
			for (int i = 0; i<count; i++) {
				Component comp = parent.getComponent(i);
				if (comp.isVisible()) {
					FlowDimension dim = new FlowDimension(comp.getPreferredSize());
					int flowSz = dim.getFlowDir();
					if (flowOffset + flowSz + 2*gap.getFlowDir() > parentSz.getFlowDir() && !firstComponentInRow) {
		
						//wrap around
						flowOffset = 0;
						wrapOffset += largestWrapSz + gap.getWrapDir();
						
						//firstComponentInRow = true;
						if (largestTotalFlowSz < flowOffset) largestTotalFlowSz = flowOffset;
					}
					firstComponentInRow = false;
					int wrapSz = dim.getWrapDir();
					if (largestWrapSz < wrapSz) largestWrapSz = wrapSz;
					flowOffset += flowSz + gap.getFlowDir();
				}
			}
			
			if (largestTotalFlowSz < flowOffset) largestTotalFlowSz = flowOffset;
			
			Dimension result = new FlowDimension(largestTotalFlowSz + gap.getFlowDir(), 
					wrapOffset + largestWrapSz + gap.getWrapDir()).toDimension();
			Insets insets = parent.getInsets();
			result.height += insets.top + insets.bottom;
			result.width += insets.left + insets.right;
			return result;
		}
		
	}
	
	private FlowDimension getParentDims(Container parent) {
		Insets insets = parent.getInsets();
		
		Dimension size = parent.getSize();
		Dimension sizeMinusInsets = new Dimension(size.width - insets.left - insets.right,
				size.height - insets.top - insets.bottom);
		return new FlowDimension(sizeMinusInsets);
	}

	/*
	protected FlowDimension getInsetDims(Container comp) {
		Insets insets = comp.getInsets();
		return new FlowDimension(new Dimension(insets.left + insets.right, insets.top + insets.bottom));
	}
	*/
	
	public void positionComponents(Container parent, int startIdx, int endIdx, int largestWrapSz, FlowDimension gap, int wrapOffset) {
		int flowOffset = gap.getFlowDir();
		for (int i = startIdx; i < endIdx; i++) {
			Component comp = parent.getComponent(i);
			if (comp.isVisible()) {
				FlowDimension dim = new FlowDimension(comp.getPreferredSize());
				comp.setSize(new FlowDimension(dim.getFlowDir(), largestWrapSz).toDimension());
				comp.setLocation(new FlowPosition(flowOffset, wrapOffset).toLocation(comp, parent));
				flowOffset += gap.getFlowDir() + dim.getFlowDir();
			}
		}
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		//Nothing to do
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		//Nothing to do
	}
	
	protected class FlowDimension {
		
		public String toString() {
			return "FlowDimension(flow=" + flowDirSz + ", wrap=" + wrapDir + ")";
		}
		
		public FlowDimension(int flowDirSz, int wrapDirSz) {
			super();
			this.flowDirSz = flowDirSz;
			this.wrapDir = wrapDirSz;
		}
		
		public FlowDimension(Dimension dim) {
			super();
			
			if (orientation == ORIENTATION_HORIZONTAL) {
				this.flowDirSz = dim.width;
				this.wrapDir = dim.height;
			} else {
				this.flowDirSz = dim.height;
				this.wrapDir = dim.width;
			}
		}
		
		public int getFlowDir() {
			return flowDirSz;
		}
		public void setFlowDir(int flowDirSz) {
			this.flowDirSz = flowDirSz;
		}
		public int getWrapDir() {
			return wrapDir;
		}
		public void setWrapDir(int wrapDirSz) {
			this.wrapDir = wrapDirSz;
		}
		int flowDirSz;
		int wrapDir;
		
		public Dimension toDimension() {
			return orientation == ORIENTATION_HORIZONTAL?
					new Dimension(this.getFlowDir(), this.getWrapDir()):
						new Dimension(this.getWrapDir(), this.getFlowDir());
		}
		
	}
	
	protected class FlowPosition {
		
		public FlowPosition(int flowDir, int wrapDir) {
			super();
			this.flowDir = flowDir;
			this.wrapDir = wrapDir;
		}
		public int getFlowDir() {
			return flowDir;
		}
		public void setFlowDir(int flowDir) {
			this.flowDir = flowDir;
		}
		public int getWrapDir() {
			return wrapDir;
		}
		public void setWrapDir(int wrapDir) {
			this.wrapDir = wrapDir;
		}
		int flowDir;
		int wrapDir;
		
		public Point toLocation(Component comp, Container parent) {
			Insets insets = parent.getInsets();
			boolean h = orientation == ORIENTATION_HORIZONTAL;
			int x = (h?flowDir:wrapDir);
			int y = (h?wrapDir:flowDir);
			if (alignment == ALIGNMENT_TRAILING) {
				x = h?parent.getWidth()-insets.right-x-comp.getWidth()
						:x+insets.left;
				y = h?y+insets.top
						:parent.getHeight()-insets.bottom-y-comp.getHeight();
			} else {
				x += insets.left;
				y += insets.top;
			}
			
			return new Point(x,y);
		}
		
	}
	
	
	
	
	

}
