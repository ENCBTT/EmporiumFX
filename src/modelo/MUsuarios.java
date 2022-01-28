package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import java.sql.*;

public class MUsuarios {
    private IntegerProperty codUsr;
    private StringProperty desUsr;
    private StringProperty passUsr;
    private MFuncionarios funcionario;
    private MEstados estado;

    public MUsuarios(Integer codUsr, String desUsr, String passUsr, MFuncionarios funcionario, MEstados estado) {
        this.codUsr = new SimpleIntegerProperty(codUsr);
        this.desUsr = new SimpleStringProperty(desUsr);
        this.passUsr = new SimpleStringProperty(passUsr);
        this.funcionario = funcionario;
        this.estado = estado;
    }
    public MUsuarios(Integer codUsr, String desUsr) {
        this.codUsr = new SimpleIntegerProperty(codUsr);
        this.desUsr = new SimpleStringProperty(desUsr);
    }

    public MUsuarios(String desUsr) {
        this.desUsr = new SimpleStringProperty(desUsr);
    }

    public MUsuarios(){}

    public int getCodUsr() { return codUsr.get(); }

    public IntegerProperty codUsrProperty() { return codUsr; }

    public void setCodUsr(int codUsr) { this.codUsr.set(codUsr); }

    public String getDesUsr() { return desUsr.get(); }

    public StringProperty desUsrProperty() { return desUsr; }

    public void setDesUsr(String desUsr) { this.desUsr.set(desUsr); }

    public String getPassUsr() { return passUsr.get(); }

    public StringProperty passUsrProperty() { return passUsr; }

    public void setPassUsr(String passUsr) { this.passUsr.set(passUsr); }

    public MFuncionarios getFuncionario() { return funcionario; }

    public void setFuncionario(MFuncionarios funcionario) { this.funcionario = funcionario; }

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MUsuarios> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.id_usuario, a.login, a.pass, a.funcionario_id, b.descripcion, a.estado_id, c.descripcion FROM usuarios a " +
                    "INNER JOIN funcionarios b ON (a.funcionario_id = b.id_funcionario)" +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado)" +
                    "ORDER BY a.id_usuario;");
            while (resultado.next()){
                lista.add(new MUsuarios(resultado.getInt("id_usuario"),
                        resultado.getString("login"),
                        resultado.getString("pass"),
                        new MFuncionarios(resultado.getInt("a.funcionario_id"), resultado.getString("b.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into usuarios(id_usuario, login, pass, funcionario_id) values(?,?,?,?);");
            instruccion.setInt(1,codUsr.get());
            instruccion.setString(2, desUsr.get());
            instruccion.setString(3, passUsr.get());
            instruccion.setInt(4,funcionario.getCodFun());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE usuarios SET login = ?, funcionario_id = ?, estado_id = ? WHERE id_usuario = ?");
            instruccion.setString(1, desUsr.get());
            instruccion.setInt(2,funcionario.getCodFun());
            instruccion.setInt(3,estado.getCodMun());
            instruccion.setInt(4, codUsr.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE usuarios SET estado_id = 2 WHERE id_usuario = ?");
            instruccion.setInt(1,codUsr.get());
            return instruccion.executeUpdate();
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoUser(Connection connection, ObservableList<MUsuarios> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.id_usuario, a.login, a.pass, a.funcionario_ia, b.descripcion. estado_id, c.descripcion FROM productos a " +
                    "INNER JOIN funcionarios b ON (a.funcionario_id = b.id_funcionario) " +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado) WHERER (login like ?)");
            instruccion.setString(1, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MUsuarios(resultado.getInt("id_usuario"),
                        resultado.getString("login"),
                        resultado.getString("pass"),
                        new MFuncionarios(resultado.getInt("a.funcionario_id"), resultado.getString("b.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Buscar usuario para formularios

    public static void busquedaInfoMun(Connection connection, ObservableList<MUsuarios> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.id_usuario, a.login, a.pass, a.funcionario_ia, b.descripcion. estado_id, c.descripcion FROM productos a " +
                    "INNER JOIN funcionarios b ON (a.funcionario_id = b.id_funcionario) " +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado) WHERER (id_usuario =? or login like ?)");
            instruccion.setString(1, filtro );
            instruccion.setString(2, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MUsuarios(resultado.getInt("id_usuario"),
                        resultado.getString("login"),
                        resultado.getString("pass"),
                        new MFuncionarios(resultado.getInt("a.funcionario_id"), resultado.getString("b.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_usuario) FROM usuarios");
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
        return codUsr.get() + " - " + desUsr.get();
    }
}
