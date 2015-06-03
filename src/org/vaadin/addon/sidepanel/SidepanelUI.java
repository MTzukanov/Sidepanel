package org.vaadin.addon.sidepanel;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("sidepanel")
public class SidepanelUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SidepanelUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final SidePanel sidePanel = new SidePanel();
		sidePanel.setSizeFull();
		
		sidePanel.addTabChangeListener(new TabChangeListener() {
			@Override
			public void tabChanged(SidePanelTab newTab) {
				Notification.show(newTab.toString());
			}
		});
		
		final SidePanelTab tab1 = sidePanel.addTab(FontAwesome.ADJUST, "sdf", new Label("test"));
		final SidePanelTab tab2 = sidePanel.addTab(FontAwesome.ADJUST, "sdf2", new Label("test2"));
		
		Button button = new Button("open tab2", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				sidePanel.setSelectedTab(tab2);
			}
		});
		button.setWidth("100%");
		
		sidePanel.setMainContent(button);
		
		setContent(sidePanel);		
	}
}