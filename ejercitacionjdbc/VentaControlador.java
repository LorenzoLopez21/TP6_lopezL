import java.sql.SQLException;
import java.util.Scanner;

public class VentaControlador {
    private VentaVista vista;

    public VentaControlador(Scanner scanner) { 
        vista = new VentaVista(scanner); }
    public void ejecutar() { boolean volver=false; while(!volver) { 
        try { switch(vista.mostrarMenuVentas()) { 
            case 1 -> vista.mostrarVentas(VentaDAO.listarVentas()); 
            case 2 -> vista.mostrarVentas(java.util.List.of(VentaDAO.buscarPorId(vista.leerEntero("ID: ")))); 
            case 3 -> { Venta venta=vista.pedirVenta(); 
                VentaDAO.registrarVenta(venta); 
                vista.mensaje(String.format("venta registrada, descuento: %.0f%% __ Total: $%.2f",venta.getDescuento()*100,venta.getTotal())); } 
            case 4 -> vista.mostrarVentas(VentaDAO.buscarVentasPorVideojuego(vista.leerEntero("id del videojuego: "))); 
            case 5 -> vista.mostrarVentas(VentaDAO.listarVentasDelMesActual()); 
            case 0 -> volver=true; default -> vista.mensaje("opción inválida"); } } 
            catch (Exception e) { vista.mensaje("error: " + e.getMessage()); } } }
}
