package com.smalltiantian.genetics;

import java.util.Random;
import java.awt.image.BufferedImage;

class Utils {
    private static final Random RD = new Random();

    public static String systemProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    /**
     * 获得 int 类型的随机数
     *
     * @param  max 随机数的最大值
     * @return 0 - max 的数
     */
    public static int randomInt(int max) {
		return RD.nextInt(max);
    }

    public static double randomDoubleInRange(double range, boolean needMinus) {
        double v = RD.nextDouble() * range;
        return needMinus ? RD.nextBoolean() ? 0 - v : v : v;
    }

    public static double nextDouble() {
        return RD.nextDouble();
    }

    public static int ensureDoubleVarInRange(int check, int l, int r) {
        if (check < l || check > r) {
            String msg = String.format("Number `%d` must in [`%d`, `%d`].", check, l, r);
            throw new IllegalArgumentException(msg);
        }
        return check;
    }

    public static double ensureDoubleVarInRange(double check, double l, double r) {
        if (check < l || check > r) {
            String msg = String.format("Number `%d` must in [`%d`, `%d`].", check, l, r);
            throw new IllegalArgumentException(msg);
        }
        return check;
    }

    public static boolean stringHasContent(String v) {
        return !(v == null || v.trim().isEmpty());
    }

    public static String ensureStringHasContent(String check) {
        if (stringHasContent(check)) {
            return check;
        }
        throw new IllegalArgumentException("Value is null or empty.");
    }

    public static int ensureIntVarInRange(int v, int l, int r) {
        if (v < l || v > r) {
            String msg = "Value `%s` is > `%s` or < `%s`.";
            throw new IllegalArgumentException(String.format(msg, v, l, r));
        }
        return v;
    }

    public static int ensureIntVarLt(int check, int standard) {
        if (check <= standard) {
            String msg = String.format("Number `%d` must > `%d`.", check, standard);
            throw new IllegalArgumentException(msg);
        }
        return check;
    }

    public static int checkSimilarity(Picture check, Picture base) {
        int similarity = 0;
        BufferedImage checkImage = check.drawImge();
        BufferedImage baseImage  = base.drawImge();

        for (int i = 0; i < baseImage.getWidth(); i++) {
            for (int j = 0; j < baseImage.getHeight(); j++) {
                int checkRGB = checkImage.getRGB(i, j);
                int baseRGB  = baseImage.getRGB(i, j);
                similarity  += sameRGB(checkRGB, baseRGB);
            }
        }
        return similarity;
    }

    private static int sameRGB(int one, int two) {
        return 255 * 3 * 3 - compareRGB(one, two);
    }

    /**
     * 比较两个点 RGB 值的差异
     *
     * @param  one 第一个点的 RGB 值
     * @param  two 第二个点的 RGB 值
     * @return 差异
     */
    private static int compareRGB(int one, int two) {
        int r = Math.abs(((one & 0xff0000) >> 16) - ((two & 0xff0000) >> 16));
		int g = Math.abs(((one & 0xff00) >> 8) - ((two & 0xff00) >> 8));
		int b = Math.abs((one & 0xff) - (two & 0xff));
		return r + g + b;
    }

    private Utils(){}
}
