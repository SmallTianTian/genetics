package com.smalltiantian.genetics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;

public class BaseUtil {
    static Random random = new Random();
    private static Gson gson     = new Gson();

    /**
     * 获得 int 类型的随机数
     *
     * @param  max 随机数的最大值
     * @return 0 - max 的数
     */
    public static int getRandomInt(int max) {
		return random.nextInt(max);
    }

    /**
     * 将目前所有状态保存到本地磁盘上
     */
    public static void storedToDisk() {
        int stronger = whoIsStronger();

        String baseData = gson.toJson(BaseData.getInstance());

        FileOutputStream os = null;
        try {
            BaseData.getInstance().fathers.get(stronger).writeToLocal();
            os = new FileOutputStream(new File(Config.getInstance().saveAddress() + "/base_data.json"));
            IOUtils.write(baseData, os, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * 单例获取 Gson
     *
     * @return Gson
     */
    public static Gson gson() {
        return gson;
    }

    /**
     * 获得缓冲图片的每个点的 RGB 值
     *
     * @param  image 缓冲图片
     * @return 包含每个点 RGB 值的二维数组
     */
    static int[][] getRGB(BufferedImage image) {
        int[][] rgb = new int[BaseData.getInstance().originalImageWidth][BaseData.getInstance().originalImageHeight];
        for (int i = 0; i < BaseData.getInstance().originalImageWidth; i++) {
            for (int j = 0; j < BaseData.getInstance().originalImageHeight; j++)
                rgb[i][j] = image.getRGB(i, j);
        }
        return rgb;
    }

    /**
     * 比较两个点 RGB 值的差异
     *
     * @param  one 第一个点的 RGB 值
     * @param  two 第二个点的 RGB 值
     * @return 差异
     */
    static int compareRGB(int one, int two) {
        int r = Math.abs(((one & 0xff0000) >> 16) - ((two & 0xff0000) >> 16));
		int g = Math.abs(((one & 0xff00) >> 8)-((two & 0xff00) >> 8));
		int b = Math.abs((one & 0xff)-(two & 0xff));
		return r + g + b;
    }

    /**
     * 与原始图片比较每个点的 RGB 相同量
     *
     * @param  pic 将要比较的图片
     * @return 所有点的 RGB 差异总和
     */
    public static int checkSimilarity(Picture pic) {
        int[][] newPic = getRGB(pic.drawImge());
        long diff = 0;

        for (int i = 0; i < BaseData.getInstance().originalImageWidth; i++) {
			for (int j = 0; j < BaseData.getInstance().originalImageHeight; j++)
				diff += compareRGB(BaseData.getInstance().originalImageRGB[i][j], newPic[i][j]);
		}
        double similarity = (double)(BaseData.getInstance().originalImageRGBSun - diff) / BaseData.getInstance().originalImageRGBSun;
        while ((similarity *= 10) < 1) {}

        pic.similarity = (int)(similarity * 10000000);

        BaseData.getInstance().fathers.add(pic);
        return pic.similarity;
    }

    /**
     * 物尽天择，高富帅生存
     *
     * 根据与 GOD 的相似度来确定高富帅，越像就有更大概率让别人为他生猴子，上一代的高富帅还能和子孙XX，保证下一代的高富帅一定不会比上一代差
     * 下一代可能接受宇宙射线的洗礼，发生基因突变
     *
     * @param  sonsNum 下一代孩子的总数
     * @param  varianceRatio 变异的概率
     */
    public static void newborn(int populationNum, int varianceRatio) {
        List<Picture> willAdd = new ArrayList<>();

        int stronger = whoIsStronger();
        willAdd.add(BaseData.getInstance().fathers.get(stronger));

        for (int i = 1; i < populationNum; i++) {
            long random1 = (long)(random.nextDouble() * BaseData.getInstance().similarityWithAllPic);
			long random2 = (long)(random.nextDouble() * BaseData.getInstance().similarityWithAllPic);

            List<Triangle> newPic = new ArrayList<>();

            for (Picture pic : BaseData.getInstance().fathers) {
				if ((random1 -= pic.similarity) < 0) {
					newPic.addAll(pic.getHeadTriangle());
					break;
				}
			}
			for (Picture pic : BaseData.getInstance().fathers) {
				if ((random2 -= pic.similarity) < 0) {
					newPic.addAll(pic.getBottonTriangle());
					break;
				}
			}

            willAdd.add(new Picture(newPic, getRandomInt(100) < varianceRatio));
        }

        BaseData.getInstance().fathers.clear();

        for (Picture pic : willAdd) {
            try {
                BaseData.getInstance().sons.put(pic);
            } catch (Exception e) {
                // 简单粗暴，不推荐
                e.printStackTrace();
            }
        }
    }

    /**
     * 是时候选出谁才是真正的高富帅了
     *
     * 评选过程会计算差异的总值
     *
     * @return 高富帅在这一代中的学号
     */
    public static int whoIsStronger() {
		long all  = 0;
		int max   = 0;
		int index = 0;

        for (int i = 0; i < BaseData.getInstance().fathers.size(); i++) {
			int similarity = BaseData.getInstance().fathers.get(i).similarity;
			all += similarity;
			if (similarity > max) {
				index = i;
				max = similarity;
			}
		}

        BaseData.getInstance().similarityWithAllPic = all;
		return index;
	}

    private BaseUtil(){}
}
