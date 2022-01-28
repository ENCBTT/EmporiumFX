package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MRecetas {
    private IntegerProperty codMun;
    private StringProperty desMun;



    public MRecetas(Integer codMun, String desMun ){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() {return desMun.get();}

    public StringProperty desMunProperty() {return desMun;}

    public void setDesMun(String desMun) {this.desMun.set(desMun);}

    public MRecetas(){};

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MRecetas> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM recetas ORDER BY id_receta");
            while (resultado.next()){
                lista.add(new MRecetas(resultado.getInt("id_receta"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            // insertar datos cabecera
            PreparedStatement instruccion = connection.prepareStatement("insert into recetas(id_receta, descripcion) values(?,?);");
            instruccion.setInt(1, codMun.get());
            instruccion.setString(2,desMun.get());
            //falta insertar datos detalle
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE recetas SET descripcion = ? WHERE id_receta = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2, codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM recetas WHERE id_receta = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
                // detalle eliminar tambien
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MRecetas> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM recetas WHERE (descripcion LIKE ? or id_receta = ?) ORDER BY id_receta;");
            instruccion.setString(1, "%"+filtro+"%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MRecetas(resultado.getInt("id_receta"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_receta) FROM recetas");
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
