package polytech.ecom.g02.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RepasTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Repas getRepasSample1() {
        return new Repas().id(1L).nom("nom1").description("description1");
    }

    public static Repas getRepasSample2() {
        return new Repas().id(2L).nom("nom2").description("description2");
    }

    public static Repas getRepasRandomSampleGenerator() {
        return new Repas().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
