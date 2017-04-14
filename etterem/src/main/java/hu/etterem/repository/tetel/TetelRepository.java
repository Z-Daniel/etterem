package hu.etterem.repository.tetel;

import hu.etterem.api.tetel.entity.Tetel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Repository
public interface TetelRepository extends JpaRepository<Tetel,Integer> {
}
