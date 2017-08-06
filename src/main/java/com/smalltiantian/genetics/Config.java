package com.smalltiantian.genetics;

import java.io.File;

class Config {
    private static Config config = null;

    private final String saveAddress;   // 保存地址
    private final int varianceRatio;    // 变异概率（-1 为自动选择）
    private final int saveTimer;        // 多少代保存一次
    private final int countThreadNum;   // 多少个线程参与计算
    private final int initialNum;       // 初代种数量
    private final double growthRate;    // 增长率（-1 为自动选择）
    private final int DNANum;           // 每一个个体有多少个 DNA

    public static Config initByDefault() {
        String path = System.getProperty("user.dir");
        int countThreadNum = Runtime.getRuntime().availableProcessors();
        return init(path, -1, 20, countThreadNum, 100, -1, 100);
    }

    public static Config init(String saveAddress, int varianceRatio, int saveTimer, int countThreadNum, int initialNum, double growthRate, int DNANum) {
        if (config != null)
            throw new IllegalArgumentException("Error : You couldn't init this(`Config`) again.");
        config = new Config(saveAddress, varianceRatio, saveTimer, countThreadNum, initialNum, growthRate, DNANum);
        return config;
    }

    public static Config getInstance() {
        if (config == null)
            throw new IllegalStateException("Error : You should init this instance(`Config`) by someway.");
        return config;
    }

    Config(String saveAddress, int varianceRatio, int saveTimer, int countThreadNum, int initialNum, double growthRate, int DNANum) {
        File file = new File(saveAddress);
        if (!file.canWrite())
            throw new IllegalArgumentException("Error : Your `saveAddress` couldn't write.Please check it in you config.");
        if (saveTimer <= 0)
            throw new IllegalArgumentException("Error : You mast let `saveTimer` which in your config > 0.");
        if (varianceRatio < -1 || varianceRatio >100)
            throw new IllegalArgumentException("Error : You mast let `varianceRatio` which in your config >= -1 or <= 100.");
        if (countThreadNum < 1)
            throw new IllegalArgumentException("Error : You mast let `countThreadNum` which in your config >= 1.");
        if (initialNum < 20)
            throw new IllegalArgumentException("Error : You mast let `initialNum` which in your config >= 20.");
        if (growthRate >= 1 || growthRate < -1)
            throw new IllegalArgumentException("Error : You mast let `growthRate` which in your config < 1 or >-1.");
        if (DNANum < 50)
            throw new IllegalArgumentException("Error : You mast let `DNANum` which in your config >= 50");

        this.growthRate     = growthRate;
        this.initialNum     = initialNum;
        this.saveTimer      = saveTimer;
        this.saveAddress    = saveAddress;
        this.varianceRatio  = varianceRatio;
        this.DNANum         = DNANum;
        switch (countThreadNum) {
            case 0:
               this.countThreadNum =  Runtime.getRuntime().availableProcessors();
               break;
            default:
               this.countThreadNum = countThreadNum;
        }
    }

    String saveAddress() {
        return this.saveAddress;
    }

    int DNANum() {
        return this.DNANum;
    }

    int varianceRatio() {
        switch (this.varianceRatio) {
            case 0:
                return BaseUtil.getRandomInt(30);
            case -1:
                int similarity = BaseData.getInstance().fathers.get(BaseUtil.whoIsStronger()).similarity;
                while ((similarity %= 10) > 100) {}
                return 100 - similarity;
            default:
                return this.varianceRatio;
        }
    }

    int saveTimer() {
        return this.saveTimer;
    }

    int countThreadNum() {
        return this.countThreadNum;
    }

    int populationNum() {
        double growthRate;
        if (this.growthRate == -1)
            growthRate = BaseUtil.random.nextBoolean() ? BaseUtil.random.nextDouble() * 0.3 : 0 - BaseUtil.random.nextDouble() * 0.3;
        else
            growthRate = this.growthRate;
        return this.initialNum + (int)(this.initialNum * growthRate);
    }
}
