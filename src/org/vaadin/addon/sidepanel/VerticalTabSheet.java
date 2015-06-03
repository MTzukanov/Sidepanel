package org.vaadin.addon.sidepanel;

import java.util.ArrayList;

import org.vaadin.addon.sidepanel.SidePanelTab.TabHeaderClickListener;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
class VerticalTabSheet extends CustomComponent {
	private static final String COLLAPSE_BUTTON = "collapse-button";
	private static final String TABS_PANEL_STYLENAME = "tabs-panel";
	private static final String SELECTED_STYLENAME = "selected";

	private final Button collapseButton = new Button(">");
	private final HorizontalLayout mainLayout = new HorizontalLayout();
	private final CssLayout tabLayout = new CssLayout();
	private final Panel content = new Panel();

	private ArrayList<SidePanelTab> tabs = new ArrayList<>();
	private TabHeaderClickListener tabHeaderClickListener = new TabHeaderClickListener() {
		@Override
		public void tabHeaderClicked(SidePanelTab tab) {
			setSelectedTab(tab);
		}
	};

	public VerticalTabSheet(final int tabWidth, final Unit tabWidthUnits) {
		mainLayout.setSizeFull();

		mainLayout.addComponent(createTabPanel(tabWidth, tabWidthUnits));

		mainLayout.addComponent(content);
		content.setSizeFull();
		mainLayout.setExpandRatio(content, 1);

		setCompositionRoot(mainLayout);
	}

	private Panel createTabPanel(final int tabWidth, final Unit tabWidthUnits) {
		final Panel tabsPanel = new Panel(tabLayout);
		tabLayout.addComponent(createCollapseButton());
		tabLayout.setWidth("100%");
		tabsPanel.addStyleName(TABS_PANEL_STYLENAME);
		tabsPanel.setSizeFull();
		tabsPanel.setWidth(tabWidth, tabWidthUnits);
		return tabsPanel;
	}

	private Component createCollapseButton() {
		collapseButton.setWidth("100%");
		collapseButton.addStyleName(COLLAPSE_BUTTON);
		return collapseButton;
	}

	public int addTab(Resource icon, String description, Component content) {
		final SidePanelTab tab = new SidePanelTab(icon, description, content);

		tab.addClickListener(tabHeaderClickListener);

		tabLayout.addComponent(tab.getTabHeader());
		tabs.add(tab);
		if (tabLayout.getComponentCount() == 1)
			setSelectedTab(tab);

		return tabs.indexOf(tab);
	}

	private void setSelectedTab(SidePanelTab tab) {
		unselectAllTabs();
		tab.getTabHeader().addStyleName(SELECTED_STYLENAME);
		content.setContent(tab.getContent());
	}

	public void setSelectedTab(int index) {
		setSelectedTab(tabs.get(index));
	}

	protected void unselectAllTabs() {
		for (Component c : tabLayout)
			c.removeStyleName(SELECTED_STYLENAME);
	}

	public void addCollapseClickListener(Button.ClickListener listener) {
		collapseButton.addClickListener(listener);
	}
}
