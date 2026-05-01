package com.example.pdiproject.Algortitmos;

public class DensidadDePotencia {
    public static double[][] calcularDPP(double[][] matrizDeProbabilidades){
        double[][] matrizDPP = new double[4][256];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 256; j++){
                matrizDPP[i][j] = matrizDeProbabilidades[i][j] *  matrizDeProbabilidades[i][j];
            }
        }
        return matrizDPP;
    }
}
