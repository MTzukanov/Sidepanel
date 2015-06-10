package org.vaadin.addon.sidepanel;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
public class SidePanel extends CustomComponent {
	/**
	 * Gets notified when the panel is being opened or closed.
	 */
	public interface OpenCloseListener {
		/**
		 * Is called if when the panel is being opened or closed.
		 * 
		 * @param open true if the panel is being opened
		 */
		void panelStatusChanged(boolean open);
	}

	private final static String COLLAPSE_BUTTON = "collapse-button";
	private final static int TABBAR_WIDTH_DEFAULT = 40;
	private final static int SIDE_PANEL_WIDTH_DEFAULT = 300;
	private final static Unit UNIT_DEFAULT = Unit.PIXELS;

	private final AnimatingSplitPanel panel;
	private final VerticalTabSheet tabSheet;
	
	private final List<OpenCloseListener> listeners = new ArrayList<>();

	public SidePanel() {
		this(TABBAR_WIDTH_DEFAULT, SIDE_PANEL_WIDTH_DEFAULT, UNIT_DEFAULT,
				false);
	}

	public SidePanel(final int tabBarWidth, final int sidePanelWidth,
			Unit unit, boolean initiallyOpen) {
		tabSheet = new VerticalTabSheet(tabBarWidth, unit);
		tabSheet.setStyleName("side-panel");
		tabSheet.setSizeFull();
		
		tabSheet.getTabLayout().addComponent(createCollapseButton());

		tabSheet.addTabChangeListener(new TabChangeListener() {
			@Override
			public void tabChanged(SidePanelTab newTab) {
				panel.setOpen(true);
			}
		});

		panel = new AnimatingSplitPanel(tabBarWidth, sidePanelWidth, unit);
		panel.setOpen(initiallyOpen);
		panel.setSecondComponent(tabSheet);
		panel.setSizeFull();

		setCompositionRoot(panel);
	}
	
	private Component createCollapseButton() {
		Button collapseButton = new Button("", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (!panel.isOpen())
					open();
				else
					close();
			}
		});

		collapseButton.setWidth("100%");
		collapseButton.addStyleName(COLLAPSE_BUTTON);
		return collapseButton;
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

	public SidePanelTab getSelectedTab() {
		return tabSheet.getSelectedTab();
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
	
	public void open() {
		tabSheet.selectAnyTab();
		panel.setOpen(true);
		
		for (OpenCloseListener listener : listeners)
			listener.panelStatusChanged(true);
	}
	
	public boolean isOpen() {
		return panel.isOpen();
	}

	public void toggle() {
		if (isOpen())
			close();
		else
			open();
	}
	
	public void close() {
		panel.setOpen(false);
		
		for (OpenCloseListener listener : listeners)
			listener.panelStatusChanged(false);
	}
	
	public void addOpenPanelListener(OpenCloseListener listener) {
		listeners.add(listener);
	}

	public void removeOpenPanelListener(OpenCloseListener listener) {
		listeners.remove(listener);
	}
}
