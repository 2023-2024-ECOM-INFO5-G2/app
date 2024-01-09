package polytech.ecom.g02.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MesureAlbumineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MesureAlbumine getMesureAlbumineSample1() {
        return new MesureAlbumine().id(1L);
    }

    public static MesureAlbumine getMesureAlbumineSample2() {
        return new MesureAlbumine().id(2L);
    }

    public static MesureAlbumine getMesureAlbumineRandomSampleGenerator() {
        return new MesureAlbumine().id(longCount.incrementAndGet());
    }
}
