package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MPersonas {
    private IntegerProperty codMun;
    private StringProperty desMun;
    private StringProperty apeMun;
    private StringProperty ciMun;
    private StringProperty dirMun;
    private StringProperty telMun;
    private MMunicipios municipio;
    private MLocalidades localidad;
    private MNacionalidades nacionalidad;
    private MEstados estado;
    private MClase_persona_det clase_persona;
    private MCargos cargo;
    private MClientes cliente;
    private MFuncionarios funcionario;
    private MProveedores proveedor;

    public MPersonas(){}

    public MPersonas(Integer codMun, String desMun, String apeMun, String ciMun, String dirMun, String telMun, MMunicipios municipio, MLocalidades localidad, MNacionalidades nacionalidad, MEstados estado, MClase_persona_det clase_persona, MFuncionarios funcionario, MClientes cliente, MProveedores proveedor, MCargos cargo){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.apeMun = new SimpleStringProperty(apeMun);
        this.ciMun = new SimpleStringProperty(ciMun);
        this.dirMun = new SimpleStringProperty(dirMun);
        this.telMun = new SimpleStringProperty(telMun);
        this.municipio = municipio;
        this.localidad = localidad;
        this.nacionalidad = nacionalidad;
        this.estado = estado;
        this.clase_persona = clase_persona;
        this.funcionario = funcionario;
        this.cliente = cliente;
        this.proveedor = proveedor;
        this.cargo = cargo;
    }
    public MPersonas(Integer codMun, String desMun, String apeMun, String ciMun, String dirMun, String telMun, MMunicipios municipio, MLocalidades localidad, MNacionalidades nacionalidad, MClase_persona_det clase_persona, MFuncionarios funcionario, MClientes cliente, MProveedores proveedor){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.apeMun = new SimpleStringProperty(apeMun);
        this.ciMun = new SimpleStringProperty(ciMun);
        this.dirMun = new SimpleStringProperty(dirMun);
        this.telMun = new SimpleStringProperty(telMun);
        this.municipio = municipio;
        this.localidad = localidad;
        this.nacionalidad = nacionalidad;
        this.clase_persona = clase_persona;
        this.funcionario = funcionario;
        this.cliente = cliente;
        this.proveedor = proveedor;
    }

    public MPersonas(Integer codMun, String desMun, String apeMun, String ciMun, String dirMun, String telMun, MMunicipios municipio, MLocalidades localidad, MNacionalidades nacionalidad, MEstados estado, MClase_persona_det clase_persona){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.apeMun = new SimpleStringProperty(apeMun);
        this.ciMun = new SimpleStringProperty(ciMun);
        this.dirMun = new SimpleStringProperty(dirMun);
        this.telMun = new SimpleStringProperty(telMun);
        this.municipio = municipio;
        this.localidad = localidad;
        this.nacionalidad = nacionalidad;
        this.estado = estado;
        this.clase_persona = clase_persona;
    }
    public MPersonas(Integer codMun, String desMun, String apeMun, String ciMun, String dirMun, String telMun, MMunicipios municipio, MLocalidades localidad, MNacionalidades nacionalidad, MEstados estado){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.apeMun = new SimpleStringProperty(apeMun);
        this.ciMun = new SimpleStringProperty(ciMun);
        this.dirMun = new SimpleStringProperty(dirMun);
        this.telMun = new SimpleStringProperty(telMun);
        this.municipio = municipio;
        this.localidad = localidad;
        this.nacionalidad = nacionalidad;
        this.estado = estado;
    }

    public MPersonas(Integer codMun, String desMun, String apeMun, String ciMun, String dirMun, String telMun, MMunicipios municipio, MLocalidades localidad, MNacionalidades nacionalidad){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.apeMun = new SimpleStringProperty(apeMun);
        this.ciMun = new SimpleStringProperty(ciMun);
        this.dirMun = new SimpleStringProperty(dirMun);
        this.telMun = new SimpleStringProperty(telMun);
        this.municipio = municipio;
        this.localidad = localidad;
        this.nacionalidad = nacionalidad;
    }

    public MPersonas(Integer codMun, String desMun, String apeMun, String ciMun, String dirMun, String telMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
        this.apeMun = new SimpleStringProperty(apeMun);
        this.ciMun = new SimpleStringProperty(ciMun);
        this.dirMun = new SimpleStringProperty(dirMun);
        this.telMun = new SimpleStringProperty(telMun);
    }

    public MPersonas(Integer codMun, String desMun){
        this.codMun = new SimpleIntegerProperty(codMun);
        this.desMun = new SimpleStringProperty(desMun);
    }

    public MNacionalidades getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(MNacionalidades nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getApeMun() {
        return apeMun.get();
    }

    public StringProperty apeMunProperty() {
        return apeMun;
    }

    public void setApeMun(String apeMun) {
        this.apeMun.set(apeMun);
    }

    public String getCiMun() {
        return ciMun.get();
    }

    public StringProperty ciMunProperty() {
        return ciMun;
    }

    public void setCiMun(String ciMun) {
        this.ciMun.set(ciMun);
    }

    public String getDirMun() {
        return dirMun.get();
    }

    public StringProperty dirMunProperty() {
        return dirMun;
    }

    public void setDirMun(String dirMun) {
        this.dirMun.set(dirMun);
    }

    public String getTelMun() {
        return telMun.get();
    }

    public StringProperty telMunProperty() {
        return telMun;
    }

    public void setTelMun(String telMun) {
        this.telMun.set(telMun);
    }

    public MMunicipios getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MMunicipios municipio) {
        this.municipio = municipio;
    }

    public MLocalidades getLocalidad() {
        return localidad;
    }

    public void setLocalidad(MLocalidades localidad) {
        this.localidad = localidad;
    }

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    public MClase_persona_det getClase_persona() { return clase_persona; }

    public void setClase_persona(MClase_persona_det clase_persona) { this.clase_persona = clase_persona; }

    public int getCodMun() { return codMun.get(); }

    public IntegerProperty codMunProperty() { return codMun; }

    public void setCodMun(int codMun) { this.codMun.set(codMun); }

    public String getDesMun() { return desMun.get(); }

    public StringProperty desMunProperty() { return desMun; }

    public void setDesMun(String desMun) { this.desMun.set(desMun); }

    public MCargos getCargo() { return cargo; }

    public void setCargo(MCargos cargo) { this.cargo = cargo;}

    public MClientes getCliente() { return cliente; }

    public void setCliente(MClientes cliente) { this.cliente = cliente; }

    public MFuncionarios getFuncionario() { return funcionario; }

    public void setFuncionario(MFuncionarios funcionario) { this.funcionario = funcionario; }

    public MProveedores getProveedor() { return proveedor; }

    public void setProveedor(MProveedores proveedor) { this.proveedor = proveedor; }

    //llenar tabla
    public static void obtenerInfoMun(Connection connection, ObservableList<MPersonas> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, c.*, d.*, e.*, b.*, f.*, g.*, h.*, i.*, j.* FROM personas a " +
                    "INNER JOIN estados b ON (a.estado_id = b.id_estado) " +
                    "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                    "INNER JOIN localidades d ON (a.localidad_id = d.id_localidad)" +
                    "INNER JOIN nacionalidades e ON (a.nacionalidad_id = e.id_nacionalidad)" +
                    "LEFT JOIN clases_personas f ON (a.id_persona = f.persona_id)" +
                    "LEFT JOIN funcionarios g ON (a.id_persona = g.id_funcionario)" +
                    "LEFT JOIN clientes h ON (a.id_persona = h.id_cliente)" +
                    "LEFT JOIN proveedores i ON (a.id_persona = i.id_proveedor)" +
                    "LEFT JOIN cargos j ON (id_cargo = g.cargo_id)" +
                    "ORDER BY id_persona");
            while (resultado.next()){
                lista.add(new MPersonas(resultado.getInt("id_persona"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("ci"),
                        resultado.getString("direccion"),
                        resultado.getString("tel"),
                        new MMunicipios(resultado.getInt("a.municipio_id"), resultado.getString("c.descripcion")),
                        new MLocalidades(resultado.getInt("a.localidad_id"), resultado.getString("d.descripcion")),
                        new MNacionalidades(resultado.getInt("a.nacionalidad_id"), resultado.getString("e.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("b.descripcion")),
                        new MClase_persona_det(resultado.getInt("a.id_persona"), resultado.getInt("f.funcionario_id"), resultado.getInt("f.cliente_id"), resultado.getInt("f.proveedor_id")),
                        new MFuncionarios(resultado.getInt("g.id_funcionario"), resultado.getString("g.descripcion"), new MCargos(resultado.getInt("g.cargo_id"),resultado.getString("j.descripcion"))),
                        new MClientes(resultado.getInt("h.id_cliente"), resultado.getString("h.descripcion"), resultado.getString("h.ruc")),
                        new MProveedores(resultado.getInt("i.id_proveedor"), resultado.getString("i.descripcion"), resultado.getString("i.ruc")),
                        new MCargos(resultado.getInt("j.id_cargo"), resultado.getString("j.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("insert into personas(id_persona, nombre, apellido, ci, direccion, tel, municipio_id, localidad_id, nacionalidad_id)" +
                    " values(?,?,?,?,?,?,?,?,?);");
            instruccion.setInt(1, codMun.get());
            instruccion.setString(2, desMun.get());
            instruccion.setString(3,apeMun.get());
            instruccion.setString(4,ciMun.get());
            instruccion.setString(5,dirMun.get());
            instruccion.setString(6,telMun.get());
            instruccion.setInt(7,municipio.getCodMun());
            instruccion.setInt(8,localidad.getCodMun());
            instruccion.setInt(9, nacionalidad.getCodMun());

            PreparedStatement instruccion1 = connection.prepareStatement("INSERT INTO clases_personas (persona_id) values(?)");
            instruccion1.setInt(1, codMun.get());

            int res = instruccion.executeUpdate();
            int res1 = instruccion1.executeUpdate();
            //evaluacion para registro de clase persona
            if (res == 1 && res1 ==1) {
                if (funcionario.getCodFun() != 0) {
                    PreparedStatement instruccion2 = connection.prepareStatement("insert into funcionarios(id_funcionario, descripcion, cargo_id) values(?,?,?);");
                    instruccion2.setInt(1, funcionario.getCodFun());
                    instruccion2.setString(2, funcionario.getDesFun());
                    instruccion2.setInt(3, funcionario.getCargos().getcodCargo());

                    PreparedStatement instruccion21 = connection.prepareStatement("UPDATE clases_personas SET funcionario_id = ? WHERE persona_id = ?");
                    instruccion21.setInt(1, funcionario.getCodFun());
                    instruccion21.setInt(2, codMun.get());
                    instruccion2.executeUpdate();
                    instruccion21.executeUpdate();
                }
                if (cliente.getCodCli() != 0) {
                    PreparedStatement instruccion3 = connection.prepareStatement("insert into clientes(id_cliente, descripcion, ruc)" +
                            " values(?,?,?);");
                    instruccion3.setInt(1, cliente.getCodCli());
                    instruccion3.setString(2, cliente.getDesCli());
                    instruccion3.setString(3, cliente.getRucCli());
                    PreparedStatement instruccion31 = connection.prepareStatement("UPDATE clases_personas SET cliente_id = ? WHERE persona_id = ?");
                    instruccion31.setInt(1, cliente.getCodCli());
                    instruccion31.setInt(2, codMun.get());
                    instruccion3.executeUpdate();
                    instruccion31.executeUpdate();
                }
                if (proveedor.getCodPro() !=0) {
                    PreparedStatement instruccion4 = connection.prepareStatement("insert into proveedores(id_proveedor, descripcion, ruc) values(?,?,?);");
                    instruccion4.setInt(1, proveedor.getCodPro());
                    instruccion4.setString(2, proveedor.getDesPro());
                    instruccion4.setString(3, proveedor.getRucPro());
                    PreparedStatement instruccion41 = connection.prepareStatement("UPDATE clases_personas SET proveedor_id = ? WHERE persona_id = ?");
                    instruccion41.setInt(1, proveedor.getCodPro());
                    instruccion41.setInt(2, codMun.get());
                    instruccion4.executeUpdate();
                    instruccion41.executeUpdate();
                }
            } else {
                System.out.println("No se pudo registrar las clases personas");
            }

            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE personas SET nombre = ?, apellido = ?, ci = ?, direccion = ?, tel = ?, municipio_id = ?, localidad_id = ?, " +
                    " nacionalidad_id = ? WHERE id_persona = ?");
            instruccion.setString(1, desMun.get());
            instruccion.setString(2,apeMun.get());
            instruccion.setString(3,ciMun.get());
            instruccion.setString(4,dirMun.get());
            instruccion.setString(5,telMun.get());
            instruccion.setInt(6,municipio.getCodMun());
            instruccion.setInt(7,localidad.getCodMun());
            instruccion.setInt(8, nacionalidad.getCodMun());
            instruccion.setInt(9,codMun.get());

            int res = instruccion.executeUpdate();
            //evaluacion para registro de clase persona
            if (res == 1) {
                //>>Evaluación funcionario<<//
                if (funcionario.getCodFun() != 0) {
                    PreparedStatement instruccion2 = connection.prepareStatement("SELECT id_funcionario FROM funcionarios WHERE id_funcionario = ?");
                    instruccion2.setInt(1, codMun.get());
                    ResultSet rs = instruccion2.executeQuery();
                    if (rs.next()){
                        System.out.println("primero " + rs.next());
                        instruccion2 = connection.prepareStatement("UPDATE funcionarios SET descripcion = ?, cargo_id = ? WHERE id_funcionario = ?");
                        instruccion2.setString(1, funcionario.getDesFun());
                        instruccion2.setInt(2, funcionario.getCargos().getcodCargo());
                        instruccion2.setInt(3, codMun.get());
                        System.out.println("Edito");
                    } else {
                        System.out.println("segundo " + rs.next());
                        instruccion2 = connection.prepareStatement("insert into funcionarios(id_funcionario, descripcion, cargo_id) values(?,?,?)");
                        instruccion2.setInt(1,funcionario.getCodFun());
                        instruccion2.setString(2, funcionario.getDesFun());
                        instruccion2.setInt(3, funcionario.getCargos().getcodCargo());
                        System.out.println("Inserto");
                    }
                    PreparedStatement instruccion21 = connection.prepareStatement("UPDATE clases_personas SET funcionario_id = ? WHERE persona_id = ?");
                    instruccion21.setInt(1, funcionario.getCodFun());
                    instruccion21.setInt(2, codMun.get());
                    instruccion2.executeUpdate();
                    instruccion21.executeUpdate();
                } else {
                    PreparedStatement instruccion21 = connection.prepareStatement("UPDATE clases_personas SET funcionario_id = NULL WHERE persona_id = ?");
                    instruccion21.setInt(1, codMun.get());
                    instruccion21.executeUpdate();
                }
                //>>Evaluación cliente<<//
                if (cliente.getCodCli() != 0) { // busca si existe un registro con mismo codigo de persona
                    PreparedStatement instruccion2 = connection.prepareStatement("SELECT id_cliente FROM clientes WHERE id_cliente = ?");
                    instruccion2.setInt(1, codMun.get());
                    ResultSet rs = instruccion2.executeQuery();
                    if (rs.next()){ // si hay conincidencias debe actualizarla nomás
                        System.out.println("primero " + rs.next());
                        instruccion2 = connection.prepareStatement("UPDATE clientes SET descripcion = ?, ruc = ? WHERE id_cliente = ?");
                        instruccion2.setString(1, funcionario.getDesFun());
                        instruccion2.setInt(2, funcionario.getCargos().getcodCargo());
                        instruccion2.setInt(3, codMun.get());
                        System.out.println("Edito");
                    } else {// si no, debe guardar un nuevo registro
                        System.out.println("segundo " + rs.next());
                        instruccion2 = connection.prepareStatement("insert into clientes (id_cliente, descripcion, ruc) values(?,?,?)");
                        instruccion2.setInt(1,cliente.getCodCli());
                        instruccion2.setString(2, cliente.getDesCli());
                        instruccion2.setString(3, cliente.getRucCli());
                        System.out.println("Inserto");
                    }
                    PreparedStatement instruccion21 = connection.prepareStatement("UPDATE clases_personas SET cliente_id = ? WHERE persona_id = ?");
                    instruccion21.setInt(1, cliente.getCodCli());
                    instruccion21.setInt(2, codMun.get());
                    instruccion2.executeUpdate();
                    instruccion21.executeUpdate();
                } else {
                    PreparedStatement instruccion21 = connection.prepareStatement("UPDATE clases_personas SET cliente_id = NULL WHERE persona_id = ?");
                    instruccion21.setInt(1, codMun.get());
                    instruccion21.executeUpdate();
                }
                //>>Evaluación cliente<<//
                if (proveedor.getCodPro() !=0) {
                    PreparedStatement instruccion4 = connection.prepareStatement("UPDATE proveedores SET descripcion = ?, ruc = ? WHERE id_proveedor = ?");
                    instruccion4.setString(1, proveedor.getDesPro());
                    instruccion4.setString(2, proveedor.getRucPro());
                    instruccion4.setInt(3, codMun.get());
                    PreparedStatement instruccion41 = connection.prepareStatement("UPDATE clases_personas SET proveedor_id = ? WHERE persona_id = ?");
                    instruccion41.setInt(1, proveedor.getCodPro());
                    instruccion41.setInt(2, codMun.get());
                    instruccion4.executeUpdate();
                    instruccion41.executeUpdate();
                } else {
                    PreparedStatement instruccion4 = connection.prepareStatement("insert into proveedores(id_proveedor, descripcion, ruc) values(?,?,?);");
                    instruccion4.setInt(1, proveedor.getCodPro());
                    instruccion4.setString(2, proveedor.getDesPro());
                    instruccion4.setString(3, proveedor.getRucPro());
                    PreparedStatement instruccion41 = connection.prepareStatement("UPDATE clases_personas SET proveedor_id = NULL WHERE persona_id = ?");
                    instruccion41.setInt(1, codMun.get());
                    instruccion41.executeUpdate();
                }
            } else {
                System.out.println("No se pudo registrar las clases personas");
            }
            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE personas SET estado_id = 2 WHERE id_persona = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MPersonas> lista, String filtro){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, " +
                    "c.*, d.*, e.*, b.*, f.*, g.*, h.*, i.*, j.* FROM personas a " +
                    "INNER JOIN estados b ON (a.estado_id = b.id_estado) " +
                    "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                    "INNER JOIN localidades d ON (a.localidad_id = d.id_localidad)" +
                    "INNER JOIN nacionalidades e ON (a.nacionalidad_id = e.id_nacionalidad)" +
                    "LEFT JOIN clases_personas f ON (a.id_persona = f.persona_id)" +
                    "LEFT JOIN funcionarios g ON (a.id_persona = g.id_funcionario)" +
                    "LEFT JOIN clientes h ON (a.id_persona = h.id_cliente)" +
                    "LEFT JOIN proveedores i ON (a.id_persona = i.id_proveedor)" +
                    "LEFT JOIN cargos j ON (id_cargo = g.cargo_id)" +
                    "WHERE (a.nombre LIKE ? or a.ci=?) ORDER BY id_persona");
                    instruccion.setString(1, "%" + filtro + "%");
                    instruccion.setString(2, "%" + filtro + "%");
                    ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MPersonas(resultado.getInt("id_persona"),
                        resultado.getString("a.nombre"),
                        resultado.getString("a.apellido"),
                        resultado.getString("a.ci"),
                        resultado.getString("a.direccion"),
                        resultado.getString("a.tel"),
                        new MMunicipios(resultado.getInt("a.municipio_id"), resultado.getString("c.descripcion")),
                        new MLocalidades(resultado.getInt("a.localidad_id"), resultado.getString("d.descripcion")),
                        new MNacionalidades(resultado.getInt("a.nacionalidad_id"), resultado.getString("e.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("b.descripcion")),
                        new MClase_persona_det(resultado.getInt("a.id_persona"), resultado.getInt("f.funcionario_id"), resultado.getInt("f.cliente_id"), resultado.getInt("f.proveedor_id")),
                        new MFuncionarios(resultado.getInt("g.id_funcionario"), resultado.getString("g.descripcion"), new MCargos(resultado.getInt("g.cargo_id"),resultado.getString("j.descripcion"))),
                        new MClientes(resultado.getInt("h.id_cliente"), resultado.getString("h.descripcion"), resultado.getString("h.ruc")),
                        new MProveedores(resultado.getInt("i.id_proveedor"), resultado.getString("i.descripcion"), resultado.getString("i.ruc")),
                        new MCargos(resultado.getInt("j.id_cargo"), resultado.getString("j.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void busquedaClientesD(Connection connection, ObservableList<MPersonas> lista, String cod){
        try {
            PreparedStatement instruccion;
            if (cod.equals("")){
                instruccion = connection.prepareStatement("SELECT a.*, " +
                        "c.*, d.*, e.*, b.*, f.*, g.*, h.*, i.*, j.* FROM personas a " +
                        "INNER JOIN estados b ON (a.estado_id = b.id_estado) " +
                        "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                        "INNER JOIN localidades d ON (a.localidad_id = d.id_localidad)" +
                        "INNER JOIN nacionalidades e ON (a.nacionalidad_id = e.id_nacionalidad)" +
                        "LEFT JOIN clases_personas f ON (a.id_persona = f.persona_id)" +
                        "LEFT JOIN funcionarios g ON (a.id_persona = g.id_funcionario)" +
                        "LEFT JOIN clientes h ON (a.id_persona = h.id_cliente)" +
                        "LEFT JOIN proveedores i ON (a.id_persona = i.id_proveedor)" +
                        "LEFT JOIN cargos j ON (id_cargo = g.cargo_id)" +
                        "WHERE (f.cliente_id = a.id_persona) ORDER BY id_persona");
            }else {
            instruccion = connection.prepareStatement("SELECT a.*, " +
                        "c.*, d.*, e.*, b.*, f.*, g.*, h.*, i.*, j.* FROM personas a " +
                        "INNER JOIN estados b ON (a.estado_id = b.id_estado) " +
                        "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                        "INNER JOIN localidades d ON (a.localidad_id = d.id_localidad)" +
                        "INNER JOIN nacionalidades e ON (a.nacionalidad_id = e.id_nacionalidad)" +
                        "LEFT JOIN clases_personas f ON (a.id_persona = f.persona_id)" +
                        "LEFT JOIN funcionarios g ON (a.id_persona = g.id_funcionario)" +
                        "LEFT JOIN clientes h ON (a.id_persona = h.id_cliente)" +
                        "LEFT JOIN proveedores i ON (a.id_persona = i.id_proveedor)" +
                        "LEFT JOIN cargos j ON (id_cargo = g.cargo_id)" +
                        "WHERE (f.cliente_id = ?) ORDER BY id_persona");
                instruccion.setString(1, cod);
            }
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MPersonas(resultado.getInt("id_persona"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("ci"),
                        resultado.getString("direccion"),
                        resultado.getString("tel"),
                        new MMunicipios(resultado.getInt("a.municipio_id"), resultado.getString("c.descripcion")),
                        new MLocalidades(resultado.getInt("a.localidad_id"), resultado.getString("d.descripcion")),
                        new MNacionalidades(resultado.getInt("a.nacionalidad_id"), resultado.getString("e.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("b.descripcion")),
                        new MClase_persona_det(resultado.getInt("a.id_persona"), resultado.getInt("f.funcionario_id"), resultado.getInt("f.cliente_id"), resultado.getInt("f.proveedor_id")),
                        new MFuncionarios(resultado.getInt("g.id_funcionario"), resultado.getString("g.descripcion"), new MCargos(resultado.getInt("g.cargo_id"),resultado.getString("j.descripcion"))),
                        new MClientes(resultado.getInt("h.id_cliente"), resultado.getString("h.descripcion"), resultado.getString("h.ruc")),
                        new MProveedores(resultado.getInt("i.id_proveedor"), resultado.getString("i.descripcion"), resultado.getString("i.ruc")),
                        new MCargos(resultado.getInt("j.id_cargo"), resultado.getString("j.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void busquedaProveedoresD(Connection connection, ObservableList<MPersonas> lista, String cod){
        try {
            PreparedStatement instruccion;
            if (cod.equals("")){
                instruccion = connection.prepareStatement("SELECT a.*, " +
                        "c.*, d.*, e.*, b.*, f.*, g.*, h.*, i.*, j.* FROM personas a " +
                        "INNER JOIN estados b ON (a.estado_id = b.id_estado) " +
                        "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                        "INNER JOIN localidades d ON (a.localidad_id = d.id_localidad)" +
                        "INNER JOIN nacionalidades e ON (a.nacionalidad_id = e.id_nacionalidad)" +
                        "LEFT JOIN clases_personas f ON (a.id_persona = f.persona_id)" +
                        "LEFT JOIN funcionarios g ON (a.id_persona = g.id_funcionario)" +
                        "LEFT JOIN clientes h ON (a.id_persona = h.id_cliente)" +
                        "LEFT JOIN proveedores i ON (a.id_persona = i.id_proveedor)" +
                        "LEFT JOIN cargos j ON (id_cargo = g.cargo_id)" +
                        "WHERE (f.proveedor_id = a.id_persona) ORDER BY id_persona");
            } else{
            instruccion = connection.prepareStatement("SELECT a.*, " +
                    "c.*, d.*, e.*, b.*, f.*, g.*, h.*, i.*, j.* FROM personas a " +
                    "INNER JOIN estados b ON (a.estado_id = b.id_estado) " +
                    "INNER JOIN municipios c ON (a.municipio_id = c.id_municipio) " +
                    "INNER JOIN localidades d ON (a.localidad_id = d.id_localidad)" +
                    "INNER JOIN nacionalidades e ON (a.nacionalidad_id = e.id_nacionalidad)" +
                    "LEFT JOIN clases_personas f ON (a.id_persona = f.persona_id)" +
                    "LEFT JOIN funcionarios g ON (a.id_persona = g.id_funcionario)" +
                    "LEFT JOIN clientes h ON (a.id_persona = h.id_cliente)" +
                    "LEFT JOIN proveedores i ON (a.id_persona = i.id_proveedor)" +
                    "LEFT JOIN cargos j ON (id_cargo = g.cargo_id)" +
                    "WHERE (f.proveedor_id = ?) ORDER BY id_persona");
                instruccion.setString(1, cod);
            }
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MPersonas(resultado.getInt("id_persona"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("ci"),
                        resultado.getString("direccion"),
                        resultado.getString("tel"),
                        new MMunicipios(resultado.getInt("a.municipio_id"), resultado.getString("c.descripcion")),
                        new MLocalidades(resultado.getInt("a.localidad_id"), resultado.getString("d.descripcion")),
                        new MNacionalidades(resultado.getInt("a.nacionalidad_id"), resultado.getString("e.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("b.descripcion")),
                        new MClase_persona_det(resultado.getInt("a.id_persona"), resultado.getInt("f.funcionario_id"), resultado.getInt("f.cliente_id"), resultado.getInt("f.proveedor_id")),
                        new MFuncionarios(resultado.getInt("g.id_funcionario"), resultado.getString("g.descripcion"), new MCargos(resultado.getInt("g.cargo_id"),resultado.getString("j.descripcion"))),
                        new MClientes(resultado.getInt("h.id_cliente"), resultado.getString("h.descripcion"), resultado.getString("h.ruc")),
                        new MProveedores(resultado.getInt("i.id_proveedor"), resultado.getString("i.descripcion"), resultado.getString("i.ruc")),
                        new MCargos(resultado.getInt("j.id_cargo"), resultado.getString("j.descripcion"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_persona) FROM personas");
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
        return codMun.get() + " - " + desMun.get();
    }
}
