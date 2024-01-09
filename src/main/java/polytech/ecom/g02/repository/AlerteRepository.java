package polytech.ecom.g02.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.ecom.g02.domain.Alerte;

/**
 * Spring Data JPA repository for the Alerte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {}
