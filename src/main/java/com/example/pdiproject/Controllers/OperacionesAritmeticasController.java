package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.OperacionesAritmeticas;
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

public class OperacionesAritmeticasController {

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
        tipoOperacion.getItems().addAll("Suma", "Resta", "Multiplicacion", "Division");
        sliderEscalar.valueProperty().addListener((obs, old, val) ->
                labelEscalar.setText(String.format("%.2f", val.doubleValue())));
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
        panelImagen2.setVisible(seleccion != null);
        panelImagen2.setManaged(seleccion != null);
        panelEscalar.setVisible(false);
        panelEscalar.setManaged(false);
    }

    @FXML
    void aplicarOperacion() {
        if (imagenOriginal == null || tipoOperacion.getValue() == null) return;

        BufferedImage buf1 = SwingFXUtils.fromFXImage(imagenOriginal, null);
        BufferedImage resultado;

        switch (tipoOperacion.getValue()) {
            case "Suma" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesAritmeticas.sumar(buf1, buf2);
                } else {
                    resultado = OperacionesAritmeticas.sumarEscalar(buf1, (int) sliderEscalar.getValue());
                }
            }
            case "Resta" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesAritmeticas.restar(buf1, buf2);
                } else {
                    resultado = OperacionesAritmeticas.restarEscalar(buf1, (int) sliderEscalar.getValue());
                }
            }
            case "Multiplicacion" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesAritmeticas.multiplicar(buf1, buf2);
                } else {
                    resultado = OperacionesAritmeticas.multiplicarEscalar(buf1, sliderEscalar.getValue());
                }
            }
            case "Division" -> {
                if (imagen2 != null) {
                    BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
                    resultado = OperacionesAritmeticas.dividir(buf1, buf2);
                } else {
                    resultado = OperacionesAritmeticas.dividirEscalar(buf1, sliderEscalar.getValue());
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
