package polytech.ecom.g02.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MesureEPATestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MesureEPA getMesureEPASample1() {
        return new MesureEPA().id(1L);
    }

    public static MesureEPA getMesureEPASample2() {
        return new MesureEPA().id(2L);
    }

    public static MesureEPA getMesureEPARandomSampleGenerator() {
        return new MesureEPA().id(longCount.incrementAndGet());
    }
}
