import java.util.List;
import java.util.Scanner;

public class VideojuegoVista {
    private Scanner scanner;

    public VideojuegoVista(Scanner scanner) { this.scanner = scanner; }

    public int mostrarMenuVideojuegos() {
        System.out.println("\n=== GESTIÓN DE VIDEOJUEGOS ===");
        System.out.println("1. Listar videojuegos");
        System.out.println("2. Buscar videojuego por ID");
        System.out.println("3. Agregar videojuego");
        System.out.println("4. Actualizar videojuego");
        System.out.println("5. Eliminar videojuego");
        System.out.println("6. Videojuegos que necesitan reposición");
        System.out.println("7. Videojuegos disponibles para la venta");
        System.out.println("0. Volver al menú principal");
        return leerEntero("Seleccione una opción: ");
    }

    public int leerEntero(String mensaje) { System.out.print(mensaje); return Integer.parseInt(scanner.nextLine()); }
    public double leerDouble(String mensaje) { System.out.print(mensaje); return Double.parseDouble(scanner.nextLine()); }
    public String leerTexto(String mensaje) { System.out.print(mensaje); return scanner.nextLine(); }

    public Videojuego pedirVideojuego(boolean conId) {
        int id = 0;
        if (conId) id = leerEntero("id: ");
        String nombre = leerTexto("nombre: ");
        String genero = leerTexto("género: ");
        double precio = leerDouble("precio: ");
        int unidades = leerEntero("unidades disponibles: ");
        int reposicion = leerEntero("nivel de reposición: ");
        int suspendido = leerEntero("suspendido (1/0): ");
        return new Videojuego(id, nombre, genero, precio, unidades, reposicion, suspendido);
    }

    public void mostrarVideojuegos(List<Videojuego> videojuegos) {
        for (Videojuego videojuego : videojuegos) {
            System.out.printf("ID: %d __ %s __ %s __ $%.2f __ stock: %d __ reposición: %d __ disponible: %s%n",
                    videojuego.getId(), videojuego.getNombre(), videojuego.getGenero(),
                    videojuego.getPrecio(), videojuego.getUnidadesDisponibles(),
                    videojuego.getNivelReposicion(), videojuego.getSuspendido() == 1 ? "sí" : "no");
        }
    }

    public void mostrar(Videojuego videojuego) { 
        mostrarVideojuegos(List.of(videojuego)); }
    public void mensaje(String mensaje) { 
        System.out.println(mensaje); }
}
