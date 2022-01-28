package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MDeliverys {
    private IntegerProperty codDel;
    private StringProperty dirDel;
    private MLocalidades localidad;
    private MMunicipios municipio;
    private MUsuarios usuario;

    public MDeliverys(Integer codDel, String dirDel, MLocalidades localidad, MMunicipios municipio, MUsuarios usuario) {
        this.codDel = new SimpleIntegerProperty(codDel);
        this.dirDel = new SimpleStringProperty(dirDel);
        this.localidad = localidad;
        this.municipio = municipio;
        this.usuario = usuario;
    }

    public MDeliverys(Integer codDel, String dirDel) {
        this.codDel = new SimpleIntegerProperty(codDel);
        this.dirDel = new SimpleStringProperty(dirDel);
    }

    public MDeliverys(){}

    public int getCodDel() {return codDel.get();}

    public IntegerProperty codDelProperty() {return codDel;}

    public void setCodDel(int codDel) {this.codDel.set(codDel);}

    public String getDirDel() {return dirDel.get();}

    public StringProperty dirDelProperty() {return dirDel;}

    public void setDirDel(String dirDel) {this.dirDel.set(dirDel);}

    public MLocalidades getLocalidad() {return localidad;}

    public void setLocalidad(MLocalidades localidad) {this.localidad = localidad;}

    public MMunicipios getMunicipio() {return municipio;}

    public void setMunicipio(MMunicipios municipio) {this.municipio = municipio;}

    public MUsuarios getUsuario() {return usuario;}

    public void setUsuario(MUsuarios usuario) {this.usuario = usuario;}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MDeliverys> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.*, d.id_usuario, d.login FROM deliverys a " +
                    "INNER JOIN localidades b ON (a.localidad_id = b.id_localidad) " +
                    "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                    "INNER JOIN usuarios d ON (a.usuario_id = d.id_usuario) ORDER BY id_delivery");
            while (resultado.next()){
                lista.add(new MDeliverys(resultado.getInt("id_delivery"),
                        resultado.getString("direccion"),
                        new MLocalidades(resultado.getInt("a.localidad_id"), resultado.getString("b.descripcion")),
                        new MMunicipios(resultado.getInt("a.municipio_id"), resultado.getString("c.descripcion")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("d.login"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO deliverys(id_delivery, direccion, localidad_id, municipio_id, usuario_id) values(default,?,?,?,?)");
            //instruccion.setInt(1, codDel.get());
            instruccion.setString(1, dirDel.get());
            instruccion.setInt(2, localidad.getCodMun());
            instruccion.setInt(3, municipio.getCodMun());
            instruccion.setInt(4, usuario.getCodUsr());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE deliverys SET direccion = ?, localidad_id = ?, municipio_id = ?, usuario_id = ? WHERE id_delivery = ?");
            instruccion.setString(1, dirDel.get());
            instruccion.setInt(2, localidad.getCodMun());
            instruccion.setInt(3, municipio.getCodMun());
            instruccion.setInt(4, usuario.getCodUsr());
            instruccion.setInt(5, codDel.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM deliverys WHERE id_delivery = ?");
            instruccion.setInt(1,codDel.get());
            return instruccion.executeUpdate();
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MDeliverys> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.*, d.id_usuario, d.login FROM deliverys a " +
                    "INNER JOIN localidades b ON (a.localidad_id = b.id_localidad) " +
                    "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                    "INNER JOIN usuarios d ON (a.usuario_id = d.id_usuario) WHERE (direccion LIKE ? or id_delivery=?) ORDER BY id_delivery");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MDeliverys(resultado.getInt("id_delivery"),
                        resultado.getString("direccion"),
                        new MLocalidades(resultado.getInt("a.localidad_id"), resultado.getString("b.descripcion")),
                        new MMunicipios(resultado.getInt("a.municipio_id"), resultado.getString("c.descripcion")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("d.login"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //llenar combo box busqueda
    public static void buscarRegreducido(Connection connection, ObservableList<MDeliverys> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT id_delivery, descripcion FROM deliverys " +
                    "WHERE (descripcion LIKE ? OR id_delivery = ?) ORDER BY id_delivery  LIMIT 10");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MDeliverys(resultado.getInt("id_delivery"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_delivery) FROM deliverys");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return codDel.get() + " - " + dirDel.get(); }
}
