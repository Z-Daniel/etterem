package hu.etterem.riportEredm;

/**
 * Created by Murdoc on 4/19/2017.
 */
public class DolgozoRiport {
    private String dolgozoNev;
    private Integer fogyasztas;

    public DolgozoRiport(String dolgozoNev, Integer fogyasztas) {
        this.dolgozoNev = dolgozoNev;
        this.fogyasztas = fogyasztas;
    }

    public String getDolgozoNev() {//getter alapján szedi ki az értékeket a grid
        return dolgozoNev;
    }

    public Integer getFogyasztas() {
        return fogyasztas;
    }
}
