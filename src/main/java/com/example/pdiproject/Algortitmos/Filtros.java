package com.example.pdiproject.Algortitmos;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Filtros {
    static int getGray(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y) >> 16) & 0xFF;
    }

    static int[][] mascaraGaussiana(int N){
        int[][] mascara;
        if (N == 3) {
            mascara = new int[][]{
                    {1, 2, 1},
                    {2, 4, 2},
                    {1, 2, 1}
            };
        } else {
            mascara = new int[][]{
                    { 1,  4,  7,  4,  1},
                    { 4, 16, 26, 16,  4},
                    { 7, 26, 41, 26,  7},
                    { 4, 16, 26, 16,  4},
                    { 1,  4,  7,  4,  1}
            };
        }
        return mascara;
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

    public static BufferedImage mediaAritmetica(BufferedImage img, int N) {
        BufferedImage filt = copy(img);
        int half = N / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = half; y < height - half; y++) {
            for (int x = half; x < width - half; x++) {
                int sum = 0;
                for(int i = -half; i <= half; i++) {
                    for(int j = -half; j <= half; j++) {
                        sum += getGray(filt, x + j, y + i);
                    }
                }
                setGray(filt, x, y, sum/(N*N));
            }
        }
        return filt;
    }

    public static BufferedImage mediaGeometrica(BufferedImage img, int N) {
        BufferedImage filt = copy(img);
        int half = N / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = half; y < height - half; y++) {
            for (int x = half; x < width - half; x++) {
                double acum = 1.0;
                for(int i = -half; i <= half; i++) {
                    for(int j = -half; j <= half; j++) {
                        if (getGray(img, x + j, y + i) != 0) acum *= getGray(img, x + j, y + i);
                    }
                }
                setGray(filt, x, y, (int)Math.round(Math.pow((double)acum,1.0/(N*N))));
            }
        }
        return filt;
    }

    public static BufferedImage mediaArmonica(BufferedImage img, int N) {
        BufferedImage filt = copy(img);
        int half = N / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = half; y < height - half; y++) {
            for (int x = half; x < width - half; x++) {
                double acum = 0;
                for(int i = -half; i <= half; i++) {
                    for(int j = -half; j <= half; j++) {
                        if(getGray(img, x + j, y + i) != 0) acum += 1.0/(getGray(img, x + j, y + i));
                    }
                }
                setGray(filt, x, y, (int)Math.floor((N*N)/acum));
            }
        }
        return filt;
    }

    public static BufferedImage gaussiano(BufferedImage img, int N) {
        int sumPesos = (N==3) ? 16 : 273;
        int[][] matrizPesos = mascaraGaussiana(N);
        BufferedImage filt = copy(img);
        int half = N / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = half; y < height - half; y++) {
            for (int x = half; x < width - half; x++) {
                int acum = 0;
                for(int i = -half; i <= half; i++) {
                    for(int j = -half; j <= half; j++) {
                        acum += matrizPesos[i + half][j + half]*(getGray(img, x + j, y + i));
                    }
                }
                setGray(filt, x, y, acum/sumPesos);
            }
        }
        return filt;
    }

    public static BufferedImage mediana(BufferedImage img, int N) {
        BufferedImage filt = copy(img);
        int half = N / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = half; y < height - half; y++) {
            for (int x = half; x < width - half; x++) {
                int cont = 0;
                int[] arr = new  int[N*N];
                for(int i = -half; i <= half; i++) {
                    for(int j = -half; j <= half; j++) {
                        arr[cont] = getGray(img, x + j, y + i);
                        cont++;
                    }
                }
                arr = Arrays.stream(arr).sorted().toArray();
                setGray(filt, x, y, arr[(N*N)/2]);
            }
        }
        return filt;
    }

    public static BufferedImage alphaTrimmedMean(BufferedImage img, int N) {
        BufferedImage filt = copy(img);
        int P = (N==3) ? 2 : 4;
        int half = N / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = half; y < height - half; y++) {
            for (int x = half; x < width - half; x++) {
                int cont = 0, sum = 0;
                int[] arr = new int[N*N];
                for(int i = -half; i <= half; i++) {
                    for(int j = -half; j <= half; j++) {
                        arr[cont] = getGray(img, x + j, y + i);
                        cont++;
                    }
                }
                Arrays.sort(arr);
                for(int w = P; w < (N*N) - P; w++){
                    sum += arr[w];
                }
                setGray(filt, x, y, sum/(N*N));
            }
        }
        return filt;
    }
}