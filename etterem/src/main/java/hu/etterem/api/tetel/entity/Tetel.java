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
public class Tetel implements Serializable {

    @Id
    @GeneratedValue(generator = "tetel_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "tetel_seq",sequenceName = "tetel_seq")
    private Integer id;

    @Column
    private Integer darabSzam = 0;

    @ManyToOne
    private Termek termekId;

    @ManyToOne
    private Vasarlas vasarlasId;

    //az adatbázisba be nem kerülő field
    @Transient
    private double internalId = generateInternalId();

    private double generateInternalId() {
        return Math.random() * 10000;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tetel tetel = (Tetel) o;

        if (Double.compare(tetel.internalId, internalId) != 0) return false;
        return id != null ? id.equals(tetel.id) : tetel.id == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        temp = Double.doubleToLongBits(internalId);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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

    public double getInternalId() {
        return internalId;
    }
}
