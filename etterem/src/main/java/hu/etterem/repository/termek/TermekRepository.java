package hu.etterem.repository.termek;

import hu.etterem.api.termek.entity.Termek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Zsidó Dániel on 4/14/2017.
 * A Termek entitás osztályhoz definiált JPA repo, a termek táblából betölti az objektumokat a VasarlasView-on található termekId legördülőbe.
 */
@Repository
public interface TermekRepository extends JpaRepository<Termek,Integer>{

    /**
     * A termékek fogyásának riportjához szükséges sql lekérdezést végrehajtó metódus.
     * @Param curDate az aktuális hónap első napja
     * @Param endDate az aktuális hónap utolsó napja
     * @return a query eredménye
     */
    @Query(value = "SELECT p.termek_nev, SUM(i.darabszam) FROM vasarlas v INNER JOIN tetel I ON v.id = i.vasarlas_id INNER JOIN termek p ON p.id = i.termek_id WHERE v.vasarlas_datum BETWEEN :curDate AND :endDate GROUP BY p.termek_nev ORDER BY SUM(i.darabszam) DESC;",nativeQuery = true)
    List<Object[]> termekekFogyasa(@Param("curDate") Date curDate, @Param("endDate") Date endDate );
}
