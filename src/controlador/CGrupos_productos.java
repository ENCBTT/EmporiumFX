package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.ConexionBD;
import modelo.MGrupos_productos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CGrupos_productos implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MGrupos_productos, Integer> clCod;
    @FXML
    private TableColumn<MGrupos_productos, String> clDes;
    //Componentes GUI
    @FXML
    private Button bt_nuevo;
    @FXML
    private Button bt_editar;
    @FXML
    private Button bt_eliminar;
    @FXML
    private Button bt_guardar;
    @FXML
    private TextField txt_cod;
    @FXML
    private TextField txt_des;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MGrupos_productos> tb_principal;
    
    //collecion
    ObservableList<MGrupos_productos> listaTbMun;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CGrupos_productos.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbMun = FXCollections.observableArrayList();
        //llamar metodos
        MGrupos_productos.obtenerInfoMun(conexion.getConnection(), listaTbMun);
        //cargar tabla
        tb_principal.setItems(listaTbMun);
        //Enlazar columnas con atributos
        clCod.setCellValueFactory(new PropertyValueFactory<MGrupos_productos, Integer>("codMun"));
        clDes.setCellValueFactory(new PropertyValueFactory<MGrupos_productos, String>("desMun"));
        //Eventos
        gestionar_eventos();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MGrupos_productos>() {
            @Override
            public void changed(ObservableValue<? extends MGrupos_productos> observableValue, MGrupos_productos valorAnterior, MGrupos_productos valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    txt_des.setText(valorSeleccionado.getDesMun());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText("");
        txt_des.setText("");
        //botone
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        //foco
        txt_des.requestFocus();
    }

    @FXML
    public void nuevo(){
        LOGGER.info("Se ha precionado el boton nuevo");
        limpiarCampos();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo grupos_productos
        MGrupos_productos a = new MGrupos_productos(ultCod(), txt_des.getText());
        LOGGER.info("Se ha precionado el boton guardar");
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase grupos_productos
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.add(a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.setHeaderText("Resultado:");
                LOGGER.info("El registro se ha guardado = " + a.getCodMun());
                mensaje.show();
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML
    public void editarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo grupos_productos
        MGrupos_productos a = new MGrupos_productos(Integer.valueOf(txt_cod.getText()), txt_des.getText());
        LOGGER.info("Se ha precionado el boton editar");
        if(!validacion() || tb_principal.getSelectionModel().getSelectedIndex() == -1){
            LOGGER.info("Falta eleccionar registro");
            mensaje.setTitle("Falta seleccionar Registro");
            mensaje.setContentText("Se debe seleccionar el registro a ser editado en la tabla");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo editar de la clase grupos_productos
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro modificado es =" + a.getCodMun());
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML
    public void eliminarRegistro() {
        LOGGER.info("Se ha precionado el boton eliminar");
        Alert confMensaje = new Alert(Alert.AlertType.CONFIRMATION);
        confMensaje.setTitle("Eliminar Registro");
        confMensaje.setContentText("");
        confMensaje.setHeaderText("Deseas eliminar el registro ?");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        Optional<ButtonType> resultConf = confMensaje.showAndWait();
        if(resultConf.get() == ButtonType.OK){
            //abre conexion
            conexion.iniciarConexion();
            int resultado = tb_principal.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.remove(tb_principal.getSelectionModel().getSelectedIndex());
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El registro ha sido eliminado correctamente");
                mensaje.show();
                LOGGER.info("Se ha eliminado el Registro = " + tb_principal.getSelectionModel().getSelectedItem());
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    public int ultCod(){
        //abre conexion
        conexion.iniciarConexion();
        MGrupos_productos m = new MGrupos_productos();
        int result = m.ultRegistro(conexion.getConnection());
        if (result >= 1){
            return result;
        }
        //cerrar conexion
        conexion.cerrarConexion();
        return 0;

    }
    @FXML
    public void busqueda() {
        (new Thread(() -> {
            String filtro = this.txt_buscar.getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listaTbMun.removeAll(tb_principal.getItems());
            //cargar tabla con resultado de la busqueda
            MGrupos_productos.busquedaInfoMun(conexion.getConnection(),listaTbMun, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
        })).start();
    }

    @FXML
    public void salirVentanaMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VMenuPrincipal.fxml"));
            Parent root = loader.load();
            //Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.show();
            Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void salirVentana(){
            Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
            myStage.close();
    }

    @FXML
    public void imprimir(){
        (new Thread(() -> {
            String rt = CGrupos_productos.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        if(this.txt_des.getText().isEmpty() || this.txt_des == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Faltan informacion obligatoria");
            mensaje.show();
            return false;
         } else {
            return true;
        }
    }

}