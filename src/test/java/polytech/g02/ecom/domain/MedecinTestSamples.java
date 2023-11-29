package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MedecinTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medecin getMedecinSample1() {
        return new Medecin().id(1L);
    }

    public static Medecin getMedecinSample2() {
        return new Medecin().id(2L);
    }

    public static Medecin getMedecinRandomSampleGenerator() {
        return new Medecin().id(longCount.incrementAndGet());
    }
}
