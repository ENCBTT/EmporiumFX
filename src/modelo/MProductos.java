package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MProductos {
    private IntegerProperty codMun;
    private StringProperty nomMun;
    private StringProperty desMun;
    private IntegerProperty pcMun;
    private IntegerProperty pvMun;
    private IntegerProperty ivaMun;
    private DoubleProperty stoMun;
    private MSubgrupos_productos subgrupo_producto;
    private MEstados estado;
    private MEmbalajes embalaje;

    public MProductos(Integer codMun, String nomMun, String desMun, Integer pcMun, Integer pvMun, Integer ivaMun, Double stoMun, MSubgrupos_productos subgrupo_producto, MEstados estado, MEmbalajes embalaje){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.nomMun = new SimpleStringProperty(nomMun);
        this.pcMun = new SimpleIntegerProperty(pcMun);
        this.pvMun = new SimpleIntegerProperty(pvMun);
        this.ivaMun = new SimpleIntegerProperty(ivaMun);
        this.stoMun = new SimpleDoubleProperty(stoMun);
        this.subgrupo_producto = subgrupo_producto;
        this.estado = estado;
        this.embalaje = embalaje;
    }

    public  MProductos(Integer codMun, String nomMun, String desMun, Integer pcMun, Integer pvMun, Integer ivaMun, MSubgrupos_productos subgrupo_producto, MEstados estado, MEmbalajes embalaje){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.nomMun = new SimpleStringProperty(nomMun);
        this.pcMun = new SimpleIntegerProperty(pcMun);
        this.pvMun = new SimpleIntegerProperty(pvMun);
        this.ivaMun = new SimpleIntegerProperty(ivaMun);
        this.subgrupo_producto = subgrupo_producto;
        this.estado = estado;
        this.embalaje = embalaje;
    }

    public  MProductos(Integer codMun, String nomMun, String desMun, Integer pvMun, Double stoMun,MSubgrupos_productos subgrupo_producto){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.nomMun = new SimpleStringProperty(nomMun);
        this.pvMun = new SimpleIntegerProperty(pvMun);
        this.stoMun = new SimpleDoubleProperty(stoMun);
        this.subgrupo_producto = subgrupo_producto;
    }

    public  MProductos(Integer codMun, String nomMun, String desMun, Integer pvMun, Integer ivaMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.nomMun = new SimpleStringProperty(nomMun);
        this.pvMun = new SimpleIntegerProperty(pvMun);
        this.ivaMun = new SimpleIntegerProperty(ivaMun);
    }

    public  MProductos(Integer codMun, String nomMun, Integer pvMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.nomMun = new SimpleStringProperty(nomMun);
        this.pvMun = new SimpleIntegerProperty(pvMun);
    }

    public  MProductos(Integer codMun, String nomMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.nomMun = new SimpleStringProperty(nomMun);
    }

    public MProductos(Integer codMun, Integer pcMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.pcMun = new SimpleIntegerProperty(pcMun);
    }

    public MProductos(Integer codMun, Double stoMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.stoMun = new SimpleDoubleProperty(stoMun);
    }

    public MProductos(String nomMun) {
        this.nomMun = new SimpleStringProperty(nomMun);
    }

    public String getNomMun() { return nomMun.get(); }

    public StringProperty nomMunProperty() { return nomMun; }

    public void setNomMun(String nomMun) {  this.nomMun.set(nomMun); }

    public int getPcMun() { return pcMun.get(); }

    public IntegerProperty pcMunProperty() { return pcMun; }

    public void setPcMun(int pcMun) { this.pcMun.set(pcMun); }

    public int getPvMun() { return pvMun.get(); }

    public IntegerProperty pvMunProperty() { return pvMun; }

    public void setPvMun(int pvMun) { this.pvMun.set(pvMun); }

    public int getIvaMun() { return ivaMun.get(); }

    public IntegerProperty ivaMunProperty() { return ivaMun; }

    public void setIvaMun(int ivaMun) { this.ivaMun.set(ivaMun); }

    public double getStoMun() { return stoMun.get(); }

    public DoubleProperty stoMunProperty() { return stoMun; }

    public void setStoMun(int stoMun) { this.stoMun.set(stoMun); }

    public MSubgrupos_productos getSubgrupo_producto() { return subgrupo_producto; }

    public void setSubgrupo_producto(MSubgrupos_productos subgrupo_producto) { this.subgrupo_producto = subgrupo_producto; }

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    public MEmbalajes getEmbalaje() {return embalaje;}

    public void setEmbalaje(MEmbalajes embalaje) {this.embalaje = embalaje;}

    public MProductos(){};

    public int getCodMun() {return codMun.get();}

    public IntegerProperty codMunProperty() {return codMun;}

    public void setCodMun(int codMun) {
        this.codMun.set(codMun);
    }

    public String getDesMun() {return desMun.get();}

    public StringProperty desMunProperty() {
        return desMun;
    }

    public void setDesMun(String desMun) {
        this.desMun.set(desMun);
    }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MProductos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.*, d.* FROM productos a" +
                   " INNER JOIN subgrupos_productos b ON (a.subgrupo_producto_id = b.id_subgrupo_producto)" +
                    " INNER JOIN embalajes d ON (a.embalaje_id = d.id_embalaje)" +
                    " INNER JOIN estados c ON (a.estado_id = c.id_estado) ORDER BY id_producto");
            while (resultado.next()){
                lista.add(new MProductos(resultado.getInt("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion"),
                        resultado.getInt("precio_costo"),
                        resultado.getInt("precio_venta"),
                        resultado.getInt("iva"),
                        resultado.getDouble("stock"),
                        new MSubgrupos_productos(resultado.getInt("a.subgrupo_producto_id"), resultado.getString("b.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion")),
                        new MEmbalajes(resultado.getInt("a.embalaje_id"), resultado.getString("d.descripcion"), resultado.getString("d.sigla"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into productos(id_producto, nombre, descripcion, precio_costo, precio_venta, iva, stock, subgrupo_producto_id, estado_id, embalaje_id)" +
                    " values(?,?,?,?,?,?,?,?,?,?);");
            instruccion.setInt(1, codMun.get());
            instruccion.setString(2, nomMun.get());
            instruccion.setString(3,desMun.get());
            instruccion.setInt(4,pcMun.get());
            instruccion.setInt(5,pvMun.get());
            instruccion.setInt(6,ivaMun.get());
            instruccion.setInt(7,0);
            instruccion.setInt(8,subgrupo_producto.getCodMun());
            instruccion.setInt(9, 1);
            instruccion.setInt(10,embalaje.getCodEmb());
            /*
               TOMAR SALDO DE ACUERDO EL AJUSTe DE STOCK
            */
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE productos SET nombre = ?, descripcion = ?, iva = ?, subgrupo_producto_id = ?, " +
                    "estado_id = ?, embalaje_id = ? WHERE id_producto = ?");
            instruccion.setString(1, nomMun.get());
            instruccion.setString(2, desMun.get());
            instruccion.setInt(3,ivaMun.get());
            instruccion.setInt(4,subgrupo_producto.getCodMun());
            instruccion.setInt(5, estado.getCodMun());
            instruccion.setInt(6, embalaje.getCodEmb());
            instruccion.setInt(7,codMun.get());

            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarPrecio(Connection connection, Integer tip){
        try {
            int ret = 0;
            if (tip == 0){
                PreparedStatement instruccion = connection.prepareStatement("UPDATE productos SET precio_costo = ? WHERE id_producto = ?");
                instruccion.setInt(1,pcMun.get());
                instruccion.setInt(2,codMun.get());
                ret = instruccion.executeUpdate();
            } else {
                PreparedStatement instruccion = connection.prepareStatement("UPDATE productos SET precio_venta = ? WHERE id_producto = ?");
                instruccion.setInt(1,pcMun.get());
                instruccion.setInt(2,codMun.get());
                ret = instruccion.executeUpdate();
            }
            return ret;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarStock(Connection connection){
        try {
                PreparedStatement instruccion = connection.prepareStatement("UPDATE productos SET stock = stock + ? WHERE id_producto = ?");
                instruccion.setDouble(1,stoMun.get());
                instruccion.setInt(2,codMun.get());

            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM productos WHERE id_producto = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MProductos> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.*, d.* FROM productos a" +
                    " INNER JOIN subgrupos_productos b ON (a.subgrupo_producto_id = b.id_subgrupo_producto)" +
                    " INNER JOIN embalajes d ON (a.embalaje_id = d.id_embalaje)" +
                    " INNER JOIN estados c ON (a.estado_id = c.id_estado) " +
                    "WHERE (a.nombre LIKE ? OR a.id_producto = ?) ORDER BY id_producto");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MProductos(resultado.getInt("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion"),
                        resultado.getInt("precio_costo"),
                        resultado.getInt("precio_venta"),
                        resultado.getInt("iva"),
                        resultado.getDouble("stock"),
                        new MSubgrupos_productos(resultado.getInt("a.subgrupo_producto_id"), resultado.getString("b.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion")),
                        new MEmbalajes(resultado.getInt("a.embalaje_id"), resultado.getString("d.descripcion"), resultado.getString("d.sigla"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //llenar combo box busqueda
    public static void buscarRegreducido(Connection connection, ObservableList<MProductos> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT id_producto, nombre, precio_venta FROM productos " +
                    "WHERE (nombre LIKE ? OR id_producto = ?) ORDER BY id_producto  LIMIT 10");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MProductos(resultado.getInt("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getInt("precio_venta")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void buscarRegistroDetalle(Connection connection, ObservableList<MProductos> lista, String des, Integer idG){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.descripcion, c.descripcion FROM productos a " +
                    "INNER JOIN subgrupos_productos b ON (a.subgrupo_producto_id = b.id_subgrupo_producto) " +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado) " +
                    "WHERE (a.nombre Like ? or a.grupo_producto_id =?) and (a.estado_id = 1 and stock > 0) ORDER BY id_producto");
            instruccion.setString(1, "%" + des + "%");
            instruccion.setInt(2, idG);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MProductos(resultado.getInt("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion"),
                        resultado.getInt("precio_venta"),
                        resultado.getDouble("a.stock"),
                        new MSubgrupos_productos(resultado.getInt("a.subgrupo_producto_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean verificarSaldoDetalle(Connection connection, String cod, String cant){ //modificar para verificar saldo por departamento
        boolean b = false;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.descripcion, c.descripcion FROM productos a " +
                    "INNER JOIN subgrupos_productos b ON (a.subgrupo_producto_id = b.id_subgrupo_producto) " +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado) " +
                    "WHERE (a.id_producto = ? and a.stock > ?) ORDER BY id_producto");
            instruccion.setString(1,  cod);
            instruccion.setString(2, cant);
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                System.out.println("saldo disponible producto: " + resultado.getInt("a.id_producto") + " es de: " + resultado.getInt("a.stock"));
            b = true;
            } else {
                System.out.println("saldo indisponible producto: " + resultado.getInt("a.id_producto") + " es de: " + resultado.getInt("a.stock" + " cantidad solicitada es? " + cant));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return b;
    }

    public static void obtenerInfoDet(Connection conncection, ObservableList<MProductos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.descripcion, c.descripcion FROM productos a " +
                    "INNER JOIN subgrupos_productos b ON (a.subgrupo_producto_id = b.id_subgrupo_producto) " +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado) WHERE (a.estado_id = 1) ORDER BY id_producto");
            while (resultado.next()){
                lista.add(new MProductos(
                        resultado.getInt("a.id_producto"),
                        resultado.getString("a.nombre"),
                        resultado.getString("a.descripcion"),
                        resultado.getInt("a.precio_venta"),
                        resultado.getDouble("a.stock"),
                        new MSubgrupos_productos(resultado.getInt("a.subgrupo_producto_id"), resultado.getString("b.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MProductos obtenerProDet(Connection connection, String cod) {
        MProductos p = null;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM productos " +
                    "WHERE (id_producto = ? and estado_id = 1)");
            instruccion.setString(1,  cod);
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                System.out.println("Obtener producto");
                p = new MProductos(
                        resultado.getInt("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion"),
                        resultado.getInt("precio_venta"),
                        resultado.getInt("iva"));
            } else {
                System.out.println("No se encontro coincidencia de productos");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return p;
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_producto) FROM productos");
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
        return codMun.get() + "-" + nomMun.get();
    }
}
