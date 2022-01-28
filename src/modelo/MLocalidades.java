package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MLocalidades {
    private IntegerProperty codMun;
    private StringProperty desMun;

    public MLocalidades(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }
    public MLocalidades(){};

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
    public static void obtenerInfoMun(Connection conncection, ObservableList<MLocalidades> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM localidades");
            while (resultado.next()){
                lista.add(new MLocalidades(resultado.getInt("id_localidad"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into localidades(descripcion) values(?);");
            instruccion.setString(1, desMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE localidades SET descripcion = ? WHERE id_localidad = ?");
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
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM localidades WHERE id_localidad = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MLocalidades> lista, String filtro) {
        try {
            PreparedStatement instruccion = conncection.prepareStatement("SELECT * FROM localidades WHERE (descripcion LIKE ? or id_localidad='?')");
            instruccion.setString(1, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MLocalidades(resultado.getInt("id_localidad"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //llenar combo box busqueda
    public static void buscarRegreducido(Connection connection, ObservableList<MLocalidades> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT id_localidad, descripcion FROM localidades " +
                    "WHERE (descripcion LIKE ? OR id_localidad = ?) ORDER BY id_localidad LIMIT 10");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MLocalidades(resultado.getInt("id_localidad"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_localidad) FROM localidades");
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
