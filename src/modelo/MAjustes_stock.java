package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MAjustes_stock {
    private IntegerProperty codAs;
    private MEstablecimientos establecimiento;
    private MProductos producto;
    private MTipos_ajustes tipo_ajuste;
    private MUsuarios usuario;
    private Date fecAs;
    private StringProperty hisAs;
    private DoubleProperty canAs;
    //para relacionar con tabla compras y ventas
    private IntegerProperty codFact;

    public MAjustes_stock(Integer codAs, MEstablecimientos establecimiento, MProductos producto, MTipos_ajustes tipo_ajuste, MUsuarios usuario, Date fecAs, String hisAs, Double canAs){
        this.codAs = new SimpleIntegerProperty(codAs);
        this.establecimiento = establecimiento;
        this.producto = producto;
        this.tipo_ajuste = tipo_ajuste;
        this.usuario = usuario;
        this.fecAs = fecAs;
        this.hisAs = new SimpleStringProperty(hisAs);
        this.canAs = new SimpleDoubleProperty(canAs);
    }

    //para relacionar con tabla compras y ventas
    public MAjustes_stock(Integer codAs, MEstablecimientos establecimiento, MProductos producto, MTipos_ajustes tipo_ajuste, MUsuarios usuario, Date fecAs, String hisAs, Double canAs, Integer codFact){
        this.codAs = new SimpleIntegerProperty(codAs);
        this.establecimiento = establecimiento;
        this.producto = producto;
        this.tipo_ajuste = tipo_ajuste;
        this.usuario = usuario;
        this.fecAs = fecAs;
        this.hisAs = new SimpleStringProperty(hisAs);
        this.canAs = new SimpleDoubleProperty(canAs);
        this.codFact = new SimpleIntegerProperty(codFact);
    }
    public MAjustes_stock(MEstablecimientos establecimiento, MProductos producto, MTipos_ajustes tipo_ajuste, MUsuarios usuario, Date fecAs, String hisAs, Double canAs){
        this.establecimiento = establecimiento;
        this.producto = producto;
        this.tipo_ajuste = tipo_ajuste;
        this.usuario = usuario;
        this.fecAs = fecAs;
        this.hisAs = new SimpleStringProperty(hisAs);
        this.canAs = new SimpleDoubleProperty(canAs);
    }

    public MAjustes_stock(){}

    public int getCodAs() {return codAs.get();}

    public IntegerProperty codAsProperty() {return codAs;}

    public void setCodAs(int codAs) {this.codAs.set(codAs);}

    public MEstablecimientos getEstablecimiento() {return establecimiento;}

    public void setEstablecimiento(MEstablecimientos establecimiento) {this.establecimiento = establecimiento;}

    public MProductos getProducto() {return producto;}

    public void setProducto(MProductos producto) {this.producto = producto;}

    public MUsuarios getUsuario() {return usuario;}

    public void setUsuario(MUsuarios usuario) {this.usuario = usuario;}

    public MTipos_ajustes getTipo_ajuste() {return tipo_ajuste;}

    public void setTipo_ajuste(MTipos_ajustes tipo_ajuste) {this.tipo_ajuste = tipo_ajuste;}

    public String getHisAs() {return hisAs.get();}

    public StringProperty hisAsProperty() {return hisAs;}

    public void setHisAs(String hisAs) {this.hisAs.set(hisAs);}

    public Date getFecAs() {return fecAs;}

    public void setFecAs(Date fecAs) {this.fecAs = fecAs;}

    public double getCanAs() {return canAs.get();}

    public DoubleProperty canAsProperty() {return canAs;}

    public void setCanAs(double canAs) {this.canAs.set(canAs);}

    public int getCodFact() {return codFact.get();}

    public IntegerProperty codFactProperty() {return codFact;}

    public void setCodFact(int codFact) {this.codFact.set(codFact);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MAjustes_stock> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, DATE_FORMAT(a.fecha, '%d-%m-%Y'), b.*, c.id_producto, c.nombre, d.*, e.id_usuario, e.login FROM ajustes_stock a " +
                    "INNER JOIN establecimientos b ON (a.establecimiento_id = b.id_establecimiento) " +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto) " +
                    "INNER JOIN tipos_ajustes d ON (a.tipo_ajuste_id = d.id_tipo_ajuste) " +
                    "INNER JOIN usuarios e ON (a.usuario_id = e.id_usuario) ORDER BY id_ajuste_stock");
            while (resultado.next()){
                lista.add(new MAjustes_stock(resultado.getInt("id_ajuste_stock"),
                        new MEstablecimientos(resultado.getInt("a.establecimiento_id"), resultado.getString("b.descripcion")),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("c.nombre")),
                        new MTipos_ajustes(resultado.getInt("a.tipo_ajuste_id"), resultado.getString("d.descripcion"), resultado.getInt("d.clasificacion")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("e.login")),
                        resultado.getDate("fecha"), resultado.getString("historico"), resultado.getDouble("cantidad")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into ajustes_stock(id_ajuste_stock, establecimiento_id, producto_id, tipo_ajuste_id, usuario_id, fecha, historico, cantidad) values(default,?,?,?,?,?,?,?)");
            instruccion.setInt(1, establecimiento.getCodEst());
            instruccion.setInt(2, producto.getCodMun());
            instruccion.setInt(3, tipo_ajuste.getCodMun());
            instruccion.setInt(4, usuario.getCodUsr());
            instruccion.setDate(5, fecAs);
            instruccion.setString(6, hisAs.get());
            instruccion.setDouble(7, canAs.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int guardarRegistroFact(Connection connection){
        int ret = 0;
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO ajustes_stock(id_ajuste_stock, establecimiento_id, producto_id, tipo_ajuste_id, usuario_id, fecha, historico, cantidad) VALUES (?,?,?,?,?,?,?,?)");
            instruccion.setInt(1, codAs.get());
            instruccion.setInt(2, establecimiento.getCodEst());
            instruccion.setInt(3, producto.getCodMun());
            instruccion.setInt(4, tipo_ajuste.getCodMun());
            instruccion.setInt(5, usuario.getCodUsr());
            instruccion.setDate(6, fecAs);
            instruccion.setString(7, hisAs.get());
            instruccion.setDouble(8, canAs.get());
            ret = instruccion.executeUpdate();
            if (ret == 1 ){
                PreparedStatement instruccion2 = connection.prepareStatement("UPDATE productos SET stock = stock + ? WHERE id_producto = ?");
                instruccion2.setDouble(1,canAs.get());
                instruccion2.setInt(2,producto.getCodMun());
                instruccion2.executeUpdate();
                System.out.println("Actualizando stock cod pro: "+producto.getCodMun()+" cantidad = "+canAs.get());
                PreparedStatement instruccion3 = connection.prepareStatement("INSERT INTO ajuste_stock_compras VALUES(?,?,?)");
                instruccion3.setInt(1, codFact.get());
                instruccion3.setInt(2, producto.getCodMun());
                instruccion3.setInt(3, codAs.get());
                instruccion3.executeUpdate();
                System.out.println("Generando relacion cod. fact: "+codFact.get()+" cod.pro: "+producto.getCodMun()+" cod. as: "+codAs.get());
            }
            return ret;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE ajustes_stock SET establecimiento_id = ?, producto_id = ?, tipo_ajuste_id, usuario_id = ?, fecha = ?, historico = ?, cantidad = ? WHERE id_ajuste_stock = ?");
            instruccion.setInt(1, establecimiento.getCodEst());
            instruccion.setInt(2, producto.getCodMun());
            instruccion.setInt(3, tipo_ajuste.getCodMun());
            instruccion.setInt(4, usuario.getCodUsr());
            instruccion.setDate(5, fecAs);
            instruccion.setString(6, hisAs.get());
            instruccion.setDouble(7, canAs.get());
            instruccion.setInt(8,codAs.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM ajustes_stock WHERE id_ajuste_stock = ?");
            instruccion.setInt(1,codAs.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MAjustes_stock> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.id_producto, c.nombre, d.*, e.id_usuario, e.login FROM ajustes_stock a " +
                    "INNER JOIN establecimientos b ON (a.establecimiento_id = b.id_establecimiento) " +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto) " +
                    "INNER JOIN tipos_ajustes d ON (a.tipo_ajuste_id = d.id_tipo_ajuste) " +
                    "INNER JOIN usuarios e ON (a.usuario_id = e.id_usuario) WHERE (historico LIKE ? or id_ajuste_stock=?) ORDER BY id_ajuste_stock");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MAjustes_stock(resultado.getInt("id_ajuste_stock"),
                        new MEstablecimientos(resultado.getInt("a.establecimiento_id"), resultado.getString("b.descripcion")),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("c.nombre")),
                        new MTipos_ajustes(resultado.getInt("a.tipo_ajuste_id"), resultado.getString("d.descripcion"), resultado.getInt("d.clasificacion")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("e.login")),
                        resultado.getDate("fecha"), resultado.getString("historico"), resultado.getDouble("cantidad")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_ajuste_stock) FROM ajustes_stock");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return codAs.get() + " - " + hisAs.get(); }
}
