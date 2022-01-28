package modelo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class MTimbrados {
    private IntegerProperty codMun;
    private IntegerProperty numTMun;
    private DoubleProperty numAMun;
    private Date fecIMun;
    private Date fecFMun;
    private MEmpresas empresa;


    public MTimbrados(Integer codMun, Integer numTMun, Double numAMun, Date fecIMun, Date fecFMun, MEmpresas empresa){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.numTMun = new SimpleIntegerProperty(numTMun);
        this.numAMun = new SimpleDoubleProperty(numAMun);
        this.fecIMun = fecIMun;
        this.fecFMun = fecFMun;
        this.empresa = empresa;
    }

    public MTimbrados(Integer numTMun){
        this.numTMun = new SimpleIntegerProperty(numTMun);
    }

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public int getNumTMun() { return numTMun.get(); }

    public IntegerProperty numTMunProperty() { return numTMun; }

    public void setNumTMun(int numTMun) { this.numTMun.set(numTMun); }

    public double getNumAMun() { return numAMun.get(); }

    public DoubleProperty numAMunProperty() { return numAMun; }

    public void setNumAMun(int numAMun) { this.numAMun.set(numAMun); }

    public Date getFecIMun() { return fecIMun; }

    public void setFecIMun(Date fecIMun) { this.fecIMun = fecIMun; }

    public Date getFecFMun() { return fecFMun; }

    public void setFecFMun(Date fecFMun) { this.fecFMun = fecFMun; }

    public void setNumAMun(double numAMun) {this.numAMun.set(numAMun);}

    public MEmpresas getEmpresa() {return empresa;}

    public void setEmpresa(MEmpresas empresa) {this.empresa = empresa;}

    public MTimbrados(){};

    public MTimbrados(Integer codMun, Integer numTMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.numTMun = new SimpleIntegerProperty(numTMun);
    }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MTimbrados> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.razon_social FROM timbrados `a`" +
                    "INNER JOIN empresas b ON (a.empresa_id = b.id_empresa) ORDER BY id_timbrado");
            while (resultado.next()){
                lista.add(new MTimbrados(resultado.getInt("id_timbrado"),
                        resultado.getInt("num_timbrado"),
                        resultado.getDouble("num_autorizacion"),
                        resultado.getDate("fecha_vigencia"),
                        resultado.getDate("fecha_fin"),
                        new MEmpresas(resultado.getInt("a.empresa_id"), resultado.getString("b.razon_social"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            // insertar datos cabecera
            PreparedStatement instruccion = connection.prepareStatement("insert into timbrados(id_timbrado, num_timbrado, num_autorizacion, fecha_vigencia, fecha_fin, empresa_id)" +
                    " values(?,?,?,?,?,?);");
            instruccion.setInt(1, codMun.get());
            instruccion.setInt(2,numTMun.get());
            instruccion.setDouble(3,numAMun.get());
            instruccion.setDate(4, fecIMun);
            instruccion.setDate(5, fecFMun);
            instruccion.setInt(6, empresa.getCodEmp());
            //falta insertar datos detalle
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE timbrados SET num_timbrado = ?, num_autorizacion = ?, fecha_vigencia = ?, fecha_fin = ?, id_empresa = ? WHERE id_timbrado = ?");
            instruccion.setInt(1, codMun.get());
            instruccion.setInt(2,numTMun.get());
            instruccion.setDouble(3,numAMun.get());
            instruccion.setDate(4, fecIMun);
            instruccion.setInt(5, empresa.getCodEmp());
            instruccion.setInt(6, codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM timbrados WHERE id_timbrado = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
                // inactivar registro y detalle no eliminar
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MTimbrados> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.razon_social FROM timbrados `a`" +
                    "INNER JOIN empresas b ON (a.empresa_id = b.id_empresa) WHERE (num_timbrado = ? or id_timbrado = ?)ORDER BY id_timbrado;");
            instruccion.setString(1, filtro);
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MTimbrados(resultado.getInt("id_timbrado"),
                        resultado.getInt("num_timbrado"),
                        resultado.getDouble("num_autorizacion"),
                        resultado.getDate("fecha_vigencia"),
                        resultado.getDate("fecha_fin"),
                        new MEmpresas(resultado.getInt("a.empresa_id"), resultado.getString("b.razon_social"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void buscarRegistroFact(Connection connection, ObservableList<MTimbrados> lista) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.* FROM timbrados `a`" +
                    "WHERE (fecha_vigencia < ?  and fecha_fin > ? ) ORDER BY id_timbrado;");
            instruccion.setDate(1, Date.valueOf(LocalDate.now()));
            instruccion.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MTimbrados(resultado.getInt("id_timbrado"),
                        resultado.getInt("num_timbrado")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MTimbrados buscarRegistroFactDIst(Connection connection) {
        MTimbrados t = null;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.* FROM timbrados `a`" +
                    "WHERE (fecha_vigencia < ?  and fecha_fin > ? ) ORDER BY id_timbrado;");
            instruccion.setDate(1, Date.valueOf(LocalDate.now()));
            instruccion.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                t = new MTimbrados(resultado.getInt("id_timbrado"),
                        resultado.getInt("num_timbrado"));
            }else {
                System.out.println("Nos e ha encontrado coincidencias");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return t;
    }


    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_timbrado) FROM timbrados");
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
        return codMun.get() + " - " + numTMun.get();
    }
}
