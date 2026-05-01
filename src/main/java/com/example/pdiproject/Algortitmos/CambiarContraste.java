package com.example.pdiproject.Algortitmos;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CambiarContraste {
    public static BufferedImage aplicar(Image imagen, double valor){
        try{
            BufferedImage imagenAModificar = SwingFXUtils.fromFXImage(imagen, null);
            int ancho = imagenAModificar.getWidth();
            int alto = imagenAModificar.getHeight();
            for (int x = 0; x < ancho; x++) {
                for (int y = 0; y < alto; y++) {
                    int pixel = imagenAModificar.getRGB(x, y);
                    int a = (pixel >> 24) & 0xff;
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    r = (int)Math.floor(r * valor);
                    g = (int)Math.floor(g * valor)  ;
                    b = (int)Math.floor(b * valor);
                    r = Math.max(0, Math.min(r, 255));
                    g = Math.max(0, Math.min(g, 255));
                    b = Math.max(0, Math.min(b, 255));
                    pixel = (a << 24) | (r << 16) | (g << 8) | b;
                    imagenAModificar.setRGB(x, y, pixel);
                }
            }
            return imagenAModificar;
        }catch(Exception e){
            e.printStackTrace();
            return  null;
        }
    }
}
