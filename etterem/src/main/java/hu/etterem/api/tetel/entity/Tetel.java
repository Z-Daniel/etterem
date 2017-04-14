package hu.etterem.api.tetel.entity;

import hu.etterem.api.termek.entity.Termek;
import hu.etterem.api.vasarlas.entity.Vasarlas;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Entity
@Table
public class Tetel implements Serializable{

    @Id
    private Integer id;

    @Column
    private Integer darabSzam;

    @ManyToOne
    private Termek termekId;

    @ManyToOne
    private Vasarlas vasarlasId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tetel tetel = (Tetel) o;

        return id == tetel.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Integer getDarabSzam() {

        return darabSzam;
    }

    public void setDarabSzam(Integer darabSzam) {
        this.darabSzam = darabSzam;
    }

    public Termek getTermekId() {
        return termekId;
    }

    public void setTermekId(Termek termekId) {
        this.termekId = termekId;
    }

    public Vasarlas getVasarlasId() {
        return vasarlasId;
    }

    public void setVasarlasId(Vasarlas vasarlasId) {
        this.vasarlasId = vasarlasId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
