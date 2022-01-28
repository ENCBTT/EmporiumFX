package modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class MEmbalajes {
    private IntegerProperty codEmb;
    private StringProperty desEmb;
    private StringProperty sigEmb;

    public MEmbalajes(Integer codEmb, String desEmb,  String sigEmb){
        this.codEmb = new SimpleIntegerProperty(codEmb);
        this.desEmb = new SimpleStringProperty(desEmb);
        this.sigEmb = new SimpleStringProperty(sigEmb);
    }

    public MEmbalajes(){}

    public int getCodEmb() {return codEmb.get();}

    public IntegerProperty codEmbProperty() {return codEmb;}

    public void setCodEmb(int codEmb) {this.codEmb.set(codEmb);}

    public String getDesEmb() {return desEmb.get();}

    public StringProperty desEmbProperty() {return desEmb;}

    public void setDesEmb(String desEmb) {this.desEmb.set(desEmb);}

    public String getSigEmb() {return sigEmb.get();}

    public StringProperty sigEmbProperty() {return sigEmb;}

    public void setSigEmb(String sigEmb) {this.sigEmb.set(sigEmb);}

    //llenar tabla
    public static void obtenerInfoMun(Connection connection, ObservableList<MEmbalajes> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM embalajes;");
            while (resultado.next()){
                lista.add(new MEmbalajes(resultado.getInt("id_embalaje"),
                        resultado.getString("descripcion"),
                        resultado.getString("sigla")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int guardarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO embalajes(id_embalaje, descripcion, sigla) VALUES(default,?,?)");
            instruccion.setString(1, desEmb.get());
            instruccion.setString(2, sigEmb.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }

    }
    public int editarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("UPDATE embalajes SET descripcion = ?, sigla = ? WHERE id_embalaje = ?");
            instruccion.setString(1, desEmb.get());
            instruccion.setString(2, sigEmb.get());
            instruccion.setInt(3,codEmb.get());
            return instruccion.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM embalajes WHERE id_embalaje = ?");
            instruccion.setInt(1,codEmb.get());
            return instruccion.executeUpdate();

            } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    //llenar tabla busqueda
    public static void busquedaInfoMun(Connection connection, ObservableList<MEmbalajes> lista, String filtro) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM embalajes WHERE (descripcion LIKE ? or id_embalaje=?)");
            instruccion.setString(1, "%" + filtro + "%");
            instruccion.setString(2, filtro);
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()){
                lista.add(new MEmbalajes(resultado.getInt("id_embalaje"),
                        resultado.getString("descripcion"),
                        resultado.getString("sigla")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int ultRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT MAX(id_embalaje) FROM embalajes");
            ResultSet rs = instruccion.executeQuery();
            rs.next();
            return (rs.getInt(1)+1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() { return codEmb.get() + " - " + desEmb.get(); }
}
