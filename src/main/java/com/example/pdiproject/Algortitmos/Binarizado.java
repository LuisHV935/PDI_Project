package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;

import static com.example.pdiproject.Algortitmos.ConversionesColor.yiqARgb;
import static com.example.pdiproject.Algortitmos.ConversionesColor.rgbAYiq;

public class Binarizado {

    public static BufferedImage binarizar1Umbral(BufferedImage src) {
        double[][] yiq = rgbAYiq(src);
        double[] Y = yiq[0];
        double[] I = yiq[1];
        double[] Q = yiq[2];
        int width = src.getWidth();
        int height = src.getHeight();
        double[] Yb = new double[Y.length];
        for (int i = 0; i < Y.length; i++) {
            int gray = (int) Math.round(Y[i] * 255);
            Yb[i] = gray > 128 ? 1.0 : 0.0;
        }
        return ConversionesColor.yiqARgb(Yb, I, Q, width, height);
    }

    public static BufferedImage binarizar2Umbrales(BufferedImage src) {
        double[][] yiq = rgbAYiq(src);
        double[] Y = yiq[0];
        double[] I = yiq[1];
        double[] Q = yiq[2];
        int width = src.getWidth();
        int height = src.getHeight();
        double[] Yb = new double[Y.length];
        for (int i = 0; i < Y.length; i++) {
            int gray = (int) Math.round(Y[i] * 255);
            Yb[i] = gray > 85 ? (gray > 171 ? 1.0 : 128.0 / 255.0) : 0.0;
        }
        return yiqARgb(Yb, I, Q, width, height);
    }

    public static BufferedImage binarizar3Umbrales(BufferedImage src) {
        double[][] yiq = rgbAYiq(src);
        double[] Y = yiq[0];
        double[] I = yiq[1];
        double[] Q = yiq[2];
        int width = src.getWidth();
        int height = src.getHeight();
        double[] Yb = new double[Y.length];
        for (int i = 0; i < Y.length; i++) {
            int gray = (int) Math.round(Y[i] * 255);
            Yb[i] = gray > 64 ? (gray > 128 ? (gray > 192 ? 1.0 : 170.0 / 255.0) : 85.0 / 255.0) : 0.0;
        }
        return yiqARgb(Yb, I, Q, width, height);
    }

    public static BufferedImage binarizar4Umbrales(BufferedImage src) {
        double[][] yiq = rgbAYiq(src);
        double[] Y = yiq[0];
        double[] I = yiq[1];
        double[] Q = yiq[2];
        int width = src.getWidth();
        int height = src.getHeight();
        double[] Yb = new double[Y.length];
        for (int i = 0; i < Y.length; i++) {
            int gray = (int) Math.round(Y[i] * 255);
            Yb[i] = gray > 51 ? (gray > 102 ? (gray > 153 ? (gray > 204 ? 1.0 : 192.0 / 255.0) : 128.0 / 255.0) : 64.0 / 255.0) : 0.0;
        }
        return yiqARgb(Yb, I, Q, width, height);
    }

    public static BufferedImage invertirBinarizacion(BufferedImage src) {
        double[][] yiq = rgbAYiq(src);
        double[] Y = yiq[0];
        double[] I = yiq[1];
        double[] Q = yiq[2];
        int width = src.getWidth();
        int height = src.getHeight();
        double[] Yb = new double[Y.length];
        for (int i = 0; i < Y.length; i++) {
            Yb[i] = 1.0 - Y[i];
        }
        return yiqARgb(Yb, I, Q, width, height);
    }
}
