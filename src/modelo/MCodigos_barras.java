package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MCodigos_barras {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private MProductos producto;

    public MProductos getProducto() { return producto; }

    public void setProducto(MProductos producto) { this.producto = producto; }

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() { return desMun.get(); }

    public StringProperty desMunProperty() { return desMun; }

    public void setDesMun(String desMun) { this.desMun.set(desMun); }

    public MCodigos_barras(Integer codMun, String desMun, MProductos producto){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.producto = producto;
    }

    public MCodigos_barras(){};


    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MCodigos_barras> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.nombre FROM codigos_barras a INNER JOIN productos b ON (a.producto_id = b.id_producto);");
            while (resultado.next()){
                lista.add(new MCodigos_barras(resultado.getInt("id_codigo_barra"), // usar los prefijos para no tener problemas con los campos con mismos nombres
                        resultado.getString("descripcion"),
                        new MProductos(resultado.getInt("producto_id"), resultado.getString("b.nombre"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO codigos_barras(id_codigo_barra, descripcion, producto_id) VALUES(?,?,?);");
            instruccion.setInt(1,codMun.get());
            instruccion.setString(2, desMun.get());
            instruccion.setInt(3,producto.getCodMun());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE codigos_barras SET descripcion = ?, producto_id = ? WHERE id_codigo_barra = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,producto.getCodMun());
            instruccion.setInt(3,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM codigos_barras WHERE id_codigo_barra = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MCodigos_barras> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.nombre FROM codigos_barras a INNER JOIN productos b ON (a.producto_id = b.id_producto) WHERE (a.descripcion LIKE ? or a.id_codigo_barra=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MCodigos_barras(resultado.getInt("id_codigo_barra"),
                        resultado.getString("descripcion"),
                        new MProductos(resultado.getInt("producto_id"), resultado.getString("b.nombre"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_codigo_barra) FROM codigos_barras");
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
