package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.Binarizado;
import com.example.pdiproject.Algortitmos.ConversionesColor;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public class BinarizadoController {

    @FXML private VBox root;
    @FXML private ImageView imgOriginal;
    @FXML private ImageView imgResultado;
    @FXML private ImageView imgCanalYOriginal;
    @FXML private ImageView imgCanalYBinarizado;
    @FXML private ComboBox<String> selectorBinarizacion;
    @FXML private Label labelResultado;
    @FXML private Label labelDescripcion;

    private Image imagenOriginal;

    private static final String DESC_1 = "1 umbral (T=128): cada píxel cuya luminancia Y supera 128/255 se convierte en blanco (1.0); el resto en negro (0.0). Resultado binario clásico.";
    private static final String DESC_2 = "2 umbrales (T₁=85, T₂=171): produce tres niveles — negro, gris medio (128/255) y blanco. Útil para separar fondos, medios tonos y luces.";
    private static final String DESC_3 = "3 umbrales (T₁=64, T₂=128, T₃=192): cuatro niveles de gris (0, 85/255, 170/255, 1). Mayor graduación tonal conservando estructura de la imagen.";
    private static final String DESC_4 = "4 umbrales (T₁=51, T₂=102, T₃=153, T₄=204): cinco niveles cuantizados. Aproximación discreta del espacio tonal completo con mayor fidelidad perceptual.";
    private static final String DESC_INV = "Inversión: cada valor Y se sustituye por (1 - Y). Los blancos se vuelven negros y viceversa. Se aplica sobre la imagen original sin umbralización previa.";

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
        selectorBinarizacion.getItems().addAll(
                "1 umbral (T=128)",
                "2 umbrales",
                "3 umbrales",
                "4 umbrales",
                "Inversión"
        );
        selectorBinarizacion.setOnAction(e -> actualizarDescripcion());
    }

    public void setImagen(Image imagen) {
        this.imagenOriginal = imagen;
        imgOriginal.setImage(imagen);

        // Mostrar canal Y de la imagen original
        BufferedImage src = SwingFXUtils.fromFXImage(imagen, null);
        double[][] yiq = ConversionesColor.rgbAYiq(src);
        double[] Y = yiq[0];
        imgCanalYOriginal.setImage(SwingFXUtils.toFXImage(
                ConversionesColor.canalAGris(Y, src.getWidth(), src.getHeight()), null));
    }

    @FXML
    public void aplicarBinarizacion() {
        if (imagenOriginal == null || selectorBinarizacion.getValue() == null) return;

        BufferedImage src = SwingFXUtils.fromFXImage(imagenOriginal, null);
        String metodo = selectorBinarizacion.getValue();
        BufferedImage resultado;

        switch (metodo) {
            case "1 umbral (T=128)" -> {
                resultado = Binarizado.binarizar1Umbral(src);
                labelResultado.setText("Resultado: 1 umbral (T=128)");
            }
            case "2 umbrales" -> {
                resultado = Binarizado.binarizar2Umbrales(src);
                labelResultado.setText("Resultado: 2 umbrales (T₁=85, T₂=171)");
            }
            case "3 umbrales" -> {
                resultado = Binarizado.binarizar3Umbrales(src);
                labelResultado.setText("Resultado: 3 umbrales (T₁=64, T₂=128, T₃=192)");
            }
            case "4 umbrales" -> {
                resultado = Binarizado.binarizar4Umbrales(src);
                labelResultado.setText("Resultado: 4 umbrales (T₁=51, T₂=102, T₃=153, T₄=204)");
            }
            case "Inversión" -> {
                resultado = Binarizado.invertirBinarizacion(src);
                labelResultado.setText("Resultado: Inversión (1 - Y)");
            }
            default -> { return; }
        }

        imgResultado.setImage(SwingFXUtils.toFXImage(resultado, null));

        double[][] yiqRes = ConversionesColor.rgbAYiq(resultado);
        double[] Yres = yiqRes[0];
        imgCanalYBinarizado.setImage(SwingFXUtils.toFXImage(
                ConversionesColor.canalAGris(Yres, resultado.getWidth(), resultado.getHeight()), null));
    }

    private void actualizarDescripcion() {
        String metodo = selectorBinarizacion.getValue();
        if (metodo == null) return;
        String desc = switch (metodo) {
            case "1 umbral (T=128)"  -> DESC_1;
            case "2 umbrales"        -> DESC_2;
            case "3 umbrales"        -> DESC_3;
            case "4 umbrales"        -> DESC_4;
            case "Inversión"         -> DESC_INV;
            default                  -> "";
        };
        labelDescripcion.setText(desc);
    }
}