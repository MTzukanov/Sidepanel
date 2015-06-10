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
/**
 * SidePanel component has the main content on the left side and 
 * a vertical tab sheet on the right side, which has fixed size.
 * 
 * The opening/closing of a the right side is optionally animated.
 * When right side is opened/closed, the left side is resized.
 * 
 * @author Michael Tzukanov
 */
public class SidePanel extends CustomComponent {
	/**
	 * Gets notified when the panel is being opened or closed.
	 */
	public interface OpenCloseListener {
		/**
		 * Is called if when the panel is being opened or closed.
		 * 
		 * @param open
		 *            true if the panel is being opened
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

	/**
	 * Constructs a side panel with the default paramenters (tabbar 40 pixels
	 * and the right panel 300 pixels).
	 */
	public SidePanel() {
		this(TABBAR_WIDTH_DEFAULT, SIDE_PANEL_WIDTH_DEFAULT, UNIT_DEFAULT,
				false);
	}

	/**
	 * Constructs a side panel with given parameters.
	 * 
	 * @param tabBarWidth
	 * @param rightPanelWidth
	 *            including the tab bar
	 * @param unit
	 *            Units for tab bar and right panel
	 * @param initiallyOpen
	 */
	public SidePanel(final int tabBarWidth, final int rightPanelWidth,
			Unit unit, boolean initiallyOpen) {
		tabSheet = new VerticalTabSheet(tabBarWidth, unit);
		tabSheet.setStyleName("side-panel");
		tabSheet.setSizeFull();

		tabSheet.getTabLayout().addComponent(createCollapseButton());

		tabSheet.addTabChangeListener(new TabChangeListener() {
			@Override
			public void tabChanged(SidePanelTab newTab) {
				open();
			}
		});

		panel = new AnimatingSplitPanel(tabBarWidth, rightPanelWidth, unit);
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

	/**
	 * Sets the main (left side) content of the component.
	 * 
	 * @param content
	 */
	public void setMainContent(Component content) {
		panel.setFirstComponent(content);
	}

	/**
	 * Adds a tab with the given parameters. All the parameters can be changed
	 * later. It is possible to set the tab content before displaying by using a
	 * TabChangeListener.
	 * 
	 * @param icon
	 * @param description AKA tooltip.
	 * @param content
	 * @return SidePanelTab object that can be used to remove/select and modify tab.
	 */
	public SidePanelTab addTab(Resource icon, String description,
			Component content) {
		return tabSheet.addTab(icon, description, content);
	}

	/**
	 * Removes an existing tab from the panel.
	 */
	public void removeTab(SidePanelTab tab) {
		tabSheet.removeTab(tab);
	}

	/**
	 * Selects an existing tab.
	 * 
	 * @param newTabToSelect null to deselect all tabs.
	 * @throws IllegalArgumentException
	 *             if the tab does not exist.
	 */
	public void setSelectedTab(SidePanelTab tab) {
		tabSheet.setSelectedTab(tab);
	}

	/**
	 * @return The selected tab or null.
	 */
	public SidePanelTab getSelectedTab() {
		return tabSheet.getSelectedTab();
	}

	/**
	 * The listener will be called when the selected tab is changed.
	 */
	public void addTabChangeListener(TabChangeListener listener) {
		tabSheet.addTabChangeListener(listener);
	}

	/**
	 * Removes an existing listener.
	 */
	public void removeTabChangeListener(TabChangeListener listener) {
		tabSheet.removeTabChangeListener(listener);
	}

	/**
	 * @return true if animation is enabled.
	 */
	public boolean isAnimationEnabled() {
		return panel.isAnimationEnabled();
	}

	/**
	 * @param true to enable the animation, false to disable.
	 */
	public void setAnimationEnabled(boolean isAnimationEnabled) {
		panel.setAnimationEnabled(isAnimationEnabled);
	}

	/**
	 * Open the tab sheet. If no tab is selected and there exists one, a tab will be selected.
	 */
	public void open() {
		tabSheet.selectAnyTab();
		panel.setOpen(true);

		for (OpenCloseListener listener : listeners)
			listener.panelStatusChanged(true);
	}

	/**
	 * @return true if right panel is open
	 */
	public boolean isOpen() {
		return panel.isOpen();
	}

	/**
	 * Open the panel if it's closed, close if open.
	 */
	public void toggle() {
		if (isOpen())
			close();
		else
			open();
	}

	/**
	 * Closes the panel.
	 */
	public void close() {
		panel.setOpen(false);

		for (OpenCloseListener listener : listeners)
			listener.panelStatusChanged(false);
	}

	/**
	 * Adds a listener that will be invoked when the panel is opened or closed.
	 */
	public void addOpenPanelListener(OpenCloseListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes an existing listener.
	 */
	public void removeOpenPanelListener(OpenCloseListener listener) {
		listeners.remove(listener);
	}
}
