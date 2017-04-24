package hu.etterem.ui.report;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import hu.etterem.repository.dolgozo.DolgozoRepository;
import hu.etterem.repository.termek.TermekRepository;
import hu.etterem.riportEredm.RiportEredm;
import hu.etterem.ui.main.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Created by Zsidó Dániel on 4/19/2017.
 * A a kétféle riportot megjelenítő és exportálni képes felület.
 */
@SpringView(name = JelentesView.VIEW_NAME, ui = MainUI.class)
public class JelentesView extends VerticalLayout implements View {

    /**
     * A view name segítségévelhivatkozik rá a MainUi és érjük el egy gomb segítségével, melynek a felirata a caption.
     */
    public static final String VIEW_NAME = "REPORT_VIEW";
    public static final String CAPTION = "Jelentés";

    /**
     * Vaadin felületi komponensek.
     */
    private Grid riportGrid;
    private VerticalLayout root;
    private OptionGroup radioGombok;

    private ArrayList<RiportEredm> riportList;

    /**
     * A dolgozó táblát elérő interface.
     */
    @Autowired
    private DolgozoRepository dolgozoRepository;

    /**
     * A termék táblát elérő interface.
     */
    @Autowired
    private TermekRepository termekRepository;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        Button megjelenit;
        Button exportCSV;

        /**
         * A View felépítése.
         */
        root = new VerticalLayout(
                radioGombok = new OptionGroup(),
                megjelenit = new Button("Megjelenítés"),
                exportCSV = new Button("Export"),
                riportGrid = new Grid()
        );
        riportGrid.setSelectionMode(Grid.SelectionMode.NONE);
        riportGrid.setSizeFull();
        radioGombok.addItem("Dolgozó riport");
        radioGombok.addItem("Termék riport");
        radioGombok.setValue("Dolgozó riport");

        /**
         * A kiválasztott lekérdezés gridben történő megjelenítését szolgáló funkciógomb működése.
         */
        megjelenit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                riportList = new ArrayList<RiportEredm>();
                /**
                 * A jelen hónapban levásárolt egyenleg dolgozónként.
                 */
                if (radioGombok.getValue().equals("Dolgozó riport")) {
                    /**
                     * A grid hozzászabása a kiválasztott riporthoz.
                     */
                    riportGrid.removeAllColumns();

                    BeanItemContainer<RiportEredm> container = new BeanItemContainer<>(RiportEredm.class);
                    riportGrid.setContainerDataSource(container);
                    setupRiportGrid("Dolgozó név", "Havi fogyasztás");

                    /**
                     * Az objectList tartalmazza a lekérdezés eredményét, ami a riport.
                     */
                    List<Object[]> objectList = getObjectList(radioGombok.getValue().toString());//Objektum tömbök vannak a listában, egy objektum tömb egy sor a tömb elemei az oszlopok

                    /**
                     * A grid adatforrásába tölti az object list tartalmát
                     */
                    objectList.forEach(objects -> container.addItem(new RiportEredm(objects[0].toString(), Integer.valueOf(objects[1].toString()))));

                    /**
                     * A jelen hónapban elfogyott mennyiségek termékenként.
                     */
                } else if (radioGombok.getValue().equals("Termék riport")) {
                    riportGrid.removeAllColumns();

                    BeanItemContainer<RiportEredm> container = new BeanItemContainer<>(RiportEredm.class);
                    riportGrid.setContainerDataSource(container);
                    setupRiportGrid("Termékek", "Havi fogyasztás");

                    List<Object[]> objectList = getObjectList(radioGombok.getValue().toString());
                    objectList.forEach(objects -> container.addItem(new RiportEredm(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
                }
            }
        });

        /**
         * Az alábbi exportCSV funkciógomb segítségével, a felhasználó letöltheti a rádiógombokkal kijelölt riport
         * tartalmát egy csv fájlba, melynek neve tartalmazza a riport típusát és az aktuális dátumot.
         */
        exportCSV.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                riportList = new ArrayList<>();
                List<Object[]> objectList = getObjectList(radioGombok.getValue().toString());
                /**
                 * Az objectList tartalmát (a lekérdezés eredményét) soronként példányosítjuk egy riportList ArrayListbe,
                 * ami a RiportEredm osztály szerkezetét használja.
                 */
                objectList.forEach(objects -> riportList.add(new RiportEredm(objects[0].toString(), Integer.valueOf(objects[1].toString()))));

                if (riportList != null && !riportList.isEmpty()) {
                    try {
                        /**
                         * Az inputStreambe beállításra kerül a tartalomhoz tartozó encoding, majd attól függően kap a
                         * letölthető fájl nevet, hogy milyen típusú riport kerül bele.
                         */
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContentAdapter().getBytes("windows-1250"));
                        if (radioGombok.getValue() == "Dolgozó riport") {
                            Page.getCurrent().open(new StreamResource((StreamResource.StreamSource) () -> inputStream, "ETTEREM_REPORT_" + getDate4CsvName() + "_DOLGOZO.csv"), null, false);
                        } else if (radioGombok.getValue() == "Termék riport") {
                            Page.getCurrent().open(new StreamResource((StreamResource.StreamSource) () -> inputStream, "ETTEREM_REPORT_" + getDate4CsvName() + "_TERMEK.csv"), null, false);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        addComponent(root);
    }

    /**
     * Feltölti az object listet annak megfelelően, hogy melyik riportot kéri a felhasználó.
     *
     * @param riportTipus két lehetséges állapottal rendelkező paraméter, ami meghatározza a riport típusát.
     * @return objectList a kért lekérdezés eredménye
     */
    private List<Object[]> getObjectList(String riportTipus) {
        if (riportTipus.equals("Dolgozó riport")) {
            return dolgozoRepository.dolgozokFogyasztas(getStartDate(), getEndDate());
        } else {
            return termekRepository.termekekFogyasa(getStartDate(), getEndDate());
        }
    }

    /**
     * A riportot megjelnítő kétoszlopos grid beállítása.
     *
     * @param elsoOszlopNeve
     * @param masodikOszlopNeve
     */
    private void setupRiportGrid(String elsoOszlopNeve, String masodikOszlopNeve) {
        riportGrid.getColumn("nev").setHeaderCaption(elsoOszlopNeve);//az oszlopok azonosítói a gridben lévő datasource objektumának mezői
        riportGrid.getColumn("mennyiseg").setHeaderCaption(masodikOszlopNeve);
        riportGrid.setSelectionMode(Grid.SelectionMode.NONE);
        riportGrid.setColumnOrder("nev", "mennyiseg");
        root.addComponent(riportGrid);
        riportGrid.setSelectionMode(Grid.SelectionMode.NONE);
        riportGrid.setSizeFull();
        root.setComponentAlignment(riportGrid, Alignment.MIDDLE_CENTER);
    }

    /**
     * A CSV fájl tartalmának összeállítása egy String-be.
     *
     * @return Az exportálható CSV fájl tartalma.
     */
    private String csvContentAdapter() {
        StringJoiner csvContent = new StringJoiner("\n");
        riportList.forEach(o -> {
            StringJoiner csvRow = new StringJoiner(";");
            RiportEredm objects = (RiportEredm) o;
            Stream.of(objects).forEach(o1 -> {
                csvRow.add(o1.getNev());
                csvRow.add(o1.getMennyiseg().toString());
            });
            csvContent.add(csvRow.toString());
        });
        return csvContent.toString();
    }

    /**
     * Az aktuális dátum meghatározása a CSV fájl nevéhez.
     *
     * @return aktuális dátum a megadott formátumban
     */
    private String getDate4CsvName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(new Date());
        return strDate;
    }

    /**
     * Aktuális hónap első napjához tartozó dátum meghatározása a lekérdezésekhez.
     */
    private Date getStartDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();

    }

    /**
     * Aktuális hónap utolsó napjához tartozó dátum meghatározása a lekérdezésekhez.
     */
    private Date getEndDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

}
