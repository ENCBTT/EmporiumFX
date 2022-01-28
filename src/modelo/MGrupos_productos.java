package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MGrupos_productos {
    private IntegerProperty codMun;
    private StringProperty desMun;

    public MGrupos_productos(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }
    public MGrupos_productos(){};

    public int getCodMun() {
        return codMun.get();
    }

    public IntegerProperty codMunProperty() {
        return codMun;
    }

    public void setCodMun(int codMun) {
        this.codMun.set(codMun);
    }

    public String getDesMun() {
        return desMun.get();
    }

    public StringProperty desMunProperty() {
        return desMun;
    }

    public void setDesMun(String desMun) {
        this.desMun.set(desMun);
    }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MGrupos_productos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM grupos_productos");
            while (resultado.next()){
                lista.add(new MGrupos_productos(resultado.getInt("id_grupo_producto"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into grupos_productos(descripcion) values(?);"); // evita injeccion sql
            instruccion.setString(1, desMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE grupos_productos SET descripcion = ? WHERE id_grupo_producto = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM grupos_productos WHERE id_grupo_producto = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MGrupos_productos> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM grupos_productos WHERE (descripcion LIKE ? or id_grupo_producto=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2,filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MGrupos_productos(resultado.getInt("id_grupo_producto"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_grupo_producto) FROM grupos_productos");
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
