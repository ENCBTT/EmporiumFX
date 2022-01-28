package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MPuntos_expediciones {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private MEstablecimientos establecimiento;

    public MPuntos_expediciones(Integer codMun, String desMun, MEstablecimientos establecimiento){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.establecimiento = establecimiento;
    }

    public MPuntos_expediciones(Integer codMun, String desMun) {
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public MEstablecimientos getEstablecimiento() { return establecimiento; }

    public void setEstablecimiento(MEstablecimientos Establecimiento) { this.establecimiento = establecimiento; }

    public MPuntos_expediciones(){};

    public int getCodMun() { return codMun.get();    }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() { return desMun.get(); }

    public StringProperty desMunProperty() { return desMun; }

    public void setDesMun(String desMun) { this.desMun.set(desMun); }


    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MPuntos_expediciones> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.id_expedicion, a.descripcion, a.establecimiento_id, b.descripcion FROM puntos_expediciones a " +
                    "INNER JOIN establecimientos b ON (a.establecimiento_id = b.id_establecimiento);");
            while (resultado.next()){
                lista.add(new MPuntos_expediciones(resultado.getInt("id_expedicion"), // usar los prefijos para no tener problemas con los campos con mismos nombres
                        resultado.getString("descripcion"),
                        new MEstablecimientos(resultado.getInt("establecimiento_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into puntos_expediciones(id_expedicion, descripcion,establecimiento_id) values(?,?,?);");
            instruccion.setInt(1, codMun.get());
            instruccion.setString(2, desMun.get());
            instruccion.setInt(3,establecimiento.getCodEst());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE puntos_expediciones SET descripcion = ?, establecimiento_id = ? WHERE id_expedicion = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,establecimiento.getCodEst());
            instruccion.setInt(3,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM puntos_expediciones WHERE id_expedicion = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda

public static void busquedaInfoMun(Connection conncection, ObservableList<MPuntos_expediciones> lista, String filtro) {
        try {
            PreparedStatement instruccion = conncection.prepareStatement("SELECT a.id_expedicion, a.descripcion, a.establecimiento_id, b.descripcion FROM puntos_expediciones a INNER JOIN establecimientos b ON (a.establecimiento_id = b.id_establecimiento) WHERE (a.id_expedicion = ? or a.descripcion LIKE ?)");
            instruccion.setString(1, filtro);
            instruccion.setString(2, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MPuntos_expediciones(resultado.getInt("id_expedicion"),
                        resultado.getString("descripcion"),
                        new MEstablecimientos(resultado.getInt("a.establecimiento_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_expedicion) FROM puntos_expediciones");
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
