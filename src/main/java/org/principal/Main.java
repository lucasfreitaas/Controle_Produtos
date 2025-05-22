package org.principal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 250);
        stage.setTitle("Cadastro de Produtos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Método necessário para iniciar a aplicação JavaFX
        launch(args);
    }
}