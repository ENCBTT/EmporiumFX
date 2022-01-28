package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MEmpresas {
    private IntegerProperty codEmp;
    private StringProperty razEmp;
    private StringProperty rucEmp;

    public MEmpresas(Integer codEmp, String razEmp, String rucEmp){
        this.codEmp = new SimpleIntegerProperty(codEmp);
        this.razEmp = new SimpleStringProperty(razEmp);
        this.rucEmp = new SimpleStringProperty(rucEmp);
    }

    public MEmpresas(Integer codEmp, String razEmp){
        this.codEmp = new SimpleIntegerProperty(codEmp);
        this.razEmp = new SimpleStringProperty(razEmp);
    }

    public MEmpresas(){}

    public int getCodEmp() {return codEmp.get();}

    public IntegerProperty codEmpProperty() {return codEmp;}

    public void setCodEmp(int codEmp) {this.codEmp.set(codEmp);}

    public String getRazEmp() {return razEmp.get();}

    public StringProperty razEmpProperty() {return razEmp;}

    public void setRazEmp(String razEmp) {this.razEmp.set(razEmp);}

    public String getRucEmp() {return rucEmp.get();}

    public StringProperty rucEmpProperty() {return rucEmp;}

    public void setRucEmp(String rucEmp) {this.rucEmp.set(rucEmp);}

    //llenar tabla
    public static void obtenerInfoMun(Connection connection, ObservableList<MEmpresas> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM empresas;");
            while (resultado.next()){
                lista.add(new MEmpresas(resultado.getInt("id_empresa"),
                        resultado.getString("razon_social"),
                        resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO empresas(id_empresa, razon_social, ruc) VALUES(default,?,?)");
            instruccion.setString(1, razEmp.get());
            instruccion.setString(2, rucEmp.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE empresas SET razon_social = ?, ruc = ? WHERE id_empresa = ?");
            instruccion.setString(1, razEmp.get());
            instruccion.setString(2, rucEmp.get());
            instruccion.setInt(3,codEmp.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM empresas WHERE id_empresa = ?");
            instruccion.setInt(1,codEmp.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MEmpresas> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM empresas WHERE (razon_social LIKE ? or id_empresa=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MEmpresas(resultado.getInt("id_empresa"),
                        resultado.getString("razon_social"),
                        resultado.getString("ruc")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_empresa) FROM empresas");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return codEmp.get() + " - " + razEmp.get(); }
}
