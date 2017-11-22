package com.smalltiantian.genetics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

class Triangle {
    int[] xPoint = new int[3];
	int[] yPoint = new int[3];
	int[] rgb    = new int[3];

    public Triangle() {
		for(int i = 0; i < xPoint.length; i++)
			this.xPoint[i] = BaseUtil.getRandomInt(BaseData.getInstance().originalImageWidth);
		for(int i = 0; i < yPoint.length; i++)
			this.yPoint[i] = BaseUtil.getRandomInt(BaseData.getInstance().originalImageHeight);
		for(int i = 0; i < rgb.length; i++)
			this.rgb[i]    = BaseUtil.getRandomInt(255);
	}

    public Triangle(int[] xPoint, int[] yPoint, int[] rgb) {
		this.xPoint = xPoint;
		this.yPoint = yPoint;
		this.rgb    = rgb;
	}

    public Triangle(JsonObject element) {
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
    Triangle variation() {
		//随机改变第几位
		int index = BaseUtil.getRandomInt(2);
		//选择随机改变的变量
		switch (BaseUtil.getRandomInt(2)) {
		case 0:
			int[] newXPoint = new int[]{this.xPoint[0], this.xPoint[1], this.xPoint[2]};
			newXPoint[index] = BaseUtil.getRandomInt(BaseData.getInstance().originalImageWidth);
			return new Triangle(newXPoint, this.yPoint, this.rgb);
		case 1:
			int[] newYPoint = new int[]{this.yPoint[0], this.yPoint[1], this.yPoint[2]};
			newYPoint[index] = BaseUtil.getRandomInt(BaseData.getInstance().originalImageHeight);
			return new Triangle(xPoint, newYPoint, rgb);
		default:
			int[] newRgb = new int[]{this.rgb[0], this.rgb[1], this.rgb[2]};
			newRgb[index] = BaseUtil.getRandomInt(256);
			return new Triangle(xPoint, yPoint, newRgb);
		}
	}

    /**
     * 将数据转换为 Json 格式输出。
     *
     * 示例：
     * {'x':[1,2,3], 'y':[4,5,6], 'r':[100,254,31]}
     *
     * @return JsonElement
     */
    public JsonElement toJson() {
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
        return object;
    }
}
