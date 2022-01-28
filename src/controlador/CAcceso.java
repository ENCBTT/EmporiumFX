package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CAcceso implements Initializable {

    //Listado de componentes de GUI
    @FXML private Button bt_salir;
    @FXML private Button bt_ingresar;
    @FXML private TextField txt_user;
    @FXML private TextField txt_pass;
    @FXML private ComboBox<MEstablecimientos> cb_est;
    @FXML private ComboBox<MPuntos_expediciones> cb_exp;

    //Colecciones
    ObservableList<MEstablecimientos> listaCbEst;
    ObservableList<MPuntos_expediciones> listaCbExp;
    //conexion
    private ConexionBD conexion;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CAcceso.class.getSimpleName());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        listaCbEst = FXCollections.observableArrayList();
        listaCbExp = FXCollections.observableArrayList();
        //llamar metodos
        MEstablecimientos.obtenerInfoMun(conexion.getConnection(),listaCbEst);
        MPuntos_expediciones.obtenerInfoMun(conexion.getConnection(),listaCbExp);
        //cargar combo box
        cb_est.setItems(listaCbEst);
        cb_exp.setItems(listaCbExp);
        //cerrar conexion
        conexion.cerrarConexion();
        LOGGER.info("Sistema en ejecución");
    }

    public void login(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //nueva instancia de Acceso
        MAcceso a = new MAcceso(txt_user.getText(), txt_pass.getText(), cb_exp.getSelectionModel().getSelectedItem(), cb_est.getSelectionModel().getSelectedItem());
        //System.out.println("login = " + txt_user.getText() + " establecimiento id = " + cb_est.getSelectionModel().getSelectedItem() + " espedicion id = " + cb_exp.getSelectionModel().getSelectedItem());
        if(!validacion()) {
            LOGGER.info("Informaciones de inicio no validas\n" +
                    "Login = " +txt_user.getText()+"\n"+
                    "Establecimiento = "+cb_est.getSelectionModel().getSelectedItem()+"\n"+
                    "Punto de Expedicion = "+cb_exp.getSelectionModel().getSelectedItem());
        } else {
            //abre conexion
            conexion.iniciarConexion();
            a =  a.entrar(conexion.getConnection());
                if (a==null){
                    System.out.println(" (A) es NULL");
                    LOGGER.info("Informaciones de inicio de session no valida, retorno (A = NULL)");
                    mensaje.setTitle("Datos incorrectos");
                    mensaje.setContentText("");
                    mensaje.setHeaderText("Credenciales de inicio no son invalidas");
                    mensaje.show();
                }else {
                    LOGGER.info("Informaciones de inicio de session\n" +
                            "Login = " + a.getCodUser() + "\n" +
                            "Login = " + a.getLogin() + "\n" +
                            "Establecimiento = " + cb_est.getSelectionModel().getSelectedItem() + "\n" +
                            "Punto de Expedicion = " + cb_exp.getSelectionModel().getSelectedItem());
                    ventanaMenu(a);
                }
        }
        //abre conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void salirVentana(){
        Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
        myStage.close();
    }

    @FXML
    public void ventanaMenu2(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VMenuPrincipal.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ventanaMenu(MAcceso acc){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VMenuPrincipal.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stageProd = new Stage();
            CMenuPrincipal MenuPrincipal = fxmlLoader.getController();
            MenuPrincipal.datosAcces(acc);
            stageProd.setScene(new Scene(root1));
            stageProd.show();
            Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validacion(){
        if(this.txt_user.getText().isEmpty() || this.txt_user == null || this.txt_pass.getText().isEmpty() || this.txt_pass == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Error al ingresar");
            mensaje.setContentText("");
            mensaje.setHeaderText("Usuario o contrseña no son validos");
            mensaje.show();
            return false;
        } else if (cb_est.getSelectionModel().getSelectedItem() == null || cb_exp.getSelectionModel().getSelectedItem() == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Falta seleccionar Establecimiento o Punto de Expedición");
            mensaje.show();
            return false;
        }else {
            return true;
        }
    }

}

