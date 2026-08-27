import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAO {

    public static void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS videojuegos (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL, genero VARCHAR(100) NOT NULL, precio DOUBLE NOT NULL, unidades_disponibles INT NOT NULL DEFAULT 0, nivel_reposicion INT NOT NULL DEFAULT 0, suspendido INT NOT NULL DEFAULT 1)";
        try (Connection conn = ConexionBD.obtenerConexion(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void crearVideojuego(Videojuego videojuego) throws SQLException {
        validar(videojuego);
        String sql = "INSERT INTO videojuegos (nombre, genero, precio, unidades_disponibles, nivel_reposicion, suspendido) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) { 
            cargar(stmt, videojuego); 
            stmt.executeUpdate(); 
        }
    }

    public static List<Videojuego> listarVideojuegos() throws SQLException { 
        return consultar("SELECT * FROM videojuegos ORDER BY id"); 
    }

    public static List<Videojuego> listarDisponibles() throws SQLException { 
        return consultar("SELECT * FROM videojuegos WHERE suspendido = 1 ORDER BY id"); 
    }

    public static List<Videojuego> listarNecesitanReposicion() throws SQLException { 
        return consultar("SELECT * FROM videojuegos WHERE unidades_disponibles < nivel_reposicion ORDER BY id"); 
    }

    public static Videojuego buscarPorId(int id) throws SQLException, VideojuegoNoEncontradoExcepcion {
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM videojuegos WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) { 
                if (rs.next()) return mapear(rs); 
            }
        }
        throw new VideojuegoNoEncontradoExcepcion("No existe un videojuego con el ID indicado.");
    }

    public static void actualizarVideojuego(Videojuego videojuego) throws SQLException, VideojuegoNoEncontradoExcepcion {
        validar(videojuego);
        String sql = "UPDATE videojuegos SET nombre=?, genero=?, precio=?, unidades_disponibles=?, nivel_reposicion=?, suspendido=? WHERE id=?";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            cargar(stmt, videojuego); 
            stmt.setInt(7, videojuego.getId());
            if (stmt.executeUpdate() == 0) throw new VideojuegoNoEncontradoExcepcion("No existe un videojuego con el ID indicado.");
        }
    }

    public static void eliminarVideojuego(int id) throws SQLException, VideojuegoNoEncontradoExcepcion {
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM videojuegos WHERE id=?")) {
            stmt.setInt(1, id); 
                if (stmt.executeUpdate() == 0) throw new VideojuegoNoEncontradoExcepcion("No existe un videojuego con el ID indicado.");
        }
    }

    private static List<Videojuego> consultar(String sql) throws SQLException {
        List<Videojuego> videojuegos = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { 
            while (rs.next()) videojuegos.add(mapear(rs)); 
        }
        return videojuegos;
    }

    private static Videojuego mapear(ResultSet rs) throws SQLException { 
        return new Videojuego(rs.getInt("id"), rs.getString("nombre"), rs.getString("genero"), rs.getDouble("precio"), rs.getInt("unidades_disponibles"), rs.getInt("nivel_reposicion"), rs.getInt("suspendido")); 
    }

    private static void cargar(PreparedStatement stmt, Videojuego v) throws SQLException { 
        stmt.setString(1, v.getNombre()); 
        stmt.setString(2, v.getGenero()); 
        stmt.setDouble(3, v.getPrecio()); 
        stmt.setInt(4, v.getUnidadesDisponibles()); 
        stmt.setInt(5, v.getNivelReposicion()); 
        stmt.setInt(6, v.getSuspendido()); 
    }

    private static void validar(Videojuego v) { 
        if (v.getPrecio() <= 0) throw new IllegalArgumentException("el precio debe ser mayor a cero"); 
        if (v.getUnidadesDisponibles() < 0) throw new IllegalArgumentException("el stock no puede ser negativo"); 
    }
}
