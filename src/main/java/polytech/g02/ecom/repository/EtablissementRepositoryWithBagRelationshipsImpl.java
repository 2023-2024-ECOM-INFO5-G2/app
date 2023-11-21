package polytech.g02.ecom.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import polytech.g02.ecom.domain.Etablissement;
import polytech.g02.ecom.domain.Patient;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EtablissementRepositoryWithBagRelationshipsImpl implements EtablissementRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Etablissement> fetchBagRelationships(Optional<Etablissement> etablissement) {
        return etablissement.map(this::fetchUsers);
    }

    @Override
    public Page<Etablissement> fetchBagRelationships(Page<Etablissement> etablissements) {
        return new PageImpl<>(
            fetchBagRelationships(etablissements.getContent()),
            etablissements.getPageable(),
            etablissements.getTotalElements()
        );
    }

    @Override
    public List<Etablissement> fetchBagRelationships(List<Etablissement> etablissements) {
        return Optional.of(etablissements).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Etablissement fetchUsers(Etablissement result) {
        return entityManager
            .createQuery(
                "select etablissement from Etablissement etablissement left join fetch etablissement.users where etablissement.id = :id",
                Etablissement.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Etablissement> fetchUsers(List<Etablissement> etablissements) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, etablissements.size()).forEach(index -> order.put(etablissements.get(index).getId(), index));
        List<Etablissement> result = entityManager
            .createQuery(
                "select etablissement from Etablissement etablissement left join fetch etablissement.users where etablissement in :etablissements",
                Etablissement.class
            )
            .setParameter("etablissements", etablissements)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
    //récuperer la liste des patients d'un établissement -- passer par la methode getPatient dans la classe etablissement
    //    public List<Patient> getPatientEtablissement(long id){
    //        return entityManager
    //            .createQuery(
    //                "select patient from Patient where etablissement_id = :id",
    //                Patient.class
    //            )
    //            .setParameter("id",id)
    //            .getResultList();
    //    }

}
