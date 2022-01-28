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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.reports.Reportes;
import vista.VCartas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CCartas implements Initializable {
    //columnas tabla
    @FXML
    private TableColumn<MCartas, Integer> cl1;
    @FXML
    private TableColumn<MCartas, String> cl2;
    @FXML
    private TableColumn<MCartas, String> cl3;
    @FXML
    private TableColumn<MCartas, Double> cl4;
    @FXML
    private TableColumn<MCartas, MProductos> cl5;
    @FXML
    private TableColumn<MCartas, MRecetas> cl6;
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
    private TextField txt_pre;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button bt_salir;
    @FXML
    private Button bt_imp;
    @FXML
    private Button bt_img;
    @FXML
    private TableView<MCartas> tb_principal;
    @FXML
    private ComboBox<MProductos> cb1;
    @FXML
    private ComboBox<MRecetas> cb2;
    @FXML
    private ImageView iv_img;

    //collecion
    ObservableList<MCartas> listatbPrincipal;
    ObservableList<MProductos> listacb1;
    ObservableList<MRecetas> listacb2;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CCartas.class.getSimpleName());
    private ConexionBD conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        //colleciones
        listatbPrincipal = FXCollections.observableArrayList();
        listacb1 = FXCollections.observableArrayList();
        listacb2 = FXCollections.observableArrayList();
        //llamar metodos
        MCartas.obtenerInfoMun(conexion.getConnection(), listatbPrincipal);
        MProductos.obtenerInfoMun(conexion.getConnection(), listacb1);
        MRecetas.obtenerInfoMun(conexion.getConnection(), listacb2);
        //cargar tabla
        tb_principal.setItems(listatbPrincipal);
        //Enlazar columnas con atributos
        cl1.setCellValueFactory(new PropertyValueFactory<>("codCar"));
        cl2.setCellValueFactory(new PropertyValueFactory<>("desCar"));
        cl3.setCellValueFactory(new PropertyValueFactory<>("imgCar"));
        cl4.setCellValueFactory(new PropertyValueFactory<>("preCar"));
        cl5.setCellValueFactory(new PropertyValueFactory<>("producto"));
        cl6.setCellValueFactory(new PropertyValueFactory<>("receta")); // producto sin recetas no aparecen el la tabla
        //ComboBox
        cb1.setItems(listacb1);
        cb2.setItems(listacb2);
        //Eventos
        gestionar_eventos();
        txt_des.requestFocus();
        //cerrar conexion
        conexion.cerrarConexion();
    }

    public void gestionar_eventos() {
        tb_principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCartas>() {
            @Override
            public void changed(ObservableValue<? extends MCartas> observableValue, MCartas valorAnterior, MCartas valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txt_cod.setText(String.valueOf(valorSeleccionado.getCodCar()));
                    txt_des.setText(valorSeleccionado.getDesCar());
                    txt_pre.setText(String.valueOf(valorSeleccionado.getPreCar()));
                    cb1.setValue(valorSeleccionado.getProducto());
                    cb2.setValue(valorSeleccionado.getReceta());
                    Image e = new Image("file:C:\\Users\\User\\IdeaProjects\\javaFxHellow\\src\\resources\\imagenes\\"+valorSeleccionado.getImgCar());
                    bt_guardar.setDisable(true);
                    bt_eliminar.setDisable(false);
                    bt_editar.setDisable(false);
                    iv_img.setImage(e);
                }
            }
        });
    }

    @FXML
    public void limpiarCampos() {
        //campos
        txt_cod.setText(null);
        txt_des.setText("");
        txt_pre.setText("");
        iv_img.setImage(null);
        cb1.setValue(null);
        cb2.setValue(null);
        //botones
        bt_editar.setDisable(true);
        bt_eliminar.setDisable(true);
        bt_guardar.setDisable(false);
        bt_nuevo.requestFocus();
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
            System.out.println(urlImg());
            // por ser un combo box editable se debe obtener el texto del editor y separlo
            String cb1part [] = cb1.getEditor().getText().split("-" );
            System.out.println(cb1part[0] + " || " + cb1part[1] + " = valor separado");
            //String cb2part [] = cb2.getEditor().getText().split(" - ");
            // System.out.println(cb2part[0] + " || " + cb2part[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            MProductos b = new MProductos(Integer.valueOf(cb1part[0]), cb1part[1]);
            MRecetas c = cbrecetas();
            //crea una nueva instancia del tipo personas
            MCartas a = new MCartas(ultCod(), txt_des.getText(),urlImg(),Double.valueOf(txt_pre.getText()),b,c);
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
                LOGGER.info("El registro se ha guardado = " + a.getCodCar());
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
        if(!validacion()){
            LOGGER.info("Informaciones no validas");
        } else {
            // por ser un combo box editable se debe obtener el texto del editor y separlo
            String cb1part [] = cb1.getEditor().getText().split("-" );
            System.out.println(cb1part[0] + " || " + cb1part[1] + " = valor separado");
            String cb2part [] = cb2.getEditor().getText().split(" - ");
            System.out.println(cb2part[0] + " || " + cb2part[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            MProductos b = new MProductos(Integer.valueOf(cb1part[0]), cb1part[1]);
            MRecetas c = new MRecetas(Integer.valueOf(cb2part[0]), cb2part[1]);
            //crea una nueva instancia del tipo personas // almacenar en carpeta imagen mejorar
            MCartas a = new MCartas(ultCod(), txt_des.getText(),urlImg(),Double.valueOf(txt_pre.getText()),b,c);
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
                LOGGER.info("El registro modificado es =" + a.getCodCar());
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
            LOGGER.info("El registro a ser eliminado es =" + tb_principal.getSelectionModel().getSelectedItem().getCodCar());
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
        MCartas m = new MCartas();
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
            MCartas.buscarRegistro(conexion.getConnection(),listatbPrincipal, filtro);
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
            String rt = CCartas.this.getClass().getSimpleName();
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
        if(this.txt_des.getText().isEmpty() || this.txt_des == null || this.txt_pre.getText().isEmpty() || this.txt_pre == null){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Faltan informacion obligatoria");
            mensaje.show();
            return false;
        } else if(this.cb1.getEditor().getText() == null || this.cb1.getEditor().getText().isEmpty()){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Index = " + cb1.getSelectionModel().getSelectedIndex() + "\n"
                    + "Valor = " + cb1.getSelectionModel().getSelectedItem());
            mensaje.setHeaderText("Falta seleccionar Producto o Receta");
            mensaje.show();
            return false;
        } else if (this.cb1.getSelectionModel().getSelectedItem() == null || this.cb1.getSelectionModel().getSelectedIndex() != -1){
            mensaje.setTitle("Faltan datos");
            mensaje.setContentText("Index = " + cb1.getSelectionModel().getSelectedIndex() + "\n"
                    + "Valor = " + cb1.getSelectionModel().getSelectedItem());
            mensaje.setHeaderText("Se debe seleccionar Producto o Receta");
            mensaje.show();
            return false;
        } else {
            return true;
        }
    }

    public void cargarImg(){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar Imagen");
            fileChooser.setInitialDirectory(new File("C:\\Users\\User\\IdeaProjects\\javaFxHellow\\src\\resources\\imagenes"));
            // Agregar filtros para facilitar la busqueda
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            // Obtener la imagen seleccionada
            File imgFile = fileChooser.showOpenDialog(null);

            // Mostar la imagen
            if (imgFile != null) {
                Image image = new Image("file:" + imgFile.getAbsolutePath());
                System.out.println(image.getUrl().substring(68));
                iv_img.setImage(image);
            }
    }

    public String urlImg(){
        if (iv_img.getImage() == null){
            return "";
        } else {
            return iv_img.getImage().getUrl().substring(68);
        }
    }

    public MRecetas cbrecetas(){
        MRecetas c ;
        if (cb2.getEditor().getText().isEmpty() || cb2.getEditor().getText() == ""){
            return c = new MRecetas(0,"");
        } else {
            String cb2part [] = cb2.getEditor().getText().split(" - ");
            System.out.println(cb2part[0] + " || " + cb2part[1] + " = valor separado");
            // crea la instancia con el resultado de la busqueda en el combobox
            c = new MRecetas(Integer.valueOf(cb2part[0]), cb2part[1]);
            return c;
        }
    }

}
