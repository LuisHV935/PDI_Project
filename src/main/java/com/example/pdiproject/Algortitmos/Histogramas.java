package com.example.pdiproject.Algortitmos;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.sql.SQLOutput;

public class Histogramas {
    public static int[][] obtenerHistogramasRGB(Image img){
        BufferedImage bimg = SwingFXUtils.fromFXImage(img, null);
        int[][] hRgb = new int[3][256];
        int x = bimg.getWidth();
        int y = bimg.getHeight();
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                int rgb = bimg.getRGB(i, j);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                ++hRgb[0][r];
                ++hRgb[1][g];
                ++hRgb[2][b];
            }
        }
        return hRgb;
    }

    public static int[] histGE(Image img){
        BufferedImage bimg = SwingFXUtils.fromFXImage(img, null);
        int[] hist = new int[256];
        int ancho = bimg.getWidth();
        int alto = bimg.getHeight();
        for (int x = 0; x < ancho; x++){
            for (int y = 0; y < alto; y++){
                int rgb = bimg.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (r + g + b) / 3;
                hist[gray]++;
            }
        }
        return hist;
    }
}
