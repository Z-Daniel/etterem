package hu.etterem.ui.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import hu.etterem.ui.main.MainUI;

/**
 * Created by Murdoc on 4/14/2017.
 */
@SpringView(name = EmptyView.VIEW_NAME,ui = MainUI.class)
public class EmptyView extends CssLayout implements View {
    public static final String VIEW_NAME = "EMPTY_VIEW";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
