package com.smalltiantian.genetics;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.Gson;

public class Genetics {
    /**
     * 运行时需要输入参数。
     * 可以输入图片或配置信息地址
     *
     * 若什么都不输入，程序会自动去 ./image 目录中寻找已有的记录
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            String content = IOUtils.resourceToString(args[0], java.nio.charset.Charset.forName("UTF-8"));
            Map<String, String> config = args[0].endsWith("properties")
                                         ? propertiesConfig(content) : jsonConfig(content);
            setEnv(config);
        }
        addAnimal();
        Earth.instance().breedNaturally();
    }

    private static Map<String, String> propertiesConfig(String content) throws IOException {
        Properties p = new Properties();
        p.load(new ByteArrayInputStream(content.getBytes()));

        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : p.entrySet()) {
            map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }

        return map;
    }

    private static Map<String, String> jsonConfig(String content) {
        JsonObject json = new Gson().fromJson(content, JsonObject.class);
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        return map;
    }

    private static void setEnv(Map<String, String> config) {
        for (Map.Entry<String, String> entry : config.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void addAnimal() throws IOException {
        Earth earth    = Earth.instance();

        File data = new File(earth.address(), "data.json");

        if (data.exists()) {
            String content = IOUtils.toString(data.toURI(), java.nio.charset.Charset.forName("UTF-8"));
            JsonObject object = new Gson().fromJson(content, JsonObject.class);
            int year = object.get("year").getAsInt();
            earth.year(year);

            JsonArray pics = object.getAsJsonArray("pics");
            for (JsonElement pic : pics) {
                earth.addAnimal(new Picture(pic.getAsJsonObject(), earth));
            }
        } else {
            int initialNum = earth.initialNum();
            int DNANum     = earth.DNANum();
            for (int i = 0; i < initialNum; i++) {
                earth.addAnimal(new Picture(DNANum, earth));
            }
        }
    }
}
