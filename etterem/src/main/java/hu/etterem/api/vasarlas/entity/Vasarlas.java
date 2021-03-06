package hu.etterem.api.vasarlas.entity;

import hu.etterem.api.dolgozo.entity.Dolgozo;
import hu.etterem.api.tetel.entity.Tetel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Zsidó Dániel on 4/14/2017.
 */
@Entity
@Table
public class Vasarlas implements Serializable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true,nullable = false)
    private Integer id;

    @Column(name = "VEGOSSZEG")
    private Integer vegosszeg;

    @Column(name = "VASARLAS_DATUM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vasarlasDatuma;

    @ManyToOne
    @JoinColumn(name = "dolgozo_id")
    private Dolgozo dolgozoId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "vasarlasId")
    //orphanRemoval --> így nem létezhet tétel a vásárlás nélkül (törli őket, ha a vásárlást törlik)
    private Set<Tetel> tetelekSet = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vasarlas vasarlas = (Vasarlas) o;

        return id != null ? id.equals(vasarlas.id) : vasarlas.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVegosszeg() {
        return vegosszeg;
    }

    public void setVegosszeg(Integer vegosszeg) {
        this.vegosszeg = vegosszeg;
    }

    public Date getVasarlasDatuma() {
        return vasarlasDatuma;
    }

    public void setVasarlasDatuma(Date vasarlasDatuma) {
        this.vasarlasDatuma = vasarlasDatuma;
    }

    public Dolgozo getDolgozoId() {
        return dolgozoId;
    }

    public void setDolgozoId(Dolgozo dolgozoId) {
        this.dolgozoId = dolgozoId;
    }

    public Set<Tetel> getTetelekSet() {
        return tetelekSet;
    }

    public void setTetelekSet(Set<Tetel> tetelekSet) {
        this.tetelekSet = tetelekSet;
    }
}
