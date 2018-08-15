package com.smalltiantian.genetics;

import com.smalltiantian.genetics.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.CountDownLatch;

@Test
public class EarthFeature {
    public void accessMultithreadingAddToFather() throws Exception {
        int num = 10;
        final Earth earth = TestHelper.produceEarth(null);
        final CountDownLatch downLatch = new CountDownLatch(num);

        Assert.assertTrue(earth.fathers().size() == 0);

        Runnable addFather = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    earth.addAnimal(new Picture(100));
                }
                downLatch.countDown();
            }
        }

        for (int i = 0; i < num; i ++) {
            new Thread(addFather).start();
        }

        countDown.await();
        Assert.assertTrue(earth.fathers().size() == 10 * num);
    }

    public void accessBornNext() throws Exception {
        int num = 100;
        Earth earth = TestHelper.produceEarth(null);

        for (int i = 0; i < num; i++) {
            earth.addAnimal(new Picture(100));
        }
        Assert.assertEquals(earth.year(), 0);

        earth.procreate();
        Assert.assertEquals(earth.year(), 1);
    }

    public void accessSonIsStrongerThanFather() throws Exception {
        int num = 100;
        Earth earth = TestHelper.produceEarth(null);

        for (int i = 0; i < num; i++) {
            earth.addAnimal(new Picture(100));
        }
        Assert.assertEquals(earth.year(), 0);
        Picture fatherStronger = earth.strongest();

        earth.procreate();
        Assert.assertEquals(earth.year(), 1);
        Picture sonStronger = earth.strongest();

        Assert.assertTrue(fatherStronger.similarity() <= sonStronger.similarity());
    }
}
