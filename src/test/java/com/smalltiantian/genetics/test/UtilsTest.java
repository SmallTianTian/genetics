package com.smalltiantian.genetics;

import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class UtilsTest {
    public void similarityOfTwoPicRGB() {
        Picture one = new Picture(100);
        Picture two = new Picture(100);
        int diff = Utils.checkSimilarity(one, two);

        Assert.assertTrue(diff > 0);
    }
}
