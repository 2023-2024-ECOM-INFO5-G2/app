package polytech.ecom.g02.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RappelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Rappel getRappelSample1() {
        return new Rappel().id(1L).intervaleJours(1).tache("tache1");
    }

    public static Rappel getRappelSample2() {
        return new Rappel().id(2L).intervaleJours(2).tache("tache2");
    }

    public static Rappel getRappelRandomSampleGenerator() {
        return new Rappel().id(longCount.incrementAndGet()).intervaleJours(intCount.incrementAndGet()).tache(UUID.randomUUID().toString());
    }
}
