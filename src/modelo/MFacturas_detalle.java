package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MFacturas_detalle {
    private MFacturas factura;
    private MProductos producto;
    private DoubleProperty cant;
    private DoubleProperty precio;
    private DoubleProperty sub;


    public MFacturas_detalle(MFacturas factura, MProductos producto, Double cant, Double precio, Double sub) {
        this.factura = factura;
        this.producto = producto;
        this.cant = new SimpleDoubleProperty(cant);
        this.precio = new SimpleDoubleProperty(precio);
        this.sub = new SimpleDoubleProperty(sub);
    }
    public MFacturas_detalle(MProductos producto, Double cant, Double precio, Double sub) {
        this.producto = producto;
        this.cant = new SimpleDoubleProperty(cant);
        this.precio = new SimpleDoubleProperty(precio);
        this.sub = new SimpleDoubleProperty(sub);
    }


    public double getCant() { return cant.get(); }

    public DoubleProperty cantProperty() { return cant; }

    public void setCant(double cant) { this.cant.set(cant); }

    public double getPrecio() { return precio.get(); }

    public DoubleProperty precioProperty() { return precio; }

    public void setPrecio(double precio) { this.precio.set(precio); }

    public double getSub() { return sub.get(); }

    public DoubleProperty subProperty() { return sub; }

    public void setSub(double sub) { this.sub.set(sub); }

    public MFacturas getFactura() {return factura;}

    public void setFactura(MFacturas factura) {this.factura = factura;}

    public MProductos getProducto() {return producto;}

    public void setProducto(MProductos producto) {this.producto = producto;}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MFacturas_detalle> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.factura_id, a.producto_id, a.cantidad, a.precio, a.subtotal FROM facturas_detalle a " +
                    "INNER JOIN facturas b ON (a.factura_id = b.id_factura) " +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto);");
            while (resultado.next()){
                lista.add(new MFacturas_detalle(
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("c.nombre")),
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
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO facturas_detalle(factura_id,producto_id,cantidad,precio,subtotal) values(?,?,?,?,?);");
            instruccion.setInt(1, factura.getCodFact());
            instruccion.setInt(2, producto.getCodMun());
            instruccion.setDouble(3, precio.get());
            instruccion.setDouble(4, cant.get());
            instruccion.setDouble(5, sub.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE facturas_detalle SET producto_id = ?, precio = ?," +
                    " cantidad = ?, subtotal = ? " +
                    " WHERE factura_id = ?");
            instruccion.setInt(1, producto.getCodMun());
            instruccion.setDouble(2, precio.get());
            instruccion.setDouble(3, cant.get());
            instruccion.setDouble(4, sub.get());
            instruccion.setInt(5,factura.getCodFact());
            //VALIDAR
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
    public int anularRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM facturas_detalle WHERE id_factura = ?");
            instruccion.setInt(1,getFactura().getCodFact());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MFacturas_detalle> lista, String filtro) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.factura_id, a.producto_id, a.cantidad, a.precio, a.subtotal FROM facturas_detalle a " +
                    "INNER JOIN facturas b ON (a.factura_id = b.id_factura) " +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto)" +
                    "WHERE (factura_id ='"+ filtro +"');");

            while (resultado.next()){
                lista.add(new MFacturas_detalle(
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("c.nombre")),
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
