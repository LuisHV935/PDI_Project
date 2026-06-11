package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;

public class OperacionesAritmeticas {

    static BufferedImage copy(BufferedImage src) {
        BufferedImage dst = new BufferedImage(
                src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(src, 0, 0, null);
        return dst;
    }

    static int clamp(int v) {
        return Math.clamp(v, 0, 255);
    }

    static int getR(int rgb) { return (rgb >> 16) & 0xFF; }
    static int getG(int rgb) { return (rgb >> 8) & 0xFF; }
    static int getB(int rgb) { return rgb & 0xFF; }
    static int getA(int rgb) { return (rgb >> 24) & 0xFF; }

    static int makePixel(int a, int r, int g, int b) {
        return (a << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    }

    public static BufferedImage sumar(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r = getR(p1) + getR(p2);
                int g = getG(p1) + getG(p2);
                int b = getB(p1) + getB(p2);
                int a = Math.max(getA(p1), getA(p2));
                resultado.setRGB(x, y, makePixel(a, r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage sumarEscalar(BufferedImage img, int valor) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                int r = getR(p) + valor;
                int g = getG(p) + valor;
                int b = getB(p) + valor;
                resultado.setRGB(x, y, makePixel(getA(p), r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage restar(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r = getR(p1) - getR(p2);
                int g = getG(p1) - getG(p2);
                int b = getB(p1) - getB(p2);
                int a = Math.max(getA(p1), getA(p2));
                resultado.setRGB(x, y, makePixel(a, r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage restarEscalar(BufferedImage img, int valor) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                int r = getR(p) - valor;
                int g = getG(p) - valor;
                int b = getB(p) - valor;
                resultado.setRGB(x, y, makePixel(getA(p), r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage multiplicar(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r = (getR(p1) * getR(p2)) / 255;
                int g = (getG(p1) * getG(p2)) / 255;
                int b = (getB(p1) * getB(p2)) / 255;
                int a = Math.max(getA(p1), getA(p2));
                resultado.setRGB(x, y, makePixel(a, r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage multiplicarEscalar(BufferedImage img, double valor) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                int r = (int) (getR(p) * valor);
                int g = (int) (getG(p) * valor);
                int b = (int) (getB(p) * valor);
                resultado.setRGB(x, y, makePixel(getA(p), r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage dividir(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r = getR(p2) == 0 ? 0 : (getR(p1) * 255) / getR(p2);
                int g = getG(p2) == 0 ? 0 : (getG(p1) * 255) / getG(p2);
                int b = getB(p2) == 0 ? 0 : (getB(p1) * 255) / getB(p2);
                int a = Math.max(getA(p1), getA(p2));
                resultado.setRGB(x, y, makePixel(a, r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage dividirEscalar(BufferedImage img, double valor) {
        BufferedImage resultado = copy(img);
        if (valor == 0) return resultado;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                int r = (int) (getR(p) / valor);
                int g = (int) (getG(p) / valor);
                int b = (int) (getB(p) / valor);
                resultado.setRGB(x, y, makePixel(getA(p), r, g, b));
            }
        }
        return resultado;
    }
}
