package org.vaadin.addon.sidepanel;

import java.util.logging.Logger;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;

@PreserveOnRefresh
@SuppressWarnings("serial")
public class SidePanel extends CustomComponent {

	private static final String OPEN_STYLE = "open";
	private static final String CLOSE_STYLE = "close";

	private Logger logger = Logger.getLogger(getClass().getSimpleName());

	private final static int TABBAR_WIDTH_DEFAULT = 40;
	private final int TABBAR_WIDTH;

	private float SIDE_PANEL_WIDTH = 300;
	private Unit SIDE_PANEL_WIDTH_UNIT = Unit.PIXELS;

	private final HorizontalSplitPanel panel = new HorizontalSplitPanel();
	private final VerticalTabSheet tabSheet;

	public SidePanel() {
		this(TABBAR_WIDTH_DEFAULT);
	}

	public SidePanel(final int tabBarWidth) {
		panel.setLocked(true);

		TABBAR_WIDTH = tabBarWidth;

		tabSheet = new VerticalTabSheet(TABBAR_WIDTH, Unit.PIXELS);
		tabSheet.setStyleName("side-panel");
		tabSheet.setSizeFull();
		panel.setSecondComponent(tabSheet);

		setSidePanelWidth(SIDE_PANEL_WIDTH, Unit.PIXELS);

		tabSheet.addCollapseClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (panel.getSplitPosition() == SIDE_PANEL_WIDTH) {
					panel.setSplitPosition(TABBAR_WIDTH, Unit.PIXELS, true);
					panel.addStyleName(CLOSE_STYLE);
					panel.removeStyleName(OPEN_STYLE);
				} else {
					panel.setSplitPosition(SIDE_PANEL_WIDTH,
							SIDE_PANEL_WIDTH_UNIT, true);
					panel.addStyleName(OPEN_STYLE);
					panel.removeStyleName(CLOSE_STYLE);
				}
			}
		});

		setCompositionRoot(panel);
	}

	public void setSidePanelWidth(float size, Unit unit) {
		SIDE_PANEL_WIDTH = size;
		SIDE_PANEL_WIDTH_UNIT = unit;

		panel.setSplitPosition(SIDE_PANEL_WIDTH, SIDE_PANEL_WIDTH_UNIT, true);

		if (this.getWidth() < 0)
			throw new IllegalStateException(
					"Side menu has to have a defined width");

		addKeyframesToPage();
	}

	private void addKeyframesToPage() {
		addKeyframesToPage("-webkit-");
		addKeyframesToPage("");
	}

	private void addKeyframesToPage(String prefix) {
		// if IE doesn't support calc use this, thie will restrict to px width:
		// final String openPanelWidthWithoutTabBar = "" + (SIDE_PANEL_WIDTH -
		// TABBAR_WIDTH) + SIDE_PANEL_WIDTH_UNIT;

		final String openPanelWidthWithoutTabBar = "calc(" + SIDE_PANEL_WIDTH
				+ SIDE_PANEL_WIDTH_UNIT + " - " + TABBAR_WIDTH + "px)";
		final String negOpenPanelWidthWithoutTabBar = "calc(-"
				+ SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + " + "
				+ TABBAR_WIDTH + "px)";

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes openSecondPart {"
						+ " from {right: " + negOpenPanelWidthWithoutTabBar
						+ ";} " + " to   {right: 0;} " + "}");

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes closeSecondPart {"
						+ " from { width: " + SIDE_PANEL_WIDTH
						+ SIDE_PANEL_WIDTH_UNIT + "; " + "  right: "
						+ openPanelWidthWithoutTabBar + ";} "
						+ " to   { /*width: " + TABBAR_WIDTH + "px;*/"
						+ "  right: 0;} " + "}");
	}

	public void setMainContent(Component content) {
		panel.setFirstComponent(content);
	}

	public SidePanelTab addTab(Resource icon, String description, Component content) {
		return tabSheet.addTab(icon, description, content);
	}

	public void setSelectedTab(SidePanelTab tab) {
		tabSheet.setSelectedTab(tab);
	}
	
	public void addTabChangeListener(TabChangeListener listener) {
		tabSheet.addTabChangeListener(listener);
	}
	
	public void removeTabChangeListener(TabChangeListener listener) {
		tabSheet.removeTabChangeListener(listener);
	}
}
