package com.smalltiantian.genetics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import javax.imageio.ImageIO;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

class BaseData {
    private static BaseData baseData = null;

    List<Picture> fathers    = new ArrayList<Picture>();
    LinkedBlockingQueue<Picture> sons = new LinkedBlockingQueue<Picture>();

    // 目前经过了多少代的繁育
    int index;
    long similarityWithAllPic;
    // 原始图片相关信息
    final int originalImageWidth;
    final int originalImageHeight;
    final int[][] originalImageRGB;
    final long originalImageRGBSun;

    /**
     * 获取实例。
     *
     * @return 实例
     */
    public static BaseData getInstance() {
        if (baseData == null)
            throw new IllegalStateException("Error : You should init this instance(`BaseData`) by someway.");
        return baseData;
    }

    /**
     * 根据已有凡人数据及上帝图片初始化。
     *
     * 注意：此方法会将凡人数据复位，继续进化。
     *
     * @param  json Json 格式的凡人数据。
     * @param  imagePath 上帝图片位置。
     * @return BaseData
     */
    static BaseData init(JsonObject json, File imagePath) throws IOException {
        if (baseData != null)
            throw new IllegalStateException("Error : You couldn't init this(`BaseData`) again.");

        int index = json.get("index").getAsInt();
        baseData  = new BaseData(0, imagePath);

        JsonArray array = json.getAsJsonArray("pics");
        for (JsonElement e : array) {
            baseData.fathers.add(new Picture(e.getAsJsonObject()));
        }

        long similarity = json.get("similarity").getAsLong();
        baseData.similarityWithAllPic = similarity;

        return baseData;
    }

    /**
     * 根据上帝图片初始化。
     *
     * 以上帝图片作为方向去进化。
     *
     * 此方法会根据配置文件生成初代种。
     *
     * @param  imagePath 上帝图片文件
     * @return BaseData
     */
    static BaseData init(File imagePath) throws IOException {
        if (baseData != null)
            throw new IllegalStateException("Error : You couldn't init this(`BaseData`) again.");
        
        baseData = new BaseData(0, imagePath);

        int populationNum = Config.getInstance().populationNum();
        for (int i = 0; i < populationNum; i ++) {
            BaseUtil.checkSimilarity(new Picture(Config.getInstance().DNANum()));
        }
        return baseData;
    }

    /**
     * 添加一张已检测的图片。
     *
     * 防止多线程问题。
     *
     * @param  pic 已检测的图片
     */
    public synchronized void addFathers(Picture pic) {
        this.fathers.add(pic);
    }

    /**
     * 添加一张待检测图片。
     *
     * 为不抛出异常
     *
     * @param  pic 待检测图片
     */
    public void addANewSon(Picture pic) {
        try {
            this.sons.put(pic);
        } catch (Exception e) {
            // 简单粗暴的，不推荐
            e.printStackTrace();
        }
    }

    /**
     * 将数据转换为 Json 格式。
     *
     * @return Json 格式数据
     */
    public JsonElement toJson() {
        if (sons.size() > 0) {
            throw new RuntimeException("Error : The program stage hasn't finish, some son hasn't compare. Please waiting.");
        }
        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();
        for (Picture pic : this.fathers) {
            array.add(pic.toJson());
        }

        object.addProperty("index", this.index);
        object.addProperty("similarity", this.similarityWithAllPic);
        object.add("pics", array);

        return object;
    }

   private BaseData(int index, File imagePath) throws IOException {
        BufferedImage image = ImageIO.read(imagePath);
		this.originalImageWidth  = image.getHeight();
		this.originalImageHeight = image.getWidth();
        this.originalImageRGBSun = this.originalImageWidth * this.originalImageHeight * 255 * 3L;

        this.originalImageRGB  = new int[this.originalImageWidth][this.originalImageHeight];
        for (int i = 0; i < this.originalImageWidth; i++) {
            for (int j = 0; j < this.originalImageHeight; j++)
                this.originalImageRGB[i][j] = image.getRGB(i, j);
        }

        this.index = index;
    }
}
