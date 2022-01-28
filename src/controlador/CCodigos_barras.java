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
import modelo.MCodigos_barras;
import modelo.MProductos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CCodigos_barras implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MCodigos_barras, Integer> cl1;
    @FXML
    private TableColumn<MCodigos_barras, String> cl2;
    @FXML
    private TableColumn<MCodigos_barras, MProductos> cl3;
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
    private TableView<MCodigos_barras> tb_principal;
    @FXML
    private ComboBox<MProductos> cb_1;

    //collecion
    ObservableList<MCodigos_barras> listaTbMun;
    ObservableList<MProductos> listaCb1;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CCodigos_barras.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbMun = FXCollections.observableArrayList();
        listaCb1 =FXCollections.observableArrayList();
        //llamar metodos
        MCodigos_barras.obtenerInfoMun(conexion.getConnection(), listaTbMun);
        MProductos.obtenerInfoMun(conexion.getConnection(), listaCb1);
        //cargar tabla
        tb_principal.setItems(listaTbMun);
        //Enlazar con atributos
        //Columnas
        cl1.setCellValueFactory(new PropertyValueFactory<MCodigos_barras, Integer>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<MCodigos_barras, String>("desMun"));
        cl3.setCellValueFactory(new PropertyValueFactory<MCodigos_barras, MProductos>("producto"));
        //ComboBox
        cb_1.setItems(listaCb1);
        //Eventos
        gestionar_eventos();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCodigos_barras>() {
            @Override
            public void changed(ObservableValue<? extends MCodigos_barras> observableValue, MCodigos_barras valorAnterior, MCodigos_barras valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    txt_des.setText(valorSeleccionado.getDesMun());
                    cb_1.setValue(valorSeleccionado.getProducto());
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
        txt_des.setText("");
        cb_1.setValue(null);
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
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        String cbpart [] = cb_1.getEditor().getText().split("-");
        System.out.println(cbpart[0] + " || " + cbpart[1] + " = valor separado");
        //crea una nueva instancia del tipo codigos_barras
        MCodigos_barras a = new MCodigos_barras(ultCod(), txt_des.getText(), new MProductos(Integer.valueOf(cbpart[0]), cbpart[1]));
        LOGGER.info("Se ha precionado el boton guardar");
        System.out.println(cb_1.getSelectionModel().getSelectedItem() + " = valor si seleccionado");
        System.out.println(cb_1.getSelectionModel().getSelectedIndex() + " = index");
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase codigos_barras
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listaTbMun.add(a);
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
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        String cbpart [] = cb_1.getEditor().getText().split("-");
        System.out.println(cbpart[0] + " || " + cbpart[1] + " = valor separado");
        //crea una nueva instancia del tipo codigos_barras
        MCodigos_barras a = new MCodigos_barras(ultCod(), txt_des.getText(), new MProductos(Integer.valueOf(cbpart[0]), cbpart[1]));
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo editar de la clase codigos_barras
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodMun());
            if (resultado == 1) {
                listaTbMun.remove(tb_principal.getSelectionModel().getSelectedIndex());
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
        MCodigos_barras m = new MCodigos_barras();
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
            MCodigos_barras.busquedaInfoMun(conexion.getConnection(),listaTbMun, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
        })).start();
    }

    @FXML
    public void busquedaPro() {
        (new Thread(() -> {
            String filtro = this.cb_1.getEditor().getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listaCb1.removeAll(cb_1.getItems());
            //cargar tabla con resultado de la busqueda
            MProductos.buscarRegreducido(conexion.getConnection(),listaCb1, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
            cb_1.setItems(listaCb1);
            System.out.println(cb_1.getSelectionModel().getSelectedItem());
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
            String rt = CCodigos_barras.this.getClass().getSimpleName();
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
         } else if (cb_1.getSelectionModel().getSelectedItem() == null){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Falta seleccionar producto");
            mensaje.show();
            return false;
        }else {
            return true;
        }
    }

}