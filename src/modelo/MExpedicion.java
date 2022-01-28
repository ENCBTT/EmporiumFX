package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MExpedicion extends ConexionBD {
    private IntegerProperty codExp;
    private StringProperty desExp;
    private IntegerProperty estado;
    /** Constructor de clase */
    public MExpedicion(Integer codExp, String desExp, Integer estado){
        this.codExp = new SimpleIntegerProperty(codExp);
        this.desExp = new SimpleStringProperty(desExp);
        this.estado = new SimpleIntegerProperty(estado);
    }
    public MExpedicion(Integer codExp, String desExp){
        this.codExp = new SimpleIntegerProperty(codExp);
        this.desExp = new SimpleStringProperty(desExp);
    }
    //llenar combo box
    public static void obtenerInfoExp(Connection conncection, ObservableList<MExpedicion> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT id_expedicion, descripcion FROM puntos_expediciones WHERE estado_id = 1");
            while (resultado.next()){
                lista.add(new MExpedicion(resultado.getInt("id_expedicion"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return codExp.get() + " - " + desExp.get();
    }
}