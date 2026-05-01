package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.Ruido;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class RuidoController {

    @FXML private ImageView imageView;
    @FXML private VBox root;
    @FXML private ComboBox<String> tipoRuido;
    @FXML private VBox panelVarianza, panelAlpha, panelProbabilidad;
    @FXML private Slider sliderVarianza, sliderAlpha, sliderProbabilidad;
    @FXML private Label labelVarianza, labelProbabilidad;

    private Image imagenOriginal;
    private Consumer<Image> onImagenProcesada;
    private Image imagenConRuido;

    @FXML
    public void initialize() {
        tipoRuido.getItems().addAll(
                "Gaussiano", "Salt & Pepper", "Uniforme",
                "Rayleigh", "Exponencial Negativo", "Gamma"
        );
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
        sliderVarianza.valueProperty().addListener((obs, old, val) ->
                labelVarianza.setText(String.valueOf(val.intValue())));

        sliderProbabilidad.valueProperty().addListener((obs, old, val) ->
                labelProbabilidad.setText(String.format("%.0f%%", val.doubleValue() * 100)));
    }

    public void setImagen(Image imagen) {
        this.imagenOriginal = imagen;
        imageView.setImage(imagen);
    }

    public void setOnImagenProcesada(Consumer<Image> callback) {
        this.onImagenProcesada = callback;
    }

    @FXML
    void onRuidoSeleccionado() {
        String seleccion = tipoRuido.getValue();

        panelVarianza.setVisible(false);     panelVarianza.setManaged(false);
        panelAlpha.setVisible(false);        panelAlpha.setManaged(false);
        panelProbabilidad.setVisible(false); panelProbabilidad.setManaged(false);

        switch (seleccion) {
            case "Salt & Pepper" -> {
                panelProbabilidad.setVisible(true);
                panelProbabilidad.setManaged(true);
            }
            case "Gamma" -> {
                panelVarianza.setVisible(true); panelVarianza.setManaged(true);
                panelAlpha.setVisible(true);    panelAlpha.setManaged(true);
            }
            default -> {
                panelVarianza.setVisible(true);
                panelVarianza.setManaged(true);
            }
        }
    }

    @FXML
    void aplicarRuido() {
        if (imagenOriginal == null || tipoRuido.getValue() == null) return;

        BufferedImage buf = SwingFXUtils.fromFXImage(imagenOriginal, null);
        double varianza   = sliderVarianza.getValue();
        double prob       = sliderProbabilidad.getValue();
        int alpha         = (int) sliderAlpha.getValue();

        BufferedImage resultado = switch (tipoRuido.getValue()) {
            case "Gaussiano"            -> Ruido.gaussiano(buf, Math.sqrt(varianza));
            case "Salt & Pepper"        -> Ruido.saltAndPepper(buf, prob);
            case "Uniforme"             -> Ruido.uniforme(buf, varianza);
            case "Rayleigh"             -> Ruido.rayleigh(buf, varianza);
            case "Exponencial Negativo" -> Ruido.exponencialNegativo(buf, varianza);
            case "Gamma"                -> Ruido.gamma(buf, varianza, alpha);
            default                     -> buf;
        };
        this.imagenConRuido = SwingFXUtils.toFXImage(resultado, null);
        imageView.setImage(SwingFXUtils.toFXImage(resultado, null));
    }

    public void handleEnviarAlIndice() {
        if (onImagenProcesada != null && imagenConRuido != null)
            onImagenProcesada.accept(imagenConRuido);

        ((Stage) imageView.getScene().getWindow()).close();
    }
}