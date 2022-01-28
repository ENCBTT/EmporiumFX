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
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CUsuarios implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MUsuarios, Integer> cl1;
    @FXML
    private TableColumn<MUsuarios, String> cl2;
    @FXML
    private TableColumn<MUsuarios, MFuncionarios> cl3;
    @FXML
    private TableColumn<MUsuarios, MEstados> cl4;
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
    private TextField txt_pass;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MUsuarios> tb_principal;
    @FXML
    private ComboBox<MFuncionarios> cb1;
    @FXML
    private ComboBox<MEstados> cb2;

    //collecion
    ObservableList<MUsuarios> listaTbMun;
    ObservableList<MFuncionarios> listaCb1;
    ObservableList<MEstados> listaCb2;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CUsuarios.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbMun = FXCollections.observableArrayList();
        listaCb1 =FXCollections.observableArrayList();
        listaCb2 =FXCollections.observableArrayList();
        //llamar metodos
        MUsuarios.obtenerInfoMun(conexion.getConnection(), listaTbMun);
        MFuncionarios.obtenerInfoMun(conexion.getConnection(), listaCb1);
        MEstados.obtenerInfoMun(conexion.getConnection(), listaCb2);
        //cargar tabla
        tb_principal.setItems(listaTbMun);
        //Enlazar con atributos
        //Columnas
        cl1.setCellValueFactory(new PropertyValueFactory<MUsuarios, Integer>("codUsr"));
        cl2.setCellValueFactory(new PropertyValueFactory<MUsuarios, String>("desUsr"));
        cl3.setCellValueFactory(new PropertyValueFactory<MUsuarios,MFuncionarios>("funcionario"));
        cl4.setCellValueFactory(new PropertyValueFactory<MUsuarios, MEstados>("estado"));
        //ComboBox
        cb1.setItems(listaCb1);
        cb2.setItems(listaCb2);
        //Eventos
        gestionar_eventos();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MUsuarios>() {
            @Override
            public void changed(ObservableValue<? extends MUsuarios> observableValue, MUsuarios valorAnterior, MUsuarios valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodUsr()));
                    txt_des.setText(valorSeleccionado.getDesUsr());
                    cb1.setValue(valorSeleccionado.getFuncionario());
                    cb2.setValue(valorSeleccionado.getEstado());
                    bt_guardar.setDisable(true);
                    if (valorSeleccionado.getEstado().getCodMun() == 2){
                        bt_editar.setDisable(true);bt_eliminar.setDisable(true);txt_des.setEditable(true);cb1.setDisable(true);
                    }else {bt_editar.setDisable(false);bt_eliminar.setDisable(false);txt_des.setEditable(false);cb1.setDisable(false);}
                }
            }
        });
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText("");
        txt_des.setText("");
        txt_pass.setText("");
        cb1.setValue(null);
        cb2.setValue(null);
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
        //crea una nueva instancia del tipo funcionarios
        MUsuarios a = new MUsuarios(ultCod(), txt_des.getText(), txt_pass.getText(), cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem());
        LOGGER.info("Se ha precionado el boton guardar");
        if(validacion() == false){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase usuarios
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.add(a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro se ha guardado = " + a.getCodUsr());
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
        //crea una nueva instancia del tipo Usuarios
        MUsuarios a = new MUsuarios(Integer.valueOf(txt_cod.getText()), txt_des.getText(), txt_pass.getText(), cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem());
        LOGGER.info("Se ha precionado el boton editar");
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //Llama al metodo editar registro
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro modificado es =" + a.getCodUsr());
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML
    public void eliminarRegistro() {
        Alert confMensaje = new Alert(Alert.AlertType.CONFIRMATION);
        confMensaje.setTitle("Inactivar Registro");
        confMensaje.setContentText("");
        confMensaje.setHeaderText("Deseas INACTIVAR el usuario ?");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        Optional<ButtonType> resultConf = confMensaje.showAndWait();
        LOGGER.info("Se ha precionado el boton eliminar");
        if(resultConf.get() == ButtonType.OK){
            //abre conexion
            conexion.iniciarConexion();
            int resultado = tb_principal.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
            LOGGER.info("El registro a ser inactivado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodUsr());
            if (resultado == 1) {
                mensaje.setTitle("Registro inactivado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El registro ha sido inactivado correctamente");
                mensaje.show();
                LOGGER.info("Se ha inactivado el Registro");
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
        MUsuarios m = new MUsuarios();
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
        MUsuarios.busquedaInfoMun(conexion.getConnection(),listaTbMun, filtro);
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
            String rt = CUsuarios.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        if(this.txt_des.getText().isEmpty() || this.txt_des == null || this.txt_des == null){
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