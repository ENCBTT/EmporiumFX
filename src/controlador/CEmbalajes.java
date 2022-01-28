package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.ConexionBD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;
import modelo.MEmbalajes;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class CEmbalajes implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MEmbalajes, Integer> clCod;
    @FXML
    private TableColumn<MEmbalajes, String> clDes;
    @FXML
    private TableColumn<MEmbalajes, String> clSig;
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
    private TextField txt_sig;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MEmbalajes> tb_principal;

    //collecion
    ObservableList<MEmbalajes> listaTbEmbalajes;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CEmbalajes.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbEmbalajes = FXCollections.observableArrayList();
        //llamar metodos
        MEmbalajes.obtenerInfoMun(conexion.getConnection(), listaTbEmbalajes);
        //cargar tabla
        tb_principal.setItems(listaTbEmbalajes);
        //Enlazar columnas con atributos
        clCod.setCellValueFactory(new PropertyValueFactory<MEmbalajes, Integer>("codEmb"));
        clDes.setCellValueFactory(new PropertyValueFactory<MEmbalajes, String>("desEmb"));
        clSig.setCellValueFactory(new PropertyValueFactory<MEmbalajes, String>("sigEmb"));
        //Eventos
        gestionar_eventos(); // carga datos de la fila seleccionada al formulario
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MEmbalajes>() {
            @Override
            public void changed(ObservableValue<? extends MEmbalajes> observableValue, MEmbalajes valorAnterior, MEmbalajes valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodEmb()));
                    txt_des.setText(valorSeleccionado.getDesEmb());
                    txt_sig.setText(valorSeleccionado.getSigEmb());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                    bt_imp.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText(null);
        txt_des.setText("");
        txt_sig.setText("");
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
        //crea una nueva instancia del tipo embalajes
        MEmbalajes a = new MEmbalajes(ultCod(), txt_des.getText(), txt_sig.getText());
        LOGGER.info("Se ha precionado el boton guardar");
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase embalajes
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbEmbalajes.add(a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro se ha guardado = " + a.getCodEmb());
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML
    public void editarRegistro() {
        LOGGER.info("Se ha precionado el boton editar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo embalajes
        MEmbalajes a = new MEmbalajes(Integer.valueOf(txt_cod.getText()), txt_des.getText(), txt_sig.getText());
        if(!validacion()){
            LOGGER.info("Informacions no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //Llama al metodo editar registro
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbEmbalajes.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro modificado es =" + a.getCodEmb());
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodEmb());
            if (resultado == 1) {
                listaTbEmbalajes.remove(tb_principal.getSelectionModel().getSelectedIndex());
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El registro ha sido eliminado correctamente");
                mensaje.show();
                LOGGER.info("Se ha eliminado el Registro");
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
        MEmbalajes m = new MEmbalajes();
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
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listaTbEmbalajes.removeAll(tb_principal.getItems());
            //cargar tabla con resultado de la busqueda
            MEmbalajes.busquedaInfoMun(conexion.getConnection(),listaTbEmbalajes, txt_buscar.getText());
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
            String rt = CEmbalajes.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        if(this.txt_des.getText().isEmpty() || this.txt_des == null || this.txt_sig.getText().isEmpty() || this.txt_sig.getText() == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
         } else {
            return true;
        }
    }

}//final