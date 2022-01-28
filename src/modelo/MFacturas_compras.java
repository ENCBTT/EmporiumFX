package modelo;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.*;

public class MFacturas_compras {
    private IntegerProperty codFact;
    private StringProperty numTim;
    private StringProperty numCom;
    private Date fecFact;
    private Date fecConf;
    private DoubleProperty total;
    private DoubleProperty iva;
    private MUsuarios usuario;
    private MClases_facturas clase;
    private MEstados estado;
    private MCondiciones_facturas condicion;
    private MProveedores proveedor;
    private IntegerProperty codUsr;
    private IntegerProperty codProv;

    public MFacturas_compras(Integer codFact, String numTim, String numCom, Date fecFact, Date fecConf, Double total, Double iva, MUsuarios usuario, MClases_facturas clase, MEstados estado, MCondiciones_facturas condicion, MProveedores proveedor) {
        this.codFact = new SimpleIntegerProperty(codFact);
        this.numTim = new SimpleStringProperty(numTim);
        this.numCom = new SimpleStringProperty(numCom);
        this.iva = new SimpleDoubleProperty(iva);
        this.total = new SimpleDoubleProperty(total);
        this.fecFact = fecFact;
        this.fecConf = fecConf;
        this.usuario = usuario;
        this.proveedor = proveedor;
        this.clase = clase;
        this.estado = estado;
        this.condicion = condicion;
    }

    public MFacturas_compras(Integer codFact, Double iva, Double total, Date fecFact, Date fecConf, Integer codUsr, Integer codPer, MClases_facturas clase, MEstados estado, MCondiciones_facturas condicion) {
        this.codFact = new SimpleIntegerProperty(codFact);
        this.iva = new SimpleDoubleProperty(iva);
        this.total = new SimpleDoubleProperty(total);
        this.fecFact = fecFact;
        this.fecConf = fecConf;
        this.codUsr = new SimpleIntegerProperty(codUsr);
        this.codProv = new SimpleIntegerProperty(codPer);
        this.clase = clase;
        this.estado = estado;
        this.condicion = condicion;
    }

    public MFacturas_compras(Integer codFact, Double iva, Double total, Date fecFact, Integer codUsr, Integer codPer) {
        this.codFact = new SimpleIntegerProperty(codFact);
        this.iva = new SimpleDoubleProperty(iva);
        this.total = new SimpleDoubleProperty(total);
        this.fecFact = fecFact;
        this.codUsr = new SimpleIntegerProperty(codUsr);
        this.codProv = new SimpleIntegerProperty(codPer);
    }

    public MFacturas_compras(Integer codFact) {
        this.codFact = new SimpleIntegerProperty(codFact);
    }

    public MFacturas_compras(){}

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

    public MUsuarios getUsuario() {return usuario;}

    public void setUsuario(MUsuarios usuario) {this.usuario = usuario;}

    public MClases_facturas getClase() {return clase;}

    public void setClase(MClases_facturas clase) {this.clase = clase;}

    public MCondiciones_facturas getCondicion() {return condicion;}

    public void setCondicion(MCondiciones_facturas condicion) {this.condicion = condicion;}

    public MProveedores getProveedor() {return proveedor;}

    public void setProveedor(MProveedores proveedor) {this.proveedor = proveedor;}

    public String getNumTim() {return numTim.get();}

    public StringProperty numTimProperty() {return numTim;}

    public void setNumTim(String numTim) {this.numTim.set(numTim);}

    public String getNumCom() {return numCom.get();}

    public StringProperty numComProperty() {return numCom;}

    public void setNumCom(String numCom) {this.numCom.set(numCom);}

    public void setCodProv(int codProv) {this.codProv.set(codProv);}

    public int getCodUsr() {return codUsr.get();}

    public IntegerProperty codUsrProperty() {return codUsr;}

    public void setCodUsr(int codUsr) {this.codUsr.set(codUsr);}

    public int getCodProv() {return codProv.get();}

    public IntegerProperty codProvProperty() {return codProv;}

    public void setCodPer(int codPer) {this.codProv.set(codPer);}

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MFacturas_compras> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.id_usuario, b.login, c.*, d.*, e.*, f.*  FROM compras a " +
                    "INNER JOIN usuarios b ON (a.usuario_id = b.id_usuario) " +
                    "INNER JOIN clases_facturas c ON (a.clase_factura_id = c.id_clase_factura) " +
                    "iNNER JOIN estados d ON (a.estado_id = d.id_estado) " +
                    "INNER JOIN condiciones_facturas e ON (a.condicion_id = e.id_condicion) " +
                    "INNER JOIN proveedores f ON (a.proveedor_id = f.id_proveedor) " +
                    "ORDER BY a.id_compra");
            while (resultado.next()){
                lista.add(new MFacturas_compras(resultado.getInt("a.id_compra"),
                        resultado.getString("a.timbrado"),
                        resultado.getString("a.num_comp"),
                        resultado.getDate("a.fecha"),
                        resultado.getDate("a.fecha_conf"),
                        resultado.getDouble("a.total"),
                        resultado.getDouble("a.iva"),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("b.login")),
                        new MClases_facturas(resultado.getInt("a.clase_factura_id"), resultado.getString("c.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("d.descripcion")),
                        new MCondiciones_facturas(resultado.getInt("a.condicion_id"), resultado.getString("e.descripcion")),
                        new MProveedores(resultado.getInt("a.proveedor_id"), resultado.getString("f.descripcion"), resultado.getString("f.ruc"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            // insertar datos cabecera
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO compras(id_compra, timbrado, num_comp, fecha, fecha_conf, total, iva, usuario_id, condicion_id, estado_id, clase_factura_id, proveedor_id)values(?,?,?,?,?,?,?,?,?,?,?,?)");
            instruccion.setInt(1, codFact.get());
            instruccion.setString(2,numTim.get());
            instruccion.setString(3, numCom.get());
            instruccion.setDate(4, fecFact);
            instruccion.setDate(5, fecConf);
            instruccion.setDouble(6,total.get());
            instruccion.setDouble(7, iva.get());
            instruccion.setInt(8, usuario.getCodUsr());
            instruccion.setInt(9, condicion.getCodMun());
            instruccion.setInt(10,estado.getCodMun());
            instruccion.setInt(11, clase.getCodMun());
            instruccion.setInt(12, proveedor.getCodPro());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

   public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE compras SET timbrado= ?, num_comp = ?, proveedor_id = ? WHERE id_compra = ?");
            instruccion.setString(1, numTim.get());
            instruccion.setString(2, numCom.get());
            instruccion.setInt(3, proveedor.getCodPro());
            instruccion.setInt(4, codFact.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int anularRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE compras SET estado_id = 2 WHERE id_compra = ?");
            instruccion.setInt(1,codFact.get());

            return instruccion.executeUpdate();
                // inactivar registro y detalle
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MFacturas_compras> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.id_usuario, b.login, c.descripcion, e.descripcion, f.*  FROM compras a " +
                    "INNER JOIN usuarios b ON (a.usuario_id = b.id_usuario) " +
                    "INNER JOIN clases_facturas c ON (a.clase_factura_id = c.id_clase_factura) " +
                    "iNNER JOIN estados d ON (a.estado_id = d.id_estado) " +
                    "INNER JOIN condiciones_facturas e ON (a.condicion_id = e.id_condicion) " +
                    "INNER JOIN proveedores f ON (a.proveedor_id = f.id_proveedor) " +
                    "WHERE (f.descripcion LIKE ?) OR (a.id_compra = ?) OR (a.fecha_conf LIKE?) ORDER BY id_compra");
            instruccion.setString(1, "%"+filtro+"%");
            instruccion.setString(2, filtro);
            instruccion.setString(3, "%"+filtro+"%");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MFacturas_compras(resultado.getInt("a.id_compra"),
                        resultado.getString("a.timbrado"),
                        resultado.getString("a.num_comp"),
                        resultado.getDate("a.fecha"),
                        resultado.getDate("a.fecha_conf"),
                        resultado.getDouble("a.total"),
                        resultado.getDouble("a.iva"),
                        new MUsuarios(resultado.getInt("a.usuario_id"), resultado.getString("b.login")),
                        new MClases_facturas(resultado.getInt("a.clase_factura_id"), resultado.getString("c.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("d.descripcion")),
                        new MCondiciones_facturas(resultado.getInt("a.condicion_id"), resultado.getString("e.descripcion")),
                        new MProveedores(resultado.getInt("a.proveedor_id"), resultado.getString("f.descripcion"), resultado.getString("f.ruc"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_compra) FROM compras");
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
