package hu.etterem.ui.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import hu.etterem.ui.main.MainUI;

/**
 * Created by Murdoc on 4/14/2017.
 */
@SpringView(name = "error",ui = MainUI.class)
public class ErrorView implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
