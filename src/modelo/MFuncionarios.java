package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;


import java.sql.*;

public class MFuncionarios {
    private IntegerProperty codFun;
    private StringProperty desFun;
    private MCargos cargos;

    public MFuncionarios(Integer codFun, String desFun, MCargos cargos){
        this.codFun = new SimpleIntegerProperty(codFun);
        this.desFun = new SimpleStringProperty(desFun);
        this.cargos = cargos;
    }
    public MFuncionarios(Integer codFun, String desFun){
        this.codFun = new SimpleIntegerProperty(codFun);
        this.desFun = new SimpleStringProperty(desFun);
    }

    public MFuncionarios(){};

    public MCargos getCargos() { return cargos; }

    public void setCargos(MCargos cargos) { this.cargos = cargos; }

    public int getCodFun() { return codFun.get(); }

    public IntegerProperty codFunProperty() { return codFun; }

    public void setCodFun(int codFun) { this.codFun.set(codFun); }

    public String getDesFun() { return desFun.get(); }

    public StringProperty desFunProperty() { return desFun; }

    public void setDesFun(String desFun) { this.desFun.set(desFun); }


    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MFuncionarios> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.id_funcionario, a.descripcion, a.cargo_id, b.descripcion FROM funcionarios a INNER JOIN cargos b ON (a.cargo_id = b.id_cargo);");
            while (resultado.next()){
                lista.add(new MFuncionarios(resultado.getInt("id_funcionario"),
                        resultado.getString("descripcion"),
                        new MCargos(resultado.getInt("cargo_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into funcionarios(descripcion,cargo_id) values(?,?);"); // evita injeccion sql
            instruccion.setString(1, desFun.get());
            instruccion.setInt(2,cargos.getcodCargo());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE funcionarios SET descripcion = ?, cargo_id = ? WHERE id_funcionario = ?");
            instruccion.setString(1, desFun.get());
            instruccion.setInt(2,cargos.getcodCargo());
            instruccion.setInt(3,codFun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM funcionarios WHERE id_funcionario = ?");
            instruccion.setInt(1,codFun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection conncection, ObservableList<MFuncionarios> lista, String filtro) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM funcionarios WHERE (id_funcionario ='"+ filtro +"' or descripcion LIKE '%"+filtro+"%')");

            while (resultado.next()){
                lista.add(new MFuncionarios(resultado.getInt("id_funcionario"),
                        resultado.getString("descripcion"),
                        new MCargos(resultado.getInt("cargo_id"), resultado.getString("descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void buscarRegistro(Connection connection, ObservableList<MFuncionarios> lista, String des){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM funcionarios WHERE (descripcion LIKE '%"+ des +"%')");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MFuncionarios(resultado.getInt("id_funcionario"),
                        resultado.getString("descripcion"),
                        new MCargos(resultado.getInt("cargo_id"), resultado.getString("descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void buscarMozo(Connection connection, ObservableList<MFuncionarios> lista){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM funcionarios a " +
                    "INNER JOIN cargos b ON (a.cargo_id = b.id_cargo) WHERE (b.descripcion LIKE '%Mozo%')");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MFuncionarios(resultado.getInt("a.id_funcionario"),
                        resultado.getString("a.descripcion"),
                        new MCargos(resultado.getInt("a.cargo_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //llenar combo box busqueda
    public static void buscarRegreducido(Connection connection, ObservableList<MFuncionarios> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT id_funcionario, descripcion FROM funcionarios " +
                    "WHERE (descripcion LIKE ? OR id_funcionario = ?) and (cargo_id = 3) ORDER BY id_funcionario  LIMIT 10");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MFuncionarios(resultado.getInt("id_funcionario"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_funcionario) FROM funcionarios");
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
        return codFun.get() + " - " + desFun.get();
    }
}
