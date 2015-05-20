package org.vaadin.addon.sidepanel;

import java.util.ArrayList;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class VerticalTabSheet extends CustomComponent {
	private static final String COLLAPSE_BUTTON = "collapse-button";
	private static final String TABS_PANEL_STYLENAME = "tabs-panel";
	private static final String SELECTED_STYLENAME = "selected";
	
	private final Button collapseButton = new Button(">");
	private final HorizontalLayout mainLayout = new HorizontalLayout();
	private final CssLayout tabLayout = new CssLayout();
	private final Panel content = new Panel();
	
	private ArrayList<Tab> tabs = new ArrayList<>();

	private class Tab extends Button {
		private Component content;

		public Tab(Resource icon, String description, Component content) {
			this.content = content;
			this.setIcon(icon);
			this.setDescription(description);
			this.setWidth("100%");
			
			this.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					setSelectedTab(Tab.this);
				}
			});
		}
	}

	public VerticalTabSheet(final int tabWidth, final Unit tabWidthUnits) {
		mainLayout.setSizeFull();

		mainLayout.addComponent(createTabPanel(tabWidth, tabWidthUnits));

		mainLayout.addComponent(content);
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
		Tab tab = new Tab(icon, description, content);
		tabLayout.addComponent(tab);
		tabs.add(tab);
		if (tabLayout.getComponentCount() == 1)
			setSelectedTab(tab);
		
		return tabs.indexOf(tab);
	}

	private void setSelectedTab(Tab tab) {
		unselectAllTabs();
		tab.addStyleName(SELECTED_STYLENAME);
		content.setContent(tab.content);
	}
	
	public void setSelectedTab(int index)
	{
		setSelectedTab(tabs.get(index));
	}
	
	protected void unselectAllTabs()
	{
		for (Component c : tabLayout)
			c.removeStyleName(SELECTED_STYLENAME);
	}
	
	public void addCollapseClickListener(Button.ClickListener listener)
	{
		collapseButton.addClickListener(listener);
	}	
}
