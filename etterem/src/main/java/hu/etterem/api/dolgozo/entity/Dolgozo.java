package hu.etterem.api.dolgozo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Entity
@Table
public class Dolgozo implements Serializable {

    @Id
    private Integer id;

    @Column(name = "DOLGOZO_NEV")
    private String nev;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dolgozo dolgozo = (Dolgozo) o;

        return id != null ? id.equals(dolgozo.id) : dolgozo.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
