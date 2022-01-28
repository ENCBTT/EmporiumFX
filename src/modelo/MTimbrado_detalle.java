package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MTimbrado_detalle {
    private MTimbrados timbrados;
    private MEstablecimientos establecimientos;
    private MPuntos_expediciones expedicion;
    private MEstados estado;
    private MTipos_documentos tipos_documentos;
    private IntegerProperty codFact;
    private IntegerProperty numAc;
    private IntegerProperty numI;
    private IntegerProperty numF;

    public MTimbrado_detalle(MTimbrados timbrados, MEstablecimientos establecimientos, MPuntos_expediciones expedicion, MEstados estado,  MTipos_documentos tipos_documentos, Integer codFact,  Integer numAC, Integer numI, Integer numF){
        this.timbrados = timbrados;
        this.establecimientos = establecimientos;
        this.expedicion = expedicion;
        this.estado = estado;
        this.tipos_documentos = tipos_documentos;
        this.codFact = new SimpleIntegerProperty(codFact);
        this.numAc = new SimpleIntegerProperty(numAC);
        this.numI = new SimpleIntegerProperty(numI);
        this.numF = new SimpleIntegerProperty(numF);
    }

    public MTimbrado_detalle(MTimbrados timbrados, MEstablecimientos establecimientos, MPuntos_expediciones expedicion, MEstados estado,  MTipos_documentos tipos_documentos, Integer numI, Integer numF){
        this.timbrados = timbrados;
        this.establecimientos = establecimientos;
        this.expedicion = expedicion;
        this.estado = estado;
        this.tipos_documentos = tipos_documentos;
        this.numI = new SimpleIntegerProperty(numI);
        this.numF = new SimpleIntegerProperty(numF);
    }

    public MTimbrado_detalle(MTimbrados timbrados, MEstablecimientos establecimientos, MPuntos_expediciones expedicion,  MTipos_documentos tipos_documentos, Integer codFact){
        this.timbrados = timbrados;
        this.establecimientos = establecimientos;
        this.expedicion = expedicion;
        this.tipos_documentos = tipos_documentos;
        this.codFact = new SimpleIntegerProperty(codFact);
    }

    public MTimbrado_detalle(){};

    public int getCodFact() { return codFact.get(); }

    public IntegerProperty codFactProperty() { return codFact; }

    public void setCodFact(int codDt) { this.codFact.set(codDt); }

    public int getNumI() { return numI.get(); }

    public IntegerProperty numIProperty() { return numI; }

    public void setNumI(int numI) { this.numI.set(numI); }

    public int getNumF() { return numF.get(); }

    public IntegerProperty numFProperty() { return numF; }

    public void setNumF(int numF) { this.numF.set(numF); }

    public int getNumAc() { return numAc.get(); }

    public IntegerProperty numAcProperty() { return numAc; }

    public void setNumAc(int numAc) { this.numAc.set(numAc); }

    public MTimbrados getTimbrados() { return timbrados; }

    public void setTimbrados(MTimbrados timbrados) { this.timbrados = timbrados; }

    public MEstablecimientos getEstablecimientos() { return establecimientos; }

    public void setEstablecimientos(MEstablecimientos establecimientos) { this.establecimientos = establecimientos; }

    public MPuntos_expediciones getExpedicion() { return expedicion; }

    public void setExpedicion(MPuntos_expediciones expedicion) { this.expedicion = expedicion; }

    public MTipos_documentos getTipos_documentos() { return tipos_documentos; }

    public void setTipos_documentos(MTipos_documentos tipos_documentos) { this.tipos_documentos = tipos_documentos; }

    public MEstados getEstado() { return estado; }

    public void setEstado(MEstados estado) { this.estado = estado; }

    //llenar tabla
    public static void obtenerInfoMun(Connection conncection, ObservableList<MTimbrado_detalle> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT a.*, b.*, c.*, d.*, e.*, f.* FROM detalle_timbrados `a`" +
                    "INNER JOIN timbrados b ON (a.timbrado_id = b.id_timbrado)" +
                    "INNER JOIN establecimientos c ON (a.establecimiento_id = c.id_establecimiento)" +
                    "INNER JOIN puntos_expediciones d ON (a.expedicion_id = d.id_expedicion)" +
                    "INNER JOIN estados e ON (a.estado_id = e.id_estado)" +
                    "INNER JOIN tipos_documentos f ON (a.documento_id = f.id_documento)" +
                    "ORDER BY timbrado_id");
            while (resultado.next()){
                lista.add(new MTimbrado_detalle(
                        new MTimbrados(resultado.getInt("a.timbrado_id"), resultado.getInt("b.num_timbrado")),
                        new MEstablecimientos(resultado.getInt("a.establecimiento_id"), resultado.getString("c.descripcion")),
                        new MPuntos_expediciones(resultado.getInt("a.expedicion_id"), resultado.getString("d.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("e.descripcion")),
                        new MTipos_documentos(resultado.getInt("a.documento_id"), resultado.getString("f.descripcion")),
                        resultado.getInt("codigo_factura"),
                        resultado.getInt("secuencia"),
                        resultado.getInt("num_inicial"),
                        resultado.getInt("num_final")
                        ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO detalle_timbrados(timbrado_id, establecimiento_id, expedicion_id, estado_id, documento_id, num_inicial, num_final)" +
                    " values(?,?,?,?,?,?,?);");
            instruccion.setInt(1, timbrados.getCodMun());
            instruccion.setInt(2, establecimientos.getCodEst());
            instruccion.setInt(3, expedicion.getCodMun());
            instruccion.setInt(4,estado.getCodMun());
            instruccion.setInt(5,tipos_documentos.getCodMun());
            instruccion.setInt(6, numI.get());
            instruccion.setInt(7, numF.get());
            /*
            faltan hacer validaciones
            */
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int guardarConFact(Connection connection) {
        try {
            int sec;
            PreparedStatement instruccion; //falta validar vigencia de timbrado , se puede hacer un metodo en MTimbrado para eso y devolver un boolean
            PreparedStatement instruccion1 = connection.prepareStatement("SELECT * FROM detalle_timbrados WHERE timbrado_id = ? and establecimiento_id = ? and expedicion_id = ? " +
                    "and documento_id = ? ORDER BY secuencia ASC LIMIT 1;");
            instruccion1.setInt(1, timbrados.getCodMun());
            instruccion1.setInt(2, establecimientos.getCodEst());
            instruccion1.setInt(3, expedicion.getCodMun());
            instruccion1.setInt(4, tipos_documentos.getCodMun());
            ResultSet rs = instruccion1.executeQuery();
            rs.next();
            if (rs.getInt("secuencia") == 0 && rs.getInt("factura_id") == 0){ // si hay conincidencia debera editar esse primero registro para que sea la primera secuencia
                instruccion = connection.prepareStatement("UPDATE detalle_timbrados SET secuencia = ?, factura_id = ?" +
                        " WHERE timbrado_id = ? and establecimiento_id = ? and expedicion_id = ? and documento_id = ?");
                instruccion.setInt(1,1);
                instruccion.setInt(2, codFact.get());
                instruccion.setInt(3, timbrados.getCodMun());
                instruccion.setInt(4, establecimientos.getCodEst());
                instruccion.setInt(5, expedicion.getCodMun());
                instruccion.setInt(6, tipos_documentos.getCodMun());
                return instruccion.executeUpdate();
            } else { // caso no debera insertar un nuevo registro con la siguiente secuencia
                sec = (rs.getInt("secuencia") + 1);
                instruccion = connection.prepareStatement("INSERT INTO detalle_timbrados(timbrado_id, establecimiento_id, expedicion_id, estado_id, documento_id, num_inicial, num_final, id_factura, secuencia)" +
                        " values(?,?,?,?,?,?,?);");
                instruccion.setInt(1, timbrados.getCodMun());
                instruccion.setInt(2, establecimientos.getCodEst());
                instruccion.setInt(3, expedicion.getCodMun());
                instruccion.setInt(4, 1);
                instruccion.setInt(5, tipos_documentos.getCodMun());
                instruccion.setInt(6, rs.getInt("num_inicial"));
                instruccion.setInt(7, rs.getInt("num_final"));
                instruccion.setInt(8, codFact.get());
                instruccion.setInt(9, sec);
                return instruccion.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE detalle_timbrados SET expedicion_id = ?, establecimiento_id = ?," +
                    " num_inicial = ?, num_final = ?, secuencia = ?, estado_id = ?" +
                    " WHERE timbrado_id = ?");

            instruccion.setInt(1, expedicion.getCodMun());
            instruccion.setInt(2, establecimientos.getCodEst());
            instruccion.setInt(3, numI.get());
            instruccion.setInt(4, numF.get());
            instruccion.setInt(5, numAc.get());
            instruccion.setInt(6,estado.getCodMun());
            instruccion.setInt(7,tipos_documentos.getCodMun());
            instruccion.setInt(8, timbrados.getCodMun());
            //VALIDAR
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE detalle_timbrados SET estado_id = 2 WHERE id_timbrado = ?");
            instruccion.setInt(1,timbrados.getCodMun());
            return instruccion.executeUpdate();
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void buscarRegistro(Connection connection, ObservableList<MTimbrado_detalle> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT a.*, b.*, c.*, d.*, e.*, f.* FROM detalle_timbrados `a`" +
                    "INNER JOIN timbrados b ON (a.timbrado_id = b.id_timbrado)" +
                    "INNER JOIN establecimientos c ON (a.establecimiento_id = c.id_establecimiento)" +
                    "INNER JOIN puntos_expediciones d ON (a.expedicion_id = d.id_expedicion)" +
                    "INNER JOIN estados e ON (a.estado_id = e.id_estado)" +
                    "INNER JOIN tipos_documentos f ON (a.documento_id = f.id_documento)" +
                    "WHERE (a.timbrado_id = ? OR b.num_timbrado = ?) ORDER BY timbrado_id");
            instruccion.setString(1, filtro);
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();

            while (resultado.next()){
                lista.add(new MTimbrado_detalle(
                        new MTimbrados(resultado.getInt("a.timbrado_id"), resultado.getInt("b.num_timbrado")),
                        new MEstablecimientos(resultado.getInt("a.establecimiento_id"), resultado.getString("c.descripcion")),
                        new MPuntos_expediciones(resultado.getInt("a.expedicion_id"), resultado.getString("d.descripcion")),
                        new MEstados(resultado.getInt("a.estado_id"), resultado.getString("e.descripcion")),
                        new MTipos_documentos(resultado.getInt("a.documento_id"), resultado.getString("f.descripcion")),
                        resultado.getInt("id_factura"),
                        resultado.getInt("secuencia"),
                        resultado.getInt("num_inicial"),
                        resultado.getInt("num_final")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultSecuencia(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(secuencia) FROM detalle_timbrados WHERE timbrado_id = ?");
            instruccion.setInt(1, timbrados.getCodMun());
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
        return establecimientos.getCodEst() + " - " + expedicion.getCodMun() + " - " + numAc.get();
    }
}
