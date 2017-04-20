package hu.etterem.riportEredm;

/**
 * Created by Murdoc on 4/20/2017.
 */
public class TermekRiport {
    private String termekNev;
    private Integer fogyas;

    public TermekRiport(String termekNev, Integer fogyas){
        this.termekNev = termekNev;
        this.fogyas = fogyas;
    }

    public String getTermekNev() {
        return termekNev;
    }

    public Integer getFogyas() {
        return fogyas;
    }
}
