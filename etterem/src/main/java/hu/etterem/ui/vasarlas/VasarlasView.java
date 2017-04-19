package hu.etterem.ui.vasarlas;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.etterem.api.dolgozo.entity.Dolgozo;
import hu.etterem.api.termek.entity.Termek;
import hu.etterem.api.tetel.entity.Tetel;
import hu.etterem.api.vasarlas.entity.Vasarlas;
import hu.etterem.repository.dolgozo.DolgozoRepository;
import hu.etterem.repository.termek.TermekRepository;
import hu.etterem.repository.vasarlas.VasarlasRepository;
import hu.etterem.ui.main.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Murdoc on 4/14/2017.
 * TODO Ha már van egy tétel a gridben akkor ne lehessen dolgozót változtatni
 * TODO Workaround a módosítás bugolása miatt
 * TODO Annak az esetleges kezelése, hogy egy termékfajtát többször adnak a gridhez
 */
@SpringView(name = VasarlasView.VIEW_NAME, ui = MainUI.class)
public class VasarlasView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "VASARLAS_VIEW";
    public static final String CAPTION = "Vásárlás";
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

        HorizontalLayout formLayout;
        HorizontalLayout tetelGombokLayout;
        HorizontalLayout globalGombokLayout;
        Button hozzaad;
        Button torles;
        Button mentes;
        Button osszesTorlese;
        Grid tetelekGrid;
        VerticalLayout gombokEsGridLayout;

        VerticalLayout root = new VerticalLayout(
                formLayout = new HorizontalLayout(
                    dolgozoId = new ComboBox(),
                    termekId = new ComboBox(),
                    darabSzam = new TextField("Darabszám: ")
                ),
                gombokEsGridLayout = new VerticalLayout(
                    tetelGombokLayout = new HorizontalLayout(
                        hozzaad = new Button("Hozzáadás"),
                        torles = new Button("Törlés")
                    ),
                    tetelekGrid = new Grid("A felvett tételek: "),
                    globalGombokLayout = new HorizontalLayout(
                       mentes = new Button("Mentés"),
                       osszesTorlese = new Button("Összes törlése")
                    )
                )
        );

        termekId.setCaption("Termék: ");
        termekId.addItems(termekRepository.findAll());

        dolgozoId.setCaption("Vásárló: ");
        dolgozoId.addItems(dolgozoRepository.findAll());

        //hozzárendeli az objektum fieldjeit a textfieldekhez (ezt minden új példányosításnál meg kell tenni)
        fieldGroup = new BeanFieldGroup<>(Tetel.class);
        formLayout.setWidth("100%");
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

        tetelekGrid.addSelectionListener(selectionEvent -> {
            if (tetelekGrid.getSelectedRow() != null) {
                tetel = (Tetel) tetelekGrid.getSelectedRow();
                bind();
            } else {
                tetel = new Tetel();
                bind();
            }
        });

        tetelekGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        tetelekGrid.setContainerDataSource(container);
        tetelekGrid.removeAllColumns();
        tetelekGrid.addColumn("termekId").setHeaderCaption("Termék");
        tetelekGrid.addColumn("darabSzam").setHeaderCaption("Darabszám");
        tetelekGrid.setWidth("100%");
        root.setComponentAlignment(gombokEsGridLayout,Alignment.MIDDLE_CENTER);
        tetelGombokLayout.setWidth("100%");
        tetelGombokLayout.setComponentAlignment(torles,Alignment.MIDDLE_RIGHT);
        root.setComponentAlignment(formLayout,Alignment.MIDDLE_CENTER);
        formLayout.setComponentAlignment(darabSzam,Alignment.MIDDLE_RIGHT);
        formLayout.setComponentAlignment(termekId,Alignment.MIDDLE_CENTER);
        formLayout.setComponentAlignment(dolgozoId,Alignment.MIDDLE_LEFT);
        globalGombokLayout.setWidth("100%");
        globalGombokLayout.setComponentAlignment(osszesTorlese,Alignment.MIDDLE_RIGHT);
        gombokEsGridLayout.setWidth("90%");

        mentes.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Vasarlas vasarlas = new Vasarlas();
                vasarlas.getTetelekSet().addAll((Collection<? extends Tetel>) tetelekGrid.getContainerDataSource().getItemIds());
                vasarlas.getTetelekSet().forEach(tetel1 -> tetel1.setVasarlasId(vasarlas)); //azért kell mert a tételek (külön) táblába mentjük a tételeket, ahol jelölni kell, hogy melyik vásárláshoz tartoznak

                //végösszeg kiszámítása úgy, hogy kikeresi a termék táblából a tételhez rendelt termék alapján az egységárat, beszorozza a tételhez tartozó mennyiséggel és hozzáadja a végösszeghez
                Integer vegosszeg = 0;
                for (Tetel curTetel : vasarlas.getTetelekSet()) {
                    Termek curTerm = termekRepository.findOne(curTetel.getTermekId().getId());
                    vegosszeg += curTetel.getDarabSzam() * curTerm.getAr();
                }

                vasarlas.setVegosszeg(vegosszeg);
                vasarlas.setDolgozoId((Dolgozo) dolgozoId.getValue());
                vasarlas.setVasarlasDatuma(new Date());

                vasarlasRepository.save(vasarlas);
                tetelekGrid.getContainerDataSource().removeAllItems();
                Notification.show("A vásárlás sikeres és rögzítésre került.", Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        osszesTorlese.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                tetelekGrid.getContainerDataSource().removeAllItems();
            }
        });

        globalGombokLayout.setComponentAlignment(osszesTorlese,Alignment.MIDDLE_RIGHT);
        root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponents(root);
    }

    private void bind() {
        fieldGroup.setItemDataSource(tetel);
        fieldGroup.bind(termekId, "termekId");
        fieldGroup.bind(darabSzam, "darabSzam");
    }

}
