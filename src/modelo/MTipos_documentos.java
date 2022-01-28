package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MTipos_documentos {
    private IntegerProperty codMun;
    private StringProperty desMun;

    public MTipos_documentos(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }
    public MTipos_documentos(){};

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
    public static void obtenerInfoMun(Connection conncection, ObservableList<MTipos_documentos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM tipos_documentos");
            while (resultado.next()){
                lista.add(new MTipos_documentos(resultado.getInt("id_documento"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into tipos_documentos(descripcion) values(?);"); // evita injeccion sql
            instruccion.setString(1, desMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE tipos_documentos SET descripcion = ? WHERE id_documento = ?");
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
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM tipos_documentos WHERE id_documento = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MTipos_documentos> lista, String filtro) {
        try {
            System.out.println("Filtro = " + filtro);
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM tipos_documentos WHERE (id_documento = ? or descripcion LIKE ?)");
            instruccion.setString(1, filtro);
            instruccion.setString(2, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MTipos_documentos(resultado.getInt("id_documento"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_documento) FROM tipos_documentos");
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
