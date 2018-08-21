package com.smalltiantian.genetics;

import java.util.Map;
import java.util.HashMap;

public class TestHelper {
    public static Earth produceEarth(Map<String, String> map) {
        if (map == null) map = new HashMap<String, String>();

        String savePath    = getOrDefault(map, "savePath", "data");
        String godPath     = getOrDefault(map, "godPath",
                                          System.getProperty("user.dir") + "/src/test/resource/firefox.jpg");
        int varianceRatio  = Integer.valueOf(getOrDefault(map, "varianceRatio", "-1"));
        int saveTimer      = Integer.valueOf(getOrDefault(map, "saveTimer", "20"));
        int countThreadNum = Integer.valueOf(getOrDefault(map, "countThreadNum", "-1"));
        int initialNum     = Integer.valueOf(getOrDefault(map, "initialNum", "100"));
        int DNANum         = Integer.valueOf(getOrDefault(map, "DNANum", "100"));
        int maxYear        = Integer.valueOf(getOrDefault(map, "maxYear", "-1"));
        double growthRate  = Double.valueOf(getOrDefault(map, "growthRate", "-1"));

        return new Earth(savePath, godPath, varianceRatio, saveTimer, countThreadNum,
                         initialNum, growthRate, DNANum, maxYear);
    }

    private static String getOrDefault(Map<String, String> map, String k, String defaultV) {
        boolean flag = map.containsKey(k);
        return flag ? map.get(k) : defaultV;
    }
}
