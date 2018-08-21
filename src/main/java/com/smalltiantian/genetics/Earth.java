package com.smalltiantian.genetics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Earth {
    private static class SingleHolder {
        private static final Earth INSTANCE;

        static {
            String savePath    = Utils.systemProperty("savePath", "data");
            String godPath     = Utils.systemProperty("godPath", "god.jpg");
            int varianceRatio  = Integer.valueOf(Utils.systemProperty("varianceRatio", "-1"));
            int saveTimer      = Integer.valueOf(Utils.systemProperty("saveTimer", "20"));
            int countThreadNum = Integer.valueOf(Utils.systemProperty("countThreadNum", "-1"));
            int initialNum     = Integer.valueOf(Utils.systemProperty("initialNum", "100"));
            int DNANum         = Integer.valueOf(Utils.systemProperty("DNANum", "100"));
            int maxYear        = Integer.valueOf(Utils.systemProperty("maxYear", "-1"));
            double growthRate  = Double.valueOf(Utils.systemProperty("growthRate", "-1"));

            INSTANCE = new Earth(savePath, godPath, varianceRatio, saveTimer, countThreadNum,
                                 initialNum, growthRate, DNANum, maxYear);
        }
    }

    private class CalculateThread implements Runnable {
        @Override
        public void run() {
            while (canOperation()) {
                Picture pic = null;
                synchronized (newborns) {
                    for (;;) {
                        if (newborns.size() > 0) {
                            try {
                                pic = newborns.pop();
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (++sleepCalculateThread == calculateThread.length) {
                                synchronized (fathers) {
                                    fathers.notify();
                                }
                                sleepCalculateThread = 0;
                            }
                            try {
                                newborns.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                int similarity = Utils.checkSimilarity(pic, god);
                pic.similarity(similarity);
                addFathers(pic);
            }
        }
    }

    private final String savePath;      // 保存地址
    private final Picture god;          // 原始图片位置
    private final int varianceRatio;    // 变异概率（-1 为自动选择）
    private final int saveTimer;        // 多少代保存一次
    private final int countThreadNum;   // 多少个线程参与计算
    private final int initialNum;       // 初代种数量
    private final double growthRate;    // 增长率（-1 为自动选择）
    private final int DNANum;           // 每一个个体有多少个 DNA
    private final int maxYear;          // 繁殖多少代后停止（-1 为无休止）
    private final Thread[] calculateThread;
    private final LinkedList<Picture> newborns = new LinkedList<Picture>();
    private final List<Picture> fathers        = new ArrayList<Picture>();

    private volatile int sleepCalculateThread = 0;

    private int year = 0;
    private int strongest = -1;
    private long allSimilarity;

    public static Earth instance() {
        return SingleHolder.INSTANCE;
    }

    public Earth addAnimal(Picture pic) {
        synchronized (this.newborns) {
            this.newborns.add(pic);
        }
        return this;
    }

    public Earth growup() {
        int expect = this.newborns.size();
        synchronized (this.newborns) {
            this.newborns.notifyAll();
        }
        synchronized (this.fathers) {
            while (expect != this.fathers.size()) {
                try {
                    this.fathers.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        statisticsAnimals();

        return this;
    }

    public void breedNaturally() {
        while (canOperation()) {
            growup();
            evolution();
        }
    }

    public Picture strongest() {
        if (this.strongest == -1) {
            statisticsAnimals();
        }
        return this.fathers.get(this.strongest);
    }

    public int year() {
        return this.year;
    }

    String address() {
        return this.savePath;
    }

    Picture god() {
        return this.god;
    }

    List<Picture> newborns() {
        return new ArrayList<Picture>(this.newborns);
    }

    List<Picture> fathers() {
        return new ArrayList<Picture>(this.fathers);
    }

    void evolution() {
        Picture strongest = strongest();
        addAnimal(strongest);

        for (int i = 1; i < nextSize(); i++) {
            long random1 = (long) (Utils.nextDouble() * this.allSimilarity);
            long random2 = (long) (Utils.nextDouble() * this.allSimilarity);
            boolean flag1 = true;
            boolean flag2 = true;

            List<Triangle> cells = new ArrayList<>();

            Picture father = null;
            Picture mather = null;
            for (Picture pic : this.fathers) {
                if (flag1 && (random1 -= pic.similarity()) < 0) {
                    father = pic;
                    flag1 = false;
                }
                if (flag2 && (random2 -= pic.similarity()) < 0) {
                    mather = pic;
                    flag2 = false;
                }
                if (!flag1 && !flag2) {
                    addAnimal(new Picture(father, mather, Utils.randomInt(100) < this.varianceRatio));
                    break;
                }
            }
        }
        happyNewYear();
    }

    private void addFathers(Picture pic) {
        synchronized (this.fathers) {
            this.fathers.add(pic);
        }
    }

    private int varianceRatio() {
        return this.varianceRatio == -1 ? 3 : this.varianceRatio;
    }

    private int nextSize() {
        double growth = this.growthRate == -1 ? Utils.randomDoubleInRange(0.03, true) : this.growthRate;
        return this.fathers.size() + (int) (this.fathers.size() * growth);
    }

    private void statisticsAnimals() {
        this.allSimilarity = 0;
        int max = 0;

        for (int i = 0; i < this.fathers.size(); i++) {
            Picture pic = this.fathers.get(i);
            this.allSimilarity += pic.similarity();
            if (max < pic.similarity()) {
                max = pic.similarity();
                this.strongest = i;
            }
        }
    }

    private int countThreadNum() {
        return this.countThreadNum > 0 ? this.countThreadNum :
               this.countThreadNum == 0 ? 1 : Runtime.getRuntime().availableProcessors();
    }

    private Thread[] prepareEarthRule() {
        Thread[] threads = new Thread[countThreadNum()];
        ThreadGroup group = new ThreadGroup("Calculate picture same");
        group.setDaemon(true);
        for (int i = 0; i < threads.length; i++) {
            Thread t = new Thread(group, new CalculateThread());
            threads[i] = t;
        }
        return threads;
    }

    private void happyNewYear() {
        this.fathers.clear();
        this.strongest = -1;
        this.allSimilarity = -1;
        this.year++;
    }

    private boolean canOperation() {
        return this.maxYear == -1 || this.year < this.maxYear;
    }

    Earth(String savePath, String godFile, int varianceRatio, int saveTimer, int countThreadNum, int initialNum,
          double growthRate, int DNANum, int maxYear) {
        this.DNANum         = Utils.ensureIntVarLt(DNANum, 0);
        this.maxYear        = Utils.ensureIntVarLt(maxYear, -2);
        this.savePath       = Utils.ensureStringHasContent(savePath);
        this.saveTimer      = Utils.ensureIntVarLt(saveTimer, -2);
        this.initialNum     = Utils.ensureIntVarLt(initialNum, 0);
        this.growthRate     = Utils.ensureDoubleVarInRange(growthRate, -1, 1);
        this.varianceRatio  = Utils.ensureIntVarInRange(varianceRatio, -1, 100);
        this.countThreadNum = Utils.ensureIntVarLt(countThreadNum, -2);

        Utils.ensureStringHasContent(godFile);
        try {
        this.god            = new Picture(ImageIO.read(new File(godFile).toURL()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to get pic.", e);
        }
        this.calculateThread = prepareEarthRule();
        for (Thread t : this.calculateThread) {
            t.start();
        }
    }

    /**
     * 将数据转换为 Json 格式。
     *
     * @return Json 格式数据
     */
    public JsonElement toJson() {
        if (this.newborns.size() > 0) {
            throw new IllegalStateException("The program hasn't finish, some son hasn't compare. Please waiting.");
        }
        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();
        for (Picture pic : this.fathers) {
            array.add(pic.toJson());
        }

        object.addProperty("year", this.year);
        object.addProperty("allSimilarity", this.allSimilarity);
        object.add("pics", array);

        return object;
    }
}
