package com.example.pdiproject.Algortitmos;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CambiarIluminacion {
    static public BufferedImage aplicar(Image imagen, int valor){
        if(imagen != null){
            BufferedImage imagenAModificar = SwingFXUtils.fromFXImage(imagen, null);
            int ancho = imagenAModificar.getWidth();
            int alto = imagenAModificar.getHeight();
            for (int i = 0; i < ancho; i++) {
                for (int j = 0; j < alto; j++) {
                    int rgb = imagenAModificar.getRGB(i, j);
                    int a = (rgb >> 24) & 0xff;
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    r += valor;
                    g += valor;
                    b += valor;
                    r = Math.max(0, Math.min(r, 255));
                    g = Math.max(0, Math.min(g, 255));
                    b = Math.max(0, Math.min(b, 255));
                    rgb = (a << 24) | (r << 16) | (g << 8) | b;
                    imagenAModificar.setRGB(i, j, rgb);
                }
            }
            return imagenAModificar;
        }else{
            return null;
        }
    }
}
