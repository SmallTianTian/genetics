package com.smalltiantian.genetics.test;

import com.smalltiantian.genetics.*;

import org.testng.annotations.*;
import org.testng.Assert;

public class BaseTest {
    @BeforeSuite
    public void initConfigs() throws Exception {
        BaseDataHelper.init();

        Assert.assertTrue(BaseDataHelper.fathers().size() > 0);
        Assert.assertEquals(BaseDataHelper.sons().size(), 0);
        Assert.assertEquals(BaseDataHelper.index(), 0);
        Assert.assertNotNull(BaseDataHelper.originalImagePath());
    }
}
