package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import java.sql.*;

public class MCargos {
    private IntegerProperty codCargo;
    private StringProperty desCargo;

    public MCargos(Integer codCargo, String desCargo){
        this.codCargo = new SimpleIntegerProperty(codCargo);
        this.desCargo = new SimpleStringProperty(desCargo);
    }
    public MCargos(){}

    public int getcodCargo() {
        return codCargo.get();
    }

    public IntegerProperty codCargoProperty() {
        return codCargo;
    }

    public void setcodCargo(int codCargo) {
        this.codCargo.set(codCargo);
    }

    public String getdesCargo() {
        return desCargo.get();
    }

    public StringProperty desCargoProperty() {
        return desCargo;
    }

    public void setdesCargo(String desCargo) {
        this.desCargo.set(desCargo);
    }



    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MCargos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM cargos;");
            while (resultado.next()){
                lista.add(new MCargos(resultado.getInt("id_cargo"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO cargos(id_cargo , descripcion) values(default,?)");
            instruccion.setString(1, desCargo.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE cargos SET descripcion = ? WHERE id_cargo = ?");
            instruccion.setString(1, desCargo.get());
            instruccion.setInt(2,codCargo.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM cargos WHERE id_cargo = ?");
            instruccion.setInt(1,codCargo.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MCargos> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM cargos WHERE (descripcion LIKE ? or id_cargo=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MCargos(resultado.getInt("id_cargo"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_cargo) FROM cargos");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return codCargo.get() + " - " + desCargo.get(); }
}
