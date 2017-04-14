package hu.etterem.repository.termek;

import hu.etterem.api.termek.entity.Termek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Repository
public interface TermekRepository extends JpaRepository<Termek,Integer>{
}
