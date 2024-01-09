package polytech.ecom.g02.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MesurePoidsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MesurePoids getMesurePoidsSample1() {
        return new MesurePoids().id(1L);
    }

    public static MesurePoids getMesurePoidsSample2() {
        return new MesurePoids().id(2L);
    }

    public static MesurePoids getMesurePoidsRandomSampleGenerator() {
        return new MesurePoids().id(longCount.incrementAndGet());
    }
}
