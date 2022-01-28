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
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CPedidos implements Initializable {
    CPedidos Cpedidos;
    //columnas tabla
    @FXML
    private TableColumn<MPedidos, Integer> cl1;
    @FXML
    private TableColumn<MPedidos, String> cl2;
    @FXML
    private TableColumn<MPedidos, String> cl3;
    @FXML
    private TableColumn<MPedidos, Integer> cl4;
    @FXML
    private TableColumn<MPedidos, Integer> cl5;
    @FXML
    private TableColumn<MPedidos, MMesas> cl6;
    @FXML
    private TableColumn<MPedidos, MEstados> cl7;
    @FXML
    private TableColumn<MPedidos, MDeliverys> cl8;
    @FXML
    private TableColumn<MPedidos, MFuncionarios> cl9;
    // tabla segundaria
    @FXML
    private TableColumn<MPedidos_detalle, Integer> cl21;
    @FXML
    private TableColumn<MPedidos_detalle, Double> cl22;
    @FXML
    private TableColumn<MPedidos_detalle, Double> cl23;
    @FXML
    private TableColumn<MPedidos_detalle, Double> cl24;
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
    private DatePicker dt_fecha;
    @FXML
    private TextField txt_hor;
    @FXML
    private TextField txt_total;
    @FXML
    private TextField txt_num;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_agregarD;
    @FXML
    private Button bt_editarD;
    @FXML
    private Button bt_buscarD;
    @FXML
    private Button bt_eliminarD;
    @FXML
    private TextField txt_precio;
    @FXML
    private TextField txt_cant;
    @FXML
    private TableView<MPedidos> tb_principal;
    @FXML
    private TableView<MPedidos_detalle> tb_segundaria;
    @FXML
    private ComboBox<MMesas> cb1;
    @FXML
    private ComboBox<MEstados> cb2;
    @FXML
    private ComboBox<MDeliverys> cb3;
    @FXML
    private ComboBox<MFuncionarios> cb4;
    @FXML
    private ComboBox<MCartas> cb5;
    @FXML
    private SplitPane stPane;

    //collecion
    ObservableList<MPedidos> listatbPrincipal;
    ObservableList<MMesas> listacb1;
    ObservableList<MEstados> listacb2;
    ObservableList<MDeliverys> listacb3;
    ObservableList<MFuncionarios> listacb4;
    ObservableList<MCartas> listacb5;
    ObservableList<MPedidos_detalle> listatbSegundaria;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(CPedidos.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        Cpedidos = this;
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listatbSegundaria = FXCollections.observableArrayList();
        listacb1 = FXCollections.observableArrayList();
        listacb2 = FXCollections.observableArrayList();
        listacb3 = FXCollections.observableArrayList();
        listacb4 = FXCollections.observableArrayList();
        listacb5 = FXCollections.observableArrayList();
        //llamar metodos
        MPedidos.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        MMesas.obtenerInfoMunActivos(conexion.getConnection(), listacb1);
        MEstados.obtenerInfoMun(conexion.getConnection(), listacb2);
        MFuncionarios.buscarMozo(conexion.getConnection(), listacb4);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        tb_segundaria.setItems(listatbSegundaria);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<>("fecMun"));
        cl3.setCellValueFactory(new PropertyValueFactory<>("horMun"));
        cl4.setCellValueFactory(new PropertyValueFactory<>("totalMun"));
        cl5.setCellValueFactory(new PropertyValueFactory<>("numMun"));
        cl6.setCellValueFactory(new PropertyValueFactory<>("mesas"));
        cl7.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cl8.setCellValueFactory(new PropertyValueFactory<>("delivery"));
        cl9.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
        //Enlazar columnas con atributos
        cl21.setCellValueFactory(new PropertyValueFactory<>("carta"));
        cl22.setCellValueFactory(new PropertyValueFactory<>("cant"));
        cl23.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cl24.setCellValueFactory(new PropertyValueFactory<>("sub"));
        //ComboBox
        cb1.setItems(listacb1);
        cb2.setItems(listacb2);
        //cb3.setItems(listacb3);
        cb4.setItems(listacb4);
        //Eventos
        txt_hor.setText(LocalTime.now().format(dtf));
        dt_fecha.setValue(LocalDate.now());
        gestionar_eventos();
        txt_num.requestFocus();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MPedidos>() {
            @Override
            public void changed(ObservableValue<? extends MPedidos> observableValue, MPedidos valorAnterior, MPedidos valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    dt_fecha.setValue(valorSeleccionado.getFecMun().toLocalDate());
                    txt_hor.setText(String.valueOf(valorSeleccionado.getHorMun()));
                    txt_total.setText(String.valueOf(valorSeleccionado.getTotalMun()));
                    txt_num.setText(String.valueOf(valorSeleccionado.getNumMun()));
                    cb1.setValue(valorSeleccionado.getMesas());
                    cb2.setValue(valorSeleccionado.getEstado());
                    cb3.setValue(valorSeleccionado.getDelivery());
                    cb4.setValue(valorSeleccionado.getFuncionario());
                    cb1.setDisable(false);
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                    cargarTb2(String.valueOf(valorSeleccionado.getCodMun()));
                }
            }
        });

        tb_segundaria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MPedidos_detalle>() {
            @Override
            public void changed(ObservableValue<? extends MPedidos_detalle> observableValue, MPedidos_detalle valorAnterior, MPedidos_detalle valorSeleccionado) {
                if (valorSeleccionado != null) {
                    cb5.setValue(valorSeleccionado.getCarta());
                    txt_cant.setText(String.valueOf(valorSeleccionado.getCant()));
                    txt_precio.setText(String.valueOf(valorSeleccionado.getPrecio()));
                    bt_editarD.setDisable(false);
                    bt_agregarD.setDisable(false);
                    bt_eliminarD.setDisable(false);
                    bt_buscarD.setDisable(false);
                }
            }
        });
    }

    public void cargarTb2(String cod) {
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbSegundaria.removeAll(tb_segundaria.getItems());
        //cargar tabla con resultado de la busqueda
        MPedidos_detalle.buscarRegistro(conexion.getConnection(),listatbSegundaria, cod);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void limpiarCampos() {
        //abre conexion
        conexion.iniciarConexion();
        //campos
        txt_cod.setText("");
        txt_hor.setText(LocalTime.now().format(dtf));
        dt_fecha.setValue(LocalDate.now());
        txt_total.setText("0");
        txt_num.setText(String.valueOf(MPedidos.ultNum(conexion.getConnection())));
        cb1.setValue(null);
        cb2.setValue(null);
        cb3.setValue(null);
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        txt_num.requestFocus();
        bt_agregarD.setDisable(false);
        bt_buscarD.setDisable(false);
        bt_eliminarD.setDisable(false);
        limpiarCamposD();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void guardarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        Integer idPed= ultCod();
        MPedidos a;
        MFuncionarios f;
        String st = "P";
        if (!cb4.getEditor().getText().isEmpty() && !cb3.getSelectionModel().isEmpty()){
            st="DF";
            String[] cbLpart = cb4.getEditor().getText().split(" - ");
            f = new MFuncionarios(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem(), f);
        } else if(!cb4.getEditor().getText().isEmpty() && cb3.getSelectionModel().isEmpty()){
            String[] cbLpart = cb4.getEditor().getText().split(" - ");
            st="F";
            f = new MFuncionarios(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem(), f);
        } else if (cb4.getEditor().getText().isEmpty() && !cb3.getSelectionModel().isEmpty()){
            st="D";
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem());
        }else {
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem());
        }
        if(!validacion()){ // realiza validacion de campos
            LOGGER.info("Informaciones no validas");
            pruebaguardarRegistro();
        } else {
            //abre conexion par
            conexion.iniciarConexion();
            if (!cb1.getSelectionModel().isEmpty()){ // validacion de mesa para cambiar su estado
                cb1.getSelectionModel().getSelectedItem().alterarEstadoMesaOff(conexion.getConnection());
                LOGGER.info("Mesa " +cb1.getSelectionModel().getSelectedItem().getDesMun()+ " pertenece a pedido: " + ultCod());
            }
            LOGGER.info("Validacin de cb = " +st);
            //llamar al metodo guardar de la clase pedidos
            int resultado = a.guardarRegistro(conexion.getConnection(), st);
            if (resultado == 1) {
                // si se guarda verifica cantidad de items para posteriormente guardar los itenes de detalle
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de acuerto la posicion
                    MPedidos_detalle b = new MPedidos_detalle(a, listatbSegundaria.get(i).getCarta(), listatbSegundaria.get(i).getCant(), listatbSegundaria.get(i).getPrecio(), listatbSegundaria.get(i).getSub());
                    if (listatbSegundaria.get(i).getCarta().getReceta()==null){ // validacion de receta para generar orden de cocina
                        System.out.println(">>no hay receta para orden de cocina<<");
                    } else {
                        MOrdenes_cocina o = new MOrdenes_cocina(a,listatbSegundaria.get(i).getCarta().getReceta(), listatbSegundaria.get(i).getCant(), Time.valueOf(LocalTime.now().format(dtf)));
                        System.out.println("guardando "+o+ " ordem cocina " + o.getCantidad() + " cantidad  ");
                    }
                    int result = b.guardarRegistro(conexion.getConnection());
                    if (result == 1){
                        LOGGER.info(" Producto: "+ b + " Pedido: " + b.getPedido().getCodMun() );
                    }
                }

            } else {
                mensaje.setTitle("Error");
                mensaje.setContentText("El registro no se pudo guardar el registro");
                mensaje.setHeaderText("Error al guardar");
                mensaje.show();
                }
            }
            listatbPrincipal.add(a);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro ha sido agregado correctamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
            limpiarCampos();
            this.bt_nuevo.requestFocus();
            //cerrar conexion
            conexion.cerrarConexion();
    }

    public void pruebaguardarRegistro() {
        Integer idPed= ultCod();
        MPedidos a;
        MFuncionarios f;
        String st = "P";
        if (!cb4.getEditor().getText().isEmpty() && !cb3.getSelectionModel().isEmpty()){
            st="DF";
            String[] cbLpart = cb4.getEditor().getText().split(" - ");
            f = new MFuncionarios(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem(), f);
        } else if(!cb4.getEditor().getText().isEmpty() && cb3.getSelectionModel().isEmpty()){
            String[] cbLpart = cb4.getEditor().getText().split(" - ");
            st="F";
            f = new MFuncionarios(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem(), f);
        } else if (cb4.getEditor().getText().isEmpty() && !cb3.getSelectionModel().isEmpty()){
            st="D";
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem());
        }else {
            a = new MPedidos(idPed, Date.valueOf(dt_fecha.getValue()), Time.valueOf(txt_hor.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                    cb2.getSelectionModel().getSelectedItem(), cb1.getSelectionModel().getSelectedItem());
        }
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {//abre conexion
            conexion.iniciarConexion();
            if (!cb1.getSelectionModel().isEmpty()){ //validacion de mesa
                LOGGER.info("Mesa " +cb1.getSelectionModel().getSelectedItem().getDesMun()+ " pertenece a pedido: " + idPed);
            }
            LOGGER.info("Validacin de cb = " +st);
            System.out.println("codigo P: "+a.getCodMun());
            System.out.println("numero P: "+a.getNumMun());
            System.out.println("fecha P: "+a.getFecMun());
            System.out.println("fecha P: "+a.getHorMun());
            System.out.println("total P: "+a.getTotalMun());
            System.out.println("total P: "+a.getEstado());
            System.out.println("total P: "+a.getMesas());
            System.out.println("total P: "+a.getDelivery());
            System.out.println("total P: "+a.getFuncionario());
                // verifica cantidad de items
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de acuerto la posicion
                    MPedidos_detalle b = new MPedidos_detalle(a, listatbSegundaria.get(i).getCarta(), listatbSegundaria.get(i).getCant(), listatbSegundaria.get(i).getPrecio(), listatbSegundaria.get(i).getSub());
                    if (listatbSegundaria.get(i).getCarta().getReceta()==null){
                        System.out.println(">>no hay receta para orden de cocina<<");
                    } else {
                        MOrdenes_cocina o = new MOrdenes_cocina(a,listatbSegundaria.get(i).getCarta().getReceta(), listatbSegundaria.get(i).getCant(), Time.valueOf(LocalTime.now().format(dtf)));
                        System.out.println(o + ">>cantidad " + o.getCantidad() + " ordem cocina <<");
                    }
                    LOGGER.info(" Producto: "+ b.getCarta().getProducto() + " pedido: " + b.getPedido());
                }
        }
        //cerrar conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void editarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo pedidos
        //Llama al metodo editar registro
        MPedidos a = new MPedidos(Integer.valueOf(txt_cod.getText()), Double.valueOf(txt_total.getText()), Integer.valueOf(txt_num.getText()),
                cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem());
        if(!validacion()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informaciones obligatorias");
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
        MPedidos m = new MPedidos();
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
        MPedidos.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void busquedaCar() {
        (new Thread(() -> {
            String filtro = this.cb5.getEditor().getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listacb5.removeAll(cb5.getItems());
            //cargar tabla con resultado de la busqueda
            MCartas.buscarRegreducido(conexion.getConnection(), listacb5, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
            cb5.setItems(listacb5);
        })).start();
    }

    public void busquedaFun() {
        (new Thread(() -> {
            String filtro = this.cb4.getEditor().getText();
            //abre conexion
            conexion.iniciarConexion();
            //remover itens de la tabla
            listacb4.removeAll(cb4.getItems());
            //cargar tabla con resultado de la busqueda
            MFuncionarios.buscarRegreducido(conexion.getConnection(),listacb4, filtro);
            //cerrar conexion
            conexion.cerrarConexion();
            cb4.setItems(listacb4);
            System.out.println(cb4.getSelectionModel().getSelectedItem());
        })).start();
    }

    public void setarPrecio(){
        if (!cb5.getSelectionModel().isEmpty()) {
            txt_precio.setText(String.valueOf(cb5.getSelectionModel().getSelectedItem().getPreCar()));
        }
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
        (new Thread(() -> {
            String rt = CPedidos.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.reportePedidos(tb_principal.getSelectionModel().getSelectedItem(), listatbSegundaria);
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.txt_total.getText().isEmpty() || this.txt_total.equals("")){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("campo: Total");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
         } else if(this.txt_num.getText().isEmpty() || this.txt_num.equals("")){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("campo: Numero");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
        } else{
            return true;
        }
    }

    public boolean validacionDet(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.cb5.getEditor().getText().isEmpty()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("campo: PRODUCTO");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
        } else if(this.txt_cant.getText().isEmpty() || this.txt_cant.getText() == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Campo: CANTIDAD");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
        } else if(this.txt_precio.getText().isEmpty() || this.txt_precio.getText() == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Campo: PRECIO");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
        }else {
            return true;
        }
    }

    public void abrirCartas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VCartasD.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stageCart = new Stage();
            CCartasD carta = (CCartasD) fxmlLoader.getController();
            carta.recibirPed(Cpedidos);
            bt_agregarD.setDisable(false);
            stageCart.setScene(new Scene(root1));
            stageCart.initModality(Modality.APPLICATION_MODAL);
            stageCart.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirDeliverys(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VDeliverys.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stageDel = new Stage();
            CDeliverys deliverys = (CDeliverys) fxmlLoader.getController();
            deliverys.recibirPed(Cpedidos);
            stageDel.setScene(new Scene(root1));
            stageDel.initModality(Modality.APPLICATION_MODAL);
            stageDel.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void limpiarCamposD(){
        cb5.setValue(null);
        txt_precio.setText("");
        txt_cant.setText("");
        cb5.requestFocus();
    }

    public void agregarD(){
        LOGGER.info("Se ha precionado el boton agregarD");
        if(!validacionDet()){
            LOGGER.info("Informaciones para detalle no validas");
        } else {
            conexion.iniciarConexion();
            double subtotal = (Double.parseDouble(txt_cant.getText()) * Double.parseDouble(txt_precio.getText()));
            double total;
            // por ser un combo box editable se debe obtener el texto del editor y separlo
            String[] cbLpart = cb5.getEditor().getText().split(" - ");
            System.out.println(cbLpart[0] + " || " + cbLpart[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            MCartas b = MCartas.obtenerCarta(conexion.getConnection(),cbLpart[0]);
            //crea una nueva instancia de Detalle Recetas
            MPedidos_detalle a = new MPedidos_detalle(b, Double.parseDouble(txt_cant.getText()), Double.valueOf(txt_precio.getText()), subtotal);
            System.out.println("cartaD: "+a.getCarta());
            System.out.println("Receta: "+a.getCarta().getReceta());
            System.out.println("Producto: "+a.getCarta().getProducto());
            listatbSegundaria.add(a);
            total = Double.parseDouble(txt_total.getText());
            txt_total.setText(String.valueOf(subtotal+total));
            LOGGER.info("El producto se ha agregado = " + a);
            bt_eliminarD.setDisable(false);
            bt_editarD.setDisable(false);
            this.bt_buscarD.requestFocus();
            limpiarCamposD();
            }
        }


    public void editarD(){
        LOGGER.info("Se ha precionado el boton editarD");
        if(!validacionDet()){
            LOGGER.info("Informaciones para detalle no validas");
        } else {
            // va agregar con las nuevas ediciones
            agregarD();
            // va remover la fila seleccionada
            listatbSegundaria.remove(tb_segundaria.getSelectionModel().getSelectedIndex());

        }
        bt_agregarD.setDisable(false);
    }

    public void eliminarD(){
        LOGGER.info("Se ha precionado el boton eliminar Detalle");
        //abre conexion
        conexion.iniciarConexion();
        listatbSegundaria.remove(tb_segundaria.getSelectionModel().getSelectedIndex());
        LOGGER.info("Se ha eliminado el Registro de la lista tabla segundaria");
        if (txt_cod.getText().equals("") || txt_cod.getText().isEmpty()) {
            LOGGER.info("No tiene registros en base de datos");
        } else {
            MPedidos_detalle a = new MPedidos_detalle(tb_segundaria.getSelectionModel().getSelectedItem().getPedido(), tb_segundaria.getSelectionModel().getSelectedItem().getCarta());
            int resultado = a.eliminarRegistro2(conexion.getConnection());
            LOGGER.info("El registro eliminado es =" + a.getCarta());
        }
        tb_segundaria.setItems(listatbSegundaria);
        subtotal();
        limpiarCamposD();
        this.cb5.requestFocus();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void subtotal(){
        double total = 0.0;
        if (tb_segundaria.getItems().size() != 0) {
            for (int i = 0; i < tb_segundaria.getItems().size(); i++) {
                total += listatbSegundaria.get(i).getSub();
            }
        }else {
            total = 0.0;
        }
    }

    public void recibirDatosCar(MCartas carta){
        cb5.setValue(carta);
        txt_precio.setText(String.valueOf(carta.getPreCar()));
    }
    public void recibirDatosDel(MDeliverys delivery){
        cb3.setValue(delivery);
    }
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