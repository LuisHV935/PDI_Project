package com.example.pdiproject.Controllers;

import com.example.pdiproject.Algortitmos.CambiarContraste;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CambiarContrasteController {
    private Image imagenAModificar;
    @FXML
    private ImageView imagenConCambiosDeContraste;
    @FXML
    private Slider valor;
    @FXML
    private Button guardarImagen;
    @FXML
    VBox root;

    @FXML
    public void initialize() {
        try{
            root.getStylesheets().add(
                    getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
            );
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void setImagenConCambiosDeIluminacion(Image imagen) {
        this.imagenAModificar = imagen;
    }

    @FXML
    public void handlerAplicar(){
        BufferedImage imagenModificada = CambiarContraste.aplicar(this.imagenAModificar, this.valor.getValue());
        Image imagenModificado = SwingFXUtils.toFXImage(imagenModificada, null);
        imagenConCambiosDeContraste.setImage(imagenModificado);
        guardarImagen.setDisable(false);
    }

    @FXML
    public void handleGuardar(){
        FileChooser  fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar imagen");
        fileChooser.setInitialFileName("mi_imagen_con_cambios_de_contraste.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen", "*.png"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imagenConCambiosDeContraste.getImage(), null), "png", file);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
