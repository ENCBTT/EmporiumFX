package vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VMenuPrincipal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/vista/VAcceso.fxml"));
        primaryStage.setTitle("ACCESO AL SISTEMA");
        primaryStage.setScene(new Scene(root, 425, 310));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
