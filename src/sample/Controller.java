package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import modelo.ConexionBD;
import modelo.MCargos;
import modelo.MMesas;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ComboBox cb1;
    @FXML
    private ComboBox cb2;
    @FXML
    private Button bt1;
    @FXML
    private Button bt2;
    @FXML
    private Label lbl_hora;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    ObservableList <MCargos> listacb2;
    ObservableList <MMesas> listacb1;
    ConexionBD conexion;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion = new ConexionBD();
        conexion.iniciarConexion();
        listacb1 = FXCollections.observableArrayList();
        listacb2 = FXCollections.observableArrayList();
        MCargos.obtenerInfoMun(conexion.getConnection(), listacb2);
        MMesas.obtenerInfoMun(conexion.getConnection(), listacb1);
        cb2.setItems(listacb2);
        cb1.setItems(listacb1);

    }

    public void testeCB(){
        if (!cb1.getSelectionModel().isEmpty()){ // si esta o no seleccionado se puede usar
            System.out.println("CB1 no editable no vacio");
        }else {System.out.println("CB1 no editable vacio");}
        if (cb1.getSelectionModel().getSelectedIndex() < 2){ // tambein se puede usar por que queda como m-1 si no esta seleccionado
            System.out.println("CB1 Valor index = " + cb1.getSelectionModel().getSelectedIndex());
        }
        if (cb2.getSelectionModel().isEmpty()){ // no funciona con cb editable
            System.out.println("CB2 editable vacio");
        }
        if (cb2.getSelectionModel().getSelectedIndex() < 2){
            System.out.println("CB2 Valor index = " + cb2.getSelectionModel().getSelectedIndex());
        }
        if (!cb2.getEditor().getText().isEmpty()){ // no funciona con null
            System.out.println("Texto de CB2 editable = " + cb2.getEditor().getText());
        }
        if (!cb1.getEditor().getText().isEmpty()){ // no funciona con null
            System.out.println("Texto de CB1 no editable = " + cb1.getEditor().getText());
        }
        if (cb2.getEditor().getText().equals("")){ // funciona
            System.out.println("CB2 null");
        }

    }

    public void hora(){
        lbl_hora.setText(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }


}
