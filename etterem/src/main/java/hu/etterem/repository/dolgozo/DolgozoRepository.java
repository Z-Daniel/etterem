package hu.etterem.repository.dolgozo;


import hu.etterem.api.dolgozo.entity.Dolgozo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Zsidó Dániel on 4/18/2017.
 * A Dolgozo entitás osztályhoz definiált JPA repo, a dolgozo táblából betölti az objektumokat a VasarlasView-on található dolgozoId legördülőbe.
 */
@Repository
public interface DolgozoRepository extends JpaRepository<Dolgozo, Integer> {

    /**
     * A dolgoi fogyasztás riporthoz szükséges sql lekérdezést végrehajtó metódus.
     * @Param curDate az aktuális hónap első napja
     * @Param endDate az aktuális hónap utolsó napja
     * @return a query eredménye
     */
    @Query(value = "SELECT d.dolgozo_nev, SUM(v.vegosszeg) FROM dolgozo d INNER JOIN vasarlas v ON d.id = v.dolgozo_id WHERE v.vasarlas_datum BETWEEN :curDate AND :endDate GROUP BY d.dolgozo_nev;", nativeQuery = true)
    List<Object[]> dolgozokFogyasztas(@Param("curDate") Date curDate, @Param("endDate") Date endDate );
}
