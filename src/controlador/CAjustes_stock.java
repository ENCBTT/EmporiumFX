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
import java.util.Optional;
import java.util.ResourceBundle;


public class CAjustes_stock implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MAjustes_stock, Integer> clCod;
    @FXML
    private TableColumn<MAjustes_stock, MEstablecimientos> clEst;
    @FXML
    private TableColumn<MAjustes_stock, MProductos> clPro;
    @FXML
    private TableColumn<MAjustes_stock, MTipos_ajustes> clTip;
    @FXML
    private TableColumn<MAjustes_stock, MUsuarios> clUsu;
    @FXML
    private TableColumn<MAjustes_stock, String> clHis;
    @FXML
    private TableColumn<MAjustes_stock, Date> clFec;
    @FXML
    private TableColumn<MAjustes_stock, Double> clCan;
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
    private DatePicker dtFec;
    @FXML
    private TextArea txtHis;
    @FXML
    private TextField txtCan;
    @FXML
    private ComboBox <MEstablecimientos>cbEst;
    @FXML
    private ComboBox <MProductos>cbPro;
    @FXML
    private ComboBox <MTipos_ajustes>cbTip;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MAjustes_stock> tb_principal;
    //collecion
    ObservableList<MAjustes_stock> listaTbAp;
    ObservableList<MEstablecimientos> listacbEst;
    ObservableList<MProductos> listacbPro;
    ObservableList<MTipos_ajustes> listaCbTip;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CAjustes_stock.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbAp = FXCollections.observableArrayList();
        listacbEst = FXCollections.observableArrayList();
        listacbPro = FXCollections.observableArrayList();
        listaCbTip = FXCollections.observableArrayList();
        //llamar metodos
        MAjustes_stock.obtenerInfoMun(conexion.getConnection(), listaTbAp);
        MEstablecimientos.obtenerInfoMun(conexion.getConnection(), listacbEst);
        MProductos.obtenerInfoMun(conexion.getConnection(), listacbPro);
        MTipos_ajustes.obtenerInfoMun(conexion.getConnection(), listaCbTip);
        //cargar tabla
        tb_principal.setItems(listaTbAp);
        //ComboBox
        cbPro.setItems(listacbPro);
        cbTip.setItems(listaCbTip);
        cbEst.setItems(listacbEst);
        //Enlazar columnas con atributos
        clCod.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, Integer>("codAs"));
        clEst.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, MEstablecimientos>("establecimiento"));
        clPro.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, MProductos>("producto"));
        clTip.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, MTipos_ajustes>("tipo_ajuste"));
        clUsu.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, MUsuarios>("usuario"));
        clFec.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, Date>("fecAs"));
        clHis.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, String>("hisAs"));
        clCan.setCellValueFactory(new PropertyValueFactory<MAjustes_stock, Double>("canAs"));
        //Eventos
        gestionar_eventos(); // carga datos de la fila seleccionada al formulario
        dtFec.setValue(LocalDate.now()); //obtener fecha
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MAjustes_stock>() {
            @Override
            public void changed(ObservableValue<? extends MAjustes_stock> observableValue, MAjustes_stock valorAnterior, MAjustes_stock valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCod.setText(String.valueOf(valorSeleccionado.getCodAs()));
                    cbEst.setValue(valorSeleccionado.getEstablecimiento());
                    cbPro.setValue(valorSeleccionado.getProducto());
                    cbTip.setValue(valorSeleccionado.getTipo_ajuste());
                    txtUsu.setText(valorSeleccionado.getUsuario().getDesUsr());
                    dtFec.setValue(valorSeleccionado.getFecAs().toLocalDate());
                    txtHis.setText(valorSeleccionado.getHisAs());
                    txtCan.setText(String.valueOf(valorSeleccionado.getCanAs()));
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
        txtCod.setText(null);
        txtHis.setText("");
        txtUsu.setText("");
        dtFec.setValue(LocalDate.now());
        txtCan.setText("");
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
        LOGGER.info("Se ha precionado el boton guardar");
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        String cbpart [] = cbPro.getEditor().getText().split("-");
        System.out.println(cbpart[0] + " || " + cbpart[1] + " = valor separado");
        //evaluacion de tipo de ajuste
        Double cant = 0.0;
        if (cbTip.getSelectionModel().getSelectedItem().getClaMun() == 0){
            cant = Double.parseDouble(txtCan.getText());
            System.out.println("Clasificaion positiva");
        } else {
            cant = Double.parseDouble("-" + txtCan.getText());
            System.out.println("Clasificaion negativa");
        }
        //crea una nueva instancia Mproductos para realizar la edicion del campo saldo
        MProductos b = new MProductos(Integer.valueOf(cbpart[0]), cant);
        //crea una nueva instancia del Ajustes Stock
        MAjustes_stock a = new MAjustes_stock(ultCod(), cbEst.getSelectionModel().getSelectedItem(), b, cbTip.getSelectionModel().getSelectedItem(), new MUsuarios(1, "Root"),
                Date.valueOf(dtFec.getValue()), txtHis.getText(), cant);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase cargos
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                int resultado2 = b.editarStock(conexion.getConnection());
                if (resultado2 == 1) {
                    listaTbAp.add(a);
                    mensaje.setTitle("Registro agregado");
                    mensaje.setContentText("El registro ha sido agregado correctamente");
                    mensaje.setHeaderText("Resultado:");
                    mensaje.show();
                    LOGGER.info("El registro se ha guardado = " + a.getCodAs());
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
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        LOGGER.info("Se ha precionado el boton editar");
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        String cbpart [] = cbPro.getEditor().getText().split("-");
        System.out.println(cbpart[0] + " || " + cbpart[1] + " = valor separado");
        //evaluacion de tipo de ajuste
        Double cant = 0.0;
        if (cbTip.getSelectionModel().getSelectedItem().getClaMun() == 0){
            cant = Double.parseDouble(txtCan.getText());
            System.out.println("Clasificaion positiva");
        } else {
            cant = Double.parseDouble("-" + txtCan.getText());
            System.out.println("Clasificaion negativa");
        }
        //crea una nueva instancia del Ajustes Stock
        MAjustes_stock a = new MAjustes_stock(Integer.valueOf(txtCod.getText()), cbEst.getSelectionModel().getSelectedItem(), cbPro.getSelectionModel().getSelectedItem(), cbTip.getSelectionModel().getSelectedItem(), new MUsuarios(1, "Root"),
                Date.valueOf(dtFec.getValue()), txtHis.getText(), cant);
        //crea una nueva instancia Mproductos para realizar la edicion del campo saldo
        MProductos b = new MProductos(Integer.valueOf(cbpart[0]), cant);;
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //Llama al metodo editar registro
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                int resultado2 = b.editarStock(conexion.getConnection());
                if (resultado2 == 1) {
                    listaTbAp.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                    mensaje.setTitle("Registro agregado");
                    mensaje.setContentText("El registro ha sido actualizado correctamente");
                    mensaje.setHeaderText("Resultado:");
                    mensaje.show();
                    LOGGER.info("El registro modificado es =" + a.getCodAs());
                    limpiarCampos();
                    this.bt_nuevo.requestFocus();
                }
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodAs());
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
        MAjustes_stock m = new MAjustes_stock();
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
            MAjustes_stock.busquedaInfoMun(conexion.getConnection(),listaTbAp, txt_buscar.getText());
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
            String rt = CAjustes_stock.this.getClass().getSimpleName();
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
         } else {
            return true;
        }
    }



}