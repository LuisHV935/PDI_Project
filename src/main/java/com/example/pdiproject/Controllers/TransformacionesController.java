package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.Transformaciones;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class TransformacionesController {

    @FXML private ImageView imageView;
    @FXML private ImageView imageViewResultado;
    @FXML private VBox root;
    @FXML private ComboBox<String> tipoOperacion;
    @FXML private VBox panelTraslacion, panelRotacion, panelInterpolacion;
    @FXML private Slider sliderDX, sliderDY, sliderAngulo;
    @FXML private Label labelDX, labelDY, labelAngulo;
    @FXML private ComboBox<String> tipoInterpolacion;

    private Image imagenOriginal;
    private Consumer<Image> onImagenProcesada;
    private Image imagenResultado;

    @FXML
    public void initialize() {
        tipoOperacion.getItems().addAll("Traslacion", "Rotacion");
        tipoInterpolacion.getItems().addAll("Vecino mas cercano", "Bilineal");
        tipoInterpolacion.setValue("Vecino mas cercano");
        sliderDX.valueProperty().addListener((obs, old, val) ->
                labelDX.setText(String.valueOf(val.intValue())));
        sliderDY.valueProperty().addListener((obs, old, val) ->
                labelDY.setText(String.valueOf(val.intValue())));
        sliderAngulo.valueProperty().addListener((obs, old, val) ->
                labelAngulo.setText(String.format("%.1f°", val.doubleValue())));
    }

    public void setImagen(Image imagen) {
        this.imagenOriginal = imagen;
        imageView.setImage(imagen);
    }

    public void setOnImagenProcesada(Consumer<Image> callback) {
        this.onImagenProcesada = callback;
    }

    @FXML
    void onOperacionSeleccionada() {
        String seleccion = tipoOperacion.getValue();
        panelTraslacion.setVisible(false); panelTraslacion.setManaged(false);
        panelRotacion.setVisible(false); panelRotacion.setManaged(false);
        panelInterpolacion.setVisible(true); panelInterpolacion.setManaged(true);

        switch (seleccion) {
            case "Traslacion" -> {
                panelTraslacion.setVisible(true); panelTraslacion.setManaged(true);
            }
            case "Rotacion" -> {
                panelRotacion.setVisible(true); panelRotacion.setManaged(true);
            }
        }
    }

    @FXML
    void aplicarTransformacion() {
        if (imagenOriginal == null || tipoOperacion.getValue() == null) return;

        BufferedImage buf = SwingFXUtils.fromFXImage(imagenOriginal, null);
        String interpolacion = tipoInterpolacion.getValue();
        BufferedImage resultado;

        switch (tipoOperacion.getValue()) {
            case "Traslacion" -> {
                int dx = (int) sliderDX.getValue();
                int dy = (int) sliderDY.getValue();
                resultado = Transformaciones.traslacion(buf, dx, dy, interpolacion);
            }
            case "Rotacion" -> {
                double angulo = sliderAngulo.getValue();
                resultado = Transformaciones.rotacion(buf, angulo, interpolacion);
            }
            default -> resultado = buf;
        }

        this.imagenResultado = SwingFXUtils.toFXImage(resultado, null);
        imageViewResultado.setImage(this.imagenResultado);
    }

    @FXML
    void handleEnviarAlIndice() {
        if (onImagenProcesada != null && imagenResultado != null)
            onImagenProcesada.accept(imagenResultado);
        ((Stage) imageView.getScene().getWindow()).close();
    }
}
