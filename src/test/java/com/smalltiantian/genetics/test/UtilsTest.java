package com.smalltiantian.genetics;

import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class UtilsTest {
    public void similarityOfTwoPicRGB() {
        Earth earth = TestHelper.produceEarth(null);

        Picture one = new Picture(100, earth);
        Picture two = new Picture(100, earth);
        int diff = Utils.checkSimilarity(one, two);

        Assert.assertTrue(diff > 0);
    }
}
