package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MPedidos_detalle {
    private MPedidos pedido;
    private MCartas carta;
    private DoubleProperty cant;
    private DoubleProperty precio;
    private DoubleProperty sub;



    public MPedidos_detalle(MPedidos pedido, MCartas carta, Double cant, Double precio, Double sub) {
        this.pedido = pedido;
        this.carta = carta;
        this.cant = new SimpleDoubleProperty(cant);
        this.precio = new SimpleDoubleProperty(precio);
        this.sub = new SimpleDoubleProperty(sub);
    }

    public MPedidos_detalle(MCartas carta, Double cant, Double precio, Double sub) {
        this.carta = carta;
        this.cant = new SimpleDoubleProperty(cant);
        this.precio = new SimpleDoubleProperty(precio);
        this.sub = new SimpleDoubleProperty(sub);
    }

    public MPedidos_detalle(MPedidos pedido, MCartas carta) {
        this.pedido = pedido;
        this.carta = carta;
    }

    public MPedidos_detalle(){};

    public MCartas getCarta() { return carta; }

    public void setCarta(MCartas carta) { this.carta = carta; }

    public double getCant() { return cant.get(); }

    public DoubleProperty cantProperty() { return cant; }

    public void setCant(double cant) { this.cant.set(cant); }

    public double getPrecio() { return precio.get(); }

    public DoubleProperty precioProperty() { return precio; }

    public void setPrecio(double precio) { this.precio.set(precio); }

    public double getSub() { return sub.get(); }

    public DoubleProperty subProperty() { return sub; }

    public void setSub(double sub) { this.sub.set(sub); }

    public MPedidos getPedido() { return pedido; }

    public void setPedido(MPedidos pedido) { this.pedido = pedido; }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MPedidos_detalle> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.id_pedido, b.numero, c.*  FROM pedidos_detalle a " +
                    "INNER JOIN pedidos b ON (a.pedido_id = b.id_pedido) " +
                    "INNER JOIN cartas c ON (a.carta_id = c.id_carta)" +
                    "LEFT JOIN recetas d ON (c.receta_id = d.id_receta) " +
                    "LEFT JOIN productos e ON (c.producto_id = e.id_producto) " +
                    "ORDER BY a.pedidos_id");
            while (resultado.next()){
                lista.add(new MPedidos_detalle(
                        new MPedidos(resultado.getInt("a.pedido_id"), resultado.getInt("b.numero")),
                        new MCartas(resultado.getInt("a.carta_id"), resultado.getString("c.descripcion"), new MRecetas(resultado.getInt("codRec"), resultado.getString("descripcion")), new MProductos(resultado.getInt("codPro"), resultado.getString("nombre"))),
                        resultado.getDouble("a.cantidad"),
                        resultado.getDouble("c.precio"),
                        resultado.getDouble("a.cantidad")*resultado.getDouble("c.precio")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            //Primera instrunccion guarda detalle
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO pedidos_detalle(pedido_id,carta_id,cantidad) values(?,?,?);");
            instruccion.setInt(1, pedido.getCodMun());
            instruccion.setInt(2, carta.getCodCar());
            instruccion.setDouble(3, cant.get());
            //segunda instruccion guarda orden de cocina
            PreparedStatement instruccion2;
            if (carta.getReceta()==null){
                System.out.println("no hay recetas");
            }else {
                System.out.println("Ingresando : " + carta.getReceta() + " pedido: "+ pedido.getCodMun()+ "cantidad: " + cant.get());
             instruccion2 = connection.prepareStatement("INSERT INTO ordenes_cocina(id_orden_cocina, detalle_pedido_id, receta_id, cantidad, hora) values(default,?,?,?,?)");
             instruccion2.setInt(1, pedido.getCodMun());
             instruccion2.setInt(2, carta.getReceta().getCodMun());
             instruccion2.setDouble(3, cant.get());
             instruccion2.setTime(4, Time.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
             instruccion2.executeUpdate();
            }
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion2;
            PreparedStatement instruccion3;
            //consulta para ver si ya esta registrado
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM pedidos_detalle `a`" +
                    "WHERE (pedido_id = ? AND carta_id = ?) ORDER BY pedido_id");
            instruccion.setInt(1, pedido.getCodMun());
            instruccion.setInt(2, carta.getCodCar());
            ResultSet resultado = instruccion.executeQuery();
            // si existe coincidendia se debera alterar el registro existente
            if (resultado.next()){
                if (resultado.getDouble("cantidad") != cant.get()) {
                    instruccion2 = connection.prepareStatement("UPDATE pedidos_detalle SET cantidad = ? WHERE pedido_id = ? and carta_id = ?");
                    instruccion2.setDouble(1, cant.get());
                    instruccion2.setInt(2, pedido.getCodMun());
                    instruccion2.setInt(3, carta.getCodCar());
                    System.out.println("modificando registro: " + carta.getDesCar());
                        if (carta.getReceta() == null){
                            System.out.println("No hay receta para modificar");
                        } else {
                            instruccion3 = connection.prepareStatement("UPDATE ordenes_cocina SET cantidad = ? WHERE detalle_pedido_id= ? and receta_id = ?");
                            instruccion3.setDouble(1, cant.get());
                            instruccion3.setInt(2, pedido.getCodMun());
                            instruccion3.setInt(3, carta.getReceta().getCodMun());
                            instruccion3.executeUpdate();
                            System.out.println("Orden de cocina alterada");
                        }
                    return instruccion2.executeUpdate();
                }else {
                    System.out.println("No se ha modificado, cantidad es " + resultado.getDouble("cantidad") +" Cantidad solicitada es "+ cant.get());
                    return 0;
                }
            } else { // caso no tenga una coincidencia el debera registrar un nuevo detalle recetas
                System.out.println("guardando registro: " + carta.getCodCar());
                return guardarRegistro(connection);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("No se pudo realizar ninguna modificacion, registro: " + carta.getCodCar());
            return 0;
        }
    }
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion3;
            PreparedStatement instruccion2;
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM pedidos_detalle `a`" +
                    "WHERE (pedido_id = ? AND carta_id = ?) ORDER BY carta_id");
            instruccion.setInt(1, pedido.getCodMun());
            instruccion.setInt(2, carta.getCodCar());
            ResultSet resultado = instruccion.executeQuery();
            // si existe coincidendia se debera eliminar el registro existente
            if (resultado.next()){
                instruccion2 = connection.prepareStatement("DELETE FROM pedidos_detalle WHERE (pedido_id = ? and carta_id = ?)");
                instruccion2.setInt(1, pedido.getCodMun());
                instruccion2.setInt(2, carta.getCodCar());
                System.out.println("Eliminando registro: " + carta.getCodCar());
                if (carta.getReceta() == null){
                    System.out.println("No hay receta para modificar");
                } else {
                    instruccion3 = connection.prepareStatement("UPDATE ordenes_cocina SET situacion = cancelado WHERE detalle_pedido_id= ? and receta_id = ?");
                    instruccion3.setInt(1, pedido.getCodMun());
                    instruccion3.setInt(2, carta.getReceta().getCodMun());
                    instruccion3.executeUpdate();
                    System.out.println("Orden de cocina alterada");
                }

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

    public int eliminarRegistro2(Connection connection){
        try {
            PreparedStatement instruccion2;
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM pedidos_detalle `a`" +
                    "WHERE (pedido_id = ? AND carta_id = ?) ORDER BY carta_id");
            instruccion.setInt(1, pedido.getCodMun());
            instruccion.setInt(2, carta.getCodCar());
            ResultSet resultado = instruccion.executeQuery();
            // si existe coincidendia se debera eliminar el registro existente
            if (resultado.next()){
                instruccion2 = connection.prepareStatement("DELETE FROM pedidos_detalle WHERE (pedido_id = ? and carta_id = ?)");
                instruccion2.setInt(1, pedido.getCodMun());
                instruccion2.setInt(2, carta.getCodCar());
                System.out.println("Eliminando registro: " + carta.getCodCar());
                return instruccion2.executeUpdate();
            } else { // caso no tenga una coincidencia el debera eliminar la fila seleccionada
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
    public static void buscarRegistro(Connection connection, ObservableList<MPedidos_detalle> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.id_pedido, b.numero, c.*, d.*, e.*  FROM pedidos_detalle a " +
                    "INNER JOIN pedidos b ON (a.pedido_id = b.id_pedido) " +
                    "INNER JOIN cartas c ON (a.carta_id = c.id_carta)" +
                    "LEFT JOIN recetas d ON (c.receta_id = d.id_receta) " +
                    "LEFT JOIN productos e ON (c.producto_id = e.id_producto) " +
                    "WHERE (d.descripcion = ? OR a.pedido_id = ?) ORDER BY a.pedido_id");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MPedidos_detalle(
                        new MPedidos(resultado.getInt("a.pedido_id"), resultado.getInt("b.numero")),
                        new MCartas(resultado.getInt("a.carta_id"), resultado.getString("c.descripcion"), new MRecetas(resultado.getInt("d.id_receta"), resultado.getString("d.descripcion")), new MProductos(resultado.getInt("e.id_producto"), resultado.getString("e.nombre"))),
                        resultado.getDouble("a.cantidad"),
                        resultado.getDouble("c.precio"),
                        resultado.getDouble("a.cantidad")*resultado.getDouble("c.precio")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return carta.getDesCar() + " - " + cant.get();
    }
}
