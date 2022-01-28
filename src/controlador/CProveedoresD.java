package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.ConexionBD;
import modelo.MPersonas;

import java.net.URL;
import java.util.ResourceBundle;

public class CProveedoresD implements Initializable {

    CProveedoresD CclientesD;
    CFacturas Cfacturas;
    CFacturas_compras compra;
    //Componentes busqueda producto
    @FXML TextField txt_buscarD;
    @FXML Button bt_salirD;
    @FXML TableView tb_proD;
    @FXML TableColumn <MPersonas, Integer> cl31;
    @FXML TableColumn <MPersonas, String> cl32;
    @FXML TableColumn <MPersonas, String> cl33;
    @FXML TableColumn <MPersonas, String> cl34;
    @FXML TableColumn <MPersonas, String> cl35;
    @FXML TableColumn <MPersonas, String> cl36;

    ObservableList<MPersonas> listatbBusqueda;

    private ConexionBD conexion;
    private String clase;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        CclientesD = this;
        //colleciones
        listatbBusqueda = FXCollections.observableArrayList();
        //llamar metodos
        MPersonas.busquedaProveedoresD(conexion.getConnection(), listatbBusqueda, "");
        //cargar tabla
        tb_proD.setItems(listatbBusqueda);
        //Enlazar columnas con atributos
        cl31.setCellValueFactory(new PropertyValueFactory<MPersonas, Integer>("codMun"));
        cl32.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("desMun"));
        cl33.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("apeMun"));
        cl34.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("ciMun"));
        cl35.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("dirMun"));
        cl36.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("telMun"));
        //Eventos
        this.gestionar_eventos();
        txt_buscarD.requestFocus();

        //cerrar conexion
        conexion.cerrarConexion();
    }
    public void gestionar_eventos() {

        tb_proD.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MPersonas>() {
            @Override
            public void changed(ObservableValue<? extends MPersonas> observableValue, MPersonas valorAnterior, MPersonas valorSeleccionado) {
                if (valorSeleccionado != null) {

                    switch (clase){
                        case "facturaC":
                            System.out.println("Controlador factura compra");
                            compra.recibirDatosProv(
                                    String.valueOf(valorSeleccionado.getCodMun()),
                                    valorSeleccionado.getDesMun());
                            salirVentana();
                            break;
                    }


                }
            }
        });
    }

    @FXML
    public void addMov(){
        tb_proD.getSelectionModel().selectedItemProperty();
            cl31.getCellFactory();
    }

    @FXML
    public void busqueda2() {
        String des = this.txt_buscarD.getText();
        //String string = String.valueOf(cbD1.getSelectionModel().getSelectedItem());
       // String[] parts = string.split(" ");
       // String part1 = parts[0];
        //Integer idG = Integer.valueOf(part1);
        String val = "ok";
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbBusqueda.removeAll(tb_proD.getItems());
        //cargar tabla con resultado de la busqueda
        MPersonas.busquedaInfoMun(conexion.getConnection(),listatbBusqueda, des);
        tb_proD.setItems(listatbBusqueda);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void salirVentana(){
            Stage myStage = (Stage) this.bt_salirD.getScene().getWindow();
            myStage.close();
    }
    public void recibir(CFacturas stageFacturas){Cfacturas = stageFacturas; clase = "facturaV";}
    public void recibirFC(CFacturas_compras stageFacturas){ compra = stageFacturas; clase = "facturaC";}

}