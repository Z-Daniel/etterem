package hu.etterem.repository.termek;

import hu.etterem.api.termek.entity.Termek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Repository
public interface TermekRepository extends JpaRepository<Termek,Integer>{
    @Query(value = "SELECT p.termek_nev, SUM(i.darab_szam) FROM vasarlas v INNER JOIN tetel I ON v.id = i.vasarlas_id_id INNER JOIN termek p ON p.id = i.termek_id_id WHERE v.vasarlas_datum BETWEEN :curDate AND :endDate GROUP BY p.termek_nev",nativeQuery = true)
    List<Object[]> termekekFogyasa(@Param("curDate") Date curDate, @Param("endDate") Date endDate );
}
