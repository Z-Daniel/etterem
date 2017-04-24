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
 * Created by Zsidó Dániel on 4/14/2017.
 * A VásárlásView-n keresztül lehet felvenni egy új vásárlás tételeit.
 */
@SpringView(name = VasarlasView.VIEW_NAME, ui = MainUI.class)
public class VasarlasView extends VerticalLayout implements View {

    /**
     * A view name segítségévelhivatkozik rá a MainUi és érjük el egy gomb segítségével, melynek a felirata a caption.
     */
    public static final String VIEW_NAME = "VASARLAS_VIEW";
    public static final String CAPTION = "Vásárlás";

    /**
     * A vásárlás egy tétele, amit a gridbe felvehet a felhasználó.
     */
    private Tetel tetel = new Tetel();

    /**
     * A fieldGroup bindolja össze a megfelelő entitás propertyket a darabSzam szövegdoboz és a termekId legördülő tartalmával.
     */
    private BeanFieldGroup<Tetel> fieldGroup;

    /**
     * A container tartalmazza a grid által megjelenített tételeket.
     */
    private BeanItemContainer<Tetel> container;

    /**
     * A vásárlás entitáshoz, amit a mentes gombbal hoz létre a felhasználó, tartoznia kell egy dolgozónak,
     * akit ebből a legördülőből választhat ki.
     */
    private ComboBox dolgozoId;

    /**
     * Egy tételhez tartozó termékfajta kiválasztását célzó legördülő.
     */
    private ComboBox termekId;

    /**
     * Egy tételhez tartozó, vásárolt darabszám megadását célzó szövegdoboz.
     */
    private TextField darabSzam;

    /**
     * Jelzi, ha a felveendő új tétel, vagy a módosított tétel olyan termékfajtával rendelkezik, ami már
     * szerepel a tétellistában.
     */
    private boolean found = false;

    /**
     * Vaadinos felületi komponensek.
     */
    private VerticalLayout root;
    private Grid tetelekGrid;
    private HorizontalLayout formLayout;
    private HorizontalLayout tetelGombokLayout;
    private HorizontalLayout globalGombokLayout;
    private Button hozzaad;
    private Button torles;
    private Button mentes;
    private Button osszesTorlese;
    private VerticalLayout gombokEsGridLayout;

    /**
     * A termék tábla elérésére szolgáló interface.
     */
    @Autowired
    private TermekRepository termekRepository;

    /**
     * A vásárlás táblát elérő interface.
     */
    @Autowired
    private VasarlasRepository vasarlasRepository;

    /**
     * A dolgozó táblát elérő interface.
     */
    @Autowired
    private DolgozoRepository dolgozoRepository;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        container = new BeanItemContainer<Tetel>(Tetel.class);
        /**
        * A View felépítése.
        */
        root = new VerticalLayout(
                formLayout = new HorizontalLayout(
                        dolgozoId = new ComboBox(),
                        termekId = new ComboBox(),
                        darabSzam = new TextField("Darabszám: ")
                ),
                gombokEsGridLayout = new VerticalLayout(
                        tetelGombokLayout = new HorizontalLayout(
                                hozzaad = new Button("Hozzáadás/Módosítás"),
                                torles = new Button("Törlés")
                        ),
                        tetelekGrid = new Grid("A felvett tételek: "),
                        globalGombokLayout = new HorizontalLayout(
                                mentes = new Button("Mentés"),
                                osszesTorlese = new Button("Összes törlése")
                        )
                )
        );

        /**
         * Felcímkézi a termekId legördülőt és a repository segítségével lekéri bele a termek tábla tartalmát.
         */
        termekId.setCaption("Termék: ");
        termekId.addItems(termekRepository.findAll());

        /**
         * Felcímkézi a dolgozoId legördülőt és a repository segítségével lekéri bele a dolgozó tábla tartalmát.
         */
        dolgozoId.setCaption("Vásárló: ");
        dolgozoId.addItems(dolgozoRepository.findAll());

        fieldGroup = new BeanFieldGroup<>(Tetel.class);
        bind();

        /**
         * A hozzáadás/módosítás gomb lenyomásával megtörténő utasítások.
         */
        hozzaad.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    fieldGroup.commit();
                    List<Tetel> validateList = new ArrayList<>();
                    validateList.addAll(container.getItemIds());
                    /**
                     * Itt kezeli azt, ha már létező tételfajtára módosítanak. (hozzáadja a másik létezőhöz a darabszámát.)
                     * Ha nem volt kijelölés, tehát nem módosítás, hanem új tétel felvétele történik, akkor is ellenőrzi ezt
                     * az alábbi foreach.
                     */
                    if (!tetelekGrid.getSelectedRows().isEmpty()) {
                        Tetel modositottTetel = (Tetel) tetelekGrid.getSelectedRow();
                        validateList.remove(modositottTetel);
                    }
                    validateList.forEach(curTetel -> {
                        if (curTetel.getTermekId() == tetel.getTermekId() && found == false) {
                            curTetel.setDarabSzam(curTetel.getDarabSzam() + tetel.getDarabSzam());
                            found = true;
                        }
                    });
                    if (!found) {
                        validateList.add(tetel);
                    } else {
                        found = false;
                    }
                    /**
                     * Az átmeneti validateList tartalmának betöltése a containerbe, és a container tartalmának betöltése a gridbe.
                     */
                    container = new BeanItemContainer<Tetel>(Tetel.class);
                    container.addAll(validateList);
                    tetelekGrid.setContainerDataSource(container);
                    tetel = new Tetel();
                    bind();
                } catch (FieldGroup.CommitException e) {
                    if (Integer.valueOf(darabSzam.getValue()) <= 0) {
                        Notification.show("Csak nullánál nagyobb darabszámú tétel vehető fel!", Notification.Type.HUMANIZED_MESSAGE);
                    }
                    e.printStackTrace();
                }
            }
        });
        /**
        * Kiválasztott tétel törlése a listából.
        */
        torles.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                container.removeItem(tetel);
            }
        });
        /**
         * Ha a felhasználó kiválaszt egy tételt a gridből, akkor az belekerül a tetel objectbe.
         */
        tetelekGrid.addSelectionListener(selectionEvent -> {
            if (tetelekGrid.getSelectedRow() != null) {
                tetel = (Tetel) tetelekGrid.getSelectedRow();
                bind();
            } else {
                tetel = new Tetel();
                bind();
            }
        });

        gridSetup(container);
        layoutAdjustents();
        /**
         * A mentés során létrejön a vásárlás entitás, amit itt fel kell építeni és a tételeket hozzá kell rendelni,
         * a tétel gyűjteményt és a vásárlást menteni az adatbázisba.
         */
        mentes.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!dolgozoId.isEmpty() && tetelekGrid.getContainerDataSource().size() > 0) {
                    Vasarlas vasarlas = new Vasarlas();
                    vasarlas.getTetelekSet().addAll((Collection<? extends Tetel>) tetelekGrid.getContainerDataSource().getItemIds());
                    /**
                     * Itt tölti fel a tételeket a vásárlás azonosítójával.
                     */
                    vasarlas.getTetelekSet().forEach(tetel1 -> tetel1.setVasarlasId(vasarlas));

                    /**
                     * A vásárlás rekordban feljegyzett végösszeg kiszámítása.
                     */
                    Integer vegosszeg = 0;
                    for (Tetel curTetel : vasarlas.getTetelekSet()) {
                        vegosszeg += curTetel.getDarabSzam() * curTetel.getTermekId().getAr();
                    }
                    vasarlas.setVegosszeg(vegosszeg);

                    /**
                     * A dolgozó és a mai dátum hozzárendelése a vásárláshoz.
                     */
                    vasarlas.setDolgozoId((Dolgozo) dolgozoId.getValue());
                    vasarlas.setVasarlasDatuma(new Date());

                    /**
                     * A vásárlás mentése adatbázisba (a tételek is mentésre kerülnek a tetelekSeten keresztül).
                     */
                    vasarlasRepository.save(vasarlas);
                    container.removeAllItems();
                    Notification.show("A vásárlás sikeres és rögzítésre került.", Notification.Type.HUMANIZED_MESSAGE);
                } else if (dolgozoId.isEmpty()) {
                    Notification.show("Válasszon dolgozót a vásárláshoz!", Notification.TYPE_HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Adjon tételeket a vásárláshoz!", Notification.TYPE_HUMANIZED_MESSAGE);
                }
            }
        });

        /**
         * Minden tétel törlését lehetővé tévő gomb.
         */
        osszesTorlese.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                tetelekGrid.getContainerDataSource().removeAllItems();
            }
        });

        globalGombokLayout.setComponentAlignment(osszesTorlese, Alignment.MIDDLE_RIGHT);
        root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponents(root);
    }

    /**
     * Hozzárendeli az objektum fieldjeit a felület komponenseihez (ezt minden új, fieldgroup példányosításnál meg kell tenni).
     */
    private void bind() {
        fieldGroup.setItemDataSource(tetel);
        fieldGroup.bind(termekId, "termekId");
        fieldGroup.bind(darabSzam, "darabSzam");
    }

    /**
     * A grid alap jellemzőinek beállítása.
     * @param container a grid tartalmát meghatározó adatforrás
     */
    private void gridSetup(BeanItemContainer<Tetel> container) {
        tetelekGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        tetelekGrid.setContainerDataSource(container);
        tetelekGrid.removeAllColumns();
        tetelekGrid.addColumn("termekId").setHeaderCaption("Termék");
        tetelekGrid.addColumn("darabSzam").setHeaderCaption("Darabszám");
        tetelekGrid.setWidth("100%");
    }

    /**
     * A komponensek rendezgetését végző metódus.
     */
    private void layoutAdjustents() {
        root.setComponentAlignment(gombokEsGridLayout, Alignment.MIDDLE_CENTER);
        tetelGombokLayout.setWidth("100%");
        tetelGombokLayout.setComponentAlignment(torles, Alignment.MIDDLE_RIGHT);
        root.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
        formLayout.setComponentAlignment(darabSzam, Alignment.MIDDLE_RIGHT);
        formLayout.setComponentAlignment(termekId, Alignment.MIDDLE_CENTER);
        formLayout.setComponentAlignment(dolgozoId, Alignment.MIDDLE_LEFT);
        globalGombokLayout.setWidth("100%");
        globalGombokLayout.setComponentAlignment(osszesTorlese, Alignment.MIDDLE_RIGHT);
        gombokEsGridLayout.setWidth("90%");
        formLayout.setWidth("100%");
    }

}
