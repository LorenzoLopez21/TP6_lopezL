import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class VentaVista {
    private Scanner scanner;

    public VentaVista(Scanner scanner) { this.scanner = scanner; }

    public int mostrarMenuVentas() {
        System.out.println("\n=== GESTIÓN DE VENTAS ===");
        System.out.println("1. Listar ventas");
        System.out.println("2. Buscar venta por ID");
        System.out.println("3. Registrar venta");
        System.out.println("4. Buscar ventas de un videojuego");
        System.out.println("5. Reporte de ventas del mes actual");
        System.out.println("0. Volver al menú principal");
        return leerEntero("Seleccione una opción: ");
    }

    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(scanner.nextLine());
    }

    public LocalDate leerFecha() {
        System.out.print("Fecha (AAAA-MM-DD): ");
        return LocalDate.parse(scanner.nextLine());
    }

    public Venta pedirVenta() {
        LocalDate fecha = leerFecha();
        int cantidad = leerEntero("Cantidad: ");
        int videojuegoId = leerEntero("ID del videojuego: ");
        return new Venta(fecha, cantidad, videojuegoId);
    }

    public void mostrarVentas(List<Venta> ventas) {
        for (Venta venta : ventas) {
            String nombre = venta.getVideojuegoNombre();
            if (nombre == null) nombre = String.valueOf(venta.getVideojuegoId());
            System.out.printf("ID: %d __ fecha: %s __ videojuego: %s __ cantidad: %d __ descuento: %.0f%% __ total: $%.2f%n",
                    venta.getId(), venta.getFecha(), nombre, venta.getCantidad(),
                    venta.getDescuento() * 100, venta.getTotal());
        }
    }

    public void mensaje(String mensaje) { System.out.println(mensaje); }
}
