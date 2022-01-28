package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MMesas {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private MEstados estado;

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() { return desMun.get(); }

    public StringProperty desMunProperty() { return desMun; }

    public void setDesMun(String desMun) { this.desMun.set(desMun); }

    public MMesas(Integer codMun, String desMun, MEstados estado){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.estado = estado;
    }
    public MMesas(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    };

    public MMesas(){};


    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MMesas> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.id_mesa, a.descripcion, a.estado_id, b.descripcion FROM mesas a INNER JOIN estados b ON (a.estado_id = b.id_estado);");
            while (resultado.next()){
                lista.add(new MMesas(resultado.getInt("id_mesa"), // usar los prefijos para no tener problemas con los campos con mismos nombres
                        resultado.getString("descripcion"),
                        new MEstados(resultado.getInt("estado_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    ///busqueda para solo estados activos
    public static void obtenerInfoMunActivos(Connection conncection, ObservableList<MMesas> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.descripcion FROM mesas a INNER JOIN estados b ON (a.estado_id = b.id_estado)" +
                    " WHERE a.estado_id = 1 or a.estado_id = 3");
            while (resultado.next()){
                lista.add(new MMesas(resultado.getInt("id_mesa"), // usar los prefijos para no tener problemas con los campos con mismos nombres
                        resultado.getString("descripcion"),
                        new MEstados(resultado.getInt("estado_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into mesas(descripcion,estado_id) values(?,?);"); // evita injeccion sql
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,estado.getCodMun());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE mesas SET descripcion = ?, estado_id = ? WHERE id_mesa = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setInt(2,estado.getCodMun());
            instruccion.setInt(3,codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM mesas WHERE id_mesa = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MMesas> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.descripcion FROM mesas a INNER JOIN estados b ON (a.estado_id = b.id_estado) WHERE (a.descripcion LIKE ? or a.id_mesa=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MMesas(resultado.getInt("id_mesa"),
                        resultado.getString("descripcion"),
                        new MEstados(resultado.getInt("estado_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_mesa) FROM mesas");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
// para modificar el estado de la mesa a ocupado
    public int alterarEstadoMesaOff(Connection connection){

            try {
                PreparedStatement instruccion = connection.prepareStatement("UPDATE mesas SET estado_id = 4 WHERE id_mesa = ?");
                instruccion.setInt(1,codMun.get());
                return instruccion.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return 0;
            }
        }

    // para modificar el estado de la mesa a libre
    public int alterarEstadoMesaOn(Connection connection){

        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE mesas SET estado_id = ? WHERE id_mesa = ?");
            instruccion.setInt(1,3);
            instruccion.setInt(2,codMun.get());
            return instruccion.executeUpdate();
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
