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
import hu.etterem.ui.report.JelentesView;
import hu.etterem.ui.vasarlas.VasarlasView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Zsidó Dániel on 4/14/2017.
 * A menü, illetve a main screen view felépítése.
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
        HorizontalLayout root = new HorizontalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        Panel viewContainer = new Panel();
        viewContainer.setSizeFull();

        /**
         * A többi view-ra irányító gombok layout-ja.
         */
        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.addComponent(createNavigationButton(VasarlasView.CAPTION, VasarlasView.VIEW_NAME));
        menuLayout.addComponent(createNavigationButton(JelentesView.CAPTION, JelentesView.VIEW_NAME));
        menuLayout.setWidth(10,Unit.PERCENTAGE);

        root.addComponents(menuLayout,viewContainer);

        /**
         * Név alapján navigál a view-ok között.
         */
        Navigator navigator = new Navigator(this,viewContainer);
        navigator.addProvider(viewProvider);
        navigator.navigateTo(EmptyView.VIEW_NAME);
        setNavigator(navigator);

    }

    /**
     * A gombok létrehozásáért felelős metódus.
     * @param caption  a gomb felirata
     * @param viewName a view neve, amire mutat a gomb
     * @return
     */
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
