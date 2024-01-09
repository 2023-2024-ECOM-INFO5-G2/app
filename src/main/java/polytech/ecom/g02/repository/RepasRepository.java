package polytech.ecom.g02.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.ecom.g02.domain.Repas;

/**
 * Spring Data JPA repository for the Repas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepasRepository extends JpaRepository<Repas, Long> {}
