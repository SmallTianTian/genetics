package com.smalltiantian.genetics;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Picture {
    private List<Triangle> triangle = new ArrayList<>();
    int similarity;

    /**
     * 仅第一次初始化时调用
     */
    public Picture(int num) {
        while (--num > 0)
            this.triangle.add(new Triangle());
    }

    public Picture(List<Triangle> triangles, boolean isVariation) {
		if (triangles.size() == 0)
			throw new IllegalStateException("Error : You must hava one Triangle in this Picture.");
		this.triangle = triangles;
        Collections.shuffle(this.triangle);

		if (isVariation)
			variation();
	}

    /**
     * 选择一个三角形变异
     */
    private void variation() {
		int index = BaseUtil.getRandomInt(triangle.size());
		Triangle triangle = this.triangle.get(index).variation();
		this.triangle.set(index, triangle);
	}

    /**
     * 取前一半的三角形
     *
     * 例：总数 10 ，返回前 5 个
     *     总数 11 ，返回前 5 个
     *
     * @return 前一半的三角形 {@code List} 集合
     */
    public List<Triangle> getHeadTriangle() {
		return new ArrayList<>(triangle.subList(0, this.triangle.size() / 2));
	}

    /**
     * 取后一半三角形
     *
     * 例：总数 10 ，返回后 5 个
     *     总数 11 ，返回后 6 个
     *
     * @return 后一半的三角形 {@code List} 集合
     */
    public List<Triangle> getBottonTriangle() {
		return new ArrayList<>(triangle.subList(this.triangle.size() / 2, this.triangle.size()));
	}

    /**
     * 根据已有的三角形集合开始在内存中构建图像
     *
     * @return 缓存的图像
     */
    public BufferedImage drawImge() {
		BufferedImage image = new BufferedImage(BaseData.getInstance().originalImageWidth, BaseData.getInstance().originalImageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics   = image.getGraphics();

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,0,BaseData.getInstance().originalImageWidth, BaseData.getInstance().originalImageHeight);

		for (Triangle tri : this.triangle) {
			int[] x = tri.xPoint;
			int[] y = tri.yPoint;
			int[] r = tri.rgb;
			graphics.setColor(new Color(r[0], r[1], r[2], 64));
			graphics.fillPolygon(x, y, 3);
		}
		return image;
	}

    /**
     * 根据已有的三角形集合画图并存储在本地磁盘上
     *
     */
    public void writeToLocal() throws IOException {
		String name = String.format(Config.getInstance().saveAddress() + "/%d-%d.jpg", BaseData.getInstance().index, similarity);
		ImageIO.write(drawImge(), "jpg", new File(name));
	}
}
