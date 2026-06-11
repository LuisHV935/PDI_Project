package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;

public class Transformaciones {

    static int getRGB(BufferedImage img, int x, int y) {
        return img.getRGB(x, y);
    }

    static void setRGB(BufferedImage img, int x, int y, int rgb) {
        if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
            img.setRGB(x, y, rgb);
        }
    }

    static BufferedImage copy(BufferedImage src) {
        BufferedImage dst = new BufferedImage(
                src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(src, 0, 0, null);
        return dst;
    }

    static int getPixel(BufferedImage img, double x, double y, String interpolacion) {
        int xi = (int) Math.floor(x);
        int yi = (int) Math.floor(y);
        double dx = x - xi;
        double dy = y - yi;

        if (interpolacion.equals("Vecino mas cercano")) {
            int xn = (int) Math.round(x);
            int yn = (int) Math.round(y);
            xn = Math.clamp(xn, 0, img.getWidth() - 1);
            yn = Math.clamp(yn, 0, img.getHeight() - 1);
            return img.getRGB(xn, yn);
        }

        // Bilineal
        int x0 = Math.clamp(xi, 0, img.getWidth() - 1);
        int x1 = Math.clamp(xi + 1, 0, img.getWidth() - 1);
        int y0 = Math.clamp(yi, 0, img.getHeight() - 1);
        int y1 = Math.clamp(yi + 1, 0, img.getHeight() - 1);

        int p00 = img.getRGB(x0, y0);
        int p10 = img.getRGB(x1, y0);
        int p01 = img.getRGB(x0, y1);
        int p11 = img.getRGB(x1, y1);

        int a0 = interpol(p00, p10, dx);
        int a1 = interpol(p01, p11, dx);
        return interpol(a0, a1, dy);
    }

    static int interpol(int c1, int c2, double t) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = (int) Math.round(a1 + (a2 - a1) * t);
        int r = (int) Math.round(r1 + (r2 - r1) * t);
        int g = (int) Math.round(g1 + (g2 - g1) * t);
        int b = (int) Math.round(b1 + (b2 - b1) * t);
        return (a << 24) | (Math.clamp(r, 0, 255) << 16) | (Math.clamp(g, 0, 255) << 8) | Math.clamp(b, 0, 255);
    }

    public static BufferedImage traslacion(BufferedImage img, int dx, int dy, String interpolacion) {
        BufferedImage resultado = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                double srcX = x - dx;
                double srcY = y - dy;
                if (srcX >= 0 && srcX < img.getWidth() && srcY >= 0 && srcY < img.getHeight()) {
                    int rgb = getPixel(img, srcX, srcY, interpolacion);
                    resultado.setRGB(x, y, rgb);
                } else {
                    resultado.setRGB(x, y, 0xFF000000);
                }
            }
        }
        return resultado;
    }

    public static BufferedImage rotacion(BufferedImage img, double anguloGrados, String interpolacion) {
        BufferedImage resultado = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        double anguloRad = Math.toRadians(anguloGrados);
        double cos = Math.cos(anguloRad);
        double sin = Math.sin(anguloRad);
        int cx = img.getWidth() / 2;
        int cy = img.getHeight() / 2;

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int dx = x - cx;
                int dy = y - cy;
                double srcX = cos * dx + sin * dy + cx;
                double srcY = -sin * dx + cos * dy + cy;
                if (srcX >= 0 && srcX < img.getWidth() && srcY >= 0 && srcY < img.getHeight()) {
                    int rgb = getPixel(img, srcX, srcY, interpolacion);
                    resultado.setRGB(x, y, rgb);
                } else {
                    resultado.setRGB(x, y, 0xFF000000);
                }
            }
        }
        return resultado;
    }
}
