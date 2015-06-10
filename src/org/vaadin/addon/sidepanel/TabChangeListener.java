package org.vaadin.addon.sidepanel;

/**
 * Listens to tab changes.
 * 
 * @author Michael Tzukanov
 */
public interface TabChangeListener {
	/**
	 * Is called when the tab is changed.
	 * 
	 * @param newTab Can be null if no tab is selected.
	 */
	void tabChanged(SidePanelTab newTab);
}
