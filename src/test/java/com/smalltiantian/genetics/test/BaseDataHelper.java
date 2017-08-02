package com.smalltiantian.genetics;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;

public class BaseDataHelper {
    public static void init() throws IOException {
        String imagePath = System.getProperty("user.dir") + "/src/test/resource/" + "firefox.jpg";
        BaseData.init(imagePath);
    }

    public static void addANewSon(Picture pic) {
        BaseData.getInstance().addANewSon(pic);
    }

    public static List<Picture> fathers() {
        return BaseData.getInstance().fathers;
    }

    public static LinkedList<Picture> sons() {
        return BaseData.getInstance().sons;
    }

    public static int index() {
        return BaseData.getInstance().index;
    }

    public static String originalImagePath() {
        return BaseData.getInstance().originalImagePath;
    }

    public static int fathersSimilarity(int index) {
        return BaseData.getInstance().fathers.get(index).similarity;
    }
}
