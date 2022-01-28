package modelo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MRecetas_detalles {
    private MRecetas receta;
    private MProductos producto;
    private DoubleProperty cantidad;

    public MRecetas_detalles(MRecetas receta, MProductos producto, Double cantidad) {
        this.receta = receta;
        this.producto = producto;
        this.cantidad = new SimpleDoubleProperty(cantidad);
    }
    public MRecetas_detalles(MProductos producto, Double cantidad) {
        this.producto = producto;
        this.cantidad = new SimpleDoubleProperty(cantidad);
    }
    public MRecetas_detalles(MRecetas receta, MProductos producto) {
        this.receta = receta;
        this.producto = producto;
    }

    public MRecetas getReceta() {return receta;}

    public void setReceta(MRecetas receta) {this.receta = receta;}

    public MProductos getProducto() {return producto;}

    public void setProducto(MProductos producto) {this.producto = producto;}

    public double getCantidad() {return cantidad.get();}

    public DoubleProperty cantidadProperty() {return cantidad;}

    public void setCantidad(double cantidad) {this.cantidad.set(cantidad);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MRecetas_detalles> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.id_producto, c.nombre FROM detalle_recetas `a`" +
                    "INNER JOIN recetas b ON (a.receta_id = b.id_receta)" +
                    "INNER JOIN productos c ON (a.producto_id = c.id_producto)" +
                    "ORDER BY id_producto");
            while (resultado.next()){
                lista.add(new MRecetas_detalles(
                        new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("b.descripcion")),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("c.nombre")),
                        resultado.getDouble("cantidad")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO detalle_recetas(receta_id, producto_id, cantidad) values(?,?,?)");
            instruccion.setInt(1, receta.getCodMun());
            instruccion.setInt(2, producto.getCodMun());
            instruccion.setDouble(3, cantidad.get());
            /*
            faltan hacer validaciones
            */
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion2;
            //consulta para ver si ya esta registrado
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM detalle_recetas `a`" +
                    "WHERE (receta_id = ? AND producto_id = ?) ORDER BY producto_id");
            instruccion.setInt(1, receta.getCodMun());
            instruccion.setInt(2, producto.getCodMun());
            ResultSet resultado = instruccion.executeQuery();
            // si existe coincidendia se debera alterar el registro existente
            if (resultado.next()){
                if (resultado.getDouble("cantidad") != cantidad.get()) {
                    instruccion2 = connection.prepareStatement("UPDATE detalle_recetas SET cantidad = ? WHERE receta_id = ? and producto_id = ?");
                    instruccion2.setDouble(1, cantidad.get());
                    instruccion2.setInt(2, receta.getCodMun());
                    instruccion2.setInt(3, producto.getCodMun());
                    System.out.println("modificando registro: " + producto.getCodMun());
                    return instruccion2.executeUpdate();
                }else {
                    System.out.println("No se ha modificado, cantidad es " + resultado.getDouble("cantidad") +" Cantidad solicitada es "+ cantidad.get());
                    return 0;
                }
            } else { // caso no tenga una coincidencia el debera registrar un nuevo detalle recetas
                System.out.println("guardando registro: " + producto.getCodMun());
                return guardarRegistro(connection);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("No se pudo realizar ninguna modificacion, registro: " + producto.getCodMun());
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
                PreparedStatement instruccion2;
                PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM detalle_recetas `a`" +
                        "WHERE (receta_id = ? AND producto_id = ?) ORDER BY producto_id");
                instruccion.setInt(1, receta.getCodMun());
                instruccion.setInt(2, producto.getCodMun());
                ResultSet resultado = instruccion.executeQuery();
                // si existe coincidendia se debera eliminar el registro existente
                if (resultado.next()){
                    instruccion2 = connection.prepareStatement("DELETE FROM detalle_recetas WHERE (receta_id = ? and producto = ?)");
                    instruccion2.setInt(1,receta.getCodMun());
                    instruccion2.setInt(2, producto.getCodMun());
                    System.out.println("Eliminando registro: " + producto.getCodMun());
                    return instruccion2.executeUpdate();
                } else { // caso no tenga una coincidencia el debera registrar un nuevo detalle recetas
                    System.out.println("No existe coincidencia");
                    return 0;
                }
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MRecetas_detalles> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.id_producto, c.nombre FROM detalle_recetas `a`" +
                            "INNER JOIN recetas b ON (a.receta_id = b.id_receta) " +
                            "INNER JOIN productos c ON (a.producto_id = c.id_producto) " +
                            "WHERE (a.receta_id = ? OR c.nombre LIKE ?) ORDER BY receta_id");
            instruccion.setString(1, filtro);
            instruccion.setString(2, "%"+filtro+"%");
            ResultSet resultado = instruccion.executeQuery();

            while (resultado.next()){
                lista.add(new MRecetas_detalles(
                        new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("b.descripcion")),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("c.nombre")),
                        resultado.getDouble("cantidad")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return producto.getNomMun() + " - " + cantidad.get();
    }
}
