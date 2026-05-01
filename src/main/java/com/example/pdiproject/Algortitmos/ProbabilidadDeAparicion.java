package com.example.pdiproject.Algortitmos;

import javafx.scene.image.Image;

public class ProbabilidadDeAparicion {
    public static double[] calcularProb(int numPixeles, int[] canal){
        double[] probabilidades = new double[256];
        for(int i=0; i<256;i++){
            probabilidades[i] = (double)canal[i]/(double)numPixeles;
            IO.println(probabilidades[i]);
        }
        return probabilidades;
    }
    
}
