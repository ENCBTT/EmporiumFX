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
import modelo.MFacturas_compras;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CFacturas_compras implements Initializable {
    //globales
    CFacturas_compras facturas_compras;
    //columnas tabla
    @FXML
    private TableColumn<MFacturas_compras, Integer> cl1;
    @FXML
    private TableColumn<MFacturas_compras, Date> cl2;
    @FXML
    private TableColumn<MFacturas_compras, String> cl3;
    @FXML
    private TableColumn<MFacturas_compras, String> cl4;
    @FXML
    private TableColumn<MFacturas_compras, MUsuarios> cl5;
    @FXML
    private TableColumn<MFacturas_compras, MPersonas> cl6;
    @FXML
    private TableColumn<MFacturas_compras, MClases_facturas> cl7;
    @FXML
    private TableColumn<MFacturas_compras, MEstados> cl8;
    @FXML
    private TableColumn<MFacturas_compras, MCondiciones_facturas> cl9;
    @FXML
    private TableColumn<MFacturas_compras, Double> cl10;
    //Componentes GUI
    @FXML
    private Label lblUser;
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
    private TextField txt_num;
    @FXML
    private TextField txt_tim;
    @FXML
    private TextField txt_prov;
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
    private ComboBox<MClases_facturas> cb1;
    @FXML
    private ComboBox<MEstados> cb2;
    @FXML
    private ComboBox<MCondiciones_facturas> cb3;
    @FXML
    private TableView<MFacturas_compras> tb_principal;
    @FXML
    private SplitPane stPane;
    //collecion
    ObservableList<MFacturas_compras> listatbPrincipal;
    ObservableList<MCompras_detalle> listatbSegundaria;
    ObservableList<MClases_facturas> listacbClase;
    ObservableList<MCondiciones_facturas> listacbCond;
    ObservableList<MEstados> listacbEstado;
    //elementos detalle
    @FXML
    private TableView<MCompras_detalle> tb_segundaria;
    @FXML
    private Button bt_buscarD;
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
    private TableColumn<MCompras_detalle, MProductos> cl21;
    @FXML
    private TableColumn<MCompras_detalle, Double> cl22;
    @FXML
    private TableColumn<MCompras_detalle, Double> cl23;
    @FXML
    private TableColumn<MCompras_detalle, Double> cl24;

    MAcceso datosUser;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CFacturas_compras.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        facturas_compras = this;
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listatbSegundaria = FXCollections.observableArrayList();
        listacbCond = FXCollections.observableArrayList();
        listacbClase = FXCollections.observableArrayList();
        listacbEstado = FXCollections.observableArrayList();
        //llamar metodos
        MFacturas_compras.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        //MCompras_detalle.obtenerInfoMun(conexion.getConnection(), listatbSegundaria);
        MEstados.obtenerInfoMun(conexion.getConnection(), listacbEstado);
        MClases_facturas.obtenerInfoMun(conexion.getConnection(), listacbClase);
        MCondiciones_facturas.obtenerInfoMun(conexion.getConnection(), listacbCond);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        tb_segundaria.setItems(listatbSegundaria);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<>("codFact"));
        cl2.setCellValueFactory(new PropertyValueFactory<>("fecFact"));
        cl3.setCellValueFactory(new PropertyValueFactory<>("numTim"));
        cl4.setCellValueFactory(new PropertyValueFactory<>("numCom"));
        cl5.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        cl6.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        cl7.setCellValueFactory(new PropertyValueFactory<>("clase"));
        cl8.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cl9.setCellValueFactory(new PropertyValueFactory<>("condicion"));
        cl10.setCellValueFactory(new PropertyValueFactory<>("total"));
        //Enlazar columnas con atributos
        cl21.setCellValueFactory(new PropertyValueFactory<>("productos"));
        cl22.setCellValueFactory(new PropertyValueFactory<>("cant"));
        cl23.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cl24.setCellValueFactory(new PropertyValueFactory<>("sub"));
        //ComboBox
        cb1.setItems(listacbClase);
        cb2.setItems(listacbEstado);
        cb3.setItems(listacbCond);
        //Eventos
        gestionar_eventos();
        txt_prov.requestFocus();
        dt_fecha.setValue(LocalDate.now());
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MFacturas_compras>() {
            @Override
            public void changed(ObservableValue<? extends MFacturas_compras> observableValue, MFacturas_compras valorAnterior, MFacturas_compras valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodFact()));
                    txt_prov.setText(String.valueOf(valorSeleccionado.getProveedor()));
                    dt_fecha.setValue(valorSeleccionado.getFecFact().toLocalDate());
                    txt_total.setText(String.valueOf(valorSeleccionado.getTotal()));
                    txt_iva10.setText(String.valueOf(valorSeleccionado.getIva()));
                    cb1.setValue(valorSeleccionado.getClase());
                    cb2.setValue(valorSeleccionado.getEstado());
                    cb3.setValue(valorSeleccionado.getCondicion());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                    cargarTb2(String.valueOf(valorSeleccionado.getCodFact()));
                }
            }
        });

        tb_segundaria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCompras_detalle>() {
            @Override
            public void changed(ObservableValue<? extends MCompras_detalle> observableValue, MCompras_detalle valorAnterior, MCompras_detalle valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_codPro.setText(String.valueOf(valorSeleccionado.getProductos().getCodMun()));
                    txt_descPro.setText(valorSeleccionado.getProductos().getNomMun());
                    txt_precio.setText(String.valueOf(valorSeleccionado.getPrecio()));
                    txt_cant.setText(String.valueOf(valorSeleccionado.getCant()));

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
        MCompras_detalle.buscarRegistro(conexion.getConnection(),listatbSegundaria, cod);
        //cerrar conexion
        conexion.cerrarConexion();
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
        txt_num.setText("");
        txt_tim.setText("");
        txt_prov.setText("");
        txt_codPro.setText("");
        txt_descPro.setText("");
        txt_precio.setText("");
        txt_cant.setText("");
        txt_iva5.setText("0");
        txt_iva10.setText("0");
        txt_total.setText("0");
        cb1.setValue(null);
        cb2.setValue(null);
        cb3.setValue(null);
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        txt_prov.requestFocus();
        limpiarCamposD();
    }

    @FXML
    public void guardarRegistro() {
        LOGGER.info("Se ha precionado el boton guardar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            pruebaGuardarRegistro();
            Integer idFact = ultCod(); // genera el ultimo codigo
            //abre conexion
            conexion.iniciarConexion();
            //crea una nueva instancia del tipo facturas
            MProveedores c = MProveedores.obtenerProveedor(conexion.getConnection(), txt_prov.getText());
            MFacturas_compras a = new MFacturas_compras(idFact, txt_tim.getText(), txt_num.getText(), Date.valueOf(dt_fecha.getValue()), Date.valueOf(LocalDate.now()),
                    Double.valueOf(txt_total.getText()), Double.valueOf(txt_iva10.getText())+Double.valueOf(txt_iva5.getText()), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                    cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem(), c);
            //llamar al metodo guardar de la clase facturas
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                listatbPrincipal.add(a);
                // inresa detalle de acuerdo al indice
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de acuerto la posicion
                    MCompras_detalle b = new MCompras_detalle(a, listatbSegundaria.get(i).getProductos(),listatbSegundaria.get(i).getCant(),listatbSegundaria.get(i).getPrecio(), listatbSegundaria.get(i).getSub());
                    MAjustes_stock s = new MAjustes_stock(MAjustes_stock.ultRegistro(conexion.getConnection()), datosUser.getEstablecimiento(), listatbSegundaria.get(i).getProductos(), new MTipos_ajustes(1,"Entrada por facturación"),
                            new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()), Date.valueOf(dt_fecha.getValue()), "FACT. " + idFact, listatbSegundaria.get(i).getCant(), a.getCodFact());
                    MAjustes_precios p = new MAjustes_precios(listatbSegundaria.get(i).getProductos(), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                            "FACT. " + idFact, Date.valueOf(dt_fecha.getValue()), listatbSegundaria.get(i).getPrecio(), 0);
                    //ejecutar metodos de guardar registros
                    int result = b.guardarRegistro(conexion.getConnection());
                    result += s.guardarRegistroFact(conexion.getConnection());
                    result+= p.guardarRegistroFact(conexion.getConnection());
                    if (result == 3){
                        System.out.println(" Producto: "+ listatbSegundaria.get(i).getProductos() + " Factura: " + idFact );
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
        LOGGER.info("|| --Ejecutando guardar Prueba-- ||");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            Integer idFact = ultCod(); // genera el ultimo codigo
            //abre conexion
            conexion.iniciarConexion();
            //crea una nueva instancia del tipo facturas
            MProveedores c = MProveedores.obtenerProveedor(conexion.getConnection(), txt_prov.getText());
            MFacturas_compras a = new MFacturas_compras(idFact, txt_tim.getText(), txt_num.getText(), Date.valueOf(dt_fecha.getValue()), Date.valueOf(LocalDate.now()),
                    Double.valueOf(txt_total.getText()), Double.valueOf(txt_iva10.getText())+Double.valueOf(txt_iva5.getText()), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                    cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem(), c);

            System.out.println("||Prueba Cabecera||");
            System.out.println("id fact: "+a.getCodFact());
            System.out.println("Timbrado: "+a.getNumTim() +" - " + a.getNumCom());
            System.out.println("fecha: "+a.getFecConf());
            System.out.println("Total: "+a.getTotal());
            System.out.println("IVA: "+a.getIva());
            System.out.println("Usuario: "+a.getUsuario());
            System.out.println("Clase factura: "+a.getClase());
            System.out.println("Estado: " + a.getEstado());
            System.out.println("concidicon: "+a.getCondicion());  // teste cabecera y envio de datos para timbrado detalle OK
            System.out.println("proveedor: "+a.getProveedor());
            for (int i =0; i <tb_segundaria.getItems().size(); i++){
                //genera objeto de tipo detalle y agrega de acuerto la posicion
                System.out.println("||Prueba de detalle");
                MCompras_detalle b = new MCompras_detalle(a, listatbSegundaria.get(i).getProductos(),listatbSegundaria.get(i).getCant(),listatbSegundaria.get(i).getPrecio(), listatbSegundaria.get(i).getSub());
                System.out.println("fact D:" +b.getCompras().getCodFact());
                System.out.println("Producto D:" +b.getProductos());
                System.out.println("cantidad D:" +b.getCant());
                System.out.println("Precio D:" +b.getPrecio());
                System.out.println("sub total D:" +b.getSub());
                System.out.println("||Prueba de ajuste Stock||");
                MAjustes_stock s = new MAjustes_stock(MAjustes_stock.ultRegistro(conexion.getConnection()), datosUser.getEstablecimiento(), listatbSegundaria.get(i).getProductos(), new MTipos_ajustes(1,"Entrada por facturación"),
                        new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()), Date.valueOf(dt_fecha.getValue()), "FACT. " + idFact, listatbSegundaria.get(i).getCant(), a.getCodFact());
                System.out.println("codigo As: "+s.getCodAs());
                System.out.println("Establecimiento As: "+s.getEstablecimiento());
                System.out.println("Producto As: "+s.getProducto());
                System.out.println("Tipo de ajuste As: "+s.getTipo_ajuste());
                System.out.println("Usuario As: "+s.getUsuario());
                System.out.println("fecha As: "+s.getFecAs());
                System.out.println("Establecimiento As: "+s.getHisAs());
                System.out.println("Cantidad: "+s.getCanAs());
                System.out.println("||Prueba de ajuste de precio||");
                MAjustes_precios p = new MAjustes_precios(listatbSegundaria.get(i).getProductos(), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                       "FACT. " + idFact, Date.valueOf(dt_fecha.getValue()), listatbSegundaria.get(i).getPrecio(), 0);
                System.out.println("Producto AP: "+p.getProducto());
                System.out.println("Usuario AP: "+p.getUsuario());
                System.out.println("Historico AP: "+p.getHisAp());
                System.out.println("Fecha AP: "+p.getFecAp());
                System.out.println("Valor AP: "+p.getValAp());
                System.out.println("Tipo precio AP: "+p.getTprAp());

                }
        }
    }

    @FXML
    public void editarRegistro() {
     Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo facturas
        //Llama al metodo editar registro
        MProveedores c = MProveedores.obtenerProveedor(conexion.getConnection(), txt_prov.getText());
        MFacturas_compras a = new MFacturas_compras(Integer.valueOf(txt_cod.getText()), txt_tim.getText(), txt_num.getText(), Date.valueOf(dt_fecha.getValue()), Date.valueOf(LocalDate.now()),
                Double.valueOf(txt_total.getText()), Double.valueOf(txt_iva10.getText())+Double.valueOf(txt_iva5.getText()), new MUsuarios(datosUser.getCodUser(), datosUser.getLogin()),
                cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem(), cb3.getSelectionModel().getSelectedItem(), c);
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
                    MCompras_detalle b = tb_segundaria.getItems().get(i);
                    resultado += b.anularRegistro(conexion.getConnection());
                        System.out.println(" Producto: "+ tb_segundaria.getItems().get(i).getProductos().getCodMun() + " Factura: " + tb_segundaria.getItems().get(i).getCompras().getCodFact());
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
        MFacturas_compras m = new MFacturas_compras();
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
        MFacturas_compras.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
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

    public void abrirProveedores(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VProveedoresD.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stageProv = new Stage();
            CProveedoresD proveedor = fxmlLoader.getController();
            proveedor.recibirFC(facturas_compras);
            stageProv.setScene(new Scene(root1));
            stageProv.initModality(Modality.APPLICATION_MODAL);
            stageProv.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void abrirProductos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VProductosD.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stageProd = new Stage();
            CProductosD cProductosDinst = fxmlLoader.getController();
            cProductosDinst.recibirFC(facturas_compras);
            stageProd.setScene(new Scene(root1));
            stageProd.initModality(Modality.APPLICATION_MODAL);
            stageProd.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirPedidos_compra(){
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
        if(this.txt_prov.getText().isEmpty() || this.txt_prov.equals("")){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Falta informacion campo Proveedor");
            mensaje.setHeaderText("Falta información");
            mensaje.show();
            return false;
         } else if (this.txt_tim.getText().isEmpty() || this.txt_tim.equals("") || this.txt_num.getText().isEmpty() || this.txt_num.equals("")){
                mensaje.setTitle("Faltan datos");
                mensaje.setContentText("Falta información campo Timbrado o Numero Comprobante");
                mensaje.setHeaderText("Falta información");
                mensaje.show();
            return false;
        } else if (this.cb1.getSelectionModel().isEmpty()){
                mensaje.setTitle("Faltan datos");
                mensaje.setContentText("Falta seleccionar Clase factura");
                mensaje.setHeaderText("Falta información");
                mensaje.show();
            return false;
        }else if (this.cb2.getSelectionModel().isEmpty()){
                mensaje.setTitle("Faltan datos");
                mensaje.setContentText("Falta seleccionar Estado");
                mensaje.setHeaderText("Falta información");
                mensaje.show();
            return false;
        } else if (this.cb3.getSelectionModel().isEmpty()) {
                mensaje.setTitle("Faltan datos");
                mensaje.setContentText("Falta seleccionar Condicion");
                mensaje.setHeaderText("Falta información");
                mensaje.show();
            return false;
        } else if(tb_segundaria.getItems().size()<1){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Falta agregar productos");
            mensaje.setHeaderText("Falta información");
            mensaje.show();
            return false;
        }else {
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
        LOGGER.info("Se ha precionado el boton agregar detalle");
        conexion.iniciarConexion();
        if (!validacionDet()){
            LOGGER.info("informaciones de detalle no validas");
        }else {
            MProductos pro = MProductos.obtenerProDet(conexion.getConnection(), txt_codPro.getText());
            System.out.println(pro.getIvaMun());
            //crea una nueva instancia del tipo detalle facturas
            MCompras_detalle a = new MCompras_detalle(pro,
                    Double.valueOf(txt_cant.getText()), Double.valueOf(txt_precio.getText()), subtotal);
            double total;
            LOGGER.info("Agregando detalle");
            total = Double.parseDouble(txt_total.getText());
            txt_total.setText(String.valueOf(subtotal+total));
            switch (pro.getIvaMun()) {
                case 10 -> txt_iva10.setText(String.valueOf(Double.parseDouble(txt_iva10.getText()) + (subtotal / 11)));
                case 5 -> txt_iva5.setText(String.valueOf(Double.parseDouble(txt_iva5.getText()) + (total / 21)));
            }
            listatbSegundaria.add(a);
            limpiarCamposD();
            this.bt_buscarD.requestFocus();
        }
        conexion.cerrarConexion();
    }


    public void editarDetalle(){}

    public void eliminarD(){
        LOGGER.info("Eliminando detalle");
        double total = Double.parseDouble(txt_total.getText());
        double sub = tb_segundaria.getSelectionModel().getSelectedItem().getSub();
            txt_total.setText(String.valueOf(total-sub));
        switch (tb_segundaria.getSelectionModel().getSelectedItem().getProductos().getIvaMun()) {
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

    public void recibirDatosPro(String cod, String nom){
        txt_codPro.setText(cod);
        txt_descPro.setText(nom);
    }

    public void recibirDatosProv(String cod, String nom){
        txt_prov.setText(cod +" - "+ nom);
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