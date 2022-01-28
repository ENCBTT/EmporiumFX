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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.*;
import modelo.MTimbrado_detalle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CTimbrados implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MTimbrados, Integer> cl1;
    @FXML
    private TableColumn<MTimbrados, String> cl2;
    @FXML
    private TableColumn<MTimbrados, String> cl3;
    @FXML
    private TableColumn<MTimbrados, Integer> cl4;
    @FXML
    private TableColumn<MTimbrados, Integer> cl5;
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
    private DatePicker dt_fechaI;
    @FXML
    private DatePicker dt_fechaF;
    @FXML
    private TextField txt_numT;
    @FXML
    private TextField txt_numA;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MTimbrados> tb_principal;
    @FXML
    private SplitPane stPane;
    @FXML
    private AnchorPane panel1;
    @FXML
    private AnchorPane panel2;
    @FXML
    private Pane panelPrincipal;
    //collecion
    ObservableList<MTimbrados> listatbPrincipal;
    ObservableList<MTimbrado_detalle> listatbSegundaria;
    ObservableList<MEmpresas> listacbEmp;
    ObservableList<MTimbrados> listacbTimbrado;
    ObservableList<MEstablecimientos> listacbEst;
    ObservableList<MPuntos_expediciones> listacbExp;
    ObservableList<MEstados> listacbEstado;
    ObservableList<MTipos_documentos> listacbDoc;
    //elementos detalle
    @FXML
    private TableView<MTimbrado_detalle> tb_segundaria;
    @FXML
    private Button bt_guardarD;
    @FXML
    private Button bt_editarD;
    @FXML
    private Button bt_eliminarD;
    @FXML
    private Button bt_nuevoD;
    @FXML
    private ComboBox<MEmpresas> cbEmp;
    @FXML
    private ComboBox<MTimbrados> cbTim;
    @FXML
    private ComboBox<MEstablecimientos> cbEst;
    @FXML
    private ComboBox<MPuntos_expediciones> cbExp;
    @FXML
    private ComboBox<MTipos_documentos> cbDoc;
    @FXML
    private ComboBox<MEstados> cbEstado;
    @FXML
    private TextField txt_numAC;
    @FXML
    private TextField txt_numI;
    @FXML
    private TextField txt_numF;
    // tabla segundaria
    @FXML
    private TableColumn<MTimbrado_detalle, MTimbrados> cl21;
    @FXML
    private TableColumn<MTimbrado_detalle, MEstablecimientos> cl22;
    @FXML
    private TableColumn<MTimbrado_detalle, MPuntos_expediciones> cl23;
    @FXML
    private TableColumn<MTimbrado_detalle, MTipos_documentos> cl24;
    @FXML
    private TableColumn<MTimbrado_detalle, Integer> cl25;
    @FXML
    private TableColumn<MTimbrado_detalle, Integer> cl26;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CTimbrados.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listatbSegundaria = FXCollections.observableArrayList();
        listacbEmp = FXCollections.observableArrayList();
        listacbTimbrado = FXCollections.observableArrayList();
        listacbEst = FXCollections.observableArrayList();
        listacbExp = FXCollections.observableArrayList();
        listacbDoc = FXCollections.observableArrayList();
        listacbEstado = FXCollections.observableArrayList();
        //llamar metodos
        MTimbrados.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        MTimbrado_detalle.obtenerInfoMun(conexion.getConnection(), listatbSegundaria);
        MEmpresas.obtenerInfoMun(conexion.getConnection(),listacbEmp);
        MTimbrados.obtenerInfoMun(conexion.getConnection(), listacbTimbrado);
        MEstablecimientos.obtenerInfoMun(conexion.getConnection(), listacbEst);
        MPuntos_expediciones.obtenerInfoMun(conexion.getConnection(), listacbExp);
        MTipos_documentos.obtenerInfoMun(conexion.getConnection(), listacbDoc);
        MEstados.obtenerInfoMun(conexion.getConnection(), listacbEstado);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        tb_segundaria.setItems(listatbSegundaria);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<MTimbrados, Integer>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<MTimbrados, String>("numTMun"));
        cl3.setCellValueFactory(new PropertyValueFactory<MTimbrados, String>("numAMun"));
        cl4.setCellValueFactory(new PropertyValueFactory<MTimbrados, Integer>("fecIMun"));
        cl5.setCellValueFactory(new PropertyValueFactory<MTimbrados, Integer>("fecFMun"));
        //Enlazar columnas con atributos
        cl21.setCellValueFactory(new PropertyValueFactory<MTimbrado_detalle, MTimbrados>("timbrados"));
        cl22.setCellValueFactory(new PropertyValueFactory<MTimbrado_detalle, MEstablecimientos>("establecimientos"));
        cl23.setCellValueFactory(new PropertyValueFactory<MTimbrado_detalle, MPuntos_expediciones>("expedicion"));
        cl24.setCellValueFactory(new PropertyValueFactory<MTimbrado_detalle, MTipos_documentos>("tipos_documentos"));
        cl25.setCellValueFactory(new PropertyValueFactory<MTimbrado_detalle, Integer>("numAc"));
        cl26.setCellValueFactory(new PropertyValueFactory<MTimbrado_detalle, Integer>("codFact"));
        //ComboBox
        cbTim.setItems(listacbTimbrado);
        cbDoc.setItems(listacbDoc);
        cbEst.setItems(listacbEst);
        cbExp.setItems(listacbExp);
        cbEstado.setItems(listacbEstado);
        cbEmp.setItems(listacbEmp);
        //Eventos
        gestionar_eventos();
        txt_numT.requestFocus();
        bt_guardar.setDisable(true);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MTimbrados>() {
            @Override
            public void changed(ObservableValue<? extends MTimbrados> observableValue, MTimbrados valorAnterior, MTimbrados valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    txt_numT.setText(String.valueOf(valorSeleccionado.getNumTMun()));
                    txt_numA.setText(String.valueOf(valorSeleccionado.getNumAMun()));
                    dt_fechaI.setValue(valorSeleccionado.getFecIMun().toLocalDate());
                    dt_fechaI.setValue(valorSeleccionado.getFecIMun().toLocalDate());
                    cbTim.setValue(valorSeleccionado);
                    cargarTb2(txt_cod.getText());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                }
            }
        });

        tb_segundaria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MTimbrado_detalle>() {
            @Override
            public void changed(ObservableValue<? extends MTimbrado_detalle> observableValue, MTimbrado_detalle valorAnterior, MTimbrado_detalle valorSeleccionado) {
                if (valorSeleccionado != null) {
                    //txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    //txt_numT.setText(String.valueOf(valorSeleccionado.getNumTMun()));
                    //txt_numA.setText(String.valueOf(valorSeleccionado.getNumAMun()));
                    //dt_fechaI.setValue(valorSeleccionado.getFecIMun().toLocalDate());
                    //dt_fechaI.setValue(valorSeleccionado.getFecIMun().toLocalDate());
                    //bt_guardar.setDisable(true);
                    //bt_eliminar.setDisable(false);
                    //bt_editar.setDisable(false);
                }
            }
        });
    }


    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText("");
        dt_fechaI.setValue(LocalDate.now().withDayOfMonth(1));
        dt_fechaF.setValue(LocalDate.now().plusYears(1).withDayOfMonth(31));
        txt_numT.setText("");
        txt_numA.setText("");
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        txt_numT.requestFocus();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);   //Reubicar las variables para guardar
        //crea una nueva instancia del tipo pedidos
        MTimbrados a = new MTimbrados(ultCod(), Integer.valueOf(txt_numT.getText()),Double.valueOf(txt_numA.getText()), Date.valueOf(dt_fechaI.getValue()), Date.valueOf(dt_fechaF.getValue()), cbEmp.getSelectionModel().getSelectedItem());
        //llamar al metodo guardar de la clase pedidos
        //abre conexion
        if(!validacion()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informacion obligatoria");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        } else {
            conexion.iniciarConexion();
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listatbPrincipal.add(a);
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
        //crea una nueva instancia del tipo pedidos
        //Llama al metodo editar registro
        MTimbrados a = new MTimbrados(Integer.valueOf(txt_cod.getText()), Integer.valueOf(txt_numT.getText()),Double.valueOf(txt_numA.getText()), Date.valueOf(dt_fechaI.getValue()), Date.valueOf(dt_fechaF.getValue()), cbEmp.getSelectionModel().getSelectedItem());
        if(!validacion()){

            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informacion obligatoria");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase pedidos
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listatbPrincipal.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
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
                listatbPrincipal.remove(tb_principal.getSelectionModel().getSelectedIndex());
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
        MTimbrados m = new MTimbrados();
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
        listatbPrincipal.removeAll(tb_principal.getItems());
        //cargar tabla con resultado de la busqueda
        MTimbrados.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void cargarTb2(String cod) {
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbSegundaria.removeAll(tb_segundaria.getItems());
        //cargar tabla con resultado de la busqueda
        MTimbrado_detalle.buscarRegistro(conexion.getConnection(),listatbSegundaria, cod);
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
        if(this.txt_numA.getText().isEmpty() || this.txt_numA == null){
            return false;
         } else {
            return true;
        }

    }

    public boolean validacionDet(){
        if(this.txt_numI.getText().isEmpty() || this.txt_numI == null){
            return false;
        } else {
            return true;
        }
    }

    public void limpiarCamposD(){}

    public void guardarDetalle(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);   //Reubicar las variables para guardar
        //crea una nueva instancia de Detalle Timbrados  new MTimbrados(Integer.valueOf(txt_cod.getText()), Integer.valueOf(txt_numT.getText())),
        MTimbrado_detalle a = new MTimbrado_detalle(cbTim.getSelectionModel().getSelectedItem(), cbEst.getSelectionModel().getSelectedItem(), cbExp.getSelectionModel().getSelectedItem(), cbEstado.getSelectionModel().getSelectedItem(), cbDoc.getSelectionModel().getSelectedItem(),Integer.valueOf(txt_numI.getText()), Integer.valueOf(txt_numF.getText()));
        //llamar al metodo guardar de la clase pedidos
        //abre conexion
        if(!validacionDet()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informacion obligatoria");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        } else {
            conexion.iniciarConexion();
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listatbSegundaria.add(a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                limpiarCampos();
                this.bt_nuevoD.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }
    public void editarDetalle(){}
    public void eliminarDetalle(){}


   public void mostrarDet(){
       //stPane.setDividerPositions(0.96f, 0.6f, 0.2f);

       double[] stP = stPane.getDividerPositions();
       if (stP[0] >= 0.950){
           //System.out.println("0425");
           //System.out.println(stP[0]);
           stPane.setDividerPositions(0.425f, 0.0f, 0.0f);
       } else {
           //System.out.println("0960");
           //System.out.println(stP[0]);
           stPane.setDividerPositions(1.0f, 0.0f, 0.0f);
       }
    }

    public void mostrarTabla(){

        double[] stP = stPane.getDividerPositions();
        if (stP[0] >= 0.950){
            System.out.println("0425");
            //System.out.println(stP[0]);
            stPane.setDividerPositions(0.425f, 0.0f, 0.0f);
        } else {
            System.out.println("0960");
            //System.out.println(stP[0]);
            stPane.setDividerPositions(0.0f, 0.0f, 0.0f);
        }
    }



}