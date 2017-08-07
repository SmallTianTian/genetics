package com.smalltiantian.genetics;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonObject;

public class Genetics {
    private static Genetics genetics = null;

    public static Genetics init() {
        if (genetics != null)
            throw new IllegalStateException("Error : You already init this(`Genetics`).");
        Config config = Config.initByDefault();
        genetics = new Genetics();
        return genetics;
    }

    public static Genetics init(String configPath) {
        if (genetics != null)
            throw new IllegalStateException("Error : You already init this(`Genetics`).");
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File(configPath));
            String configStr = IOUtils.toString(is);
            JsonObject obj   = BaseUtil.gson().fromJson(configStr, JsonObject.class);
            String saveAddress = obj.get("saveAddress").getAsString();
            int varianceRatio  = obj.get("varianceRatio").getAsInt();
            int saveTimer      = obj.get("saveTimer").getAsInt();
            int countThreadNum = obj.get("countThreadNum").getAsInt();
            Config config = Config.init(saveAddress, varianceRatio, saveTimer, countThreadNum);
            genetics = new Genetics();
        } catch (Exception e) {
            throw new RuntimeException("init Failed", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return genetics;
    }

    public void beginGenetics() {
        
    }
}
