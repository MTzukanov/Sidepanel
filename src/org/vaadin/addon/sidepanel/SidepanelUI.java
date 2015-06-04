package org.vaadin.addon.sidepanel;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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

		final SidePanelTab tab1 = sidePanel.addTab(FontAwesome.ADJUST,
				"Lazy init", null);
		final SidePanelTab tab2 = sidePanel.addTab(FontAwesome.ADJUST,
				"Tab number 2", new Label("Tab number 2 content"));

		sidePanel.addTabChangeListener(new TabChangeListener() {
			@Override
			public void tabChanged(SidePanelTab newTab) {
				Notification.show(newTab == null ? "No tab selected" : newTab
						.getDescription());

				if (tab1.equals(newTab) && tab1.getContent() == null) {
					tab1.setContent(new Label("Lazy init content"));
				}
			}
		});

		Button button = new Button("Open tab number 2",
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						sidePanel.setSelectedTab(tab2);
					}
				});

		button.setWidth("100%");

		sidePanel.setMainContent(new VerticalLayout(button,
				createNewTabPanel(sidePanel)));

		setContent(sidePanel);
	}

	private Component createNewTabPanel(final SidePanel sidePanel) {
		final TextField textField = new TextField("New tab's caption");
		Button create = new Button("Create new tab");

		create.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Button removeButton = new Button("Remove this tab");

				final SidePanelTab tab = sidePanel.addTab(FontAwesome.ADJUST,
						textField.getValue(), removeButton);

				removeButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						sidePanel.removeTab(tab);
					}
				});
			}
		});

		VerticalLayout l = new VerticalLayout(textField, create);
		l.setMargin(true);
		l.setSpacing(true);
		return l;
	}
}