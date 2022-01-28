package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MSucursales {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private MEmpresas empresa;

    public MSucursales(Integer codMun, String desMun, MEmpresas empresa) {
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.empresa = empresa;
    }
    public MSucursales(Integer codMun, String desMun) {
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public MSucursales(){};

    public int getCodMun() {return codMun.get();}

    public IntegerProperty codMunProperty() {return codMun;}

    public void setCodMun(int codMun) {this.codMun.set(codMun);}

    public String getDesMun() {return desMun.get();}

    public StringProperty desMunProperty() {return desMun;}

    public void setDesMun(String desMun) {this.desMun.set(desMun);}

    public MEmpresas getEmpresa() {return empresa;}

    public void setEmpresa(MEmpresas empresa) {this.empresa = empresa;}

    //llenar tabla
    public static void obtenerInfoMun(Connection connection, ObservableList<MSucursales> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.razon_social FROM sucursales a INNER JOIN empresas b ON (a.empresa_id = b.id_empresa) ORDER BY id_sucursal;");
            while (resultado.next()){
                lista.add(new MSucursales(resultado.getInt("id_sucursal"),
                        resultado.getString("descripcion"),
                        new MEmpresas(resultado.getInt("a.empresa_id"), resultado.getString("b.razon_social"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into sucursales(id_sucursal, descripcion,empresa_id) values(?,?,?)");
            instruccion.setInt(1,codMun.get());
            instruccion.setString(2, desMun.get());
            instruccion.setInt(3,empresa.getCodEmp());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE sucursales SET descripcion = ?, empresa_id = ? WHERE id_sucursal = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,empresa.getCodEmp());
            instruccion.setInt(3,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM sucursales WHERE id_sucursal = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MSucursales> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM sucursales SELECT a.*, b.razon_social FROM sucursales a " +
                    "INNER JOIN empresas b ON (a.empresa_id = b.id_empresa) WHERE (a.descripcion LIKE ? or a.id_sucursal=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MSucursales(resultado.getInt("id_sucursal"),
                        resultado.getString("descripcion"),
                        new MEmpresas(resultado.getInt("a.empresa_id"), resultado.getString("b.razon_social"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_sucursal) FROM sucursales");
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
