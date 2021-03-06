package controlador;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.ConexionBD;
import modelo.MAcceso;
import modelo.MEstablecimientos;
import modelo.MPuntos_expediciones;
import net.sf.jasperreports.engine.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CMenuPrincipal{
    //botones
    @FXML
    private Label lbluser;
    @FXML
    private Label lblest;
    @FXML
    private Label lblexp;
    @FXML
    private Button bt_salir;
    //Log
    private static final Logger LOGGER = LoggerFactory.getLogger(CMenuPrincipal.class);

    MAcceso datosUser;
    Stage stgMesas = new Stage();
    Stage stgAjp = new Stage();
    Stage stgDel = new Stage();
    Stage stgFact = new Stage();
    Stage stgComp = new Stage();
    Stage stgPed = new Stage();
    Stage stgProd = new Stage();
    Stage stgPers = new Stage();

    public void datosAcces(MAcceso datosUsuario){
        lbluser.setText(datosUsuario.getCodUser()+" - "+datosUsuario.getLogin());
        lblest.setText(datosUsuario.getEstablecimiento().getCodEst()+" - "+ datosUsuario.getEstablecimiento().getDesEst());
        lblexp.setText(datosUsuario.getPunto_expedicion().getCodMun()+" - "+ datosUsuario.getPunto_expedicion().getDesMun());
        datosUser = datosUsuario;
    }

    //public int getCodUser(){ return datosUser.getCodUser();}
    public String getLoginUser(){return  lbluser.getText();}
    @FXML
    public void salirVentana(){
        Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
        myStage.close();
    }
    String teste = "teste";
    @FXML //metodo para abrir cualquier ventana
    public void abrirVentana(Event event){
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        MenuItem item = (MenuItem) event.getSource();
        String ruta = item.getId();
        try {
            LOGGER.info("Iniciando formulario = " + ruta +" - Codigo =" + datosUser.getCodUser() +" - Usuario = " + datosUser.getLogin());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/" + ruta +".fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            LOGGER.info("Formulario no encontrado: " + ruta);
            mensaje.setTitle("Error");
            mensaje.setContentText("No se pudo abrir el formulario");
            mensaje.setHeaderText(ruta);
            mensaje.show();
            e.printStackTrace();
        }
    }

    @FXML
    public void ajustes_precios(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VAjustes_precios.fxml"));
            Parent root1 = fxmlLoader.load();
            //crea una instancia del controlador con el controlador inicializado por fxmlLoader
            CAjustes_precios ajustes_precios = fxmlLoader.getController();
            //llama al metodo para passar el objeto de tipo Acceso con los datos de inicio de session
            ajustes_precios.datosUser(datosUser);
            stgAjp.setScene(new Scene(root1));
            if (!stgAjp.isShowing()) {
                stgAjp.show();
            } else if(stgAjp.isIconified()){
                stgAjp.setIconified(false);
            }else {
                stgAjp.toFront();
            }
            Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deliverys(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VDeliverys.fxml"));
            Parent root1 = fxmlLoader.load();
            //crea una instancia del controlador con el controlador inicializado por fxmlLoader
            CDeliverys delivery = fxmlLoader.getController();
            //llama al metodo para passar el objeto de tipo Acceso con los datos de inicio de session
            delivery.datosUser(datosUser);
            stgDel.setScene(new Scene(root1));
            if (!stgDel.isShowing()) {
                stgDel.show();
            } else if(stgDel.isIconified()){
                stgDel.setIconified(false);
            }else {
                stgDel.toFront();
            }
            Stage myStage = (Stage) this.bt_salir.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirMesas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VMesas.fxml"));
            Parent root1 = fxmlLoader.load();
            stgMesas.setScene(new Scene(root1));
            if (!stgMesas.isShowing()) {
                stgMesas.show();
            } else if(stgMesas.isIconified()){
                stgMesas.setIconified(false);
            }else {
                stgMesas.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirFacturas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VFacturas.fxml"));
            Parent root1 = fxmlLoader.load();
            //crea una instancia del controlador con el controlador inicializado por fxmlLoader
            CFacturas factura = fxmlLoader.getController();
            //llama al metodo para passar el objeto de tipo Acceso con los datos de inicio de session
            factura.datosUser(datosUser);
            stgFact.setScene(new Scene(root1));
            if (!stgFact.isShowing()) {
                stgFact.show();
            } else if(stgFact.isIconified()){
                stgFact.setIconified(false);
            }else {
                stgFact.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirCompras(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VFacturas_compras.fxml"));
            Parent root1 = fxmlLoader.load();
            //crea una instancia del controlador con el controlador inicializado por fxmlLoader
            CFacturas_compras factura = fxmlLoader.getController();
            //llama al metodo para passar el objeto de tipo Acceso con los datos de inicio de session
            factura.datosUser(datosUser);
            stgComp.setScene(new Scene(root1));
            if (!stgComp.isShowing()) {
                stgComp.show();
            } else if(stgComp.isIconified()){
                stgComp.setIconified(false);
            }else {
                stgComp.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirPedidos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VPedidos.fxml"));
            Parent root1 = fxmlLoader.load();
            stgPed.setScene(new Scene(root1));
            if (!stgPed.isShowing()) {
                stgPed.show();
            } else if(stgPed.isIconified()){
                stgPed.setIconified(false);
            }else {
                stgPed.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirProductos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VProductos.fxml"));
            Parent root1 = fxmlLoader.load();
            stgProd.setScene(new Scene(root1));
            if (!stgProd.isShowing()) {
                stgProd.show();
            } else if(stgProd.isIconified()){
                stgProd.setIconified(false);
            }else {
                stgProd.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirPersonas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VPersonas2.fxml"));
            Parent root1 = fxmlLoader.load();
            stgPers.setScene(new Scene(root1));
            if (!stgPers.isShowing()) {
                stgPers.show();
            } else if(stgPers.isIconified()){
                stgPers.setIconified(false);
            }else {
                stgPers.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    @FXML
    public void abrirClientes(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VClientes.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirFuncionarios(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VSucursales.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirClasesPersonas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VClases_personas.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirProveedores(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VProveedores.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirCargos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VEmpresas.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirUsuarios(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VUsuarios.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirEstado(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VEstados.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirNacionalidad(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VNacionalidades.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirLocalidad(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VLocalidades.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirMunicipio(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VMunicipios.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirSucursal(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VMunicipios.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirClasesFacturas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VClases_facturas.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirCondicionesPago(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VCondiciones.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirGruposProductos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VGrupos_productos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirEstablecimientos(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VEstablecimientos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirExpediciones(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VPuntos_expediciones.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirTiposDoc(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VTipos_docuemntos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirTimbrados(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/VRecetas.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

}
