package hu.etterem.ui.vasarlas;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import hu.etterem.api.dolgozo.entity.Dolgozo;
import hu.etterem.api.tetel.entity.Tetel;
import hu.etterem.api.vasarlas.entity.Vasarlas;
import hu.etterem.repository.termek.TermekRepository;
import hu.etterem.repository.vasarlas.VasarlasRepository;
import hu.etterem.ui.main.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Murdoc on 4/14/2017.
 */
@SpringView(name = VasarlasView.VIEW_NAME, ui = MainUI.class)
public class VasarlasView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "VASARLAS_VIEW";
    public static final String CAPTION = "VÁSÁRLÁS";
    private Tetel tetel = new Tetel();
    private BeanFieldGroup<Tetel> fieldGroup;
    private ComboBox termekId;
    private TextField darabSzam;

    @Autowired
    private TermekRepository termekRepository;

    @Autowired
    private VasarlasRepository vasarlasRepository;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        BeanItemContainer<Tetel> container = new BeanItemContainer<Tetel>(Tetel.class);

        termekId = new ComboBox();
        termekId.setCaption("Termék: ");
        termekId.addItems(termekRepository.findAll());

        ComboBox dolgozoId = new ComboBox();
        dolgozoId.setCaption("Vásárló: ");
        dolgozoId.addItems(vasarlasRepository.findAll());

        darabSzam = new TextField("Darabszám: ");

        VerticalLayout root = new VerticalLayout();
        root.addComponents(termekId, darabSzam);

        //hozzárendeli az objektum fieldjeit a textfieldekhez (ezt minden új példányosításnál meg kell tenni)
        fieldGroup = new BeanFieldGroup<>(Tetel.class);
        bind();

        Button hozzaad = new Button("Hozzáadás");
        hozzaad.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    fieldGroup.commit();
                    container.addItem(tetel);
                    tetel = new Tetel();
                    bind();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }
            }
        });
        root.addComponent(hozzaad);

        Button torles = new Button("Törlés");
        torles.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                container.removeItem(tetel);
            }
        });
        root.addComponents(torles);

        Grid grid = new Grid();
        grid.addSelectionListener(selectionEvent -> {
            if (grid.getSelectedRow()!=null){
                tetel = (Tetel) grid.getSelectedRow();
                bind();
            }else{
                tetel = new Tetel();
                bind();
            }
        });
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setContainerDataSource(container);
        grid.removeAllColumns();
        grid.addColumn("darabSzam").setHeaderCaption("Darabszám");
        grid.addColumn("termekId").setHeaderCaption("Termék");
        root.addComponents(grid);

        Button mentes = new Button("Mentés");
        mentes.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Vasarlas vasarlas = new Vasarlas();
                vasarlas.getTetelekSet().addAll((Collection<? extends Tetel>) grid.getContainerDataSource().getItemIds());
                vasarlas.getTetelekSet().forEach(tetel1 -> tetel1.setVasarlasId(vasarlas));
                vasarlas.setDolgozoId((Dolgozo) dolgozoId.getValue());
                vasarlas.setVasarlasDatuma(new Date());
                vasarlasRepository.save(vasarlas);
                grid.getContainerDataSource().removeAllItems();
                Notification.show("KABBE","FAROK", Notification.Type.ERROR_MESSAGE);
            }
        });
        root.addComponents(mentes);

        addComponents(dolgozoId, root);
    }

    private void bind() {
        fieldGroup.setItemDataSource(tetel);
        fieldGroup.bind(termekId, "termekId");
        fieldGroup.bind(darabSzam, "darabSzam");
    }
}
