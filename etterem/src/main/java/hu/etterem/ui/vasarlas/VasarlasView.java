package hu.etterem.ui.vasarlas;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import hu.etterem.api.dolgozo.entity.Dolgozo;
import hu.etterem.api.termek.entity.Termek;
import hu.etterem.api.tetel.entity.Tetel;
import hu.etterem.api.vasarlas.entity.Vasarlas;
import hu.etterem.repository.dolgozo.DolgozoRepository;
import hu.etterem.repository.termek.TermekRepository;
import hu.etterem.repository.vasarlas.VasarlasRepository;
import hu.etterem.ui.main.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Murdoc on 4/14/2017.
 */
@SpringView(name = VasarlasView.VIEW_NAME, ui = MainUI.class)
public class VasarlasView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "VASARLAS_VIEW";
    public static final String CAPTION = "VÁSÁRLÁS";
    private Tetel tetel = new Tetel();
    private BeanFieldGroup<Tetel> fieldGroup;
    private ComboBox dolgozoId;
    private ComboBox termekId;
    private TextField darabSzam;

    @Autowired
    private TermekRepository termekRepository;

    @Autowired
    private VasarlasRepository vasarlasRepository;

    @Autowired
    private DolgozoRepository dolgozoRepository;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        BeanItemContainer<Tetel> container = new BeanItemContainer<Tetel>(Tetel.class);

        HorizontalLayout fieldsLayout;
        HorizontalLayout buttonsLayout;
        Button hozzaad;
        Button torles;
        Button mentes;
        Grid grid;

        VerticalLayout root = new VerticalLayout(
                fieldsLayout = new HorizontalLayout(
                    dolgozoId = new ComboBox(),
                    termekId = new ComboBox(),
                    darabSzam = new TextField("Darabszám: ")
                ),
                buttonsLayout = new HorizontalLayout(
                    hozzaad = new Button("Hozzáadás"),
                    torles = new Button("Törlés")
                ),
                grid = new Grid(),
                mentes = new Button("Mentés")
        );

        termekId.setCaption("Termék: ");
        termekId.addItems(termekRepository.findAll());

        dolgozoId.setCaption("Vásárló: ");
        dolgozoId.addItems(dolgozoRepository.findAll());

        //hozzárendeli az objektum fieldjeit a textfieldekhez (ezt minden új példányosításnál meg kell tenni)
        fieldGroup = new BeanFieldGroup<>(Tetel.class);
        bind();


        hozzaad.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    fieldGroup.commit();
                    container.addItem(tetel);
                    tetel = new Tetel();
                    bind();
                } catch (FieldGroup.CommitException e) {
                    Notification.show("Csak nullánál nagyobb darabszámú tétel vehető fel!", Notification.Type.HUMANIZED_MESSAGE);
                    e.printStackTrace();
                }
            }
        });

        torles.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                container.removeItem(tetel);
            }
        });

        grid.addSelectionListener(selectionEvent -> {
            if (grid.getSelectedRow() != null) {
                tetel = (Tetel) grid.getSelectedRow();
                bind();
            } else {
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

        mentes.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Vasarlas vasarlas = new Vasarlas();
                vasarlas.getTetelekSet().addAll((Collection<? extends Tetel>) grid.getContainerDataSource().getItemIds());
                vasarlas.getTetelekSet().forEach(tetel1 -> tetel1.setVasarlasId(vasarlas)); //azért kell mert a tételek (külön) táblába mentjük a tételeket, ahol jelölni kell, hogy melyik vásárláshoz tartoznak

                //végösszeg kiszámítása úgy, hogy kikeresi a termék táblából a tételhez rendelt termék alapján az egységárat, beszorozza a tételhez tartozó mennyiséggel és hozzáadja a végösszeghez
                Integer vegosszeg = 0;
                for (Tetel curTetel: vasarlas.getTetelekSet()) {
                    Termek curTerm = termekRepository.findOne(curTetel.getTermekId().getId());
                    vegosszeg += curTetel.getDarabSzam()*curTerm.getAr();
                }
                vasarlas.setVegosszeg(vegosszeg);

                //vasarlas.setVegosszeg();
                vasarlas.setDolgozoId((Dolgozo) dolgozoId.getValue());

                vasarlas.setVasarlasDatuma(new Date());
                vasarlasRepository.save(vasarlas);
                grid.getContainerDataSource().removeAllItems();
                Notification.show("A vásárlás sikeres és rögzítésre került.", Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        addComponents(root);
    }

    private void bind() {
        fieldGroup.setItemDataSource(tetel);
        fieldGroup.bind(termekId, "termekId");
        fieldGroup.bind(darabSzam, "darabSzam");
    }

}
