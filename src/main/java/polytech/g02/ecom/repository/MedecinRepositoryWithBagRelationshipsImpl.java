package polytech.g02.ecom.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import polytech.g02.ecom.domain.Medecin;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MedecinRepositoryWithBagRelationshipsImpl implements MedecinRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Medecin> fetchBagRelationships(Optional<Medecin> medecin) {
        return medecin.map(this::fetchUsers).map(this::fetchAlertes);
    }

    @Override
    public Page<Medecin> fetchBagRelationships(Page<Medecin> medecins) {
        return new PageImpl<>(fetchBagRelationships(medecins.getContent()), medecins.getPageable(), medecins.getTotalElements());
    }

    @Override
    public List<Medecin> fetchBagRelationships(List<Medecin> medecins) {
        return Optional.of(medecins).map(this::fetchUsers).map(this::fetchAlertes).orElse(Collections.emptyList());
    }

    Medecin fetchUsers(Medecin result) {
        return entityManager
            .createQuery("select medecin from Medecin medecin left join fetch medecin.users where medecin.id = :id", Medecin.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Medecin> fetchUsers(List<Medecin> medecins) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, medecins.size()).forEach(index -> order.put(medecins.get(index).getId(), index));
        List<Medecin> result = entityManager
            .createQuery("select medecin from Medecin medecin left join fetch medecin.users where medecin in :medecins", Medecin.class)
            .setParameter("medecins", medecins)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Medecin fetchAlertes(Medecin result) {
        return entityManager
            .createQuery("select medecin from Medecin medecin left join fetch medecin.alertes where medecin.id = :id", Medecin.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Medecin> fetchAlertes(List<Medecin> medecins) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, medecins.size()).forEach(index -> order.put(medecins.get(index).getId(), index));
        List<Medecin> result = entityManager
            .createQuery("select medecin from Medecin medecin left join fetch medecin.alertes where medecin in :medecins", Medecin.class)
            .setParameter("medecins", medecins)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
