package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MPedidos {
    private IntegerProperty codMun;
    private Date fecMun;
    private Time horMun;
    private DoubleProperty totalMun;
    private IntegerProperty numMun;
    private MEstados estado;
    private MMesas mesas;
    private MDeliverys delivery;
    private MFuncionarios funcionario;

    public MPedidos(Integer codMun, Date fecMun, Time horMun, Double totalMun, Integer numMun, MEstados estado, MMesas mesas, MDeliverys delivery, MFuncionarios funcionario){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.fecMun = fecMun;
        this.horMun = horMun;
        this.totalMun = new SimpleDoubleProperty(totalMun);
        this.numMun = new SimpleIntegerProperty(numMun);
        this.estado = estado;
        this.mesas = mesas;
        this.delivery = delivery;
        this.funcionario = funcionario;
    }

    public MPedidos(Integer codMun, Date fecMun, Time horMun, Double totalMun, Integer numMun, MEstados estado, MMesas mesas, MFuncionarios funcionario){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.fecMun = fecMun;
        this.horMun = horMun;
        this.totalMun = new SimpleDoubleProperty(totalMun);
        this.numMun = new SimpleIntegerProperty(numMun);
        this.estado = estado;
        this.mesas = mesas;
        this.funcionario = funcionario;
    }

    public MPedidos(Integer codMun, Date fecMun, Time horMun, Double totalMun, Integer numMun, MEstados estado, MMesas mesas, MDeliverys delivery){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.fecMun = fecMun;
        this.horMun = horMun;
        this.totalMun = new SimpleDoubleProperty(totalMun);
        this.numMun = new SimpleIntegerProperty(numMun);
        this.estado = estado;
        this.mesas = mesas;
        this.delivery = delivery;
    }

    public MPedidos(Integer codMun, Date fecMun, Time horMun, Double totalMun, Integer numMun, MEstados estado, MMesas mesas){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.fecMun = fecMun;
        this.horMun = horMun;
        this.totalMun = new SimpleDoubleProperty(totalMun);
        this.numMun = new SimpleIntegerProperty(numMun);
        this.estado = estado;
        this.mesas = mesas;
    }

    public MPedidos(Integer codMun, Double totalMun, Integer numMun, MMesas mesas, MEstados estado){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.totalMun = new SimpleDoubleProperty(totalMun);
        this.numMun = new SimpleIntegerProperty(numMun);
        this.mesas = mesas;
        this.estado = estado;
    }

    public MPedidos(Integer codMun, Integer numMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.numMun = new SimpleIntegerProperty(numMun);
    }

    public MPedidos(Integer codMun){
        this.codMun = new SimpleIntegerProperty(codMun);
    }


    public MMesas getMesas() { return mesas; }

    public void setMesas(MMesas mesas) { this.mesas = mesas; }

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public Date getFecMun() { return fecMun; }

    public void setFecMun(Date fecMun) { this.fecMun = fecMun; }

    public Time getHorMun() { return horMun; }

    public void setHorMun(Time horMun) { this.horMun = horMun; }

    public double getTotalMun() { return totalMun.get(); }

    public DoubleProperty totalMunProperty() { return totalMun; }

    public void setTotalMun(double totalMun) { this.totalMun.set(totalMun); }

    public int getNumMun() { return numMun.get(); }

    public IntegerProperty numMunProperty() { return numMun; }

    public void setNumMun(int numMun) { this.numMun.set(numMun); }

    public MDeliverys getDelivery() {return delivery;}

    public void setDelivery(MDeliverys delivery) {this.delivery = delivery;}

    public MFuncionarios getFuncionario() {return funcionario;}

    public void setFuncionario(MFuncionarios funcionario) {this.funcionario = funcionario;}

    public MPedidos(){};

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MPedidos> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.id_estado, c.descripcion, d.id_delivery, d.direccion, e.id_funcionario, e.descripcion FROM pedidos a " +
                    "INNER JOIN mesas b ON (a.mesa_id = b.id_mesa) " +
                    "INNER JOIN estados c ON (a.estado_id = c.id_estado) " +
                    "LEFT JOIN deliverys d ON (a.delivery_id = d.id_delivery) " +
                    "LEFT JOIN funcionarios e ON (a.funcionario_id = e.id_funcionario) " +
                    "ORDER BY id_pedido");
            while (resultado.next()){
                lista.add(new MPedidos(resultado.getInt("id_pedido"),
                        resultado.getDate("fecha"),
                        resultado.getTime("hora"),
                        resultado.getDouble("total"),
                        resultado.getInt("numero"),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion")),
                        new MMesas(resultado.getInt("a.mesa_id"), resultado.getString("b.descripcion")),
                        new MDeliverys(resultado.getInt("a.delivery_id"), resultado.getString("d.direccion")),
                        new MFuncionarios(resultado.getInt("a.funcionario_id"), resultado.getString("e.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection, String st){
        try {
            // insertar datos cabecera
            PreparedStatement instruccion = connection.prepareStatement("insert into pedidos(id_pedido, fecha, hora, total, numero, mesa_id, estado_id)" +
                    " values(?,?,?,?,?,?,?);");
            switch (st){
                case "P":
                    System.out.println("Ingresando Pedidos normal");

                    break;
                case "D":
                    instruccion = connection.prepareStatement("insert into pedidos(id_pedido, fecha, hora, total, numero, mesa_id, estado_id, delivery_id)" +
                            " values(?,?,?,?,?,?,?,?);");
                    instruccion.setInt(8, delivery.getCodDel());
                    System.out.println("Ingresando Pedidos/Deliverys");
                    break;
                case "F":
                    instruccion = connection.prepareStatement("insert into pedidos(id_pedido, fecha, hora, total, numero, mesa_id, estado_id, funcionario_id)" +
                            " values(?,?,?,?,?,?,?,?);");
                    instruccion.setInt(8, funcionario.getCodFun());
                    System.out.println("Ingresando Pedidos/funcionarios");
                    break;
                case "DF":
                    instruccion = connection.prepareStatement("insert into pedidos(id_pedido, fecha, hora, total, numero, mesa_id, estado_id, delivery_id, funcionario_id)" +
                            " values(?,?,?,?,?,?,?,?,?);");
                    instruccion.setInt(8, delivery.getCodDel());
                    instruccion.setInt(9, funcionario.getCodFun());
                    System.out.println("Ingresando Pedidos total");
                    break;
            }

            if (mesas == null){
                instruccion.setString(6, "default");
                System.out.println("Mesa Null");
            } else {
                instruccion.setInt(6,mesas.getCodMun());
            }
            instruccion.setInt(1, codMun.get());
            instruccion.setDate(2, fecMun);
            instruccion.setTime(3, horMun);
            instruccion.setDouble(4,totalMun.get());
            instruccion.setInt(5,numMun.get());
            instruccion.setInt(7, estado.getCodMun());

            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE pedidos SET total = ?, numero = ?, mesa_id = ?, estado_id = ? WHERE id_pedido = ?");
            instruccion.setDouble(1,totalMun.get());
            instruccion.setInt(2,numMun.get());
            instruccion.setInt(3,mesas.getCodMun());
            instruccion.setInt(4, estado.getCodMun());
            instruccion.setInt(5, codMun.get());

            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE pedidos SET estado_id = 2 WHERE id_pedido = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
                // inactivar registro y detalle
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MPedidos> lista, String filtro) {
        try {
             PreparedStatement instruccion = connection.prepareStatement("SELECT a.id_pedido, a.fecha, a.hora, a.total, a.numero, a.mesa_id, b.descripcion, a.estado_id, c.descripcion FROM pedidos a" +
                     "INNER JOIN mesas b ON (a.mesa_id = b.id_mesa) " +
                     "INNER JOIN estados c ON (a.estado_id = c.id_estado)" +
                     "LEFT JOIN deliverys d ON (a.delivery_id = d.id_delivery) " +
                     "LEFT JOIN funcionarios e ON (a.funcionario_id = e.id_funcionario) " +
                     "WHERE  (id_pedido =?) or (numero LIKE ?) or (fecha LIKE ?) order by id_pedido");
            instruccion.setString(1, filtro);
            instruccion.setString(2, "%" + filtro + "%");
            instruccion.setString(3, "%" + filtro + "%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MPedidos(resultado.getInt("id_pedido"),
                        resultado.getDate("fecha"),
                        resultado.getTime("hora"),
                        resultado.getDouble("total"),
                        resultado.getInt("numero"),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("c.descripcion")),
                        new MMesas(resultado.getInt("a.mesa_id"), resultado.getString("b.descripcion")),
                        new MDeliverys(resultado.getInt("a.delivery_id"), resultado.getString("d.direccion")),
                        new MFuncionarios(resultado.getInt("a.funcionario_id"), resultado.getString("e.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MPedidos obtenerPedido(Connection connection, String cod) {
        MPedidos p = null;
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.id_pedido, a.numero FROM pedidos a " +
                    "WHERE (a.id_pedido =?) ORDER BY a.id_pedido");
            instruccion.setString(1, cod);
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()){
                System.out.println("Encontro coincidencias de pedidos");
                p = new MPedidos(resultado.getInt("id_pedido"),
                        resultado.getInt("numero"));
            } else {
                System.out.println("No se ha encontrado coincidencias de Pedidos");
                return p;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return p;
    }


    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_pedido) FROM pedidos");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static int ultNum(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(numero) FROM pedidos");
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
        return codMun.get() + " - " + numMun.get();
    }
}
