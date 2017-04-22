package hu.etterem.riportEredm;

/**
 * Created by Murdoc on 4/20/2017.
 */
public class RiportEredm {
    private String nev;
    private Integer mennyiseg;

    public RiportEredm(String nev, Integer mennyiseg){
        this.nev = nev;
        this.mennyiseg = mennyiseg;
    }

    public String getNev() {
        return nev;
    }

    public Integer getMennyiseg() {
        return mennyiseg;
    }
}
