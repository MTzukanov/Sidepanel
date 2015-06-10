package org.vaadin.addon.sidepanel;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.JavaScript;

/**
 * Implements an animating horizontal split panel.
 * 
 * In addition, the programmer defines a "tab bar width", a margin on the right
 * panel that is always visible.
 * 
 * Because of IE limitations (calc is not supported in animations) tab bar width
 * and the whole side panel width must use the same units.
 * 
 * Also because we add constant style names this component should be used only
 * once or all the animating instances should have the same tabbar/panel width.
 * 
 * @author Michael Tzukanov
 */
@SuppressWarnings("serial")
class AnimatingSplitPanel extends CustomComponent {
	private static final String OPEN_ANIMATING_STYLE = "open-animating";
	private static final String CLOSE_ANIMATING_STYLE = "close-animating";

	private static final String OPEN_STYLE = "open";
	private static final String CLOSE_STYLE = "close";

	private static final String STYLE_NAME = "animating-split-panel";

	private final int TABBAR_WIDTH;
	private final int SIDE_PANEL_WIDTH;
	private final Unit UNIT;

	private boolean animationEnabled = true;
	private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

	public AnimatingSplitPanel(int tabBarWidth, int sidePanelWidth, Unit unit) {
		TABBAR_WIDTH = tabBarWidth;
		SIDE_PANEL_WIDTH = sidePanelWidth;
		UNIT = unit;

		setCompositionRoot(splitPanel);

		splitPanel.setLocked(true);
		splitPanel.addStyleName(STYLE_NAME);
		splitPanel.addStyleName(CLOSE_STYLE);

		init();
	}

	private void init() {
		splitPanel.setSplitPosition(TABBAR_WIDTH, UNIT, true);

		addDynamicCssToPage();

		addKeyframesToPage();
	}

	private void addDynamicCssToPage() {
		Page.getCurrent()
				.getStyles()
				.add("." + STYLE_NAME + " .v-splitpanel-second-container {"
						+ "	width: " + SIDE_PANEL_WIDTH + UNIT
						+ " !important; } " + "}");

		// this is the trick for the first container to keep it's width until
		// animation ends
		Page.getCurrent()
				.getStyles()
				.add(".wide.v-splitpanel-first-container {"
						+ "width: calc(100% - " + TABBAR_WIDTH + UNIT
						+ ") !important;" + "}");
	}

	private void addKeyframesToPage() {
		addKeyframesToPage("-webkit-");
		addKeyframesToPage("");
	}

	private void addKeyframesToPage(String prefix) {
		final String openPanelWidthWithoutTabBar = ""
				+ (SIDE_PANEL_WIDTH - TABBAR_WIDTH) + UNIT;

		final String negOpenPanelWidthWithoutTabBar = "-"
				+ openPanelWidthWithoutTabBar;

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes openSecondPart {"
						+ " from { right: " + negOpenPanelWidthWithoutTabBar
						+ "; } " + " to   { right: 0; } " + "}");

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes closeSecondPart {"
						+ " from { right: " + openPanelWidthWithoutTabBar
						+ "; } " + " to   { right: 0; } " + "}");
	}

	public boolean isOpen() {
		return splitPanel.getSplitPosition() == SIDE_PANEL_WIDTH;
	}

	public void setOpen(boolean open) {
		if (open)
			open();
		else
			close();
	}

	private void open() {
		if (isOpen())
			return;

		splitPanel.setSplitPosition(SIDE_PANEL_WIDTH, UNIT, true);
		splitPanel.addStyleName(OPEN_STYLE);
		splitPanel.removeStyleName(CLOSE_STYLE);
		if (isAnimationEnabled()) {
			splitPanel.addStyleName(OPEN_ANIMATING_STYLE);
			splitPanel.removeStyleName(CLOSE_ANIMATING_STYLE);

			// this is the trick for the first container to keep it's width
			// until animation ends
			JavaScript
					.eval("document.getElementsByClassName('animating-split-panel')[0].firstChild.firstChild.className += ' wide';"
							+ ""
							+ "setTimeout(function() {"
							+ "	document.getElementsByClassName('animating-split-panel')[0].firstChild.firstChild.className ="
							+ "		document.getElementsByClassName('animating-split-panel')[0].firstChild.firstChild.className.replace(/\\bwide\\b/,'');"
							+ "}, 500);");
		}
	}

	private void close() {
		if (!isOpen())
			return;

		splitPanel.setSplitPosition(TABBAR_WIDTH, UNIT, true);
		splitPanel.addStyleName(CLOSE_STYLE);
		splitPanel.removeStyleName(OPEN_STYLE);
		if (isAnimationEnabled()) {
			splitPanel.addStyleName(CLOSE_ANIMATING_STYLE);
			splitPanel.removeStyleName(OPEN_ANIMATING_STYLE);
		}
	}

	public boolean isAnimationEnabled() {
		return animationEnabled;
	}

	public void setAnimationEnabled(boolean isAnimationEnabled) {
		this.animationEnabled = isAnimationEnabled;
	}

	public void setFirstComponent(Component content) {
		splitPanel.setFirstComponent(content);
	}

	public void setSecondComponent(VerticalTabSheet tabSheet) {
		splitPanel.setSecondComponent(tabSheet);
	}
}
