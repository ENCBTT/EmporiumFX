package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MClientes {
    private IntegerProperty codCli;
    private StringProperty desCli;
    private StringProperty rucCli;

    public MClientes(Integer codCli, String desCli, String rucCli){
        this.codCli = new SimpleIntegerProperty(codCli);
        this.desCli = new SimpleStringProperty(desCli);
        this.rucCli = new SimpleStringProperty(rucCli);
    }
    public MClientes(){};

    public int getCodCli() { return codCli.get(); }

    public IntegerProperty codCliProperty() { return codCli; }

    public void setCodCli(int codCli) { this.codCli.set(codCli); }

    public String getDesCli() { return desCli.get(); }

    public StringProperty desCliProperty() { return desCli; }

    public void setDesCli(String desCli) { this.desCli.set(desCli); }

    public String getRucCli() { return rucCli.get(); }

    public StringProperty rucCliProperty() { return rucCli; }

    public void setRucCli(String rucCli) { this.rucCli.set(rucCli); }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MClientes> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM clientes");
            while (resultado.next()){
                lista.add(new MClientes(resultado.getInt("id_cliente"),
                        resultado.getString("descripcion"),
                        resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into clientes(descripcion, ruc) values(?,?)");
            instruccion.setString(1, desCli.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE clientes SET descripcion = ? WHERE id_cliente = ?");
            instruccion.setString(1, desCli.get());
            instruccion.setInt(2,codCli.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM clientes WHERE id_cliente = ?");
            instruccion.setInt(1,codCli.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MClientes> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM clientes WHERE (descripcion LIKE ? or id_cliente=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MClientes(resultado.getInt("id_cliente"),
                        resultado.getString("descripcion"), resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MClientes obtenerCliente(Connection connection, String cod) {
        MClientes c = null;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM clientes WHERE (id_cliente=?)");
            instruccion.setString(1, cod);
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                System.out.println("Encontro coincidencias de clientes");
                c = new MClientes(resultado.getInt("id_cliente"),
                        resultado.getString("descripcion"),
                        resultado.getString("ruc"));
            }else {
                System.out.println("no se encontro coincidencias de clientes");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return c;
    }



    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_cliente) FROM clientes");
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
        return codCli.get() + " - " + desCli.get();
    }
}
