package org.vaadin.addon.sidepanel;

import java.util.logging.Logger;

import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;

@SuppressWarnings("serial")
public class SidePanel extends CustomComponent {
	
	private static final String OPEN_STYLE = "open";
	private static final String CLOSE_STYLE = "close";

	private Logger logger = Logger.getLogger(getClass().getSimpleName());
	
	private final static int TABBAR_WIDTH_DEFAULT = 40;
	private final int TABBAR_WIDTH;

	private float SIDE_PANEL_WIDTH = 100;
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
		tabSheet.setSizeFull();
		panel.setSecondComponent(tabSheet);

		setSideMenuWidth(SIDE_PANEL_WIDTH, Unit.PIXELS);
		
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

	public void setSideMenuWidth(float size, Unit unit) {
		SIDE_PANEL_WIDTH = size;
		SIDE_PANEL_WIDTH_UNIT = unit;

		panel.setSplitPosition(SIDE_PANEL_WIDTH, SIDE_PANEL_WIDTH_UNIT, true);

		if (this.getWidth() < 0)
			throw new IllegalStateException("Side menu has to have a defined width");

		String firstPanelOpenWidth = "calc(" + this.getWidth()
				+ this.getWidthUnits() + " - " + SIDE_PANEL_WIDTH
				+ SIDE_PANEL_WIDTH_UNIT + ")";
		
		String firstPanelClosedWidth = "calc(" + this.getWidth()
				+ this.getWidthUnits() + " - " + TABBAR_WIDTH + "px" + ")";

		// logger.info("first panel open / closed width: " + firstPanelOpenWidth + " / " + firstPanelClosedWidth);

		addKeyframesToPage(firstPanelOpenWidth, firstPanelClosedWidth);
	}

	private void addKeyframesToPage(String firstPanelOpenWidth,
			String firstPanelClosedWidth) {
		addKeyframesToPage(firstPanelOpenWidth, firstPanelClosedWidth, "-webkit-");
		addKeyframesToPage(firstPanelOpenWidth, firstPanelClosedWidth, "");
	}

	private void addKeyframesToPage(String firstPanelOpenWidth,
			String firstPanelClosedWidth, String prefix) {
		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes openFirstPart {" + "from {width: "
						+ firstPanelClosedWidth + ";}" + "to {width: "
						+ firstPanelOpenWidth + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes closeFirstPart {" + "from {width: "
						+ firstPanelOpenWidth + ";}" + "to {width: "
						+ firstPanelClosedWidth + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes openSecondPart {" + "from {width: "
						+ TABBAR_WIDTH + "px;}" + "to {width: "
						+ SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@" + prefix + "keyframes closeSecondPart {" + "from {width: "
						+ SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + ";}"
						+ "to {width: " + TABBAR_WIDTH + "px;}" + "}");
	}

	public void setMainContent(Component content) {
		panel.setFirstComponent(content);
	}
	
	public int addTab(Resource icon, String description, Component content) {
		return tabSheet.addTab(icon, description, content);
	}

	public void setSelectedTab(int index) {
		tabSheet.setSelectedTab(index);
	}	
}
