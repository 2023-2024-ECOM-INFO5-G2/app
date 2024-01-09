package polytech.g02.ecom.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.g02.ecom.domain.AideSoignant;

public interface AideSoignantRepositoryWithBagRelationships {
    Optional<AideSoignant> fetchBagRelationships(Optional<AideSoignant> aideSoignant);

    List<AideSoignant> fetchBagRelationships(List<AideSoignant> aideSoignants);

    Page<AideSoignant> fetchBagRelationships(Page<AideSoignant> aideSoignants);
}
