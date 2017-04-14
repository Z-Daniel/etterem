package hu.etterem.ui.main;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import hu.etterem.ui.error.ErrorView;
import hu.etterem.ui.vasarlas.VasarlasView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Title("Étterem üzemeltetés")
@Theme("valo")
@SpringUI
@PreserveOnRefresh
public class MainUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0F);
        root.addComponents(createNavigationButton(VasarlasView.CAPTION, VasarlasView.VIEW_NAME));

        Navigator navigator = new SpringNavigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(ErrorView.class);
        setNavigator(navigator);

    }

    private Button createNavigationButton(String caption, String viewName) {
        Button button = new Button(caption);

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getCurrent().getNavigator().navigateTo(viewName);
            }
        });
        return button;
    }
}
