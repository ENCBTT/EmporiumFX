package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MAjustes_precios {
    private IntegerProperty codAp;
    private MProductos producto;
    private MUsuarios usuario;
    private StringProperty hisAp;
    private Date fecAp;
    private DoubleProperty valAp;
    private IntegerProperty tprAp;


    public MAjustes_precios(Integer codAp, MProductos producto, MUsuarios usuario, String hisAp, Date fecAp, Double valAp, Integer tprAp){
        this.codAp = new SimpleIntegerProperty(codAp);
        this.producto = producto;
        this.usuario = usuario;
        this.hisAp = new SimpleStringProperty(hisAp);
        this.fecAp = fecAp;
        this.valAp = new SimpleDoubleProperty(valAp);
        this.tprAp = new SimpleIntegerProperty(tprAp);
    }
    public MAjustes_precios(MProductos producto, MUsuarios usuario, String hisAp, Date fecAp, Double valAp, Integer tprAp){
        this.producto = producto;
        this.usuario = usuario;
        this.hisAp = new SimpleStringProperty(hisAp);
        this.fecAp = fecAp;
        this.valAp = new SimpleDoubleProperty(valAp);
        this.tprAp = new SimpleIntegerProperty(tprAp);
    }

    public MAjustes_precios(){}

    public int getCodAp() {return codAp.get();}

    public IntegerProperty codApProperty() {return codAp;}

    public void setCodAp(int codAp) {this.codAp.set(codAp);}

    public MProductos getProducto() {return producto;}

    public void setProducto(MProductos producto) {this.producto = producto;}

    public MUsuarios getUsuario() {return usuario;}

    public void setUsuario(MUsuarios usuario) {this.usuario = usuario;}

    public String getHisAp() {return hisAp.get();}

    public StringProperty hisApProperty() {return hisAp;}

    public void setHisAp(String hisAp) {this.hisAp.set(hisAp);}

    public Date getFecAp() {return fecAp;}

    public void setFecAp(Date fecAp) {this.fecAp = fecAp;}

    public double getValAp() {return valAp.get();}

    public DoubleProperty valApProperty() {return valAp;}

    public void setValAp(double valAp) {this.valAp.set(valAp);}

    public int getTprAp() {return tprAp.get();}

    public IntegerProperty tprApProperty() {return tprAp;}

    public void setTprAp(int tprAp) {this.tprAp.set(tprAp);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MAjustes_precios> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.* FROM ajustes_precios a " +
                    "INNER JOIN productos b ON (a.producto_id = b.id_producto) " +
                    "INNER JOIN usuarios c ON (a.usuario_id = c.id_usuario) ORDER BY id_ajuste_precio");
            while (resultado.next()){
                lista.add(new MAjustes_precios(resultado.getInt("id_ajuste_precio"),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("c.login")),
                        resultado.getString("historico"), resultado.getDate("fecha"), resultado.getDouble("valor"),
                        resultado.getInt("tipo_precio")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into ajustes_precios(id_ajuste_precio, producto_id, usuario_id, historico, fecha, valor, tipo_precio) values(default,?,?,?,?,?,?)");
            instruccion.setInt(1, producto.getCodMun());
            instruccion.setInt(2, usuario.getCodUsr());
            instruccion.setString(3, hisAp.get());
            instruccion.setDate(4, fecAp);
            instruccion.setDouble(5, valAp.get());
            instruccion.setInt(6, tprAp.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int guardarRegistroFact(Connection connection){
        int ret = 0;
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into ajustes_precios(id_ajuste_precio, producto_id, usuario_id, historico, fecha, valor, tipo_precio) values(default,?,?,?,?,?,?)");
            instruccion.setInt(1, producto.getCodMun());
            instruccion.setInt(2, usuario.getCodUsr());
            instruccion.setString(3, hisAp.get());
            instruccion.setDate(4, fecAp);
            instruccion.setDouble(5, valAp.get());
            instruccion.setInt(6, tprAp.get());
            ret = instruccion.executeUpdate();
            if (ret == 1 && tprAp.get()<2){
                PreparedStatement instruccion2;
                switch (tprAp.get()) {
                    case 0 -> instruccion2 = connection.prepareStatement("UPDATE productos SET precio_costo = ? WHERE id_producto = ?");
                    case 1 -> instruccion2 = connection.prepareStatement("UPDATE productos SET precio_venta = ? WHERE id_producto = ?");
                    default -> throw new IllegalStateException("Unexpected value: " + tprAp.get() + "no se edito precio de producto");
                }
                instruccion2.setDouble(1,valAp.get());
                instruccion2.setInt(2,producto.getCodMun());
                instruccion2.executeUpdate();
            }
            return ret;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE ajustes_precios SET producto_id = ?, usuario_id = ?, historico = ?, fecha = ?, valor = ?, tipo_precio = ? WHERE id_ajuste_precio = ?");
            instruccion.setInt(1, producto.getCodMun());
            instruccion.setInt(2, usuario.getCodUsr());
            instruccion.setString(3, hisAp.get());
            instruccion.setDate(4, fecAp);
            instruccion.setDouble(5, valAp.get());
            instruccion.setInt(6, tprAp.get());
            instruccion.setInt(7,codAp.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM ajustes_precios WHERE id_ajuste_precio = ?");
            instruccion.setInt(1,codAp.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MAjustes_precios> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.* FROM ajustes_precios a" +
                    " INNER JOIN productos b ON (a.producto_id = b.id_producto)" +
                    " INNER JOIN usuarios c ON (a.usuario_id = c.id_usuario) WHERE (historico LIKE ? or id_ajuste_precio=?) ORDER BY id_ajuste_precio");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MAjustes_precios(resultado.getInt("id_ajuste_precio"),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("c.login")),
                        resultado.getString("historico"), resultado.getDate("fecha"), resultado.getDouble("valor"),
                        resultado.getInt("tipo_precio")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_ajuste_precio) FROM ajustes_precios");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return codAp.get() + " - " + hisAp.get(); }
}
