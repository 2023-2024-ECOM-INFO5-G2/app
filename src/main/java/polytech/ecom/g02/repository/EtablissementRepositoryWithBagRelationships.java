package polytech.ecom.g02.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.ecom.g02.domain.Etablissement;

public interface EtablissementRepositoryWithBagRelationships {
    Optional<Etablissement> fetchBagRelationships(Optional<Etablissement> etablissement);

    List<Etablissement> fetchBagRelationships(List<Etablissement> etablissements);

    Page<Etablissement> fetchBagRelationships(Page<Etablissement> etablissements);
}
