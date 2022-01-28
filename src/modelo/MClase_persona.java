package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MClase_persona {
    private IntegerProperty codMun;
    private StringProperty desMun;


    public MClase_persona(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }
    public MClase_persona(){};

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
    public static void obtenerInfoMun(Connection conncection, ObservableList<MClase_persona> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM clases_personas");
            while (resultado.next()){
                lista.add(new MClase_persona(resultado.getInt("id_clase_persona"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into clases_personas(descripcion) values(?);"); // evita injeccion sql
            instruccion.setString(1, desMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE clases_personas SET descripcion = ? WHERE id_clase_persona = ?");
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
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM clases_personas WHERE id_clase_persona = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MClase_persona> lista, String filtro) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM clases_personas WHERE (id_clase_persona ='"+ filtro +"' or descripcion LIKE '%"+filtro+"%')");

            while (resultado.next()){
                lista.add(new MClase_persona(resultado.getInt("id_clase_persona"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void buscarRegistro(Connection connection, ObservableList<MClase_persona> lista, String des){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM clases_personas WHERE (descripcion LIKE '%"+ des +"%')");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MClase_persona(resultado.getInt("id_clase_persona"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_clase_persona) FROM clases_personas");
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
