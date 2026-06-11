package com.example.pdiproject.Controllers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.example.pdiproject.Algortitmos.DensidadDePotencia;
import com.example.pdiproject.Algortitmos.Histogramas;
import com.example.pdiproject.Algortitmos.ObtenerImagenAEscalaDeGrises;
import com.example.pdiproject.Algortitmos.ProbabilidadDeAparicion;
import com.example.pdiproject.Controllers.TransformacionesController;
import com.example.pdiproject.Controllers.OperacionesAritmeticasController;
import com.example.pdiproject.Controllers.OperacionesLogicasController;
import com.example.pdiproject.Controllers.OperacionesRelacionalesController;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class IndexController {
    @FXML private BorderPane root;
    @FXML private ImageView imagenAMostrar;
    @FXML private Button cargarImagen;
    @FXML private MenuButton modificacionesDisponibles;
    @FXML private MenuButton elegirImagenATrabajar;
    @FXML private MenuButton metricasDeLaImagen;
    @FXML private MenuItem histogramaB;
    @FXML private MenuItem probabilidadB;
    @FXML private Button ruidoB;
    @FXML private Button filtrosB;
    @FXML private Button conversionesB;
    @FXML private Button binarizadoB;
    @FXML private MenuButton OperacionesB;
    @FXML private MenuItem transformacionesB;
    @FXML private MenuItem aritmeticasB;
    @FXML private MenuItem logicasB;
    @FXML private MenuItem relacionalesB;

    private Image imagenOriginal;
    private Image imagenAEscalaDeGrises;
    private Image imagenSeleccionada;

    //Arrays para histogramas
    private int[][] hRGB = new  int[3][256];
    private int[] hGE = new int[256];

    //Matriz para almacenar las probabilidades de todos los canales
    private  double[][] matrizDeProbabilidades = new double[4][256];

    //Matriz para almacenar las DPP de todos los canales
    private double[][] matrizDPP = new double[4][256];

    @FXML
    public void initialize() {
        root.getStylesheets().add(
                getClass().getResource("/com/example/pdiproject/PDIGUIStyle.css").toExternalForm()
        );
    }

    //Practica 1
    @FXML
    public void handleCargarImagen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona la imagen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagenes", "*.jpg","*.jpeg", "*.png", "*.gif"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File archivo = fileChooser.showOpenDialog(imagenAMostrar.getScene().getWindow());
        if(archivo != null){
            Image image = new Image(archivo.toURI().toString());
            configuracionDeCarga(image);
        }
    }

    @FXML
    public void handleIluminacion(){
        String rutaFXML = "/com/example/pdiproject/CambiarIluminacion.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            CambiarIluminacionController controller = loader.getController();
            controller.setImagenConCambiosDeIluminacion(this.imagenSeleccionada);
            Scene scene = new Scene(root);
            Stage ventanaIluminacion = new Stage();
            ventanaIluminacion.setTitle("Modificar Iluminacion");
            ventanaIluminacion.setScene(scene);
            ventanaIluminacion.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleContraste(){
        String rutaFXML = "/com/example/pdiproject/CambiarContraste.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            CambiarContrasteController controller = loader.getController();
            controller.setImagenConCambiosDeIluminacion(this.imagenSeleccionada);
            Scene scene = new Scene(root);
            Stage ventanaContraste = new Stage();
            ventanaContraste.setTitle("Modificar Contraste");
            ventanaContraste.setScene(scene);
            ventanaContraste.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleOriginal(){
        this.imagenSeleccionada = this.imagenOriginal;
        this.imagenAMostrar.setImage(this.imagenSeleccionada);
    }

    @FXML
    public void handleEscalaDeGrises(){
        generaraANivelesDeGris();
        this.imagenSeleccionada =  imagenAEscalaDeGrises;
        this.imagenAMostrar.setImage(this.imagenSeleccionada);
    }


    //Practica 2
    @FXML
    public void handleHistograma(){
        calcularHistogramas();
        String rutaFXML = "/com/example/pdiproject/Histogramas.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            HistogramasController controller = loader.getController();
            controller.setHistogramaGE(this.hGE);
            controller.setHistogramaRGB(this.hRGB);
            controller.mostrarHist();
            Scene scene = new Scene(root, 900, 900);
            Stage histogramas = new Stage();
            fijarDimensionMax(histogramas);
            histogramas.setTitle("Histogramas");
            histogramas.setScene(scene);
            histogramas.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleProbabilidad(){
        calcularProbabilidades();
        String rutaFXML = "/com/example/pdiproject/ProbabilidadDeApa.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            ProbabilidadController controller = loader.getController();
            controller.setMatrizDeProbabilidades(matrizDeProbabilidades);
            controller.mostrarProb();
            Scene scene = new Scene(root, 900, 800);
            Stage probabilidades = new Stage();
            fijarDimensionMax(probabilidades);
            probabilidades.setTitle("Probabilidades");
            probabilidades.setScene(scene);
            probabilidades.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDPP(ActionEvent actionEvent){

        calcularDPP();
        String rutaFXML = "/com/example/pdiproject/DPP.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            DPPController controller = loader.getController();
            controller.setMatrizDPP(this.matrizDPP);
            controller.mostrarDPP();
            Scene scene = new Scene(root, 900, 800);
            Stage dpp = new Stage();
            fijarDimensionMax(dpp);
            dpp.setTitle("Densidad De Potencia De Probabilidades");
            dpp.setScene(scene);
            dpp.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //Caso 01
    @FXML
    public void handleAplicarRuido() {
        String rutaFXML = "/com/example/pdiproject/Ruido.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            RuidoController controller = loader.getController();
            generaraANivelesDeGris();
            controller.setImagen(this.imagenAEscalaDeGrises);
            controller.setOnImagenProcesada(img -> {
                imagenSeleccionada = img;
                imagenAMostrar.setImage(img);
                configuracionDeCarga(img);
            });
            Scene scene = new Scene(root, 900, 700);
            Stage ventanaRuidos = new Stage();
            ventanaRuidos.setTitle("Aplicar Ruido");
            ventanaRuidos.setScene(scene);
            ventanaRuidos.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleFiltros(ActionEvent actionEvent) {
        String rutaFXML = "/com/example/pdiproject/Filtros.fxml";
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            FiltroController controller = loader.getController();
            generaraANivelesDeGris();
            controller.setImagen(this.imagenAEscalaDeGrises);
            controller.setOnImagenProcesada(img -> {
                imagenSeleccionada = img;
                imagenAMostrar.setImage(img);
                configuracionDeCarga(img);
            });
            Scene scene = new Scene(root, 900, 700);
            Stage ventanaFiltros = new Stage();
            ventanaFiltros.setTitle("Aplicar Filtro");
            ventanaFiltros.setScene(scene);
            ventanaFiltros.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Practica 3
    @FXML
    public void handleConversiones() {
        String rutaFXML = "/com/example/pdiproject/Conversiones.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            ConversionController controller = loader.getController();
            controller.setImagen(this.imagenSeleccionada);
            Scene scene = new Scene(root);
            Stage ventana = new Stage();
            fijarDimensionMax(ventana);
            ventana.setTitle("Conversión entre modelos de color");
            ventana.setScene(scene);
            ventana.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Practica 4
    @FXML
    public void handleBinarizado() {
        String rutaFXML = "/com/example/pdiproject/Binarizado.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            BinarizadoController controller = loader.getController();
            controller.setImagen(this.imagenSeleccionada);
            Scene scene = new Scene(root);
            Stage ventana = new Stage();
            fijarDimensionMax(ventana);
            ventana.setTitle("Binarización");
            ventana.setScene(scene);
            ventana.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Practica 5 - Operaciones con imagenes
    @FXML
    public void handleTransformaciones() {
        String rutaFXML = "/com/example/pdiproject/Transformaciones.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            TransformacionesController controller = loader.getController();
            controller.setImagen(this.imagenSeleccionada);
            controller.setOnImagenProcesada(img -> {
                imagenSeleccionada = img;
                imagenAMostrar.setImage(img);
                configuracionDeCarga(img);
            });
            Scene scene = new Scene(root, 900, 700);
            Stage ventana = new Stage();
            ventana.setTitle("Transformaciones");
            ventana.setScene(scene);
            ventana.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAritmeticas() {
        String rutaFXML = "/com/example/pdiproject/OperacionesAritmeticas.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            OperacionesAritmeticasController controller = loader.getController();
            controller.setImagen(this.imagenSeleccionada);
            controller.setOnImagenProcesada(img -> {
                imagenSeleccionada = img;
                imagenAMostrar.setImage(img);
                configuracionDeCarga(img);
            });
            Scene scene = new Scene(root, 900, 700);
            Stage ventana = new Stage();
            ventana.setTitle("Operaciones Aritmeticas");
            ventana.setScene(scene);
            ventana.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogicas() {
        String rutaFXML = "/com/example/pdiproject/OperacionesLogicas.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            OperacionesLogicasController controller = loader.getController();
            controller.setImagen(this.imagenSeleccionada);
            controller.setOnImagenProcesada(img -> {
                imagenSeleccionada = img;
                imagenAMostrar.setImage(img);
                configuracionDeCarga(img);
            });
            Scene scene = new Scene(root, 900, 700);
            Stage ventana = new Stage();
            ventana.setTitle("Operaciones Logicas");
            ventana.setScene(scene);
            ventana.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRelacionales() {
        String rutaFXML = "/com/example/pdiproject/OperacionesRelacionales.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            OperacionesRelacionalesController controller = loader.getController();
            controller.setImagen(this.imagenSeleccionada);
            controller.setOnImagenProcesada(img -> {
                imagenSeleccionada = img;
                imagenAMostrar.setImage(img);
                configuracionDeCarga(img);
            });
            Scene scene = new Scene(root, 900, 700);
            Stage ventana = new Stage();
            ventana.setTitle("Operaciones Relacionales");
            ventana.setScene(scene);
            ventana.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Metodos Auxiliares
    public void generaraANivelesDeGris(){
        if(this.imagenAEscalaDeGrises == null){
            this.imagenAEscalaDeGrises = ObtenerImagenAEscalaDeGrises.obtener(this.imagenOriginal);
        }
    }

    public void configuracionDeCarga(Image image){
        this.imagenSeleccionada = image;
        this.imagenOriginal = image;
        this.imagenAEscalaDeGrises = null;
        this.imagenAMostrar.setImage(image);
        this.modificacionesDisponibles.setDisable(false);
        this.elegirImagenATrabajar.setDisable(false);
        this.histogramaB.setDisable(false);
        this.probabilidadB.setDisable(false);
        this.hRGB[0][0] = Integer.MIN_VALUE;
        this.hGE[0] = Integer.MIN_VALUE;
        this.matrizDeProbabilidades[0][0] = Double.MIN_VALUE;
        this.matrizDPP[0][0] = Double.MIN_VALUE;
        this.filtrosB.setDisable(false);
        this.ruidoB.setDisable(false);
        this.metricasDeLaImagen.setDisable(false);
        this.conversionesB.setDisable(false);
        this.binarizadoB.setDisable(false);
        this.OperacionesB.setDisable(false);
    }

    public void calcularProbabilidades(){
        calcularHistogramas();
        if(matrizDeProbabilidades[0][0] == Double.MIN_VALUE){
            int dim = (int)(this.imagenSeleccionada.getHeight() * this.imagenSeleccionada.getWidth());
            matrizDeProbabilidades[0] = ProbabilidadDeAparicion.calcularProb(dim,hRGB[0]);
            matrizDeProbabilidades[1] = ProbabilidadDeAparicion.calcularProb(dim,hRGB[1]);
            matrizDeProbabilidades[2] =  ProbabilidadDeAparicion.calcularProb(dim,hRGB[2]);
            matrizDeProbabilidades[3] =  ProbabilidadDeAparicion.calcularProb(dim,hGE);
        }
    }

    public void calcularHistogramas(){
        if(this.hRGB[0][0] == Integer.MIN_VALUE) this.hRGB = Histogramas.obtenerHistogramasRGB(this.imagenOriginal);
        if(this.hGE[0] == Integer.MIN_VALUE) this.hGE = Histogramas.histGE(this.imagenOriginal);
    }

    public void calcularDPP(){
        calcularProbabilidades();
        if(this.matrizDPP[0][0] == Double.MIN_VALUE){
            this.matrizDPP = DensidadDePotencia.calcularDPP(this.matrizDeProbabilidades);
        }
    }

    public void fijarDimensionMax(Stage stage){
        Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();
        stage.setMaxHeight(pantalla.getHeight());
        stage.setMaxWidth(pantalla.getWidth());
    }

}

