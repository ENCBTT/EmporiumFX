package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MSubgrupos_productos {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private MGrupos_productos grupo_producto;

    public MSubgrupos_productos(Integer codMun, String desMun, MGrupos_productos grupo_producto) {
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.grupo_producto = grupo_producto;
    }
    public MSubgrupos_productos(Integer codMun, String desMun) {
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public MSubgrupos_productos(){};

    public int getCodMun() {return codMun.get();}

    public IntegerProperty codMunProperty() {return codMun;}

    public void setCodMun(int codMun) {this.codMun.set(codMun);}

    public String getDesMun() {return desMun.get();}

    public StringProperty desMunProperty() {return desMun;}

    public void setDesMun(String desMun) {this.desMun.set(desMun);}

    public MGrupos_productos getGrupo_producto() {return grupo_producto;}

    public void setGrupo_producto(MGrupos_productos grupo_producto) {this.grupo_producto = grupo_producto;}

    //llenar tabla
    public static void obtenerInfoMun(Connection connection, ObservableList<MSubgrupos_productos> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.id_subgrupo_producto, a.descripcion, a.grupo_producto_id, b.descripcion FROM subgrupos_productos a INNER JOIN grupos_productos b ON (a.grupo_producto_id = b.id_grupo_producto);");
            while (resultado.next()){
                lista.add(new MSubgrupos_productos(resultado.getInt("id_subgrupo_producto"),
                        resultado.getString("descripcion"),
                        new MGrupos_productos(resultado.getInt("grupo_producto_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into subgrupos_productos(id_subgrupo_producto, descripcion,grupo_producto_id) values(?,?,?)");
            instruccion.setInt(1,codMun.get());
            instruccion.setString(2, desMun.get());
            instruccion.setInt(3,grupo_producto.getCodMun());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE subgrupos_productos SET descripcion = ?, grupo_producto_id = ? WHERE id_subgrupo_producto = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,grupo_producto.getCodMun());
            instruccion.setInt(3,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM subgrupos_productos WHERE id_subgrupo_producto = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MSubgrupos_productos> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM subgrupos_productos WHERE (descripcion LIKE ? or id_subgrupo_producto=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MSubgrupos_productos(resultado.getInt("id_subgrupo_producto"),
                        resultado.getString("descripcion"),
                        new MGrupos_productos(resultado.getInt("grupo_producto_id"), resultado.getString("descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_subgrupo_producto) FROM subgrupos_productos");
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
