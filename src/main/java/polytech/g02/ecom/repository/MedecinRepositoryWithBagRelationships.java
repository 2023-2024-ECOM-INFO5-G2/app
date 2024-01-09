package polytech.g02.ecom.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.g02.ecom.domain.Medecin;

public interface MedecinRepositoryWithBagRelationships {
    Optional<Medecin> fetchBagRelationships(Optional<Medecin> medecin);

    List<Medecin> fetchBagRelationships(List<Medecin> medecins);

    Page<Medecin> fetchBagRelationships(Page<Medecin> medecins);
}
