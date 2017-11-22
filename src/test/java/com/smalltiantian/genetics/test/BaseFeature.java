package com.smalltiantian.genetics.test;

import com.smalltiantian.genetics.*;
import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class BaseFeature extends BaseTest {
    public void accessCheckDifferent() throws Exception {
        int diff = BaseDataHelper.checkSimilarityByBaseUtil(BaseDataHelper.newPicture(100));

        Assert.assertTrue(diff > 0);
    }

    public void accessBornNext() throws Exception {
        int populationNum = 100; // just > 0
        int varianceRatio = 0;

        Assert.assertTrue(BaseDataHelper.fathers().size() > 0);

        BaseDataHelper.bornByBaseUtil(populationNum, varianceRatio);
        Assert.assertEquals(BaseDataHelper.fathers().size(), 0);
    }

    public void accessSonIsStrongerThanFather() throws Exception {
        int populationNum = 100;
        int varianceRatio = 0;

        int fatherStronger = BaseDataHelper.fathersSimilarity(BaseDataHelper.whoIsStrongerByBaseUtil());

        BaseDataHelper.bornByBaseUtil(populationNum, varianceRatio);
        while (BaseDataHelper.sons().size() > 0)
            BaseDataHelper.checkSimilarityByBaseUtil(BaseDataHelper.takeFromSons());
        int sonStronger    = BaseDataHelper.fathersSimilarity(BaseDataHelper.whoIsStrongerByBaseUtil());
        
        Assert.assertTrue(fatherStronger <= sonStronger);
    }
}
