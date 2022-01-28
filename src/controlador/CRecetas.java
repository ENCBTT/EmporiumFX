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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CRecetas implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MRecetas, Integer> cl1;
    @FXML
    private TableColumn<MRecetas, String> cl2;
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
    private TableView<MRecetas> tb_principal;
    //collecion
    ObservableList<MRecetas> listatbPrincipal;
    ObservableList<MRecetas_detalles> listatbSegundaria;
    ObservableList<MProductos> listacbPro;
    //elementos detalle
    @FXML
    private TableView<MRecetas_detalles> tb_segundaria;
    @FXML
    private Button bt_agregarD;
    @FXML
    private Button bt_editarD;
    @FXML
    private Button bt_eliminarD;
    @FXML
    private Button bt_guardarD;
    @FXML
    private ComboBox<MProductos> cbPro;
    @FXML
    private TextField txt_can;
    // tabla segundaria
    @FXML
    private TableColumn<MRecetas_detalles, MProductos> cl21;
    @FXML
    private TableColumn<MRecetas_detalles, Double> cl22;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CRecetas.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listatbSegundaria = FXCollections.observableArrayList();
        listacbPro = FXCollections.observableArrayList();
        //llamar metodos
        MRecetas.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        //MRecetas_detalles.obtenerInfoMun(conexion.getConnection(), listatbSegundaria);
        MProductos.obtenerInfoMun(conexion.getConnection(),listacbPro);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        tb_segundaria.setItems(listatbSegundaria);
        //Enlazar tb1 columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<MRecetas, Integer>("codMun"));
        cl2.setCellValueFactory(new PropertyValueFactory<MRecetas, String>("desMun"));
        //Enlazar tb2 columnas con atributos
        cl21.setCellValueFactory(new PropertyValueFactory<MRecetas_detalles, MProductos>("producto"));
        cl22.setCellValueFactory(new PropertyValueFactory<MRecetas_detalles, Double>("cantidad"));
        //ComboBox
        cbPro.setItems(listacbPro);
        //Eventos
        gestionar_eventos();
        limpiarCampos();
        txt_des.requestFocus();
        bt_guardar.setDisable(true);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MRecetas>() {
            @Override
            public void changed(ObservableValue<? extends MRecetas> observableValue, MRecetas valorAnterior, MRecetas valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodMun()));
                    txt_des.setText(valorSeleccionado.getDesMun());
                    cargarTb2(String.valueOf(valorSeleccionado.getCodMun()));
                    bt_editar.setDisable(false);
                    bt_agregarD.setDisable(false);
                }
            }
        });

        tb_segundaria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MRecetas_detalles>() {
            @Override
            public void changed(ObservableValue<? extends MRecetas_detalles> observableValue, MRecetas_detalles valorAnterior, MRecetas_detalles valorSeleccionado) {
                if (valorSeleccionado != null) {
                    cbPro.setValue(valorSeleccionado.getProducto());
                    txt_can.setText(String.valueOf(valorSeleccionado.getCantidad()));
                    bt_agregarD.setDisable(true);
                    bt_editarD.setDisable(false);
                }
            }
        });

        // tambien se puede hacer evento de change listener para logs
    }


    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText(null);
        txt_des.setText("");
        txt_can.setText("");
        cbPro.setValue(null);
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        txt_des.requestFocus();
        //limpiar tb segundaria
        listatbSegundaria.removeAll(tb_segundaria.getItems());


    }

    @FXML
    public void nuevo(){
        LOGGER.info("Se ha precionado el boton nuevo");
        limpiarCampos();
    }

    @FXML
    public void guardarRegistro() {
        LOGGER.info("Se ha precionado el boton guardar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia de recetas
        MRecetas a = new MRecetas(ultCod(), txt_des.getText());
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase recetas
            int resultado = a.guardarRegistro(conexion.getConnection());
            if (resultado == 1) {
                // verifica cantidad de items
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de acuerto la posicion index,
                    MRecetas_detalles b = new MRecetas_detalles(a, listatbSegundaria.get(i).getProducto(), listatbSegundaria.get(i).getCantidad());
                    int result = b.guardarRegistro(conexion.getConnection());
                    if (result == 1){
                        LOGGER.info(" Producto: "+ listatbSegundaria.get(i).getProducto().getCodMun() + " receta: " + a.getCodMun() );
                    } else {
                        LOGGER.info("No se ha guardado del detalle - G1");
                    }
                }
                listatbPrincipal.add(a);
                mensaje.setTitle("Registro agregado");
                mensaje.setHeaderText("Registro guardado:");
                mensaje.setContentText("El registro ha sido agregado correctamente");
                mensaje.show();
                LOGGER.info("El registro se ha guardado = " + a.getCodMun());
                limpiarCampos();
            }
            //cerrar conexion
            conexion.cerrarConexion();
        }
    }

    @FXML
    public void editarRegistro() {
        LOGGER.info("Se ha precionado el boton editar");
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        //crea una nueva instancia del tipo recetas
        MRecetas a = new MRecetas(Integer.valueOf(txt_cod.getText()), txt_des.getText());
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            //abre conexion
            conexion.iniciarConexion();
            //llamar al metodo guardar de la clase recetas
            int resultado = a.editarRegistro(conexion.getConnection());
            if (resultado == 1) {
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de recorriendo la posicion index,
                    MRecetas_detalles b = new MRecetas_detalles(a, listatbSegundaria.get(i).getProducto(), listatbSegundaria.get(i).getCantidad());
                    int result = b.editarRegistro(conexion.getConnection());
                    if (result == 1){
                        LOGGER.info("Editanto Producto: "+ listatbSegundaria.get(i).getProducto().getCodMun() + " receta: " + a.getCodMun() );
                    } else {
                        LOGGER.info("No se ha Editado el detalle - E1");
                    }
                }
                listatbPrincipal.set(tb_principal.getSelectionModel().getSelectedIndex(), a);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("El registro ha sido actualizado correctamente");
                mensaje.setHeaderText("Se ha modificado");
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
            MRecetas a = tb_principal.getSelectionModel().getSelectedItem();
            int resultado = a.eliminarRegistro(conexion.getConnection());
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodMun());
            if (resultado == 1) {
                for (int i =0; i <tb_segundaria.getItems().size(); i++){
                    //genera objeto de tipo detalle y agrega de recorriendo la posicion index,
                    MRecetas_detalles b = new MRecetas_detalles(a, listatbSegundaria.get(i).getProducto(), listatbSegundaria.get(i).getCantidad());
                    int result = b.eliminarRegistro(conexion.getConnection());
                    if (result == 1){
                        LOGGER.info("Editanto Producto: "+ listatbSegundaria.get(i).getProducto() + " receta: " + a.getCodMun() );
                    } else {
                        LOGGER.info("No se ha Editado el detalle - E1");
                    }
                }
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
        MRecetas m = new MRecetas();
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
            MRecetas.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
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

    public void cargarTb2(String cod) {
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbSegundaria.removeAll(tb_segundaria.getItems());
        //cargar tabla con resultado de la busqueda
        MRecetas_detalles.buscarRegistro(conexion.getConnection(),listatbSegundaria, cod);
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
        (new Thread(() -> {
            String rt = CRecetas.this.getClass().getSimpleName();
            LOGGER.info("Imprimiendo listado de " + rt);
            conexion.iniciarConexion();
            System.out.println(rt);
            Reportes reporte = new Reportes(rt);
            reporte.generarReporte(conexion.getConnection());
            conexion.cerrarConexion();
        })).start();
    }

    public boolean validacion(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.txt_des.getText().isEmpty() || this.txt_des == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Campo: DESCRIPCION");
            mensaje.setHeaderText("Falta información obligatoria" );
            mensaje.show();
            return false;
         } else {
            return true;
        }

    }

    public boolean validacionDet(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        if(this.cbPro.getEditor().getText().isEmpty() || this.cbPro.getSelectionModel().getSelectedItem() == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("campo: PRODUCTO");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
        } else if(this.txt_can.getText().isEmpty() || this.txt_can.getText() == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Campo: CANTIDAD");
            mensaje.setHeaderText("Falta información obligatoria");
            mensaje.show();
            return false;
        } else {
            return true;
        }
    }

    public void limpiarCamposD(){
        cbPro.setValue(null);
        txt_can.setText("");
        cbPro.requestFocus();
    }

    public void agregarD(){
        LOGGER.info("Se ha precionado el boton agregarD");
      if(!validacionDet()){
          LOGGER.info("Informaciones para detalle no validas");
        } else {
          System.out.println("Codigo receta = " + txt_cod.getText() + "desc = " + txt_des.getText());
            // por ser un combo box editable se debe obtener el texto del editor y separlo
            String cbLpart [] = cbPro.getEditor().getText().split("-");
            System.out.println(cbLpart[0] + " || " + cbLpart[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            MProductos b = new MProductos(Integer.valueOf(cbLpart[0]), cbLpart[1]);
            //crea una nueva instancia de Detalle Recetas
            MRecetas_detalles a = new MRecetas_detalles(b, Double.parseDouble(txt_can.getText()));
            listatbSegundaria.add(a);
            LOGGER.info("El producto se ha agregado = " + a);
            bt_eliminarD.setDisable(false);
            bt_editarD.setDisable(false);
            limpiarCamposD();
        }
    }
    //Metodo secundario para guardar tabla detalle no se esta usando
    public int guardarD(){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        LOGGER.info("Guardando detalle Recetas");
        int resultado = 0;
        if(listatbSegundaria.isEmpty()){
            LOGGER.info("tabla detalle recetas vacia");
            mensaje.setTitle("Registros no agregados");
            mensaje.setContentText("Los registros no se han agregado");
            mensaje.setHeaderText("Tabla de productos vacia");
            mensaje.show();
        } else {
            //abre conexion
            conexion.iniciarConexion();
            int i = listatbSegundaria.sorted().size();
            int x = 0;
            while (x < i){
                //llamar al metodo guardar de la clase detalle recetas de grabando cada elemento de la lista
                resultado = listatbSegundaria.get(x).guardarRegistro(conexion.getConnection());
                System.out.println(listatbSegundaria.get(x) + " || index = " + x + " || resultado = " + resultado + " || valor de i = " + (i-1) );
                x ++;
                if (x == (i-1)){
                    LOGGER.info("Detalles guardados " + x + " de " + (i-1));
                }
            }
            //cerrar conexion
            conexion.cerrarConexion();
            limpiarCampos();
        }
        return resultado;
    }



    public void editarD(){

        LOGGER.info("Se ha precionado el boton editarD");
        if(!validacionDet()){
            LOGGER.info("Informaciones para detalle no validas");
        } else {
            // ca remover la fila seleccionada
            listatbSegundaria.remove(tb_segundaria.getSelectionModel().getSelectedIndex());
            // va agregar con las nuevas ediciones
            agregarD();
        }

        bt_agregarD.setDisable(false);
        bt_editarD.setDisable(true);
    }

    public void eliminarD(){
        LOGGER.info("Se ha precionado el boton eliminar Detalle");
            //abre conexion
            conexion.iniciarConexion();
            MRecetas_detalles a = new MRecetas_detalles(new MRecetas(Integer.parseInt(txt_cod.getText()), txt_des.getText()), tb_segundaria.getSelectionModel().getSelectedItem().getProducto());
            int resultado = a.eliminarRegistro(conexion.getConnection());
            LOGGER.info("El registro a ser eliminado es =" + a.getProducto());
            if (resultado == 1) {
                listatbSegundaria.remove(tb_segundaria.getSelectionModel().getSelectedIndex());
                LOGGER.info("Se ha eliminado el Registro en base de datos");
            } else {
                listatbSegundaria.remove(tb_segundaria.getSelectionModel().getSelectedIndex());
                LOGGER.info("Se ha eliminado el Registro de la lista tabla segundaria");
            }
        limpiarCamposD();
        this.cbPro.requestFocus();
            //cerrar conexion
            conexion.cerrarConexion();
        }

}