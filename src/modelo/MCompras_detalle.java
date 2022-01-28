package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MCompras_detalle {
    private MFacturas_compras compras;
    private MProductos productos;
    private DoubleProperty cant;
    private DoubleProperty precio;
    private DoubleProperty sub;


    public MCompras_detalle(MFacturas_compras compras, MProductos productos, Double cant, Double precio, Double sub) {
        this.compras = compras;
        this.productos = productos;
        this.cant = new SimpleDoubleProperty(cant);
        this.precio = new SimpleDoubleProperty(precio);
        this.sub = new SimpleDoubleProperty(sub);
    }

    public MCompras_detalle(MProductos productos, Double cant, Double precio, Double sub) {
        this.productos = productos;
        this.cant = new SimpleDoubleProperty(cant);
        this.precio = new SimpleDoubleProperty(precio);
        this.sub = new SimpleDoubleProperty(sub);
    }


    public MCompras_detalle(){};

    public MFacturas_compras getCompras() { return compras; }

    public void setCompras(MFacturas_compras compras) { this.compras = compras; }

    public MProductos getProductos() { return productos; }

    public void setproductos(MProductos productos) { this.productos = productos; }

    public double getCant() { return cant.get(); }

    public DoubleProperty cantProperty() { return cant; }

    public void setCant(double cant) { this.cant.set(cant); }

    public double getPrecio() { return precio.get(); }

    public DoubleProperty precioProperty() { return precio; }

    public void setPrecio(double precio) { this.precio.set(precio); }

    public double getSub() { return sub.get(); }

    public DoubleProperty subProperty() { return sub; }

    public void setSub(double sub) { this.sub.set(sub); }
    

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MCompras_detalle> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.compra_id, a.producto_id, a.cantidad, a.precio, a.subtotal FROM compras_detalle a " +
                    "INNER JOIN compras b ON (a.compra_id = b.id_compra) " +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto);");
            while (resultado.next()){
                lista.add(new MCompras_detalle(
                        new MFacturas_compras(resultado.getInt("a.compra_id")),
                        new MProductos(resultado.getInt("a.producto_id"),resultado.getString("c.nombre")),
                        resultado.getDouble("a.cantidad"),
                        resultado.getDouble("a.precio"),
                        resultado.getDouble("a.subtotal")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO compras_detalle(compra_id,producto_id,cantidad,precio,subtotal) values(?,?,?,?,?);");
            instruccion.setInt(1, compras.getCodFact());
            instruccion.setInt(2, productos.getCodMun());
            instruccion.setDouble(3, cant.get());
            instruccion.setDouble(4, precio.get());
            instruccion.setDouble(5, sub.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE compras_detalle SET producto_id = ?, precio = ?," +
                    " cantidad = ?, subtotal = ? " +
                    " WHERE compra_id = ?");
            instruccion.setInt(1, productos.getCodMun());
            instruccion.setDouble(2, precio.get());
            instruccion.setDouble(3, cant.get());
            instruccion.setDouble(4, sub.get());
            instruccion.setInt(5,compras.getCodFact());
            //VALIDAR
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int anularRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM compras_detalle WHERE id_factura = ?");
            instruccion.setInt(1,getCompras().getCodFact());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection conncection, ObservableList<MCompras_detalle> lista, String filtro) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, c.id_producto, c.nombre FROM compras_detalle a " +
                    "INNER JOIN compras b ON (a.compra_id = b.id_compra) " +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto)" +
                    "WHERE (compra_id ='"+ filtro +"');");
            while (resultado.next()){
                lista.add(new MCompras_detalle(
                        new MFacturas_compras(resultado.getInt("a.compra_id")),
                        new MProductos(resultado.getInt("a.producto_id"),resultado.getString("c.nombre")),
                        resultado.getDouble("a.cantidad"),
                        resultado.getDouble("a.precio"),
                        resultado.getDouble("a.subtotal")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




/*
    @Override
    public String toString() {
        return codMun.get() + " - " + desMun.get();
    }*/
}
