import java.util.Scanner;

public class VistaGeneral {
    private Scanner scanner;

    public VistaGeneral(Scanner scanner) { 
        this.scanner = scanner; }
    public int mostrarMenuPrincipal() { 
        System.out.println("\n=== MENÚ PRINCIPAL ===\n1. Gestión de Videojuegos\n2. Gestión de Ventas\n0. Salir"); return leerEntero("Seleccione una opción: "); }
    public int mostrarMenuVideojuegos() { 
        System.out.println("\n=== GESTIÓN DE VIDEOJUEGOS ===\n1. Listar videojuegos\n2. Buscar videojuego por ID\n3. Agregar videojuego\n4. Actualizar videojuego\n5. Eliminar videojuego\n6. Videojuegos que necesitan reposición\n7. Videojuegos disponibles para la venta\n0. Volver al menú principal"); return leerEntero("Seleccione una opción: "); }
    public int mostrarMenuVentas() { 
        System.out.println("\n=== GESTIÓN DE VENTAS ===\n1. Listar ventas\n2. Buscar venta por ID\n3. Registrar venta\n4. Buscar ventas de un videojuego\n5. Reporte de ventas del mes actual\n0. Volver al menú principal"); return leerEntero("Seleccione una opción: "); }
    private int leerEntero(String mensaje) { 
        System.out.print(mensaje); return Integer.parseInt(scanner.nextLine()); }
    public Scanner getScanner() { 
        return scanner; }
}
