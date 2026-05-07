package com.example.pdiproject.Algortitmos;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class ObtenerImagenAEscalaDeGrises {
    public static Image obtener(Image imagen){
        BufferedImage imagenConCambiosDeGrises = SwingFXUtils.fromFXImage(imagen, null);
        int ancho = imagenConCambiosDeGrises.getWidth();
        int alto = imagenConCambiosDeGrises.getHeight();
        for (int x = 0; x < ancho; x++){
            for (int y = 0; y < alto; y++){
                int rgb = imagenConCambiosDeGrises.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (r + g + b) / 3;
                r = g = b = gray;
                rgb = (a << 24) | (r << 16) | (g << 8) | b;
                imagenConCambiosDeGrises.setRGB(x, y, rgb);
            }
        }
        return SwingFXUtils.toFXImage(imagenConCambiosDeGrises, null);
    }
}
