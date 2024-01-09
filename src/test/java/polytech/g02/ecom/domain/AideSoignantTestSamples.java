package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AideSoignantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AideSoignant getAideSoignantSample1() {
        return new AideSoignant().id(1L);
    }

    public static AideSoignant getAideSoignantSample2() {
        return new AideSoignant().id(2L);
    }

    public static AideSoignant getAideSoignantRandomSampleGenerator() {
        return new AideSoignant().id(longCount.incrementAndGet());
    }
}
