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


public class CDeliverys implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MDeliverys, Integer> clCod;
    @FXML
    private TableColumn<MDeliverys, MUsuarios> clUsu;
    @FXML
    private TableColumn<MDeliverys, String> clDir;
    @FXML
    private TableColumn<MDeliverys, MLocalidades> clLoc;
    @FXML
    private TableColumn<MDeliverys, MMunicipios> clMun;
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
    private TextField txtDir;
    @FXML
    private ComboBox <MLocalidades>cbLoc;
    @FXML
    private ComboBox <MMunicipios>cbMun;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MDeliverys> tb_principal;
    //collecion
    ObservableList<MDeliverys> listaTbAp;
    ObservableList<MLocalidades> listacbLoc;
    ObservableList<MMunicipios> listacbMun;
    MAcceso datosUser;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CDeliverys.class.getSimpleName());
    private ConexionBD conexion;
    private CPedidos pedido;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbAp = FXCollections.observableArrayList();
        listacbLoc = FXCollections.observableArrayList();
        listacbMun = FXCollections.observableArrayList();
        //llamar metodos
        MDeliverys.obtenerInfoMun(conexion.getConnection(), listaTbAp);
        MLocalidades.obtenerInfoMun(conexion.getConnection(), listacbLoc);
        MMunicipios.obtenerInfoMun(conexion.getConnection(), listacbMun);
        //cargar tabla
        tb_principal.setItems(listaTbAp);
        //ComboBox
        cbLoc.setItems(listacbLoc);
        cbMun.setItems(listacbMun);
        //llenar campos

        //Enlazar columnas con atributos
        clCod.setCellValueFactory(new PropertyValueFactory<MDeliverys, Integer>("codDel"));
        clDir.setCellValueFactory(new PropertyValueFactory<MDeliverys, String>("dirDel"));
        clLoc.setCellValueFactory(new PropertyValueFactory<MDeliverys, MLocalidades>("localidad"));
        clMun.setCellValueFactory(new PropertyValueFactory<MDeliverys, MMunicipios>("municipio"));
        clUsu.setCellValueFactory(new PropertyValueFactory<MDeliverys, MUsuarios>("usuario"));
        //Eventos
        gestionar_eventos(); // carga datos de la fila seleccionada al formulario
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MDeliverys>() {
            @Override
            public void changed(ObservableValue<? extends MDeliverys> observableValue, MDeliverys valorAnterior, MDeliverys valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCod.setText(String.valueOf(valorSeleccionado.getCodDel()));
                    txtDir.setText(valorSeleccionado.getDirDel());
                    cbLoc.setValue(valorSeleccionado.getLocalidad());
                    cbMun.setValue(valorSeleccionado.getMunicipio());
                    txtUsu.setText(valorSeleccionado.getUsuario().getDesUsr());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                    bt_imp.setDisable(false);
                }
            }
        });
    }

    public void datosUser(MAcceso datosUsuario){
        txtUsu.setText(datosUsuario.getLogin());
        datosUser = datosUsuario; // Mejora observada, cargar estos datos en un objeto de tipo usuario o acceso con las evaluaciones de Permisos de usuarios
        System.out.println(datosUser.getLogin() + " - " +datosUser.getCodUser());
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txtCod.setText(null);
        txtDir.setText("");
        cbLoc.setValue(null);
        cbMun.setValue(null);
        //botone
        //bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        //foco
        txtDir.requestFocus();
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
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            // por ser un combo box editable se debe obtener el texto del editor y separlo
            String cbLpart [] = cbLoc.getEditor().getText().split(" - " );
            System.out.println(cbLpart[0] + " || " + cbLpart[1] + " = valor separado");
            String cbMpart [] = cbMun.getEditor().getText().split(" - ");
            System.out.println(cbMpart[0] + " || " + cbMpart[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            MLocalidades b = new MLocalidades(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            MMunicipios c = new MMunicipios(Integer.valueOf(cbMpart[0]), cbMpart[1]);
            //crea una nueva instancia del tipo cargos                                   this.datosUser.getCodUser(), this.datosUser.getLogin()
            MDeliverys a = new MDeliverys(ultCod(), txtDir.getText(), b, c,new MUsuarios(1,"super"));
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase cargos
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                    listaTbAp.add(a);
                    mensaje.setTitle("Registro agregado");
                    mensaje.setContentText("El registro ha sido agregado correctamente");
                    mensaje.setHeaderText("Resultado:");
                    mensaje.show();
                    LOGGER.info("El registro se ha guardado = " + a.getCodDel());
                    limpiarCampos();
                    if (pedido != null){
                        pedido.recibirDatosDel(a);
                        salirVentana();
                    } else {
                        System.out.println("CPedidos null");
                    }
                    this.bt_nuevo.requestFocus();
                }
            }
            //cerrar conexion
            conexion.cerrarConexion();
    }

    @FXML //esta accion no se realiza con este formulario segun documentacion
    public void editarRegistro() {
        LOGGER.info("Se ha precionado el boton editar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        String cbLpart [] = cbLoc.getEditor().getText().split(" - ");
        System.out.println(cbLpart[0] + " || " + cbLpart[1] + " = valor separado");
        String cbMpart [] = cbMun.getEditor().getText().split(" - ");
        System.out.println(cbMpart[0] + " || " + cbMpart[1] + " = valor separado");
        // crea la instancia con el resultado de la busqueda en el combobox
        MLocalidades b = new MLocalidades(Integer.valueOf(cbLpart[0]), cbLpart[1]);
        MMunicipios c = new MMunicipios(Integer.valueOf(cbMpart[0]), cbMpart[1]);
        //crea una nueva instancia del tipo cargos
        MDeliverys a = new MDeliverys(Integer.valueOf(txtCod.getText()), txtDir.getText(), b, c,new MUsuarios(this.datosUser.getCodUser(), this.datosUser.getLogin()));
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
                LOGGER.info("El registro modificado es =" + a.getCodDel());
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodDel());
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
        MDeliverys m = new MDeliverys();
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
            MDeliverys.busquedaInfoMun(conexion.getConnection(),listaTbAp, txt_buscar.getText());
            //cerrar conexion
            conexion.cerrarConexion();
        })).start();
    }

    @FXML
    public void busquedaLoc() {
        (new Thread(() -> {
            String filtro = this.cbLoc.getEditor().getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listacbLoc.removeAll(cbLoc.getItems());
            //cargar tabla con resultado de la busqueda
            MLocalidades.buscarRegreducido(conexion.getConnection(),listacbLoc, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
            cbLoc.setItems(listacbLoc);
        })).start();
    }

    @FXML
    public void busquedaMun() {
        (new Thread(() -> {
            String filtro = this.cbMun.getEditor().getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listacbMun.removeAll(cbMun.getItems());
            //cargar tabla con resultado de la busqueda
            MMunicipios.buscarRegreducido(conexion.getConnection(),listacbMun, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
            cbMun.setItems(listacbMun);
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
            String rt = CDeliverys.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public void recibirPed(CPedidos stagePedidos){
        pedido = stagePedidos;
        System.out.println("Recibio controlador CPedidos");
    }

    public boolean validacion(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.txtDir.getText().isEmpty() || this.txtDir == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Faltan informacion obligatoria");
            mensaje.show();
            return false;
         } else if(this.cbLoc.getEditor().getText() == null || this.cbLoc.getEditor().getText().isEmpty() || this.cbMun.getEditor().getText() == null || this.cbMun.getEditor().getText().isEmpty()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Index = " + cbLoc.getSelectionModel().getSelectedIndex() + "\n"
                    + "Valor = " + cbLoc.getSelectionModel().getSelectedItem());
            mensaje.setHeaderText("Falta seleccionar Localidad o Municipio");
            mensaje.show();
            return false;
        } else if (this.cbLoc.getSelectionModel().getSelectedItem() == null || this.cbMun.getSelectionModel().getSelectedItem() == null || this.cbLoc.getSelectionModel().getSelectedIndex() != -1 || this.cbMun.getSelectionModel().getSelectedIndex() != -1){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Index = " + cbLoc.getSelectionModel().getSelectedIndex() + "\n"
            + "Valor = " + cbLoc.getSelectionModel().getSelectedItem());
            mensaje.setHeaderText("Se debe seleccionar Localidad o Municipio");
            mensaje.show();
            return false;
        } else {
            return true;
        }
    }



}