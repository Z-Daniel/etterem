package hu.etterem.repository.dolgozo;


import hu.etterem.api.dolgozo.entity.Dolgozo;
import hu.etterem.riportEredm.DolgozoRiport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Murdoc on 4/18/2017.
 */
@Repository
public interface DolgozoRepository extends JpaRepository<Dolgozo, Integer> {

    @Query(value = "SELECT d.dolgozo_nev, SUM(v.vegosszeg) FROM dolgozo d INNER JOIN vasarlas v ON d.id = v.dolgozo_id_id WHERE v.vasarlas_datum BETWEEN :curDate AND :endDate GROUP BY d.dolgozo_nev;", nativeQuery = true)
    List<DolgozoRiport> dolgozokFogyasztas(@Param("curDate") Date curDate, @Param("endDate") Date endDate );
}
