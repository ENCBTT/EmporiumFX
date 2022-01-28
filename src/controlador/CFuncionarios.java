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
import modelo.MCargos;
import modelo.MFuncionarios;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CFuncionarios implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MFuncionarios, Integer> cl1;
    @FXML
    private TableColumn<MFuncionarios, String> cl2;
    @FXML
    private TableColumn<MFuncionarios, MCargos> cl3;
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
    private TableView<MFuncionarios> tb_principal;
    @FXML
    private ComboBox<MCargos> cb_1;

    //collecion
    ObservableList<MFuncionarios> listaTbMun;
    ObservableList<MCargos> listaCb1;

    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbMun = FXCollections.observableArrayList();
        listaCb1 =FXCollections.observableArrayList();
        //llamar metodos
        MFuncionarios.obtenerInfoMun(conexion.getConnection(), listaTbMun);
        MCargos.obtenerInfoMun(conexion.getConnection(), listaCb1);
        //cargar tabla
        tb_principal.setItems(listaTbMun);
        //Enlazar con atributos
        //Columnas
        cl1.setCellValueFactory(new PropertyValueFactory<MFuncionarios, Integer>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<MFuncionarios, String>("desMun"));
        cl3.setCellValueFactory(new PropertyValueFactory<MFuncionarios,MCargos>("cargos"));
        //ComboBox
        cb_1.setItems(listaCb1);
        //Eventos
        gestionar_eventos();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MFuncionarios>() {
            @Override
            public void changed(ObservableValue<? extends MFuncionarios> observableValue, MFuncionarios valorAnterior, MFuncionarios valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodFun()));
                    txt_des.setText(valorSeleccionado.getDesFun());
                    cb_1.setValue(valorSeleccionado.getCargos());
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
        txt_cod.setText(null);
        txt_des.setText(null);
        cb_1.setValue(null);
        //botone
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        //foco
        txt_des.requestFocus();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo funcionarios

        MFuncionarios a = new MFuncionarios(ultCod(), txt_des.getText(),cb_1.getSelectionModel().getSelectedItem());
        //llamar al metodo guardar de la clase funcionarios
        //abre conexion
        if(validacion() == false){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informacion obligatoria");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        } else {
            conexion.iniciarConexion();
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.add(a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.setHeaderText("Resultado:");
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
        //crea una nueva instancia del tipo funcionarios
        //Llama al metodo editar registro
        MFuncionarios a = new MFuncionarios(Integer.valueOf(txt_cod.getText()), txt_des.getText(), cb_1.getSelectionModel().getSelectedItem());
        if(validacion() == false){

            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informacion obligatoria");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase funcionarios
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
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
        MFuncionarios m = new MFuncionarios();
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
        String filtro = this.txt_buscar.getText();
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listaTbMun.removeAll(tb_principal.getItems());
        //cargar tabla con resultado de la busqueda
        MFuncionarios.buscarRegistro(conexion.getConnection(),listaTbMun, filtro);
        //cerrar conexion
        conexion.cerrarConexion();

    }

    @FXML
    public void salirVentanaMenu(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VMenuPrincipal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
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


    }

    public boolean validacion(){
        if(this.txt_des.getText().isEmpty() || this.txt_des == null){
            return false;
         } else {
            return true;
        }
    }

}