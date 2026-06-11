package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.OperacionesLogicas;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class OperacionesLogicasController {

    @FXML private ImageView imageView;
    @FXML private ImageView imageViewResultado;
    @FXML private VBox root;
    @FXML private ComboBox<String> tipoOperacion;
    @FXML private VBox panelImagen2, panelEscalar;
    @FXML private Slider sliderEscalar;
    @FXML private Label labelEscalar;
    @FXML private ImageView imageView2;
    @FXML private Button cargarImagen2;

    private Image imagenOriginal;
    private Image imagen2;
    private Consumer<Image> onImagenProcesada;
    private Image imagenResultado;

    @FXML
    public void initialize() {
        tipoOperacion.getItems().addAll("AND", "OR", "NOT", "XOR");
        sliderEscalar.valueProperty().addListener((obs, old, val) ->
                labelEscalar.setText(String.format("0x%08X", val.intValue())));
    }

    public void setImagen(Image imagen) {
        this.imagenOriginal = imagen;
        imageView.setImage(imagen);
    }

    public void setOnImagenProcesada(Consumer<Image> callback) {
        this.onImagenProcesada = callback;
    }

    @FXML
    void handleCargarImagen2() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona la segunda imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagenes", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File archivo = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (archivo != null) {
            imagen2 = new Image(archivo.toURI().toString());
            imageView2.setImage(imagen2);
        }
    }

    @FXML
    void onOperacionSeleccionada() {
        String seleccion = tipoOperacion.getValue();
        panelImagen2.setVisible(false); panelImagen2.setManaged(false);
        panelEscalar.setVisible(false); panelEscalar.setManaged(false);

        switch (seleccion) {
            case "NOT" -> {}
            case "AND", "OR", "XOR" -> {
                panelImagen2.setVisible(true); panelImagen2.setManaged(true);
            }
        }
    }

    @FXML
    void aplicarOperacion() {
        if (imagenOriginal == null || tipoOperacion.getValue() == null) return;

        BufferedImage buf1 = SwingFXUtils.fromFXImage(imagenOriginal, null);
        BufferedImage resultado;

        switch (tipoOperacion.getValue()) {
            case "AND" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesLogicas.and(buf1, buf2);
                } else {
                    resultado = OperacionesLogicas.andEscalar(buf1, (int) sliderEscalar.getValue());
                }
            }
            case "OR" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesLogicas.or(buf1, buf2);
                } else {
                    resultado = OperacionesLogicas.orEscalar(buf1, (int) sliderEscalar.getValue());
                }
            }
            case "NOT" -> resultado = OperacionesLogicas.not(buf1);
            case "XOR" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesLogicas.xor(buf1, buf2);
                } else {
                    resultado = OperacionesLogicas.xorEscalar(buf1, (int) sliderEscalar.getValue());
                }
            }
            default -> resultado = buf1;
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
