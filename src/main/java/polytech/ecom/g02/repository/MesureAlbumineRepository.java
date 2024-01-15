package polytech.ecom.g02.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.ecom.g02.domain.MesureAlbumine;

/**
 * Spring Data JPA repository for the MesureAlbumine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MesureAlbumineRepository extends JpaRepository<MesureAlbumine, Long> {}
