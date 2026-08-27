import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            VideojuegoDAO.crearTabla();
            VentaDAO.crearTabla();

            Scanner scanner = new Scanner(System.in);
            VistaGeneral vista = new VistaGeneral(scanner);
            VideojuegoControlador videojuegos = new VideojuegoControlador(scanner);
            VentaControlador ventas = new VentaControlador(scanner);

            boolean salir = false;
            while (!salir) {
                switch (vista.mostrarMenuPrincipal()) {
                    case 1 -> videojuegos.ejecutar();
                    case 2 -> ventas.ejecutar();
                    case 0 -> salir = true;
                    default -> System.out.println("opción inválida");
                }
            }
            scanner.close();
        } catch (SQLException e) {
            System.out.println("error de base de datos: " + e.getMessage());
        }
    }
}
