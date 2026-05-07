package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.Filtros;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class FiltroController {
    @FXML private VBox root;
    @FXML private ImageView imageView;
    @FXML private ComboBox<String> tipoFiltro;
    @FXML private ComboBox<Integer> selectorN;
    @FXML private VBox panelN;

    private Image imagenOriginal;
    private Image imagenFiltrada;
    private Consumer<Image> onImagenProcesada;

    @FXML
    public void initialize() {
        tipoFiltro.getItems().addAll(
                "Media Aritmética", "Media Geométrica", "Media Armónica",
                "Gaussiano", "Mediana", "Alpha-Trimmed Mean"
        );
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );

        selectorN.getItems().addAll(3, 5);
        selectorN.setValue(3);
    }

    public void setOnImagenProcesada(Consumer<Image> callback) {
        this.onImagenProcesada = callback;
    }

    public void setImagen(Image imagen) {
        this.imagenOriginal = imagen;
        imageView.setImage(imagen);
    }

    @FXML
    void onFiltroSeleccionado() {
        panelN.setVisible(false);
        panelN.setManaged(false);

        if (tipoFiltro.getValue() == null) return;

        panelN.setVisible(true);
        panelN.setManaged(true);
    }

    @FXML
    void aplicarFiltro() {
        if (imagenOriginal == null || tipoFiltro.getValue() == null) return;

        BufferedImage buf = SwingFXUtils.fromFXImage(imagenOriginal, null);
        int N = selectorN.getValue() != null ? selectorN.getValue() : 3;

        BufferedImage resultado = switch (tipoFiltro.getValue()) {
            case "Media Aritmética"   -> Filtros.mediaAritmetica(buf, N);
            case "Media Geométrica"   -> Filtros.mediaGeometrica(buf, N);
            case "Media Armónica"     -> Filtros.mediaArmonica(buf, N);
            case "Gaussiano"          -> Filtros.gaussiano(buf, N);
            case "Mediana"            -> Filtros.mediana(buf, N);
            case "Alpha-Trimmed Mean" -> Filtros.alphaTrimmedMean(buf, N);
            default                   -> buf;
        };

        imageView.setImage(SwingFXUtils.toFXImage(resultado, null));
        this.imagenFiltrada = SwingFXUtils.toFXImage(resultado, null);
    }

    public void handleEnviarAlIndice() {
        if (onImagenProcesada != null && imagenFiltrada != null)
            onImagenProcesada.accept(imagenFiltrada);

        ((Stage) imageView.getScene().getWindow()).close();
    }
}