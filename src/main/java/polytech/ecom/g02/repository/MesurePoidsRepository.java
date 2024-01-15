package polytech.ecom.g02.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.ecom.g02.domain.MesurePoids;

/**
 * Spring Data JPA repository for the MesurePoids entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MesurePoidsRepository extends JpaRepository<MesurePoids, Long> {}
