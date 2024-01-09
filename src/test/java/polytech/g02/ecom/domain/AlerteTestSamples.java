package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlerteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Alerte getAlerteSample1() {
        return new Alerte().id(1L).description("description1");
    }

    public static Alerte getAlerteSample2() {
        return new Alerte().id(2L).description("description2");
    }

    public static Alerte getAlerteRandomSampleGenerator() {
        return new Alerte().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
