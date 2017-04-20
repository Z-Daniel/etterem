package hu.etterem.api.tetel.entity;

import hu.etterem.api.termek.entity.Termek;
import hu.etterem.api.vasarlas.entity.Vasarlas;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Entity
@Table
public class Tetel implements Serializable {

    @javax.persistence.Id
    @GenericGenerator(name = "tetel_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "tetel_seq", value = "tetel_seq"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(generator = "tetel_seq", strategy = GenerationType.SEQUENCE)
    @Column(unique = true,nullable = false)
    private Integer id;

    @Column
    @Min(value = 1,message = "Csak nullánál nagyobb darabszámú tétel vehető fel!")
    private Integer darabSzam = 1;

    @NotNull// kötelező legyen kitölteni
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
