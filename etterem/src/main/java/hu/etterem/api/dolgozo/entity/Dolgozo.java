package hu.etterem.api.dolgozo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Entity
@Table
public class Dolgozo implements Serializable {

    @Id
    @GenericGenerator(name = "dolgozo_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "dolgozo_seq", value = "dolgozo_seq"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(generator = "dolgozo_seq", strategy = GenerationType.SEQUENCE)
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
