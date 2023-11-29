package polytech.g02.ecom.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.g02.ecom.domain.Alerte;

public interface AlerteRepositoryWithBagRelationships {
    Optional<Alerte> fetchBagRelationships(Optional<Alerte> alerte);

    List<Alerte> fetchBagRelationships(List<Alerte> alertes);

    Page<Alerte> fetchBagRelationships(Page<Alerte> alertes);
}
