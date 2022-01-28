package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.ConexionBD;
import modelo.MCartas;
import modelo.MGrupos_productos;
import modelo.MProductos;

import java.net.URL;
import java.util.ResourceBundle;

public class CCartasD implements Initializable {

    CCartasD carta;
    CPedidos pedido;
    //Componentes busqueda producto
    @FXML ImageView iv_img;
    @FXML TextField txt_buscarD;
    @FXML Button bt_add;
    @FXML Button bt_salirD;
    @FXML TableView tb_principalD;
    @FXML TableColumn <MCartas, Integer> cl31;
    @FXML TableColumn <MCartas, String> cl32;
    @FXML TableColumn <MCartas, Double> cl33;

    ObservableList<MCartas> listatbBusqueda;

    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        carta = this;
        //colleciones
        listatbBusqueda = FXCollections.observableArrayList();
        //llamar metodos
        MCartas.obtenerInfoMun(conexion.getConnection(), listatbBusqueda);
        //cargar tabla
        tb_principalD.setItems(listatbBusqueda);
        //Enlazar columnas con atributos
        cl31.setCellValueFactory(new PropertyValueFactory<MCartas, Integer>("codCar"));
        cl32.setCellValueFactory(new PropertyValueFactory<MCartas, String>("desCar"));
        cl33.setCellValueFactory(new PropertyValueFactory<MCartas, Double>("preCar"));
        //Eventos
        this.gestionar_eventos();
        txt_buscarD.requestFocus();
        //cerrar conexion
        conexion.cerrarConexion();
    }
    public void gestionar_eventos() {

        tb_principalD.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCartas>() {
            @Override
            public void changed(ObservableValue<? extends MCartas> observableValue, MCartas valorAnterior, MCartas valorSeleccionado) {
                if (valorSeleccionado != null) {
                    pedido.recibirDatosCar(valorSeleccionado);
                    Image e = new Image("file:C:\\Users\\User\\IdeaProjects\\javaFxHellow\\src\\resources\\imagenes\\"+valorSeleccionado.getImgCar());
                    iv_img.setImage(e);
                    //System.out.println(valorSeleccionado.getCodCar() + "<cod>" + valorSeleccionado.getPreCar() + "<valor>" + valorSeleccionado.getDesCar());
                }
            }
        });
    }


    @FXML
    public void busqueda2() {
        String des = this.txt_buscarD.getText();
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbBusqueda.removeAll(tb_principalD.getItems());
        //cargar tabla con resultado de la busqueda
        MCartas.buscarRegistro(conexion.getConnection(),listatbBusqueda, des);
        tb_principalD.setItems(listatbBusqueda);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void salirVentana(){
            Stage myStage = (Stage) this.bt_salirD.getScene().getWindow();
            myStage.close();
    }
    public void recibirPed(CPedidos stagePedidos){
        pedido = stagePedidos;
    }
}