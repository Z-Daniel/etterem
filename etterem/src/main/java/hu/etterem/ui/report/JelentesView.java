package hu.etterem.ui.report;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import hu.etterem.repository.dolgozo.DolgozoRepository;
import hu.etterem.repository.termek.TermekRepository;
import hu.etterem.riportEredm.DolgozoRiport;
import hu.etterem.ui.main.MainUI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Murdoc on 4/19/2017.
 */
@SpringView(name = JelentesView.VIEW_NAME, ui = MainUI.class)
public class JelentesView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "REPORT_VIEW";
    public static final String CAPTION = "Jelentés";

    private Grid riportGrid;

    @Autowired
    private DolgozoRepository dolgozoRepository;

    @Autowired
    private TermekRepository termekRepository;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        Button dolgozoFogyaszt;
        Button termekFogyaszt;

        VerticalLayout root = new VerticalLayout(
                new HorizontalLayout(
                        dolgozoFogyaszt = new Button("Dolgozók fogyasztása"),
                        termekFogyaszt = new Button("Termékek fogyása")
                ),
                riportGrid = new Grid()
        );

        dolgozoFogyaszt.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (riportGrid != null) {
                    root.removeComponent(riportGrid);
                    riportGrid = new Grid();
                } else {
                    riportGrid = new Grid();
                }
                BeanItemContainer<DolgozoRiport> container = new BeanItemContainer<>(DolgozoRiport.class);
                riportGrid.setContainerDataSource(container);
                root.addComponent(riportGrid);
                riportGrid.getColumn("dolgozoNev").setHeaderCaption("Dolgozó név");
                riportGrid.getColumn("fogyasztas").setHeaderCaption("Havi fogyasztás");

                List<Object[]> objectList = dolgozoRepository.dolgozokFogyasztas(getStartDate(), getEndDate());//Objektum tömbök vannak a listában, egy objektum tömb egy sor a tömb elemei az oszlopok
                objectList.forEach(objects -> container.addItem(new DolgozoRiport(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
            }
        });

        termekFogyaszt.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (riportGrid != null) {
                    root.removeComponent(riportGrid);
                    riportGrid = new Grid();
                } else {
                    riportGrid = new Grid();
                }
                BeanItemContainer<DolgozoRiport> container = new BeanItemContainer<>(DolgozoRiport.class);
                riportGrid.setContainerDataSource(container);
                root.addComponent(riportGrid);
                riportGrid.getColumn("termekNev").setHeaderCaption("Termékek");//az oszlopok azonosítói a gridben lévő datasource objektumának mezői
                riportGrid.getColumn("fogyas").setHeaderCaption("Havi fogyás");

                List<Object[]> objectList = termekRepository.termekekFogyasa(getStartDate(), getEndDate());//Objektum tömbök vannak a listában, egy objektum tömb egy sor a tömb elemei az oszlopok
                objectList.forEach(objects -> container.addItem(new DolgozoRiport(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
            }
        });
        addComponent(root);
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
}
