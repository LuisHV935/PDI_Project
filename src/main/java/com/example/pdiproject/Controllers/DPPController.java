package com.example.pdiproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class DPPController {
    @FXML private BarChart DPPR;
    @FXML private BarChart DPPG;
    @FXML private BarChart DPPB;
    @FXML private BarChart DPPI;
    @FXML private GridPane root;
    private double[][] matrizDPP;

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
    }

    public void setMatrizDPP(double[][] matrizDPP) {
        this.matrizDPP = matrizDPP;
    }

    public void mostrarDPP() {
        cargarSeries(DPPR, matrizDPP[0]);
        cargarSeries(DPPG, matrizDPP[1]);
        cargarSeries(DPPB, matrizDPP[2]);
        cargarSeries(DPPI, matrizDPP[3]);
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
