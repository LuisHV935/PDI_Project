package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Ruido {

    static int getGray(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y) >> 16) & 0xFF;
    }

    static void setGray(BufferedImage img, int x, int y, int gray) {
        gray = Math.clamp(gray, 0, 255);
        int rgb = (255 << 24) | (gray << 16) | (gray << 8) | gray;
        img.setRGB(x, y, rgb);
    }

    static BufferedImage copy(BufferedImage src) {
        BufferedImage dst = new BufferedImage(
                src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(src, 0, 0, null);
        return dst;
    }

    public static BufferedImage gaussiano(BufferedImage src, double sigma) {
        BufferedImage dst = copy(src);
        Random rnd = new Random();

        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                setGray(dst, x, y, (int)(getGray(src, x, y) + sigma * rnd.nextGaussian()));

        return dst;
    }

    public static BufferedImage saltAndPepper(BufferedImage src, double probabilidad) {
        BufferedImage dst = copy(src);
        Random rnd = new Random();

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                double r = rnd.nextDouble();
                if (r < probabilidad / 2.0) setGray(dst, x, y, 0);
                else if (r < probabilidad) setGray(dst, x, y, 255);
                else setGray(dst, x, y, getGray(src, x, y));
            }
        }
        return dst;
    }

    public static BufferedImage uniforme(BufferedImage src, double varianza) {
        BufferedImage dst = copy(src);
        Random rnd = new Random();
        double limite = Math.sqrt(3.0 * varianza);

        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                double ruido = (rnd.nextDouble() * 2.0 - 1.0) * limite;
                setGray(dst, x, y, (int)(getGray(src, x, y) + ruido));
            }

        return dst;
    }

    public static BufferedImage rayleigh(BufferedImage src, double varianza) {
        BufferedImage dst = copy(src);
        Random rnd = new Random();
        double a = Math.sqrt(varianza / (2.0 - Math.PI / 2.0));

        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                double ruido = Math.sqrt(-2.0 * a * a * Math.log(1.0 - rnd.nextDouble()));
                setGray(dst, x, y, (int)(getGray(src, x, y) + ruido));
            }

        return dst;
    }

    public static BufferedImage exponencialNegativo(BufferedImage src, double varianza) {
        BufferedImage dst = copy(src);
        Random rnd = new Random();
        double a = Math.sqrt(varianza);

        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                double ruido = -a * Math.log(1.0 - rnd.nextDouble());
                setGray(dst, x, y, (int)(getGray(src, x, y) + ruido));
            }

        return dst;
    }

    public static BufferedImage gamma(BufferedImage src, double varianza, int alpha) {
        BufferedImage dst = copy(src);
        Random rnd = new Random();
        double a = Math.sqrt(varianza / (double) alpha) / 2.0;

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                double acumulado = 0;
                for (int k = 0; k < alpha; k++) {
                    double ruido = Math.sqrt(-2.0 * a * Math.log(1.0 - rnd.nextDouble()));
                    double theta = rnd.nextDouble() * 2.0 * Math.PI - Math.PI;
                    double rx = ruido * Math.cos(theta);
                    double ry = ruido * Math.sin(theta);
                    acumulado += rx * rx + ry * ry;
                }
                setGray(dst, x, y, (int)(getGray(src, x, y) + acumulado));
            }
        }
        return dst;
    }
}