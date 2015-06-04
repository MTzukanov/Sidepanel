package org.vaadin.addon.sidepanel;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
public class SidePanel extends CustomComponent {
	private final static int TABBAR_WIDTH_DEFAULT = 40;
	private final static int SIDE_PANEL_WIDTH_DEFAULT = 300;
	private final static Unit UNIT_DEFAULT = Unit.PIXELS;

	private final AnimatingSplitPanel panel;
	private final VerticalTabSheet tabSheet;

	public SidePanel() {
		this(TABBAR_WIDTH_DEFAULT, SIDE_PANEL_WIDTH_DEFAULT, UNIT_DEFAULT);
	}

	public SidePanel(final int tabBarWidth, final int sidePanelWidth, Unit unit) {
		tabSheet = new VerticalTabSheet(tabBarWidth, unit);
		tabSheet.setStyleName("side-panel");
		tabSheet.setSizeFull();

		tabSheet.addCollapseClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				panel.toggleSidePanel();
			}
		});

		panel = new AnimatingSplitPanel(tabBarWidth, sidePanelWidth, unit);
		panel.setSecondComponent(tabSheet);

		setCompositionRoot(panel);
	}

	public void setMainContent(Component content) {
		panel.setFirstComponent(content);
	}

	public SidePanelTab addTab(Resource icon, String description,
			Component content) {
		return tabSheet.addTab(icon, description, content);
	}

	public void removeTab(SidePanelTab tab) {
		tabSheet.removeTab(tab);
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
	
	public boolean isAnimationEnabled() {
		return panel.isAnimationEnabled();
	}

	public void setAnimationEnabled(boolean isAnimationEnabled) {
		panel.setAnimationEnabled(isAnimationEnabled);
	}
}
