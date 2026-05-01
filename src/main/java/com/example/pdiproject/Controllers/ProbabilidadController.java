package com.example.pdiproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class ProbabilidadController {
    @FXML private BarChart probabilidadR;
    @FXML private BarChart probabilidadG;
    @FXML private BarChart probabilidadB;
    @FXML private BarChart probabilidadI;
    @FXML private GridPane root;
    private double[][] matrizDeProbabilidades;

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
    }

    public void setMatrizDeProbabilidades(double[][] matrizDeProbabilidades) {
        this.matrizDeProbabilidades = matrizDeProbabilidades;
    }

    public void mostrarProb() {
        cargarSeries(probabilidadR, matrizDeProbabilidades[0]);
        cargarSeries(probabilidadG, matrizDeProbabilidades[1]);
        cargarSeries(probabilidadB, matrizDeProbabilidades[2]);
        cargarSeries(probabilidadI, matrizDeProbabilidades[3]);
    }

    private void cargarSeries(BarChart<String, Number> chart, double[] datos) {
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < 256; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), datos[i]));
        }
        chart.getData().add(series);
    }

}
