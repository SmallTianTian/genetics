package com.smalltiantian.genetics;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class Genetics {
    private static Genetics genetics = null;
    private static String flag       = "";
    private boolean canBreed = true;

    /**
     * 运行时需要输入参数。
     * 可以输入图片或配置信息地址
     *
     * 若什么都不输入，程序会自动去 ./image 目录中寻找已有的记录
     */
    public static void main(String[] args) throws Exception {
        Genetics g = null;
        if (args.length == 0) {
            String file = "image/genetics.json";
            g = Genetics.init(file);
        }
        if (BaseUtil.isImage(args[0])) {
            g = Genetics.init(new File(args[0]));
        } else {
            g = Genetics.init(args[0]);
        }
        g.begin();
    }

    public static Genetics init(File imagePath) throws IOException {
        if (genetics != null)
            throw new IllegalStateException("Error : You already init this(`Genetics`).");
        BaseUtil.ensureVarIsNotNull(imagePath, "imagePath");

        Config.initByDefault(imagePath.getAbsolutePath());
        genetics = new Genetics();
        return genetics;
    }

    public static Genetics init(String configPath) throws IOException {
        if (genetics != null)
            throw new IllegalStateException("Error : You already init this(`Genetics`).");
        BaseUtil.ensureVarIsNotNull(configPath, "configPath");

        FileInputStream is = null;
        String imageFile   = null;
        String data        = null;
        try {
            is = new FileInputStream(new File(configPath));
            String configStr = IOUtils.toString(is, "utf-8");
            JsonObject obj   = BaseUtil.gson().fromJson(configStr, JsonObject.class);

            Config.initByJson(obj);
            genetics = new Genetics();
        } catch (Exception e) {
            if (imageFile != null) {
                return init(new File(imageFile));
            }
            throw new RuntimeException("init Failed", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return genetics;
    }

    public void begin() {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < Config.getInstance().countThreadNum(); i++) {
            Thread calculate = new Thread(new Runnable() {
                @Override
                public void run() {
                    canBreed = Config.getInstance().canBreed();
                    while (canBreed) {
                        try {
                            BaseUtil.checkSimilarity(BaseData.getInstance().sons.take());
                            synchronized (flag) {
                                flag.notifyAll();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    BaseUtil.storedToDisk();
                }
            });
            list.add(calculate);
        }

        Thread born = new Thread(new Runnable() {
            int popNum = BaseData.getInstance().fathers.size();

            @Override
            public void run() {
                synchronized (flag) {
                    while (canBreed) {
                        if (BaseData.getInstance().fathers.size() == popNum) {
                            if ((++BaseData.getInstance().index % Config.getInstance().saveTimer()) == 0)
                                BaseUtil.storedToDisk();
                            popNum = Config.getInstance().populationNum();
                            BaseUtil.newborn(popNum, Config.getInstance().varianceRatio());
                        } else {
                            try {
                                flag.wait();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        new Thread(born).start();

        for (Thread thread : list)
            new Thread(thread).start();
    }
}
