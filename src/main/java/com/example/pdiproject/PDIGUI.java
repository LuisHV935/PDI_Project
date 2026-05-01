
package com.example.pdiproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PDIGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PDIGUI.class.getResource("Index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PDIGui - Luis Alberto Hernandez Velazquez");
        stage.setScene(scene);
        stage.show();
    }
}
