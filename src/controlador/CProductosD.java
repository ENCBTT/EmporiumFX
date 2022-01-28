package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.ConexionBD;
import modelo.MGrupos_productos;
import modelo.MProductos;
import modelo.MSubgrupos_productos;

import java.net.URL;
import java.util.ResourceBundle;

public class CProductosD implements Initializable {

    CProductosD CproductosD;
    CFacturas factura;
    CFacturas_compras compra;
    CPedidos pedido;
    //Componentes busqueda producto
    @FXML private ComboBox<MSubgrupos_productos> cbD1;
    @FXML TextField txt_buscarD;
    @FXML Button bt_add;
    @FXML Button bt_salirD;
    @FXML TableView tb_proD;
    @FXML TableColumn <MProductos, Integer> cl31;
    @FXML TableColumn <MProductos, String> cl32;
    @FXML TableColumn <MProductos, String> cl33;
    @FXML TableColumn <MProductos, Integer> cl34;
    @FXML TableColumn <MProductos, MGrupos_productos> cl35;
    @FXML TableColumn <MProductos, Double> cl36;

    ObservableList<MSubgrupos_productos> listacb1;
    ObservableList<MProductos> listatbBusqueda;

    private ConexionBD conexion;
    private String clase;
    //private Class classe;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        CproductosD = this;
        //colleciones
        listacb1 = FXCollections.observableArrayList();
        listatbBusqueda = FXCollections.observableArrayList();
        //llamar metodos
        MProductos.obtenerInfoDet(conexion.getConnection(), listatbBusqueda);
        MSubgrupos_productos.obtenerInfoMun(conexion.getConnection(), listacb1);
        //cargar tabla
        tb_proD.setItems(listatbBusqueda);
        //Enlazar columnas con atributos
        cl31.setCellValueFactory(new PropertyValueFactory<>("codMun"));
        cl32.setCellValueFactory(new PropertyValueFactory<>("nomMun"));
        cl33.setCellValueFactory(new PropertyValueFactory<>("desMun"));
        cl34.setCellValueFactory(new PropertyValueFactory<>("pvMun"));
        cl35.setCellValueFactory(new PropertyValueFactory<>("subgrupo_producto"));
        cl36.setCellValueFactory(new PropertyValueFactory<>("stoMun"));
        //ComboBox
        cbD1.setItems(listacb1);
        //Eventos
        this.gestionar_eventos();
        txt_buscarD.requestFocus();

        //cerrar conexion
        conexion.cerrarConexion();
    }
    public void gestionar_eventos() {

        tb_proD.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MProductos>() {
            @Override
            public void changed(ObservableValue<? extends MProductos> observableValue, MProductos valorAnterior, MProductos valorSeleccionado) {
                if (valorSeleccionado != null) {

                        switch (clase){
                            case "facturaV":
                                System.out.println("Controlador factura venta");
                                factura.recibirDatosPro(
                                        String.valueOf(valorSeleccionado.getCodMun()),
                                        valorSeleccionado.getNomMun(),
                                        String.valueOf(valorSeleccionado.getPvMun()));
                                break;
                            case "facturaC":
                                System.out.println("Controlador factura compra");
                                compra.recibirDatosPro(
                                        String.valueOf(valorSeleccionado.getCodMun()),
                                        valorSeleccionado.getNomMun());
                                break;
                        }

                    System.out.println(valorSeleccionado.getCodMun() + "<cod>" + valorSeleccionado.getPvMun() + "<valor>" + valorSeleccionado.getNomMun());
                }
            }
        });
    }

    @FXML
    public void busqueda2() {
        String des = this.txt_buscarD.getText();
        String string = String.valueOf(cbD1.getSelectionModel().getSelectedItem());
        String[] parts = string.split(" - ");
        String part1 = parts[0];
        Integer idG = Integer.valueOf(part1);
        //abre conexion
        conexion.iniciarConexion();
        //remover itens de la tabla
        listatbBusqueda.removeAll(tb_proD.getItems());
        //cargar tabla con resultado de la busqueda
        MProductos.buscarRegistroDetalle(conexion.getConnection(),listatbBusqueda, des, idG);
        tb_proD.setItems(listatbBusqueda);
        //cerrar conexion
        conexion.cerrarConexion();
    }

    @FXML
    public void salirVentana(){
            Stage myStage = (Stage) this.bt_salirD.getScene().getWindow();
            myStage.close();
    }
    public void recibir(CFacturas stageFacturas){
        factura = stageFacturas; clase = "facturaV";
    }
    public void recibirFC(CFacturas_compras stageFacturas){
        compra = stageFacturas; clase = "facturaC";
    }
    public void recibirPed(CPedidos stagePedidos){
        pedido = stagePedidos; clase = "pedidos";
    }
    //public void recibirClase(Class stageClase){classe = stageClase;}
}