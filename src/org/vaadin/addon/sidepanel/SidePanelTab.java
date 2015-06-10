package org.vaadin.addon.sidepanel;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
/**
 * Contains data about a tab, including the tab content and tab header.
 * 
 * @author Michael Tzukanov
 */
public class SidePanelTab {
	public interface TabHeaderClickListener {
		void tabHeaderClicked(SidePanelTab tab);
	}

	private Component content;
	private Button tabHeader = new Button();
	private List<TabHeaderClickListener> listeners = new ArrayList<>();

	/**
	 * Constructs a new tab with the given parameters.
	 * 
	 * @param icon
	 *            Icon on the tab header.
	 * @param description
	 *            Description (tooltip) on the tab header
	 * @param content
	 *            Content component of the tab.
	 */
	public SidePanelTab(Resource icon, String description, Component content) {
		this.content = content;
		tabHeader.setIcon(icon);
		tabHeader.setDescription(description);
		tabHeader.setWidth("100%");

		tabHeader.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				for (TabHeaderClickListener l : listeners)
					l.tabHeaderClicked(SidePanelTab.this);
			}
		});
	}

	protected AbstractComponent getTabHeader() {
		return tabHeader;
	}

	/**
	 * @return the current content of the tab.
	 */
	public Component getContent() {
		return content;
	}

	/**
	 * Sets the content of the tab. If the tab is currently shown, it has to be
	 * selected again to show the new content.
	 * 
	 * @param content
	 */
	public void setContent(Component content) {
		this.content = content;
	}

	/**
	 * Adds a click listener to this specific tab.
	 * 
	 * @param listener
	 */
	public void addClickListener(TabHeaderClickListener listener) {
		listeners.add(listener);
	}

	public void removeClickListener(TabHeaderClickListener listener) {
		listeners.remove(listener);
	}

	public String getDescription() {
		return getTabHeader().getDescription();
	}

	/**
	 * Changes the tooltip for this tab header.
	 * @param description
	 */
	public void setDecription(String description) {
		getTabHeader().setDescription(description);
	}

	/**
	 * @return true if the tab header is visible.
	 */
	public boolean isVisible() {
		return getTabHeader().isVisible();
	}

	/**
	 * Defines if the tab header is visible.
	 */
	public void setVisible(boolean visible) {
		getTabHeader().setVisible(visible);
	}

	/**
	 * @return true if the tab header is enabled.
	 */
	public boolean isEnabled() {
		return getTabHeader().isEnabled();
	}

	/**
	 * Sets the tab header to enabled or disabled.
	 */
	public void setEnabled(boolean enabled) {
		getTabHeader().setEnabled(enabled);
	}

	/**
	 * @return current tab header icon
	 */
	public Resource getIcon() {
		return getTabHeader().getIcon();
	}

	/**
	 * Sets an icon to the tab header.
	 */
	public void setIcon(Resource icon) {
		getTabHeader().setIcon(icon);
	}

	/**
	 * Adds a stylename or stylenames to the tab header.
	 */
	public void addStyleName(String style) {
		getTabHeader().addStyleName(style);
	}

	/**
	 * Removes a stylename from the tab header.
	 */
	public void removeStyleName(String style) {
		getTabHeader().removeStyleName(style);
	}
}
