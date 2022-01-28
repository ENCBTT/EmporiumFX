
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private String db = "emporium1.1";
    private String user = "root";
    private String password = "";
    private String url = "jdbc:mysql://localhost/" + db;
    private Connection conn = null;

    public void iniciarConexion() {
        this.url = "jdbc:mysql://localhost/" + this.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.out.println("Error conexionBD no conecta");
        }
    }

    public Connection getConnection(){
        return this.conn;
    }

    public void setConnection(Connection conn) { this.conn = conn; }

    public void cerrarConexion(){
        try{
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
