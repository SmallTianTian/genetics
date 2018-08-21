package com.smalltiantian.genetics;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

class Picture {
    private List<Triangle> cells = new ArrayList<>();
    private int similarity;
    private BufferedImage cache;
    private final Earth earth;

    /**
     * 仅第一次初始化时调用
     */
    public Picture(int num, Earth earth) {
        this.earth = earth;

        while (--num > 0) {
            int height = earth.god().cache.getHeight();
            int width  = earth.god().cache.getWidth();
            this.cells.add(new Triangle(height, width));
        }
    }

    Picture(JsonObject picStr, Earth earth) {
        this.earth = earth;

        JsonArray pic = picStr.getAsJsonArray("pic");

        List<Triangle> cells = new ArrayList<>(pic.size());
        for(JsonElement element : pic) {
            cells.add(new Triangle(element.getAsJsonObject()));
        }
        this.cells = cells;
        this.similarity = picStr.get("similarity").getAsInt();
    }

    Picture(Picture father, Picture mather, boolean isVariation) {
        if (father.earth != mather.earth) throw new IllegalStateException("Father come from Earth, mather come from Mars.");

        this.earth = father.earth;

        List<Triangle> cells = new ArrayList(father.cells.size());
        cells.addAll(father.getHeadTriangle());
        cells.addAll(mather.getBottonTriangle());

        Collections.shuffle(cells);

		if (isVariation)
			variation();
	}

    Picture(BufferedImage image) {
        this.earth = null;
        this.cache = image;
    }

    public int similarity() {
        return this.similarity;
    }

    /**
     * 根据已有的三角形集合画图并存储在本地磁盘上
     *
     */
    public void draw() throws IOException {
		String name = String.format("%s/%d-%d.jpg", this.earth.address(),
                                    this.earth.year(), this.similarity);
		ImageIO.write(drawImge(), "jpg", new File(name));
	}

    /**
     * 将数据转换为 Json 格式输出。
     *
     * 示例：
     * {"pic":[{'x':[1,2,3], 'y':[4,5,6], 'r':[100,254,31]}, {'x':[1,2,3], 'y':[4,5,6], 'r':[100,254,31]}], "similarity":100000}
     * @return JsonElement
     */
    JsonElement toJson() {
        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();
        for (Triangle t : this.cells) {
            array.add(t.toJson());
        }

        object.add("pic", array);
        object.addProperty("similarity", this.similarity);
        return object;
    }

    void similarity(int similarity) {
        this.similarity = similarity;
    }

    /**
     * 根据已有的三角形集合开始在内存中构建图像
     *
     * @return 缓存的图像
     */
    BufferedImage drawImge() {
        if (this.cache == null) {
            int height = this.earth.god().cache.getHeight();
            int width  = this.earth.god().cache.getWidth();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics   = image.getGraphics();

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);

            for (Triangle tri : this.cells) {
                int[] x = tri.xPoint();
                int[] y = tri.yPoint();
                int[] r = tri.rgb();
                graphics.setColor(new Color(r[0], r[1], r[2], 64));
                graphics.fillPolygon(x, y, 3);
            }
            this.cache = image;
        }
		return this.cache;
	}

    /**
     * 取前一半的三角形
     *
     * 例：总数 10 ，返回前 5 个
     *     总数 11 ，返回前 5 个
     *
     * @return 前一半的三角形 {@code List} 集合
     */
    private List<Triangle> getHeadTriangle() {
		return new ArrayList<>(cells.subList(0, this.cells.size() / 2));
	}

    /**
     * 取后一半三角形
     *
     * 例：总数 10 ，返回后 5 个
     *     总数 11 ，返回后 6 个
     *
     * @return 后一半的三角形 {@code List} 集合
     */
    private List<Triangle> getBottonTriangle() {
		return new ArrayList<>(cells.subList(this.cells.size() / 2, this.cells.size()));
	}

    /**
     * 选择一个三角形变异
     */
    private void variation() {
		int index = Utils.randomInt(cells.size());
		this.cells.get(index).variation();
	}
}
