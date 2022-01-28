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
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class CAjustes_precios implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MAjustes_precios, Integer> clCod;
    @FXML
    private TableColumn<MAjustes_precios, MProductos> clPro;
    @FXML
    private TableColumn<MAjustes_precios, MUsuarios> clUsu;
    @FXML
    private TableColumn<MAjustes_precios, String> clHis;
    @FXML
    private TableColumn<MAjustes_precios, String> clFec;
    @FXML
    private TableColumn<MAjustes_precios, Double> clVal;
    @FXML
    private TableColumn<MAjustes_precios, Integer> clTip;
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
    private TextField txtCod;
    @FXML
    private TextField txtUsu;
    @FXML
    private TextField txtHis;
    @FXML
    private DatePicker dtFec;
    @FXML
    private TextField txtVal;
    @FXML
    private ComboBox <MProductos>cbPro;
    @FXML
    private ComboBox <String>cbTip;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MAjustes_precios> tb_principal;
    //collecion
    ObservableList<MAjustes_precios> listaTbAp;
    ObservableList<MProductos> listacbPro;
    ObservableList<String> listaCbTip;
    MAcceso datosUser;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CAjustes_precios.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbAp = FXCollections.observableArrayList();
        listacbPro = FXCollections.observableArrayList();
        listaCbTip = FXCollections.observableArrayList("Costo", "Venta");
        //llamar metodos
        //MUsuarios.busquedaInfoUser(conexion.getConnection(), datosUser);
        MAjustes_precios.obtenerInfoMun(conexion.getConnection(), listaTbAp);
        MProductos.obtenerInfoMun(conexion.getConnection(), listacbPro);
        //cargar tabla
        tb_principal.setItems(listaTbAp);
        //ComboBox
        cbPro.setItems(listacbPro);
        cbTip.setItems(listaCbTip);
        //llenar campos

        //Enlazar columnas con atributos
        clCod.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, Integer>("codAp"));
        clPro.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, MProductos>("producto"));
        clUsu.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, MUsuarios>("usuario"));
        clHis.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, String>("hisAp"));
        clFec.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, String>("fecAp"));
        clVal.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, Double>("valAp"));
        //clTip.setCellValueFactory(new PropertyValueFactory<MAjustes_precios, Integer>("tprAp"));
        //Eventos
        gestionar_eventos(); // carga datos de la fila seleccionada al formulario
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MAjustes_precios>() {
            @Override
            public void changed(ObservableValue<? extends MAjustes_precios> observableValue, MAjustes_precios valorAnterior, MAjustes_precios valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCod.setText(String.valueOf(valorSeleccionado.getCodAp()));
                    cbPro.setValue(valorSeleccionado.getProducto());
                    txtUsu.setText(valorSeleccionado.getUsuario().getDesUsr());
                    txtHis.setText(valorSeleccionado.getHisAp());
                    dtFec.setValue(valorSeleccionado.getFecAp().toLocalDate());
                    txtVal.setText(String.valueOf(valorSeleccionado.getValAp()));
                    if(valorSeleccionado.getTprAp() == 0){
                        cbTip.setValue("Costo");
                    }else {
                        cbTip.setValue("Venta");
                    }
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    //bt_editar.setDisable(false);
                    bt_imp.setDisable(false);
                }
            }
        });
    }

    public void datosUser(MAcceso datosUsuario){
        txtUsu.setText(datosUsuario.getLogin());
        datosUser = datosUsuario; // Mejora observada, cargar estos datos en un objeto de tipo usuario o acceso con las envaluaciones de Permisos de ususarios
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txtCod.setText(null);
        txtHis.setText("");
        dtFec.setValue(LocalDate.now());
        txtVal.setText("");
        cbPro.setValue(null);
        cbTip.setValue(null);
        //botone
        //bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        //foco
        cbPro.requestFocus();
    }

    @FXML
    public void nuevo(){
        LOGGER.info("Se ha precionado el boton nuevo");
        limpiarCampos();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        String cbpart [] = cbPro.getEditor().getText().split("-");
        System.out.println(cbpart[0] + " || " + cbpart[1] + " = valor separado");
        // crea la instancia de productos con el resultado de la busqueda en el combobox
        MProductos b = new MProductos(Integer.valueOf(cbpart[0]), Integer.parseInt(txtVal.getText()));
        //crea una nueva instancia del tipo cargos
        MAjustes_precios a = new MAjustes_precios(ultCod(), b, new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                txtHis.getText(), Date.valueOf(dtFec.getValue()), Double.parseDouble(txtVal.getText()), cbTip.getSelectionModel().getSelectedIndex());

        LOGGER.info("Se ha precionado el boton guardar");
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase cargos
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                int resultado2 = b.editarPrecio(conexion.getConnection(), cbTip.getSelectionModel().getSelectedIndex());
                if (resultado2 == 1) {
                    listaTbAp.add(a);
                    mensaje.setTitle("Registro agregado");
                    mensaje.setContentText("El registro ha sido agregado correctamente");
                    mensaje.setHeaderText("Resultado:");
                    mensaje.show();
                    LOGGER.info("El registro se ha guardado = " + a.getCodAp());
                    limpiarCampos();
                    this.bt_nuevo.requestFocus();
                }
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML //esta accion no se realiza con este formulario segun documentacion
    public void editarRegistro() {
        LOGGER.info("Se ha precionado el boton editar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo cargos
        MAjustes_precios a = new MAjustes_precios(Integer.valueOf(txtCod.getText()), cbPro.getSelectionModel().getSelectedItem(), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                txtHis.getText(), Date.valueOf(dtFec.getValue()), Double.parseDouble(txtVal.getText()), cbTip.getSelectionModel().getSelectedIndex());
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //Llama al metodo editar registro
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbAp.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro modificado es =" + a.getCodAp());
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodAp());
            if (resultado == 1) {
                listaTbAp.remove(tb_principal.getSelectionModel().getSelectedIndex());
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
        MAjustes_precios m = new MAjustes_precios();
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
            listaTbAp.removeAll(tb_principal.getItems());
            //cargar tabla con resultado de la busqueda
            MAjustes_precios.busquedaInfoMun(conexion.getConnection(),listaTbAp, txt_buscar.getText());
            //cerrar conexion
            conexion.cerrarConexion();
        })).start();
    }

    @FXML
    public void busquedaPro() {
        (new Thread(() -> {
            String filtro = this.cbPro.getEditor().getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listacbPro.removeAll(cbPro.getItems());
            //cargar tabla con resultado de la busqueda
            MProductos.buscarRegreducido(conexion.getConnection(),listacbPro, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
            cbPro.setItems(listacbPro);
            System.out.println(cbPro.getSelectionModel().getSelectedItem());
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
            String rt = CAjustes_precios.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        if(this.txtHis.getText().isEmpty() || this.txtHis == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Faltan informacion obligatoria");
            mensaje.show();
            return false;
         } else if(cbPro.getEditor().getText().isEmpty() || cbTip.getEditor().getText().isEmpty()){
            return false;
        } else {
            return true;
        }
    }



}