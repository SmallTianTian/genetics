package com.smalltiantian.genetics;

import org.testng.Assert;
import org.testng.annotations.*;

@Test(enabled = false)
public class GeneticsFeature {
    public void accessDesignatedYear() throws Exception {
        int year = 10;
        System.setProperty("maxYear", String.valueOf(year));
        System.setProperty("godPath", System.getProperty("user.dir") + "/src/test/resource/firefox.jpg");

        Genetics.main(new String[]{});

        Assert.assertEquals(Earth.instance().year(), year);
    }

}
