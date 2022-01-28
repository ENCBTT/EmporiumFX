package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MEstablecimientos {
    private IntegerProperty codEst;
    private StringProperty desEst;

    public MEstablecimientos(Integer codEst, String desEst){
        this.codEst = new SimpleIntegerProperty(codEst);
        this.desEst = new SimpleStringProperty(desEst);
    }


    public MEstablecimientos(){};

    public int getCodEst() { return codEst.get(); }

    public IntegerProperty CodEstProperty() { return codEst; }

    public void seCcodEst(int codEst) { this.codEst.set(codEst); }

    public String getDesEst() { return desEst.get(); }

    public StringProperty DesEstProperty() { return desEst; }

    public void setDesEst(String desEst) { this.desEst.set(desEst); }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MEstablecimientos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM establecimientos;");
            while (resultado.next()){
                lista.add(new MEstablecimientos(resultado.getInt("id_establecimiento"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into establecimientos(id_establecimiento, descripcion) values(?,?)");
            instruccion.setInt(1, codEst.get());
            instruccion.setString(2, desEst.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE establecimientos SET descripcion = ? WHERE id_establecimiento = ?");
            instruccion.setString(1, desEst.get());
            instruccion.setInt(2,codEst.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM establecimientos WHERE id_establecimiento = ?");
            instruccion.setInt(1,codEst.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MEstablecimientos> lista, String filtro) {
        try {
            PreparedStatement instruccion = conncection.prepareStatement("SELECT * FROM establecimientos WHERE (descripcion LIKE ? or id_establecimiento=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MEstablecimientos(resultado.getInt("id_establecimiento"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_establecimiento) FROM establecimientos");
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
        return codEst.get() + " - " + desEst.get();
    }
}
