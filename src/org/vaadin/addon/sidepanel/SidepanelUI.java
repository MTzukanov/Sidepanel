package org.vaadin.addon.sidepanel;

import javax.servlet.annotation.WebServlet;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
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
//		sidePanel.setMainContent(new ComplexPanel());
		
		int tab1 = sidePanel.addTab(FontAwesome.ADJUST, "sdf", new Label("test"));
		final int tab2 = sidePanel.addTab(FontAwesome.ADJUST, "sdf2", new Label("test2"));
		
		sidePanel.setMainContent(new Button("open tab2", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				sidePanel.setSelectedTab(tab2);
			}
		}));
		
		setContent(sidePanel);		
	}
	
	static class ComplexPanel extends VerticalLayout {
//		public ComplexPanel() {
//			for (int i : IntStream.range(0, 15).toArray()) {
//				ComplexPanel complexPanel = new ComplexPanel(false);
//				addComponents(new Button(" test "), complexPanel);
//				setExpandRatio(complexPanel, 1);
//			}
//		}
//		public ComplexPanel(boolean b) {
//			for (int i : IntStream.range(0, 30).toArray()) {
//				Button button = new Button(" test ");
//				button.setWidth("100%");
//				addComponents(button);
//			}
//		}
	}
}