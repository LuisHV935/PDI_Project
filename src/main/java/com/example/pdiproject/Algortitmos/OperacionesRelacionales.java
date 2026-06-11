package com.example.pdiproject.Algortitmos;

import java.awt.image.BufferedImage;

public class OperacionesRelacionales {

    public static BufferedImage mayorQue(BufferedImage img1, BufferedImage img2) {
        return aplicarOperador(img1, img2, (v1, v2) -> v1 > v2 ? 255 : 0);
    }

    public static BufferedImage menorQue(BufferedImage img1, BufferedImage img2) {
        return aplicarOperador(img1, img2, (v1, v2) -> v1 < v2 ? 255 : 0);
    }

    public static BufferedImage igualQue(BufferedImage img1, BufferedImage img2) {
        return aplicarOperador(img1, img2, (v1, v2) -> v1 == v2 ? 255 : 0);
    }

    public static BufferedImage diferenteDe(BufferedImage img1, BufferedImage img2) {
        return aplicarOperador(img1, img2, (v1, v2) -> v1 != v2 ? 255 : 0);
    }

    public static BufferedImage mayorOIgual(BufferedImage img1, BufferedImage img2) {
        return aplicarOperador(img1, img2, (v1, v2) -> v1 >= v2 ? 255 : 0);
    }

    public static BufferedImage menorOIgual(BufferedImage img1, BufferedImage img2) {
        return aplicarOperador(img1, img2, (v1, v2) -> v1 <= v2 ? 255 : 0);
    }

    public static BufferedImage mayorQueEscalar(BufferedImage img, int valor) {
        return aplicarOperadorEscalar(img, valor, (v, val) -> v > val ? 255 : 0);
    }

    public static BufferedImage menorQueEscalar(BufferedImage img, int valor) {
        return aplicarOperadorEscalar(img, valor, (v, val) -> v < val ? 255 : 0);
    }

    public static BufferedImage igualQueEscalar(BufferedImage img, int valor) {
        return aplicarOperadorEscalar(img, valor, (v, val) -> v == val ? 255 : 0);
    }

    public static BufferedImage diferenteDeEscalar(BufferedImage img, int valor) {
        return aplicarOperadorEscalar(img, valor, (v, val) -> v != val ? 255 : 0);
    }

    public static BufferedImage mayorOIgualEscalar(BufferedImage img, int valor) {
        return aplicarOperadorEscalar(img, valor, (v, val) -> v >= val ? 255 : 0);
    }

    public static BufferedImage menorOIgualEscalar(BufferedImage img, int valor) {
        return aplicarOperadorEscalar(img, valor, (v, val) -> v <= val ? 255 : 0);
    }

    @FunctionalInterface
    interface OperadorBinario {
        int aplicar(int v1, int v2);
    }

    private static BufferedImage aplicarOperador(BufferedImage img1, BufferedImage img2, OperadorBinario op) {
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r1 = (p1 >> 16) & 0xFF, g1 = (p1 >> 8) & 0xFF, b1 = p1 & 0xFF;
                int r2 = (p2 >> 16) & 0xFF, g2 = (p2 >> 8) & 0xFF, b2 = p2 & 0xFF;
                int r = op.aplicar(r1, r2);
                int g = op.aplicar(g1, g2);
                int b = op.aplicar(b1, b2);
                int a = Math.max((p1 >> 24) & 0xFF, (p2 >> 24) & 0xFF);
                resultado.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return resultado;
    }

    private static BufferedImage aplicarOperadorEscalar(BufferedImage img, int valor, OperadorBinario op) {
        BufferedImage resultado = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x, y);
                int r = (p >> 16) & 0xFF, g = (p >> 8) & 0xFF, b = p & 0xFF;
                int rn = op.aplicar(r, valor);
                int gn = op.aplicar(g, valor);
                int bn = op.aplicar(b, valor);
                int a = (p >> 24) & 0xFF;
                resultado.setRGB(x, y, (a << 24) | (rn << 16) | (gn << 8) | bn);
            }
        }
        return resultado;
    }
}
