package org.vaadin.addon.sidepanel;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addon.sidepanel.SidePanelTab.TabHeaderClickListener;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
/**
 * Vertical tab sheet component. Uses {@link SidePanelTab} to store tab data and the tab header component.
 * 
 * @author Michael Tzukanov
 */
class VerticalTabSheet extends CustomComponent {
	private static final String TABS_PANEL_STYLENAME = "tabs-panel";
	private static final String SELECTED_STYLENAME = "selected";

	private final CssLayout tabLayout = new CssLayout();
	private final Panel content = new Panel();

	private final List<SidePanelTab> tabs = new ArrayList<>();
	private SidePanelTab selectedTab;

	private final List<TabChangeListener> changeListeners = new ArrayList<>();

	private final TabHeaderClickListener tabHeaderClickListener = new TabHeaderClickListener() {
		@Override
		public void tabHeaderClicked(SidePanelTab tab) {
			setSelectedTab(tab);
		}
	};

	public VerticalTabSheet(final int tabWidth, final Unit tabWidthUnits) {
		final HorizontalLayout mainLayout = new HorizontalLayout();

		mainLayout.setSizeFull();

		mainLayout.addComponent(createTabPanel(tabWidth, tabWidthUnits));

		mainLayout.addComponent(content);

		content.setSizeFull();
		mainLayout.setExpandRatio(content, 1);

		setCompositionRoot(mainLayout);
	}

	private Panel createTabPanel(final int tabWidth, final Unit tabWidthUnits) {
		final Panel tabsPanel = new Panel(tabLayout);
		tabLayout.setWidth("100%");
		tabsPanel.addStyleName(TABS_PANEL_STYLENAME);
		tabsPanel.setSizeFull();
		tabsPanel.setWidth(tabWidth, tabWidthUnits);
		return tabsPanel;
	}

	/**
	 * Currently used to inject the collapse button.
	 * 
	 * @return The tab layout
	 */
	protected Layout getTabLayout() {
		return tabLayout;
	}

	/**
	 * Adds a new tab.
	 */
	public SidePanelTab addTab(Resource icon, String tooltip, Component content) {
		final SidePanelTab tab = new SidePanelTab(icon, tooltip, content);

		tab.addClickListener(tabHeaderClickListener);

		tabLayout.addComponent(tab.getTabHeader());
		tabs.add(tab);

		return tab;
	}

	/**
	 * Removes an existing tab.
	 */
	public void removeTab(SidePanelTab tab) {
		tabs.remove(tab);
		tabLayout.removeComponent(tab.getTabHeader());

		if (tab.equals(getSelectedTab()))
			setSelectedTab(null);
	}

	/**
	 * Selects an existing tab.
	 * 
	 * @param newTabToSelect
	 * @throws IllegalArgumentException
	 *             if the tab does not exist.
	 */
	public void setSelectedTab(SidePanelTab newTabToSelect) {
		if (newTabToSelect != null && !tabs.contains(newTabToSelect))
			throw new IllegalArgumentException(
					"Tab is not part of this TabSheet");

		if (selectedTab != null)
			selectedTab.removeStyleName(SELECTED_STYLENAME);

		// calling it before setContent to allow lazy content initialization
		for (TabChangeListener l : changeListeners)
			l.tabChanged(newTabToSelect);

		if (newTabToSelect != null) {
			newTabToSelect.addStyleName(SELECTED_STYLENAME);
			content.setContent(newTabToSelect.getContent());
		} else {
			content.setContent(null);
		}

		selectedTab = newTabToSelect;
	}

	/**
	 * If no tab is selected and there is at least one tab, the first tab will
	 * be selected.
	 */
	public void selectAnyTab() {
		if (getSelectedTab() == null && tabs.size() > 0)
			setSelectedTab(tabs.get(0));
	}

	/**
	 * @return Currently selected tab or null if none is selected.
	 */
	public SidePanelTab getSelectedTab() {
		return selectedTab;
	}

	public void addTabChangeListener(TabChangeListener listener) {
		changeListeners.add(listener);
	}

	public void removeTabChangeListener(TabChangeListener listener) {
		changeListeners.remove(listener);
	}
}
