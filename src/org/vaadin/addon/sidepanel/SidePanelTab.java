package org.vaadin.addon.sidepanel;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class SidePanelTab {
	public interface TabHeaderClickListener {
		void tabHeaderClicked(SidePanelTab tab);
	}
	
	private Component content;
	private Button tabHeader = new Button();
	private List<TabHeaderClickListener> listeners = new ArrayList<>();

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
	
	public Component getContent() {
		return content;
	}
	
	public void setContent(Component content) {
		this.content = content;
	}
	
	public void addClickListener(TabHeaderClickListener listener) {
		listeners.add(listener);
	}
	
	public void removeClickListener(TabHeaderClickListener listener) {
		listeners.remove(listener);
	}
	
	public String getDescription() {
		return getTabHeader().getDescription();
	}
	
	public void setDecription(String description) {
		getTabHeader().setDescription(description);
	}

	public boolean isVisible() {
		return getTabHeader().isVisible();
	}

	public void setVisible(boolean visible) {
		getTabHeader().setVisible(visible);;
	}

	public boolean isEnabled() {
		return getTabHeader().isEnabled();
	}

	public void setEnabled(boolean enabled) {
		getTabHeader().setEnabled(enabled);
	}

	public Resource getIcon() {
		return getTabHeader().getIcon();
	}

	public void setIcon(Resource icon) {
		getTabHeader().setIcon(icon);
	}
	
	public void addStyleName(String style) {
		getTabHeader().addStyleName(style);
	}

	public void removeStyleName(String style) {
		getTabHeader().removeStyleName(style);
	}
}
