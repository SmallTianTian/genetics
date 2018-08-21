package com.smalltiantian.genetics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

class Triangle {
    private final int[] xPoint = new int[3];
    private final int[] yPoint = new int[3];
    private final int[] rgb    = new int[3];
    private final int height;
    private final int width;

    public Triangle(int height, int width) {
        this.height = height;
        this.width  = width;

		for(int i = 0; i < xPoint.length; i++)
			this.xPoint[i] = Utils.randomInt(this.width);
		for(int i = 0; i < yPoint.length; i++)
			this.yPoint[i] = Utils.randomInt(this.height);
		for(int i = 0; i < rgb.length; i++)
			this.rgb[i]    = Utils.randomInt(255);
	}

    Triangle(JsonObject element) {
        this.height = element.get("h").getAsInt();
        this.width  = element.get("w").getAsInt();

        JsonArray xPoint = element.getAsJsonArray("x");
        JsonArray yPoint = element.getAsJsonArray("y");
        JsonArray rgb    = element.getAsJsonArray("r");
        for (int i = 0; i < 3; i++) {
            this.xPoint[i] = xPoint.get(i).getAsInt();
            this.yPoint[i] = yPoint.get(i).getAsInt();
            this.rgb[i]    = rgb.get(i).getAsInt();
        }
    }

    /**
     * 进行变异
     *
     * 随机选择三个定点或 RGB 中一个值进行变异
     *
     * @return  变异后的三角形
     */
    void variation() {
		//随机改变第几位
		int index = Utils.randomInt(2);
		//选择随机改变的变量
		switch (Utils.randomInt(2)) {
		case 0:
			int[] newXPoint = new int[]{this.xPoint[0], this.xPoint[1], this.xPoint[2]};
			newXPoint[index] = Utils.randomInt(this.width);
		case 1:
			int[] newYPoint = new int[]{this.yPoint[0], this.yPoint[1], this.yPoint[2]};
			newYPoint[index] = Utils.randomInt(this.height);
		default:
			int[] newRgb = new int[]{this.rgb[0], this.rgb[1], this.rgb[2]};
			newRgb[index] = Utils.randomInt(255);
		}
	}

    int[] xPoint() {
        return this.xPoint;
    }

    int[] yPoint() {
        return this.yPoint;
    }

    int[] rgb() {
        return this.rgb;
    }

    /**
     * 将数据转换为 Json 格式输出。
     *
     * 示例：
     * {'x':[1,2,3], 'y':[4,5,6], 'r':[100,254,31], 'w':100, 'h':100}
     *
     * @return JsonElement
     */
    JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray xPoint = new JsonArray();
        JsonArray yPoint = new JsonArray();
        JsonArray rgb    = new JsonArray();
        for (int i = 0; i < 3; i++) {
            xPoint.add(this.xPoint[i]);
            yPoint.add(this.yPoint[i]);
            rgb.add(this.rgb[i]);
        }
        object.add("x", xPoint);
        object.add("y", yPoint);
        object.add("r", rgb);
        object.addProperty("h", this.height);
        object.addProperty("w", this.width);
        return object;
    }
}
