package hu.etterem.ui.main;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import hu.etterem.ui.error.EmptyView;
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

//    @Autowired
//    private SpringNavigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout root = new HorizontalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        Panel viewContainer = new Panel();
        viewContainer.setWidth(90,Unit.PERCENTAGE);
        viewContainer.setSizeFull();

        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.setWidth(10,Unit.PERCENTAGE);
        menuLayout.addComponent(createNavigationButton(VasarlasView.CAPTION, VasarlasView.VIEW_NAME));

        root.addComponents(menuLayout,viewContainer);

        //név alapján navigál a view-ok között
        Navigator navigator = new Navigator(this,viewContainer);
        navigator.addProvider(viewProvider);
        navigator.navigateTo(EmptyView.VIEW_NAME);
        setNavigator(navigator);

//        navigator.init(this,viewContainer);
//        navigator.addView("error",ErrorView.class);
//        setNavigator(navigator);

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
