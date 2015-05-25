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
		
		int tab1 = sidePanel.addTab(FontAwesome.ADJUST, "sdf", new Label("test"));
		final int tab2 = sidePanel.addTab(FontAwesome.ADJUST, "sdf2", new Label("test2"));
		
		Button button = new Button("open tab2", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				sidePanel.setSelectedTab(tab2);
			}
		});
		button.setWidth("100%");
		sidePanel.setMainContent(button);
		
//		Grid content = new Grid("", createIndexedContainer());
//		content.setSizeFull();
//		CssLayout cssLayout = new CssLayout(content);
//		cssLayout.setSizeFull();
//		sidePanel.setMainContent(cssLayout);

//		Table tabcontent = new Table("", new FilesystemContainer(new File("/Users/mtzukanov/docs")));
//		tabcontent.setSizeFull();
//		sidePanel.addTab(null, "", tabcontent);
//		
//		createIndexedContainer();

		setContent(sidePanel);		
	}

//	@SuppressWarnings("unused")
//	private Container.Indexed createIndexedContainer() {
//		IndexedContainer indexed = new IndexedContainer();
//		indexed.addContainerProperty("1", String.class, "sdf");
//		indexed.addContainerProperty("2", String.class, "sdf");
//		indexed.addContainerProperty("3", String.class, "sdf");
//		indexed.addContainerProperty("4", String.class, "sdf");
//		
//		for (int i = 0; i < 5000; i++)
//		{
//			Item item = indexed.addItem(i);
//			item.getItemProperty("1").setValue("asdfsd");
//			item.getItemProperty("2").setValue("asdfsd");
//			item.getItemProperty("3").setValue("asdfsd");
//			item.getItemProperty("4").setValue("asdfsd");
//		}
//		
//		return indexed;
//	}
}