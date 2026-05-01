package com.example.pdiproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class HistogramasController {

    private int[][] histogramaRGB;
    private int[] histogramaGE;

    @FXML private BarChart<String, Number> histograma1;
    @FXML private BarChart<String, Number> histograma2;
    @FXML private BarChart<String, Number> histograma3;
    @FXML private BarChart<String, Number> histograma4;
    @FXML private GridPane root;

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
    }

    public void setHistogramaRGB(int[][] histogramaRGB) {
        this.histogramaRGB = histogramaRGB;
    }

    public void setHistogramaGE(int[] histogramaGE) {
        this.histogramaGE = histogramaGE;
    }

    public void mostrarHist() {
        cargarSeries(histograma1, histogramaRGB[0]);
        cargarSeries(histograma2, histogramaRGB[1]);
        cargarSeries(histograma3, histogramaRGB[2]);
        cargarSeries(histograma4, histogramaGE);
    }

    private void cargarSeries(BarChart<String, Number> chart, int[] datos) {
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < 256; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), datos[i]));
        }
        chart.getData().add(series);
    }
}