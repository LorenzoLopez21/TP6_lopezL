import java.sql.SQLException;
import java.util.Scanner;

public class VideojuegoControlador {
    private final VideojuegoVista vista;
    public VideojuegoControlador(Scanner scanner) { 
        vista = new VideojuegoVista(scanner); }
    public void ejecutar() { 
        boolean volver=false; 
        while(!volver) { 
            try { switch(vista.mostrarMenuVideojuegos()) {
                case 1 -> vista.mostrarVideojuegos(VideojuegoDAO.listarVideojuegos()); 
                case 2 -> vista.mostrar(VideojuegoDAO.buscarPorId(vista.leerEntero("id: "))); 
                case 3 -> { VideojuegoDAO.crearVideojuego(vista.pedirVideojuego(false)); vista.mensaje("videojuego agregado"); } 
                case 4 -> { VideojuegoDAO.actualizarVideojuego(vista.pedirVideojuego(true)); vista.mensaje("videojuego actualizado"); } 
                case 5 -> { VideojuegoDAO.eliminarVideojuego(vista.leerEntero("id: ")); vista.mensaje("videojuego eliminado."); } 
                case 6 -> vista.mostrarVideojuegos(VideojuegoDAO.listarNecesitanReposicion()); case 7 -> vista.mostrarVideojuegos(VideojuegoDAO.listarDisponibles()); 
                case 0 -> volver=true; default -> vista.mensaje("opción inválida."); } } 
                catch (Exception e) { vista.mensaje("error: " + e.getMessage()); } } }
}
