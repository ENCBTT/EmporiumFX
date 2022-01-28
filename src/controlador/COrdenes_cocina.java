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
import javafx.util.converter.LocalTimeStringConverter;
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;
import modelo.MOrdenes_cocina;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;


public class COrdenes_cocina implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MOrdenes_cocina, Integer> clCod;
    @FXML
    private TableColumn<MOrdenes_cocina, MPedidos> clPed;
    @FXML
    private TableColumn<MOrdenes_cocina, MRecetas> clRec;
    @FXML
    private TableColumn<MOrdenes_cocina, String> clSit;
    @FXML
    private TableColumn<MOrdenes_cocina, Timer> clHi;
    @FXML
    private TableColumn<MOrdenes_cocina, Timer> clHf;
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
    private TextField txtCant;
    @FXML
    private TextField txtHi;
    @FXML
    private TextField txtHf;
    @FXML
    private ComboBox <String>cbSit;
    @FXML
    private ComboBox <MPedidos>cbPed;
    @FXML
    private ComboBox <MRecetas>cbRec;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private TableView<MOrdenes_cocina> tb_principal;
    //collecion
    ObservableList<MOrdenes_cocina> listaTbAp;
    ObservableList<MPedidos> listacbPed;
    ObservableList<MRecetas> listacbRec;
    ObservableList<String> listacbSit;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(COrdenes_cocina.class.getSimpleName());
    private ConexionBD conexion;
    private CPedidos pedido;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listaTbAp = FXCollections.observableArrayList();
        listacbPed = FXCollections.observableArrayList();
        listacbRec = FXCollections.observableArrayList();
        listacbSit = FXCollections.observableArrayList("Pendiente", "Iniciado", "Listo", "Cancelado");
        //llamar metodos
        MOrdenes_cocina.obtenerInfoMun(conexion.getConnection(), listaTbAp);
        MPedidos.obtenerInfoMun(conexion.getConnection(), listacbPed);
        MRecetas.obtenerInfoMun(conexion.getConnection(), listacbRec);
        //cargar tabla
        tb_principal.setItems(listaTbAp);
        //ComboBox
        cbPed.setItems(listacbPed);
        cbRec.setItems(listacbRec);
        cbSit.setItems(listacbSit);
        //llenar campos

        //Enlazar columnas con atributos
        clCod.setCellValueFactory(new PropertyValueFactory<>("codMun"));
        clPed.setCellValueFactory(new PropertyValueFactory<>("pedido"));
        clRec.setCellValueFactory(new PropertyValueFactory<>("receta"));
        clSit.setCellValueFactory(new PropertyValueFactory<>("situacion"));
        clHi.setCellValueFactory(new PropertyValueFactory<>("hora"));
        clHf.setCellValueFactory(new PropertyValueFactory<>("horaF"));
        //Eventos
        gestionar_eventos(); // carga datos de la fila seleccionada al formulario
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MOrdenes_cocina>() {
            @Override
            public void changed(ObservableValue<? extends MOrdenes_cocina> observableValue, MOrdenes_cocina valorAnterior, MOrdenes_cocina valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    cbPed.setValue(valorSeleccionado.getPedido());
                    cbRec.setValue(valorSeleccionado.getReceta());
                    cbSit.setValue(valorSeleccionado.getSituacion());
                    txtHi.setText(valorSeleccionado.getHora().toString());
                    txtHf.setText(valorSeleccionado.getHoraF().toString());
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
        txtHf.setText("");
        txtHi.setText("");
        cbPed.setValue(null);
        cbRec.setValue(null);
        cbSit.setValue(null);
        //botone
        //bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        //foco
        txt_buscar.requestFocus();
    }

    @FXML
    public void nuevo(){
        LOGGER.info("Se ha precionado el boton nuevo");
        limpiarCampos();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Accion no habilitada");
        mensaje.setContentText("Solo se pued egenerar ordenes de cocina a traves de pedidos");
        mensaje.setHeaderText("Accion no habilidad");
        mensaje.show();
        LOGGER.info("Se ha precionado el boton guardar");
    /*    if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            // por ser un combo box editable se debe obtener el texto del editor y separlo
            //String cbLpart [] = cbLoc.getEditor().getText().split(" - " );
            //System.out.println(cbLpart[0] + " || " + cbLpart[1] + " = valor separado");
            //String cbMpart [] = cbMun.getEditor().getText().split(" - ");
            //System.out.println(cbMpart[0] + " || " + cbMpart[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            //MLocalidades b = new MLocalidades(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            //MMunicipios c = new MMunicipios(Integer.valueOf(cbMpart[0]), cbMpart[1]);
            //crea una nueva instancia del tipo cargos                                   this.datosUser.getCodUser(), this.datosUser.getLogin()
            MOrdenes_cocina a = new MOrdenes_cocina(ultCod(), txtDir.getText(), b, c,new MUsuarios(1,"super"));
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
            conexion.cerrarConexion();   */
    }

    @FXML //esta accion no se realiza con este formulario segun documentacion
    public void editarRegistro() {
        LOGGER.info("Se ha precionado el boton editar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // por ser un combo box editable se debe obtener el texto del editor y separlo
        //String cbLpart [] = cbLoc.getEditor().getText().split(" - ");
        //System.out.println(cbLpart[0] + " || " + cbLpart[1] + " = valor separado");
        // cbMpart [] = cbMun.getEditor().getText().split(" - ");
        //System.out.println(cbMpart[0] + " || " + cbMpart[1] + " = valor separado");
        // crea la instancia con el resultado de la busqueda en el combobox
        //MLocalidades b = new MLocalidades(Integer.valueOf(cbLpart[0]), cbLpart[1]);
        //MMunicipios c = new MMunicipios(Integer.valueOf(cbMpart[0]), cbMpart[1]);
        //crea una nueva instancia del tipo cargos
        MOrdenes_cocina a = new MOrdenes_cocina(Integer.valueOf(txtCod.getText()), cbPed.getSelectionModel().getSelectedItem(), cbRec.getSelectionModel().getSelectedItem(), Double.parseDouble(txtCant.getText()), cbSit.getSelectionModel().getSelectedItem(), Time.valueOf(txtCod.getText()), Time.valueOf(txtHf.getText()));
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
        MOrdenes_cocina m = new MOrdenes_cocina();
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
            MOrdenes_cocina.busquedaInfoMun(conexion.getConnection(),listaTbAp, txt_buscar.getText());
            //cerrar conexion
            conexion.cerrarConexion();
        })).start();
    }

    /*@FXML
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
    }*/

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
            String rt = COrdenes_cocina.this.getClass().getSimpleName();
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
        if(this.txtHi.getText().isEmpty() || this.txtHi == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Faltan informacion obligatoria");
            mensaje.show();
            return false;
         } else if(this.cbPed.getSelectionModel().isEmpty() || this.cbPed.getSelectionModel().getSelectedItem() == null || this.cbRec.getSelectionModel().getSelectedItem() == null || this.cbRec.getSelectionModel().isEmpty()) {
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Falta seleccionar Pedido o Receta");
            mensaje.setHeaderText("Faltan datos");
            mensaje.show();
            return false;
        } else {
            return true;
        }
    }



}