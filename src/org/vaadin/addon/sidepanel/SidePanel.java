package org.vaadin.addon.sidepanel;

import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;

@SuppressWarnings("serial")
public class SidePanel extends CustomComponent {
	private final int TABBAR_WIDTH = 40;

	private float SIDE_PANEL_WIDTH = 100;
	private Unit SIDE_PANEL_WIDTH_UNIT = Unit.PIXELS;

	HorizontalSplitPanel panel = new HorizontalSplitPanel();
	VerticalTabSheet tabSheet = new VerticalTabSheet(TABBAR_WIDTH, Unit.PIXELS);

	public SidePanel() {
		panel.setLocked(true);

		Button button = new Button("first");
		button.setWidth("100%");
		panel.setFirstComponent(button);

		Button wideButton = new Button("test");
		wideButton.setWidth("100%");

		tabSheet.setSizeFull();

		panel.setSecondComponent(tabSheet);

		setSideMenuWidth(SIDE_PANEL_WIDTH, Unit.PIXELS);
		
		tabSheet.addCollapseClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (panel.getSplitPosition() == SIDE_PANEL_WIDTH) {
					panel.setSplitPosition(TABBAR_WIDTH, Unit.PIXELS, true);
					panel.addStyleName("close");
					panel.removeStyleName("open");
				} else {
					panel.setSplitPosition(SIDE_PANEL_WIDTH,
							SIDE_PANEL_WIDTH_UNIT, true);
					panel.addStyleName("open");
					panel.removeStyleName("close");
				}
			}
		});

		setCompositionRoot(panel);
	}

	public void setSideMenuWidth(float size, Unit unit) {
		SIDE_PANEL_WIDTH = size;
		SIDE_PANEL_WIDTH_UNIT = unit;

		panel.setSplitPosition(SIDE_PANEL_WIDTH, SIDE_PANEL_WIDTH_UNIT, true);

		assert this.getWidth() > 0;

		String firstPanelOpenWidth = "calc(" + this.getWidth()
				+ this.getWidthUnits() + " - " + SIDE_PANEL_WIDTH
				+ SIDE_PANEL_WIDTH_UNIT + ")";
		String firstPanelClosedWidth = "calc(" + this.getWidth()
				+ this.getWidthUnits() + " - " + TABBAR_WIDTH + "px" + ")";

		System.out.println("open: " + firstPanelOpenWidth);
		System.out.println("closed: " + firstPanelClosedWidth);

		Page.getCurrent()
				.getStyles()
				.add("@-webkit-keyframes openFirstPart {" + "from {width: "
						+ firstPanelClosedWidth + ";}" + "to {width: "
						+ firstPanelOpenWidth + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@-webkit-keyframes closeFirstPart {" + "from {width: "
						+ firstPanelOpenWidth + ";}" + "to {width: "
						+ firstPanelClosedWidth + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@-webkit-keyframes openSecondPart {" + "from {width: "
						+ TABBAR_WIDTH + "px;}" + "to {width: "
						+ SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@-webkit-keyframes closeSecondPart {" + "from {width: "
						+ SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + ";}"
						+ "to {width: " + TABBAR_WIDTH + "px;}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@keyframes openFirstPart {" + "from {width: "
						+ firstPanelClosedWidth + ";}" + "to {width: "
						+ firstPanelOpenWidth + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@keyframes closeFirstPart {" + "from {width: "
						+ firstPanelOpenWidth + ";}" + "to {width: "
						+ firstPanelClosedWidth + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@keyframes openSecondPart {" + "from {width: "
						+ TABBAR_WIDTH + "px;}" + "to {width: "
						+ SIDE_PANEL_WIDTH + SIDE_PANEL_WIDTH_UNIT + ";}" + "}");

		Page.getCurrent()
				.getStyles()
				.add("@keyframes closeSecondPart {" + "from {width: "
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
