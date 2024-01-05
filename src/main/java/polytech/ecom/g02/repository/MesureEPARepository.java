package polytech.ecom.g02.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.ecom.g02.domain.MesureEPA;

/**
 * Spring Data JPA repository for the MesureEPA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MesureEPARepository extends JpaRepository<MesureEPA, Long> {}
