package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;

public class OperacionesLogicas {

    static BufferedImage copy(BufferedImage src) {
        BufferedImage dst = new BufferedImage(
                src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(src, 0, 0, null);
        return dst;
    }

    public static BufferedImage and(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                resultado.setRGB(x, y, p1 & p2);
            }
        }
        return resultado;
    }

    public static BufferedImage andEscalar(BufferedImage img, int valor) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                resultado.setRGB(x, y, p & valor);
            }
        }
        return resultado;
    }

    public static BufferedImage or(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                resultado.setRGB(x, y, p1 | p2);
            }
        }
        return resultado;
    }

    public static BufferedImage orEscalar(BufferedImage img, int valor) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                resultado.setRGB(x, y, p | valor);
            }
        }
        return resultado;
    }

    public static BufferedImage not(BufferedImage img) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xFF;
                int r = (~(p >> 16)) & 0xFF;
                int g = (~(p >> 8)) & 0xFF;
                int b = (~p) & 0xFF;
                resultado.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return resultado;
    }

    public static BufferedImage xor(BufferedImage img1, BufferedImage img2) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int a = Math.max((p1 >> 24) & 0xFF, (p2 >> 24) & 0xFF);
                int r = ((p1 >> 16) & 0xFF) ^ ((p2 >> 16) & 0xFF);
                int g = ((p1 >> 8) & 0xFF) ^ ((p2 >> 8) & 0xFF);
                int b = (p1 & 0xFF) ^ (p2 & 0xFF);
                resultado.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return resultado;
    }

    public static BufferedImage xorEscalar(BufferedImage img, int valor) {
        BufferedImage resultado = copy(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                resultado.setRGB(x, y, p ^ valor);
            }
        }
        return resultado;
    }
}
