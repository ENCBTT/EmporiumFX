package vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VTipos_documentos extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/vista/VTipos_documentos.fxml"));
        primaryStage.setTitle("REGISTRO DE TIPOS DE DOCUMENTOS");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


