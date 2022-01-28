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
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLSyntaxErrorException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CPersonas implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MPersonas, Integer> cl1;
    @FXML
    private TableColumn<MPersonas, String> cl2;
    @FXML
    private TableColumn<MPersonas, String> cl3;
    @FXML
    private TableColumn<MPersonas, String> cl4;
    @FXML
    private TableColumn<MPersonas, String> cl5;
    @FXML
    private TableColumn<MPersonas, String> cl6;
    @FXML
    private TableColumn<MPersonas, MMunicipios> cl7;
    @FXML
    private TableColumn<MPersonas, MLocalidades> cl8;
    @FXML
    private TableColumn<MPersonas, MNacionalidades> cl9;
    @FXML
    private TableColumn<MEstados, MEstados> cl10;
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
    private TextField txt1;
    @FXML
    private TextField txt2;
    @FXML
    private TextField txt3;
    @FXML
    private TextField txt4;
    @FXML
    private TextField txt5;
    @FXML
    private TextField txt6;
    @FXML
    private TextField txt7;
    @FXML
    private TextField txt8;
    @FXML
    private TextField txt9;
    @FXML
    private TextField txt10;
    @FXML
    private TextField txt11;
    @FXML
    private TextField txt12;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private CheckBox ck_1;
    @FXML
    private CheckBox ck_2;
    @FXML
    private CheckBox ck_3;
    @FXML
    private TableView<MPersonas> tb_principal;
    @FXML
    private ComboBox<MEstados> cb1;
    @FXML
    private ComboBox<MMunicipios> cb2;
    @FXML
    private ComboBox<MLocalidades> cb3;
    @FXML
    private ComboBox<MNacionalidades> cb4;
    @FXML
    private ComboBox<MCargos> cb5;
    @FXML
    private Tab tabFun;
    @FXML
    private Tab tabCli;
    @FXML
    private Tab tabPro;
    //collecion
    ObservableList<MPersonas> listatbPrincipal;
    ObservableList<MEstados> listacb1;
    ObservableList<MMunicipios> listacb2;
    ObservableList<MLocalidades> listacb3;
    ObservableList<MNacionalidades> listacb4;
    ObservableList<MClase_persona_det> listaCP;
    ObservableList<MFuncionarios> listaFun;
    ObservableList<MClientes> listaCli;
    ObservableList<MProveedores> listaPro;
    ObservableList<MCargos> listaCargos;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CPersonas.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listacb1 = FXCollections.observableArrayList();
        listacb2 = FXCollections.observableArrayList();
        listacb3 = FXCollections.observableArrayList();
        listacb4 = FXCollections.observableArrayList();
        listaCP = FXCollections.observableArrayList();
        listaFun = FXCollections.observableArrayList();
        listaCli = FXCollections.observableArrayList();
        listaPro = FXCollections.observableArrayList();
        listaCargos = FXCollections.observableArrayList();

        //llamar metodos
        MPersonas.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        MEstados.obtenerInfoMun(conexion.getConnection(), listacb1);
        MMunicipios.obtenerInfoMun(conexion.getConnection(), listacb2);
        MLocalidades.obtenerInfoMun(conexion.getConnection(), listacb3);
        MNacionalidades.obtenerInfoMun(conexion.getConnection(), listacb4);
        MClase_persona_det.obtenerInfoMun(conexion.getConnection(), listaCP);
        MFuncionarios.obtenerInfoMun(conexion.getConnection(), listaFun);
        MClientes.obtenerInfoMun(conexion.getConnection(), listaCli);
        MProveedores.obtenerInfoMun(conexion.getConnection(), listaPro);
        MCargos.obtenerInfoMun(conexion.getConnection(), listaCargos);

        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<MPersonas, Integer>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("desMun"));
        cl3.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("apeMun"));
        cl4.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("ciMun"));
        cl5.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("dirMun"));
        cl6.setCellValueFactory(new PropertyValueFactory<MPersonas, String>("telMun"));
        cl7.setCellValueFactory(new PropertyValueFactory<MPersonas, MMunicipios>("municipio"));
        cl8.setCellValueFactory(new PropertyValueFactory<MPersonas, MLocalidades>("localidad"));
        cl9.setCellValueFactory(new PropertyValueFactory<MPersonas, MNacionalidades>("nacionalidad"));
        cl10.setCellValueFactory(new PropertyValueFactory<MEstados, MEstados>("estado"));
        //ComboBox
        cb1.setItems(listacb1);
        cb2.setItems(listacb2);
        cb3.setItems(listacb3);
        cb4.setItems(listacb4);
        cb5.setItems(listaCargos);
        //Eventos
        gestionar_eventos();
        txt2.requestFocus();
        tabFun.setDisable(true);
        tabCli.setDisable(true);
        tabPro.setDisable(true);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MPersonas>() {
            @Override
            public void changed(ObservableValue<? extends MPersonas> observableValue, MPersonas valorAnterior, MPersonas valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt1.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    txt2.setText(valorSeleccionado.getDesMun());
                    txt3.setText(valorSeleccionado.getDirMun());
                    txt4.setText(valorSeleccionado.getCiMun());
                    txt5.setText(valorSeleccionado.getTelMun());
                    txt6.setText(valorSeleccionado.getApeMun());
                    cb1.setValue(valorSeleccionado.getEstado());
                    //if(valorSeleccionado.getEstado().getCodMun() == 2){ bt_editar.setDisable(true);}
                    cb2.setValue(valorSeleccionado.getMunicipio());
                    cb3.setValue(valorSeleccionado.getLocalidad());
                    cb4.setValue(valorSeleccionado.getNacionalidad());
                    if(valorSeleccionado.getClase_persona().getcodFun()==(valorSeleccionado.getCodMun())){
                        ck_1.setSelected(true);
                        tabFun.setDisable(false);
                    }else {
                        ck_1.setSelected(false);
                        tabFun.setDisable(true);
                    }
                    if(valorSeleccionado.getClase_persona().getCodCli()==(valorSeleccionado.getCodMun())){
                        ck_2.setSelected(true);
                        tabCli.setDisable(false);
                    }else {
                        ck_2.setSelected(false);
                        tabCli.setDisable(true);
                    }
                    if(valorSeleccionado.getClase_persona().getCodPro()==(valorSeleccionado.getCodMun())){
                        ck_3.setSelected(true);
                        tabPro.setDisable(false);
                    } else {
                        ck_3.setSelected(false);
                        tabPro.setDisable(true);
                    }
                    txt7.setText(valorSeleccionado.getFuncionario().getDesFun());
                    txt8.setText(valorSeleccionado.getCiMun()); // falta agregar en la tabla o ver como hacerlo
                    cb5.setValue(valorSeleccionado.getFuncionario().getCargos()); //estira cargos del modelo Funcionario, se puede quitar la clase cargos del modelo personas
                    txt9.setText(valorSeleccionado.getCliente().getDesCli());
                    txt10.setText(valorSeleccionado.getCliente().getRucCli());
                    txt11.setText(valorSeleccionado.getProveedor().getDesPro());
                    txt12.setText(valorSeleccionado.getProveedor().getRucPro());
                    bt_guardar.setDisable(true);
                    if (valorSeleccionado.getEstado().getCodMun() == 2){
                        bt_editar.setDisable(true);bt_eliminar.setDisable(true);txt2.setEditable(false);cb1.setDisable(true);
                    }else {bt_editar.setDisable(false);bt_eliminar.setDisable(false);txt2.setEditable(true);cb1.setDisable(false);}
                }
            }
        });
    }
    @FXML
    public void habilitarCK(){
        if(this.ck_1.selectedProperty().getValue()){
            tabFun.setDisable(false);
        }else {
            tabFun.setDisable(true);
        }
        // ck_2 es clientes
        if(this.ck_2.selectedProperty().getValue()){
            tabCli.setDisable(false);
        }else {
            tabCli.setDisable(true);
        }
        // ck_3 es proveedores
        if(this.ck_3.selectedProperty().getValue()){
           tabPro.setDisable(false);
        }else {
            tabPro.setDisable(true);
        }
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");
        txt5.setText("");
        txt6.setText("");
        txt7.setText("");
        txt8.setText("");
        txt9.setText("");
        txt10.setText("");
        txt11.setText("");
        txt12.setText("");
        txt2.requestFocus();
        cb2.setValue(null);
        cb3.setValue(null);
        cb4.setValue(null);
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        // check box
        ck_1.setSelected(false);
        ck_2.setSelected(false);
        ck_3.setSelected(false);
        // Tabs
        tabFun.setDisable(true);
        tabCli.setDisable(true);
        tabPro.setDisable(true);
    }

    @FXML
    public void nuevo(){
        LOGGER.info("Se ha precionado el boton nuevo");
        limpiarCampos();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        int cod = ultCod();
        int cod1 = cod;
        int cod2 = cod;
        int cod3 = cod;
        LOGGER.info("Se ha presionado el botón guardar");
        // ck_1 es funcionarios
        if(!this.ck_1.selectedProperty().getValue()){
            cod1 = 0;
            System.out.println("ck1" + this.ck_1.selectedProperty().getValue());
        }else {
            System.out.println("ck1" + this.ck_1.selectedProperty().getValue());
        }
        // ck_2 es clientes
        if(!this.ck_2.selectedProperty().getValue()){
            cod2 = 0;
            System.out.println("ck2" + this.ck_3.selectedProperty().getValue());
        }else {
            System.out.println("ck2" + this.ck_2.selectedProperty().getValue());
        }
        // ck_3 es proveedores
        if(!this.ck_3.selectedProperty().getValue()){
            cod3 = 0;
            System.out.println("ck3" + this.ck_3.selectedProperty().getValue());
        }else {
            System.out.println("ck3" + this.ck_3.selectedProperty().getValue());
        }
        //crea una nueva instancia del tipo clases personas
        MClase_persona_det b = new MClase_persona_det(cod, cod1, cod2, cod3);
        //crea una nueva instancia del tipo funcionarios
        MFuncionarios f = new MFuncionarios(cod1, txt2.getText()+" "+txt6.getText(), cb5.getSelectionModel().getSelectedItem());
        //crea una nueva instancia del tipo clientes
        MClientes c = new MClientes(cod2, txt2.getText()+" "+txt6.getText(), txt4.getText());
        //crea una nueva instancia del tipo Proveedores
        MProveedores p = new MProveedores(cod3, txt2.getText()+" "+txt6.getText(), txt4.getText());
        //crea una nueva instancia del tipo personas
        MPersonas a = new MPersonas(cod, txt2.getText(), txt6.getText(), txt4.getText(), txt3.getText(), txt5.getText(), cb2.getSelectionModel().getSelectedItem(),
                cb3.getSelectionModel().getSelectedItem(), cb4.getSelectionModel().getSelectedItem(), b, f, c, p);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase personas
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                   listatbPrincipal.add(a);
                   mensaje.setTitle("Registro agregado");
                   mensaje.setContentText("El registro ha sido agregado correctamente");
                   mensaje.setHeaderText("Resultado:");
                   mensaje.show();
                    LOGGER.info("Se ingresado un nuevo registro =" + a.getCodMun());
                   limpiarCampos();
                   this.bt_nuevo.requestFocus();
               }else{
                LOGGER.info("No se ha podido ingresar el registro " + resultado);
            }
        }
            //cerrar conexion
            conexion.cerrarConexion();
        }

    @FXML
    public void editarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        int cod1 = Integer.valueOf(txt1.getText());
        int cod2 = Integer.valueOf(txt1.getText());
        int cod3 = Integer.valueOf(txt1.getText());
        LOGGER.info("Se ha presionado el botón guardar");
        // ck_1 es funcionarios
        if(!this.ck_1.selectedProperty().getValue()){
            cod1 = 0;
            System.out.println("ck1" + this.ck_1.selectedProperty().getValue());
        }
        // ck_2 es clientes
        if(!this.ck_2.selectedProperty().getValue()){
            cod2 = 0;
            System.out.println("ck2" + this.ck_3.selectedProperty().getValue());
        }
        // ck_3 es proveedores
        if(!this.ck_3.selectedProperty().getValue()){
            cod3 = 0;
            System.out.println("ck3" + this.ck_3.selectedProperty().getValue());
        }
        //crea una nueva instancia del tipo clases personas
        MClase_persona_det b = new MClase_persona_det(Integer.valueOf(txt1.getText()), cod1, cod2, cod3);
        //crea una nueva instancia del tipo funcionarios
        MFuncionarios f = new MFuncionarios(cod1, txt2.getText()+" "+txt6.getText(), cb5.getSelectionModel().getSelectedItem());
        //crea una nueva instancia del tipo clientes
        MClientes c = new MClientes(cod2, txt2.getText()+" "+txt6.getText(), txt4.getText());
        //crea una nueva instancia del tipo Proveedores
        MProveedores p = new MProveedores(cod3, txt2.getText()+" "+txt6.getText(), txt4.getText());
        //crea una nueva instancia del tipo personas
        MPersonas a = new MPersonas(Integer.valueOf(txt1.getText()), txt2.getText(), txt6.getText(), txt4.getText(), txt3.getText(), txt5.getText(), cb2.getSelectionModel().getSelectedItem(),
                cb3.getSelectionModel().getSelectedItem(), cb4.getSelectionModel().getSelectedItem(), b, f, c, p);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase personas
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listatbPrincipal.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro editado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro se ha editado con exito");
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }else{
                LOGGER.info("No se ha podido editar el registro " + resultado);
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML
    public void eliminarRegistro() {
        LOGGER.info("Se ha presionado el botón inactivar");
        Alert confMensaje = new Alert(Alert.AlertType.CONFIRMATION);
        confMensaje.setTitle("Inactivar Registro");
        confMensaje.setContentText("");
        confMensaje.setHeaderText("Deseas inactivar el registro ?");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        Optional<ButtonType> resultConf = confMensaje.showAndWait();
        if(resultConf.get() == ButtonType.OK){
            //abre conexion
            conexion.iniciarConexion();
            int resultado = tb_principal.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
            if (resultado == 1) {
                mensaje.setTitle("Registro inactivado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El registro ha sido inactivado correctamente");
                mensaje.show();
                LOGGER.info("El registro se ha inactivado con exito");
            } else {
                mensaje.setTitle("Error");
                mensaje.setContentText("");
                mensaje.setHeaderText("No fue posible inactivar el registro Err-" + resultado);
                mensaje.show();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    public int ultCod(){
        //abre conexion
        conexion.iniciarConexion();
        MPersonas m = new MPersonas();
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
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbPrincipal.removeAll(tb_principal.getItems());
        //cargar tabla con resultado de la busqueda
        MPersonas.busquedaInfoMun(conexion.getConnection(),listatbPrincipal, txt_buscar.getText());
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
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.txt2.getText().isEmpty() || this.txt2 == null || this.txt3.getText().isEmpty() || this.txt4 == null || this.txt6.getText().isEmpty()) {
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan información obligatoria");
            mensaje.setHeaderText("Falta información:");
            mensaje.show();
            return false;
         } else if(!this.ck_1.selectedProperty().getValue() && !this.ck_2.selectedProperty().getValue() && !this.ck_3.selectedProperty().get()) {
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Falta seleccionar la clase de Persona");
            mensaje.setHeaderText("Falta información:");
            mensaje.show();
            return false;
        } else if(ck_1.selectedProperty().getValue() && cb5.getSelectionModel().getSelectedIndex() == 0){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Falta seleccionar el cargo del funcionario");
            mensaje.setHeaderText("Falta información:");
            mensaje.show();
            return false;
        } else if(cb1.getSelectionModel().isEmpty()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Falta seleccionar Estado");
            mensaje.setHeaderText("Falta información: ");
            mensaje.show();
            return false;
        } else {
            return true;
            }
        }

}