package modelo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MFacturas {
    private IntegerProperty codFact;
    private DoubleProperty iva;
    private DoubleProperty total;
    private Date fecFact;
    private Date fecConf;
    private MPedidos pedido;
    private MUsuarios usuario;
    private MClases_facturas clase;
    private MEstados estado;
    private MTimbrados timbrado;
    private MCondiciones_facturas condicion;
    private MClientes cliente;
    private IntegerProperty codTim;
    private IntegerProperty codPed;
    private IntegerProperty codUsr;
    private IntegerProperty codPer;

    public MFacturas(Integer codFact, Double iva, Double total, Date fecFact, Date fecConf, MPedidos pedido,  MUsuarios usuario, MClases_facturas clase, MEstados estado, MTimbrados timbrado, MCondiciones_facturas condicion, MClientes cliente) {
        this.codFact = new SimpleIntegerProperty(codFact);
        this.iva = new SimpleDoubleProperty(iva);
        this.total = new SimpleDoubleProperty(total);
        this.fecFact = fecFact;
        this.fecConf = fecConf;
        this.timbrado = timbrado;
        this.pedido = pedido;
        this.usuario = usuario;
        this.cliente = cliente;
        this.clase = clase;
        this.estado = estado;
        this.condicion = condicion;
    }

    public MFacturas(Integer codFact, Double iva, Double total, Date fecFact, Date fecConf, Integer codTim, Integer codPed, Integer codUsr, Integer codPer, MClases_facturas clase, MEstados estado, MCondiciones_facturas condicion) {
        this.codFact = new SimpleIntegerProperty(codFact);
        this.iva = new SimpleDoubleProperty(iva);
        this.total = new SimpleDoubleProperty(total);
        this.fecFact = fecFact;
        this.fecConf = fecConf;
        this.codTim = new SimpleIntegerProperty(codTim);
        this.codPed = new SimpleIntegerProperty(codPed);
        this.codUsr = new SimpleIntegerProperty(codUsr);
        this.codPer = new SimpleIntegerProperty(codPer);
        this.clase = clase;
        this.estado = estado;
        this.condicion = condicion;
    }
    public MFacturas(Integer codFact, Double iva, Double total, Date fecFact, Date fecConf, MUsuarios usuario, MClases_facturas clase, MEstados estado, MTimbrados timbrado, MCondiciones_facturas condicion, MClientes cliente) {
        this.codFact = new SimpleIntegerProperty(codFact);
        this.iva = new SimpleDoubleProperty(iva);
        this.total = new SimpleDoubleProperty(total);
        this.fecFact = fecFact;
        this.fecConf = fecConf;
        this.timbrado = timbrado;
        this.usuario = usuario;
        this.cliente = cliente;
        this.clase = clase;
        this.estado = estado;
        this.condicion = condicion;
    }


    public MFacturas(){}

    public int getCodFact() { return codFact.get(); }

    public IntegerProperty codFactProperty() { return codFact; }

    public void setCodFact(int codFact) { this.codFact.set(codFact); }

    public double getIva() { return iva.get(); }

    public DoubleProperty ivaProperty() { return iva; }

    public void setIva(double iva) { this.iva.set(iva); }

    public double getTotal() { return total.get(); }

    public DoubleProperty totalProperty() { return total; }

    public void setTotal(double total) { this.total.set(total); }

    public Date getFecFact() { return fecFact; }

    public void setFecFact(Date fecFact) { this.fecFact = fecFact; }

    public Date getFecConf() { return fecConf; }

    public void setFecConf(Date fecConf) { this.fecConf = fecConf; }

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    public MPedidos getPedido() {return pedido;}

    public void setPedido(MPedidos pedido) {this.pedido = pedido;}

    public MUsuarios getUsuario() {return usuario;}

    public void setUsuario(MUsuarios usuario) {this.usuario = usuario;}

    public MClases_facturas getClase() {return clase;}

    public void setClase(MClases_facturas clase) {this.clase = clase;}

    public MTimbrados getTimbrado() {return timbrado;}

    public void setTimbrado(MTimbrados timbrado) {this.timbrado = timbrado;}

    public MCondiciones_facturas getCondicion() {return condicion;}

    public void setCondicion(MCondiciones_facturas condicion) {this.condicion = condicion;}

    public MClientes getCliente() {return cliente;}

    public void setCliente(MClientes cliente) {this.cliente = cliente;}

    public int getCodTim() {return codTim.get();}

    public IntegerProperty codTimProperty() {return codTim;}

    public void setCodTim(int codTim) {this.codTim.set(codTim);}

    public int getCodPed() {return codPed.get();}

    public IntegerProperty codPedProperty() {return codPed;}

    public void setCodPed(int codPed) {this.codPed.set(codPed);}

    public int getCodUsr() {return codUsr.get();}

    public IntegerProperty codUsrProperty() {return codUsr;}

    public void setCodUsr(int codUsr) {this.codUsr.set(codUsr);}

    public int getCodPer() {return codPer.get();}

    public IntegerProperty codPerProperty() {return codPer;}

    public void setCodPer(int codPer) {this.codPer.set(codPer);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MFacturas> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, " +
                    "c.login, d.descripcion, e.descripcion, f.*, g.*, h.*  FROM facturas a " +
                    "LEFT JOIN pedidos b ON (a.pedido_id = b.id_pedido) " +
                    "INNER JOIN usuarios c ON (a.usuario_id = c.id_usuario) " +
                    "INNER JOIN clases_facturas d ON (a.clase_factura_id = d.id_clase_factura) " +
                    "INNER JOIN estados e ON (a.estado_id = e.id_estado) " +
                    "INNER JOIN timbrados f ON (a.timbrado_id = f.id_timbrado) " +
                    "INNER JOIN condiciones_facturas g ON (a.condicion_id = g.id_condicion) " +
                    "INNER JOIN clientes h ON (a.cliente_id = h.id_cliente) " +
                    "ORDER BY id_factura");
            while (resultado.next()){
                lista.add(new MFacturas(resultado.getInt("a.id_factura"),
                        resultado.getDouble("a.iva"),
                        resultado.getDouble("a.total"),
                        resultado.getDate("a.fecha_factura"),
                        resultado.getDate("a.fecha_conf"),
                        new MPedidos(resultado.getInt("a.pedido_id"), resultado.getInt("b.numero")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("c.login")),
                        new MClases_facturas(resultado.getInt("a.clase_factura_id"), resultado.getString("d.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("e.descripcion")),
                        new MTimbrados(resultado.getInt("a.timbrado_id"), resultado.getInt("f.num_timbrado")),
                        new MCondiciones_facturas(resultado.getInt("a.condicion_id"), resultado.getString("g.descripcion")),
                        new MClientes(resultado.getInt("a.cliente_id"), resultado.getString("h.descripcion"), resultado.getString("h.ruc"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion;
            if (getPedido()==null){
                instruccion = connection.prepareStatement("insert into facturas(id_factura, iva, total, fecha_factura, fecha_conf, usuario_id, clase_factura_id, estado_id, timbrado_id, condicion_id, cliente_id)values(?,?,?,?,?,?,?,?,?,?,?)");
                instruccion.setInt(1, codFact.get());
                instruccion.setDouble(2, iva.get());
                instruccion.setDouble(3,total.get());
                instruccion.setDate(4, fecFact);
                instruccion.setDate(5, fecConf);
                instruccion.setInt(6, usuario.getCodUsr());
                instruccion.setInt(7, clase.getCodMun());
                instruccion.setInt(8,estado.getCodMun());
                instruccion.setInt(9, timbrado.getCodMun());
                instruccion.setInt(10, condicion.getCodMun());
                instruccion.setInt(11, cliente.getCodCli());
            }else {
                instruccion = connection.prepareStatement("insert into facturas(id_factura, iva, total, fecha_factura, fecha_conf, pedido_id, usuario_id, clase_factura_id, estado_id, timbrado_id, condicion_id, cliente_id)values(?,?,?,?,?,?,?,?,?,?,?,?)");
                instruccion.setInt(1, codFact.get());
                instruccion.setDouble(2, iva.get());
                instruccion.setDouble(3,total.get());
                instruccion.setDate(4, fecFact);
                instruccion.setDate(5, fecConf);
                instruccion.setInt(6, pedido.getCodMun());
                instruccion.setInt(7, usuario.getCodUsr());
                instruccion.setInt(8, clase.getCodMun());
                instruccion.setInt(9,estado.getCodMun());
                instruccion.setInt(10, timbrado.getCodMun());
                instruccion.setInt(11, condicion.getCodMun());
                instruccion.setInt(12, cliente.getCodCli());
            }

            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("?");
            instruccion.setDouble(1, iva.get());
            instruccion.setDouble(2,total.get());
            instruccion.setDate(3, fecFact);
            instruccion.setDate(4, fecConf);
            instruccion.setInt(5, usuario.getCodUsr());
            instruccion.setInt(6, cliente.getCodCli());
            instruccion.setInt(7, clase.getCodMun());
            instruccion.setInt(8,estado.getCodMun());
            instruccion.setInt(9, condicion.getCodMun());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int anularRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE  facturas set estado_id = 2 WHERE id_factura = ?");
            instruccion.setInt(1,codFact.get());
            return instruccion.executeUpdate();
                // inactivar registro y detalle
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MFacturas> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, " +
                    "c.login, d.descripcion, e.descripcion, f.num_timbrado, g.*, h.*  FROM facturas a " +
                    "LEFT JOIN pedidos b ON (a.pedido_id = b.id_pedido) " +
                    "INNER JOIN usuarios c ON (a.usuario_id = c.id_usuario) " +
                    "INNER JOIN clases_facturas d ON (a.clase_factura_id = d.id_clase_factura) " +
                    "INNER JOIN estados e ON (a.estado_id = e.id_estado) " +
                    "INNER JOIN timbrados f ON (a.timbrado_id = f.id_timbrado) " +
                    "INNER JOIN condiciones_facturas g ON (a.condicion_id = g.id_condicion) " +
                    "INNER JOIN clientes h ON (a.cliente_id = h.id_cliente) " +
                    "WHERE (h.descripcion LIKE ?) OR (a.id_factura = ?) OR (a.fecha_conf LIKE?) ORDER BY id_factura");
            instruccion.setString(1, "%"+filtro+"%");
            instruccion.setString(2, filtro);
            instruccion.setString(3, "%"+filtro+"%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MFacturas(resultado.getInt("a.id_factura"),
                        resultado.getDouble("a.iva"),
                        resultado.getDouble("a.total"),
                        resultado.getDate("a.fecha_factura"),
                        resultado.getDate("a.fecha_conf"),
                        new MPedidos(resultado.getInt("a.pedido_id"), resultado.getInt("b.numero")),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("c.login")),
                        new MClases_facturas(resultado.getInt("a.clase_factura_id"), resultado.getString("d.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("e.descripcion")),
                        new MTimbrados(resultado.getInt("f.num_timbrado")),
                        new MCondiciones_facturas(resultado.getInt("a.condicion_id"), resultado.getString("g.descripcion")),
                        new MClientes(resultado.getInt("a.cliente_id"), resultado.getString("h.descripcion"), resultado.getString("h.ruc"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_factura) FROM facturas");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    /*
    @Override
    public String toString() {
        return codMun.get() + " - " + numTMun.get();
    }*/
}
