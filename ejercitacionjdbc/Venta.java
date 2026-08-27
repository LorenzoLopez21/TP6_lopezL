import java.time.LocalDate;

public class Venta {
    private int id;
    private LocalDate fecha;
    private int cantidad;
    private int videojuegoId;
    private double descuento;
    private double total;
    private String videojuegoNombre;

    public Venta(LocalDate fecha, int cantidad, int videojuegoId) {
        this(0, fecha, cantidad, videojuegoId, 0, 0);
    }

    public Venta(int id, LocalDate fecha, int cantidad, int videojuegoId) {
        this(id, fecha, cantidad, videojuegoId, 0, 0);
    }

    public Venta(int id, LocalDate fecha, int cantidad, int videojuegoId,
            double descuento, double total) {
        this.id = id;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.videojuegoId = videojuegoId;
        this.descuento = descuento;
        this.total = total;
    }

    public int getId() { 
        return id; }
    public void setId(int id) { 
        this.id = id; }
    public LocalDate getFecha() { 
        return fecha; }
    public void setFecha(LocalDate fecha) { 
        this.fecha = fecha; }
    public int getCantidad() { 
        return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; }
    public int getVideojuegoId() { 
        return videojuegoId; }
    public void setVideojuegoId(int videojuegoId) { 
        this.videojuegoId = videojuegoId; }
    public double getDescuento() { 
        return descuento; }
    public void setDescuento(double descuento) { 
        this.descuento = descuento; }
    public double getTotal() { 
        return total; }
    public void setTotal(double total) { 
        this.total = total; }
    public String getVideojuegoNombre() { 
        return videojuegoNombre; }
    public void setVideojuegoNombre(String videojuegoNombre) { 
        this.videojuegoNombre = videojuegoNombre; }
}
