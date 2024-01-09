package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PatientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Patient getPatientSample1() {
        return new Patient().id(1L).prenom("prenom1").nom("nom1").sexe("sexe1").numChambre(1);
    }

    public static Patient getPatientSample2() {
        return new Patient().id(2L).prenom("prenom2").nom("nom2").sexe("sexe2").numChambre(2);
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(longCount.incrementAndGet())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .sexe(UUID.randomUUID().toString())
            .numChambre(intCount.incrementAndGet());
    }
}
