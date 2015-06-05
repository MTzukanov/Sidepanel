package org.vaadin.addon.sidepanel;

import com.vaadin.server.Page;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.JavaScript;

/**
 * Because of IE limitations (calc is not supported in animations) tabbar width
 * and the whole side panel width must use the same units.
 * 
 * Also because we add constant style names this component should be used only
 * once or all the components should have the same tabbar/sidepanel width.
 */
@SuppressWarnings("serial")
class AnimatingSplitPanel extends HorizontalSplitPanel {
	private static final String OPEN_ANIMATING_STYLE = "open-animating";
	private static final String CLOSE_ANIMATING_STYLE = "close-animating";

	private static final String OPEN_STYLE = "open";
	private static final String CLOSE_STYLE = "close";

	private static final String STYLE_NAME = "animating-split-panel";

	private final int TABBAR_WIDTH;
	private final int SIDE_PANEL_WIDTH;
	private final Unit UNIT;

	private boolean animationEnabled = true;

	public AnimatingSplitPanel(int tabBarWidth, int sidePanelWidth, Unit unit) {
		TABBAR_WIDTH = tabBarWidth;
		SIDE_PANEL_WIDTH = sidePanelWidth;
		UNIT = unit;

		setLocked(true);
		addStyleName(STYLE_NAME);
		addStyleName(OPEN_STYLE);
		init();
	}

	private void init() {
		setSplitPosition(SIDE_PANEL_WIDTH, UNIT, true);

		addDynamicCssToPage();

		addKeyframesToPage();
	}

	private void addDynamicCssToPage() {
		Page.getCurrent()
				.getStyles()
				.add("." + STYLE_NAME + " .v-splitpanel-second-container {"
						+ "	width: " + SIDE_PANEL_WIDTH + UNIT
						+ " !important; } " + "}");

		// this is the trick for the first container to keep it's width until animation ends
		Page.getCurrent()
				.getStyles()
				.add(".wide.v-splitpanel-first-container {"
						+ "width: calc(100% - " + TABBAR_WIDTH + UNIT + ") !important;" + "}");
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

		// this works in Chrome/FF, would be better if IE supported calc...
		// final String openPanelWidthWithoutTabBar = "calc(" + SIDE_PANEL_WIDTH
		// + SIDE_PANEL_WIDTH_UNIT + " - " + TABBAR_WIDTH + "px)";
		// final String negOpenPanelWidthWithoutTabBar = "calc(-"
		// + SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + " + "
		// + TABBAR_WIDTH + "px)";

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

	public void toggleSidePanel() {
		if (isOpen()) {
			close();
		} else {
			open();
		}
	}
	
	public boolean isOpen() {
		return getSplitPosition() == SIDE_PANEL_WIDTH;
	}

	public void open() {
		if (isOpen())
			return;
		
		setSplitPosition(SIDE_PANEL_WIDTH, UNIT, true);
		addStyleName(OPEN_STYLE);
		removeStyleName(CLOSE_STYLE);
		if (isAnimationEnabled()) {
			addStyleName(OPEN_ANIMATING_STYLE);
			removeStyleName(CLOSE_ANIMATING_STYLE);

			// this is the trick for the first container to keep it's width until animation ends
			JavaScript
					.eval("document.getElementsByClassName('animating-split-panel')[0].firstChild.firstChild.className += ' wide';"
							+ ""
							+ "setTimeout(function() {"
							+ "	document.getElementsByClassName('animating-split-panel')[0].firstChild.firstChild.className ="
							+ "		document.getElementsByClassName('animating-split-panel')[0].firstChild.firstChild.className.replace(/\\bwide\\b/,'');"
							+ "}, 500);");
		}
	}

	public void close() {
		if (!isOpen())
			return;
		
		setSplitPosition(TABBAR_WIDTH, UNIT, true);
		addStyleName(CLOSE_STYLE);
		removeStyleName(OPEN_STYLE);
		if (isAnimationEnabled()) {
			addStyleName(CLOSE_ANIMATING_STYLE);
			removeStyleName(OPEN_ANIMATING_STYLE);
		}
	}

	public boolean isAnimationEnabled() {
		return animationEnabled;
	}

	public void setAnimationEnabled(boolean isAnimationEnabled) {
		this.animationEnabled = isAnimationEnabled;
	}
}
