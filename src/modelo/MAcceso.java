package modelo;

import controlador.CAcceso;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MAcceso {
    private IntegerProperty codUser;
    private StringProperty login;
    private StringProperty pass;
    private MPuntos_expediciones punto_expedicion;
    private MEstablecimientos establecimiento;

    public MAcceso(Integer codUser, String login, MPuntos_expediciones punto_expedicion, MEstablecimientos establecimiento) {
        this.codUser = new SimpleIntegerProperty(codUser);
        this.login = new SimpleStringProperty(login);
        this.punto_expedicion = punto_expedicion;
        this.establecimiento = establecimiento;
    }

    public MAcceso(String login, String pass, MPuntos_expediciones punto_expedicion, MEstablecimientos establecimiento) {
        this.login = new SimpleStringProperty(login);
        this.pass = new SimpleStringProperty(pass);
        this.punto_expedicion = punto_expedicion;
        this.establecimiento = establecimiento;
    }


    public MAcceso(Integer codUser, String login) {
        this.codUser = new SimpleIntegerProperty(codUser);
        this.login = new SimpleStringProperty(login);
    }


    public int getCodUser() {return codUser.get();}

    public IntegerProperty codUserProperty() {return codUser;}

    public void setCodUser(int codUser) {this.codUser.set(codUser);}

    public String getLogin() {return login.get();}

    public StringProperty loginProperty() {return login;}

    public void setLogin(String login) {this.login.set(login);}

    public String getPass() {return pass.get();}

    public StringProperty passProperty() {return pass;}

    public void setPass(String pass) {this.pass.set(pass);}

    public MPuntos_expediciones getPunto_expedicion() {return punto_expedicion;}

    public void setPunto_expedicion(MPuntos_expediciones punto_expedicion) {this.punto_expedicion = punto_expedicion;}

    public MEstablecimientos getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(MEstablecimientos establecimiento) {
        this.establecimiento = establecimiento;
    }

    public MAcceso entrar(Connection connection) {
            MAcceso a = null;
            try {
                System.out.println("usuario = "+ login.get());
                PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.* FROM usuarios a " +
                        "INNER JOIN establecimientos b ON (b.id_establecimiento = ?)" +
                        "INNER JOIN puntos_expediciones c ON (c.id_expedicion = ?) " +
                        "WHERE login = ? and pass = ? and estado_id = 1 and c.establecimiento_id = b.id_establecimiento");
                instruccion.setInt(1, establecimiento.getCodEst());
                instruccion.setInt(2, punto_expedicion.getCodMun());
                instruccion.setString(3, login.get());
                instruccion.setString(4, pass.get());
                ResultSet result = instruccion.executeQuery();

                if (!result.next()) {
                    a = null;
                } else {
                    pass.set("");
                    System.out.println("id del usuario: "+result.getInt("a.id_usuario"));
                    a = new MAcceso(result.getInt("a.id_usuario"), result.getString("a.login"), new MPuntos_expediciones(result.getInt("c.id_expedicion"), result.getString("c.descripcion")),new MEstablecimientos(result.getInt("b.id_establecimiento"), result.getString("b.descripcion")));
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                System.out.println("Exception");
            }
        return a;
    }

    public MAcceso datosUser(Connection connection){
        MAcceso a = null;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.* FROM usuarios a " +
                    "INNER JOIN establecimientos b ON (b.id_establecimiento = ?)" +
                    "INNER JOIN puntos_expediciones c ON (c.id_expedicion = ?) " +
                    "WHERE login = ? and estado_id = 1 and c.establecimiento_id = b.id_establecimiento");
            instruccion.setInt(1, establecimiento.getCodEst());
            instruccion.setInt(2, punto_expedicion.getCodMun());
            instruccion.setString(3, login.get());
            ResultSet result = instruccion.executeQuery();

            if (!result.next()) {
                System.out.println("Error");
            } else {
                a = new MAcceso(result.getInt("a.id_usuario"), result.getString("a.login"), new MPuntos_expediciones(result.getInt("c.id_expedicion"), result.getString("c.descripcion")),new MEstablecimientos(result.getInt("b.id_establecimiento"), result.getString("b.descripcion")));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println("Exception");
        }

        return a;
    }
}

