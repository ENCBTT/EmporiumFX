package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MCartas {
    private IntegerProperty codCar;
    private StringProperty desCar;
    private StringProperty imgCar;
    private DoubleProperty preCar;
    private MProductos producto;
    private MRecetas receta;

    public MCartas(Integer codCar, String desCar, String imgCar, Double preCar, MProductos producto, MRecetas receta){
        this.codCar = new SimpleIntegerProperty(codCar);
        this.desCar = new SimpleStringProperty(desCar);
        this.imgCar = new SimpleStringProperty(imgCar);
        this.preCar = new SimpleDoubleProperty(preCar);
        this.producto = producto;
        this.receta = receta;
    }

    public MCartas(Integer codCar, String desCar, String imgCar, Double preCar, MProductos producto){
        this.codCar = new SimpleIntegerProperty(codCar);
        this.desCar = new SimpleStringProperty(desCar);
        this.imgCar = new SimpleStringProperty(imgCar);
        this.preCar = new SimpleDoubleProperty(preCar);
        this.producto = producto;
    }

    public MCartas(Integer codCar, String desCar, MRecetas receta, MProductos producto){
        this.codCar = new SimpleIntegerProperty(codCar);
        this.desCar = new SimpleStringProperty(desCar);
        this.receta = receta;
        this.producto = producto;
    }

    public MCartas(Integer codCar, String desCar){
        this.codCar = new SimpleIntegerProperty(codCar);
        this.desCar = new SimpleStringProperty(desCar);
    }

    public MCartas(){};

    public int getCodCar() {return codCar.get();}

    public IntegerProperty codCarProperty() {return codCar;}

    public void setCodCar(int codCar) {this.codCar.set(codCar);}

    public String getDesCar() {return desCar.get();}

    public StringProperty desCarProperty() {return desCar;}

    public void setDesCar(String desCar) {this.desCar.set(desCar);}

    public String getImgCar() {return imgCar.get();}

    public StringProperty imgCarProperty() {return imgCar;}

    public void setImgCar(String imgCar) {this.imgCar.set(imgCar);}

    public double getPreCar() {return preCar.get();}

    public DoubleProperty preCarProperty() {return preCar;}

    public void setPreCar(double preCar) {this.preCar.set(preCar);}

    public MProductos getProducto() {return producto;}

    public void setProducto(MProductos producto) {this.producto = producto;}

    public MRecetas getReceta() {return receta;}

    public void setReceta(MRecetas receta) {this.receta = receta;}

    //llenar tabla
    public static void obtenerInfoMun(Connection connection, ObservableList<MCartas> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.* FROM cartas a" +
                    " INNER JOIN productos b ON (a.producto_id = b.id_producto)" +
                    " LEFT JOIN recetas c ON (a.receta_id = c.id_receta) " +
                    "ORDER BY id_carta");
            while (resultado.next()){
                MRecetas r = new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion"));
                if (r.getCodMun() ==0){
                    System.out.println("receta null");
                    lista.add(new MCartas(resultado.getInt("id_carta"),
                            resultado.getString("descripcion"),
                            resultado.getString("imagen"),
                            resultado.getDouble("precio"),
                            new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre"))));
                } else {
                    lista.add(new MCartas(resultado.getInt("id_carta"),
                    resultado.getString("descripcion"),
                    resultado.getString("imagen"),
                    resultado.getDouble("precio"),
                    new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")),
                    new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion"))));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion;
            if (receta.getCodMun() == 0){
                instruccion = connection.prepareStatement("INSERT INTO cartas(id_carta, descripcion, imagen, precio, producto_id) values(?,?,?,?,?)");
                instruccion.setInt(1, codCar.get());
                instruccion.setString(2, desCar.get());
                instruccion.setString(3,imgCar.get());
                instruccion.setDouble(4,preCar.get());
                instruccion.setInt(5, producto.getCodMun());
                System.out.println("sin receta");
            }else {
                instruccion = connection.prepareStatement("INSERT INTO cartas(id_carta, descripcion, imagen, precio, producto_id, receta_id) values(?,?,?,?,?,?)");
                instruccion.setInt(1, codCar.get());
                instruccion.setString(2, desCar.get());
                instruccion.setString(3,imgCar.get());
                instruccion.setDouble(4,preCar.get());
                instruccion.setInt(5, producto.getCodMun());
                instruccion.setInt(6, receta.getCodMun());
                System.out.println("con receta");
            }
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE cartas SET descripcion = ?, imagen = ?, precio = ?, " +
                    "receta_id = ?, producto_id = ? WHERE id_carta = ?");
            instruccion.setString(1, desCar.get());
            instruccion.setString(2,imgCar.get());
            instruccion.setDouble(3,preCar.get());
            instruccion.setInt(4, producto.getCodMun());
            instruccion.setInt(5, receta.getCodMun());
            instruccion.setInt(6, codCar.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM cartas WHERE id_carta = ?");
            instruccion.setInt(1,codCar.get());
            return instruccion.executeUpdate();
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MCartas> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.* FROM cartas a" +
                            " INNER JOIN productos b ON (a.producto_id = b.id_producto)" +
                            " INNER JOIN recetas c ON (a.receta_id = c.id_receta)" +
                            " WHERE (a.descripcion LIKE ? OR a.id_carta = ?) ORDER BY id_carta");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MCartas(resultado.getInt("id_carta"),
                        resultado.getString("descripcion"),
                        resultado.getString("imagen"),
                        resultado.getDouble("precio"),
                        new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")),
                        new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //llenar combo box busqueda
    public static void buscarRegreducido(Connection connection, ObservableList<MCartas> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.* FROM cartas a" +
                    " INNER JOIN productos b ON (a.producto_id = b.id_producto)" +
                    " LEFT JOIN recetas c ON (a.receta_id = c.id_receta) " +
                    "WHERE (a.descripcion LIKE ? OR a.id_carta = ?) ORDER BY id_carta  LIMIT 10");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                MRecetas r = new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion"));
                if (r.getCodMun() ==0){
                    System.out.println("receta null");
                    lista.add(new MCartas(resultado.getInt("id_carta"),
                            resultado.getString("descripcion"),
                            resultado.getString("imagen"),
                            resultado.getDouble("precio"),
                            new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre"))));
                } else {
                    lista.add(new MCartas(resultado.getInt("id_carta"),
                            resultado.getString("descripcion"),
                            resultado.getString("imagen"),
                            resultado.getDouble("precio"),
                            new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")),
                            new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion"))));
                    System.out.println(resultado.getInt("a.receta_id") +" "+ resultado.getString("c.descripcion"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MCartas obtenerCarta(Connection connection, String filtro) {
        MCartas c = new MCartas();
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.* FROM cartas a" +
                    " INNER JOIN productos b ON (a.producto_id = b.id_producto)" +
                    " LEFT JOIN recetas c ON (a.receta_id = c.id_receta) " +
                    "WHERE (a.id_carta = ?) ORDER BY id_carta");
            instruccion.setString(1, filtro);
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                MRecetas r = new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion"));
                if (r.getCodMun() ==0){
                    System.out.println("receta null");
                    c = new MCartas(resultado.getInt("id_carta"),
                            resultado.getString("descripcion"),
                            resultado.getString("imagen"),
                            resultado.getDouble("precio"),
                            new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")));
                } else {
                    c = new MCartas(resultado.getInt("id_carta"),
                            resultado.getString("descripcion"),
                            resultado.getString("imagen"),
                            resultado.getDouble("precio"),
                            new MProductos(resultado.getInt("a.producto_id"), resultado.getString("b.nombre")),
                            new MRecetas(resultado.getInt("a.receta_id"), resultado.getString("c.descripcion")));
                    System.out.println(resultado.getInt("a.receta_id") +" "+ resultado.getString("c.descripcion"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return c;
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_carta) FROM cartas");
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
        return codCar.get() + " - " + desCar.get();
    }
}
