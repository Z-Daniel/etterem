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

//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-");
                Calendar c = Calendar.getInstance();
                Date curDate = c.getTime();
                curDate.setDate(1);//FIXME írd át calendar-ra
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date endDate = c.getTime();

                List<Object[]> asd = dolgozoRepository.dolgozokFogyasztas(curDate, endDate);//Objektum tömbök vannak a listában, egy objektum tömb egy sor a tömb elemei az oszlopok
                asd.forEach(objects -> container.addItem(new DolgozoRiport(objects[0].toString(), Integer.valueOf(objects[1].toString()))));
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
                root.addComponent(riportGrid);
                riportGrid.getColumn("termekId").setHeaderCaption("Termékek");//az oszlopok azonosítói a gridben lévő datasource objektumának mezői!
                riportGrid.getColumn("vegosszeg").setHeaderCaption("Havi fogyás");

            }
        });
        addComponent(root);
    }
}
