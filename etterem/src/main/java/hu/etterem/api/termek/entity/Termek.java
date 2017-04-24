package hu.etterem.api.termek.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by Zsidó Dániel on 4/14/2017.
 */
@Entity
@Table
public class Termek implements Serializable{
    @Id
    private Integer id;

    @Column(name = "TERMEK_NEV", unique = true)
    private String nev;

    @Min(value = 0)
    @Column(name = "TERMEK_AR")
    private Integer ar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Termek termek = (Termek) o;

        return id == termek.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public Integer getAr() {
        return ar;
    }

    public void setAr(Integer ar) {
        this.ar = ar;
    }

    @Override
    public String toString() {
        return nev;
    }
}
