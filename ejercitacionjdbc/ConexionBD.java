import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL =
            "jdbc:h2:file:" + System.getProperty("user.dir") + "/videojuegos;AUTO_SERVER=TRUE";
    private static final String USUARIO = "sa";
    private static final String CONTRASENA = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
