package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MClase_persona_det {

    private IntegerProperty codPer;
    private IntegerProperty codCli;
    private IntegerProperty codPro;
    private IntegerProperty codFun;

    public MClase_persona_det(Integer codPer, Integer codFun, Integer codCli, Integer codPro) {
        this.codPer = new SimpleIntegerProperty(codPer);
        this.codCli = new SimpleIntegerProperty(codCli);
        this.codPro = new SimpleIntegerProperty(codPro);
        this.codFun = new SimpleIntegerProperty(codFun);
    }

    public int getCodPer() {return codPer.get();}

    public IntegerProperty codPerProperty() {return codPer;}

    public void setCodPer(int codPer) {this.codPer.set(codPer);}

    public int getCodCli() {return codCli.get();}

    public IntegerProperty codCliProperty() {return codCli;}

    public void setCodCli(int codCli) {this.codCli.set(codCli);}

    public int getCodPro() {return codPro.get();}

    public IntegerProperty codProProperty() {return codPro;}

    public void setCodPro(int codPro) {this.codPro.set(codPro);}

    public int getcodFun() {return codFun.get();}

    public IntegerProperty codFunProperty() {return codFun;}

    public void setcodFun(int codFun) {this.codFun.set(codFun);}
    
    

//llenar tabla

    public static void obtenerInfoMun(Connection conncection, ObservableList<MClase_persona_det> lista) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM clases_personas");
            while (resultado.next()){
                lista.add(new MClase_persona_det(resultado.getInt("persona_id"),
                        resultado.getInt("cliente_id"),
                        resultado.getInt("funcionario_id"),
                        resultado.getInt("proveedor_id")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion;
            instruccion = connection.prepareStatement("insert into clases_personas(persona_id) values(?");
            instruccion.setInt(1, codPer.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE clases_personas SET clase_persona_id = ?, persona_id = ?, cliente_id = ?, proveedor_id =?, funcionario_id = ?  WHERE id_clase_persona = ?");
            instruccion.setInt(1, 0);
            instruccion.setInt(2, codPer.get());
            instruccion.setInt(3, codCli.get());
            instruccion.setInt(4, codPro.get());
            instruccion.setInt(5, codFun.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
/*
    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM clases_personas WHERE id_clase_persona = ?");
            instruccion.setInt(1,codMun.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda

    public static void busquedaInfoMun(Connection conncection, ObservableList<MClase_persona_det> lista, String filtro) {
        try {
            Statement statement = conncection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM clases_personas WHERE (id_clase_persona ='"+ filtro +"' or descripcion LIKE '%"+filtro+"%')");

            while (resultado.next()){
                lista.add(new MClase_persona_det(resultado.getInt("id_clase_persona"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void buscarRegistro(Connection connection, ObservableList<MClase_persona_det> lista, String des){
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM clases_personas WHERE (descripcion LIKE '%"+ des +"%')");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MClase_persona_det(resultado.getInt("id_clase_persona"),
                        resultado.getString("descripcion")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_clase_persona) FROM clases_personas");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }*/


}
