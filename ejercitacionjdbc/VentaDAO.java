import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    public static void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ventas (id INT AUTO_INCREMENT PRIMARY KEY, fecha DATE NOT NULL, cantidad INT NOT NULL, videojuego_id INT NOT NULL, descuento DOUBLE NOT NULL DEFAULT 0, total DOUBLE NOT NULL DEFAULT 0, FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id))";
        try (Connection conexion = ConexionBD.obtenerConexion(); Statement sentencia = conexion.createStatement()) {
            sentencia.execute(sql);
        }
    }

    public static void registrarVenta(Venta venta) throws SQLException, VideojuegoNoEncontradoExcepcion, VentaInvalidaExcepcion, ReglaNegocioExcepcion {
        if (venta.getFecha() == null || venta.getFecha().isAfter(LocalDate.now())) {
            throw new VentaInvalidaExcepcion("La fecha no puede ser futura.");
        }
        if (venta.getCantidad() <= 0) {
            throw new VentaInvalidaExcepcion("la cantidad debe ser mayor a cero");
        }

        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                Videojuego videojuego = buscarVideojuego(conexion, venta.getVideojuegoId());
                if (videojuego.getSuspendido() == 0) {
                    throw new ReglaNegocioExcepcion("videojuego suspendido");
                }
                if (videojuego.getUnidadesDisponibles() < venta.getCantidad()) {
                    throw new ReglaNegocioExcepcion("stock insuficiente.");
                }

                double descuento = descuentoPorCantidad(venta.getCantidad());
                double total = videojuego.getPrecio() * venta.getCantidad() * (1 - descuento);
                actualizarStock(conexion, venta);
                guardarVenta(conexion, venta, descuento, total);
                conexion.commit();
                venta.setDescuento(descuento);
                venta.setTotal(total);
            } catch (Exception error) {
                conexion.rollback();
                if (error instanceof SQLException) throw (SQLException) error;
                if (error instanceof VideojuegoNoEncontradoExcepcion) throw (VideojuegoNoEncontradoExcepcion) error;
                if (error instanceof VentaInvalidaExcepcion) throw (VentaInvalidaExcepcion) error;
                throw (ReglaNegocioExcepcion) error;
            }
        }
    }

    public static double descuentoPorCantidad(int cantidad) {
        if (cantidad >= 10) return 0.15;
        if (cantidad >= 5) return 0.10;
        if (cantidad >= 2) return 0.05;
        return 0;
    }

    public static List<Venta> listarVentas() throws SQLException {
        return consultar("SELECT v.*, j.nombre AS videojuego_nombre FROM ventas v JOIN videojuegos j ON j.id = v.videojuego_id ORDER BY v.fecha, v.id");
    }

    public static Venta buscarPorId(int id) throws SQLException, VentaInvalidaExcepcion {
        String sql = consultaBase() + " WHERE v.id = ?";
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) return convertir(resultado);
            }
        }
        throw new VentaInvalidaExcepcion("No existe una venta con el ID indicado.");
    }

    public static List<Venta> listarVentasDelMesActual() throws SQLException {
        return consultar(consultaBase() + " WHERE YEAR(v.fecha) = YEAR(CURRENT_DATE) AND MONTH(v.fecha) = MONTH(CURRENT_DATE) ORDER BY v.fecha, v.id");
    }

    public static List<Venta> buscarVentasPorVideojuego(int id) throws SQLException {
        String sql = consultaBase() + " WHERE v.videojuego_id = ? ORDER BY v.fecha, v.id";
        List<Venta> ventas = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) ventas.add(convertir(resultado));
            }
        }
        return ventas;
    }

    private static String consultaBase() {
        return "SELECT v.*, j.nombre AS videojuego_nombre FROM ventas v JOIN videojuegos j ON j.id = v.videojuego_id";
    }

    private static Videojuego buscarVideojuego(Connection conexion, int id) throws SQLException, VideojuegoNoEncontradoExcepcion {
        String sql = "SELECT * FROM videojuegos WHERE id = ? FOR UPDATE";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Videojuego(resultado.getInt("id"), resultado.getString("nombre"), resultado.getString("genero"), resultado.getDouble("precio"), resultado.getInt("unidades_disponibles"), resultado.getInt("nivel_reposicion"), resultado.getInt("suspendido"));
                }
            }
        }
        throw new VideojuegoNoEncontradoExcepcion("no existe un videojuego con el id indicado");
    }

    private static void actualizarStock(Connection conexion, Venta venta) throws SQLException {
        String sql = "UPDATE videojuegos SET unidades_disponibles = unidades_disponibles - ? WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, venta.getCantidad());
            sentencia.setInt(2, venta.getVideojuegoId());
            sentencia.executeUpdate();
        }
    }

    private static void guardarVenta(Connection conexion, Venta venta, double descuento, double total) throws SQLException {
        String sql = "INSERT INTO ventas (fecha, cantidad, videojuego_id, descuento, total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setDate(1, Date.valueOf(venta.getFecha()));
            sentencia.setInt(2, venta.getCantidad());
            sentencia.setInt(3, venta.getVideojuegoId());
            sentencia.setDouble(4, descuento);
            sentencia.setDouble(5, total);
            sentencia.executeUpdate();
        }
    }

    private static List<Venta> consultar(String sql) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion(); Statement sentencia = conexion.createStatement(); ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) ventas.add(convertir(resultado));
        }
        return ventas;
    }

    private static Venta convertir(ResultSet resultado) throws SQLException {
        Venta venta = new Venta(resultado.getInt("id"), resultado.getDate("fecha").toLocalDate(), resultado.getInt("cantidad"), resultado.getInt("videojuego_id"), resultado.getDouble("descuento"), resultado.getDouble("total"));
        venta.setVideojuegoNombre(resultado.getString("videojuego_nombre"));
        return venta;
    }
}
