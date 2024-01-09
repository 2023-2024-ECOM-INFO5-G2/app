package polytech.g02.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytech.g02.ecom.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
