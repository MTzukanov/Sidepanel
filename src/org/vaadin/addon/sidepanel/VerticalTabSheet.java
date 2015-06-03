package org.vaadin.addon.sidepanel;

import java.util.ArrayList;
import java.util.List;

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

	private final List<SidePanelTab> tabs = new ArrayList<>();
	private final List<TabChangeListener> changeListeners = new ArrayList<>();
	
	private final TabHeaderClickListener tabHeaderClickListener = new TabHeaderClickListener() {
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

	public SidePanelTab addTab(Resource icon, String description, Component content) {
		final SidePanelTab tab = new SidePanelTab(icon, description, content);

		tab.addClickListener(tabHeaderClickListener);

		tabLayout.addComponent(tab.getTabHeader());
		tabs.add(tab);
		if (tabLayout.getComponentCount() == 1)
			setSelectedTab(tab);

		return tab;
	}

	public void setSelectedTab(SidePanelTab tab) {
		unselectAllTabs();
		tab.getTabHeader().addStyleName(SELECTED_STYLENAME);
		content.setContent(tab.getContent());
		
		for (TabChangeListener l : changeListeners)
			l.tabChanged(tab);
	}

	protected void unselectAllTabs() {
		for (Component c : tabLayout)
			c.removeStyleName(SELECTED_STYLENAME);
	}

	public void addCollapseClickListener(Button.ClickListener listener) {
		collapseButton.addClickListener(listener);
	}

	public void addTabChangeListener(TabChangeListener listener) {
		changeListeners.add(listener);
	}
	
	public void removeTabChangeListener(TabChangeListener listener) {
		changeListeners.remove(listener);
	}
}
