package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MTipos_ajustes {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private IntegerProperty claMun;

    public MTipos_ajustes(Integer codMun, String desMun, Integer claMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.claMun = new SimpleIntegerProperty(claMun);
    }
    public MTipos_ajustes(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public MTipos_ajustes(){};

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() { return desMun.get(); }

    public StringProperty desMunProperty() { return desMun; }

    public void setDesMun(String desMun) { this.desMun.set(desMun); }

    public int getClaMun() {return claMun.get();}

    public IntegerProperty claMunProperty() {return claMun;}

    public void setClaMun(int claMun) {this.claMun.set(claMun);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MTipos_ajustes> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM tipos_ajustes;");
            while (resultado.next()){
                lista.add(new MTipos_ajustes(resultado.getInt("id_tipo_ajuste"),
                        resultado.getString("descripcion"),resultado.getInt("clasificacion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into tipos_ajustes(descripcion) values(?,?);");
            instruccion.setString(1,desMun.get());
            instruccion.setInt(2, claMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE tipos_ajustes SET descripcion = ?, clasificacion = ? WHERE id_tipo_ajuste = ?");
            instruccion.setString(1,desMun.get());
            instruccion.setInt(2,claMun.get());
            instruccion.setInt(3,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM tipos_ajustes WHERE id_tipo_ajuste = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MTipos_ajustes> lista, String filtro) {
        try {
            PreparedStatement instruccion = conncection.prepareStatement("SELECT * FROM tipos_ajustes WHERE (descripcion LIKE ? or id_tipo_ajuste='?')");
            instruccion.setString(1, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MTipos_ajustes(resultado.getInt("id_tipo_ajuste"),
                        resultado.getString("descripcion"),
                        resultado.getInt("clasificacion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_tipo_ajuste) FROM tipos_ajustes");
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
