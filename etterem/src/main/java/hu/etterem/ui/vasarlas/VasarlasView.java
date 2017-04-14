package hu.etterem.ui.vasarlas;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import hu.etterem.api.dolgozo.entity.Dolgozo;
import hu.etterem.api.termek.entity.Termek;
import hu.etterem.api.tetel.entity.Tetel;
import hu.etterem.ui.main.MainUI;

/**
 * Created by Murdoc on 4/14/2017.
 */
@SpringView(name = VasarlasView.VIEW_NAME, ui = MainUI.class)
public class VasarlasView extends VerticalLayout implements View{

    public static final String VIEW_NAME = "VASARLAS_VIEW";
    public static final String CAPTION = "VÁSÁRLÁS";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        Tetel tetel = new Tetel();

        ComboBox<Termek> termekId = new ComboBox<Termek>();
        termekId.setCaption("Termék: ");

        ComboBox<Dolgozo> dolgozoId = new ComboBox<Dolgozo>();
        dolgozoId.setCaption("Vásárló: ");

        TextField darabSzam = new TextField("Darabszám: ");

        Binder<Tetel> binder = new Binder<Tetel>();
        binder.readBean(tetel);

        VerticalLayout root = new VerticalLayout();
        root.addComponents(termekId,darabSzam);

        binder.bindInstanceFields(root);

        addComponent(root);
    }
}
