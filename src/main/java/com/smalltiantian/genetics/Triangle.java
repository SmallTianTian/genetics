package com.smalltiantian.genetics;

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
}
