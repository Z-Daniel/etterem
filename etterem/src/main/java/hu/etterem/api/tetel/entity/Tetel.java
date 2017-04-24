package hu.etterem.api.tetel.entity;

import hu.etterem.api.termek.entity.Termek;
import hu.etterem.api.vasarlas.entity.Vasarlas;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Zsidó Dániel on 4/14/2017.
 */
@Entity
@Table
public class Tetel implements Serializable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true,nullable = false)
    private Integer id;

    @Column(name = "darabszam")
    @Min(value = 1,message = "Csak nullánál nagyobb darabszámú tétel vehető fel!")
    private Integer darabSzam = 1;

    @NotNull// kötelező legyen kitölteni
    @ManyToOne
    @JoinColumn(name = "termek_id")
    private Termek termekId;

    @ManyToOne
    @JoinColumn(name = "vasarlas_id")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getInternalId() {
        return internalId;
    }
}
