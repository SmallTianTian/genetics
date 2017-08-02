package com.smalltiantian.genetics.test;

import com.smalltiantian.genetics.*;
import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class BaseFeature extends BaseTest {
    public void accessCheckDifferent() throws Exception {
        Picture pic = new Picture(100);
        int diff = BaseUtil.checkSimilarity(pic);

        Assert.assertTrue(diff > 0);
    }

    public void accessBornNext() throws Exception {
        int populationNum = 100;
        int varianceRatio = 0;
        for (int i = 0; i < populationNum; i++) {
            Picture pic = new Picture(100);
            BaseDataHelper.addANewSon(pic);
        }

        while (BaseDataHelper.sons().size() > 0)
            BaseUtil.checkSimilarity(BaseDataHelper.sons().remove());
        Assert.assertEquals(BaseDataHelper.fathers().size(), populationNum);

        BaseUtil.newborn(populationNum, varianceRatio);
        Assert.assertEquals(BaseDataHelper.fathers().size(), 0);

    }

    public void accessSonIsStrongerThanFather() throws Exception {
        int populationNum = 100;
        int varianceRatio = 0;
        for (int i = 0; i < populationNum; i++) {
            Picture pic = new Picture(100);
            BaseDataHelper.addANewSon(pic);
        }

        while (BaseDataHelper.sons().size() > 0)
            BaseUtil.checkSimilarity(BaseDataHelper.sons().remove());
        int fatherStronger = BaseDataHelper.fathersSimilarity(BaseUtil.whoIsStronger());

        BaseUtil.newborn(populationNum, varianceRatio);
        while (BaseDataHelper.sons().size() > 0)
            BaseUtil.checkSimilarity(BaseDataHelper.sons().remove());
        int sonStronger    = BaseDataHelper.fathersSimilarity(BaseUtil.whoIsStronger());
        
        Assert.assertTrue(fatherStronger <= sonStronger);
    }
}
