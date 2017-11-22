package com.smalltiantian.genetics;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;
import java.io.File;

public class BaseDataHelper {
    public static void init() throws IOException {
        String imagePath = System.getProperty("user.dir") + "/src/test/resource/" + "firefox.jpg";
        Genetics.init(new File(imagePath));
    }

    public static Picture newPicture(int triangleNum) {
        return new Picture(triangleNum);
    }

    public static Picture takeFromSons() throws Exception {
        return sons().take();
    }

    public static int checkSimilarityByBaseUtil(Picture pic) {
        return BaseUtil.checkSimilarity(pic);
    }

    public static void bornByBaseUtil(int populationNum, int varianceRatio) {
        BaseUtil.newborn(populationNum, varianceRatio);
    }

    public static int whoIsStrongerByBaseUtil() {
        return BaseUtil.whoIsStronger();
    }

    public static List<Picture> fathers() {
        return BaseData.getInstance().fathers;
    }

    public static LinkedBlockingQueue<Picture> sons() {
        return BaseData.getInstance().sons;
    }

    public static int index() {
        return BaseData.getInstance().index;
    }

    public static String originalImagePath() {
        return Config.getInstance().image();
    }

    public static int fathersSimilarity(int index) {
        return BaseData.getInstance().fathers.get(index).similarity;
    }
}
