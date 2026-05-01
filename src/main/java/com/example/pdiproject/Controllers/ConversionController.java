package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.ConversionesColor;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public class ConversionController {

    @FXML private VBox root;
    @FXML private ImageView imgOriginal;
    @FXML private ComboBox<String> selectorModelo;

    // Canales en gris
    @FXML private ImageView grisC1;
    @FXML private ImageView grisC2;
    @FXML private ImageView grisC3;
    @FXML private ImageView grisC4; // Solo CMYK

    // Canales en color
    @FXML private ImageView colorC1;
    @FXML private ImageView colorC2;
    @FXML private ImageView colorC3;
    @FXML private ImageView colorC4; // Solo CMYK

    // Labels de canales
    @FXML private Label labelC1;
    @FXML private Label labelC2;
    @FXML private Label labelC3;
    @FXML private Label labelC4;

    // Imagen reconstruida (resultado de la inversa)
    @FXML private ImageView imgReconstruida;
    @FXML private Label labelReconstruida;

    // Panel del cuarto canal (solo CMYK)
    @FXML private VBox panelC4;

    private Image imagenOriginal;

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
        selectorModelo.getItems().addAll("CMY", "CMYK", "YIQ", "HSI", "HSV", "lαβ (Reinhard)");
    }

    public void setImagen(Image imagen) {
        this.imagenOriginal = imagen;
        imgOriginal.setImage(imagen);
    }

    @FXML
    public void aplicarConversion() {
        if (imagenOriginal == null || selectorModelo.getValue() == null) return;

        BufferedImage src = SwingFXUtils.fromFXImage(imagenOriginal, null);
        String modelo = selectorModelo.getValue();

        panelC4.setVisible(false);
        panelC4.setManaged(false);

        switch (modelo) {
            case "CMY"            -> procesarCmy(src);
            case "CMYK"           -> procesarCmyk(src);
            case "YIQ"            -> procesarYiq(src);
            case "HSI"            -> procesarHsi(src);
            case "HSV"            -> procesarHsv(src);
            case "lαβ (Reinhard)" -> procesarLab(src);
        }
    }

    // -------------------------------------------------------------------------

    private void procesarCmy(BufferedImage src) {
        colorC4.setImage(null);
        int w = src.getWidth(), h = src.getHeight();
        double[][] cmy = ConversionesColor.rgbACmy(src);
        double[] C = cmy[0], M = cmy[1], Y = cmy[2];

        // Canales en gris
        grisC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(C, w, h), null));
        grisC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(M, w, h), null));
        grisC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(Y, w, h), null));

        // Canales en color
        colorC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmyEnColor(C, w, h, 0), null));
        colorC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmyEnColor(M, w, h, 1), null));
        colorC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmyEnColor(Y, w, h, 2), null));

        // Reconstrucción inversa
        imgReconstruida.setImage(SwingFXUtils.toFXImage(ConversionesColor.cmyARgb(C, M, Y, w, h), null));

        labelC1.setText("C (Cian)");
        labelC2.setText("M (Magenta)");
        labelC3.setText("Y (Amarillo)");
        labelReconstruida.setText("Reconstruida: CMY → RGB");
    }

    private void procesarCmyk(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        double[][] cmy  = ConversionesColor.rgbACmy(src);
        double[][] cmyk = ConversionesColor.cmyACmyk(cmy[0], cmy[1], cmy[2]);
        double[] Cp = cmyk[0], Mp = cmyk[1], Yp = cmyk[2], K = cmyk[3];

        grisC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(Cp, w, h), null));
        grisC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(Mp, w, h), null));
        grisC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(Yp, w, h), null));
        grisC4.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(K,  w, h), null));

        colorC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmykEnColor(Cp, w, h, 0), null));
        colorC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmykEnColor(Mp, w, h, 1), null));
        colorC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmykEnColor(Yp, w, h, 2), null));
        colorC4.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalCmykEnColor(K,  w, h, 3), null));

        double[][] cmyRec = ConversionesColor.cmykACmy(Cp, Mp, Yp, K);
        imgReconstruida.setImage(SwingFXUtils.toFXImage(ConversionesColor.cmyARgb(cmyRec[0], cmyRec[1], cmyRec[2], w, h), null));

        labelC1.setText("C' (Cian)");
        labelC2.setText("M' (Magenta)");
        labelC3.setText("Y' (Amarillo)");
        labelC4.setText("K (Negro)");
        labelReconstruida.setText("Reconstruida: CMYK → CMY → RGB");

        panelC4.setVisible(true);
        panelC4.setManaged(true);
    }

    private void procesarYiq(BufferedImage src) {
        this.colorC4.setImage(null);
        int w = src.getWidth(), h = src.getHeight();
        double[][] yiq = ConversionesColor.rgbAYiq(src);
        double[] Y = yiq[0], I = yiq[1], Q = yiq[2];

        grisC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(Y, w, h), null));
        grisC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(I, w, h), null));
        grisC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(Q, w, h), null));

        colorC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(Y, w, h), null));
        colorC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(I, w, h), null));
        colorC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(Q, w, h), null));

        imgReconstruida.setImage(SwingFXUtils.toFXImage(ConversionesColor.yiqARgb(Y, I, Q, w, h), null));

        labelC1.setText("Y (Luminancia)");
        labelC2.setText("I (Naranja-Cian)");
        labelC3.setText("Q (Verde-Magenta)");
        labelReconstruida.setText("Reconstruida: YIQ → RGB");
    }

    private void procesarHsi(BufferedImage src) {
        this.colorC4.setImage(null);
        int w = src.getWidth(), h = src.getHeight();
        double[][] hsi = ConversionesColor.rgbAHsi(src);
        double[] H = hsi[0], S = hsi[1], I = hsi[2];

        grisC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(H, w, h), null));
        grisC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(S, w, h), null));
        grisC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(I, w, h), null));

        colorC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(H, w, h), null));
        colorC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(S, w, h), null));
        colorC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(I, w, h), null));

        imgReconstruida.setImage(SwingFXUtils.toFXImage(ConversionesColor.hsiARgb(H, S, I, w, h), null));

        labelC1.setText("H (Matiz)");
        labelC2.setText("S (Saturación)");
        labelC3.setText("I (Intensidad)");
        labelReconstruida.setText("Reconstruida: HSI → RGB");
    }

    private void procesarHsv(BufferedImage src) {
        this.colorC4.setImage(null);
        int w = src.getWidth(), h = src.getHeight();
        double[][] hsv = ConversionesColor.rgbAHsv(src);
        double[] H = hsv[0], S = hsv[1], V = hsv[2];

        grisC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(H, w, h), null));
        grisC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(S, w, h), null));
        grisC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(V, w, h), null));

        colorC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(H, w, h), null));
        colorC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(S, w, h), null));
        colorC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGris(V, w, h), null));

        imgReconstruida.setImage(SwingFXUtils.toFXImage(ConversionesColor.hsvARgb(H, S, V, w, h), null));

        labelC1.setText("H (Matiz)");
        labelC2.setText("S (Saturación)");
        labelC3.setText("V (Value/Brillo)");
        labelReconstruida.setText("Reconstruida: HSV → RGB");
    }

    private void procesarLab(BufferedImage src) {
        this.colorC4.setImage(null);

        int w = src.getWidth(), h = src.getHeight();
        double[][] lab = ConversionesColor.rgbALab(src);
        double[] l = lab[0], alpha = lab[1], beta = lab[2];

        grisC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(l,     w, h), null));
        grisC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(alpha, w, h), null));
        grisC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(beta,  w, h), null));

        colorC1.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(l,     w, h), null));
        colorC2.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(alpha, w, h), null));
        colorC3.setImage(SwingFXUtils.toFXImage(ConversionesColor.canalAGrisNormalizado(beta,  w, h), null));

        imgReconstruida.setImage(SwingFXUtils.toFXImage(ConversionesColor.labARgb(l, alpha, beta, w, h), null));

        labelC1.setText("l (Luminosidad)");
        labelC2.setText("α (Amarillo-Azul)");
        labelC3.setText("β (Rojo-Verde)");
        labelReconstruida.setText("Reconstruida: lαβ → LMS → XYZ → RGB");
    }
}
