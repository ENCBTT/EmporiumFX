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
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vista.VEstablecimientos;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CFacturas implements Initializable {
    //globales
    public static String codProd;
    CFacturas Cfacturas;
    //columnas tabla
    @FXML
    private TableColumn<modelo.MFacturas, Integer> cl1;
    @FXML
    private TableColumn<modelo.MFacturas, Date> cl2;
    @FXML
    private TableColumn<modelo.MFacturas, MTimbrados> cl3;
    @FXML
    private TableColumn<modelo.MFacturas, MUsuarios> cl4;
    @FXML
    private TableColumn<modelo.MFacturas, MPersonas> cl5;
    @FXML
    private TableColumn<modelo.MFacturas, MClases_facturas> cl6;
    @FXML
    private TableColumn<modelo.MFacturas, MEstados> cl7;
    @FXML
    private TableColumn<modelo.MFacturas, MCondiciones_facturas> cl8;
    @FXML
    private TableColumn<modelo.MFacturas, Double> cl9;
    //Componentes GUI
    @FXML
    private Label lblUser;
    @FXML
    private Label lblNum;
    @FXML
    private Label lblTim;
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
    private TextField txt_numPed;
    @FXML
    private TextField txt_cliente;
    @FXML
    private TextField txt_buscar;
    @FXML
    private TextField txt_iva5;
    @FXML
    private TextField txt_iva10;
    @FXML
    private TextField txt_total;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private ComboBox<MClases_facturas> cb1;
    @FXML
    private ComboBox<MEstados> cb2;
    @FXML
    private ComboBox<MCondiciones_facturas> cb3;
    @FXML
    private ComboBox<MTimbrados> cb4;
    @FXML
    private TableView<modelo.MFacturas> tb_principal;
    @FXML
    private SplitPane stPane;
    @FXML
    private AnchorPane panel1;
    @FXML
    private AnchorPane panel2;
    @FXML
    private Pane panelPrincipal;
    //collecion
    ObservableList<MFacturas> listatbPrincipal;
    ObservableList<MFacturas_detalle> listatbSegundaria;
    ObservableList<MClases_facturas> listacbClase;
    ObservableList<MCondiciones_facturas> listacbCond;
    ObservableList<MEstados> listacbEstado;
    ObservableList<MTimbrados> listacbTim;
    //elementos detalle
    @FXML
    private TableView<MFacturas_detalle> tb_segundaria;
    @FXML
    private Button bt_agregarD;
    @FXML
    private Button bt_buscarD;
    @FXML
    private Button bt_eliminarD;
    @FXML
    private TextField txt_codPro;
    @FXML
    private TextField txt_descPro;
    @FXML
    private TextField txt_precio;
    @FXML
    private TextField txt_cant;
    // tabla segundaria
    @FXML
    private TableColumn<MFacturas_detalle, String> cl21;
    @FXML
    private TableColumn<MFacturas_detalle, Double> cl22;
    @FXML
    private TableColumn<MFacturas_detalle, Double> cl23;
    @FXML
    private TableColumn<MFacturas_detalle, Double> cl24;

    MAcceso datosUser;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CFacturas.class.getSimpleName());
    private ConexionBD conexion;

    DecimalFormat formato1 = new DecimalFormat("#.00");
    DecimalFormat formato2 = new DecimalFormat("#");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        Cfacturas = this;
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listatbSegundaria = FXCollections.observableArrayList();
        listacbCond = FXCollections.observableArrayList();
        listacbClase = FXCollections.observableArrayList();
        listacbEstado = FXCollections.observableArrayList();
        listacbTim = FXCollections.observableArrayList();
        //llamar metodos
        modelo.MFacturas.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        //MFacturas_detalle.obtenerInfoMun(conexion.getConnection(), listatbSegundaria);
        MEstados.obtenerInfoMun(conexion.getConnection(), listacbEstado);
        MClases_facturas.obtenerInfoMun(conexion.getConnection(), listacbClase);
        MCondiciones_facturas.obtenerInfoMun(conexion.getConnection(), listacbCond);
        MTimbrados.buscarRegistroFact(conexion.getConnection(), listacbTim);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        tb_segundaria.setItems(listatbSegundaria);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<>("codFact"));
        cl2.setCellValueFactory(new PropertyValueFactory<>("fecFact"));
        cl3.setCellValueFactory(new PropertyValueFactory<>("timbrado"));
        cl4.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        cl5.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        cl6.setCellValueFactory(new PropertyValueFactory<>("clase"));
        cl7.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cl8.setCellValueFactory(new PropertyValueFactory<>("condicion"));
        cl9.setCellValueFactory(new PropertyValueFactory<>("total"));
        //Enlazar columnas con atributos

        cl21.setCellValueFactory(new PropertyValueFactory<>("producto"));
        cl22.setCellValueFactory(new PropertyValueFactory<>("cant"));
        cl23.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cl24.setCellValueFactory(new PropertyValueFactory<>("sub"));
        //ComboBox
        cb1.setItems(listacbClase);
        cb2.setItems(listacbEstado);
        cb3.setItems(listacbCond);
        cb4.setItems(listacbTim);
        //Eventos
        gestionar_eventos();
        txt_cliente.requestFocus();
        dt_fecha.setValue(LocalDate.now());
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<modelo.MFacturas>() {
            @Override
            public void changed(ObservableValue<? extends modelo.MFacturas> observableValue, modelo.MFacturas valorAnterior, modelo.MFacturas valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodFact()));
                    txt_numPed.setText(String.valueOf(valorSeleccionado.getPedido()));
                    txt_cliente.setText(String.valueOf(valorSeleccionado.getCliente()));
                    dt_fecha.setValue(valorSeleccionado.getFecFact().toLocalDate());
                    txt_total.setText(String.valueOf(valorSeleccionado.getTotal()));
                    txt_iva10.setText(String.valueOf(valorSeleccionado.getIva()));
                    cb1.setValue(valorSeleccionado.getClase());
                    cb2.setValue(valorSeleccionado.getEstado());
                    cb3.setValue(valorSeleccionado.getCondicion());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                }
            }
        });

        tb_segundaria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<modelo.MFacturas_detalle>() {
            @Override
            public void changed(ObservableValue<? extends modelo.MFacturas_detalle> observableValue, modelo.MFacturas_detalle valorAnterior, modelo.MFacturas_detalle valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_codPro.setText(String.valueOf(valorSeleccionado.getProducto().getCodMun()));
                    txt_descPro.setText(valorSeleccionado.getProducto().getNomMun());
                    txt_precio.setText(String.valueOf(valorSeleccionado.getPrecio()));
                    txt_cant.setText(String.valueOf(valorSeleccionado.getCant()));

                }
            }
        });

    }

    public void datosUser(MAcceso datosUsuario){
        lblUser.setText(datosUsuario.getLogin());
        datosUser = datosUsuario; // Mejora observada, cargar estos datos en un objeto de tipo usuario o acceso con las evaluaciones de Permisos de usuarios
        System.out.println(datosUser.getLogin() + " - " +datosUser.getCodUser());
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText("");
        dt_fecha.setValue(LocalDate.now());
        txt_numPed.setText("");
        txt_cliente.setText("");
        cb1.setValue(null);
        cb2.setValue(null);
        cb3.setValue(null);
        cb4.setValue(null);
        txt_codPro.setText("");
        txt_descPro.setText("");
        txt_precio.setText("");
        txt_cant.setText("");
        txt_iva5.setText("0");
        txt_iva10.setText("0");
        txt_total.setText("0");
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        bt_agregarD.setDisable(false);
        bt_buscarD.setDisable(false);
        bt_eliminarD.setDisable(false);
        txt_cliente.requestFocus();
    }

    @FXML
    public void guardarRegistro() {
        LOGGER.info("Se ha precionado el boton guardar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            Integer idFact = ultCod(); // genera el ultimo codigo
            //abre conexion
            conexion.iniciarConexion();
            //crea una nueva instancia del tipo facturas
            MFacturas a = new modelo.MFacturas(idFact, Double.parseDouble(txt_iva10.getText() + Double.parseDouble(txt_iva5.getText())), Double.parseDouble(txt_total.getText()),
                Date.valueOf(dt_fecha.getValue()), Date.valueOf(LocalDate.now()), MPedidos.obtenerPedido(conexion.getConnection(), txt_numPed.getText()), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb4.getSelectionModel().getSelectedItem(),
                cb3.getSelectionModel().getSelectedItem(), MClientes.obtenerCliente(conexion.getConnection(), txt_cliente.getText()));
            //llamar al metodo guardar de la clase facturas
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                MTimbrado_detalle t = new modelo.MTimbrado_detalle(cb4.getSelectionModel().getSelectedItem(), datosUser.getEstablecimiento(), datosUser.getPunto_expedicion(), new
                        MTipos_documentos(1, "Factura"), idFact);
                t.guardarConFact(conexion.getConnection());
                listatbPrincipal.add(a);
                // inresa detalle de acuerdo al indice
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de acuerto la posicion
                    MFacturas_detalle b = new MFacturas_detalle(a, listatbSegundaria.get(i).getProducto(), listatbSegundaria.get(i).getCant(), listatbSegundaria.get(i).getPrecio(), listatbSegundaria.get(i).getSub());
                    int result = b.guardarRegistro(conexion.getConnection());

                    if (result == 1){
                        System.out.println(" Producto: "+ listatbSegundaria.get(i).getProducto().getCodMun() + " Factura: " + idFact );
                    }
                }
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.setHeaderText("Resultado:");
                mensaje.show();
                LOGGER.info("El registro se ha guardado cod. = " +a.getCodFact());
                limpiarCampos();
                this.bt_nuevo.requestFocus();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    public void pruebaGuardarRegistro() {
        LOGGER.info("Se ha precionado el boton guardar Prueba");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            Integer idFact = ultCod(); // genera el ultimo codigo
            //abre conexion
            conexion.iniciarConexion();
            //crea una nueva instancia del tipo facturas
            MPedidos p = MPedidos.obtenerPedido(conexion.getConnection(), txt_numPed.getText());
            MClientes c = MClientes.obtenerCliente(conexion.getConnection(), txt_cliente.getText());

            MFacturas a = new modelo.MFacturas(idFact, Double.valueOf(txt_iva10.getText()), Double.valueOf(txt_total.getText()),
                    Date.valueOf(dt_fecha.getValue()), Date.valueOf(LocalDate.now()), p, new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                    cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb4.getSelectionModel().getSelectedItem(),
                    cb3.getSelectionModel().getSelectedItem(), c);
            System.out.println("||Prueba Cabecera||");
            System.out.println("Clase factura: "+a.getClase());
            System.out.println("id fact: "+a.getCodFact());
            System.out.println("cliente: "+a.getCliente());
            System.out.println("Pedido: "+a.getPedido());
            System.out.println("concidicon: "+a.getCondicion());  // teste cabecera y envio de datos para timbrado detalle OK, falta verificar si va generar  seceuncia de timbrado
            System.out.println("fecha: "+a.getFecConf());
            System.out.println("IVA: "+a.getIva());
            System.out.println("Timbrado: "+a.getTimbrado());
            System.out.println("Total: "+a.getTotal());
            System.out.println("Usuario: "+a.getUsuario());
            MTimbrado_detalle t = new modelo.MTimbrado_detalle(cb4.getSelectionModel().getSelectedItem(), datosUser.getEstablecimiento(), datosUser.getPunto_expedicion(), new
                    MTipos_documentos(1, "Factura"), idFact);
            System.out.println("||Prueba Detalle Timbrado||");
            System.out.println("Timbrado de detalle: " + t.getTimbrados());
            System.out.println("Expedicion: "+t.getExpedicion());
            System.out.println("Establecimiento: "+t.getEstablecimientos());
            System.out.println("Documento: "+t.getTipos_documentos());
            System.out.println("id factura: "+t.getCodFact());


            System.out.println("||Prueba de detalle y ajuste Stock||");
            for (int i =0; i <tb_segundaria.getItems().size(); i++){
                //genera objeto de tipo detalle y agrega de acuerto la posicion
                MFacturas_detalle b = new MFacturas_detalle(a, listatbSegundaria.get(i).getProducto(), listatbSegundaria.get(i).getCant(), listatbSegundaria.get(i).getPrecio(), listatbSegundaria.get(i).getSub());
                MAjustes_stock s = new modelo.MAjustes_stock(datosUser.getEstablecimiento(), listatbSegundaria.get(i).getProducto(), new MTipos_ajustes(2,"Salida por facturación"),
                        new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()), Date.valueOf(dt_fecha.getValue()), "FACT. " + idFact, listatbSegundaria.get(i).getCant());
                System.out.println("fact D:" +b.getFactura().getCodFact());
                System.out.println("Producto D:" +b.getProducto());
                System.out.println("cantidad D:" +b.getCant());
                System.out.println("Precio D:" +b.getPrecio());
                System.out.println("sub total D:" +b.getSub());
                System.out.println("||Prueba de ajuste Stock||");
                System.out.println("Establecimiento As: "+s.getEstablecimiento());
                System.out.println("Producto As: "+s.getProducto());
                System.out.println("Tipo de ajuste As: "+s.getTipo_ajuste());
                System.out.println("Usuario As: "+s.getUsuario());
                System.out.println("fecha As: "+s.getFecAs());
                System.out.println("Establecimiento As: "+s.getHisAs());


            //llamar al metodo guardar de la clase facturas
                MTimbrado_detalle tim = new modelo.MTimbrado_detalle(cb4.getSelectionModel().getSelectedItem(), datosUser.getEstablecimiento(), datosUser.getPunto_expedicion(), new
                        MTipos_documentos(1, "Factura"), idFact);
                t.guardarConFact(conexion.getConnection());

            }
        }
    }

    @FXML
    public void editarRegistro() {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo facturas
        //Llama al metodo editar registro
        MFacturas a = new modelo.MFacturas(Integer.valueOf(txt_cod.getText()), Double.valueOf(txt_iva10.getText()), Double.valueOf(txt_total.getText()),
                Date.valueOf(dt_fecha.getValue()), Date.valueOf(dt_fecha.getValue()),
                0, Integer.valueOf(txt_numPed.getText()), 1, Integer.valueOf(txt_cliente.getText()), cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem());
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase facturas
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
            int resultado = tb_principal.getSelectionModel().getSelectedItem().anularRegistro(conexion.getConnection());
            if (resultado == 1) {
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de acuerto la posicion
                    MFacturas_detalle b = tb_segundaria.getItems().get(i);
                    resultado += b.anularRegistro(conexion.getConnection());

                        System.out.println(" Producto: "+ tb_segundaria.getItems().get(i).getProducto().getCodMun() + " Factura: " + tb_segundaria.getItems().get(i).getFactura().getCodFact());

                }
            }else if (resultado >= 2)
                mensaje.setTitle("Registro anulado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El registro ha sido anulado correctamente");
                mensaje.show();
                limpiarCampos();
                this.bt_nuevo.requestFocus();
                //cerrar conexion
                conexion.cerrarConexion();
        }
    }

    public int ultCod(){
        //abre conexion
        conexion.iniciarConexion();
        modelo.MFacturas m = new modelo.MFacturas();
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
        MFacturas.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
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

    public void abrirClientes(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VClientesD.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stageCli = new Stage();
            CClientesD cClientesDinst = (CClientesD) fxmlLoader.getController();
            cClientesDinst.recibir(Cfacturas);
            stageCli.setScene(new Scene(root1));
            stageCli.initModality(Modality.APPLICATION_MODAL);
            stageCli.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void abrirProductos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VProductosD.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stageProd = new Stage();
            CProductosD cProductosDinst = (CProductosD) fxmlLoader.getController();
            cProductosDinst.recibir(Cfacturas);
            stageProd.setScene(new Scene(root1));
            stageProd.initModality(Modality.APPLICATION_MODAL);
            stageProd.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirPedidos(){
       /* try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VPedidos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stageCli = new Stage();
            CPedidosD cPedidosDinst = (CProductosD) fxmlLoader.getController();
            cPedidosDinst.recibir(Cfacturas);
            stageCli.setScene(new Scene(root1));
            stageCli.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void imprimir(){


    }

    public boolean validacion(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.txt_cliente.getText().isEmpty() || this.txt_cliente == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Faltan informacion obligatoria");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
            return false;
         } else {
            return true;
        }

    }

    public boolean validacionDet(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(txt_codPro.getText().isEmpty() || this.txt_codPro.getText() == null){
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

    public void limpiarCamposD(){
        txt_descPro.setText("");
        txt_codPro.setText("");
        txt_precio.setText("");
        txt_cant.setText("");
    }

    public void agregarDetalle(){
        double subtotal = (Double.parseDouble(txt_cant.getText()) * Double.parseDouble(txt_precio.getText()));
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //verificacion de saldo
        conexion.iniciarConexion();
        MProductos pro = MProductos.obtenerProDet(conexion.getConnection(), txt_codPro.getText());
        if (!validacionDet()){
            LOGGER.info("informaciones de detalle no validas");
        }else if (pro.verificarSaldoDetalle(conexion.getConnection(), txt_codPro.getText(), txt_cant.getText())){
            //crea una nueva instancia del tipo detalle facturas
            MFacturas_detalle a = new MFacturas_detalle(pro, Double.valueOf(txt_cant.getText()), Double.valueOf(txt_precio.getText()), subtotal);
            double total;
                LOGGER.info("Agregando detalle");
                total = Double.parseDouble(txt_total.getText());
            txt_total.setText(String.valueOf(subtotal+total));
            switch (pro.getIvaMun()) {
                case 10 -> txt_iva10.setText(String.valueOf(formato2.format(Double.parseDouble(txt_iva10.getText()) + (subtotal / 11))));
                case 5 -> txt_iva5.setText(String.valueOf(formato2.format(Double.parseDouble(txt_iva5.getText()) + (total / 21))));
            }
            listatbSegundaria.add(a);
            limpiarCamposD();
            this.bt_buscarD.requestFocus();
        } else {
            mensaje.setTitle("Saldo en stock");
            mensaje.setContentText("Campo: Cantidad");
            mensaje.setHeaderText("Saldo de producto indisponible");
            mensaje.show();
        }
        conexion.cerrarConexion();
    }


    public void editarDetalle(){}

    public void eliminarD(){
        LOGGER.info("Eliminando detalle");
        double total = Double.parseDouble(txt_total.getText());
        double sub = tb_segundaria.getSelectionModel().getSelectedItem().getSub();
        txt_total.setText(String.valueOf(total-sub));
        switch (tb_segundaria.getSelectionModel().getSelectedItem().getProducto().getIvaMun()) {
            case 10 -> txt_iva10.setText(String.valueOf(Double.parseDouble(txt_iva10.getText()) - (tb_segundaria.getSelectionModel().getSelectedItem().getSub() / 11)));
            case 5 -> txt_iva5.setText(String.valueOf(Double.parseDouble(txt_iva5.getText()) - (tb_segundaria.getSelectionModel().getSelectedItem().getSub() / 21)));
        }
        listatbSegundaria.remove(tb_segundaria.getSelectionModel().getSelectedIndex());
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

    public void recibirDatosPro(String cod, String nom, String precio){
        txt_codPro.setText(cod);
        txt_precio.setText(precio);
        txt_descPro.setText(nom);
    }

    public void recibirDatosCli(String cod, String nom){
        txt_cliente.setText(cod);
        //txt_descPro.setText(nom);
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