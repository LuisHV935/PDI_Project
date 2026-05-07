package com.example.pdiproject.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

import com.example.pdiproject.Algortitmos.*;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

public class CambiarIluminacionController {
    @FXML
    private Slider valorDeIluminacion;
    @FXML
    private ImageView imagenConCambiosDeIluminacion;
    @FXML
    private Button guardarImagen;
    @FXML
    AnchorPane root;

    private Image imagenAModificar;

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
    }

    public void setImagenConCambiosDeIluminacion(Image imagen) {
        this.imagenAModificar = imagen;
    }
    @FXML
    public void handleAplicarCambios(){
        int valor = (int)valorDeIluminacion.getValue();
        BufferedImage imagenModificada = CambiarIluminacion.aplicar(imagenAModificar, valor);
        Image imagenModificadaParaMostrar = SwingFXUtils.toFXImage(imagenModificada, null);
        imagenConCambiosDeIluminacion.setImage(imagenModificadaParaMostrar);
        guardarImagen.setDisable(false);
    }

    @FXML
    public void handleGuardar(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar imagen");
        fileChooser.setInitialFileName("mi_imagen_con_cambios_de_iluminacion.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen", "*.png"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imagenConCambiosDeIluminacion.getImage(), null), "png", file);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
