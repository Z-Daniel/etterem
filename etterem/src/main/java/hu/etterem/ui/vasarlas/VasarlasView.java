package hu.etterem.ui.vasarlas;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import hu.etterem.api.tetel.entity.Tetel;
import hu.etterem.ui.main.MainUI;

/**
 * Created by Murdoc on 4/14/2017.
 */
@SpringView(name = VasarlasView.VIEW_NAME, ui = MainUI.class)
public class VasarlasView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "VASARLAS_VIEW";
    public static final String CAPTION = "VÁSÁRLÁS";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        Tetel tetel = new Tetel();

        ComboBox termekId = new ComboBox();
        termekId.setCaption("Termék: ");

        ComboBox dolgozoId = new ComboBox();
        dolgozoId.setCaption("Vásárló: ");

        TextField darabSzam = new TextField("Darabszám: ");

        VerticalLayout root = new VerticalLayout();
        root.addComponents(termekId, darabSzam);

        BeanFieldGroup<Tetel> fieldGroup = new BeanFieldGroup<>(Tetel.class);
        fieldGroup.setItemDataSource(tetel);
        fieldGroup.bind(termekId,"termekId");
        fieldGroup.bind(darabSzam,"darabSzam");

        Button mentes = new Button("Mentés");
        mentes.addClickListener(clickEvent -> {
            try {
                fieldGroup.commit();
                System.out.print(tetel.getDarabSzam());
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }
        });
        root.addComponent(mentes);

        addComponents(dolgozoId,root);
    }
}
