package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MCondiciones_facturas {
    private IntegerProperty codMun;
    private StringProperty desMun;

    public MCondiciones_facturas(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public MCondiciones_facturas(){};

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() { return desMun.get(); }

    public StringProperty desMunProperty() { return desMun; }

    public void setDesMun(String desMun) { this.desMun.set(desMun); }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MCondiciones_facturas> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT id_condicion, descripcion FROM condiciones_facturas;");
            while (resultado.next()){
                lista.add(new MCondiciones_facturas(resultado.getInt("id_condicion"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into condiciones_facturas(descripcion) values(?);"); // evita injeccion sql
            instruccion.setString(1,desMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE condiciones_facturas SET descripcion = ? WHERE id_condicion = ?");
            instruccion.setString(1,desMun.get());
            instruccion.setInt(2,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM condiciones_facturas WHERE id_condicion = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MCondiciones_facturas> lista, String filtro) {
        try {
            PreparedStatement instruccion = conncection.prepareStatement("SELECT * FROM condiciones_facturas WHERE (descripcion LIKE ? or id_condicion='?')");
            instruccion.setString(1, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MCondiciones_facturas(resultado.getInt("id_condicion"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_condicion) FROM condiciones_facturas");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return codMun.get() + " - " + desMun.get();
    }
}
