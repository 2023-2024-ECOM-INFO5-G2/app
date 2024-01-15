package polytech.ecom.g02.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.ecom.g02.domain.Rappel;

public interface RappelRepositoryWithBagRelationships {
    Optional<Rappel> fetchBagRelationships(Optional<Rappel> rappel);

    List<Rappel> fetchBagRelationships(List<Rappel> rappels);

    Page<Rappel> fetchBagRelationships(Page<Rappel> rappels);
}
