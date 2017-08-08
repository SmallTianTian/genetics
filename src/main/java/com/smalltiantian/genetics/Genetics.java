package com.smalltiantian.genetics;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonObject;

public class Genetics {
    private static Genetics genetics = null;
    private static String flag       = "";

    public static void main(String[] args) throws Exception {
        Genetics g = null;
        if (args.length == 2) {
            if (args[0].endsWith(".jpg")) {
                BaseData.init(args[0]);
                g = Genetics.init(args[1]);
            } else {
                BaseData.init(args[1]);
                g = Genetics.init(args[0]);
            }
        } else {
            File base = new File(System.getProperty("user.dir"));
            String[] file = base.list();
            for (String oneFile : file) {
                if (oneFile.endsWith(".jpg"))
                    BaseData.init(oneFile);
            }

            try {
                BaseData.getInstance();
            } catch (IllegalStateException e) {
                throw new NullPointerException("Error : No picture in this folder.");
            }

            g = Genetics.init();
        }
        g.begin();
    }

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
            int initialNum     = obj.get("initialNum").getAsInt();
            double growthRate  = obj.get("growthRate").getAsDouble();
            int DNANum         = obj.get("DNANum").getAsInt();
            Config config = Config.init(saveAddress, varianceRatio, saveTimer, countThreadNum, initialNum, growthRate, DNANum);
            genetics = new Genetics();
        } catch (Exception e) {
            throw new RuntimeException("init Failed", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return genetics;
    }

    public void begin() {
        int populationNum = beginData();
        
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < Config.getInstance().countThreadNum(); i++) {
            Thread calculate = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            BaseUtil.checkSimilarity(BaseData.getInstance().sons.take());
                            synchronized (flag) {
                                flag.notifyAll();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            list.add(calculate);
        }

        Thread born = new Thread(new Runnable() {
            int popNum = populationNum;

            @Override
            public void run() {
                synchronized (flag) {
                    while (true) {
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

    private int beginData() {
        int populationNum = Config.getInstance().populationNum();
        for (int i = 0; i < populationNum; i ++) {
            try {
                BaseData.getInstance().sons.put(new Picture(Config.getInstance().DNANum()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return populationNum;
    }
}
