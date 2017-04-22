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

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Created by Murdoc on 4/19/2017.
 */
@SpringView(name = JelentesView.VIEW_NAME, ui = MainUI.class)
public class JelentesView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "REPORT_VIEW";
    public static final String CAPTION = "Jelentés";

    private Grid riportGrid;

    private VerticalLayout root;

    private OptionGroup radioGombok;

    private ArrayList<RiportEredm> riportList;

    @Autowired
    private DolgozoRepository dolgozoRepository;

    @Autowired
    private TermekRepository termekRepository;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        Button megjelenit;
        Button exportCSV;

        root = new VerticalLayout(
                radioGombok = new OptionGroup(),
                megjelenit = new Button("Megjelenítés"),
                exportCSV = new Button("Export"),
                riportGrid = new Grid()
        );
        setCommonGridPorps();
        radioGombok.addItem("Dolgozó riport");
        radioGombok.addItem("Termék riport");
        radioGombok.setValue("Dolgozó riport");

        megjelenit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                riportList = new ArrayList<RiportEredm>();
                if(radioGombok.getValue() == "Dolgozó riport"){
                    riportGrid.removeAllColumns();

                    BeanItemContainer<RiportEredm> container = new BeanItemContainer<>(RiportEredm.class);
                    riportGrid.setContainerDataSource(container);
                    riportGrid.getColumn("nev").setHeaderCaption("Dolgozó név");
                    riportGrid.getColumn("mennyiseg").setHeaderCaption("Havi fogyasztás");
                    riportGrid.setSelectionMode(Grid.SelectionMode.NONE);
                    riportGrid.setColumnOrder("nev","mennyiseg");
                    root.addComponent(riportGrid);
                    setCommonGridPorps();

                    List<Object[]> objectList = getObjectList(radioGombok.getValue().toString());//Objektum tömbök vannak a listában, egy objektum tömb egy sor a tömb elemei az oszlopok
                    objectList.forEach(objects -> container.addItem(new RiportEredm(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
                }else if(radioGombok.getValue() == "Termék riport"){
                    riportGrid.removeAllColumns();

                    BeanItemContainer<RiportEredm> container = new BeanItemContainer<>(RiportEredm.class);
                    riportGrid.setContainerDataSource(container);
                    riportGrid.getColumn("nev").setHeaderCaption("Termékek");//az oszlopok azonosítói a gridben lévő datasource objektumának mezői
                    riportGrid.getColumn("mennyiseg").setHeaderCaption("Havi fogyás");
                    riportGrid.setColumnOrder("nev","mennyiseg");
                    root.addComponent(riportGrid);
                    setCommonGridPorps();

                    List<Object[]> objectList = getObjectList(radioGombok.getValue().toString());
                    objectList.forEach(objects -> container.addItem(new RiportEredm(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
                }
            }
        });

        exportCSV.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                riportList = new ArrayList<>();
                List<Object[]> objectList = getObjectList(radioGombok.getValue().toString());
                objectList.forEach(objects -> riportList.add(new RiportEredm(objects[0].toString(), Integer.valueOf(objects[1].toString()))));

                if(riportList != null && !riportList.isEmpty()&&radioGombok.getValue() == "Dolgozó riport"){
                    Page.getCurrent().open(new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(csvContentAdapter().getBytes()), "ETTEREM_REPORT_" + getDate4CsvName() + "_DOLGOZO.csv"), null, false);
                }else if(riportList != null && !riportList.isEmpty()&&radioGombok.getValue() == "Termék riport"){
                    Page.getCurrent().open(new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(csvContentAdapter().getBytes()), "ETTEREM_REPORT_" + getDate4CsvName() + "_TERMEK.csv"), null, false);
                }
            }
        });

        addComponent(root);
    }

    private List<Object[]> getObjectList(String riportTipus){
        if (riportTipus == "Dolgozó riport"){
            return dolgozoRepository.dolgozokFogyasztas(getStartDate(), getEndDate());
        }else if(riportTipus == "Termék riport"){
            return termekRepository.termekekFogyasa(getStartDate(), getEndDate());
        }
        return null;
    }

    private String csvContentAdapter() {
        StringJoiner csvContent = new StringJoiner("\n");
        riportList.forEach(o -> {
            StringJoiner csvRow = new StringJoiner(";");
            RiportEredm objects = (RiportEredm) o;
            Stream.of(objects).forEach(o1 ->{ csvRow.add(o1.getNev()); csvRow.add(o1.getMennyiseg().toString()); });
            csvContent.add(csvRow.toString());
        });
        return csvContent.toString();
    }

    private String getDate4CsvName(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(new Date());
        return strDate;
    }

    private Date getStartDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();

    }

    private Date getEndDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    private void setCommonGridPorps(){
        riportGrid.setSelectionMode(Grid.SelectionMode.NONE);
        riportGrid.setSizeFull();
        root.setComponentAlignment(riportGrid, Alignment.MIDDLE_CENTER);
    }
}
