package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EtablissementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Etablissement getEtablissementSample1() {
        return new Etablissement().id(1L).nom("nom1").adresse("adresse1").ville("ville1").codePostal(1);
    }

    public static Etablissement getEtablissementSample2() {
        return new Etablissement().id(2L).nom("nom2").adresse("adresse2").ville("ville2").codePostal(2);
    }

    public static Etablissement getEtablissementRandomSampleGenerator() {
        return new Etablissement()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .codePostal(intCount.incrementAndGet());
    }
}
