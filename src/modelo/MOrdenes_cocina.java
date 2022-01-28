package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MOrdenes_cocina {
    private IntegerProperty codMun;
    private MPedidos pedido;
    private MRecetas receta;
    private DoubleProperty cantidad;
    private StringProperty situacion;
    private Time hora;
    private Time horaF;

    public MOrdenes_cocina(Integer codMun, MPedidos pedido, MRecetas receta, Double cantidad, String situacion, Time hora, Time horaF) {
        this.codMun = new SimpleIntegerProperty(codMun);
        this.pedido = pedido;
        this.receta = receta;
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.situacion = new SimpleStringProperty(situacion);
        this.hora = hora;
        this.horaF = horaF;
    }

    public MOrdenes_cocina(MPedidos pedido, MRecetas receta, Double cantidad, Time hora) {
        this.pedido = pedido;
        this.receta = receta;
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.hora = hora;
    }

    public MOrdenes_cocina() {}

    public int getCodMun() {return codMun.get();}

    public IntegerProperty codMunProperty() {return codMun;}

    public void setCodMun(int codMun) {this.codMun.set(codMun);}

    public MPedidos getPedido() {return pedido;}

    public void setPedido(MPedidos pedido) {this.pedido = pedido;}

    public MRecetas getReceta() {return receta;}

    public void setReceta(MRecetas receta) {this.receta = receta;}

    public String getSituacion() {return situacion.get();}

    public StringProperty situacionProperty() {return situacion;}

    public void setSituacion(String situacion) {this.situacion.set(situacion);}

    public Time getHora() {return hora;}

    public void setHora(Time hora) {this.hora = hora;}

    public Time getHoraF() {return horaF;}

    public void setHoraF(Time horaF) {this.horaF = horaF;}

    public double getCantidad() {return cantidad.get();}

    public DoubleProperty cantidadProperty() {return cantidad;}

    public void setCantidad(double cantidad) {this.cantidad.set(cantidad);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MOrdenes_cocina> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.id_pedido, b.numero, c.id_receta, c.descripcion FROM ordenes_cocina a " +
                    "INNER JOIN pedidos b ON (a.detalle_pedido_id = b.id_pedido) " +
                    "INNER JOIN recetas c ON (a.receta_id = c.id_receta) " +
                    "ORDER BY id_orden_cocina");
            while (resultado.next()){
                lista.add(new MOrdenes_cocina(resultado.getInt("id_orden_cocina"),
                        new MPedidos(resultado.getInt("a.detalle_pedido_id"), resultado.getInt("b.numero")),
                        new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion")),
                        resultado.getDouble("a.cantidad"),
                        resultado.getString("a.situacion"),
                        resultado.getTime("a.hora"),
                        resultado.getTime("a.hora_fin")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO ordenes_cocina (id_orden_cocina, detalle_pedido_id, receta_id, situacion, hora, hora_fin) values(default,?,?,default,default,default)");
            instruccion.setInt(1, pedido.getCodMun());
            instruccion.setInt(2, receta.getCodMun());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE ordenes_cocina SET situacion = ? WHERE id_orden_cocina = ?");
            instruccion.setString(1, situacion.get());
            instruccion.setInt(5, codMun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM ordenes_cocina WHERE id_orden_cocina = ?");
            instruccion.setInt(1,getCodMun());
            return instruccion.executeUpdate();
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MOrdenes_cocina> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.id_pedido, b.numero, c.id_receta, c.descripcion FROM ordenes_cocina a " +
                    "INNER JOIN pedidos b ON (a.detalle_pedido_id = b.id_pedido) " +
                    "INNER JOIN recetas c ON (a.receta_id = c.id_receta) " +
                    "WHERE (numero = ? or id_orden_cocina = ? or c.descripcion LIKE ?) ORDER BY id_orden_cocina");
            instruccion.setString(1, filtro);
            instruccion.setString(2, filtro);
            instruccion.setString(3, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MOrdenes_cocina(resultado.getInt("id_orden_cocina"),
                        new MPedidos(resultado.getInt("a.detalle_pedido_id"), resultado.getInt("b.numero")),
                        new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion")),
                        resultado.getDouble("a.cantidad"),
                        resultado.getString("a.situacion"),
                        resultado.getTime("a.hora"),
                        resultado.getTime("a.hora_fin")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*llenar combo box busqueda
    public static void buscarRegreducido(Connection connection, ObservableList<MOrdenes_cocina> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT id_orden_cocina, descripcion FROM ordenes_cocina " +
                    "WHERE (descripcion LIKE ? OR id_orden_cocina = ?) ORDER BY id_orden_cocina  LIMIT 10");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MOrdenes_cocina(resultado.getInt("id_orden_cocina"),
                        resultado.getString("situacion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
*/
    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_orden_cocina) FROM ordenes_cocina");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return pedido.getNumMun() + " - " + receta.getDesMun(); }
}

