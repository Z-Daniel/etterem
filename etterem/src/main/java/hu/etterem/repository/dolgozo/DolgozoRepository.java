package hu.etterem.repository.dolgozo;

import hu.etterem.api.dolgozo.entity.Dolgozo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Murdoc on 4/18/2017.
 */
@Repository
public interface DolgozoRepository extends JpaRepository<Dolgozo,Integer>{
}
