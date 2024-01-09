package polytech.g02.ecom.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InfirmiereTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Infirmiere getInfirmiereSample1() {
        return new Infirmiere().id(1L);
    }

    public static Infirmiere getInfirmiereSample2() {
        return new Infirmiere().id(2L);
    }

    public static Infirmiere getInfirmiereRandomSampleGenerator() {
        return new Infirmiere().id(longCount.incrementAndGet());
    }
}
