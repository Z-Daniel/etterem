package hu.etterem.repository.vasarlas;

import hu.etterem.api.vasarlas.entity.Vasarlas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Repository
public interface VasarlasRepository extends JpaRepository<Vasarlas,Integer> {
}
