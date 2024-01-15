package polytech.ecom.g02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytech.ecom.g02.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
