package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MProveedores {
    private IntegerProperty codPro;
    private StringProperty desPro;
    private StringProperty rucPro;

    public MProveedores(Integer codPro, String desPro, String rucPro){
        this.codPro = new SimpleIntegerProperty(codPro);
        this.desPro = new SimpleStringProperty(desPro);
        this.rucPro = new SimpleStringProperty(rucPro);
    }
    public MProveedores(){};

    public int getCodPro() { return codPro.get(); }

    public IntegerProperty codProProperty() { return codPro; }

    public void setCodPro(int codPro) { this.codPro.set(codPro); }

    public String getDesPro() { return desPro.get(); }

    public StringProperty desProProperty() { return desPro; }

    public void setDesPro(String desPro) { this.desPro.set(desPro); }

    public String getRucPro() { return rucPro.get(); }

    public StringProperty rucProProperty() { return rucPro; }

    public void setRucPro(String rucPro) { this.rucPro.set(rucPro); }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MProveedores> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM proveedores");
            while (resultado.next()){
                lista.add(new MProveedores(resultado.getInt("id_proveedor"),
                        resultado.getString("descripcion"),
                        resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into proveedores(descripcion) values(?);"); // evita injeccion sql
            instruccion.setString(1, desPro.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE proveedores SET descripcion = ? WHERE id_proveedor = ?");
            instruccion.setString(1, desPro.get());
            instruccion.setInt(2,codPro.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM proveedores WHERE id_proveedor = ?");
            instruccion.setInt(1,codPro.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MProveedores> lista, String filtro) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM proveedores WHERE (id_proveedor ='"+ filtro +"' or descripcion LIKE '%"+filtro+"%')");

            while (resultado.next()){
                lista.add(new MProveedores(resultado.getInt("id_proveedor"),
                        resultado.getString("descripcion"),
                        resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void buscarRegistro(Connection connection, ObservableList<MProveedores> lista, String des){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM proveedores WHERE (descripcion LIKE '%"+ des +"%')");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MProveedores(resultado.getInt("id_proveedor"),
                        resultado.getString("descripcion"),
                        resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MProveedores obtenerProveedor(Connection connection, String cod) {
        MProveedores c = null;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM proveedores WHERE (id_proveedor=?)");
            instruccion.setString(1, cod);
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                System.out.println("Encontro coincidencias de proveedores");
                c = new MProveedores(resultado.getInt("id_proveedor"),
                        resultado.getString("descripcion"),
                        resultado.getString("ruc"));
            }else {
                System.out.println("no se encontro coincidencias de proveedores");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return c;
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_proveedor) FROM proveedores");
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
        return codPro.get() + " - " + desPro.get();
    }
}
