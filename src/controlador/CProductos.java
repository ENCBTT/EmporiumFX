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
import modelo.MProductos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CProductos implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MProductos, Integer> cl1;
    @FXML
    private TableColumn<MProductos, String> cl2;
    @FXML
    private TableColumn<MProductos, String> cl3;
    @FXML
    private TableColumn<MProductos, Integer> cl4;
    @FXML
    private TableColumn<MProductos, Integer> cl5;
    @FXML
    private TableColumn<MProductos, Integer> cl6;
    @FXML
    private TableColumn<MProductos, Integer> cl7;
    @FXML
    private TableColumn<MProductos, MEmbalajes> cl8;
    @FXML
    private TableColumn<MProductos, MSubgrupos_productos> cl9;
    @FXML
    private TableColumn<MProductos, MEstados> cl10;
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
    private TextField txt_nom;
    @FXML
    private TextField txt_des;
    @FXML
    private TextField txt_sto;
    @FXML
    private TextField txt_pc;
    @FXML
    private TextField txt_pv;
    @FXML
    private TextField txt_bar;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MProductos> tb_principal;
    @FXML
    private ComboBox<MSubgrupos_productos> cb1;
    @FXML
    private ComboBox<MEstados> cb2;
    @FXML
    private ComboBox<MEmbalajes> cb3;
    @FXML
    private RadioButton rb1;
    @FXML
    private RadioButton rb2;

    //collecion
    ObservableList<MProductos> listatbPrincipal;
    ObservableList<MSubgrupos_productos> listacb1;
    ObservableList<MEstados> listacb2;
    ObservableList<MEmbalajes> listacb3;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CProductos.class.getSimpleName());
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
        //llamar metodos
        MProductos.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        MSubgrupos_productos.obtenerInfoMun(conexion.getConnection(), listacb1);
        MEstados.obtenerInfoMun(conexion.getConnection(), listacb2);
        MEmbalajes.obtenerInfoMun(conexion.getConnection(), listacb3);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<>("nomMun"));
        cl3.setCellValueFactory(new PropertyValueFactory<>("desMun"));
        cl4.setCellValueFactory(new PropertyValueFactory<>("pcMun"));
        cl5.setCellValueFactory(new PropertyValueFactory<>("pvMun"));
        cl6.setCellValueFactory(new PropertyValueFactory<>("ivaMun"));
        cl7.setCellValueFactory(new PropertyValueFactory<>("stoMun"));
        cl8.setCellValueFactory(new PropertyValueFactory<>("embalaje"));
        cl9.setCellValueFactory(new PropertyValueFactory<>("subgrupo_producto"));
        cl10.setCellValueFactory(new PropertyValueFactory<>("estado"));
        //ComboBox
        cb1.setItems(listacb1);
        cb2.setItems(listacb2);
        cb3.setItems(listacb3);
        //Eventos
        gestionar_eventos();
        txt_nom.requestFocus();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MProductos>() {
            @Override
            public void changed(ObservableValue<? extends MProductos> observableValue, MProductos valorAnterior, MProductos valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    txt_nom.setText(valorSeleccionado.getNomMun());
                    txt_des.setText(valorSeleccionado.getDesMun());
                    txt_pc.setText(String.valueOf(valorSeleccionado.getPcMun()));
                    txt_pv.setText(String.valueOf(valorSeleccionado.getPvMun()));
                    if (valorSeleccionado.getIvaMun() == 5){
                        rb1.setSelected(true);
                        rb2.setSelected(false);
                    }else if (valorSeleccionado.getIvaMun() == 10){
                        rb1.setSelected(false);
                        rb2.setSelected(true);
                    } ///
                    txt_sto.setText(String.valueOf(valorSeleccionado.getStoMun()));
                    cb1.setValue(valorSeleccionado.getSubgrupo_producto());
                    cb2.setValue(valorSeleccionado.getEstado());
                    cb3.setValue(valorSeleccionado.getEmbalaje());
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
        txt_nom.setText("");
        txt_des.setText("");
        txt_sto.setText("");
        txt_bar.setText("");
        txt_pc.setText("");
        txt_pv.setText("");
        cb1.setValue(null);
        cb2.setValue(null);
        cb3.setValue(null);
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        txt_nom.requestFocus();
    }

    @FXML
    public void nuevo(){
        LOGGER.info("Se ha precionado el boton nuevo");
        limpiarCampos();
    }

    @FXML
    public void guardarRegistro() {
        LOGGER.info("Se ha precionado el boton guardar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);   //Reubicar las variables para guardar
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //crea una nueva instancia del tipo personas
            MProductos a = new MProductos(ultCod(), txt_nom.getText(), txt_des.getText(), Integer.valueOf(txt_pc.getText()), Integer.valueOf(txt_pv.getText()), rb1.isSelected()?5:10,
                    0.0, cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem());
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
                LOGGER.info("El registro se ha guardado = " + a.getCodMun());
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
        //crea una nueva instancia del tipo personas
        MProductos a = new MProductos(Integer.valueOf(txt_cod.getText()), txt_nom.getText(), txt_des.getText(), Integer.valueOf(txt_pc.getText()), Integer.valueOf(txt_pv.getText()), rb1.isSelected()?5:10,
                cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem());
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase personas
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listatbPrincipal.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodMun());
            if (resultado == 1) {
                listatbPrincipal.remove(tb_principal.getSelectionModel().getSelectedIndex());
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
        MProductos m = new MProductos();
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
            listatbPrincipal.removeAll(tb_principal.getItems());
            //cargar tabla con resultado de la busqueda
            MProductos.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
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
            String rt = CProductos.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        if(this.txt_nom.getText().isEmpty() || this.txt_nom == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Faltan informacion obligatoria");
            mensaje.show();
            return false;
         } else if (cb3.getSelectionModel().getSelectedItem() == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Falta seleccionar sub-grupo de producto");
            mensaje.show();
            return false;
        }else {
            return true;
        }
    }

}