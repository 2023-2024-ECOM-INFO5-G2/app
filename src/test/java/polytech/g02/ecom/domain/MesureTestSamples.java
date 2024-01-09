package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MesureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Mesure getMesureSample1() {
        return new Mesure().id(1L).nomValeur("nomValeur1");
    }

    public static Mesure getMesureSample2() {
        return new Mesure().id(2L).nomValeur("nomValeur2");
    }

    public static Mesure getMesureRandomSampleGenerator() {
        return new Mesure().id(longCount.incrementAndGet()).nomValeur(UUID.randomUUID().toString());
    }
}
