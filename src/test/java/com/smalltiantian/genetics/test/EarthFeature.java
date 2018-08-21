package com.smalltiantian.genetics;

import com.smalltiantian.genetics.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.CountDownLatch;
import java.util.Map;
import java.util.HashMap;

@Test
public class EarthFeature {
    public void accessMultithreadingAddToFather() throws Exception {
        int num = 10;
        final Earth earth = TestHelper.produceEarth(null);
        final CountDownLatch downLatch = new CountDownLatch(num);

        Assert.assertTrue(earth.newborns().size() == 0);

        Runnable addFather = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    earth.addAnimal(new Picture(100, earth));
                }
                downLatch.countDown();
            }
        };

        for (int i = 0; i < num; i ++) {
            new Thread(addFather).start();
        }

        downLatch.await();
        Assert.assertTrue(earth.newborns().size() == 10 * num);
    }

    public void accessBornNext() throws Exception {
        int num = 100;
        Earth earth = TestHelper.produceEarth(null);

        for (int i = 0; i < num; i++) {
            earth.addAnimal(new Picture(100, earth));
        }
        Assert.assertEquals(earth.year(), 0);

        earth.growup();
        earth.evolution();
        Assert.assertEquals(earth.year(), 1);
    }

    public void accessDesignatedYear() throws Exception {
        int year = 10;
        Map<String, String> config = new HashMap<String, String>();
        config.put("maxYear", String.valueOf(year));
        Earth earth = TestHelper.produceEarth(config);

        for (int i = 0; i < 100; i++) {
            earth.addAnimal(new Picture(100, earth));
        }
        Assert.assertEquals(earth.year(), 0);

        earth.breedNaturally();

        Assert.assertEquals(earth.year(), year);
    }

    public void accessSonIsStrongerThanFather() throws Exception {
        int num = 100;
        Earth earth = TestHelper.produceEarth(null);

        for (int i = 0; i < num; i++) {
            earth.addAnimal(new Picture(100, earth));
        }
        Assert.assertEquals(earth.year(), 0);
        earth.growup();
        Picture fatherStronger = earth.strongest();

        earth.evolution();
        Assert.assertEquals(earth.year(), 1);
        earth.growup();
        Picture sonStronger = earth.strongest();

        Assert.assertTrue(fatherStronger.similarity() <= sonStronger.similarity());
    }
}
