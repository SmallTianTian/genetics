package com.smalltiantian.genetics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;

class BaseData {
    private static BaseData baseData = null;

    List<Picture> fathers    = new ArrayList<Picture>();
    LinkedList<Picture> sons = new LinkedList<Picture>();

    // 目前经过了多少代的繁育
    int index;
    long similarityWithAllPic;
    // 原始图片相关信息
    final String originalImagePath;
    final int originalImageWidth;
    final int originalImageHeight;
    final int[][] originalImageRGB;
    final long originalImageRGBSun;

    public static BaseData getInstance() {
        if (baseData == null)
            throw new IllegalStateException("Error : You should init this instance(`BaseData`) by someway.");
        return baseData;
    }

    static BaseData init(String imagePath) throws IOException {
        if (baseData != null)
            throw new IllegalStateException("Error : You couldn't init this(`BaseData`) again.");
        
        baseData = new BaseData(0, imagePath);
        return baseData;
    }

    static BaseData init(List<Picture> fathers, int index, String imagePath) throws IOException {
        if (baseData != null)
            throw new IllegalStateException("Error : You couldn't init this(`BaseData`) again.");

        baseData = new BaseData(index, imagePath);
        baseData.fathers = fathers;

        return baseData;
    }

    public void addANewSon(Picture pic) {
        this.sons.add(pic);
    }

   private BaseData(int index, String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
		this.originalImageWidth  = image.getHeight();
		this.originalImageHeight = image.getWidth();
        this.originalImageRGBSun = this.originalImageWidth * this.originalImageHeight * 255 * 3L;

        this.originalImageRGB  = new int[this.originalImageWidth][this.originalImageHeight];
        for (int i = 0; i < this.originalImageWidth; i++) {
            for (int j = 0; j < this.originalImageHeight; j++)
                this.originalImageRGB[i][j] = image.getRGB(i, j);
        }

        this.index = index;
        this.originalImagePath = imagePath;
    }
}
