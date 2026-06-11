package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.OperacionesRelacionales;
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

public class OperacionesRelacionalesController {

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
        tipoOperacion.getItems().addAll("Mayor que (>)", "Menor que (<)", "Igual que (==)",
                "Diferente de (!=)", "Mayor o igual (>=)", "Menor o igual (<=)");
        sliderEscalar.valueProperty().addListener((obs, old, val) ->
                labelEscalar.setText(String.valueOf(val.intValue())));
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
        panelImagen2.setVisible(true); panelImagen2.setManaged(true);
        panelEscalar.setVisible(false); panelEscalar.setManaged(false);
    }

    @FXML
    void aplicarOperacion() {
        if (imagenOriginal == null || tipoOperacion.getValue() == null) return;

        BufferedImage buf1 = SwingFXUtils.fromFXImage(imagenOriginal, null);
        BufferedImage resultado;

        if (imagen2 != null) {
            BufferedImage buf2 = SwingFXUtils.fromFXImage(imagen2, null);
            resultado = switch (tipoOperacion.getValue()) {
                case "Mayor que (>)" -> OperacionesRelacionales.mayorQue(buf1, buf2);
                case "Menor que (<)" -> OperacionesRelacionales.menorQue(buf1, buf2);
                case "Igual que (==)" -> OperacionesRelacionales.igualQue(buf1, buf2);
                case "Diferente de (!=)" -> OperacionesRelacionales.diferenteDe(buf1, buf2);
                case "Mayor o igual (>=)" -> OperacionesRelacionales.mayorOIgual(buf1, buf2);
                case "Menor o igual (<=)" -> OperacionesRelacionales.menorOIgual(buf1, buf2);
                default -> buf1;
            };
        } else {
            int escalar = (int) sliderEscalar.getValue();
            resultado = switch (tipoOperacion.getValue()) {
                case "Mayor que (>)" -> OperacionesRelacionales.mayorQueEscalar(buf1, escalar);
                case "Menor que (<)" -> OperacionesRelacionales.menorQueEscalar(buf1, escalar);
                case "Igual que (==)" -> OperacionesRelacionales.igualQueEscalar(buf1, escalar);
                case "Diferente de (!=)" -> OperacionesRelacionales.diferenteDeEscalar(buf1, escalar);
                case "Mayor o igual (>=)" -> OperacionesRelacionales.mayorOIgualEscalar(buf1, escalar);
                case "Menor o igual (<=)" -> OperacionesRelacionales.menorOIgualEscalar(buf1, escalar);
                default -> buf1;
            };
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
